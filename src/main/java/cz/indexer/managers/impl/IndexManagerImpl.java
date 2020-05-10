package cz.indexer.managers.impl;

import cz.indexer.dao.api.IndexedFileDAO;
import cz.indexer.dao.api.MetadataDAO;
import cz.indexer.dao.impl.IndexedFileDAOImpl;
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
import javafx.scene.control.Alert;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Stream;

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
		memoryDeviceManager.refreshMemoryDevices();
		if (!memoryDevice.isConnected()) {
			Alert alert = new Alert(Alert.AlertType.ERROR, "Zarizeni neni pripojeno, nelze aktualizovat index.");
			alert.showAndWait();
			return false;
		}

		Index indexToUpdate = memoryDevice.getIndex();

		Set<String> indexedMetadataSet = getIndexedMetadataSet(memoryDevice.getIndex());
		Set<String> disabledExtensionsSet = getDisabledExtensionsSet(memoryDevice.getIndex());
		Set<String> disabledDirectoriesSet = getDisabledDirectoriesSet(memoryDevice.getIndex());

		// Walk through file tree
		Path start = new File(memoryDevice.getMount()).toPath();
		try {
			Files.walkFileTree(start,
					new SimpleFileVisitor<Path>() {
						@Override
						public FileVisitResult visitFile(Path file,
														 BasicFileAttributes attrs) throws IOException {
							return FileVisitResult.CONTINUE;
						}

						@Override
						public FileVisitResult visitFileFailed(Path file, IOException e)
								throws IOException {
							return FileVisitResult.SKIP_SUBTREE;
						}

						@Override
						public FileVisitResult preVisitDirectory(Path dir,
																 BasicFileAttributes attrs) throws IOException {
							if (!isDirectoryIndexed(dir, memoryDevice, disabledDirectoriesSet)) {
								logger.info("Skipped because directory {} is not indexed.", dir);
								return FileVisitResult.SKIP_SUBTREE;
							}

							return FileVisitResult.CONTINUE;
						}
						
						@Override
						public FileVisitResult postVisitDirectory(Path dir,
																  IOException e) throws IOException {
							logger.info("Post visit directory: " + dir.toString());

							try {
								BasicFileAttributes attrs = Files.readAttributes(dir, BasicFileAttributes.class);

								OffsetDateTime odt = OffsetDateTime.now(ZoneId.systemDefault());
								ZoneOffset zoneOffset = odt.getOffset();
								
								if (attrs.lastModifiedTime().toInstant().isBefore(indexToUpdate.getLastModifiedTime().toInstant(zoneOffset))) {
									logger.info("Nezmeneno. Pokracuj dal. " + dir.toString());
									return FileVisitResult.CONTINUE;
								}

								String trimmedPath = memoryDeviceManager.trimMountFromPath(memoryDevice, dir.toFile());
								HashMap<String, IndexedFile> indexedFilesInDirectory;
								if (StringUtils.isBlank(trimmedPath)) {
									indexedFilesInDirectory = indexedFileDAO.getFilesInDirectory(memoryDevice.getIndex(), trimmedPath, true);
								} else {
									indexedFilesInDirectory = indexedFileDAO.getFilesInDirectory(memoryDevice.getIndex(), trimmedPath, false);
								}

								List<IndexedFile> newFiles = new ArrayList<>();
								List<IndexedFile> filesToUpdate = new ArrayList<>();
								List<IndexedFile> filesToRemove = new ArrayList<>();

								try (Stream<Path> stream = Files.list(dir)) {
									stream.forEach(path -> {
										File file = path.toFile();

										if (!isFileExtensionIndexed(path, disabledExtensionsSet)) {
											logger.info("File skipped because extension is not indexed. " + file);
											return;
										}

										if (file.isDirectory()) {
											try {
												if (!isDirectoryIndexed(file.toPath(), memoryDevice, disabledDirectoriesSet)) {
													logger.info("Skipped because directory {} is not indexed.", dir);
													return;
												}
											} catch (IOException ioException) {
												logger.error("Problem s odriznutim root adresare z cesty: " + dir.toString());
												return;
											}
										}

										try {
											BasicFileAttributes fileAttrs = Files.readAttributes(path, BasicFileAttributes.class);

											if (indexedFilesInDirectory.containsKey(file.getName())) {
												IndexedFile indexedFile = indexedFilesInDirectory.get(file.getName());
												indexedFilesInDirectory.remove(file.getName());

												boolean changed = false;
												if (indexedFile.getLastModifiedTime() != null) {
													if (fileAttrs.lastModifiedTime().toInstant().isAfter(indexToUpdate.getLastModifiedTime().toInstant(zoneOffset))) {
														setFileAttributes(path, fileAttrs, indexedFile, memoryDevice, indexedMetadataSet);
														changed = true;
													}
												}

												if (!changed && indexedFile.getLastAccessTime() != null) {
													if (fileAttrs.lastAccessTime().toInstant().isAfter(indexToUpdate.getLastModifiedTime().toInstant(zoneOffset)))  {
														setFileAttributes(path, fileAttrs, indexedFile, memoryDevice, indexedMetadataSet);
														changed = true;
													}
												}

												if (changed) filesToUpdate.add(indexedFile);

											} else {
												IndexedFile newIndexedFile = new IndexedFile();
												setFileAttributes(path, fileAttrs, newIndexedFile, memoryDevice, indexedMetadataSet);
												newFiles.add(newIndexedFile);
											}
										} catch (IOException ioException) {
											logger.error("Cannot read basic attributes of file: " + path.toString());
										}
									});

								} catch (IOException ioException) {
									// ignore, no rights to read file
								}

								for(Map.Entry<String, IndexedFile> entry : indexedFilesInDirectory.entrySet()) {
									filesToRemove.add(entry.getValue());
								}

								if (!newFiles.isEmpty()) indexedFileDAO.createFiles(newFiles);
								if (!filesToUpdate.isEmpty()) indexedFileDAO.updateFiles(filesToUpdate);
								if (!filesToRemove.isEmpty()) indexedFileDAO.deleteFiles(filesToRemove);

							} catch (IOException ioException) {
								logger.error("Problem s odriznutim root adresare z cesty: " + dir.toString());
							}

							return FileVisitResult.CONTINUE;
						}
					});
		} catch (IOException e) {
			e.printStackTrace();
		}

		memoryDevice.getIndex().setLastModifiedTime(LocalDateTime.now());
		memoryDeviceManager.updateMemoryDevice(memoryDevice);

		return true;
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
		Set<String> indexedMetadataSet = getIndexedMetadataSet(memoryDevice.getIndex());
		Set<String> disabledExtensionsSet = getDisabledExtensionsSet(memoryDevice.getIndex());
		Set<String> disabledDirectoriesSet = getDisabledDirectoriesSet(memoryDevice.getIndex());

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

	private Set<String> getIndexedMetadataSet(Index index) {
		Set<String> indexedMetadataSet = new HashSet<>();

		for(Metadata metadata: index.getIndexedMetadata()) {
			indexedMetadataSet.add(metadata.getValue().toString());
		}

		return indexedMetadataSet;
	}

	private Set<String> getDisabledExtensionsSet(Index index) {
		Set<String> disabledExtensionsSet = new HashSet<>();

		for (NonIndexedExtension nonIndexedExtension: index.getNonIndexedExtensions()) {
			disabledExtensionsSet.add(nonIndexedExtension.getExtension());
		}

		return disabledExtensionsSet;
	}

	private Set<String> getDisabledDirectoriesSet(Index index) {
		Set<String> disabledDirectoriesSet = new HashSet<>();

		for (NonIndexedDirectory nonIndexedDirectory: index.getNonIndexedDirectories()) {
			disabledDirectoriesSet.add(nonIndexedDirectory.getPath());
		}

		return disabledDirectoriesSet;
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
