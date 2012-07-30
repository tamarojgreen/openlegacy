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

import org.openlegacy.annotations.screen.ScreenBooleanField;

/**
 * Defines a boolean field type registry information stored within {@link FieldDefinition}. A boolean field is created from
 * {@link ScreenBooleanField} annotation. Send the trueValue to the host, when set to true, falseValue when false.
 * 
 * @author Roi Mor
 */
public interface BooleanFieldTypeDefinition extends FieldTypeDefinition {

	String getTrueValue();

	String getFalseValue();

	/**
	 * Determine whether to set the field to null when the mapped host field position is empty
	 * 
	 * @return whether to set the field to null when host field is empty
	 */
	boolean isTreatNullAsEmpty();
}
