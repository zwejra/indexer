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

/**
 * Manages index operations.
 */
public interface IndexManager {

	/**
	 * Obtains observable list of optional metadata index will index.
	 * Used for index creation - create index window.
	 * @return observable list of metadata for indexing
	 */
	ObservableList<IndexManagerImpl.MetadataForIndexing> getMetadataForIndexing();

	/**
	 * Obtains observable list of non indexed file extensions index will ignore.
	 * Used for index creation - create index window.
	 * @return observable list of non indexed extensions
	 */
	ObservableList<NonIndexedExtension> getNonIndexedExtensions();

	/**
	 * Obtains observable list of non indexed directories index will ignore.
	 * Used for index creation - create index window.
	 * @return observable list of non indexed directories
	 */
	ObservableList<NonIndexedDirectory> getNonIndexedDirectories();

	/**
	 * Obtains task for index creation.
	 * Running of this task will create index for specified memory device.
	 * @param memoryDevice index will be created for this memory device
	 * @return void
	 */
	Task<Void> getCreateIndexTask(MemoryDevice memoryDevice);

	/**
	 * Obtains task for index update.
	 * Running of this task will update index for specified memory device.
	 * @param memoryDevice index will be updated for this memory device
	 * @return void
	 */
	Task<Void> getUpdateIndexTask(MemoryDevice memoryDevice);

	/**
	 * Removes index of selected memory device - all files and whole memory device.
	 * @param memoryDevice index and all indexed files will be deleted for this memory device
	 * @return true if success, false otherwise
	 */
	boolean deleteIndex(MemoryDevice memoryDevice);

	/**
	 * Adds directory to the observable list of non indexed directories.
	 * @param file File class representing directory in the memory device.
	 * @param memoryDevice memory device to check whether the directory is located in the memory device
	 * @throws PathFromDifferentMemoryDeviceException thrown if path to file is not located in the memory device
	 */
	void addNonIndexedDirectory(File file, MemoryDevice memoryDevice) throws PathFromDifferentMemoryDeviceException;

	/**
	 * Removes non indexed directory from the observable list of non indexed directories.
	 * @param nonIndexedDirectory selected directory to be removed from the list
	 */
	void removeNonIndexedDirectory(NonIndexedDirectory nonIndexedDirectory);

	/**
	 * Adds extension to the observable list of non indexed extensions.
	 * @param extension file extension to be non indexed
	 * @throws InputMismatchException thrown with appropriate message if extension value is not valid
	 */
	void addNonIndexedExtension(String extension) throws InputMismatchException;

	/**
	 * Removes non indexed extension from the observable list of non indexed extensions.
	 * @param nonIndexedExtension selected extension to be removed from the list
	 */
	void removeNonIndexedExtension(NonIndexedExtension nonIndexedExtension);

}
