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
package org.openlegacy.db.definitions;

import org.openlegacy.definitions.support.AbstractEntityDefinition;

import java.util.LinkedHashMap;
import java.util.Map;

public class SimpleDbEntityDefinition extends AbstractEntityDefinition<DbFieldDefinition> implements DbEntityDefinition {

	private static final long serialVersionUID = 1L;

	private boolean window;
	private DbNavigationDefinition navigationDefinition = new SimpleDbNavigationDefinition();
	private boolean child;

	private final Map<String, DbFieldDefinition> columnsDefinitions = new LinkedHashMap<String, DbFieldDefinition>();

	public SimpleDbEntityDefinition(String entityName, Class<?> containingClass) {
		super(entityName, containingClass);
	}

	@Override
	public boolean isWindow() {
		return window;
	}

	public void setWindow(boolean window) {
		this.window = window;
	}

	@Override
	public Map<String, DbFieldDefinition> getColumnFieldsDefinitions() {
		return columnsDefinitions;
	}

	@Override
	public DbNavigationDefinition getNavigationDefinition() {
		return navigationDefinition;
	}

	@Override
	public boolean isChild() {
		return child;
	}

	public void setChild(boolean child) {
		this.child = child;
	}

}
