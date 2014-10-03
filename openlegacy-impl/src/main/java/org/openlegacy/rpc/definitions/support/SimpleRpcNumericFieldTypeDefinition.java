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

	public SimpleRpcNumericFieldTypeDefinition(double minimumValue, double maximumValue, Integer decimalPlaces) {
		super(minimumValue, maximumValue);
		this.decimalPlaces = decimalPlaces;
	}

	public Integer getDecimalPlaces() {
		return decimalPlaces;
	}

	public void setDecimalPlaces(Integer decimalPlaces) {
		this.decimalPlaces = decimalPlaces;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((decimalPlaces == null) ? 0 : decimalPlaces.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (!super.equals(object)) {
			return false;
		}
		if (getClass() != object.getClass()) {
			return false;
		}
		final SimpleRpcNumericFieldTypeDefinition other = (SimpleRpcNumericFieldTypeDefinition)object;
		if (decimalPlaces == null) {
			if (other.decimalPlaces != null) {
				return false;
			}
		} else if (!decimalPlaces.equals(other.decimalPlaces)) {
			return false;
		}
		return true;
	}
}
