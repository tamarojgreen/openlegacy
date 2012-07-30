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
 * Place holder for field type definitions like: Boolean (true/false values), Date field, Auto complete (holds list of values) Any
 * field has a type to simplify field meta-data handling in web layer
 * 
 * @author Roi Mor
 */
public interface FieldTypeDefinition {

	/**
	 * Used by free-marker to determine the field type
	 */
	public String getTypeName();
}
