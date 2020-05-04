package cz.indexer.controllers.filesearch;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import cz.indexer.managers.api.IndexManager;
import cz.indexer.managers.api.MemoryDeviceManager;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class FileNameFilterController implements Initializable {

	public JFXComboBox fileNameComboBox;

	public JFXTextField fileNameTextField;

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

	public void handleFileNameComboBox(ActionEvent actionEvent) {
	}

	public void handleFileNameTextField(ActionEvent actionEvent) {
	}
}
