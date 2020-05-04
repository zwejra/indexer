package cz.indexer.dao.impl;

import cz.indexer.dao.api.NonIndexedDirectoryDAO;
import cz.indexer.model.Index;
import cz.indexer.model.NonIndexedDirectory;
import cz.indexer.tools.UtilTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class NonIndexedDirectoryDAOImpl implements NonIndexedDirectoryDAO {

	@PersistenceContext(unitName = UtilTools.PERSISTENCE_UNIT)
	EntityManager entityManager = UtilTools.getEntityManager();

	final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private static NonIndexedDirectoryDAOImpl instance = null;

	public static NonIndexedDirectoryDAOImpl getInstance() {
		if (instance == null)
			instance = new NonIndexedDirectoryDAOImpl();
		return instance;
	}

	private NonIndexedDirectoryDAOImpl() {}

	@Override
	public List<NonIndexedDirectory> getNonIndexedDirectories(Index index) {
		return null;
	}

	@Override
	public boolean deleteNonIndexedDirectories(Index index) {
		return false;
	}
}
