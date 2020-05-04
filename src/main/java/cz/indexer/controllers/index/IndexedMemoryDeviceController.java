package cz.indexer.controllers.index;

import cz.indexer.managers.api.IndexManager;
import cz.indexer.managers.api.MemoryDeviceManager;
import cz.indexer.managers.impl.IndexManagerImpl;
import cz.indexer.managers.impl.MemoryDeviceManagerImpl;
import cz.indexer.model.MemoryDevice;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IndexedMemoryDeviceController implements Initializable {

	@FXML
	private AnchorPane mainAnchorPane;

	private MemoryDevice selectedMemoryDevice;

	IndexManager indexManager;
	MemoryDeviceManager memoryDeviceManager;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void setMemoryDeviceManager(MemoryDeviceManager memoryDeviceManager) {
		this.memoryDeviceManager = memoryDeviceManager;
	}

	public void setIndexManager(IndexManager indexManager) {
		this.indexManager = indexManager;
	}

	public void setSelectedMemoryDevice(MemoryDevice memoryDevice) {
		this.selectedMemoryDevice = memoryDevice;
	}

	public void handleDeleteIndexActionButton(ActionEvent actionEvent) {
		indexManager.deleteIndex(selectedMemoryDevice);
		memoryDeviceManager.refreshMemoryDevices();
		mainAnchorPane.getChildren().clear();
	}

	public void handleUpdateIndexActionButton(ActionEvent actionEvent) {
		indexManager.updateIndex(selectedMemoryDevice);
	}
}
