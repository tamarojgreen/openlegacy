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
package org.openlegacy.terminal.definitions;

import org.openlegacy.definitions.AbstractTableDefinition;
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
import org.openlegacy.terminal.definitions.ScreenTableDefinition.ScreenColumnDefinition;
import org.openlegacy.terminal.modules.table.ScrollableTableUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes")
public class SimpleScreenTableDefinition extends AbstractTableDefinition<ScreenColumnDefinition> implements ScreenTableDefinition, PositionedPart {

	private static final long serialVersionUID = 1L;

	private int startRow;
	private int endRow;

	private TerminalAction nextScreenAction;
	private TerminalAction previousScreenAction;

	private DrilldownDefinition drilldownDefinition = new SimpleDrilldownDefinition();

	private boolean scrollable = true;

	private String tableEntityName;

	private List<String> mainDisplayFields = new ArrayList<String>();

	// private List<ActionDefinition> actions = new ArrayList<ActionDefinition>();

	private TerminalPosition partPosition;

	private int width;

	private List<ScreenTableReferenceDefinition> tableReferenceDefinitions = new ArrayList<ScreenTableReferenceDefinition>();

	private Class<? extends TableCollector> tableCollectorClass;

	private int screensCount;

	private String filterExpression;

	private boolean rightToLeft = false;

	public SimpleScreenTableDefinition(Class<?> rowClass) {
		super(rowClass);
	}

	@Override
	public int getStartRow() {
		return startRow;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	@Override
	public int getEndRow() {
		return endRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	@Override
	public TerminalAction getNextScreenAction() {
		return nextScreenAction;
	}

	public void setNextScreenAction(TerminalAction nextScreenAction) {
		this.nextScreenAction = nextScreenAction;
	}

	@Override
	public TerminalAction getPreviousScreenAction() {
		return previousScreenAction;
	}

	public void setPreviousScreenAction(TerminalAction previousScreenAction) {
		this.previousScreenAction = previousScreenAction;
	}

	@Override
	public ScreenColumnDefinition getSelectionColumn() {
		for (ScreenColumnDefinition columnDefinition : getColumnDefinitions()) {
			if (columnDefinition.isSelectionField()) {
				return columnDefinition;
			}
		}
		return null;
	}

	@Override
	public int getMaxRowsCount() {
		return (getEndRow() - getStartRow() + 1) / getRowGaps();
	}

	@Override
	public ScreenColumnDefinition getColumnDefinition(String fieldName) {
		for (ScreenColumnDefinition columnDefinition : getColumnDefinitions()) {
			if (columnDefinition.getName().equals(fieldName)) {
				return columnDefinition;
			}
		}
		return null;
	}

	@Override
	public DrilldownDefinition getDrilldownDefinition() {
		return drilldownDefinition;
	}

	@Override
	public boolean isScrollable() {
		return scrollable;
	}

	@Override
	public void setScrollable(boolean scrollable) {
		this.scrollable = scrollable;
	}

	@Override
	public Class<? extends TableCollector> getTableCollector() {
		return tableCollectorClass;
	}

	@Override
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

		@Override
		public Class<? extends RowFinder> getRowFinder() {
			return rowFinder;
		}

		@Override
		public Class<? extends RowSelector> getRowSelector() {
			return rowSelector;
		}

		@Override
		public Class<? extends RowComparator> getRowComparator() {
			return rowComparator;
		}

		@Override
		public Class<? extends TableScroller> getTableScroller() {
			return tableScroller;
		}

		@Override
		public Class<? extends TableScrollStopConditions> getTableScrollStopCondition() {
			return tableScrollStopConditions;
		}

		@Override
		public Class<? extends TableDrilldownPerformer> getDrilldownPerformer() {
			return drilldownPerformer;
		}

		@Override
		public void setRowFinder(Class<? extends RowFinder> rowFinder) {
			this.rowFinder = rowFinder;
		}

		@Override
		public void setRowComparator(Class<? extends RowComparator> rowComparator) {
			this.rowComparator = rowComparator;
		}

		@Override
		public void setRowSelector(Class<? extends RowSelector> rowSelector) {
			this.rowSelector = rowSelector;
		}

		@Override
		public void setTableScroller(Class<? extends TableScroller> tableScroller) {
			this.tableScroller = tableScroller;
		}

		@Override
		public void setTableScrollStopConditions(Class<? extends TableScrollStopConditions> tableScrollStopConditions) {
			this.tableScrollStopConditions = tableScrollStopConditions;
		}

		@Override
		public void setDrilldownPerformer(Class<? extends TableDrilldownPerformer> drilldownPerformer) {
			this.drilldownPerformer = drilldownPerformer;
		}
	}

	@Override
	public String getTableEntityName() {
		return tableEntityName;
	}

	@Override
	public void setTableEntityName(String tableEntityName) {
		this.tableEntityName = tableEntityName;
	}

	@Override
	public String getRowSelectionField() {
		return ScrollableTableUtil.getRowSelectionField(this);
	}

	@Override
	public List<String> getMainDisplayFields() {
		return mainDisplayFields;
	}

	// @Override
	// public List<ActionDefinition> getActions() {
	// return actions;
	// }

	@Override
	public void setWidth(int width) {
		this.width = width;
	}

	@Override
	public void setPartPosition(TerminalPosition partPosition) {
		this.partPosition = partPosition;
	}

	@Override
	public TerminalPosition getPartPosition() {
		return partPosition;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getScreensCount() {
		return screensCount;
	}

	public void setScreensCount(int screensCount) {
		this.screensCount = screensCount;
	}

	@Override
	public List<ScreenTableReferenceDefinition> getTableReferenceDefinitions() {
		return tableReferenceDefinitions;
	}

	@Override
	public String getFilterExpression() {
		return filterExpression;
	}

	public void setFilterExpression(String filterExpression) {
		this.filterExpression = filterExpression;
	}

	@Override
	public boolean isRightToLeft() {
		return rightToLeft;
	}

	public void setRightToLeft(boolean rightToLeft) {
		this.rightToLeft = rightToLeft;
	}

}
