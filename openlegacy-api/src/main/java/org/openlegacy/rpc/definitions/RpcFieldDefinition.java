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
package org.openlegacy.rpc.definitions;

import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.definitions.FieldDefinition;

public interface RpcFieldDefinition extends FieldDefinition, OrderedField {

	String getOriginalName();

	Direction getDirection();

	Integer getLength();

	Integer getDecimalPlaces();

	@Override
	int getOrder();

	@Override
	String getDefaultValue();

	@Override
	String getNullValue();

	String getShortName();

	String getOriginalNameForList();
}
