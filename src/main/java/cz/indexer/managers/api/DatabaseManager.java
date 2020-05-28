package cz.indexer.managers.api;

public interface DatabaseManager {

	/**
	 * Initializes the database.
	 * Creates the optional metadata in the table (if they don't exist already).
	 *
	 */
	void initializeDatabase();

}
