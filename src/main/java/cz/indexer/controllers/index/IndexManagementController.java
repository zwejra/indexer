package cz.indexer.controllers.index;

import com.jfoenix.controls.JFXListView;
import cz.indexer.managers.api.IndexManager;
import cz.indexer.managers.api.MemoryDeviceManager;
import cz.indexer.managers.impl.IndexManagerImpl;
import cz.indexer.managers.impl.MemoryDeviceManagerImpl;
import cz.indexer.model.MemoryDevice;
import cz.indexer.tools.I18N;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class IndexManagementController implements Initializable {

	private static final String NON_INDEXED_MEMORY_DEVICE_FXML = "/cz.indexer.fxml/NonIndexedMemoryDevice.fxml";
	private static final String INDEXED_MEMORY_DEVICE_FXML = "/cz.indexer.fxml/IndexedMemoryDevice.fxml";

	@FXML
	private AnchorPane memoryDeviceInfoPane;

	@FXML
	private Label connectedDevicesLabel;
	@FXML
	private Label disconnectedDevicesLabel;

	@FXML
	private JFXListView<MemoryDevice> connectedMemoryDevicesListView = new JFXListView<>();

	@FXML
	private JFXListView<MemoryDevice> disconnectedMemoryDevicesListView = new JFXListView<>();

	private MemoryDeviceManager memoryDeviceManager = MemoryDeviceManagerImpl.getInstance();
	private IndexManager indexManager = IndexManagerImpl.getInstance();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		connectedDevicesLabel.textProperty().bind(I18N.createStringBinding("label.connectedDevices"));
		disconnectedDevicesLabel.textProperty().bind(I18N.createStringBinding("label.disconnectedDevices"));

		connectedMemoryDevicesListView.setItems(memoryDeviceManager.getConnectedMemoryDevices());
		disconnectedMemoryDevicesListView.setItems(memoryDeviceManager.getDisconnectedMemoryDevices());
		memoryDeviceManager.refreshMemoryDevices();

		connectedMemoryDevicesListView.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<MemoryDevice>() {
					@Override
					public void changed(ObservableValue<? extends MemoryDevice> observable, MemoryDevice oldValue, MemoryDevice newValue) {
						if (newValue != null) {
							disconnectedMemoryDevicesListView.getSelectionModel().clearSelection();
							setMemoryDeviceInfoPane(newValue);
						}
					}
				});

		disconnectedMemoryDevicesListView.getSelectionModel().selectedItemProperty().addListener(
				new ChangeListener<MemoryDevice>() {
					@Override
					public void changed(ObservableValue<? extends MemoryDevice> observable, MemoryDevice oldValue, MemoryDevice newValue) {
						if (newValue != null) {
							connectedMemoryDevicesListView.getSelectionModel().clearSelection();
							setMemoryDeviceInfoPane(newValue);
						}
					}
				});
	}

	private void setMemoryDeviceInfoPane(MemoryDevice memoryDevice) {
		FXMLLoader loader;
		try {
			if (memoryDevice != null) {
				if (memoryDevice.isIndexed()) {
					loader = new FXMLLoader(getClass().getResource(INDEXED_MEMORY_DEVICE_FXML), I18N.getBundle());
					memoryDeviceInfoPane.getChildren().setAll((Node) loader.load());
					IndexedMemoryDeviceController indexedMemoryDeviceController = loader.getController();
					indexedMemoryDeviceController.setSelectedMemoryDevice(memoryDevice);


				} else {
					loader = new FXMLLoader(getClass().getResource(NON_INDEXED_MEMORY_DEVICE_FXML), I18N.getBundle());
					memoryDeviceInfoPane.getChildren().setAll((Node) loader.load());
					NonIndexedMemoryDeviceController nonIndexedMemoryDeviceController = loader.getController();
					nonIndexedMemoryDeviceController.setSelectedMemoryDevice(memoryDevice);
				}
			}
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	public void handleRefreshButtonAction(ActionEvent actionEvent) {
		memoryDeviceManager.refreshMemoryDevices();
	}
}
