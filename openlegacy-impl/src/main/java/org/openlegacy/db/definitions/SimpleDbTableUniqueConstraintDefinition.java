package org.openlegacy.db.definitions;

import org.openlegacy.db.definitions.DbTableDefinition.UniqueConstraintDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class SimpleDbTableUniqueConstraintDefinition implements UniqueConstraintDefinition {

	private List<String> columnNames = new ArrayList<String>();
	private String name = "";

	public SimpleDbTableUniqueConstraintDefinition() {}

	public SimpleDbTableUniqueConstraintDefinition(List<String> columnNames, String name) {
		this.columnNames = columnNames;
		this.name = name;
	}

	public void setColumnNames(List<String> columnNames) {
		this.columnNames = columnNames;
	}

	public void setName(String name) {
		this.name = name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.db.definitions.DbTableDefinition.UniqueConstraintDefinition#getColumnNames()
	 */
	@Override
	public List<String> getColumnNames() {
		return columnNames;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.db.definitions.DbTableDefinition.UniqueConstraintDefinition#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

}
