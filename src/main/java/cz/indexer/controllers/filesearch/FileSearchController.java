package cz.indexer.controllers.filesearch;

import com.jfoenix.controls.JFXButton;
import com.sun.jna.Memory;
import cz.indexer.managers.api.IndexManager;
import cz.indexer.managers.api.MemoryDeviceManager;
import cz.indexer.managers.impl.IndexManagerImpl;
import cz.indexer.managers.impl.MemoryDeviceManagerImpl;
import cz.indexer.model.MemoryDevice;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import lombok.Getter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class FileSearchController implements Initializable {

	public Label searchResultsLabel;
	public Button searchButton;

	public TableView resultsTableView;
	public TableColumn nameTableColumn;
	public TableColumn pathTableColumn;
	public TableColumn sizeTableColumn;
	public TableColumn creationTimeTableColumn;
	public TableColumn lastChangeTimeTableColumn;
	public TableColumn lastAccessTimeTableColumn;

	public Label filtersLabel;

	public JFXButton memoryDeviceFilterButton;
	public JFXButton fileNameFilterButton;
	public JFXButton dateFilterButton;
	public JFXButton fileSizeFilterButton;

	@FXML
	BorderPane rootBorderPane;

	@FXML
	Parent memoryDeviceFilter;
	@FXML
	Parent fileNameFilter;
	@FXML
	Parent dateFilter;
	@FXML
	Parent fileSizeFilter;

	@FXML
	MemoryDeviceFilterController memoryDeviceFilterController;
	@FXML
	FileNameFilterController fileNameFilterController;
	@FXML
	DateFilterController dateFilterController;
	@FXML
	FileSizeFilterController fileSizeFilterController;

	@Getter MemoryDeviceManager memoryDeviceManager = new MemoryDeviceManagerImpl();
	@Getter IndexManager indexManager = new IndexManagerImpl();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		rootBorderPane.setCenter(memoryDeviceFilter);
		memoryDeviceManager.refreshMemoryDevices();
		memoryDeviceFilterController.setMemoryDevicesListViewItems(memoryDeviceManager.getIndexedMemoryDevices());

		memoryDeviceFilterController.setFileSearchController(this);
		fileNameFilterController.setFileSearchController(this);
		dateFilterController.setFileSearchController(this);
		fileSizeFilterController.setFileSearchController(this);
	}

	public void handleSearchActionButton(ActionEvent actionEvent) {
		ObservableList<Integer> checkedMemoryDevices = memoryDeviceFilterController.getMemoryDevicesCheckListView().getCheckModel().getCheckedIndices();
	}

	public void handleMemoryDeviceFilterButton(ActionEvent actionEvent) {
		rootBorderPane.setCenter(memoryDeviceFilter);
	}

	public void handleFileNameFilterButton(ActionEvent actionEvent) {
		rootBorderPane.setCenter(fileNameFilter);
	}

	public void handleDateFilterButton(ActionEvent actionEvent) {
		rootBorderPane.setCenter(dateFilter);
	}

	public void handleFileSizeFilterButton(ActionEvent actionEvent) {
		rootBorderPane.setCenter(fileSizeFilter);
	}
}
