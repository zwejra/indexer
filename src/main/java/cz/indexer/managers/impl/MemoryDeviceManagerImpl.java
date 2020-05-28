package cz.indexer.managers.impl;

import cz.indexer.dao.api.MemoryDeviceDAO;
import cz.indexer.dao.impl.MemoryDeviceDAOImpl;
import cz.indexer.managers.api.MemoryDeviceManager;
import cz.indexer.model.MemoryDevice;
import cz.indexer.model.exceptions.PathFromDifferentMemoryDeviceException;
import cz.indexer.tools.I18N;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;
import oshi.software.os.OSFileStore;

import java.io.File;
import java.util.InputMismatchException;
import java.util.Map;

public class MemoryDeviceManagerImpl implements MemoryDeviceManager {

	final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	ObservableList<MemoryDevice> connectedMemoryDevices = FXCollections.observableArrayList();
	ObservableList<MemoryDevice> disconnectedMemoryDevices = FXCollections.observableArrayList();

	ObservableList<MemoryDevice> indexedMemoryDevices = FXCollections.observableArrayList();

	MemoryDeviceDAO memoryDeviceDAO = MemoryDeviceDAOImpl.getInstance();

	private static MemoryDeviceManagerImpl instance = null;

	public static MemoryDeviceManagerImpl getInstance() {
		if (instance == null)
			instance = new MemoryDeviceManagerImpl();
		return instance;
	}

	private MemoryDeviceManagerImpl() {}

	@Override
	public ObservableList<MemoryDevice> getConnectedMemoryDevices() {
		return connectedMemoryDevices;
	}

	@Override
	public ObservableList<MemoryDevice> getDisconnectedMemoryDevices() {
		return disconnectedMemoryDevices;
	}

	@Override
	public ObservableList<MemoryDevice> getIndexedMemoryDevices() {
		return indexedMemoryDevices;
	}

	@Override
	public void refreshMemoryDevices() {
		// Get indexed memory devices stored in database
		Map<String, MemoryDevice> storedMemoryDevices = memoryDeviceDAO.getAllMemoryDevices();
		// Clear memory devices
		connectedMemoryDevices.clear();
		disconnectedMemoryDevices.clear();
		indexedMemoryDevices.clear();

		// Get memory devices connected to computer
		SystemInfo si = new SystemInfo();
		OSFileStore[] fileStores = si.getOperatingSystem().getFileSystem().getFileStores();

		for (OSFileStore fileStore: fileStores) {
			MemoryDevice memoryDevice;
			if (storedMemoryDevices.containsKey(fileStore.getUUID())) {
				memoryDevice = storedMemoryDevices.get(fileStore.getUUID());
				memoryDevice.setLabel(fileStore.getLabel());
				memoryDevice.setMount(fileStore.getMount());
				memoryDevice.setIndexed(true);
				memoryDevice.setConnected(true);
				storedMemoryDevices.remove(fileStore.getUUID());
				indexedMemoryDevices.add(memoryDevice);
			} else {
				memoryDevice = new MemoryDevice(fileStore.getUUID(), fileStore.getLabel(), fileStore.getMount(),false, true);
			}
			connectedMemoryDevices.add(memoryDevice);
		}

		// Put rest of the indexed devices to disconnected devices
		for (Map.Entry<String, MemoryDevice> entry : storedMemoryDevices.entrySet()) {
			MemoryDevice memoryDevice = entry.getValue();
			memoryDevice.setConnected(false);
			memoryDevice.setIndexed(true);
			disconnectedMemoryDevices.add(memoryDevice);
			indexedMemoryDevices.add(memoryDevice);
		}
	}

	@Override
	public void createMemoryDevice(MemoryDevice memoryDevice) {
		logger.info(I18N.getMessage("info.memory.device.creation.started", memoryDevice));

		memoryDeviceDAO.createMemoryDevice(memoryDevice);

		logger.info(I18N.getMessage("info.memory.device.creation.finished", memoryDevice));
	}

	@Override
	public void updateMemoryDevice(MemoryDevice memoryDevice) {
		logger.info(I18N.getMessage("info.memory.device.update.started", memoryDevice));

		memoryDeviceDAO.updateMemoryDevice(memoryDevice);

		logger.info(I18N.getMessage("info.memory.device.update.finished", memoryDevice));
	}

	@Override
	public void deleteMemoryDevice(MemoryDevice memoryDevice) {
		logger.info(I18N.getMessage("info.memory.device.deleting.started", memoryDevice));

		memoryDeviceDAO.deleteMemoryDevice(memoryDevice);

		logger.info(I18N.getMessage("info.memory.device.deleting.finished", memoryDevice));
	}

	@Override
	public String trimMountFromPath(MemoryDevice memoryDevice, File file) throws PathFromDifferentMemoryDeviceException {
		if (!file.getAbsolutePath().contains(memoryDevice.getMount())) {
			throw new PathFromDifferentMemoryDeviceException(I18N.getMessage("exception.path.not.on.memory.device", file.getAbsolutePath(), memoryDevice));
		}

		return StringUtils.substringAfter(file.getAbsolutePath(), memoryDevice.getMount());
	}

	@Override
	public boolean isUserDefinedNameValid(String memoryDeviceName) throws InputMismatchException {
		if (StringUtils.isBlank(memoryDeviceName)) {
			throw new InputMismatchException(I18N.getMessage("exception.memory.device.name.empty"));
		}
		return true;
	}

	@Override
	public MemoryDevice refreshConnectedMemoryDevice(MemoryDevice fileOwner) {
		for (MemoryDevice memoryDevice: connectedMemoryDevices) {
			if (memoryDevice.getUuid().equals(fileOwner.getUuid())) return memoryDevice;
		}
		return fileOwner;
	}
}
