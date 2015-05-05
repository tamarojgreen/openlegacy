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
package org.openlegacy.rpc.definitions.support;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.openlegacy.definitions.RpcNumericFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleNumericFieldTypeDefinition;

public class SimpleRpcNumericFieldTypeDefinition extends SimpleNumericFieldTypeDefinition implements RpcNumericFieldTypeDefinition {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Integer decimalPlaces = 0;

	public SimpleRpcNumericFieldTypeDefinition() {
		super();
	}

	public SimpleRpcNumericFieldTypeDefinition(double minimumValue, double maximumValue) {
		super(minimumValue, maximumValue, "");
	}

	public SimpleRpcNumericFieldTypeDefinition(double minimumValue, double maximumValue, Integer decimalPlaces, String pattern) {
		super(minimumValue, maximumValue, pattern);
		this.decimalPlaces = decimalPlaces;
	}

	@Override
	public Integer getDecimalPlaces() {
		return decimalPlaces;
	}

	public void setDecimalPlaces(Integer decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
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
