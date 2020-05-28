package cz.indexer.model.enums;

/**
 * Enum with file types.
 */
public enum FileType {
	FILE("file"),
	DIRECTORY("directory"),
	SYMBOLIC_LINK("symbolic_link"),
	OTHER("other");

	public final String label;

	FileType(String label) {
		this.label = label;
	}
}
