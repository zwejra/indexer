package cz.indexer.managers.api;

import cz.indexer.model.IndexedFile;
import cz.indexer.model.MemoryDevice;
import cz.indexer.model.gui.SearchDateValue;
import cz.indexer.model.gui.SearchFileNameValue;
import cz.indexer.model.gui.SearchSizeValue;
import javafx.collections.ObservableList;

public interface FileSearchManager {

	ObservableList<IndexedFile> getSearchResults();

	boolean searchFiles(boolean searchAllDevices, ObservableList<MemoryDevice> checkedMemoryDevices, SearchFileNameValue searchFileNameValue,
						SearchSizeValue searchSizeValue, SearchDateValue creationSearchDateValue, SearchDateValue lastAccessSearchDateValue,
						SearchDateValue lastModifiedSearchDateValue);

}
