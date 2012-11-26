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
package org.openlegacy.support;

import org.openlegacy.DisplayItem;

import java.io.Serializable;

public class SimpleDisplayItem implements DisplayItem, Serializable {

	private static final long serialVersionUID = 1L;

	private Object value;
	private Object display;

	public SimpleDisplayItem(Object value, Object display) {
		this.value = value;
		this.display = display;
	}

	public Object getValue() {
		return value;
	}

	public Object getDisplay() {
		return display;
	}

	@Override
	public String toString() {
		return display.toString();
	}
}
