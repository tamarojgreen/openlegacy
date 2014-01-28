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
package org.openlegacy.terminal.support;

import org.openlegacy.FieldFormatter;
import org.openlegacy.terminal.FieldComparator;

import java.io.Serializable;

import javax.inject.Inject;

public class ContentDifferentFieldComparator implements FieldComparator, Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private FieldFormatter fieldFormatter;

	public boolean isFieldModified(Object screenPojo, String fieldName, Object oldValue, Object newValue) {
		if (newValue == null) {
			return false;
		}
		if (oldValue == null && newValue != null) {
			return true;
		}
		oldValue = fieldFormatter.format(String.valueOf(oldValue));
		return !oldValue.equals(String.valueOf(newValue));
	}
}
