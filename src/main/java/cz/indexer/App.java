package cz.indexer;

import cz.indexer.managers.api.DatabaseManager;
import cz.indexer.managers.impl.DatabaseManagerImpl;
import cz.indexer.tools.I18N;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Class called from the Launcher. Creates initial window and shows it to the user.
 */
public class App extends Application {

	DatabaseManager databaseManager = DatabaseManagerImpl.getInstance();

	public static void main(String[] args) {
		launch(args);
	}

	/**
	 * Initial method.
	 * Creates initial window and shows it to the user.
	 * @param stage initial scene will be set to this stage
	 * @throws Exception
	 */
	@Override
	public void start(Stage stage) throws Exception {
		databaseManager.initializeDatabase();

		Parent root = FXMLLoader.load(getClass().getResource("/MainScene.fxml"), I18N.getBundle());

		Scene scene = new Scene(root);

		stage.setScene(scene);
		stage.titleProperty().bind(I18N.createStringBinding("window.title"));
		stage.show();
	}
}