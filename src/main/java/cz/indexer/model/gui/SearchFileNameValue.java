package cz.indexer.model.gui;

import cz.indexer.model.enums.NameCondition;
import lombok.Data;

/**
 * Stores String value and NameCondition picked from the Name Filter in the File Search tab.
 */
@Data
public class SearchFileNameValue {

	/**
	 * Search string obtained from the Name filter window.
	 */
	String searchString;

	/**
	 * Condition for filename obtained from the Name filter window.
	 */
	NameCondition nameCondition;

	public SearchFileNameValue(String searchString, NameCondition nameCondition) {
		this.searchString = searchString;
		this.nameCondition = nameCondition;
	}
}
