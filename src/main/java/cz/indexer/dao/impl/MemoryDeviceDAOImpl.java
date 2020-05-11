package cz.indexer.dao.impl;

import cz.indexer.dao.api.MemoryDeviceDAO;
import cz.indexer.model.MemoryDevice;
import cz.indexer.tools.I18N;
import cz.indexer.tools.UtilTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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

	@Override
	public boolean createMemoryDevice(MemoryDevice memoryDevice) {
		entityManager.getTransaction().begin();
		logger.debug(I18N.getMessage("debug.transaction.started"));

		entityManager.persist(memoryDevice);
		logger.info(I18N.getMessage("info.transaction.memory.device.created", memoryDevice));

		entityManager.getTransaction().commit();
		logger.debug(I18N.getMessage("debug.transaction.commited"));

		return true;
	}

	@Override
	public boolean updateMemoryDevice(MemoryDevice memoryDevice) {
		entityManager.getTransaction().begin();
		logger.debug(I18N.getMessage("debug.transaction.started"));

		entityManager.persist(memoryDevice);
		logger.info(I18N.getMessage("info.transaction.memory.device.updated", memoryDevice));

		entityManager.getTransaction().commit();
		logger.debug(I18N.getMessage("debug.transaction.commited"));

		return true;
	}

	@Override
	public boolean deleteMemoryDevice(MemoryDevice memoryDevice) {
		entityManager.getTransaction().begin();
		logger.debug(I18N.getMessage("debug.transaction.started"));

		if (!entityManager.contains(memoryDevice)) {
			memoryDevice = entityManager.merge(memoryDevice);
		}

		entityManager.remove(memoryDevice);
		logger.info(I18N.getMessage("info.transaction.memory.device.removed", memoryDevice));

		entityManager.getTransaction().commit();
		logger.debug(I18N.getMessage("debug.transaction.commited"));

		return true;
	}

	@Override
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
}
