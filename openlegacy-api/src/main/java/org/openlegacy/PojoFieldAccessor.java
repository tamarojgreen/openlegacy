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
package org.openlegacy;

/**
 * A utility interface definition which is used to define access to an entity fields
 * 
 * @author Roi Mor
 * 
 */
public interface PojoFieldAccessor {

	boolean isExists(String fieldName);

	/**
	 * Determine whether the field is writable (has a setter method).
	 * 
	 * @param fieldName
	 *            the entity field to check
	 * @return whether the field is writable
	 */
	boolean isWritable(String fieldName);

	/**
	 * Set value to an entity field
	 * 
	 * @param fieldName
	 *            the field name to set value for
	 * @param value
	 *            the value to set
	 */
	void setFieldValue(String fieldName, Object value);

	/**
	 * Determine the field Java type, e.g. Integer, String, Boolean
	 * 
	 * @param fieldName
	 *            the field name
	 * @return the field Java type
	 */
	Class<?> getFieldType(String fieldName);

	/**
	 * Return a field value for a given entity field
	 * 
	 * @param fieldName
	 *            the field name
	 * @return the field value
	 */
	Object getFieldValue(String fieldName);

	Object evaluateFieldValue(String name);

}