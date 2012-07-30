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

import org.openlegacy.definitions.TableDefinition.ColumnDefinition;

import java.util.List;

/**
 * Defines a table registry information. Stores all information related to a screen table: class, name, columns, keys, max rows
 * count, actions
 * 
 * @author Roi Mor
 * 
 * @param <C>
 *            Column class definition
 */
public interface TableDefinition<C extends ColumnDefinition> {

	Class<?> getTableClass();

	String getTableEntityName();

	List<C> getColumnDefinitions();

	C getColumnDefinition(String fieldName);

	List<String> getKeyFieldNames();

	String getMainDisplayField();

	int getMaxRowsCount();

	boolean isScrollable();

	List<ActionDefinition> getActions();

	public interface ColumnDefinition extends Comparable<ColumnDefinition> {

		String getName();

		boolean isKey();

		boolean isEditable();

		String getDisplayName();

		String getSampleValue();

		Class<?> getJavaType();
	}

}
