package org.openlegacy.db.definitions;

/**
 * @author Ivan Bort
 * 
 */
public class SimpleDbTableDefinition implements DbTableDefinition {

	private String name = "";
	private String catalog = "";
	private String schema = "";

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

}
