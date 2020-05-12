package cz.indexer.managers.api;

import cz.indexer.managers.impl.IndexManagerImpl;
import cz.indexer.model.MemoryDevice;
import cz.indexer.model.NonIndexedDirectory;
import cz.indexer.model.NonIndexedExtension;
import cz.indexer.model.exceptions.MemoryDeviceNotConnectedException;
import cz.indexer.model.exceptions.PathFromDifferentMemoryDeviceException;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.io.File;
import java.util.InputMismatchException;

public interface IndexManager {

	ObservableList<IndexManagerImpl.MetadataForIndexing> getMetadataForIndexing();

	ObservableList<NonIndexedExtension> getNonIndexedExtensions();

	ObservableList<NonIndexedDirectory> getNonIndexedDirectories();

	Task<Void> getCreateIndexTask(MemoryDevice memoryDevice) throws InputMismatchException;

	Task<Void> getUpdateIndexTask(MemoryDevice memoryDevice) throws InputMismatchException;

	boolean deleteIndex(MemoryDevice memoryDevice);

	void addNonIndexedDirectory(File file, MemoryDevice memoryDevice) throws PathFromDifferentMemoryDeviceException;

	void removeNonIndexedDirectory(NonIndexedDirectory nonIndexedDirectory);

	void addNonIndexedExtension(String extension) throws InputMismatchException;

	void removeNonIndexedExtension(NonIndexedExtension nonIndexedExtension);

}
