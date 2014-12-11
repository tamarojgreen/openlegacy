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
package org.openlegacy.definitions;

import org.openlegacy.annotations.screen.ScreenNumericField;

/**
 * Defines a numeric field type registry information stored within {@link FieldDefinition}. A numeric field is created from
 * {@link ScreenNumericField} annotation.
 * 
 * @author Roi Mor
 */
public interface NumericFieldTypeDefinition extends FieldTypeDefinition {

	public double getMinimumValue();

	public double getMaximumValue();

	public String getPattern();
}
