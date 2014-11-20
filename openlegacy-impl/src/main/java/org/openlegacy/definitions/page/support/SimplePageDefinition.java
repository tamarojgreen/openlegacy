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
	private String packageName;
	private List<PageDefinition> childPagesDefinitions = new ArrayList<PageDefinition>();

	public SimplePageDefinition(EntityDefinition<?> entityDefinition) {
		this.entityDefinition = entityDefinition;
	}

	@Override
	public List<PagePartDefinition> getPageParts() {
		return pageParts;
	}

	@Override
	public EntityDefinition<?> getEntityDefinition() {
		return entityDefinition;
	}

	@Override
	public List<ActionDefinition> getActions() {
		return actions;
	}

	@Override
	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public List<PageDefinition> getChildPagesDefinitions() {
		return childPagesDefinitions;
	}
}
