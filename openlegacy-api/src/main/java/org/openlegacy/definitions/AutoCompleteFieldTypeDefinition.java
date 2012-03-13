package org.openlegacy.definitions;

import org.openlegacy.RecordsProvider;
import org.openlegacy.Session;

/**
 * Defines an auto complete field type
 * 
 */
public interface AutoCompleteFieldTypeDefinition extends FieldTypeDefinition {

	<S extends Session, T> RecordsProvider<S, T> getRecordsProvider();

	boolean isCollectAll();

	Class<?> getSourceEntityClass();

	/**
	 * For designtime purposes
	 * 
	 * @return
	 */
	String getSourceEntityClassName();

}
