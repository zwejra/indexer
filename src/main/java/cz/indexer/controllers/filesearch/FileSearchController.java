package cz.indexer.controllers.filesearch;

import com.jfoenix.controls.JFXButton;
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
import java.util.ResourceBundle;

public class FileSearchController implements Initializable {

	@FXML
	private Label searchResultsLabel;
	@FXML
	private Button searchButton;

	@FXML
	private TableView resultsTableView;
	@FXML
	private TableColumn nameTableColumn;
	@FXML
	private TableColumn pathTableColumn;
	@FXML
	private TableColumn sizeTableColumn;
	@FXML
	private TableColumn creationTimeTableColumn;
	@FXML
	private TableColumn lastChangeTimeTableColumn;
	@FXML
	private TableColumn lastAccessTimeTableColumn;

	@FXML
	private Label filtersLabel;

	@FXML
	private JFXButton memoryDeviceFilterButton;
	@FXML
	private JFXButton fileNameFilterButton;
	@FXML
	private JFXButton dateFilterButton;
	@FXML
	private JFXButton fileSizeFilterButton;

	@FXML
	private BorderPane rootBorderPane;

	@FXML
	private Parent memoryDeviceFilter;
	@FXML
	private Parent fileNameFilter;
	@FXML
	private Parent dateFilter;
	@FXML
	private Parent fileSizeFilter;

	@FXML
	private MemoryDeviceFilterController memoryDeviceFilterController;
	@FXML
	private FileNameFilterController fileNameFilterController;
	@FXML
	private DateFilterController dateFilterController;
	@FXML
	private FileSizeFilterController fileSizeFilterController;

	private MemoryDeviceManager memoryDeviceManager = MemoryDeviceManagerImpl.getInstance();
	private IndexManager indexManager = IndexManagerImpl.getInstance();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		rootBorderPane.setCenter(memoryDeviceFilter);

		memoryDeviceFilterController.setFileSearchController(this);
		fileNameFilterController.setFileSearchController(this);
		dateFilterController.setFileSearchController(this);
		fileSizeFilterController.setFileSearchController(this);
	}

	@FXML
	public void handleSearchActionButton(ActionEvent actionEvent) {
		ObservableList<MemoryDevice> checkedMemoryDevices = memoryDeviceFilterController.getMemoryDevicesCheckListView()
				.getCheckModel().getCheckedItems();

		System.out.println(checkedMemoryDevices);
	}

	@FXML
	public void handleMemoryDeviceFilterButton(ActionEvent actionEvent) {
		rootBorderPane.setCenter(memoryDeviceFilter);
	}

	@FXML
	public void handleFileNameFilterButton(ActionEvent actionEvent) {
		rootBorderPane.setCenter(fileNameFilter);
	}

	@FXML
	public void handleDateFilterButton(ActionEvent actionEvent) {
		rootBorderPane.setCenter(dateFilter);
	}

	@FXML
	public void handleFileSizeFilterButton(ActionEvent actionEvent) {
		rootBorderPane.setCenter(fileSizeFilter);
	}
}
