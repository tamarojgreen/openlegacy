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
package org.openlegacy.providers.tn5250j;

import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.tn5250j.framework.tn5250.ScreenField;

public class Tn5250jTerminalEditableField extends Tn5250jTerminalField {

	private static final long serialVersionUID = 1L;

	private ScreenField screenField;

	public Tn5250jTerminalEditableField(ScreenField screenField, int length, int fieldAttributes, String value) {
		super(value, new SimpleTerminalPosition(screenField.startRow() + 1, screenField.startCol() + 1), length, fieldAttributes,
				false);
		this.screenField = screenField;
	}

	@Override
	public String getValue() {
		if (getModifiedValue() != null) {
			return getModifiedValue();
		}
		return super.getValue();
	}

	@Override
	public boolean isEditable() {
		return true;
	}

	@Override
	public Class<?> getType() {
		if (screenField.isNumeric()) {
			return Integer.class;
		}
		return String.class;
	}

	@Override
	public void setValue(String value) {
		super.setValue(value);
		screenField.setString(value);
	}

	@Override
	public Object getDelegate() {
		return screenField;
	}

	@Override
	public boolean isRightToLeft() {
		return screenField.isRightToLeft();
	}

	@Override
	public boolean isUppercase() {
		return screenField.isToUpper();
	}
}
