package cz.indexer.model;

import cz.indexer.managers.impl.MemoryDeviceManagerImpl;
import cz.indexer.model.enums.FileType;
import lombok.Data;
import oshi.PlatformEnum;
import oshi.SystemInfo;

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

	@ManyToOne(fetch = FetchType.EAGER)
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

	public String getAbsolutePath() {
		MemoryDevice memoryDevice = this.getIndex().getMemoryDevice();
		memoryDevice = MemoryDeviceManagerImpl.getInstance().refreshConnectedMemoryDevice(memoryDevice);

		if (memoryDevice.isConnected()) {
			return memoryDevice.getMount() + path;
		} else {
			if (SystemInfo.getCurrentPlatformEnum() == PlatformEnum.WINDOWS) {
				return memoryDevice.getUserDefinedName() + "\\" + path;
			} else {
				return memoryDevice.getUserDefinedName() + "/" + path;
			}
		}
	}
}
