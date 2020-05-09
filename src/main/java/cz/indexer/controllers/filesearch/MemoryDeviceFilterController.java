package cz.indexer.controllers.filesearch;

import com.jfoenix.controls.JFXRadioButton;
import cz.indexer.managers.api.IndexManager;
import cz.indexer.managers.api.MemoryDeviceManager;
import cz.indexer.managers.impl.IndexManagerImpl;
import cz.indexer.managers.impl.MemoryDeviceManagerImpl;
import cz.indexer.model.MemoryDevice;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.Getter;
import org.controlsfx.control.CheckListView;

import java.net.URL;
import java.util.ResourceBundle;

public class MemoryDeviceFilterController implements Initializable {

	@FXML
	@Getter private JFXRadioButton chooseDevicesRadioButton;
	@FXML
	@Getter private JFXRadioButton allDevicesRadioButton;

	@FXML
	@Getter private CheckListView<MemoryDevice> memoryDevicesCheckListView = new CheckListView();

	private FileSearchController fileSearchController;

	private MemoryDeviceManager memoryDeviceManager = MemoryDeviceManagerImpl.getInstance();
	private IndexManager indexManager = IndexManagerImpl.getInstance();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		memoryDevicesCheckListView.setItems(memoryDeviceManager.getIndexedMemoryDevices());
		memoryDevicesCheckListView.getCheckModel().checkAll();

		memoryDevicesCheckListView.getItems().addListener(new ListChangeListener() {
			public void onChanged(ListChangeListener.Change c) {
				c.next();
				if (c.wasAdded() || c.wasRemoved()) {
					memoryDevicesCheckListView.getCheckModel().checkAll();
					return;
				}
			}
		});

		allDevicesRadioButton.setSelected(true);
		chooseDevicesRadioButton.setSelected(false);
		memoryDevicesCheckListView.setDisable(true);
	}

	public void setFileSearchController(FileSearchController fileSearchController) {
		this.fileSearchController = fileSearchController;
	}

	@FXML
	public void handleAllDevicesRadioButton(ActionEvent actionEvent) {
		allDevicesRadioButton.setSelected(true);
		chooseDevicesRadioButton.setSelected(false);
		memoryDevicesCheckListView.setDisable(true);
	}

	@FXML
	public void handleChooseDevicesRadioButton(ActionEvent actionEvent) {
		allDevicesRadioButton.setSelected(false);
		chooseDevicesRadioButton.setSelected(true);
		memoryDevicesCheckListView.setDisable(false);
	}
}
