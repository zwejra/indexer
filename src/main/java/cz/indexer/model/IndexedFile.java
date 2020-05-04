package cz.indexer.model;

import cz.indexer.model.enums.FileType;
import lombok.Data;
import org.eclipse.persistence.annotations.CascadeOnDelete;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
public class IndexedFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Lob
	@Column(nullable = false)
	private String fileName;

	@Lob
	@Column(nullable = false)
	private String path;

	private LocalDateTime creationTime;

	private LocalDateTime lastAccessTime;

	private LocalDateTime lastModifiedTime;

	private Long fileSize;

	@ManyToOne
	private Index index;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private FileType type;

	public IndexedFile() {}

	public IndexedFile(String fileName, String path, LocalDateTime creationTime, LocalDateTime lastAccessTime, LocalDateTime lastModifiedTime, Long fileSize, FileType type) {
		this.fileName = fileName;
		this.path = path;
		this.creationTime = creationTime;
		this.lastAccessTime = lastAccessTime;
		this.lastModifiedTime = lastModifiedTime;
		this.fileSize = fileSize;
		this.type = type;
	}
}
