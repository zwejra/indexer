package cz.indexer.controllers.index;

import cz.indexer.managers.api.IndexManager;
import cz.indexer.managers.api.MemoryDeviceManager;
import cz.indexer.model.MemoryDevice;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NonIndexedMemoryDeviceController implements Initializable {

	private static String CREATE_INDEX_DIALOG_FXML = "/cz.indexer.fxml/CreateIndexDialog.fxml";

	private MemoryDevice selectedMemoryDevice;

	private MemoryDeviceManager memoryDeviceManager;
	private IndexManager indexManager;

	@FXML
	private AnchorPane mainAnchorPane;

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

	public void handleCreateIndexButton(ActionEvent actionEvent) {
		FXMLLoader loader = new FXMLLoader(getClass().getResource(CREATE_INDEX_DIALOG_FXML));
		Parent parent;

		try {
			parent = loader.load();
			CreateIndexDialogController createIndexDialogController = loader.getController();
			createIndexDialogController.setSelectedMemoryDevice(selectedMemoryDevice);

			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);

			stage.setOnHidden(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					memoryDeviceManager.refreshMemoryDevices();
					mainAnchorPane.getChildren().clear();
				}
			});

			stage.setTitle("Vytvorit index: " + selectedMemoryDevice.toString());
			stage.setScene(scene);
			stage.showAndWait();
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
			alert.showAndWait();
		}
	}
}
