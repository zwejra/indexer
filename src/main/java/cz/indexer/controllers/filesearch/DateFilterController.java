package cz.indexer.controllers.filesearch;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import cz.indexer.managers.api.IndexManager;
import cz.indexer.managers.api.MemoryDeviceManager;
import cz.indexer.managers.impl.IndexManagerImpl;
import cz.indexer.managers.impl.MemoryDeviceManagerImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DateFilterController implements Initializable {

	@FXML
	private Label creationDateLabel;
	@FXML
	private Label lastModifiedDateLabel;
	@FXML
	private Label lastAccessDateLabel;

	@FXML
	private JFXDatePicker creationDateDatePicker;
	@FXML
	private JFXTimePicker creationDateTimePicker;

	@FXML
	private JFXDatePicker lastModifiedDateDatePicker;
	@FXML
	private JFXTimePicker lastModifiedDateTimePicker;

	@FXML
	private JFXDatePicker lastAccessDateDatePicker;
	@FXML
	private JFXTimePicker lastAccessDateTimePicker;

	@FXML
	private JFXComboBox creationDateComboBox;
	@FXML
	private JFXComboBox lastModifiedDateComboBox;
	@FXML
	private JFXComboBox lastAccessDateComboBox;

	private FileSearchController fileSearchController;

	private MemoryDeviceManager memoryDeviceManager = MemoryDeviceManagerImpl.getInstance();
	private IndexManager indexManager = IndexManagerImpl.getInstance();

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void setFileSearchController(FileSearchController fileSearchController) {
		this.fileSearchController = fileSearchController;
	}

	@FXML
	public void handleCreationDateDatePicker(ActionEvent actionEvent) {

	}

	@FXML
	public void handleCreationDateTimePicker(ActionEvent actionEvent) {
	}

	@FXML
	public void handleLastModifiedDateDatePicker(ActionEvent actionEvent) {
	}

	@FXML
	public void handleLastModifiedDateTimePicker(ActionEvent actionEvent) {
	}

	@FXML
	public void handleLastAccessDateDatePicker(ActionEvent actionEvent) {
	}

	@FXML
	public void handleLastAccessDateTimePicker(ActionEvent actionEvent) {
	}

	@FXML
	public void handleCreationDateComboBox(ActionEvent actionEvent) {
	}

	@FXML
	public void handleLastModifiedDateComboBox(ActionEvent actionEvent) {
	}

	@FXML
	public void handleLastAccessDateComboBox(ActionEvent actionEvent) {
	}
}
