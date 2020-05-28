package cz.indexer.dao.api;

import cz.indexer.model.MemoryDevice;

import java.util.HashMap;

/**
 * DAO object for management of MemoryDevice entity
 */
public interface MemoryDeviceDAO {

	/**
	 * Persists memory device in the database.
	 * @param memoryDevice memory device to be persisted
	 */
	void createMemoryDevice(MemoryDevice memoryDevice);

	/**
	 * Updates existing memory device in the database.
	 * @param memoryDevice memory device to be updated
	 */
	void updateMemoryDevice(MemoryDevice memoryDevice);

	/**
	 * Removes existing memory device from the database.
	 * @param memoryDevice memory device to be removed
	 */
	void deleteMemoryDevice(MemoryDevice memoryDevice);

	/**
	 * Gets all memory devices stored in the database.
	 * All of these memory devices are indexed.
	 * @return hashmap, where key is the UUID of the memory device and value is the MemoryDevice object
	 */
	HashMap<String, MemoryDevice> getAllMemoryDevices();
}
