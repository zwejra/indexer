package cz.indexer.dao.impl;

import cz.indexer.dao.api.IndexDAO;
import cz.indexer.model.Index;
import cz.indexer.model.MemoryDevice;
import cz.indexer.tools.UtilTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

public class IndexDAOImpl implements IndexDAO {

	@PersistenceContext(unitName = UtilTools.PERSISTENCE_UNIT)
	EntityManager entityManager = UtilTools.getEntityManager();

	final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private static IndexDAOImpl instance = null;

	public static IndexDAOImpl getInstance() {
		if (instance == null)
			instance = new IndexDAOImpl();
		return instance;
	}

	private IndexDAOImpl() {}

	@Override
	public Index getIndex(MemoryDevice memoryDevice) {
/*		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Index> criteriaQuery = criteriaBuilder.createQuery(Index.class);

		Root<Index> root = criteriaQuery.from(Index.class);
		Predicate condition = criteriaBuilder.equal(root.get(Index_.), memoryDevice.getIndex());
		criteriaQuery.where(condition);
		TypedQuery<MemoryDevice> query = entityManager.createQuery(criteriaQuery);

		return query.getResultList().get(0);*/
		return null;
	}

	@Override
	public boolean createIndex(Index index) {
		return false;
	}

	@Override
	public boolean deleteIndex(Index index) {
		return false;
	}
}
