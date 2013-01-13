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

import org.openlegacy.terminal.actions.TerminalAction;

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

	public Class<?> getAccessedFrom() {
		return accessedFrom;
	}

	public void setAccessedFrom(Class<?> accessedFrom) {
		this.accessedFrom = accessedFrom;
	}

	public List<FieldAssignDefinition> getAssignedFields() {
		return assignedFields;
	}

	public TerminalAction getTerminalAction() {
		return terminalAction;
	}

	public void setTerminalAction(TerminalAction terminalAction) {
		this.terminalAction = terminalAction;
	}

	public TerminalAction getExitAction() {
		return exitAction;
	}

	public void setExitAction(TerminalAction exitAction) {
		this.exitAction = exitAction;
	}

	public boolean isRequiresParameters() {
		return requiresParameters;
	}

	public void setRequiresParameters(boolean requiresParameters) {
		this.requiresParameters = requiresParameters;
	}

	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	public void setTargetEntity(Class<?> targetEntity) {
		this.targetEntity = targetEntity;
	}

	public String getDrilldownValue() {
		return drilldownValue;
	}

	public void setDrilldownValue(String drilldownValue) {
		this.drilldownValue = drilldownValue;
	}
}
