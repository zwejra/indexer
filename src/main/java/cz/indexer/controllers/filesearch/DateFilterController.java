package cz.indexer.controllers.filesearch;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import cz.indexer.tools.I18N;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import lombok.Getter;

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
	@Getter private JFXDatePicker creationDateDatePicker;
	@FXML
	@Getter private JFXTimePicker creationDateTimePicker;

	@FXML
	@Getter private JFXDatePicker lastModifiedDateDatePicker;
	@FXML
	@Getter private JFXTimePicker lastModifiedDateTimePicker;

	@FXML
	@Getter private JFXDatePicker lastAccessDateDatePicker;
	@FXML
	@Getter private JFXTimePicker lastAccessDateTimePicker;

	@FXML
	@Getter private JFXComboBox creationDateComboBox;
	@FXML
	@Getter private JFXComboBox lastModifiedDateComboBox;
	@FXML
	@Getter private JFXComboBox lastAccessDateComboBox;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		creationDateLabel.textProperty().bind(I18N.createStringBinding("label.date.creationDate"));
		lastModifiedDateLabel.textProperty().bind(I18N.createStringBinding("label.date.lastModifiedDate"));
		lastAccessDateLabel.textProperty().bind(I18N.createStringBinding("label.date.lastAccessDate"));

		Label before = new Label();
		Label after = new Label();

		before.textProperty().bind(I18N.createStringBinding("before"));
		after.textProperty().bind(I18N.createStringBinding("after"));

		creationDateComboBox.getItems().setAll(
				before,
				after
		);

		lastModifiedDateComboBox.getItems().setAll(
				before,
				after
		);

		lastAccessDateComboBox.getItems().setAll(
				before,
				after
		);
	}

}
