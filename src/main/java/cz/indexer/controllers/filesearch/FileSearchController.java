package cz.indexer.controllers.filesearch;

import com.jfoenix.controls.JFXButton;
import cz.indexer.managers.api.FileSearchManager;
import cz.indexer.managers.api.IndexManager;
import cz.indexer.managers.api.MemoryDeviceManager;
import cz.indexer.managers.impl.FileSearchManagerImpl;
import cz.indexer.managers.impl.IndexManagerImpl;
import cz.indexer.managers.impl.MemoryDeviceManagerImpl;
import cz.indexer.model.IndexedFile;
import cz.indexer.model.MemoryDevice;
import cz.indexer.model.enums.DateCondition;
import cz.indexer.model.enums.FileType;
import cz.indexer.model.enums.NameCondition;
import cz.indexer.model.enums.SizeCondition;
import cz.indexer.model.gui.SearchDateValue;
import cz.indexer.model.gui.SearchFileNameValue;
import cz.indexer.model.gui.SearchSizeValue;
import cz.indexer.tools.I18N;
import cz.indexer.tools.UtilTools;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.InputMismatchException;
import java.util.ResourceBundle;

public class FileSearchController implements Initializable {

	@FXML
	private Label searchResultsLabel;
	@FXML
	private Button searchButton;

	@FXML
	private TableView<IndexedFile> resultsTableView;
	@FXML
	private TableColumn<IndexedFile, String> nameTableColumn;
	@FXML
	private TableColumn<IndexedFile, String> pathTableColumn;
	@FXML
	private TableColumn<IndexedFile, Long> sizeTableColumn;
	@FXML
	private TableColumn creationTimeTableColumn;
	@FXML
	private TableColumn lastModifiedTimeTableColumn;
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
	@Getter private MemoryDeviceFilterController memoryDeviceFilterController;
	@FXML
	@Getter private FileNameFilterController fileNameFilterController;
	@FXML
	@Getter private DateFilterController dateFilterController;
	@FXML
	@Getter private FileSizeFilterController fileSizeFilterController;

	private MemoryDeviceManager memoryDeviceManager = MemoryDeviceManagerImpl.getInstance();
	private IndexManager indexManager = IndexManagerImpl.getInstance();
	private FileSearchManager fileSearchManager = FileSearchManagerImpl.getInstance();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		filtersLabel.textProperty().bind(I18N.createStringBinding("label.search.filters"));
		searchResultsLabel.textProperty().bind(I18N.createStringBinding("label.search.results"));
		memoryDeviceFilterButton.textProperty().bind(I18N.createStringBinding("button.search.memoryDevicesFilter"));
		fileNameFilterButton.textProperty().bind(I18N.createStringBinding("button.search.filenameFilter"));
		dateFilterButton.textProperty().bind(I18N.createStringBinding("button.search.dateFilter"));
		fileSizeFilterButton.textProperty().bind(I18N.createStringBinding("button.search.filesizeFilter"));
		searchButton.textProperty().bind(I18N.createStringBinding("button.search"));
		nameTableColumn.textProperty().bind(I18N.createStringBinding("column.search.name"));
		pathTableColumn.textProperty().bind(I18N.createStringBinding("column.search.path"));
		sizeTableColumn.textProperty().bind(I18N.createStringBinding("column.search.size"));
		creationTimeTableColumn.textProperty().bind(I18N.createStringBinding("column.search.creationTime"));
		lastModifiedTimeTableColumn.textProperty().bind(I18N.createStringBinding("column.search.lastModifiedTime"));
		lastAccessTimeTableColumn.textProperty().bind(I18N.createStringBinding("column.search.lastAccessTime"));

		rootBorderPane.setCenter(memoryDeviceFilter);

		resultsTableView.setItems(fileSearchManager.getSearchResults());
		nameTableColumn.setCellValueFactory(new PropertyValueFactory<IndexedFile, String>("fileName"));
		pathTableColumn.setCellValueFactory(c -> new ReadOnlyObjectWrapper(c.getValue().getAbsolutePath()));

		sizeTableColumn.setCellValueFactory(new PropertyValueFactory<IndexedFile, Long>("fileSize"));
		creationTimeTableColumn.setCellValueFactory(new PropertyValueFactory<IndexedFile, LocalDateTime>("creationTime"));
		lastModifiedTimeTableColumn.setCellValueFactory(new PropertyValueFactory<IndexedFile, LocalDateTime>("lastAccessTime"));
		lastAccessTimeTableColumn.setCellValueFactory(new PropertyValueFactory<IndexedFile, LocalDateTime>("lastModifiedTime"));

		ContextMenu cm = new ContextMenu();
		MenuItem menuItem = new MenuItem("Otevrit umisteni souboru");
		cm.getItems().add(menuItem);

		menuItem.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				IndexedFile selectedFile = resultsTableView.getSelectionModel().getSelectedItem();
				MemoryDevice fileOwner = selectedFile.getIndex().getMemoryDevice();
				fileOwner = memoryDeviceManager.refreshConnectedMemoryDevice(fileOwner);

