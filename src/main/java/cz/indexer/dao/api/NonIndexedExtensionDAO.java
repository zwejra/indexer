package cz.indexer.dao.api;

import cz.indexer.model.Index;
import cz.indexer.model.NonIndexedDirectory;
import cz.indexer.model.NonIndexedExtension;

import java.util.List;

public interface NonIndexedExtensionDAO {

	List<NonIndexedExtension> getNonIndexedExtensions(Index index);

	boolean deleteNonIndexedExtensions(Index index);

}
