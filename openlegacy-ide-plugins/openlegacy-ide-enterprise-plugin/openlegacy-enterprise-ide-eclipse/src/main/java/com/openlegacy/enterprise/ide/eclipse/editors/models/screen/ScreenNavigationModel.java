package com.openlegacy.enterprise.ide.eclipse.editors.models.screen;

import org.openlegacy.annotations.screen.ScreenNavigation;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedScreenEntityDefinition;
import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.FieldAssignDefinition;
import org.openlegacy.terminal.definitions.NavigationDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleFieldAssignDefinition;
import org.openlegacy.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Represents @ScreenNavigation annotation
 * 
 * @author Ivan Bort
 * 
 */
public class ScreenNavigationModel extends ScreenNamedObject {

	// annotation attributes
	private String accessedFromEntityName = "";
	private Class<?> accessedFrom = null;

	private Class<? extends TerminalAction> terminalAction = TerminalActions.ENTER.class;
	private String terminalActionName = "";

	private AdditionalKey additionalKey = AdditionalKey.NONE;
	private List<FieldAssignDefinition> assignedFields = new ArrayList<FieldAssignDefinition>();

	private Class<? extends TerminalAction> exitAction = TerminalActions.F3.class;
	private String exitActionName = "";

	private AdditionalKey exitAdditionalKey = AdditionalKey.NONE;
	private boolean requiresParameters = false;
	private String drilldownValue = "";

	public ScreenNavigationModel() {
		super(ScreenNavigation.class.getSimpleName());
	}

	public ScreenNavigationModel(UUID uuid) {
		super(ScreenNavigation.class.getSimpleName());
		this.uuid = uuid;
	}

	@Override
	public void init(CodeBasedScreenEntityDefinition entityDefinition) {
		NavigationDefinition definition = entityDefinition.getNavigationDefinition();
		if (definition == null) {
			return;
		}
		this.accessedFromEntityName = definition.getAccessedFromEntityName();
		this.terminalActionName = definition.getTerminalActionName() != null ? definition.getTerminalActionName() : "";
		this.additionalKey = definition.getAdditionalKey();
		this.assignedFields = definition.getAssignedFields() != null ? definition.getAssignedFields()
				: new ArrayList<FieldAssignDefinition>();
		this.exitActionName = definition.getExitActionName() != null ? definition.getExitActionName() : "";
		this.exitAdditionalKey = definition.getExitAdditionalKey();
		this.requiresParameters = definition.isRequiresParameters();

		if (!StringUtil.isEmpty(this.terminalActionName)) {
			TerminalAction newAction = TerminalActions.newAction(this.terminalActionName.toUpperCase());
			if (newAction != null) {
				this.terminalAction = newAction.getClass();
			}
		}
		if (!this.exitActionName.isEmpty()) {
			TerminalAction action = TerminalActions.newAction(this.exitActionName.toUpperCase());
			if (action != null) {
				this.exitAction = action.getClass();
			}
		}
		this.drilldownValue = (definition.getDrilldownValue() == null) ? "" : definition.getDrilldownValue();
	}

	/**
	 * Method <code>init(ScreenFieldDefinition screenFieldDefinition)</code> is not supported for this model. Use
	 * <code>init(CodeBasedScreenEntityDefinition entityDefinition)</code> instead.
	 * 
	 * @throws OpenLegacyRuntimeException
	 */
	@Override
	public void init(ScreenFieldDefinition screenFieldDefinition) {
		throw new OpenLegacyRuntimeException(
				"Method init(ScreenFieldDefinition screenFieldDefinition) is not supported for this model. Use init(CodeBasedScreenEntityDefinition entityDefinition) instead.");//$NON-NLS-1$
	}

	@Override
	public ScreenNavigationModel clone() {
		ScreenNavigationModel model = new ScreenNavigationModel(this.uuid);
		model.setAccessedFromEntityName(this.accessedFromEntityName);
		model.setAdditionalKey(this.additionalKey);
		model.setExitActionName(this.exitActionName);
		model.setExitAdditionalKey(this.exitAdditionalKey);
		model.setRequiresParameters(this.isRequiresParameters());
		model.setTerminalActionName(this.terminalActionName);
		List<FieldAssignDefinition> list = new ArrayList<FieldAssignDefinition>();
		for (FieldAssignDefinition item : this.assignedFields) {
			list.add(new SimpleFieldAssignDefinition(item.getName(), item.getValue()));
		}
		model.setAssignedFields(list);
		model.setTerminalAction(this.terminalAction);
		model.setExitAction(this.exitAction);
		model.setDrilldownValue(this.drilldownValue);
		return model;
	}

	public Class<? extends TerminalAction> getTerminalAction() {
		if (terminalAction == null) {
			terminalAction = TerminalActions.ENTER.class;
		}
		return terminalAction;
	}

	public Class<? extends TerminalAction> getExitAction() {
		if (exitAction == null) {
			exitAction = TerminalActions.F3.class;
		}
		return exitAction;
	}

	public void setTerminalAction(Class<? extends TerminalAction> terminalAction) {
		this.terminalAction = terminalAction;
	}

	public AdditionalKey getAdditionalKey() {
		return additionalKey;
	}

	public void setAdditionalKey(AdditionalKey additionalKey) {
		this.additionalKey = additionalKey;
	}

	public List<FieldAssignDefinition> getAssignedFields() {
		return assignedFields;
	}

	public void setAssignedFields(List<FieldAssignDefinition> assignedFields) {
		this.assignedFields = assignedFields;
	}

	public void setExitAction(Class<? extends TerminalAction> exitAction) {
		this.exitAction = exitAction;
	}

	public AdditionalKey getExitAdditionalKey() {
		return exitAdditionalKey;
	}

	public void setExitAdditionalKey(AdditionalKey exitAdditionalKey) {
		this.exitAdditionalKey = exitAdditionalKey;
	}

	public boolean isRequiresParameters() {
		return requiresParameters;
	}

	public void setRequiresParameters(boolean requiresParameters) {
		this.requiresParameters = requiresParameters;
	}

	public String getAccessedFromEntityName() {
		return accessedFromEntityName;
	}

	public void setAccessedFromEntityName(String accessedFromEntityName) {
		this.accessedFromEntityName = accessedFromEntityName;
	}

	public Class<?> getAccessedFrom() {
		return accessedFrom;
	}

	public void setAccessedFrom(Class<?> accessedFrom) {
		this.accessedFrom = accessedFrom;
	}

	public String getTerminalActionName() {
		return terminalActionName;
	}

	public void setTerminalActionName(String terminalActionName) {
		this.terminalActionName = terminalActionName;
	}

	public String getExitActionName() {
		return exitActionName;
	}

	public void setExitActionName(String exitActionName) {
		this.exitActionName = exitActionName;
	}

	public String getDrilldownValue() {
		return drilldownValue;
	}

	public void setDrilldownValue(String drilldownValue) {
		this.drilldownValue = drilldownValue;
	}

	public void setTerminalActionDefaultValue() {
		terminalAction = TerminalActions.ENTER.class;
		terminalActionName = terminalAction.getSimpleName();
	}

	public void setExitActionDefaultValue() {
		exitAction = TerminalActions.F3.class;
		exitActionName = exitAction.getSimpleName();
	}

	public void setAdditionalKeyDefaultValue() {
		additionalKey = AdditionalKey.NONE;
	}

	public void setExitAdditionalKeyDefaultValue() {
		exitAdditionalKey = AdditionalKey.NONE;
	}
}
