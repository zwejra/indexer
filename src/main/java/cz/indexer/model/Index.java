package cz.indexer.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Class representing index of memory device.
 * Can't exist without connection to memory device.
 */
@Entity
public class Index {

	/**
	 * Unique identificator of the index.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@Getter @Setter private LocalDateTime lastModifiedTime;

	/**
	 * Owner of the index.
	 */
	@OneToOne(mappedBy = "index", fetch = FetchType.EAGER)
	@Getter private MemoryDevice memoryDevice;

	/**
	 * Set of metadata which are indexed.
	 */
	@ManyToMany
	@Getter private Set<Metadata> indexedMetadata = new HashSet<>();

	/**
	 * Set of ignored non indexed directories.
	 */
	@OneToMany(
			mappedBy = "index",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	@Getter private Set<NonIndexedDirectory> nonIndexedDirectories = new HashSet<>();

	/**
	 * Set of ignored non indexed extensions.
	 */
	@OneToMany(
			mappedBy = "index",
			cascade = CascadeType.ALL,
			orphanRemoval = true
	)
	@Getter private Set<NonIndexedExtension> nonIndexedExtensions = new HashSet<>();

	/**
	 * Implicit constructor.
	 */
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
