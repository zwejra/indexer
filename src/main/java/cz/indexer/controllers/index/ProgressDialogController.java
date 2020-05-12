package cz.indexer.controllers.index;

import com.jfoenix.controls.JFXProgressBar;
import cz.indexer.managers.api.IndexManager;
import cz.indexer.managers.impl.IndexManagerImpl;
import cz.indexer.model.MemoryDevice;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import lombok.Getter;

import java.net.URL;
import java.util.ResourceBundle;

public class ProgressDialogController implements Initializable {

	public static final String PROGRESS_DIALOG_FXML = "/cz.indexer.fxml/ProgressDialog.fxml";

	@FXML
	@Getter private Label progressLabel;
	@FXML
	@Getter private JFXProgressBar progressBar;
	@FXML
	@Getter private Button cancelButton;

	IndexManager indexManager = IndexManagerImpl.getInstance();

	Task task;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	@FXML
	private void handleCancelButton(ActionEvent actionEvent) {
		task.cancel();
		Stage stage = (Stage) cancelButton.getScene().getWindow();
		stage.close();
	}

	public void setTask(Task task) {
		this.task=task;
	}
}
