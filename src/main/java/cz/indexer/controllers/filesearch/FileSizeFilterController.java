package cz.indexer.controllers.filesearch;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import cz.indexer.managers.api.IndexManager;
import cz.indexer.managers.api.MemoryDeviceManager;
import cz.indexer.managers.impl.IndexManagerImpl;
import cz.indexer.managers.impl.MemoryDeviceManagerImpl;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.Getter;

import java.net.URL;
import java.util.ResourceBundle;

public class FileSizeFilterController implements Initializable {

	@FXML
	@Getter private JFXComboBox fileSizeComboBox;

	@FXML
	@Getter private JFXTextField fileSizeTextField;

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
	public void handleFileSizeTextField(ActionEvent actionEvent) {
	}

	@FXML
	public void handleFileSizeComboBox(ActionEvent actionEvent) {
	}
}
