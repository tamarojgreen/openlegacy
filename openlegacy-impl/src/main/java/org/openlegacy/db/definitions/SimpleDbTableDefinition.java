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

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getCatalog() {
		return catalog;
	}

	@Override
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}

	@Override
	public String getSchema() {
		return schema;
	}

	@Override
	public void setSchema(String schema) {
		this.schema = schema;
	}

	@Override
	public List<UniqueConstraintDefinition> getUniqueConstraints() {
		return uniqueConstraints;
	}

	public void setUniqueConstraints(List<UniqueConstraintDefinition> uniqueConstraints) {
		this.uniqueConstraints = uniqueConstraints;
	}

}
