package cz.indexer.tools;

import cz.indexer.model.enums.DateCondition;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;
import org.apache.commons.lang3.ObjectUtils;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.InputMismatchException;
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

	public static LocalDateTime combineDateAndTime(LocalDate localDate, LocalTime localTime, DateCondition dateCondition) throws InputMismatchException {

		if (localTime != null) {
			if (localDate == null) {
				throw new InputMismatchException("Uzivatel vyplnil cas, ale ne datum.");
			}
			return LocalDateTime.of(localDate, localTime);
		}

		if (localDate != null) {
			if (dateCondition.equals(DateCondition.FROM)) return LocalDateTime.of(localDate, LocalTime.MIN);
			else return LocalDateTime.of(localDate, LocalTime.MAX);
		}

		return null;
	}
}
