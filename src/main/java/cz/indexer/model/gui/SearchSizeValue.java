package cz.indexer.model.gui;

import cz.indexer.model.enums.SizeCondition;
import lombok.Data;

@Data
public class SearchSizeValue {

	Long size;
	SizeCondition sizeCondition;

	public SearchSizeValue(Long size, SizeCondition sizeCondition) {
		this.size = size;
		this.sizeCondition = sizeCondition;
	}
}
