package cz.indexer.controllers.filesearch;

import com.jfoenix.controls.JFXRadioButton;
import cz.indexer.model.MemoryDevice;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.Getter;
import org.controlsfx.control.CheckListView;

import java.net.URL;
import java.util.ResourceBundle;

public class MemoryDeviceFilterController implements Initializable {

	@FXML
	private JFXRadioButton chooseDevicesRadioButton;
	@FXML
	private JFXRadioButton allDevicesRadioButton;

	@FXML
	@Getter
	private CheckListView<MemoryDevice> memoryDevicesCheckListView = new CheckListView();

	FileSearchController fileSearchController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		allDevicesRadioButton.setSelected(true);
		chooseDevicesRadioButton.setSelected(false);
		memoryDevicesCheckListView.setDisable(true);
	}

	public void setFileSearchController(FileSearchController fileSearchController) {
		this.fileSearchController = fileSearchController;
	}

	public void setMemoryDevicesListViewItems(ObservableList<MemoryDevice> memoryDevices) {
		this.memoryDevicesCheckListView.setItems(memoryDevices);
	}

	public void handleAllDevicesRadioButton(ActionEvent actionEvent) {
		allDevicesRadioButton.setSelected(true);
		chooseDevicesRadioButton.setSelected(false);
		memoryDevicesCheckListView.setDisable(true);
	}

	public void handleChooseDevicesRadioButton(ActionEvent actionEvent) {
		allDevicesRadioButton.setSelected(false);
		chooseDevicesRadioButton.setSelected(true);
		memoryDevicesCheckListView.setDisable(false);
	}
}
