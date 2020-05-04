package cz.indexer.model;

import lombok.Data;

import javax.persistence.*;

/**
 * MemoryDevice class represents memory device connected to a computer.
 * It can represent disconnected memory device as well, but it has to be previously indexed.
 */
@Data
@Entity
public class MemoryDevice {

	/**
	 * Unique ID of a memory device.
	 */
	@Id
	private String uuid;

	/**
	 * User defined name of a memory device.
	 */
	private String userDefinedName;

	/**
	 * Index of the medium. Is null when medium doesn't have an index.
	 */
	@OneToOne(cascade = CascadeType.ALL)
	private Index index;

	/**
	 * Label of a memory device.
	 */
	@Transient
	private String label;

	/**
	 * Path to the mount directory of a memory device.
	 */
	@Transient
	private String mount;

	/**
	 * State of index.
	 * Value 'Indexed' means that index for this medium exists, 'Non_indexed' means the opposite.
	 */
	@Transient
	private boolean indexed;

	/**
	 * State of medium - is connected or disconnected.
	 */
	@Transient
	private boolean connected;

	public MemoryDevice() {}

	public MemoryDevice(String uuid, String label, String mount, boolean indexed, boolean connected) {
		this.uuid = uuid;
		this.label = label;
		this.mount = mount;
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
				return "Error - shouldn't be here";
			}
		}
	}
}
