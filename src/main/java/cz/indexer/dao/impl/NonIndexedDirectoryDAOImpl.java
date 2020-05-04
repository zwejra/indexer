package cz.indexer.dao.impl;

import cz.indexer.dao.api.NonIndexedDirectoryDAO;
import cz.indexer.model.Index;
import cz.indexer.model.NonIndexedDirectory;

import java.util.List;

public class NonIndexedDirectoryDAOImpl implements NonIndexedDirectoryDAO {

	@Override
	public List<NonIndexedDirectory> getNonIndexedDirectories(Index index) {
		return null;
	}

	@Override
	public boolean deleteNonIndexedDirectories(Index index) {
		return false;
	}
}
