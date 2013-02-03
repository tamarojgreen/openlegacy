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
package org.openlegacy.terminal.definitions;

import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.exceptions.RegistryException;
import org.openlegacy.modules.table.TableCollector;
import org.openlegacy.modules.table.drilldown.RowComparator;
import org.openlegacy.modules.table.drilldown.RowFinder;
import org.openlegacy.modules.table.drilldown.RowSelector;
import org.openlegacy.modules.table.drilldown.TableDrilldownPerformer;
import org.openlegacy.modules.table.drilldown.TableScrollStopConditions;
import org.openlegacy.modules.table.drilldown.TableScroller;
import org.openlegacy.terminal.PositionedPart;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.modules.table.ScrollableTableUtil;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class SimpleScreenTableDefinition implements ScreenTableDefinition, PositionedPart, Serializable {

	private static final long serialVersionUID = 1L;

	private Class<?> rowClass;

	private int startRow;
	private int endRow;
	private List<ScreenColumnDefinition> columnDefinitions = new ArrayList<ScreenColumnDefinition>();

	private TerminalAction nextScreenAction;
	private TerminalAction previousScreenAction;

	private DrilldownDefinition drilldownDefinition = new SimpleDrilldownDefinition();

	private boolean scrollable = true;

	private Class<? extends TableCollector> tableCollectorClass;

	private String tableEntityName;

	private String mainDisplayField;

	private List<ActionDefinition> actions = new ArrayList<ActionDefinition>();

	private TerminalPosition partPosition;

	private int width;

	private ActionDefinition defaultAction;

	private int rowGaps;

	public SimpleScreenTableDefinition(Class<?> rowClass) {
		this.rowClass = rowClass;
	}

	public Class<?> getTableClass() {
		return rowClass;
	}

	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public List<ScreenColumnDefinition> getColumnDefinitions() {
		return columnDefinitions;
	}

	public TerminalAction getNextScreenAction() {
		return nextScreenAction;
	}

	public void setNextScreenAction(TerminalAction nextScreenAction) {
		this.nextScreenAction = nextScreenAction;
	}

	public TerminalAction getPreviousScreenAction() {
		return previousScreenAction;
	}

	public void setPreviousScreenAction(TerminalAction previousScreenAction) {
		this.previousScreenAction = previousScreenAction;
	}

	public List<String> getKeyFieldNames() {
		List<String> keyFields = new ArrayList<String>();
		for (ScreenColumnDefinition columnDefinition : columnDefinitions) {
			if (columnDefinition.isKey()) {
				keyFields.add(columnDefinition.getName());
			}
		}
		return keyFields;
	}

	public ScreenColumnDefinition getSelectionColumn() {
		for (ScreenColumnDefinition columnDefinition : columnDefinitions) {
			if (columnDefinition.isSelectionField()) {
				return columnDefinition;
			}
		}
		return null;
	}

	public int getMaxRowsCount() {
		// TODO handle (future) gaps
		return getEndRow() - getStartRow() + 1;
	}

	public ScreenColumnDefinition getColumnDefinition(String fieldName) {
		for (ScreenColumnDefinition columnDefinition : columnDefinitions) {
			if (columnDefinition.getName().equals(fieldName)) {
				return columnDefinition;
			}
		}
		return null;
	}

	public DrilldownDefinition getDrilldownDefinition() {
		return drilldownDefinition;
	}

	public boolean isScrollable() {
		return scrollable;
	}

	public void setScrollable(boolean scrollable) {
		this.scrollable = scrollable;
	}

	public Class<? extends TableCollector> getTableCollector() {
		return tableCollectorClass;
	}

	public void setTableCollector(Class<? extends TableCollector> tableCollectorClass) {
		this.tableCollectorClass = tableCollectorClass;
	}

	public static class SimpleDrilldownDefinition implements DrilldownDefinition, Serializable {

		private static final long serialVersionUID = 1L;

		private Class<? extends RowFinder> rowFinder = RowFinder.class;

		private Class<? extends RowComparator> rowComparator = RowComparator.class;

		private Class<? extends TableScrollStopConditions> tableScrollStopConditions = TableScrollStopConditions.class;

		private Class<? extends TableScroller> tableScroller = TableScroller.class;

		private Class<? extends RowSelector> rowSelector = RowSelector.class;

		private Class<? extends TableDrilldownPerformer> drilldownPerformer = TableDrilldownPerformer.class;

		public Class<? extends RowFinder> getRowFinder() {
			return rowFinder;
		}

		public Class<? extends RowSelector> getRowSelector() {
			return rowSelector;
		}

		public Class<? extends RowComparator> getRowComparator() {
			return rowComparator;
		}

		public Class<? extends TableScroller> getTableScroller() {
			return tableScroller;
		}

		public Class<? extends TableScrollStopConditions> getTableScrollStopCondition() {
			return tableScrollStopConditions;
		}

		public Class<? extends TableDrilldownPerformer> getDrilldownPerformer() {
			return drilldownPerformer;
		}

		public void setRowFinder(Class<? extends RowFinder> rowFinder) {
			this.rowFinder = rowFinder;
		}

		public void setRowComparator(Class<? extends RowComparator> rowComparator) {
			this.rowComparator = rowComparator;
		}

		public void setRowSelector(Class<? extends RowSelector> rowSelector) {
			this.rowSelector = rowSelector;
		}

		public void setTableScroller(Class<? extends TableScroller> tableScroller) {
			this.tableScroller = tableScroller;
		}

		public void setTableScrollStopConditions(Class<? extends TableScrollStopConditions> tableScrollStopConditions) {
			this.tableScrollStopConditions = tableScrollStopConditions;
		}

		public void setDrilldownPerformer(Class<? extends TableDrilldownPerformer> drilldownPerformer) {
			this.drilldownPerformer = drilldownPerformer;
		}
	}

	public String getTableEntityName() {
		return tableEntityName;
	}

	public void setTableEntityName(String tableEntityName) {
		this.tableEntityName = tableEntityName;
	}

	public String getRowSelectionField() {
		return ScrollableTableUtil.getRowSelectionField(this);
	}

	public String getMainDisplayField() {
		return mainDisplayField;
	}

	public void setMainDisplayField(String mainDisplayField) {
		this.mainDisplayField = mainDisplayField;
	}

	public List<ActionDefinition> getActions() {
		return actions;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setPartPosition(TerminalPosition partPosition) {
		this.partPosition = partPosition;
	}

	public TerminalPosition getPartPosition() {
		return partPosition;
	}

	public int getWidth() {
		return width;
	}

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
}
