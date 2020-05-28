package cz.indexer.tools;

import cz.indexer.model.enums.DateCondition;
import net.harawata.appdirs.AppDirs;
import net.harawata.appdirs.AppDirsFactory;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Map;

/**
 * Tools which couldn't be part of program hierarchy.
 */
public class UtilTools {

	/**
	 * Name of persistence unit in persistence.xml file.
	 */
	public final static String PERSISTENCE_UNIT = "indexer";

	/**
	 * Returns newly created entity manager with dynamically created path
	 * to the program data based on the current operating system.
	 * @return newly created entity manager
	 */
	public static EntityManager getEntityManager() {
		Map<String, String> persistenceMap = new HashMap<String, String>();
		persistenceMap.put("javax.persistence.jdbc.url", "jdbc:derby:" + resolveDataDirectory() + "/IndexerDB;create=true");

		EntityManagerFactory managerFactory = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, persistenceMap);
		return managerFactory.createEntityManager();
	}

	/**
	 * Resolves which operating system runs the program and based on that returns path to the program data folder.
	 * @return path to the program data folder
	 */
	public static String resolveDataDirectory() {
		AppDirs appDirs = AppDirsFactory.getInstance();
		return appDirs.getUserDataDir("Indexer", null, "Indexer");
	}

	/**
	 * Returns combined date and time from the date filter in file search tab.
	 * Checks if the input is valid.
	 * @param localDate date obtained from JFXDatePicker
	 * @param localTime time obtained from JFXTimePicker
	 * @param dateCondition date condition picked from the combobox (before or after)
	 * @return combined LocalDateTime object
	 * @throws InputMismatchException if date is null, but time isn't, the InputMismatchException is thrown
	 */
	public static LocalDateTime combineDateAndTime(LocalDate localDate, LocalTime localTime, DateCondition dateCondition) throws InputMismatchException {

		if (localTime != null) {
			if (localDate == null) {
				throw new InputMismatchException(I18N.getMessage("exception.time.filled.not.date"));
			}
			return LocalDateTime.of(localDate, localTime);
		}

		if (localDate != null) {
			if (dateCondition.equals(DateCondition.BEFORE)) return LocalDateTime.of(localDate, LocalTime.MIN);
			else return LocalDateTime.of(localDate, LocalTime.MAX);
		}

		return null;
	}
}
