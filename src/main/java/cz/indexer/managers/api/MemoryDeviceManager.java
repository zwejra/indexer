package cz.indexer.managers.api;

import cz.indexer.model.MemoryDevice;
import cz.indexer.model.exceptions.PathFromDifferentMemoryDeviceException;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.InputMismatchException;

public interface MemoryDeviceManager {

	ObservableList<MemoryDevice> getConnectedMemoryDevices();

	ObservableList<MemoryDevice> getDisconnectedMemoryDevices();

	ObservableList<MemoryDevice> getIndexedMemoryDevices();

	void refreshMemoryDevices();

	void createMemoryDevice(MemoryDevice memoryDevice);

	void updateMemoryDevice(MemoryDevice memoryDevice);

	void deleteMemoryDevice(MemoryDevice memoryDevice);

	String trimMountFromPath(MemoryDevice memoryDevice, File file) throws PathFromDifferentMemoryDeviceException;

	boolean isUserDefinedNameValid(String memoryDeviceName) throws InputMismatchException;

	MemoryDevice refreshConnectedMemoryDevice(MemoryDevice fileOwner);
}
