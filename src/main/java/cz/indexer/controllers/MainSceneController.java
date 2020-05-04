package cz.indexer.controllers;

import cz.indexer.managers.impl.DatabaseManagerImpl;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {

	DatabaseManagerImpl databaseManagerImpl = new DatabaseManagerImpl();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		databaseManagerImpl.initializeDatabase();
	}
}