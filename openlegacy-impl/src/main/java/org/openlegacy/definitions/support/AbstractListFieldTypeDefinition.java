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
package org.openlegacy.definitions.support;

/**
 * An abstract implementation of ListFieldTypeDefinition
 * 
 */

public class AbstractListFieldTypeDefinition {

	protected int fieldLength;
	protected int count;

	public AbstractListFieldTypeDefinition() {
		this.fieldLength = 0;
		this.count = 1;
	}

	public AbstractListFieldTypeDefinition(int fieldLength, int count) {
		this.fieldLength = fieldLength;
		this.count = count;
	}

	public int getFieldLength() {
		return fieldLength;
	}

	public int getCount() {
		return count;
	}

	public String getTypeName() {
		return "list";
	}

}