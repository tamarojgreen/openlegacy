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

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openlegacy.definitions.NumericFieldTypeDefinition;

import java.io.Serializable;

public class SimpleNumericFieldTypeDefinition implements NumericFieldTypeDefinition, Serializable {

	private static final long serialVersionUID = 1L;

	private double minimumValue = Double.MIN_VALUE;
	private double maximumValue = Double.MAX_VALUE;

	private String pattern;

	public SimpleNumericFieldTypeDefinition() {}

	public SimpleNumericFieldTypeDefinition(double minimumValue, double maximumValue, String pattern) {
		this.minimumValue = minimumValue;
		this.maximumValue = maximumValue;
		this.pattern = pattern;
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
	public String getPattern() {
		return pattern;
	}

	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	@Override
	public boolean equals(final Object object) {
		return EqualsBuilder.reflectionEquals(this, object);
	}
}
