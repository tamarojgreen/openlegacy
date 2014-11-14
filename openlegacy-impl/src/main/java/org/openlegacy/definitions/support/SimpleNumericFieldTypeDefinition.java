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

	@Override
	public double getMinimumValue() {
		return minimumValue;
	}

	@Override
	public double getMaximumValue() {
		return maximumValue;
	}

	@Override
	public String getTypeName() {
		return "number";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(maximumValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(minimumValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object)
			return true;
		if (object == null)
			return false;
		if (getClass() != object.getClass())
			return false;
		final SimpleNumericFieldTypeDefinition other = (SimpleNumericFieldTypeDefinition) object;
		if (Double.doubleToLongBits(maximumValue) != Double
				.doubleToLongBits(other.maximumValue))
			return false;
		if (Double.doubleToLongBits(minimumValue) != Double
				.doubleToLongBits(other.minimumValue))
			return false;
		return true;
	}
}
