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

import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.definitions.support.AbstractListFieldTypeDefinition;

public class SimpleRpcListFieldTypeDefinition extends AbstractListFieldTypeDefinition implements RpcListFieldTypeDefinition {

	private FieldTypeDefinition itemTypeDefinition;
	private Class<?> itemJavaType;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.rpc.definitions.RpcListFieldTypeDefinition#getItemFieldTypeDefinition()
	 */
	@Override
	public FieldTypeDefinition getItemTypeDefinition() {
		return itemTypeDefinition;
	}

	public void setItemTypeDefinition(FieldTypeDefinition itemTypeDefinition) {
		this.itemTypeDefinition = itemTypeDefinition;
	}

	public SimpleRpcListFieldTypeDefinition() {
		super(0, 0);
		itemTypeDefinition = null;
		itemJavaType = null;
	}

	public SimpleRpcListFieldTypeDefinition(int fieldLength, int count, FieldTypeDefinition fieldTypeDefinition,
			Class<?> itemJavaType) {
		super(fieldLength, count);
		this.itemTypeDefinition = fieldTypeDefinition;
		this.itemJavaType = itemJavaType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.rpc.definitions.RpcListFieldTypeDefinition#getItemJavaType()
	 */
	@Override
	public Class<?> getItemJavaType() {
		return itemJavaType;
	}

	@Override
	public String getItemJavaName() {
		return itemJavaType.getSimpleName();
	}
}
