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

/**
 * Controller for the main scene, which includes two tabs and footer with localization settings.
 * "Flag icons made by <a href="https://www.flaticon.com/authors/roundicons">Roundicons</a> from <a href="https://www.flaticon.com/">flaticon.com</a>"
 */
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
	}

	public void handleChangeLanguageToEnglish(ActionEvent actionEvent) {
		I18N.setLocale(Locale.ENGLISH);
	}
}