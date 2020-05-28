package cz.indexer.dao.api;

import cz.indexer.model.Metadata;

import java.util.List;

/**
 * DAO object for management of Metadata entity
 */
public interface MetadataDAO {

	/**
	 * Persists list of optional metadata.
	 * @param metadataList list of optional metadata to be persisted.
	 */
	void createMetadata(List<Metadata> metadataList);

	/**
	 * Removes existing metadata from the database.
	 * @param metadata metadata to be removed
	 */
	void deleteMetadata(Metadata metadata);

	/**
	 * Gets all optional metadata stored in the database.
	 * @return list of all metadata
	 */
	List<Metadata> getAllMetadata();
}
