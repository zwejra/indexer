package cz.indexer.managers.impl;

import cz.indexer.managers.api.FileSearchManager;

public class FileSearchManagerImpl implements FileSearchManager {

	private static FileSearchManagerImpl instance = null;

	public static FileSearchManagerImpl getInstance() {
		if (instance == null)
			instance = new FileSearchManagerImpl();
		return instance;
	}

	private FileSearchManagerImpl() {}

}
