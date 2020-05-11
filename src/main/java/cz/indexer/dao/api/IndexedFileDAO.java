package cz.indexer.dao.api;

import cz.indexer.model.Index;
import cz.indexer.model.IndexedFile;
import cz.indexer.model.MemoryDevice;
import cz.indexer.model.gui.SearchDateValue;
import cz.indexer.model.gui.SearchFileNameValue;
import cz.indexer.model.gui.SearchSizeValue;

import java.util.HashMap;
import java.util.List;

public interface IndexedFileDAO {

	boolean createFiles(List<IndexedFile> files);

	boolean updateFiles(List<IndexedFile> indexedFiles);

	boolean deleteFiles(List<IndexedFile> indexedFiles);

	void deleteFiles(Index index);

	List<IndexedFile> searchFiles(List<MemoryDevice> devicesToSearch, SearchFileNameValue searchFileNameValue, SearchSizeValue searchSizeValue,
					 SearchDateValue creationSearchDateValue, SearchDateValue lastAccessSearchDateValue, SearchDateValue lastModifiedSearchDateValue);

	HashMap<String, IndexedFile> getFilesInDirectory(Index index, String path, boolean isRoot);
}
