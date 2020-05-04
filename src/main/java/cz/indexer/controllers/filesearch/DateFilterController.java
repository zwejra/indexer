package cz.indexer.controllers.filesearch;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import cz.indexer.managers.api.IndexManager;
import cz.indexer.managers.api.MemoryDeviceManager;
import cz.indexer.managers.impl.IndexManagerImpl;
import cz.indexer.managers.impl.MemoryDeviceManagerImpl;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DateFilterController implements Initializable {

	public Label creationDateLabel;
	public Label lastModifiedDateLabel;
	public Label lastAccessDateLabel;

	public JFXDatePicker creationDateDatePicker;
	public JFXTimePicker creationDateTimePicker;

	public JFXDatePicker lastModifiedDateDatePicker;
	public JFXTimePicker lastModifiedDateTimePicker;

	public JFXDatePicker lastAccessDateDatePicker;
	public JFXTimePicker lastAccessDateTimePicker;

	public JFXComboBox creationDateComboBox;
	public JFXComboBox lastModifiedDateComboBox;
	public JFXComboBox lastAccessDateComboBox;

	FileSearchController fileSearchController;

	MemoryDeviceManager memoryDeviceManager;
	IndexManager indexManager;

	public void setMemoryDeviceManager(MemoryDeviceManager memoryDeviceManager) {
		this.memoryDeviceManager = memoryDeviceManager;
	}

	public void setIndexManager(IndexManager indexManager) {
		this.indexManager = indexManager;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void setFileSearchController(FileSearchController fileSearchController) {
		this.fileSearchController = fileSearchController;
	}

	public void handleCreationDateDatePicker(ActionEvent actionEvent) {

	}

	public void handleCreationDateTimePicker(ActionEvent actionEvent) {
	}

	public void handleLastModifiedDateDatePicker(ActionEvent actionEvent) {
	}

	public void handleLastModifiedDateTimePicker(ActionEvent actionEvent) {
	}

	public void handleLastAccessDateDatePicker(ActionEvent actionEvent) {
	}

	public void handleLastAccessDateTimePicker(ActionEvent actionEvent) {
	}

	public void handleCreationDateComboBox(ActionEvent actionEvent) {
	}

	public void handleLastModifiedDateComboBox(ActionEvent actionEvent) {
	}

	public void handleLastAccessDateComboBox(ActionEvent actionEvent) {
	}
}
