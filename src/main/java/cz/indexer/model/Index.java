package cz.indexer.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Index {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@Getter @Setter private LocalDateTime lastModifiedTime;

	@OneToOne(mappedBy = "index", fetch = FetchType.EAGER)
	@Getter private MemoryDevice memoryDevice;

	@ManyToMany
	@Getter private Set<Metadata> indexedMetadata = new HashSet<>();

	@OneToMany(
			mappedBy = "index",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	@Getter private Set<NonIndexedDirectory> nonIndexedDirectories = new HashSet<>();

	@OneToMany(
			mappedBy = "index",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	@Getter private Set<NonIndexedExtension> nonIndexedExtensions = new HashSet<>();

	public Index() {}

	@Override
	public String toString() {
		return "Index{" +
				"id=" + id +
				", lastModifiedTime=" + lastModifiedTime +
				", memoryDevice=" + memoryDevice +
				'}';
	}
}
