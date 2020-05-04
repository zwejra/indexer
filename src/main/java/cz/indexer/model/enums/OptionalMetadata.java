package cz.indexer.model.enums;

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
