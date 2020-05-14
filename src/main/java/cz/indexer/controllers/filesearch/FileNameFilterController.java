package cz.indexer.controllers.filesearch;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
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

		Label containsLabel = new Label();
		Label notContainsLabel = new Label();
		Label startsWith = new Label();
		Label endsWith = new Label();

		containsLabel.textProperty().bind(I18N.createStringBinding("contains"));
		notContainsLabel.textProperty().bind(I18N.createStringBinding("not.contains"));
		startsWith.textProperty().bind(I18N.createStringBinding("starts.with"));
		endsWith.textProperty().bind(I18N.createStringBinding("ends.with"));

		fileNameComboBox.getItems().setAll(
				containsLabel,
				notContainsLabel,
				startsWith,
				endsWith
		);
	}
}
