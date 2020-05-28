package cz.indexer.managers.api;

import cz.indexer.model.IndexedFile;
import cz.indexer.model.MemoryDevice;
import cz.indexer.model.gui.SearchDateValue;
import cz.indexer.model.gui.SearchFileNameValue;
import cz.indexer.model.gui.SearchSizeValue;
import javafx.collections.ObservableList;

/**
 * Manages file search tab.
 */
public interface FileSearchManager {

	/**
	 * Returns observable list of indexed files, which are result of file search.
	 * These are shown in table in File Search tab.
	 * @return observable list of indexed files
	 */
	ObservableList<IndexedFile> getSearchResults();

	/**
	 * Search indexed files based on conditions and values obtained from the user interface.
	 * @param searchAllDevices if all memory devices should be searched
	 * @param checkedMemoryDevices list of memory devices to search
	 * @param searchFileNameValue object, which stores filename condition and value
	 * @param searchSizeValue object, which stores filesize condition and value
	 * @param creationSearchDateValue object, which stores creation time condition and value
	 * @param lastAccessSearchDateValue object, which stores last access time condition and value
	 * @param lastModifiedSearchDateValue object, which stores last modified time condition and value
	 * @return true if search was successful, false otherwise
	 */
	boolean searchFiles(boolean searchAllDevices, ObservableList<MemoryDevice> checkedMemoryDevices, SearchFileNameValue searchFileNameValue,
						SearchSizeValue searchSizeValue, SearchDateValue creationSearchDateValue, SearchDateValue lastAccessSearchDateValue,
						SearchDateValue lastModifiedSearchDateValue);

}
