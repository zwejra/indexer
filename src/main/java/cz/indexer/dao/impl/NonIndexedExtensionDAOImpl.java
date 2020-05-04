package cz.indexer.dao.impl;

import cz.indexer.dao.api.NonIndexedExtensionDAO;
import cz.indexer.model.Index;
import cz.indexer.model.NonIndexedExtension;
import cz.indexer.tools.UtilTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

public class NonIndexedExtensionDAOImpl implements NonIndexedExtensionDAO {

	@PersistenceContext(unitName = UtilTools.PERSISTENCE_UNIT)
	EntityManager entityManager = UtilTools.getEntityManager();

	final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private static NonIndexedExtensionDAOImpl instance = null;

	public static NonIndexedExtensionDAOImpl getInstance() {
		if (instance == null)
			instance = new NonIndexedExtensionDAOImpl();
		return instance;
	}

	private NonIndexedExtensionDAOImpl() {}

	@Override
	public List<NonIndexedExtension> getNonIndexedExtensions(Index index) {
		return null;
	}

	@Override
	public boolean deleteNonIndexedExtensions(Index index) {
		return false;
	}
}
