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
 * Fetch FieldInformation from Cobol variable declaration.
 * 
 */

public class CobolFieldInformationFactory implements FieldInformationFactory {

	private static final char SIGN_SYMBOL = 'S';
	private static final char DIGIT_SYMBOL = '9';
	private static final char SCALE_SYMBOL = 'P';

	private enum CobolFieldType {
		SIMPLE,
		NUMERIC,
		HIERARCHY;
	}

	private static boolean isNumber(char firstChar) {

		if (firstChar == SIGN_SYMBOL || firstChar == DIGIT_SYMBOL || firstChar == SCALE_SYMBOL) {
			return true;
		}
		return false;
	}

	private static CobolFieldType getCobolFieldType(String flatPicture) {
		if (flatPicture == null) {
			return CobolFieldType.HIERARCHY;
		}

		if (isNumber(flatPicture.charAt(0))) {
			return CobolFieldType.NUMERIC;
		}

		return CobolFieldType.SIMPLE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.openlegacy.designtime.rpc.source.parsers.FieldInformation#getObject(java.lang.String)
	 */
	public FieldInformation getFieldInformation(Object variableDeclaration, int count) {
		CobolFieldType cobolFieldType = getCobolFieldType((String)variableDeclaration);
		if (count > 1) {
			return new CobolListInformation(getFieldInformation(variableDeclaration, 1), count);
		}

		if (CobolFieldType.NUMERIC == cobolFieldType) {
			return new CobolNumberInformation((String)variableDeclaration);
		}
		if (CobolFieldType.SIMPLE == cobolFieldType) {
			return new CobolTextInformation((String)variableDeclaration);
		}
		return null;

	}
}
