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
 * Fetch FieldInformation from Cobol text variable declaration.
 * 
 */

import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleTextFieldTypeDefinition;

public class CobolTextInformation implements FieldInformation {

	private int length;

	public CobolTextInformation(String flatPicture) {
		length = flatPicture.length();
	}

	@Override
	public int getLength() {

		return length;
	}

	@Override
	public Class<?> getJavaType() {

		return String.class;
	}

	@Override
	public FieldTypeDefinition getType() {
		return new SimpleTextFieldTypeDefinition();
	}
}
