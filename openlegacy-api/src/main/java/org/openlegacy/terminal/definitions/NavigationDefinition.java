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

import java.util.List;

/**
 * A screen navigation definitions. Define how to step into and out from the given screen entity.
 * 
 * @author Roi Mor
 * 
 * 
 */
public interface NavigationDefinition {

	Class<?> getAccessedFrom();

	Class<?> getTargetEntity();

	List<FieldAssignDefinition> getAssignedFields();

	TerminalAction getTerminalAction();

	TerminalAction getExitAction();

	boolean isRequiresParameters();
}
