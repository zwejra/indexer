package cz.indexer.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class NonIndexedExtension {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 30)
	private String extension;

	//(fetch = FetchType.LAZY)
	@ManyToOne
	private Index index;

	public NonIndexedExtension() {}

	public NonIndexedExtension(String extension) {
		this.extension = extension;
	}

	@Override
	public String toString() {
		return extension;
	}
}
