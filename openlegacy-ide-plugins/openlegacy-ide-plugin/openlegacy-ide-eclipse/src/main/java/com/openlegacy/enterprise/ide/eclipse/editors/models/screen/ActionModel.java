package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import org.openlegacy.Session;
import org.openlegacy.SessionAction;
import org.openlegacy.annotations.screen.Action;
import org.openlegacy.annotations.screen.Action.ActionType;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.actions.TerminalActions.SimpleTerminalMappedAction;

/**
 * @author Ivan Bort
 * 
 */
public class ActionModel extends ScreenNamedObject implements ActionDefinition {

	private static final String DEFAULT_STRING = "";
	private static final int DEFAULT_INT = 0;
	private static final AdditionalKey DEFAULT_ADDITIONL_KEY = AdditionalKey.NONE;
	private static final String DEFAULT_WHEN = ".*";
	private static final ActionType DEFAULT_ACTION_TYPE = ActionType.GENERAL;
	private static final Class<?> DEFAULT_TARGET_ENTITY = ScreenEntity.NONE.class;
	private static final boolean DEFAULT_GLOBAL = true;
	private static final Class<? extends SimpleTerminalMappedAction> DEFAULT_KEYBOARD_KEY = TerminalActions.NONE.class;

	// annotation attributes
	private SessionAction<? extends Session> action;
	private String displayName = DEFAULT_STRING;
	private int row = DEFAULT_INT;
	private int column = DEFAULT_INT;
	private AdditionalKey additionalKey = DEFAULT_ADDITIONL_KEY;
	private String alias = DEFAULT_STRING;
	private boolean global = DEFAULT_GLOBAL;
	private String focusField = DEFAULT_STRING;
	private ActionType type = DEFAULT_ACTION_TYPE;
	private int length = DEFAULT_INT;
	private String when = DEFAULT_WHEN;
	private Class<?> targetEntity = DEFAULT_TARGET_ENTITY;
	private int sleep = DEFAULT_INT;
	private Class<? extends SimpleTerminalMappedAction> keyboardKey = DEFAULT_KEYBOARD_KEY;

	private String targetEntityClassName = ScreenEntity.NONE.class.getSimpleName();
	private String prevTargetEntityClassName = ScreenEntity.NONE.class.getSimpleName();
	private String actionName;
	private String prevActionName;
	private String prevDisplayName;
	private int prevRow;
	private int prevColumn;
	private AdditionalKey prevAdditionalKey = AdditionalKey.NONE;
	private String prevAlias;
	private boolean prevGlobal;
	private String prevFocusField;
	private ActionType prevType;
	private int prevLength;
	private String prevWhen;
	private int prevSleep;
	private String keyboardKeyName;
	private String prevKeyboardKeyName;

	//	private UUID uuid = UUID.randomUUID();

