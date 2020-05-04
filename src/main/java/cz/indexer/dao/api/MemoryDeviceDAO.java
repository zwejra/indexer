package cz.indexer.dao.api;

import cz.indexer.model.MemoryDevice;

import java.util.HashMap;

public interface MemoryDeviceDAO {

	HashMap<String, MemoryDevice> getAllMemoryDevices();

	MemoryDevice getMemoryDevice(String uuid);

	boolean	createMemoryDevice(MemoryDevice memoryDevice);

	boolean deleteMemoryDevice(MemoryDevice memoryDevice);
}
