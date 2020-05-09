package cz.indexer.managers.api;

import cz.indexer.model.MemoryDevice;
import javafx.collections.ObservableList;

import java.io.File;
import java.io.IOException;

public interface MemoryDeviceManager {

	ObservableList<MemoryDevice> getConnectedMemoryDevices();

	ObservableList<MemoryDevice> getDisconnectedMemoryDevices();

	ObservableList<MemoryDevice> getIndexedMemoryDevices();

	void refreshMemoryDevices();

	void createMemoryDevice(MemoryDevice memoryDevice);

	void deleteMemoryDevice(MemoryDevice memoryDevice);

	String trimMountFromPath(MemoryDevice memoryDevice, File file) throws IOException;

	boolean isUserDefinedNameValid(String memoryDeviceName) throws IOException;

	MemoryDevice refreshConnectedMemoryDevice(MemoryDevice fileOwner);
}
