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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

public class IndexedMemoryDeviceController implements Initializable {

	@FXML
	public Button updateButton;
	@FXML
	public Button deleteButton;

	@FXML
	public Label lastIndexUpdateValue;
	@FXML
	public Label lastIndexUpdateLabel;

	@FXML
	private AnchorPane mainAnchorPane;

	private IndexManager indexManager = IndexManagerImpl.getInstance();
	private MemoryDeviceManager memoryDeviceManager = MemoryDeviceManagerImpl.getInstance();

	private MemoryDevice selectedMemoryDevice;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void setSelectedMemoryDevice(MemoryDevice memoryDevice) {
		this.selectedMemoryDevice = memoryDevice;

		if (!selectedMemoryDevice.isConnected()) {
			updateButton.setDisable(true);
		} else {
			updateButton.setDisable(false);
		}

		DateTimeFormatter format = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);
		String formatedLastUpdate = format.format(this.selectedMemoryDevice.getIndex().getLastModifiedTime());

		lastIndexUpdateValue.setText(formatedLastUpdate);
	}

	@FXML
	public void handleDeleteIndexActionButton(ActionEvent actionEvent) {
		indexManager.deleteIndex(selectedMemoryDevice);
		memoryDeviceManager.refreshMemoryDevices();
		mainAnchorPane.getChildren().clear();
	}

	@FXML
	public void handleUpdateIndexActionButton(ActionEvent actionEvent) {
		indexManager.updateIndex(selectedMemoryDevice);
	}
}
