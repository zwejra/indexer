package cz.indexer.dao.impl;

import cz.indexer.dao.api.IndexedFileDAO;
import cz.indexer.model.Index;
import cz.indexer.model.IndexedFile;
import cz.indexer.model.metamodel.IndexedFile_;
import cz.indexer.tools.UtilTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.List;

public class IndexedFileDAOImpl implements IndexedFileDAO {

	@PersistenceContext(unitName = UtilTools.PERSISTENCE_UNIT)
	EntityManager entityManager = UtilTools.getEntityManager();

	final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private static IndexedFileDAOImpl instance = null;

	public static IndexedFileDAOImpl getInstance() {
		if (instance == null)
			instance = new IndexedFileDAOImpl();
		return instance;
	}

	private IndexedFileDAOImpl() {}

	@Override
	public List<IndexedFile> getFiles(Index index) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<IndexedFile> criteriaQuery = criteriaBuilder.createQuery(IndexedFile.class);

		Root<IndexedFile> root = criteriaQuery.from(IndexedFile.class);
		Predicate condition = criteriaBuilder.equal(root.get(IndexedFile_.index), index);
		criteriaQuery.where(condition);
		TypedQuery<IndexedFile> query = entityManager.createQuery(criteriaQuery);

		return query.getResultList();
	}

	@Override
	public boolean createFile(IndexedFile file) {
		entityManager.getTransaction().begin();
		logger.debug("Transaction started.");

		entityManager.persist(file);
		logger.debug("New file: " + file.toString() + " stored to the database.");

		entityManager.getTransaction().commit();
		logger.debug("Transaction commited.");

		return true;
	}

	@Override
	public boolean createFiles(List<IndexedFile> files) {
		entityManager.getTransaction().begin();
		logger.debug("Transaction started.");

		for (IndexedFile newFile: files) {
			entityManager.persist(newFile);
			logger.debug("New file: " + newFile.toString() + " stored to the database.");
		}

		entityManager.getTransaction().commit();
		logger.debug("Transaction commited.");

		return true;
	}

	@Override
	public boolean deleteFile(IndexedFile file) {
		entityManager.getTransaction().begin();
		logger.debug("Transaction started.");

		if (!entityManager.contains(file)) {
			file = entityManager.merge(file);
		}

		entityManager.remove(file);
		logger.debug("File: " + file.toString() + " removed from the database.");

		entityManager.getTransaction().commit();
		logger.debug("Transaction commited.");

		return true;
	}

	@Override
	public boolean deleteFiles(List<IndexedFile> files) {
		entityManager.getTransaction().begin();
		logger.debug("Transaction started.");

		if (!entityManager.contains(files)) {
			files = entityManager.merge(files);
		}

		for (IndexedFile removedFile: files) {
			entityManager.remove(removedFile);
			logger.debug("File: " + removedFile.toString() + " removed from the database.");
		}

		entityManager.getTransaction().commit();
		logger.debug("Transaction commited.");

		return true;
	}

	@Override
	public void deleteFiles(Index index) {
		entityManager.getTransaction().begin();

		CriteriaBuilder cb = entityManager.getCriteriaBuilder();
		CriteriaDelete<IndexedFile> delete = cb.createCriteriaDelete(IndexedFile.class);

		Root<IndexedFile> root = delete.from(IndexedFile.class);
		Predicate condition = cb.equal(root.get("index"), index);
		delete.where(condition);
		entityManager.createQuery(delete).executeUpdate();

		entityManager.getTransaction().commit();
	}
}
