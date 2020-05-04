package cz.indexer.dao.api;

import cz.indexer.model.Metadata;

import java.util.List;

public interface MetadataDAO {

	Metadata getMetadata(String name);

	List<Metadata> getAllMetadata();

	boolean createMetadata(Metadata metadata);

	boolean createMetadata(List<Metadata> metadataList);

	boolean deleteMetadata(Metadata metadata);

	boolean deleteMetadata(List<Metadata> metadata);
}
