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

/**
 * Defines a boolean field type
 * 
 */
public interface BooleanFieldTypeDefinition extends FieldTypeDefinition {

	String getTrueValue();

	String getFalseValue();

	boolean isTreatNullAsEmpty();
}
