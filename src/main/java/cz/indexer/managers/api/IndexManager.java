package cz.indexer.managers.api;

import cz.indexer.managers.impl.IndexManagerImpl;
import cz.indexer.model.MemoryDevice;
import cz.indexer.model.NonIndexedDirectory;
import cz.indexer.model.NonIndexedExtension;
import cz.indexer.model.exceptions.MemoryDeviceNotConnectedException;
import cz.indexer.model.exceptions.PathFromDifferentMemoryDeviceException;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;
import java.util.InputMismatchException;

public interface IndexManager {

	ObservableList<IndexManagerImpl.MetadataForIndexing> getMetadataForIndexing();

	ObservableList<NonIndexedExtension> getNonIndexedExtensions();

	ObservableList<NonIndexedDirectory> getNonIndexedDirectories();

	boolean createIndex(MemoryDevice memoryDevice, String memoryDeviceName) throws InputMismatchException;

	boolean updateIndex(MemoryDevice memoryDevice) throws MemoryDeviceNotConnectedException;

	boolean deleteIndex(MemoryDevice memoryDevice);

	void addNonIndexedDirectory(File file, MemoryDevice memoryDevice) throws PathFromDifferentMemoryDeviceException;

	void removeNonIndexedDirectory(NonIndexedDirectory nonIndexedDirectory);

	void addNonIndexedExtension(String extension) throws InputMismatchException;

	void removeNonIndexedExtension(NonIndexedExtension nonIndexedExtension);

}
