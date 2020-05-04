package cz.indexer.model;

import cz.indexer.model.enums.OptionalMetadata;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Data
@Entity
public class Metadata {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private OptionalMetadata value;

	@ManyToMany(mappedBy = "indexedMetadata")
	private Set<Index> indexes = new HashSet<>();

	public Metadata() {}

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
