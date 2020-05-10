package cz.indexer.dao.impl;

import cz.indexer.dao.api.IndexedFileDAO;
import cz.indexer.model.Index;
import cz.indexer.model.IndexedFile;
import cz.indexer.model.IndexedFile_;
import cz.indexer.model.MemoryDevice;
import cz.indexer.model.enums.DateCondition;
import cz.indexer.model.enums.NameCondition;
import cz.indexer.model.enums.SizeCondition;
import cz.indexer.model.gui.SearchDateValue;
import cz.indexer.model.gui.SearchFileNameValue;
import cz.indexer.model.gui.SearchSizeValue;
import cz.indexer.tools.UtilTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import javax.persistence.metamodel.SingularAttribute;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
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
	public HashMap<String, IndexedFile> getFilesInDirectory(Index index, String path, boolean isRoot) {
		HashMap<String, IndexedFile> fileMap = new HashMap<>();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<IndexedFile> criteriaQuery = criteriaBuilder.createQuery(IndexedFile.class);

		List<Predicate> andPredicates = new ArrayList<>();

		Root<IndexedFile> root = criteriaQuery.from(IndexedFile.class);
		andPredicates.add(criteriaBuilder.equal(root.get(IndexedFile_.index), index));

		if (!isRoot) {
			Predicate pathLikeConditionWindows = criteriaBuilder.like(root.get(IndexedFile_.path), path + "\\%");
			Predicate pathLikeConditionLinux = criteriaBuilder.like(root.get(IndexedFile_.path), path + "/%");
			Predicate orLike = criteriaBuilder.or(pathLikeConditionWindows, pathLikeConditionLinux);
			andPredicates.add(orLike);
		}

		andPredicates.add(criteriaBuilder.notLike(root.get(IndexedFile_.path), path + "_%/%"));
		andPredicates.add(criteriaBuilder.notLike(root.get(IndexedFile_.path), path + "_%\\%"));

		criteriaQuery.where(andPredicates.toArray(new Predicate[andPredicates.size()]));
		TypedQuery<IndexedFile> query = entityManager.createQuery(criteriaQuery);
		List<IndexedFile> results = query.getResultList();

		for (IndexedFile result: results) {
			if (path.equals(result.getPath())) continue;
			fileMap.put(result.getFileName(), result);
		}

		return fileMap;
	}

	@Override
	public boolean updateFiles(List<IndexedFile> indexedFiles) {
		entityManager.getTransaction().begin();
		logger.debug("Transaction started.");

		for (IndexedFile fileToUpdate: indexedFiles) {
			entityManager.persist(fileToUpdate);
			logger.info("File updated in database. " + fileToUpdate);
		}

		entityManager.getTransaction().commit();
		logger.debug("Transaction commited.");

		return true;
	}

	@Override
	public boolean createFile(IndexedFile file) {
		entityManager.getTransaction().begin();
		logger.debug("Transaction started.");

		entityManager.persist(file);
		logger.info("New file: " + file.toString() + " stored to the database.");

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
			logger.info("New file: " + newFile.toString() + " stored to the database.");
		}

		entityManager.getTransaction().commit();
		entityManager.getEntityManagerFactory().getCache().evictAll();
		logger.debug("Transaction commited.");

		return true;
	}

	@Override
	public boolean deleteFile(IndexedFile file) {
		entityManager.getTransaction().begin();
		logger.debug("Transaction started.");

		entityManager.remove(file);
		logger.info("File: " + file.toString() + " removed from the database.");

		entityManager.getTransaction().commit();
		logger.debug("Transaction commited.");

		return true;
	}

	@Override
	public boolean deleteFiles(List<IndexedFile> files) {
		entityManager.getTransaction().begin();
		logger.debug("Transaction started.");

		for (IndexedFile fileToRemove: files) {
			entityManager.remove(fileToRemove);
			logger.info("File: " + fileToRemove.toString() + " removed from the database.");
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
		Predicate condition = cb.equal(root.get(IndexedFile_.index), index);
		delete.where(condition);
		entityManager.createQuery(delete).executeUpdate();

		entityManager.getTransaction().commit();
	}

	@Override
	public List<IndexedFile> getAllFiles() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<IndexedFile> criteriaQuery = criteriaBuilder.createQuery(IndexedFile.class);
		TypedQuery<IndexedFile> query = entityManager.createQuery(criteriaQuery);

		return query.getResultList();
	}

	@Override
	public List<IndexedFile> searchFiles(List<MemoryDevice> devicesToSearch, SearchFileNameValue searchFileNameValue,
										 SearchSizeValue searchSizeValue, SearchDateValue creationSearchDateValue,
										 SearchDateValue lastAccessSearchDateValue, SearchDateValue lastModifiedSearchDateValue) {
		entityManager.clear();

		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<IndexedFile> criteriaQuery = criteriaBuilder.createQuery(IndexedFile.class);
		Root<IndexedFile> root = criteriaQuery.from(IndexedFile.class);

		List<Predicate> indexPredicates = getPredicatesForIndexes(devicesToSearch, criteriaBuilder, root);
		Predicate orPredicate = criteriaBuilder.or(indexPredicates.toArray(new Predicate[indexPredicates.size()]));

		List<Predicate> andPredicates = new ArrayList<>();
		andPredicates.add(orPredicate);

		if (searchFileNameValue != null) andPredicates.add(getFileNamePredicate(searchFileNameValue, criteriaBuilder, root));
		if (searchSizeValue != null) andPredicates.add(getSizePredicate(searchSizeValue, criteriaBuilder, root));
		if (creationSearchDateValue != null) andPredicates.add(getDatePredicate(creationSearchDateValue, criteriaBuilder, root, IndexedFile_.creationTime));
		if (lastAccessSearchDateValue != null) andPredicates.add(getDatePredicate(lastAccessSearchDateValue, criteriaBuilder, root, IndexedFile_.lastAccessTime));
		if (lastModifiedSearchDateValue != null) andPredicates.add(getDatePredicate(lastModifiedSearchDateValue, criteriaBuilder, root, IndexedFile_.lastModifiedTime));

		criteriaQuery.where(criteriaBuilder.and(andPredicates.toArray(new Predicate[andPredicates.size()])));

		TypedQuery<IndexedFile> query = entityManager.createQuery(criteriaQuery);

		return query.getResultList();
	}

	private Predicate getDatePredicate(SearchDateValue searchDateValue, CriteriaBuilder criteriaBuilder, Root<IndexedFile> root,
									   SingularAttribute<IndexedFile, LocalDateTime> metamodelDateTime) {
		Predicate predicate;

		if (searchDateValue.getDateCondition().equals(DateCondition.FROM)) {
			predicate = criteriaBuilder.greaterThanOrEqualTo(root.get(metamodelDateTime), searchDateValue.getDateTime());
		} else {
			predicate = criteriaBuilder.lessThanOrEqualTo(root.get(metamodelDateTime), searchDateValue.getDateTime());
		}

		return predicate;
	}

	private Predicate getSizePredicate(SearchSizeValue searchSizeValue, CriteriaBuilder criteriaBuilder, Root<IndexedFile> root) {
		Predicate predicate;

		if (searchSizeValue.getSizeCondition().equals(SizeCondition.SMALLER)) {
			predicate = criteriaBuilder.lessThan(root.get(IndexedFile_.fileSize), searchSizeValue.getSize());
		} else if (searchSizeValue.getSizeCondition().equals(SizeCondition.SMALLER_EQUAL)) {
			predicate = criteriaBuilder.lessThanOrEqualTo(root.get(IndexedFile_.fileSize), searchSizeValue.getSize());
		} else if (searchSizeValue.getSizeCondition().equals(SizeCondition.EQUAL)) {
			predicate = criteriaBuilder.equal(root.get(IndexedFile_.fileSize), searchSizeValue.getSize());
		} else if (searchSizeValue.getSizeCondition().equals(SizeCondition.BIGGER)) {
			predicate = criteriaBuilder.greaterThan(root.get(IndexedFile_.fileSize), searchSizeValue.getSize());
		} else {
			predicate = criteriaBuilder.greaterThanOrEqualTo(root.get(IndexedFile_.fileSize), searchSizeValue.getSize());
		}

		return predicate;
	}

	private Predicate getFileNamePredicate(SearchFileNameValue searchFileNameValue, CriteriaBuilder criteriaBuilder, Root<IndexedFile> root) {
		Predicate predicate;

		if (searchFileNameValue.getNameCondition().equals(NameCondition.BEGINS_WITH)) {
			predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(IndexedFile_.fileName)),
					searchFileNameValue.getSearchString().toLowerCase() + "%");
		} else if (searchFileNameValue.getNameCondition().equals(NameCondition.ENDS_WITH)) {
			predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(IndexedFile_.fileName)),
					"%" + searchFileNameValue.getSearchString().toLowerCase());
		} else if (searchFileNameValue.getNameCondition().equals(NameCondition.CONTAINS)) {
			predicate = criteriaBuilder.like(criteriaBuilder.lower(root.get(IndexedFile_.fileName)),
					"%" + searchFileNameValue.getSearchString().toLowerCase() + "%");
		} else {
			predicate = criteriaBuilder.notLike(criteriaBuilder.lower(root.get(IndexedFile_.fileName)),
					"%" + searchFileNameValue.getSearchString().toLowerCase() + "%");
		}

		return predicate;
	}

	private List<Predicate> getPredicatesForIndexes(List<MemoryDevice> devicesToSearch, CriteriaBuilder criteriaBuilder, Root<IndexedFile> root) {
		List<Predicate> indexPredicates = new ArrayList<>();

		for (MemoryDevice memoryDevice: devicesToSearch) {
			if (memoryDevice != null) {
				indexPredicates.add(criteriaBuilder.equal(root.get(IndexedFile_.index), memoryDevice.	getIndex()));
			}
		}

		return indexPredicates;
	}
}
