package cz.indexer.dao.api;

import cz.indexer.model.Index;
import cz.indexer.model.IndexedFile;
import cz.indexer.model.MemoryDevice;

import java.util.List;

public interface IndexedFileDAO {

	List<IndexedFile> getFiles(Index index);

	boolean createFile(IndexedFile file);

	boolean createFiles(List<IndexedFile> files);

	boolean deleteFile(IndexedFile file);

	boolean deleteFiles(List<IndexedFile> indexedFiles);

	void deleteFiles(Index index);
}
