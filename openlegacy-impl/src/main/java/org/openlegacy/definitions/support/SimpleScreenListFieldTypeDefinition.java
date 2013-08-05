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

/**
 * The simple Screen list field definitions.
 */

import org.openlegacy.definitions.ScreenListFieldTypeDefinition;

public class SimpleScreenListFieldTypeDefinition extends AbstractListFieldTypeDefinition implements ScreenListFieldTypeDefinition {

	private int[] gaps;

	public SimpleScreenListFieldTypeDefinition() {
		super();
		this.gaps = null;
	}

	public SimpleScreenListFieldTypeDefinition(int fieldLength, int count, int[] gaps) {
		super(fieldLength, count);
		this.gaps = gaps;
	}

	public int[] getGaps() {
		return gaps;
	}
}
