package org.openlegacy.db.definitions;

/**
 * @author Ivan Bort
 * 
 */
public interface DbTableDefinition {

	void setName(String name);

	void setCatalog(String catalog);

	void setSchema(String schema);

	String getName();

	String getCatalog();

	String getSchema();
}
