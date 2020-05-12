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
import cz.indexer.model.exceptions.PathFromDifferentMemoryDeviceException;
import cz.indexer.tools.I18N;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.layout.AnchorPane;
import javafx.stage.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.InputMismatchException;
import java.util.ResourceBundle;

public class CreateIndexDialogController implements Initializable {

	private static final String PROGRESS_DIALOG_FXML = "/cz.indexer.fxml/ProgressDialog.fxml";

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	@FXML
	private AnchorPane createIndexWindow;

	@FXML
	private Label memoryDeviceNameLabel;
	@FXML
	private Label indexedMetadataLabel;
	@FXML
	private Label excludeDirectoryLabel;
	@FXML
	private Label excludedDirectoriesLabel;
	@FXML
	private Label excludeFileExtensionLabel;
	@FXML
	private Label excludedFileExtensionsLabel;

	@FXML
	private Button browseDirectoriesButton;
	@FXML
	private Button removeDirectoryButton;
	@FXML
	private Button addFileExtensionButton;
	@FXML
	private Button removeExtensionButton;
	@FXML
	private Button cancelButton;
	@FXML
	private Button createIndexButton;

	@FXML
	private JFXTextField mediaNameTextField;
	@FXML
	private JFXTextField extensionTextField;

	@FXML
	private JFXListView<IndexManagerImpl.MetadataForIndexing> indexedMetadataListView  = new JFXListView<>();
	@FXML
	private JFXListView<NonIndexedDirectory> excludedDirectoriesListView  = new JFXListView<>();
	@FXML
	private JFXListView<NonIndexedExtension> excludedExtensionsListView = new JFXListView<>();

	private IndexManager indexManager = IndexManagerImpl.getInstance();
	private MemoryDeviceManager memoryDeviceManager = MemoryDeviceManagerImpl.getInstance();

	private DirectoryChooser directoryChooser = new DirectoryChooser();

	private MemoryDevice selectedMemoryDevice;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		memoryDeviceNameLabel.textProperty().bind(I18N.createStringBinding("label.create.memoryDeviceName"));
		indexedMetadataLabel.textProperty().bind(I18N.createStringBinding("label.create.indexedMetadata"));
		excludeDirectoryLabel.textProperty().bind(I18N.createStringBinding("label.create.excludeDirectory"));
		excludedDirectoriesLabel.textProperty().bind(I18N.createStringBinding("label.create.excludedDirectories"));
		excludeFileExtensionLabel.textProperty().bind(I18N.createStringBinding("label.create.excludeFileExtension"));
		excludedFileExtensionsLabel.textProperty().bind(I18N.createStringBinding("label.create.excludedFileExtensions"));
		browseDirectoriesButton.textProperty().bind(I18N.createStringBinding("button.create.browseDirectories"));
		removeDirectoryButton.textProperty().bind(I18N.createStringBinding("button.create.removeDirectory"));
		addFileExtensionButton.textProperty().bind(I18N.createStringBinding("button.create.addFileExtension"));
		removeExtensionButton.textProperty().bind(I18N.createStringBinding("button.create.removeExtension"));
		cancelButton.textProperty().bind(I18N.createStringBinding("button.cancel"));
		createIndexButton.textProperty().bind(I18N.createStringBinding("button.create.createIndex"));

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
			// Check and set name
			String userDefinedName = mediaNameTextField.getText();
			memoryDeviceManager.isUserDefinedNameValid(userDefinedName);
			selectedMemoryDevice.setUserDefinedName(userDefinedName);
		} catch (InputMismatchException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
			alert.showAndWait();
			return;
		}

		Task<Void> task = indexManager.getCreateIndexTask(selectedMemoryDevice);

		FXMLLoader loader = new FXMLLoader(getClass().getResource(PROGRESS_DIALOG_FXML), I18N.getBundle());
		Parent parent;
		try {
			parent = loader.load();
			ProgressDialogController progressDialogController = loader.getController();
			progressDialogController.setSelectedMemoryDevice(selectedMemoryDevice);
			progressDialogController.setTask(task);

			Scene scene = new Scene(parent);
			Stage stage = new Stage();
			stage.setResizable(false);
			stage.initStyle(StageStyle.UTILITY);
			stage.initModality(Modality.APPLICATION_MODAL);

			stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
				@Override
				public void handle(WindowEvent event) {
					task.cancel();
					stage.close();
				}
			});

			progressDialogController.getProgressLabel().textProperty().bind(I18N.createStringBinding("label.progress.bar.create.index"));
			progressDialogController.getCancelButton().textProperty().bind(I18N.createStringBinding("button.cancel"));
			progressDialogController.getProgressBar().progressProperty().bind(task.progressProperty());

			task.setOnSucceeded(event -> {
				logger.info(I18N.get("info.task.success"));
				stage.close();
				closeStage();
			});

			task.setOnCancelled(event -> {
				logger.info(I18N.get("info.task.cancelled"));
			});

			Thread thread = new Thread(task);
			thread.start();

			stage.titleProperty().bind(I18N.createStringBinding("window.creating.index.title"));
			stage.setScene(scene);
			stage.showAndWait();

			if (task.isCancelled()) {
				indexManager.deleteIndex(selectedMemoryDevice);
				memoryDeviceManager.refreshMemoryDevices();
			}
			
			indexManager.getNonIndexedDirectories().clear();
			indexManager.getNonIndexedExtensions().clear();
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
		}
	}

	@FXML
	public void handleBrowseDirectoriesActionButton(ActionEvent actionEvent) {
		directoryChooser.setInitialDirectory(new File(selectedMemoryDevice.getMount()));
		File selectedDirectory = directoryChooser.showDialog(createIndexWindow.getScene().getWindow());

		try {
			indexManager.addNonIndexedDirectory(selectedDirectory, selectedMemoryDevice);
		} catch (PathFromDifferentMemoryDeviceException e) {
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
		} catch (InputMismatchException e) {
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
		closeStage();
	}

	private void closeStage() {
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}
}
