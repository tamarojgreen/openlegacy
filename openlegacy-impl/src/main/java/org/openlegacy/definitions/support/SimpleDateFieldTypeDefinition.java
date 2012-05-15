package org.openlegacy.definitions.support;

import org.openlegacy.definitions.DateFieldTypeDefinition;

public class SimpleDateFieldTypeDefinition implements DateFieldTypeDefinition {

	private Integer dayColumn;
	private Integer monthColumn;
	private Integer yearColumn;

	public SimpleDateFieldTypeDefinition() {}

	public SimpleDateFieldTypeDefinition(Integer dayColumn, Integer monthColumn, Integer yearColumn) {
		this.dayColumn = dayColumn;
		this.monthColumn = monthColumn;
		this.yearColumn = yearColumn;
	}

	public String getTypeName() {
		return "date";
	}

	public Integer getYearColumn() {
		return yearColumn;
	}

	public Integer getMonthColumn() {
		return monthColumn;
	}

	public Integer getDayColumn() {
		return dayColumn;
	}

}
