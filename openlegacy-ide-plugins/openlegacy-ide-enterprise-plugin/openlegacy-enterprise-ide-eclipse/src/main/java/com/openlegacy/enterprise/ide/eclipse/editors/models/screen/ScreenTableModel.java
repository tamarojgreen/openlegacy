package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.support.screen.ScreenColumnModelPositionComparator;

import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.support.SimpleActionDefinition;
import org.openlegacy.designtime.generators.CodeBasedScreenTableDefinition;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.ScreenColumnDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenColumnDefinition;
import org.openlegacy.terminal.table.ScreenTableCollector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Represents @ScreenTable annotation
 * 
 * @author Ivan Bort
 * 
 */
public class ScreenTableModel extends ScreenNamedObject {

	// annotation attributes
	private int startRow;
	private int endRow;
	private String name = "";
	private Class<? extends TerminalAction> nextScreenAction = TerminalActions.PAGE_DOWN.class;
	private Class<? extends TerminalAction> previousScreenAction = TerminalActions.PAGE_UP.class;
	private boolean supportTerminalData = false;
	private boolean scrollable = true;
	@SuppressWarnings("rawtypes")
	private Class<? extends ScreenTableCollector> tableCollector = ScreenTableCollector.class;
	private int rowGaps = 1;
	private int screensCount = 1;
	private String filterExpression = "";
	private boolean rightToLeft = false;
	// other
	private Map<UUID, ScreenColumnModel> columns = new HashMap<UUID, ScreenColumnModel>();
	private Map<UUID, TableActionModel> actions = new HashMap<UUID, TableActionModel>();
	private List<ScreenColumnModel> sortedColumns = new ArrayList<ScreenColumnModel>();
	private List<TableActionModel> sortedActions = new ArrayList<TableActionModel>();
	private List<TableActionModel> originalSortedActions = new ArrayList<TableActionModel>();

	private String nextScreenActionName = TerminalActions.PAGE_DOWN.class.getSimpleName();
	private String previousScreenActionName = TerminalActions.PAGE_UP.class.getSimpleName();
	private String tableCollectorName = ScreenTableCollector.class.getSimpleName();

	private String className = "";
	private String previousClassName = "";

	private boolean initialized = false;

	public ScreenTableModel() {
		super(ScreenTable.class.getSimpleName());
	}

	public ScreenTableModel(UUID uuid) {
		super(ScreenTable.class.getSimpleName());
		this.uuid = uuid;
	}

	public ScreenTableModel(String className) {
		super(ScreenTable.class.getSimpleName());
		this.className = className;
		this.previousClassName = className;
		// this.name = className;
	}

