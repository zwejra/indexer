package cz.indexer.model;

import cz.indexer.managers.impl.MemoryDeviceManagerImpl;
import cz.indexer.model.enums.FileType;
import lombok.Data;
import oshi.PlatformEnum;
import oshi.SystemInfo;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Class representing indexed file.
 * Always belongs to some index, can't be orphan without index.
 */
@Data
@Entity
public class IndexedFile {

	/**
	 * Unique identificator of the indexed file.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	/**
	 * Name of the file.
	 */
	@Lob
	@Column(nullable = false)
	private String fileName;

	/**
	 * Path to the file without mount (root directory of the memory device).
	 */
	@Lob
	@Column(nullable = false)
	private String path;

	/**
	 * File creation time.
	 */
	private LocalDateTime creationTime;

	/**
	 * File last access time.
	 */
	private LocalDateTime lastAccessTime;

	/**
	 * File last modified time.
	 */
	private LocalDateTime lastModifiedTime;

	/**
	 * Size of the file.
	 */
	private Long fileSize;

	/**
	 * The Index to which the file belongs.
	 */
	@ManyToOne(fetch = FetchType.EAGER)
	private Index index;

	/**
	 * Type of the file (file, directory, symbolic link, other)
	 */
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private FileType type;

	/**
	 * Implicit constructor.
	 */
	public IndexedFile() {}

	/**
	 * Returns absolute path to the file based on the state of memory device.
	 * If memory device to which the file belongs to is connected, it will generate the correct path to the file.
	 * If memory device is not connected, it will put name of the memory device as a mount to the path,
	 * so user can see where he can find this file.
	 * @return path to the file
	 */
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
