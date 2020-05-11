package cz.indexer.controllers.filesearch;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import cz.indexer.model.enums.SizeCondition;
import cz.indexer.tools.I18N;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import lombok.Getter;

import java.net.URL;
import java.util.ResourceBundle;

public class FileSizeFilterController implements Initializable {

	@FXML
	@Getter private JFXComboBox<String> fileSizeComboBox;

	@FXML
	@Getter private JFXTextField fileSizeTextField;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		refreshSizeFilterComboBox();
	}

	public void refreshSizeFilterComboBox() {
		fileSizeComboBox.getItems().clear();
		fileSizeComboBox.getItems().addAll(
				I18N.getMessage(SizeCondition.SMALLER),
				I18N.getMessage(SizeCondition.SMALLER_EQUAL),
				I18N.getMessage(SizeCondition.EQUAL),
				I18N.getMessage(SizeCondition.BIGGER),
				I18N.getMessage(SizeCondition.BIGGER_EQUAL)
		);
	}
}
