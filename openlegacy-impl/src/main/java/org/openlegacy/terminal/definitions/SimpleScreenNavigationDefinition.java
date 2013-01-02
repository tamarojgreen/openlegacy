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
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;

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

	// @author Ivan Bort refs assembla #112
	private AdditionalKey additionalKey;
	private AdditionalKey exitAdditionalKey;

	private String accessedFromEntityName;

	public String getAccessedFromEntityName() {
		if (accessedFrom != null) {
			return accessedFrom.getSimpleName();
		}
		return accessedFromEntityName;
	}

	public Class<?> getAccessedFrom() {
		return accessedFrom;
	}

	public void setAccessedFromEntityName(String accessedFromEntityName) {
		this.accessedFromEntityName = accessedFromEntityName;
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

	public void setRequiresParamaters(boolean requiresParameters) {
		this.requiresParameters = requiresParameters;
	}

	public Class<?> getTargetEntity() {
		return targetEntity;
	}

	public void setTargetEntity(Class<?> targetEntity) {
		this.targetEntity = targetEntity;
	}

	public AdditionalKey getAdditionalKey() {
		return additionalKey;
	}

	public void setAdditionalKey(AdditionalKey additionalKey) {
		this.additionalKey = additionalKey;
	}

	public AdditionalKey getExitAdditionalKey() {
		return exitAdditionalKey;
	}

	public void setExitAdditionalKey(AdditionalKey exitAdditionalKey) {
		this.exitAdditionalKey = exitAdditionalKey;
	}
}
