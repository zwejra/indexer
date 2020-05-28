package cz.indexer.model;

import cz.indexer.model.enums.OptionalMetadata;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Class representing optional metadata, which can be indexed.
 */
@Data
@Entity
public class Metadata {

	/**
	 * Unique identificator of the metadata.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Value/Name of the metadata.
	 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OptionalMetadata value;

	/**
	 * Set of all indexed which index this metadata.
	 */
	@ManyToMany(mappedBy = "indexedMetadata")
	private Set<Index> indexes = new HashSet<>();

	/**
	 * Implicit constructor.
	 */
	public Metadata() {}

	/**
	 * Constructor
	 * @param value metadata value/name from the OptionalMetadata enum
	 */
	public Metadata(OptionalMetadata value) {
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof Metadata)) return false;
		Metadata metadata = (Metadata) o;
		return value.equals(metadata.value);
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}
}
