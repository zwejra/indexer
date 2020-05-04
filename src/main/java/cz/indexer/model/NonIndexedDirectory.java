package cz.indexer.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class NonIndexedDirectory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	@Column(nullable = false)
	private String path;

	@ManyToOne
	private Index index;

	public NonIndexedDirectory() {}

	public NonIndexedDirectory(String path) {
		this.path = path;
	}

	@Override
	public String toString() {
		return path;
	}
}
