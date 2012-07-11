/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
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
