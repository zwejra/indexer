package cz.indexer.managers.impl;

import cz.indexer.dao.api.IndexedFileDAO;
import cz.indexer.dao.api.MetadataDAO;
import cz.indexer.dao.impl.IndexedFileDAOImpl;
import cz.indexer.dao.impl.MetadataDAOImpl;
import cz.indexer.managers.api.FileSearchManager;
import cz.indexer.managers.api.MemoryDeviceManager;
import cz.indexer.model.IndexedFile;
import cz.indexer.model.MemoryDevice;
import cz.indexer.model.gui.SearchDateValue;
import cz.indexer.model.gui.SearchFileNameValue;
import cz.indexer.model.gui.SearchSizeValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

public class FileSearchManagerImpl implements FileSearchManager {

	private static FileSearchManagerImpl instance = null;

	private ObservableList<IndexedFile> searchResults = FXCollections.observableArrayList();

	private MetadataDAO metadataDAO = MetadataDAOImpl.getInstance();

	private IndexedFileDAO indexedFileDAO = IndexedFileDAOImpl.getInstance();

	private MemoryDeviceManager memoryDeviceManager = MemoryDeviceManagerImpl.getInstance();

	public static FileSearchManagerImpl getInstance() {
		if (instance == null)
			instance = new FileSearchManagerImpl();
		return instance;
	}

	private FileSearchManagerImpl() {}

	@Override
	public ObservableList<IndexedFile> getSearchResults() {
		return searchResults;
	}

	@Override
	public boolean searchFiles(boolean searchAllDevices, ObservableList<MemoryDevice> checkedMemoryDevices,
							   SearchFileNameValue searchFileNameValue, SearchSizeValue searchSizeValue,
							   SearchDateValue creationSearchDateValue, SearchDateValue lastAccessSearchDateValue,
							   SearchDateValue lastModifiedSearchDateValue) {

		searchResults.clear();

		List<MemoryDevice> devicesToSearch = new ArrayList<>();
		if (searchAllDevices) {
			devicesToSearch.addAll(memoryDeviceManager.getIndexedMemoryDevices());
		} else {
			devicesToSearch.addAll(checkedMemoryDevices);
		}

		searchResults.addAll(indexedFileDAO.searchFiles(devicesToSearch, searchFileNameValue, searchSizeValue, creationSearchDateValue,
				lastAccessSearchDateValue, lastModifiedSearchDateValue));

		return true;
	}
}
