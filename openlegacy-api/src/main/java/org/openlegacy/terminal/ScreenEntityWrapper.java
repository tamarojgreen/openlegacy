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
package org.openlegacy.terminal;

import org.openlegacy.EntityDescriptor;
import org.openlegacy.definitions.ActionDefinition;

import java.util.List;

/**
 * A container class for screen entity which includes additional elements
 * 
 * @author Roi Mor
 * 
 */
public interface ScreenEntityWrapper {

	ScreenEntity getScreenEntity();

	List<EntityDescriptor> getPaths();

	List<ActionDefinition> getActions();
}
