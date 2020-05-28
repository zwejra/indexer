package cz.indexer.model.enums;

/**
 * Enum of optional metadata name values.
 * Used to create metadata values in database.
 * These are shown as options to the user in Create Index window.
 */
public enum OptionalMetadata {
	CREATION_TIME("creation_time"),
	LAST_ACCESS_TIME("last_access_time"),
	LAST_MODIFIED_TIME("last_modified_time"),
	SIZE("size");

	public final String label;

	OptionalMetadata(String label) {
		this.label = label;
	}
}
