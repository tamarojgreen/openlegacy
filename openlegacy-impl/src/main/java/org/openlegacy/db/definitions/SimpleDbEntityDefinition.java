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

public class SimpleDbEntityDefinition extends AbstractEntityDefinition<DbFieldDefinition> implements DbEntityDefinition {

	private static final long serialVersionUID = 1L;
	private boolean window;

	public SimpleDbEntityDefinition(String entityName, Class<?> containingClass) {
		super(entityName, containingClass);
	}

	public boolean isWindow() {
		return window;
	}

	public void setWindow(boolean window) {
		this.window = window;
	}

}
