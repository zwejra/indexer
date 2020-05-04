package cz.indexer.dao.api;

import cz.indexer.model.Index;
import cz.indexer.model.Metadata;
import cz.indexer.model.NonIndexedDirectory;

import java.util.List;

public interface NonIndexedDirectoryDAO {

	List<NonIndexedDirectory> getNonIndexedDirectories(Index index);

	boolean deleteNonIndexedDirectories(Index index);

}
