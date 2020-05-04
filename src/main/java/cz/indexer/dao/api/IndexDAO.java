package cz.indexer.dao.api;

import cz.indexer.model.Index;
import cz.indexer.model.MemoryDevice;

public interface IndexDAO {

	Index getIndex(MemoryDevice memoryDevice);

	boolean createIndex(Index index);

	boolean deleteIndex(Index index);
}
