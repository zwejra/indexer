package cz.indexer.model;

import cz.indexer.tools.I18N;
import lombok.Getter;
import lombok.Setter;
import oshi.PlatformEnum;
import oshi.SystemInfo;

import javax.persistence.*;

/**
 * MemoryDevice class represents memory device connected to a computer.
 * It can represent disconnected memory device as well, but it has to be previously indexed.
 */
@Entity
public class MemoryDevice {

	/**
	 * Unique ID of a memory device.
	 */
	@Id
	@Getter @Setter private String uuid;

	/**
	 * User defined name of a memory device.
	 * Used in path to the files if the memory device is disconnected.
	 */
	@Getter @Setter private String userDefinedName;

	/**
	 * Index of the medium. Is null when medium doesn't have an index.
	 */
	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn
	@Getter @Setter private Index index;

	/**
	 * Label of the memory device obtained from the operating system.
	 * Usually its the name of a partition user can see in the operating system.
	 * Not stored in database.
	 */
	@Transient
	@Getter @Setter private String label;

	/**
	 * Path to the mount (root) directory of a memory device.
	 * Not stored in database.
	 */
	@Transient
	@Getter private String mount;

	/**
	 * True when the memory device is indexed, false otherwise.
	 * Not stored in database.
	 */
	@Transient
	@Getter @Setter private boolean indexed;

	/**
	 * True when the memory device is indexed, false otherwise.
	 * Not stored in database.
	 */
	@Transient
	@Getter @Setter private boolean connected;

	public MemoryDevice() {}

	public MemoryDevice(String uuid, String label, String mount, boolean indexed, boolean connected) {
		this.uuid = uuid;
		this.label = label;
		this.setMount(mount);
		this.indexed = indexed;
		this.connected = connected;
	}

	@Override
	public String toString() {
		if (this.isConnected()) {
			if (this.isIndexed()) {
				return "(" + this.mount + ") " + this.label + " [" + this.userDefinedName + "]";
			} else {
				return "(" + this.mount + ") " + this.label;
			}
		} else {
			if (this.isIndexed()) {
				return this.userDefinedName;
			} else {
				return I18N.getMessage("error.memory.device.to.string", this.uuid);
			}
		}
	}

	public void setMount(String mount) {
		if (SystemInfo.getCurrentPlatformEnum() == PlatformEnum.WINDOWS) {
			this.mount = mount;
		} else {
			if (mount.equals("/")) this.mount = mount;
			else this.mount = mount + "/";
		}
	}
}
