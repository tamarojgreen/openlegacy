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
package org.openlegacy.designtime.rpc.source.parsers;

/**
 * Fetch FieldInformation from Cobol array of string or number declaration.
 * 
 */

import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.rpc.definitions.SimpleRpcListFieldTypeDefinition;

import java.util.List;

public class CobolListInformation implements FieldInformation {

	private int count;
	private FieldInformation fieldInformation;

	public CobolListInformation(FieldInformation fieldInformation, int count) {
		this.count = count;
		this.fieldInformation = fieldInformation;

	}

	public int getLength() {
		return fieldInformation.getLength();
	}

	public Class<?> getJavaType() {

		return List.class;
	}

	public FieldTypeDefinition getType() {

		return new SimpleRpcListFieldTypeDefinition(fieldInformation.getLength(), count, fieldInformation.getType(),
				fieldInformation.getJavaType());
	}

}
