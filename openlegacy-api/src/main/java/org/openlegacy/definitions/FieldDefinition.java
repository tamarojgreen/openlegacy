/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.definitions;

import org.openlegacy.FieldType;

/**
 * Defines a mapping between a screenEntity java field name and it's screen position and length
 * 
 * @author Roi Mor
 */
public interface FieldDefinition extends Comparable<FieldDefinition> {

	String getName();

	String getDisplayName();

	String getSampleValue();

	String getDefaultValue();

	/**
	 * Define an applicative type of the field. e.g: UserField, MenuSelectionField, ErrorField
	 * 
	 * @return field applicative type
	 */
	Class<? extends FieldType> getType();

	/**
	 * Define the field UI field type: text, password, boolean (check-box), auto complete, date (calendar)
	 * 
	 * @return field type definition
	 */
	FieldTypeDefinition getFieldTypeDefinition();

	/**
	 * Holds the field Java type: String, Date, Integer, etc
	 * 
	 * @return field Java type
	 */
	Class<?> getJavaType();

	String getHelpText();

	boolean isKey();

	int getKeyIndex();

	boolean isRightToLeft();

}
