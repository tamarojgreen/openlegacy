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

import org.openlegacy.definitions.NumericFieldTypeDefinition;

import java.io.Serializable;

public class SimpleNumericFieldTypeDefinition implements NumericFieldTypeDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private double minimumValue = Double.MIN_VALUE;
	private double maximumValue = Double.MAX_VALUE;

	public SimpleNumericFieldTypeDefinition() {}

	public SimpleNumericFieldTypeDefinition(double minimumValue, double maximumValue) {
		this.minimumValue = minimumValue;
		this.maximumValue = maximumValue;
	}

	public double getMinimumValue() {
		return minimumValue;
	}

	public double getMaximumValue() {
		return maximumValue;
	}

	public String getTypeName() {
		return "number";
	}

	@Override
	public boolean equals(Object object) {
		if (object instanceof SimpleNumericFieldTypeDefinition) {

			SimpleNumericFieldTypeDefinition convertedObject = (SimpleNumericFieldTypeDefinition)object;
			if (convertedObject.maximumValue == maximumValue && convertedObject.minimumValue == minimumValue) {
				return true;
			}
		}
		return false;
	}
}
