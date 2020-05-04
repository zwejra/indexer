package cz.indexer.managers.impl;

import cz.indexer.dao.api.IndexedFileDAO;
import cz.indexer.dao.api.MemoryDeviceDAO;
import cz.indexer.dao.api.MetadataDAO;
import cz.indexer.dao.impl.IndexedFileDAOImpl;
import cz.indexer.dao.impl.MemoryDeviceDAOImpl;
import cz.indexer.dao.impl.MetadataDAOImpl;
import cz.indexer.managers.api.IndexManager;
import cz.indexer.managers.api.MemoryDeviceManager;
import cz.indexer.model.*;
import cz.indexer.model.enums.FileType;
import cz.indexer.model.enums.OptionalMetadata;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

public class IndexManagerImpl implements IndexManager {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private ObservableList<MetadataForIndexing> metadataForIndexing = FXCollections.observableArrayList();
	private ObservableList<NonIndexedExtension> nonIndexedExtensions = FXCollections.observableArrayList();
	private ObservableList<NonIndexedDirectory> nonIndexedDirectories = FXCollections.observableArrayList();

	private MemoryDeviceManager memoryDeviceManager = MemoryDeviceManagerImpl.getInstance();

	private MetadataDAO metadataDAO = MetadataDAOImpl.getInstance();

	private IndexedFileDAO indexedFileDAO = IndexedFileDAOImpl.getInstance();

	private static IndexManagerImpl instance = null;

	public static IndexManagerImpl getInstance() {
		if (instance == null)
			instance = new IndexManagerImpl();
		return instance;
	}

	private IndexManagerImpl() {}

	@Override
	public ObservableList<MetadataForIndexing> getMetadataForIndexing() {
		metadataForIndexing.clear();
		List<Metadata> allMetadata = metadataDAO.getAllMetadata();
		for (Metadata metadata: allMetadata) {
			metadataForIndexing.add(new MetadataForIndexing(metadata, true));
		}
		return metadataForIndexing;
	}

	@Override
	public ObservableList<NonIndexedExtension> getNonIndexedExtensions() {
		return nonIndexedExtensions;
	}

	@Override
	public ObservableList<NonIndexedDirectory> getNonIndexedDirectories() {
		return nonIndexedDirectories;
	}

	@Override
	public boolean createIndex(MemoryDevice memoryDevice, String memoryDeviceName) throws IOException {
		// Check and set name
		memoryDeviceManager.isUserDefinedNameValid(memoryDeviceName);
		memoryDevice.setUserDefinedName(memoryDeviceName);

		// Create index
		Index newIndex = new Index();
		newIndex.setLastModifiedTime(LocalDateTime.now());

		for (MetadataForIndexing m: metadataForIndexing) {
			if (m.isOn()) {
				newIndex.getIndexedMetadata().add(m.getMetadata());
			}
		}
		newIndex.getNonIndexedDirectories().addAll(nonIndexedDirectories);
		newIndex.getNonIndexedExtensions().addAll(nonIndexedExtensions);

		// Set index in memory device and save it to the database
		memoryDevice.setIndex(newIndex);
		memoryDeviceManager.createMemoryDevice(memoryDevice);

		logger.info("Indexing STARTED for memory device " + memoryDevice);
		indexMemoryDevice(memoryDevice);
		logger.info("Indexing FINISHED for memory device " + memoryDevice);

		return true;
	}

	@Override
	public boolean updateIndex(MemoryDevice memoryDevice) {
		return false;
	}

	@Override
	public boolean deleteIndex(MemoryDevice memoryDevice) {
		logger.info("Deleting of index {} started.", memoryDevice.getIndex());

		indexedFileDAO.deleteFiles(memoryDevice.getIndex());
		memoryDeviceManager.deleteMemoryDevice(memoryDevice);

		logger.info("Deleting of index {} finished.", memoryDevice.getIndex());
		return true;
	}

