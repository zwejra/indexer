package cz.indexer.controllers;

import cz.indexer.managers.impl.DatabaseManagerImpl;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class MainSceneController implements Initializable {

	private DatabaseManagerImpl databaseManagerImpl = DatabaseManagerImpl.getInstance();

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		databaseManagerImpl.initializeDatabase();
	}
}