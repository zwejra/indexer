package cz.indexer.model;

import lombok.Data;

import javax.persistence.*;

/**
 * Class representing non indexed directories.
 */
@Data
@Entity
public class NonIndexedExtension {

	/**
	 * Unique identificator.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Extension of the file, which is not indexed.
	 */
	@Column(nullable = false, length = 30)
	private String extension;

	/**
	 * Index to which the non indexed extension belongs to.
	 */
	@ManyToOne
	private Index index;

	/**
	 * Implicit constructor.
	 */
	public NonIndexedExtension() {}

	/**
	 * Constructor.
	 * @param extension file extension which is ignored from indexing
	 */
	public NonIndexedExtension(String extension) {
		this.extension = extension;
	}

	@Override
	public String toString() {
		return extension;
	}
}