	@Override
	public void addNonIndexedDirectory(File directory, MemoryDevice memoryDevice) throws IOException {
		if (directory != null) {
			String path = memoryDeviceManager.trimMountFromPath(memoryDevice, directory);
			if (!StringUtils.isBlank(path)) {
				NonIndexedDirectory nonIndexedDirectory = new NonIndexedDirectory(path);
				nonIndexedDirectories.add(nonIndexedDirectory);
			}
		}
	}

	@Override
	public void removeNonIndexedDirectory(NonIndexedDirectory nonIndexedDirectory) {
		nonIndexedDirectories.remove(nonIndexedDirectory);
	}

	@Override
	public void addNonIndexedExtension(String extension) throws IOException {
		if (!StringUtils.isBlank(extension)) {
			int dotCount = StringUtils.countMatches(extension, ".");
			if (dotCount > 1) {
				throw new IOException("Pripona obsahuje vice tecek.");
			} else if (dotCount == 1) {
				if (!extension.startsWith(".")) {
					throw new IOException("Pripona smi obsahovat tecku pouze na zacatku.");
				}
			} else {
				extension = "." + extension;
			}

			NonIndexedExtension nonIndexedExtension = new NonIndexedExtension(extension);
			nonIndexedExtensions.add(nonIndexedExtension);
		}
	}

	@Override
	public void removeNonIndexedExtension(NonIndexedExtension nonIndexedExtension) {
		nonIndexedExtensions.remove(nonIndexedExtension);
	}

	public static class MetadataForIndexing {
		private final Metadata metadata;
		private final BooleanProperty on = new SimpleBooleanProperty();

		public MetadataForIndexing(Metadata metadata, boolean on) {
			this.metadata = metadata;
			setOn(on);
		}

		public final Metadata getMetadata() {
			return this.metadata;
		}

		public final BooleanProperty onProperty() {
			return this.on;
		}

		public final boolean isOn() {
			return this.onProperty().get();
		}

		public final void setOn(boolean on) {
			this.onProperty().set(on);
		}

		@Override
		public String toString() {
			return metadata.getValue().toString();
		}
	}

	private void indexMemoryDevice(MemoryDevice memoryDevice) {
		// Create hashmap with metadata name and object
		Set<String> indexedMetadataSet = new HashSet<>();
		for(Metadata metadata: memoryDevice.getIndex().getIndexedMetadata()) {
			indexedMetadataSet.add(metadata.getValue().toString());
		}

		// Create set with non indexed extensions as strings
		Set<String> disabledExtensionsSet = new HashSet<>();
		for (NonIndexedExtension nonIndexedExtension: nonIndexedExtensions) {
			disabledExtensionsSet.add(nonIndexedExtension.getExtension());
		}

		// Create set with non indexed directories as strings
		Set<String> disabledDirectoriesSet = new HashSet<>();
		for (NonIndexedDirectory nonIndexedDirectory: nonIndexedDirectories) {
			disabledDirectoriesSet.add(nonIndexedDirectory.getPath());
		}

		List<IndexedFile> indexedFileBuffer = new ArrayList<>();
		// Walk through file tree
		Path start = new File(memoryDevice.getMount()).toPath();
		try {
			Files.walkFileTree(start,
					new SimpleFileVisitor<Path>() {
						@Override
						public FileVisitResult visitFile(Path file,
														 BasicFileAttributes attrs) throws IOException {

							if (!isFileExtensionIndexed(file, disabledExtensionsSet)) {
								logger.info("File skipped because extension is not indexed. " + file);
								return FileVisitResult.CONTINUE;
							}

							return createIndexedFile(file, attrs, memoryDevice, indexedMetadataSet, indexedFileBuffer);
						}

						@Override
						public FileVisitResult visitFileFailed(Path file, IOException e)
								throws IOException {
							logger.debug("Visiting failed for" + file.toString());
							return FileVisitResult.SKIP_SUBTREE;
						}

						@Override
						public FileVisitResult preVisitDirectory(Path dir,
																 BasicFileAttributes attrs) throws IOException {
							logger.debug("About to visit directory " + dir.toString());
							if (!isDirectoryIndexed(dir, memoryDevice, disabledDirectoriesSet)) {
								logger.info("Skipped because directory {} is not indexed.", dir);
								return FileVisitResult.SKIP_SUBTREE;
							}

							if (memoryDevice.getMount().equals(dir.toString())) return FileVisitResult.CONTINUE;

							return createIndexedFile(dir, attrs, memoryDevice, indexedMetadataSet, indexedFileBuffer);
						}
					});
		} catch (IOException e) {
			e.printStackTrace();
		}

		indexedFileDAO.createFiles(indexedFileBuffer);
		indexedFileBuffer.clear();
	}

