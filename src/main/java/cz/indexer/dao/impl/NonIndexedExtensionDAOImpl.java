package cz.indexer.dao.impl;

import cz.indexer.dao.api.NonIndexedExtensionDAO;
import cz.indexer.model.Index;
import cz.indexer.model.NonIndexedExtension;

import java.util.List;

public class NonIndexedExtensionDAOImpl implements NonIndexedExtensionDAO {

	@Override
	public List<NonIndexedExtension> getNonIndexedExtensions(Index index) {
		return null;
	}

	@Override
	public boolean deleteNonIndexedExtensions(Index index) {
		return false;
	}
}
