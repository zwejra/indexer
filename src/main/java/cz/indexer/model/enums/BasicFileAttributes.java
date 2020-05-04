package cz.indexer.model.enums;

public enum BasicFileAttributes {
	FILENAME("filename"),
	PATH("path"),
	TYPE("type");

	public final String label;

	BasicFileAttributes(String label) {
		this.label = label;
	}
}
