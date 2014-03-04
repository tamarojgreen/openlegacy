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

import java.io.Serializable;

public class SimpleFieldAssignDefinition implements FieldAssignDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	private String value;

	private String role;

	private String menuText;

	public SimpleFieldAssignDefinition(String name, String value) {
		this.name = name;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public String getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getMenuText() {
		return menuText;
	}

	public void setMenuText(String menuText) {
		this.menuText = menuText;
	}
}
