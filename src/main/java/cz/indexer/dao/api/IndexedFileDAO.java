package cz.indexer.dao.api;

import cz.indexer.model.Index;
import cz.indexer.model.IndexedFile;
import cz.indexer.model.MemoryDevice;
import cz.indexer.model.gui.SearchDateValue;
import cz.indexer.model.gui.SearchFileNameValue;
import cz.indexer.model.gui.SearchSizeValue;

import java.util.HashMap;
import java.util.List;

/**
 * DAO object for management of IndexedFile entity
 */
public interface IndexedFileDAO {

	/**
	 * Persists files in the database.
	 * @param files list of files to be persisted
	 */
	void createFiles(List<IndexedFile> files);

	/**
	 * Update existing indexed files in the database.
	 * @param indexedFiles list of existing indexed files
	 */
	void updateFiles(List<IndexedFile> indexedFiles);

	/**
	 * Removes indexed files from the database.
	 * @param indexedFiles list of existing indexed files
	 */
	void deleteFiles(List<IndexedFile> indexedFiles);

	/**
	 * Removes all files, which belongs to the selected index.
	 * @param index all files connected to this index will be removed
	 */
	void deleteFiles(Index index);

	/**
	 * Search indexed files based on conditions and values obtained from the user.
	 * @param devicesToSearch memory devices which will be searched
	 * @param searchFileNameValue object, which stores filename condition and value
	 * @param searchSizeValue object, which stores filesize condition and value
	 * @param creationSearchDateValue object, which stores creation time condition and value
	 * @param lastAccessSearchDateValue object, which stores last access time condition and value
	 * @param lastModifiedSearchDateValue object, which stores last modified time condition and value
	 * @return list of file search results
	 */
	List<IndexedFile> searchFiles(List<MemoryDevice> devicesToSearch, SearchFileNameValue searchFileNameValue, SearchSizeValue searchSizeValue,
					 SearchDateValue creationSearchDateValue, SearchDateValue lastAccessSearchDateValue, SearchDateValue lastModifiedSearchDateValue);

	/**
	 * Gets all files (includes directories) in the directory in the selected index.
	 * @param index index of memory device, where the path is located
	 * @param path path to the directory
	 * @param isRoot true if the path is the root/mount directory of the memory device, false otherwise
	 * @return hash map, where key is the name of the file and value is the whole IndexedFile object
	 */
	HashMap<String, IndexedFile> getFilesInDirectory(Index index, String path, boolean isRoot);
}
