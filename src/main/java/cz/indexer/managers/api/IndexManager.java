package cz.indexer.managers.api;

import cz.indexer.managers.impl.IndexManagerImpl;
import cz.indexer.model.MemoryDevice;
import cz.indexer.model.NonIndexedDirectory;
import cz.indexer.model.NonIndexedExtension;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;

public interface IndexManager {

	public ObservableList<IndexManagerImpl.MetadataForIndexing> getMetadataForIndexing();

	public ObservableList<NonIndexedExtension> getNonIndexedExtensions();

	public ObservableList<NonIndexedDirectory> getNonIndexedDirectories();

	boolean createIndex(MemoryDevice memoryDevice, String memoryDeviceName) throws IOException;

	boolean updateIndex(MemoryDevice memoryDevice);

	boolean deleteIndex(MemoryDevice memoryDevice);

	void addNonIndexedDirectory(File file, MemoryDevice memoryDevice) throws IOException;

	void removeNonIndexedDirectory(NonIndexedDirectory nonIndexedDirectory);

	void addNonIndexedExtension(String extension) throws IOException;

	void removeNonIndexedExtension(NonIndexedExtension nonIndexedExtension);

}
