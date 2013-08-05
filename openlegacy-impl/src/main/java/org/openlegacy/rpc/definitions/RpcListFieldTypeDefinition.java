package org.openlegacy.rpc.definitions;

/**
 *  Extending ListFieldTypeDefinition with type definition of the objects in the list.  
 *  all objects are the same definition.
 */

import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.definitions.ListFieldTypeDefinition;

public interface RpcListFieldTypeDefinition extends ListFieldTypeDefinition {

	FieldTypeDefinition getItemTypeDefinition();

	Class<?> getItemJavaType();

	String getItemJavaName();

}