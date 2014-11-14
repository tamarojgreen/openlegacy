package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.annotations.screen.TableAction;
import org.openlegacy.definitions.support.SimpleActionDefinition;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.modules.table.TerminalDrilldownActions.EnterDrilldownAction;
import org.openlegacy.terminal.table.TerminalDrilldownAction;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class TableActionModel extends ScreenNamedObject {

	private Class<? extends TerminalDrilldownAction> action = EnterDrilldownAction.class;
	private boolean defaultAction = false;
	private String actionValue = "";
	private String displayName = "";
	private String alias = "";
	private Class<?> targetEntity = ScreenEntity.NONE.class;

	private String targetEntityName = ScreenEntity.NONE.class.getSimpleName();
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
		this.action = (Class<? extends TerminalDrilldownAction>)actionDefinition.getAction().getClass();
		this.defaultAction = actionDefinition.isDefaultAction();
		this.actionValue = (String)((EnterDrilldownAction)actionDefinition.getAction()).getActionValue();
		this.displayName = actionDefinition.getDisplayName();
		this.alias = actionDefinition.getAlias() != null ? actionDefinition.getAlias() : "";//$NON-NLS-1$
		if (!StringUtils.isEmpty(actionDefinition.getTargetEntityName())) {
			this.targetEntityName = actionDefinition.getTargetEntityName();
		}
		this.previousActionValue = this.actionValue;
		this.initialized = true;
	}

	@Override
	public TableActionModel clone() {
		TableActionModel model = new TableActionModel(this.uuid, this.parent);
		model.setModelName(this.modelName);
		model.setAction(this.action);
		model.setDefaultAction(this.defaultAction);
		model.setActionValue(this.actionValue);
		model.setDisplayName(this.displayName);
		model.setAlias(this.alias);
		model.setTargetEntity(this.targetEntity);
		model.setTargetEntityName(this.targetEntityName);
		model.previousActionValue = this.previousActionValue;
		model.initialized = this.initialized;
		return model;
	}

	public Class<? extends TerminalDrilldownAction> getAction() {
		return action;
	}

	public void setAction(Class<? extends TerminalDrilldownAction> action) {
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

}
