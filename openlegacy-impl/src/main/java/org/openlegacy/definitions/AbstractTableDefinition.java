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
package org.openlegacy.definitions;

import org.openlegacy.definitions.TableDefinition.ColumnDefinition;
import org.openlegacy.exceptions.RegistryException;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractTableDefinition<C extends ColumnDefinition> implements TableDefinition<C>, Serializable {

	private static final long serialVersionUID = 1L;

	private Class<?> rowClass;

	private List<C> columnDefinitions = new ArrayList<C>();

	private boolean scrollable = true;

	private String tableEntityName;

	private List<String> mainDisplayFields = new ArrayList<String>();

	private List<ActionDefinition> actions = new ArrayList<ActionDefinition>();

	private ActionDefinition defaultAction;

	private int rowGaps;

	private List<String> keyFields = null;

	public AbstractTableDefinition(Class<?> rowClass) {
		this.rowClass = rowClass;
	}

	@Override
	public Class<?> getTableClass() {
		return rowClass;
	}

	@Override
	public List<C> getColumnDefinitions() {
		return columnDefinitions;
	}

	@Override
	public List<String> getKeyFieldNames() {
		if (keyFields == null) {
			keyFields = getNewKeyFieldNames();
		}

		return keyFields;
	}

	public List<String> getNewKeyFieldNames() {
		List<String> keyFields = new ArrayList<String>();
		for (C columnDefinition : columnDefinitions) {
			if (columnDefinition.isKey()) {
				keyFields.add(columnDefinition.getName());
			}
		}
		return keyFields;
	}

	@Override
	public C getColumnDefinition(String fieldName) {
		for (C columnDefinition : columnDefinitions) {
			if (columnDefinition.getName().equals(fieldName)) {
				return columnDefinition;
			}
		}
		return null;
	}

	@Override
	public boolean isScrollable() {
		return scrollable;
	}

	public void setScrollable(boolean scrollable) {
		this.scrollable = scrollable;
	}

	@Override
	public String getTableEntityName() {
		return tableEntityName;
	}

	public void setTableEntityName(String tableEntityName) {
		this.tableEntityName = tableEntityName;
	}

	@Override
	public List<String> getMainDisplayFields() {
		return mainDisplayFields;
	}

	@Override
	public List<ActionDefinition> getActions() {
		return actions;
	}

	@Override
	public ActionDefinition getDefaultAction() {
		if (defaultAction != null) {
			return defaultAction;
		}
		for (ActionDefinition action : actions) {
			if (action.isDefaultAction()) {
				defaultAction = action;
				return defaultAction;
			}
		}
		throw (new RegistryException(MessageFormat.format("No default table action was defined for table: {0}",
				getTableEntityName())));

	}

	public int getRowGaps() {
		return rowGaps;
	}

	public void setRowsGap(int rowGaps) {
		this.rowGaps = rowGaps;
	}

	@Override
	public List<String> getSortedByFieldNames() {
		// copy the list for sorting
		List<ColumnDefinition> columns = new ArrayList<ColumnDefinition>(getColumnDefinitions());
		Collections.sort(columns, new Comparator<ColumnDefinition>() {

			@Override
			public int compare(org.openlegacy.definitions.TableDefinition.ColumnDefinition column1,
					org.openlegacy.definitions.TableDefinition.ColumnDefinition column2) {
				return column1.getSortIndex() - column2.getSortIndex();
			}
		});

		List<String> sortedByFieldNames = new ArrayList<String>();
		for (ColumnDefinition columnDefinition : columns) {
			if (columnDefinition.getSortIndex() >= 0) {
				sortedByFieldNames.add(columnDefinition.getName());
			}
		}
		if (sortedByFieldNames.size() == 0) {
			sortedByFieldNames.addAll(getKeyFieldNames());
		}
		return sortedByFieldNames;
	}

}
