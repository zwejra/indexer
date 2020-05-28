package cz.indexer.model.gui;

import cz.indexer.model.enums.SizeCondition;
import lombok.Data;

/**
 * Stores Long value and SizeCondition picked from the Size Filter in the File Search tab.
 */
@Data
public class SearchSizeValue {

	/**
	 * Size value picked from the Size Filter window.
	 */
	Long size;

	/**
	 * Size condition picked from the Size Filter window.
	 */
	SizeCondition sizeCondition;

	public SearchSizeValue(Long size, SizeCondition sizeCondition) {
		this.size = size;
		this.sizeCondition = sizeCondition;
	}
}