	/**
	 * Method <code>init(CodeBasedScreenEntityDefinition entityDefinition)</code> is not supported for this model. Use
	 * <code>init(CodeBasedScreenTableDefinition tableDefinition)</code> instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(CodeBasedScreenEntityDefinition entityDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(CodeBasedScreenEntityDefinition entityDefinition) is not supported for this model. Use init(CodeBasedScreenTableDefinition tableDefinition) instead.");//$NON-NLS-1$
	}

	/**
	 * Method <code>init(ScreenFieldDefinition screenFieldDefinition)</code> is not supported for this model. Use
	 * <code>init(CodeBasedScreenTableDefinition tableDefinition)</code> instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(ScreenFieldDefinition screenFieldDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(ScreenFieldDefinition screenFieldDefinition) is not supported for this model. Use init(CodeBasedScreenTableDefinition tableDefinition) instead.");//$NON-NLS-1$
	}

	public void init(CodeBasedScreenTableDefinition tableDefinition) {
		this.startRow = tableDefinition.getStartRow();
		this.endRow = tableDefinition.getEndRow();
		this.name = tableDefinition.getTableEntityName();
		this.supportTerminalData = tableDefinition.isSupportTerminalData();
		this.scrollable = tableDefinition.isScrollable();
		this.rowGaps = tableDefinition.getRowGaps();
		this.screensCount = tableDefinition.getScreensCount();

		List<ScreenColumnDefinition> columnDefinitions = tableDefinition.getColumnDefinitions();
		for (ScreenColumnDefinition columnDefinition : columnDefinitions) {
			ScreenColumnModel model = new ScreenColumnModel(this);
			model.init((SimpleScreenColumnDefinition)columnDefinition);
			columns.put(model.getUUID(), model);
			sortedColumns.add(model.clone());
		}
		List<ActionDefinition> actionDefinitions = tableDefinition.getActions();
		for (ActionDefinition actionDefinition : actionDefinitions) {
			TableActionModel model = new TableActionModel(this);
			model.init((SimpleActionDefinition)actionDefinition);
			actions.put(model.getUUID(), model);
			TableActionModel clone = model.clone();
			sortedActions.add(clone);
			originalSortedActions.add(clone);
		}
		this.filterExpression = tableDefinition.getFilterExpression();
		this.rightToLeft = tableDefinition.isRightToLeft();

		this.nextScreenActionName = tableDefinition.getNextScreenActionName();
		this.previousScreenActionName = tableDefinition.getPreviousScreenActionName();
		this.tableCollectorName = tableDefinition.getTableCollectorName();
		this.className = tableDefinition.getClassName();
		this.previousClassName = this.className;
		this.initialized = true;
	}

	@Override
	public ScreenTableModel clone() {
		ScreenTableModel model = new ScreenTableModel(this.uuid);
		model.setModelName(this.modelName);
		model.setStartRow(this.startRow);
		model.setEndRow(this.endRow);
		model.setName(this.name);
		model.setNextScreenAction(this.nextScreenAction);
		model.setPreviousScreenAction(this.previousScreenAction);
		model.setSupportTerminalData(this.supportTerminalData);
		model.setScrollable(this.scrollable);
		model.setTableCollector(this.tableCollector);
		model.setRowGaps(this.rowGaps);
		model.setScreensCount(this.screensCount);
		model.setFilterExpression(this.filterExpression);
		model.setRightToLeft(this.rightToLeft);

		model.setNextScreenActionName(this.nextScreenActionName);
		model.setPreviousScreenActionName(this.previousScreenActionName);
		model.setTableCollectorName(this.tableCollectorName);
		model.setClassName(this.className);
		model.previousClassName = this.previousClassName;
		model.initialized = this.initialized;
		// clone columns
		Map<UUID, ScreenColumnModel> columns = new HashMap<UUID, ScreenColumnModel>();
		for (ScreenColumnModel column : this.sortedColumns) {
			ScreenColumnModel clone = column.clone();
			columns.put(clone.getUUID(), clone);
			model.getSortedColumns().add(clone);
		}
		model.setColumns(columns);
		// clone actions
		Map<UUID, TableActionModel> actions = new HashMap<UUID, TableActionModel>();
		for (TableActionModel action : this.sortedActions) {
			TableActionModel clone = action.clone();
			actions.put(clone.getUUID(), clone);
			model.getSortedActions().add(clone);
			model.getOriginalSortedActions().add(clone);
		}
		model.setActions(actions);
		return model;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Class<? extends TerminalAction> getNextScreenAction() {
		return nextScreenAction;
	}

	public void setNextScreenAction(Class<? extends TerminalAction> nextScreenAction) {
		this.nextScreenAction = nextScreenAction;
	}

	public Class<? extends TerminalAction> getPreviousScreenAction() {
		return previousScreenAction;
	}

	public void setPreviousScreenAction(Class<? extends TerminalAction> previousScreenAction) {
		this.previousScreenAction = previousScreenAction;
	}

	public boolean isSupportTerminalData() {
		return supportTerminalData;
	}

	public void setSupportTerminalData(boolean supportTerminalData) {
		this.supportTerminalData = supportTerminalData;
	}

	public boolean isScrollable() {
		return scrollable;
	}

	public void setScrollable(boolean scrollable) {
		this.scrollable = scrollable;
	}

	@SuppressWarnings("rawtypes")
	public Class<? extends ScreenTableCollector> getTableCollector() {
		return tableCollector;
	}

	@SuppressWarnings("rawtypes")
	public void setTableCollector(Class<? extends ScreenTableCollector> tableCollector) {
		this.tableCollector = tableCollector;
	}

	public int getRowGaps() {
		return rowGaps;
	}

	public void setRowGaps(int rowGaps) {
		this.rowGaps = rowGaps;
	}

	public int getScreensCount() {
		return screensCount;
	}

	public void setScreensCount(int screensCount) {
		this.screensCount = screensCount;
	}

	public Map<UUID, ScreenColumnModel> getColumns() {
		return columns;
	}

	public void setColumns(Map<UUID, ScreenColumnModel> columns) {
		this.columns = columns;
	}

	public Map<UUID, TableActionModel> getActions() {
		return actions;
	}

	public void setActions(Map<UUID, TableActionModel> actions) {
		this.actions = actions;
	}

	public String getNextScreenActionName() {
		return nextScreenActionName;
	}

	public void setNextScreenActionName(String nextScreenActionName) {
		this.nextScreenActionName = nextScreenActionName;
	}

	public String getPreviousScreenActionName() {
		return previousScreenActionName;
	}

	public void setPreviousScreenActionName(String previousScreenActionName) {
		this.previousScreenActionName = previousScreenActionName;
	}

	public String getTableCollectorName() {
		return tableCollectorName;
	}

	public void setTableCollectorName(String tableCollectorName) {
		this.tableCollectorName = tableCollectorName;
	}

	public List<ScreenColumnModel> getSortedColumns() {
		Collections.sort(sortedColumns, ScreenColumnModelPositionComparator.INSTANCE);
		return sortedColumns;
	}

	public void setSortedColumns(List<ScreenColumnModel> sortedColumns) {
		this.sortedColumns = sortedColumns;
	}

	public List<TableActionModel> getSortedActions() {
		return sortedActions;
	}

	public void setSortedActions(List<TableActionModel> sortedActions) {
		this.sortedActions = sortedActions;
	}

	public List<TableActionModel> getOriginalSortedActions() {
		return originalSortedActions;
	}

	public void setOriginalSortedActions(List<TableActionModel> originalSortedActions) {
		this.originalSortedActions = originalSortedActions;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getPreviousClassName() {
		return this.previousClassName;
	}

	public void setNextScreenActionDefaultValue() {
		nextScreenAction = TerminalActions.PAGE_DOWN.class;
		nextScreenActionName = nextScreenAction.getSimpleName();
	}

	public void setPreviousScreenActionDefaultValue() {
		previousScreenAction = TerminalActions.PAGE_UP.class;
		previousScreenActionName = previousScreenAction.getSimpleName();
	}

	public String getFilterExpression() {
		return filterExpression;
	}

	public void setFilterExpression(String filterExpression) {
		this.filterExpression = filterExpression;
	}

	public boolean isRightToLeft() {
		return rightToLeft;
	}

	public void setRightToLeft(boolean rightToLeft) {
		this.rightToLeft = rightToLeft;
	}

	public boolean isInitialized() {
		return initialized;
	}

}
