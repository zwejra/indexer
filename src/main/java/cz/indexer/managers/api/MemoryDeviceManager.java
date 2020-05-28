package cz.indexer.managers.api;

import cz.indexer.model.MemoryDevice;
import cz.indexer.model.exceptions.PathFromDifferentMemoryDeviceException;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.InputMismatchException;

/**
 * Manages memory device operations.
 */
public interface MemoryDeviceManager {

	/**
	 * Obtains observable list of connected memory devices.
	 * Used for the list of connected devices in the Index Management tab.
	 * @return observable list of connected memory devices
	 */
	ObservableList<MemoryDevice> getConnectedMemoryDevices();

	/**
	 * Obtains observable list of disconnected memory devices.
	 * Used for the list of disconnected devices in the Index Management tab.
	 * @return observable list of disconnected memory devices
	 */
	ObservableList<MemoryDevice> getDisconnectedMemoryDevices();

	/**
	 * Obtains observable list of indexed memory devices.
	 * Used for the checklist of indexed devices in File Search tab - memory device filter.
	 * Used if all indexed memory devices are searched.
	 * @return observable list of indexed memory devices
	 */
	ObservableList<MemoryDevice> getIndexedMemoryDevices();

	/**
	 * Refreshes lists of connected/disconnected/indexed memory devices.
	 */
	void refreshMemoryDevices();

	/**
	 * Creates and persist memory device.
	 * Memory device has to include index
	 * @param memoryDevice memory device to be persisted
	 */
	void createMemoryDevice(MemoryDevice memoryDevice);

	/**
	 * Update memory device.
	 * @param memoryDevice memory device to be updated
	 */
	void updateMemoryDevice(MemoryDevice memoryDevice);

	/**
	 * Remove memory device.
	 * @param memoryDevice memory device to be removed
	 */
	void deleteMemoryDevice(MemoryDevice memoryDevice);

	/**
	 * Trims mount/root part of the file path and returns it.
	 * Based on the mount/root of the selected memory device.
	 * @param memoryDevice memory device, whose mount will be trimmed from the file path
	 * @param file file with path, which will be trimmed
	 * @return string with the trimmed path
	 * @throws PathFromDifferentMemoryDeviceException thrown if the file path is not located on the selected memory device
	 */
	String trimMountFromPath(MemoryDevice memoryDevice, File file) throws PathFromDifferentMemoryDeviceException;

	/**
	 * Checks if user defined name is valid and is not blank
	 * @param memoryDeviceName user defined name of memory device
	 * @return true if the name is valid, false otherwise
	 * @throws InputMismatchException thrown if the name is empty
	 */
	boolean isUserDefinedNameValid(String memoryDeviceName) throws InputMismatchException;

	/**
	 * Refreshes memory device, if the needed attributes are not loaded.
	 * @param fileOwner memory device, to which the file (where method is called) belongs to
	 * @return refreshed memory device
	 */
	MemoryDevice refreshConnectedMemoryDevice(MemoryDevice fileOwner);
}
