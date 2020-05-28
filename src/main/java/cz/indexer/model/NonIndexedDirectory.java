package cz.indexer.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Class representing directories, which are not indexed in the index.
 */
@Data
@Entity
public class NonIndexedDirectory {

	/**
	 * Unique identificator of the non indexed directory.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Path to the directory (trimmed - without mount/root directory)
	 */
	@Lob
	@Column(nullable = false)
	private String path;

	/**
	 * Index to which the non indexed directory belongs to.
	 */
	@ManyToOne
	private Index index;

	/**
	 * Implicit constructor.
	 */
	public NonIndexedDirectory() {}

	/**
	 * Constructor.
	 * @param path path to the directory (trimmed - without mount/root directory)
	 */
	public NonIndexedDirectory(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return path;
	}
}
