package org.openlegacy.db.definitions;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class SimpleDbTableDefinition implements DbTableDefinition {

	private String name = "";
	private String catalog = "";
	private String schema = "";
	private List<UniqueConstraintDefinition> uniqueConstraints = new ArrayList<UniqueConstraintDefinition>();

	public SimpleDbTableDefinition() {}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCatalog() {
		return catalog;
	}

	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	public String getSchema() {
		return schema;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public List<UniqueConstraintDefinition> getUniqueConstraints() {
		return uniqueConstraints;
	}

	public void setUniqueConstraints(List<UniqueConstraintDefinition> uniqueConstraints) {
		this.uniqueConstraints = uniqueConstraints;
	}

}
