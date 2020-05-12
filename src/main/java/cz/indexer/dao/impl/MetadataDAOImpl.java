package cz.indexer.dao.impl;

import cz.indexer.dao.api.MetadataDAO;
import cz.indexer.model.Metadata;
import cz.indexer.model.Metadata_;
import cz.indexer.tools.I18N;
import cz.indexer.tools.UtilTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

public class MetadataDAOImpl implements MetadataDAO {

	private static EntityManager entityManager = UtilTools.getEntityManager();

	final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private static MetadataDAOImpl instance = null;

	public static MetadataDAOImpl getInstance() {
		if (instance == null) {
			instance = new MetadataDAOImpl();
		}
		return instance;
	}

	private MetadataDAOImpl() {}

	@Override
	public boolean createMetadata(Metadata metadata) {
		entityManager.getTransaction().begin();
		logger.debug(I18N.getMessage("debug.transaction.started"));

		entityManager.persist(metadata);
		logger.info(I18N.getMessage("info.transaction.metadata.created", metadata));

		entityManager.getTransaction().commit();
		logger.debug(I18N.getMessage("debug.transaction.commited"));

		return true;
	}

	public boolean createMetadata(List<Metadata> metadataList) {
		entityManager.getTransaction().begin();
		logger.debug(I18N.getMessage("debug.transaction.started"));

		for (Metadata newMetadata: metadataList) {
			entityManager.persist(newMetadata);
			logger.info(I18N.getMessage("info.transaction.metadata.created", newMetadata));
		}

		entityManager.getTransaction().commit();
		logger.debug(I18N.getMessage("debug.transaction.commited"));

		return true;
	}

	@Override
	public boolean deleteMetadata(Metadata metadata) {
		entityManager.getTransaction().begin();
		logger.debug(I18N.getMessage("debug.transaction.started"));

		if (!entityManager.contains(metadata)) {
			metadata = entityManager.merge(metadata);
		}

		entityManager.remove(metadata);
		logger.info(I18N.getMessage("info.transaction.metadata.removed", metadata));

		entityManager.getTransaction().commit();
		logger.debug(I18N.getMessage("debug.transaction.commited"));

		return true;
	}

	@Override
	public boolean deleteMetadata(List<Metadata> metadata) {
		entityManager.getTransaction().begin();
		logger.debug(I18N.getMessage("debug.transaction.started"));

		if (!entityManager.contains(metadata)) {
			metadata = entityManager.merge(metadata);
		}

		for (Metadata removedMetadata: metadata) {
			entityManager.remove(removedMetadata);
			logger.info(I18N.getMessage("info.transaction.metadata.removed", removedMetadata));
		}

		entityManager.getTransaction().commit();
		logger.debug(I18N.getMessage("debug.transaction.commited"));

		return true;
	}

	@Override
	public List<Metadata> getAllMetadata() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Metadata> criteriaQuery = criteriaBuilder.createQuery(Metadata.class);
		TypedQuery<Metadata> query = entityManager.createQuery(criteriaQuery);

		return query.getResultList();
	}

	@Override
	public Metadata getMetadata(String name) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Metadata> criteriaQuery = criteriaBuilder.createQuery(Metadata.class);

		Root<Metadata> root = criteriaQuery.from(Metadata.class);
		Predicate condition = criteriaBuilder.equal(root.get(Metadata_.name), name);
		criteriaQuery.where(condition);
		TypedQuery<Metadata> query = entityManager.createQuery(criteriaQuery);

		return query.getResultList().get(0);
	}
}
