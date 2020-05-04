package cz.indexer.controllers.index;

import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import cz.indexer.managers.api.IndexManager;
import cz.indexer.managers.api.MemoryDeviceManager;
import cz.indexer.managers.impl.IndexManagerImpl;
import cz.indexer.managers.impl.MemoryDeviceManagerImpl;
import cz.indexer.model.MemoryDevice;
import cz.indexer.model.NonIndexedDirectory;
import cz.indexer.model.NonIndexedExtension;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateIndexDialogController implements Initializable {
	@FXML
	AnchorPane createIndexWindow;

	@FXML
	JFXTextField mediaNameTextField;
	@FXML
	JFXTextField extensionTextField;

	@FXML
	Button cancelButton;
	@FXML
	Button createIndexButton;

	@FXML
	JFXListView<IndexManagerImpl.MetadataForIndexing> indexedMetadataListView  = new JFXListView<>();
	@FXML
	JFXListView<NonIndexedDirectory> excludedDirectoriesListView  = new JFXListView<>();
	@FXML
	JFXListView<NonIndexedExtension> excludedExtensionsListView = new JFXListView<>();

	IndexManager indexManager = new IndexManagerImpl();
	MemoryDeviceManager memoryDeviceManager = new MemoryDeviceManagerImpl();

	DirectoryChooser directoryChooser = new DirectoryChooser();

	MemoryDevice selectedMemoryDevice;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		indexedMetadataListView.setItems(indexManager.getMetadataForIndexing());
		indexedMetadataListView.setCellFactory(CheckBoxListCell.forListView(IndexManagerImpl.MetadataForIndexing::onProperty));

		excludedDirectoriesListView.setItems(indexManager.getNonIndexedDirectories());
		excludedExtensionsListView.setItems(indexManager.getNonIndexedExtensions());
	}

	public void setSelectedMemoryDevice(MemoryDevice selectedMemoryDevice) {
		this.selectedMemoryDevice = selectedMemoryDevice;
	}

	@FXML
	public void handleCreateIndexActionButton(ActionEvent actionEvent) {
		try {
			indexManager.createIndex(selectedMemoryDevice, mediaNameTextField.getText());
			memoryDeviceManager.refreshMemoryDevices();

			Stage stage = (Stage) createIndexButton.getScene().getWindow();
			stage.close();
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	public void handleBrowseDirectoriesActionButton(ActionEvent actionEvent) {
		directoryChooser.setInitialDirectory(new File(selectedMemoryDevice.getMount()));
		File selectedDirectory = directoryChooser.showDialog(createIndexWindow.getScene().getWindow());

		try {
			indexManager.addNonIndexedDirectory(selectedDirectory, selectedMemoryDevice);
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	public void handleDeleteDirectoryActionButton(ActionEvent actionEvent) {
		NonIndexedDirectory directory = excludedDirectoriesListView.getSelectionModel().getSelectedItem();
		if (directory != null) {
			indexManager.removeNonIndexedDirectory(directory);
		}
	}

	@FXML
	public void handleAddExtensionActionButton(ActionEvent actionEvent) {
		try {
			indexManager.addNonIndexedExtension(extensionTextField.getText());
			extensionTextField.clear();
		} catch (IOException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
			alert.showAndWait();
		}
	}

	@FXML
	public void handleDeleteExtensionActionButton(ActionEvent actionEvent) {
		NonIndexedExtension extension = excludedExtensionsListView.getSelectionModel().getSelectedItem();
		if (extension != null) {
			indexManager.removeNonIndexedExtension(extension);
		}
	}

	@FXML
	public void handleCancelActionButton(ActionEvent actionEvent) {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}

}
