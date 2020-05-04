package cz.indexer.dao.impl;

import cz.indexer.dao.api.MemoryDeviceDAO;
import cz.indexer.model.MemoryDevice;
import cz.indexer.model.metamodel.MemoryDevice_;
import cz.indexer.tools.UtilTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.HashMap;
import java.util.List;

public class MemoryDeviceDAOImpl implements MemoryDeviceDAO {

	@PersistenceContext(unitName = UtilTools.PERSISTENCE_UNIT)
	EntityManager entityManager = UtilTools.getEntityManager();

	final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private static MemoryDeviceDAOImpl instance = null;

	public static MemoryDeviceDAOImpl getInstance() {
		if (instance == null)
			instance = new MemoryDeviceDAOImpl();
		return instance;
	}

	private MemoryDeviceDAOImpl() {}

	public HashMap<String, MemoryDevice> getAllMemoryDevices() {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MemoryDevice> criteriaQuery = criteriaBuilder.createQuery(MemoryDevice.class);
		TypedQuery<MemoryDevice> query = entityManager.createQuery(criteriaQuery);
		List<MemoryDevice> allMemoryDevices = query.getResultList();

		HashMap<String, MemoryDevice> mapWithAllDevices = new HashMap<>();
		for (MemoryDevice memoryDevice: allMemoryDevices) {
			mapWithAllDevices.put(memoryDevice.getUuid(), memoryDevice);
		}

		return mapWithAllDevices;
	}

	@Override
	public MemoryDevice getMemoryDevice(String uuid) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<MemoryDevice> criteriaQuery = criteriaBuilder.createQuery(MemoryDevice.class);

		Root<MemoryDevice> root = criteriaQuery.from(MemoryDevice.class);
		Predicate condition = criteriaBuilder.equal(root.get(MemoryDevice_.uuid), uuid);
		criteriaQuery.where(condition);
		TypedQuery<MemoryDevice> query = entityManager.createQuery(criteriaQuery);

		return query.getResultList().get(0);
	}

	@Override
	public boolean createMemoryDevice(MemoryDevice memoryDevice) {
		entityManager.getTransaction().begin();
		logger.info("Transaction started.");

		entityManager.persist(memoryDevice);
		logger.info("New memory device: " + memoryDevice.toString() + " stored to the database.");

		entityManager.getTransaction().commit();
		logger.info("Transaction commited.");

		return true;
	}

	@Override
	public boolean deleteMemoryDevice(MemoryDevice memoryDevice) {
		entityManager.getTransaction().begin();
		logger.info("Transaction started.");

		if (!entityManager.contains(memoryDevice)) {
			memoryDevice = entityManager.merge(memoryDevice);
		}

		entityManager.remove(memoryDevice);
		logger.info("Memory device: " + memoryDevice.toString() + " removed from the database.");

		entityManager.getTransaction().commit();
		logger.info("Transaction commited.");

		return true;
	}
}