	public ActionModel(String actionName, String displayName, String alias, AdditionalKey additionalKey, int row, int column,
			int length, String when, String focusField, ActionType type, String targetEntityName, int sleep, boolean global,
			String keyboardKeyName) {

		super(Action.class.getSimpleName());

		this.actionName = actionName;
		this.displayName = displayName;
		this.alias = alias;
		this.prevActionName = actionName;
		this.prevDisplayName = displayName;
		this.prevAlias = alias;
		this.additionalKey = additionalKey;
		this.prevAdditionalKey = additionalKey;
		this.prevWhen = when;
		this.when = when;
		this.row = row;
		this.prevRow = row;
		this.column = column;
		this.prevColumn = column;
		this.length = length;
		this.prevLength = length;
		this.focusField = focusField == null ? "" : focusField;
		this.prevFocusField = this.focusField;
		this.type = type;
		this.prevType = type;
		this.targetEntityClassName = targetEntityName == null ? ScreenEntity.NONE.class.getSimpleName() : targetEntityName;
		this.prevTargetEntityClassName = targetEntityName == null ? ScreenEntity.NONE.class.getSimpleName() : targetEntityName;
		this.sleep = sleep;
		this.prevSleep = sleep;
		this.global = global;
		this.prevGlobal = global;
		this.keyboardKeyName = keyboardKeyName == null ? TerminalActions.NONE.class.getSimpleName() : keyboardKeyName;
		this.prevKeyboardKeyName = this.keyboardKeyName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.definitions.ActionDefinition#getAction()
	 */
	@Override
	public SessionAction<? extends Session> getAction() {
		return action;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.definitions.ActionDefinition#getActionName()
	 */
	@Override
	public String getActionName() {
		return actionName;
	}

	public AdditionalKey getAdditionalKey() {
		return additionalKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.definitions.ActionDefinition#getAlias()
	 */
	@Override
	public String getAlias() {
		return alias;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.definitions.ActionDefinition#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return displayName;
	}

	public AdditionalKey getPrevAdditionalKey() {
		return prevAdditionalKey;
	}

	public String getPrevAlias() {
		return prevAlias;
	}

	public String getPrevDisplayName() {
		return prevDisplayName;
	}

	public String getPreviousActionName() {
		return this.prevActionName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.definitions.ActionDefinition#isDefaultAction()
	 */
	@Override
	public boolean isDefaultAction() {
		return false;
	}

	@Override
	public boolean isGlobal() {
		return global;
	}

	public void setAction(SessionAction<? extends Session> action) {
		this.action = action;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public void setAdditionalKey(AdditionalKey additionalKey) {
		this.additionalKey = additionalKey;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	@Override
	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	public ActionModel cloneModel() {
		return new ActionModel(actionName, displayName, alias, additionalKey, getRow(), getColumn(), getLength(), getWhen(),
				focusField, type, targetEntityClassName, sleep, global, keyboardKeyName);
	}

	@Override
	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	@Override
	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	@Override
	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public int getPrevRow() {
		return prevRow;
	}

	public void setPrevRow(int prevRow) {
		this.prevRow = prevRow;
	}

	public int getPrevColumn() {
		return prevColumn;
	}

	public void setPrevColumn(int prevColumn) {
		this.prevColumn = prevColumn;
	}

	public int getPrevLength() {
		return prevLength;
	}

	public void setPrevLength(int prevLength) {
		this.prevLength = prevLength;
	}

	@Override
	public String getWhen() {
		return when;
	}

	public void setWhen(String when) {
		this.when = when;
	}

	public String getPrevWhen() {
		return prevWhen;
	}

	public void setPrevWhen(String prevWhen) {
		this.prevWhen = prevWhen;
	}

	public String getFocusField() {
		return focusField;
	}

	public void setFocusField(String focusField) {
		this.focusField = focusField;
	}

	public ActionType getType() {
		return type;
	}

	public void setType(ActionType type) {
		this.type = type;
	}

	public int getSleep() {
		return sleep;
	}

	public void setSleep(int sleep) {
		this.sleep = sleep;
	}

	public void setTargetEntity(Class<?> targetEntity) {
		this.targetEntity = targetEntity;
	}

	public String getTargetEntityClassName() {
		return targetEntityClassName;
	}

	public void setTargetEntityClassName(String targetEntityClassName) {
		this.targetEntityClassName = targetEntityClassName;
	}

	public String getPrevTargetEntityClassName() {
		return prevTargetEntityClassName;
	}

	public void setPrevTargetEntityClassName(String prevTargetEntityClassName) {
		this.prevTargetEntityClassName = prevTargetEntityClassName;
	}

	public String getPrevFocusField() {
		return prevFocusField;
	}

	public void setPrevFocusField(String prevFocusField) {
		this.prevFocusField = prevFocusField;
	}

	public ActionType getPrevType() {
		return prevType;
	}

	public void setPrevType(ActionType prevType) {
		this.prevType = prevType;
	}

	public int getPrevSleep() {
		return prevSleep;
	}

	public void setPrevSleep(int prevSleep) {
		this.prevSleep = prevSleep;
	}

	public void setAdditionalKeyDefaultValue() {
		additionalKey = AdditionalKey.NONE;
	}

	public void setTypeDefaultValue() {
		type = ActionType.GENERAL;
	}

	public boolean isPrevGlobal() {
		return prevGlobal;
	}

	public void setGlobal(boolean global) {
		this.global = global;
	}

	public Class<? extends SimpleTerminalMappedAction> getKeyboardKey() {
		return keyboardKey;
	}

	public void setKeyboardKey(Class<? extends SimpleTerminalMappedAction> keyboardKey) {
		this.keyboardKey = keyboardKey;
	}

	public String getKeyboardKeyName() {
		return keyboardKeyName;
	}

	public void setKeyboardKeyName(String keyboardKeyName) {
		this.keyboardKeyName = keyboardKeyName;
	}

	public String getPrevKeyboardKeyName() {
		return prevKeyboardKeyName;
	}

	public void setKeyboardKeyDefaultValue() {
		keyboardKeyName = DEFAULT_KEYBOARD_KEY.getSimpleName();
		keyboardKey = DEFAULT_KEYBOARD_KEY;
	}

	// ----------------- DEFAULTS --------------------

	public String getDefaultDisplayName() {
		return DEFAULT_STRING;
	}

	public int getDefaultRow() {
		return DEFAULT_INT;
	}

	public int getDefaultColumn() {
		return DEFAULT_INT;
	}

	public AdditionalKey getDefaultAdditionalKey() {
		return DEFAULT_ADDITIONL_KEY;
	}

	public String getDefaultAlias() {
		return DEFAULT_STRING;
	}

	public int getDefaultLength() {
		return DEFAULT_INT;
	}

	public String getDefaultWhen() {
		return DEFAULT_WHEN;
	}

	public String getDefaultFocusField() {
		return DEFAULT_STRING;
	}

	public ActionType getDefaultActionType() {
		return DEFAULT_ACTION_TYPE;
	}

	public Class<?> getDefaultTargetEntity() {
		return DEFAULT_TARGET_ENTITY;
	}

	public int getDefaultSleep() {
		return DEFAULT_INT;
	}

	public boolean getDefaultGlobal() {
		return DEFAULT_GLOBAL;
	}

	public Class<? extends SimpleTerminalMappedAction> getDefaultKeyboardKey() {
		return DEFAULT_KEYBOARD_KEY;
	}
}
