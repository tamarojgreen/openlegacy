/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
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