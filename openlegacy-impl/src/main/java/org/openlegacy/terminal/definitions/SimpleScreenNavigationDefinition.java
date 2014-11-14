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

import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SimpleScreenNavigationDefinition implements NavigationDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private Class<?> accessedFrom;
	private List<FieldAssignDefinition> assignedFields = new ArrayList<FieldAssignDefinition>();
	private TerminalAction terminalAction;
	private TerminalAction exitAction;
	private boolean requiresParameters;
	private Class<?> targetEntity;
	private String drilldownValue;

	// @author Ivan Bort refs assembla #112
	private AdditionalKey additionalKey;
	private AdditionalKey exitAdditionalKey;
	private String terminalActionName;
	private String exitActionName;

	private String accessedFromEntityName;

	@Override
	public String getAccessedFromEntityName() {
		return accessedFromEntityName;
	}

	public void setAccessedFromEntityName(String accessedFromEntityName) {
		this.accessedFromEntityName = accessedFromEntityName;
	}

	@Override
	public Class<?> getAccessedFrom() {
		return accessedFrom;
	}

	public void setAccessedFrom(Class<?> accessedFrom) {
		this.accessedFrom = accessedFrom;
	}

	@Override
	public List<FieldAssignDefinition> getAssignedFields() {
		return assignedFields;
	}

	public void setAssignedFields(List<FieldAssignDefinition> fields) {
		this.assignedFields = fields;
	}

	@Override
	public TerminalAction getTerminalAction() {
		return terminalAction;
	}

	public void setTerminalAction(TerminalAction terminalAction) {
		this.terminalAction = terminalAction;
	}

	@Override
	public TerminalAction getExitAction() {
		if (exitAction == null) {
			exitAction = TerminalActions.F3();
		}
		return exitAction;
	}

	public void setExitAction(TerminalAction exitAction) {
		this.exitAction = exitAction;
	}

	@Override
	public boolean isRequiresParameters() {
		return requiresParameters;
	}

	public void setRequiresParameters(boolean requiresParameters) {
		this.requiresParameters = requiresParameters;
	}

	@Override
	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	public void setTargetEntity(Class<?> targetEntity) {
		this.targetEntity = targetEntity;
	}

	@Override
	public String getDrilldownValue() {
		return drilldownValue;
	}

	public void setDrilldownValue(String drilldownValue) {
		this.drilldownValue = drilldownValue;
	}

	@Override
	public AdditionalKey getAdditionalKey() {
		if (additionalKey == null) {
			additionalKey = AdditionalKey.NONE;
		}
		return additionalKey;
	}

	public void setAdditionalKey(AdditionalKey additionalKey) {
		this.additionalKey = additionalKey;
	}

	@Override
	public AdditionalKey getExitAdditionalKey() {
		if (exitAdditionalKey == null) {
			exitAdditionalKey = AdditionalKey.NONE;
		}
		return exitAdditionalKey;
	}

	public void setExitAdditionalKey(AdditionalKey exitAdditionalKey) {
		this.exitAdditionalKey = exitAdditionalKey;
	}

	@Override
	public String getTerminalActionName() {
		return terminalActionName;
	}

	public void setTerminalActionName(String terminalActionName) {
		this.terminalActionName = terminalActionName;
	}

	@Override
	public String getExitActionName() {
		return exitActionName;
	}

	public void setExitActionName(String exitActionName) {
		this.exitActionName = exitActionName;
	}
}
