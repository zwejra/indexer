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

	public void createMetadata(List<Metadata> metadataList) {
		entityManager.getTransaction().begin();
		logger.debug(I18N.getMessage("debug.transaction.started"));

		for (Metadata newMetadata: metadataList) {
			entityManager.persist(newMetadata);
			logger.info(I18N.getMessage("info.transaction.metadata.created", newMetadata));
		}

		entityManager.getTransaction().commit();
		logger.debug(I18N.getMessage("debug.transaction.commited"));

	}

	@Override
	public void deleteMetadata(Metadata metadata) {
		entityManager.getTransaction().begin();
		logger.debug(I18N.getMessage("debug.transaction.started"));

		if (!entityManager.contains(metadata)) {
			metadata = entityManager.merge(metadata);
		}

		entityManager.remove(metadata);
		logger.info(I18N.getMessage("info.transaction.metadata.removed", metadata));

		entityManager.getTransaction().commit();
		logger.debug(I18N.getMessage("debug.transaction.commited"));

	}

	@Override
	public List<Metadata> getAllMetadata() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Metadata> criteriaQuery = criteriaBuilder.createQuery(Metadata.class);
		TypedQuery<Metadata> query = entityManager.createQuery(criteriaQuery);

		return query.getResultList();
	}
}
