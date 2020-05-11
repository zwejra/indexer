package cz.indexer.controllers;

import cz.indexer.controllers.filesearch.FileSearchController;
import cz.indexer.controllers.index.IndexManagementController;
import cz.indexer.tools.I18N;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {

	@FXML
	private Tab indexManagementTab;
	@FXML
	private Tab fileSearchTab;

	@FXML
	private FileSearchController fileSearchController;
	@FXML
	private IndexManagementController indexManagementController;

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		indexManagementTab.textProperty().bind(I18N.createStringBinding("tab.indexManagement"));
		fileSearchTab.textProperty().bind(I18N.createStringBinding("tab.fileSearch"));
	}

	public void handleChangeLanguageToCzech(ActionEvent actionEvent) {
		I18N.setLocale(new Locale("cs", "CZ"));
		fileSearchController.getDateFilterController().refreshDateComboBoxes();
		fileSearchController.getFileNameFilterController().refreshNameComboBoxes();
		fileSearchController.getFileSizeFilterController().refreshSizeFilterComboBox();
	}

	public void handleChangeLanguageToEnglish(ActionEvent actionEvent) {
		I18N.setLocale(Locale.ENGLISH);
		fileSearchController.getDateFilterController().refreshDateComboBoxes();
		fileSearchController.getFileNameFilterController().refreshNameComboBoxes();
		fileSearchController.getFileSizeFilterController().refreshSizeFilterComboBox();
	}
}