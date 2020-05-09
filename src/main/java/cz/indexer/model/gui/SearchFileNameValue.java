package cz.indexer.model.gui;

import cz.indexer.model.enums.NameCondition;
import lombok.Data;

@Data
public class SearchFileNameValue {

	String searchString;
	NameCondition nameCondition;

	public SearchFileNameValue(String searchString, NameCondition nameCondition) {
		this.searchString = searchString;
		this.nameCondition = nameCondition;
	}
}
