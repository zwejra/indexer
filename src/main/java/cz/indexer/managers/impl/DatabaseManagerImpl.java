package cz.indexer.managers.impl;

import cz.indexer.dao.api.MetadataDAO;
import cz.indexer.dao.impl.MetadataDAOImpl;
import cz.indexer.managers.api.DatabaseManager;
import cz.indexer.model.Metadata;
import cz.indexer.model.enums.FileType;
import cz.indexer.model.enums.OptionalMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManagerImpl implements DatabaseManager {

	final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	MetadataDAO metadataDAO = MetadataDAOImpl.getInstance();

	private static DatabaseManagerImpl instance = null;

	public static DatabaseManagerImpl getInstance() {
		if (instance == null)
			instance = new DatabaseManagerImpl();
		return instance;
	}

	private DatabaseManagerImpl() {}

	public void initializeDatabase() {
		// Get all optional metadata from the database
		List<Metadata> existingMetadata = metadataDAO.getAllMetadata();

		for (Metadata metadata: existingMetadata) {
			logger.info("Existing metadata: " + metadata.toString());
		}

		List<Metadata> newMetadata = new ArrayList<>();
		// Create all optional metadata if they don't exist
		for (OptionalMetadata optionalMetadata: OptionalMetadata.values()) {
			Metadata possibleMetadata = new Metadata(optionalMetadata);

			if (!doesMetadataAlreadyExist(existingMetadata, possibleMetadata)) {
				newMetadata.add(possibleMetadata);
			}
		}

		// Save to the database
		if (!newMetadata.isEmpty()) {
			metadataDAO.createMetadata(newMetadata);
		}
	}

	private boolean doesMetadataAlreadyExist(List<Metadata> existingMetadata, Metadata possibleMetadata) {
		for (Metadata m: existingMetadata) {
			if (m.equals(possibleMetadata)) {
				return true;
			}
		}
		return false;
	}
}
