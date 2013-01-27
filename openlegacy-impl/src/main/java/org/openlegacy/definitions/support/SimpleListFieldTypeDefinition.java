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

package org.openlegacy.definitions.support;

import org.openlegacy.definitions.ListFieldTypeDefinition;

public class SimpleListFieldTypeDefinition implements ListFieldTypeDefinition {

	private int fieldLength;
	private int count;
	private int[] gaps;

	public SimpleListFieldTypeDefinition() {
		this.fieldLength = 0;
		this.count = 1;
		this.gaps = null;
	}

	public SimpleListFieldTypeDefinition(int fieldLength, int count, int[] gaps) {
		this.fieldLength = fieldLength;
		this.count = count;
		this.gaps = gaps;
	}

	public int getFieldLength() {
		return fieldLength;
	}

	public int getCount() {
		return count;
	}

	public int[] getGaps() {
		return gaps;
	}

	public String getTypeName() {
		return "list";
	}
}
