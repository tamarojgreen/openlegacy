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
package org.openlegacy.definitions.page.support;

import org.openlegacy.EntityDefinition;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.layout.PagePartDefinition;

import java.util.ArrayList;
import java.util.List;

public class SimplePageDefinition implements PageDefinition {

	private List<PagePartDefinition> pageParts = new ArrayList<PagePartDefinition>();
	private EntityDefinition<?> entityDefinition;
	private List<ActionDefinition> actions = new ArrayList<ActionDefinition>();

	public SimplePageDefinition(EntityDefinition<?> entityDefinition) {
		this.entityDefinition = entityDefinition;
	}

	public List<PagePartDefinition> getPageParts() {
		return pageParts;
	}

	public EntityDefinition<?> getEntityDefinition() {
		return entityDefinition;
	}

	public List<ActionDefinition> getActions() {
		return actions;
	}

}
