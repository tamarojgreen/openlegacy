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
package org.openlegacy.layout;

/**
 * Represents a page definition layout. A page definition refers to the source entity definitions, 
 * and contains page parts and actions.
 *
 * @author Roi Mor
 */
import org.openlegacy.EntityDefinition;
import org.openlegacy.definitions.ActionDefinition;

import java.util.List;

public interface PageDefinition {

	EntityDefinition<?> getEntityDefinition();

	List<PagePartDefinition> getPageParts();

	List<ActionDefinition> getActions();
	
	String getPackageName();
}
