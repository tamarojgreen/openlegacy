package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.annotations.screen.TableAction;
import org.openlegacy.definitions.support.SimpleActionDefinition;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions.ENTER;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.modules.table.TerminalDrilldownActions.SimpleDrilldownAction;
import org.openlegacy.terminal.table.TerminalDrilldownAction;

import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

/**
 * @author Ivan Bort
 * 
 */
public class TableActionModel extends ScreenNamedObject {

	private static final int DEFAULT_INT = 0;
	private static final String DEFAULT_WHEN = ".*";
	private static final Class<? extends TerminalAction> DEFAULT_ACTION = ENTER.class;
	private static final AdditionalKey DEFAULT_ADDITIONAL_KEY = AdditionalKey.NONE;
	private static final Class<?> DEFAULT_TARGET_ENTITY = ScreenEntity.NONE.class;

	// annotations attributes
	private Class<? extends TerminalAction> action = DEFAULT_ACTION;
	private AdditionalKey additionalKey = DEFAULT_ADDITIONAL_KEY;
	private boolean defaultAction = false;
	private String actionValue = "";
	private String displayName = "";
	private String alias = "";
	private Class<?> targetEntity = DEFAULT_TARGET_ENTITY;
	private int row = DEFAULT_INT;
	private int column = DEFAULT_INT;
	private int length = DEFAULT_INT;
	private String when = DEFAULT_WHEN;
	private String targetEntityName = DEFAULT_TARGET_ENTITY.getSimpleName();
	private String previousActionValue = "";
	private boolean initialized = false;

	public TableActionModel(NamedObject parent) {
		super(TableAction.class.getSimpleName());
		this.parent = parent;
	}

	public TableActionModel(UUID modelUUID, NamedObject parent) {
		super(TableAction.class.getSimpleName());
		this.uuid = modelUUID;
		this.parent = parent;
	}

	/**
	 * Method <code>init(CodeBasedScreenEntityDefinition entityDefinition)</code> is not supported for this model. Use
	 * <code>init(SimpleActionDefinition actionDefinition)</code> instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(CodeBasedScreenEntityDefinition entityDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(CodeBasedScreenEntityDefinition entityDefinition) is not supported for this model. Use init(SimpleActionDefinition actionDefinition) instead.");//$NON-NLS-1$
	}

	/**
	 * Method <code>init(ScreenFieldDefinition screenFieldDefinition)</code> is not supported for this model. Use
	 * <code>init(SimpleActionDefinition actionDefinition)</code> instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(ScreenFieldDefinition screenFieldDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(ScreenFieldDefinition screenFieldDefinition) is not supported for this model. Use init(SimpleActionDefinition actionDefinition) instead.");//$NON-NLS-1$
	}

	@SuppressWarnings("unchecked")
	public void init(SimpleActionDefinition actionDefinition) {
		if (actionDefinition.getAction() instanceof SimpleDrilldownAction) {
			SimpleDrilldownAction simpleDrilldownAction = (SimpleDrilldownAction)actionDefinition.getAction();
			this.action = simpleDrilldownAction.getAction().getClass();
			this.actionValue = (String)simpleDrilldownAction.getActionValue();
		} else {
			this.action = (Class<? extends TerminalAction>)actionDefinition.getAction().getClass();
			this.actionValue = (String)((TerminalDrilldownAction)actionDefinition.getAction()).getActionValue();
		}
		
		this.additionalKey = actionDefinition.getAdditionalKey();
		this.defaultAction = actionDefinition.isDefaultAction();
		this.displayName = actionDefinition.getDisplayName();
		this.alias = actionDefinition.getAlias() != null ? actionDefinition.getAlias() : "";//$NON-NLS-1$
		if (!StringUtils.isEmpty(actionDefinition.getTargetEntityName())) {
			this.targetEntityName = actionDefinition.getTargetEntityName();
		}
		this.row = actionDefinition.getRow();
		this.column = actionDefinition.getColumn();
		this.length = actionDefinition.getLength();
		this.when = actionDefinition.getWhen() == null ? DEFAULT_WHEN : actionDefinition.getWhen();

		this.previousActionValue = this.actionValue;
		this.initialized = true;
	}

	@Override
	public TableActionModel clone() {
		TableActionModel model = new TableActionModel(this.uuid, this.parent);
		model.setModelName(this.modelName);
		model.setAction(this.action);
		model.setAdditionalKey(this.additionalKey);
		model.setDefaultAction(this.defaultAction);
		model.setActionValue(this.actionValue);
		model.setDisplayName(this.displayName);
		model.setAlias(this.alias);
		model.setTargetEntity(this.targetEntity);
		model.setTargetEntityName(this.targetEntityName);
		model.setRow(row);
		model.setColumn(column);
		model.setLength(length);
		model.setWhen(when);
		model.previousActionValue = this.previousActionValue;
		model.initialized = this.initialized;
		return model;
	}

	public Class<? extends TerminalAction> getAction() {
		return action;
	}

	public void setAction(Class<? extends TerminalAction> action) {
		this.action = action;
	}

	public boolean isDefaultAction() {
		return defaultAction;
	}

	public void setDefaultAction(boolean defaultAction) {
		this.defaultAction = defaultAction;
	}

	public String getActionValue() {
		return actionValue;
	}

	public void setActionValue(String actionValue) {
		this.actionValue = actionValue;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	public void setTargetEntity(Class<?> targetEntity) {
		this.targetEntity = targetEntity;
	}

	public String getTargetEntityName() {
		return targetEntityName;
	}

	public void setTargetEntityName(String targetEntityName) {
		this.targetEntityName = targetEntityName;
	}

	public String getPreviousActionValue() {
		return previousActionValue;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getWhen() {
		return when;
	}

	public void setWhen(String when) {
		this.when = when;
	}

	public int getDefaultRow() {
		return DEFAULT_INT;
	}

	public int getDefaultColumn() {
		return DEFAULT_INT;
	}

	public int getDefaultLength() {
		return DEFAULT_INT;
	}

	public String getDefaultWhen() {
		return DEFAULT_WHEN;
	}

	public Class<? extends TerminalAction> getDefaultAction() {
		return DEFAULT_ACTION;
	}

	public Class<?> getDefaultTargetEntity() {
		return DEFAULT_TARGET_ENTITY;
	}
	
	public AdditionalKey getAdditionalKey() {
		return additionalKey;
	}
	
	public void setAdditionalKey(AdditionalKey additionalKey) {
		this.additionalKey = additionalKey;
	}
	
	public void setAdditionalKeyDefaultValue() {
		additionalKey = AdditionalKey.NONE;
	}

}
