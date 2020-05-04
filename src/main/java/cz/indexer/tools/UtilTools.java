package cz.indexer.tools;

import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public class UtilTools {

	public final static String PERSISTENCE_UNIT = "indexer";

	public static EntityManager getEntityManager() {
		Map<String, String> persistenceMap = new HashMap<String, String>();
		persistenceMap.put("javax.persistence.jdbc.url", "jdbc:derby:" + resolveDataDirectory() + "/IndexerDB;create=true");

		EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, persistenceMap);
		return managerFactory.createEntityManager();
	}

	public static String resolveDataDirectory() {
		AppDirs appDirs = AppDirsFactory.getInstance();
		return appDirs.getUserDataDir("Indexer", null, "Indexer");
	}
}
