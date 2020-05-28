package cz.indexer.model.gui;

import cz.indexer.model.enums.DateCondition;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Stores LocalDateTime value and DateCondition picked from the Date Filter in the File Search tab.
 */
@Data
public class SearchDateValue {

	/**
	 * LocalDateTime obtained from the Date filter window.
	 */
	LocalDateTime dateTime;

	/**
	 * Condition for date obtained from the Date filter window.
	 */
	DateCondition dateCondition;

	public SearchDateValue(LocalDateTime dateTime, DateCondition dateCondition) {
		this.dateTime = dateTime;
		this.dateCondition = dateCondition;
	}
}