				try {
					if (Desktop.isDesktopSupported()) {
						if (fileOwner.isConnected()) {
							if (selectedFile.getType().equals(FileType.DIRECTORY)) {
								Desktop.getDesktop().open(new File(selectedFile.getAbsolutePath()));
							} else {
								File path = new File(selectedFile.getAbsolutePath());
								Desktop.getDesktop().open(new File(path.getParent()));
							}
						} else {
							Alert alert = new Alert(Alert.AlertType.ERROR, "Zarizeni neni pripojeno.");
							alert.showAndWait();
						}
					} else {
						Alert alert = new Alert(Alert.AlertType.ERROR, "Tento operacni system nepodporuje tuto funkci.");
						alert.showAndWait();
					}
				} catch (IOException e) {
					Alert alert = new Alert(Alert.AlertType.ERROR, "Soubor nelze otevrit.");
					alert.showAndWait();
				}
			}
		});

		resultsTableView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				if(t.getButton() == MouseButton.SECONDARY) {
					cm.show(resultsTableView, t.getScreenX(), t.getScreenY());
				}
				if (t.getButton() == MouseButton.PRIMARY) {
					cm.hide();
				}
			}
		});

		creationTimeTableColumn.setCellFactory(column -> {
			TableCell<IndexedFile, LocalDateTime> cell = new TableCell<IndexedFile, LocalDateTime>() {
				private DateTimeFormatter format = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if(empty || item == null) {
						setText(null);
					}
					else {
						setText(format.format(item));
					}
				}
			};

			return cell;
		});

		lastAccessTimeTableColumn.setCellFactory(column -> {
			TableCell<IndexedFile, LocalDateTime> cell = new TableCell<IndexedFile, LocalDateTime>() {
				private DateTimeFormatter format = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if(empty || item == null) {
						setText(null);
					}
					else {
						setText(format.format(item));
					}
				}
			};

			return cell;
		});

		lastModifiedTimeTableColumn.setCellFactory(column -> {
			TableCell<IndexedFile, LocalDateTime> cell = new TableCell<IndexedFile, LocalDateTime>() {
				private DateTimeFormatter format = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM);

				@Override
				protected void updateItem(LocalDateTime item, boolean empty) {
					super.updateItem(item, empty);
					if(empty || item == null) {
						setText(null);
					}
					else {
						setText(format.format(item));
					}
				}
			};

			return cell;
		});
	}

	@FXML
	public void handleSearchActionButton(ActionEvent actionEvent) {
		ObservableList<MemoryDevice> checkedMemoryDevices = null;
		boolean searchAllDevices = true;

		if (memoryDeviceFilterController.getChooseDevicesRadioButton().isSelected()) {
			searchAllDevices = false;
			if (memoryDeviceFilterController.getMemoryDevicesCheckListView().getCheckModel().isEmpty()) {
				Alert alert = new Alert(Alert.AlertType.ERROR, "Pro spusteni vyhledavani musi byt vybrano alespon jedno pametove zarizeni.");
				alert.showAndWait();
				return;
			}
			checkedMemoryDevices = memoryDeviceFilterController.getMemoryDevicesCheckListView().getCheckModel().getCheckedItems();
		}

		try {
			SearchDateValue creationSearchDateValue = getSearchDateValue(dateFilterController.getCreationDateComboBox().getSelectionModel().getSelectedIndex(),
					dateFilterController.getCreationDateDatePicker().getValue(),
					dateFilterController.getCreationDateTimePicker().getValue());

			SearchDateValue lastAccessSearchDateValue = getSearchDateValue(dateFilterController.getLastAccessDateComboBox().getSelectionModel().getSelectedIndex(),
					dateFilterController.getLastAccessDateDatePicker().getValue(),
					dateFilterController.getLastAccessDateTimePicker().getValue());

			SearchDateValue lastModifiedSearchDateValue = getSearchDateValue(dateFilterController.getLastModifiedDateComboBox().getSelectionModel().getSelectedIndex(),
					dateFilterController.getLastModifiedDateDatePicker().getValue(),
					dateFilterController.getLastModifiedDateTimePicker().getValue());

			SearchSizeValue searchSizeValue = getSearchSizeValue(fileSizeFilterController.getFileSizeComboBox().getSelectionModel().getSelectedIndex(),
					fileSizeFilterController.getFileSizeTextField().getText());

			SearchFileNameValue searchFileNameValue = getSearchFileNameValue(fileNameFilterController.getFileNameComboBox().getSelectionModel().getSelectedIndex(),
					fileNameFilterController.getFileNameTextField().getText());

			fileSearchManager.searchFiles(searchAllDevices, checkedMemoryDevices, searchFileNameValue, searchSizeValue,
					creationSearchDateValue, lastAccessSearchDateValue, lastModifiedSearchDateValue);
		} catch (InputMismatchException | NumberFormatException e) {
			Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
			alert.showAndWait();
		}
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

	private SearchDateValue getSearchDateValue(int index, LocalDate localDate, LocalTime localTime) throws InputMismatchException {
		if (index == -1) return null;
		DateCondition dateCondition = DateCondition.values()[index];
		LocalDateTime localDateTime = UtilTools.combineDateAndTime(localDate, localTime, dateCondition);

		if (localDateTime == null) return null;

		return new SearchDateValue(localDateTime, dateCondition);
	}

	private SearchSizeValue getSearchSizeValue(int index, String sizeValue) throws NumberFormatException {
		if (index == -1) return null;

		SizeCondition sizeCondition = SizeCondition.values()[index];

		Long size;
		try {
			size = Long.parseLong(sizeValue);
		} catch (NumberFormatException e) {
			throw new NumberFormatException(I18N.getMessage("exception.wrong.size.format"));
		}

		return new SearchSizeValue(size, sizeCondition);
	}

	private SearchFileNameValue getSearchFileNameValue(int index, String filename) throws NumberFormatException {
		if (index == -1) return null;
		if (StringUtils.isBlank(filename)) return null;

		NameCondition nameCondition = NameCondition.values()[index];
		return new SearchFileNameValue(filename, nameCondition);
	}
}
