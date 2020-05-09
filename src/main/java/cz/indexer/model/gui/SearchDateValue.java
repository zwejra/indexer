package cz.indexer.model.gui;

import cz.indexer.model.enums.DateCondition;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchDateValue {

	LocalDateTime dateTime;
	DateCondition dateCondition;

	public SearchDateValue(LocalDateTime dateTime, DateCondition dateCondition) {
		this.dateTime = dateTime;
		this.dateCondition = dateCondition;
	}
}
