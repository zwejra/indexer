package cz.indexer.controllers.index;

import cz.indexer.managers.api.IndexManager;
import cz.indexer.managers.api.MemoryDeviceManager;
import cz.indexer.managers.impl.IndexManagerImpl;
import cz.indexer.managers.impl.MemoryDeviceManagerImpl;
import cz.indexer.model.MemoryDevice;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ResourceBundle;

public class IndexedMemoryDeviceController implements Initializable {

	private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

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
		memoryDeviceManager.refreshMemoryDevices();
		if (!selectedMemoryDevice.isConnected()) {
			Alert alert = new Alert(Alert.AlertType.ERROR, I18N.getMessage("exception.device.not.connected.for.update", selectedMemoryDevice));
			alert.showAndWait();
			return;
		}

		Task<Void> task = indexManager.getUpdateIndexTask(selectedMemoryDevice);

		FXMLLoader loader = new FXMLLoader(getClass().getResource(ProgressDialogController.PROGRESS_DIALOG_FXML), I18N.getBundle());
		Parent parent;
		try {
			parent = loader.load();
			ProgressDialogController progressDialogController = loader.getController();
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

			progressDialogController.getProgressLabel().textProperty().bind(I18N.createStringBinding("label.progress.bar.update.index"));
			progressDialogController.getCancelButton().textProperty().bind(I18N.createStringBinding("button.cancel"));
			progressDialogController.getProgressBar().progressProperty().bind(task.progressProperty());

			task.setOnSucceeded(event -> {
				logger.info(I18N.get("info.task.success"));
				stage.close();
			});

			task.setOnCancelled(event -> {
				logger.info(I18N.get("info.task.cancelled"));
			});

			Thread thread = new Thread(task);
			thread.start();

			stage.titleProperty().bind(I18N.createStringBinding("window.updating.index.title"));
			stage.setScene(scene);
			stage.showAndWait();

			memoryDeviceManager.refreshMemoryDevices();
		} catch (IOException e) {
			logger.error(e.getLocalizedMessage());
		}

	}
}
