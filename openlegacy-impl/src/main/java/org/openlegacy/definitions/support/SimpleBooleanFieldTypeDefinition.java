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

import org.openlegacy.definitions.BooleanFieldTypeDefinition;

import java.io.Serializable;

public class SimpleBooleanFieldTypeDefinition implements BooleanFieldTypeDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private String trueValue;
	private String falseValue;
	private boolean treatNullAsEmpty;

	public SimpleBooleanFieldTypeDefinition(String trueValue, String falseValue, boolean treatNullAsEmpty) {
		this.trueValue = trueValue;
		this.falseValue = falseValue;
		this.treatNullAsEmpty = treatNullAsEmpty;
	}

	@Override
	public String getTrueValue() {
		return trueValue;
	}

	@Override
	public String getFalseValue() {
		return falseValue;
	}

	@Override
	public String getTypeName() {
		return "boolean";
	}

	@Override
	public boolean isTreatNullAsEmpty() {
		return treatNullAsEmpty;
	}
}
