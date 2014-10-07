package org.openlegacy.db.definitions;

import java.util.List;

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

	List<UniqueConstraintDefinition> getUniqueConstraints();

	public interface UniqueConstraintDefinition {

		List<String> getColumnNames();

		String getName();

	}
}
