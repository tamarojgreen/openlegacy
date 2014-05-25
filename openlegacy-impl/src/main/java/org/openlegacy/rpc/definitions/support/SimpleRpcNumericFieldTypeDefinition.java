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

	@Override
	public boolean equals(Object object) {
		if (object instanceof SimpleRpcNumericFieldTypeDefinition) {
			SimpleRpcNumericFieldTypeDefinition convertedObject = (SimpleRpcNumericFieldTypeDefinition)object;

			if (super.equals(convertedObject) == true && convertedObject.decimalPlaces == decimalPlaces) {
				return true;
			}
		}
		return false;

	}
}
