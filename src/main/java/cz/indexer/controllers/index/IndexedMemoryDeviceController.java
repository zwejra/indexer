package cz.indexer.controllers.index;

import cz.indexer.managers.api.IndexManager;
import cz.indexer.managers.api.MemoryDeviceManager;
import cz.indexer.managers.impl.IndexManagerImpl;
import cz.indexer.managers.impl.MemoryDeviceManagerImpl;
import cz.indexer.model.MemoryDevice;
import cz.indexer.model.exceptions.MemoryDeviceNotConnectedException;
import cz.indexer.tools.I18N;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

public class IndexedMemoryDeviceController implements Initializable {

	@FXML
	private Button updateButton;
	@FXML
	private Button deleteButton;

	@FXML
	private Label lastIndexUpdateValue;
	@FXML
	private Label lastIndexUpdateLabel;

	@FXML
	private AnchorPane mainAnchorPane;

	private IndexManager indexManager = IndexManagerImpl.getInstance();
	private MemoryDeviceManager memoryDeviceManager = MemoryDeviceManagerImpl.getInstance();

	private MemoryDevice selectedMemoryDevice;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		updateButton.textProperty().bind(I18N.createStringBinding("button.updateIndex"));
		deleteButton.textProperty().bind(I18N.createStringBinding("button.deleteIndex"));
		lastIndexUpdateLabel.textProperty().bind(I18N.createStringBinding("label.lastIndexUpdate"));
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
		try {
			indexManager.updateIndex(selectedMemoryDevice);
		} catch (MemoryDeviceNotConnectedException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR, e.toString());
			alert.showAndWait();
		}
	}
}
