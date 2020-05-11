package cz.indexer.dao.api;

import cz.indexer.model.MemoryDevice;

import java.util.HashMap;

public interface MemoryDeviceDAO {

	boolean	createMemoryDevice(MemoryDevice memoryDevice);

	boolean updateMemoryDevice(MemoryDevice memoryDevice);

	boolean deleteMemoryDevice(MemoryDevice memoryDevice);

	HashMap<String, MemoryDevice> getAllMemoryDevices();
}