	private FileVisitResult createIndexedFile(Path file, BasicFileAttributes attrs, MemoryDevice memoryDevice,
											  Set<String> indexedMetadataSet, List<IndexedFile> indexedFileBuffer) throws IOException {

		IndexedFile indexedFile = new IndexedFile();
		setFileAttributes(file, attrs, indexedFile, memoryDevice, indexedMetadataSet);

		indexedFileBuffer.add(indexedFile);
		if (indexedFileBuffer.size() > 5000) {
			indexedFileDAO.createFiles(indexedFileBuffer);
			indexedFileBuffer.clear();
		}

		return FileVisitResult.CONTINUE;
	}

	private void setFileAttributes(Path file, BasicFileAttributes attrs, IndexedFile indexedFile,
								   MemoryDevice memoryDevice, Set<String> indexedMetadataSet) throws IOException {

		indexedFile.setFileName(file.getFileName().toString());
		indexedFile.setPath(memoryDeviceManager.trimMountFromPath(memoryDevice, file.toFile()));
		indexedFile.setIndex(memoryDevice.getIndex());

		if (indexedMetadataSet.contains(OptionalMetadata.CREATION_TIME.toString())) {
			indexedFile.setCreationTime(LocalDateTime.ofInstant(attrs.creationTime().toInstant(), ZoneId.systemDefault()));
		}

		if (indexedMetadataSet.contains(OptionalMetadata.LAST_ACCESS_TIME.toString())) {
			indexedFile.setLastAccessTime(LocalDateTime.ofInstant(attrs.lastAccessTime().toInstant(), ZoneId.systemDefault()));
		}

		if (indexedMetadataSet.contains(OptionalMetadata.LAST_MODIFIED_TIME.toString())) {
			indexedFile.setLastModifiedTime(LocalDateTime.ofInstant(attrs.lastModifiedTime().toInstant(), ZoneId.systemDefault()));
		}

		if (indexedMetadataSet.contains(OptionalMetadata.SIZE.toString())) {
			indexedFile.setFileSize(attrs.size());
		}

		if (attrs.isDirectory()) {
			indexedFile.setType(FileType.DIRECTORY);
		} else if (attrs.isRegularFile()) {
			indexedFile.setType(FileType.FILE);
		} else if (attrs.isSymbolicLink()) {
			indexedFile.setType(FileType.SYMBOLIC_LINK);
		} else {
			indexedFile.setType(FileType.OTHER);
		}
	}

	private boolean isFileExtensionIndexed(Path file, Set<String> disabledExtensions) {
		String fileExtension = getFileExtension(file.getFileName());

		if (disabledExtensions.contains(fileExtension)) return false;
		return true;
	}

	private String getFileExtension(Path fileName) {
		String name = fileName.toString();
		int lastIndexOf = name.lastIndexOf(".");
		if (lastIndexOf == -1) {
			return "";
		}
		return name.substring(lastIndexOf);
	}

	private boolean isDirectoryIndexed(Path dir, MemoryDevice memoryDevice, Set<String> disabledDirectoriesSet) throws IOException {
		String trimmedPath = memoryDeviceManager.trimMountFromPath(memoryDevice, dir.toFile());

		if (disabledDirectoriesSet.contains(trimmedPath)) return false;
		return true;
	}
}
