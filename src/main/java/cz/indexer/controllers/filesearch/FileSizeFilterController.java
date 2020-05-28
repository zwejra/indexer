package cz.indexer.controllers.filesearch;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import cz.indexer.model.enums.SizeCondition;
import cz.indexer.tools.I18N;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import lombok.Getter;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the file size filter in the File Search tab.
 */
public class FileSizeFilterController implements Initializable {

	@FXML
	private Label sizeFilterLabel;

	@FXML
	@Getter private JFXComboBox fileSizeComboBox;

	@FXML
	@Getter private JFXTextField fileSizeTextField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sizeFilterLabel.textProperty().bind(I18N.createStringBinding("label.size.filter"));

		Label smaller = new Label();
		Label smallerEqual = new Label();
		Label equal = new Label();
		Label bigger = new Label();
		Label biggerEqual = new Label();

		smaller.textProperty().bind(I18N.createStringBinding("smaller"));
		smallerEqual.textProperty().bind(I18N.createStringBinding("smaller.equal"));
		equal.textProperty().bind(I18N.createStringBinding("equal"));
		bigger.textProperty().bind(I18N.createStringBinding("bigger"));
		biggerEqual.textProperty().bind(I18N.createStringBinding("bigger.equal"));

		fileSizeComboBox.getItems().setAll(
				smaller,
				smallerEqual,
				equal,
				bigger,
				biggerEqual
		);
	}
}
