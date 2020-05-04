package cz.indexer.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
public class Index {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private LocalDateTime lastModifiedTime;

	@ManyToMany
	private Set<Metadata> indexedMetadata = new HashSet<>();

	@OneToMany(
			mappedBy = "index",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	private Set<NonIndexedDirectory> nonIndexedDirectories = new HashSet<>();

	@OneToMany(
			mappedBy = "index",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	private Set<NonIndexedExtension> nonIndexedExtensions = new HashSet<>();

	public Index() {}
}
