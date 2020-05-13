package cz.indexer.controllers.filesearch;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import cz.indexer.model.enums.NameCondition;
import cz.indexer.tools.I18N;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import lombok.Getter;

import java.net.URL;
import java.util.ResourceBundle;

public class FileNameFilterController implements Initializable {

	@FXML
	private Label nameFilterLabel;

	@FXML
	@Getter private JFXComboBox fileNameComboBox;

	@FXML
	@Getter private JFXTextField fileNameTextField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		nameFilterLabel.textProperty().bind(I18N.createStringBinding("label.name.filter"));
		refreshNameComboBoxes();
	}

	public void refreshNameComboBoxes() {
		fileNameComboBox.getItems().clear();
		fileNameComboBox.getItems().addAll(
				I18N.getMessage(NameCondition.CONTAINS),
				I18N.getMessage(NameCondition.NOT_CONTAINS),
				I18N.getMessage(NameCondition.STARTS_WITH),
				I18N.getMessage(NameCondition.ENDS_WITH)
		);
	}
}
