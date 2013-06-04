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
package org.openlegacy.rpc;

import org.openlegacy.annotations.rpc.Direction;

import java.io.Serializable;

/**
 * Defines an rpc field to send
 * 
 */
public interface RpcField extends Serializable, Cloneable {

	/**
	 * The name of the field
	 * 
	 * @return name of the field
	 */
	String getName();

	/**
	 * The value of the field
	 * 
	 * @return value of the field
	 */
	Object getValue();

	/**
	 * The original value of the field before set by user
	 * 
	 * @return original value of the field
	 */
	Object getOriginalValue();

	/**
	 * Sets the value of the field for editable fields
	 * 
	 * @param value
	 *            the new value
	 */
	void setValue(Object value);

	/**
	 * The length of the field. Double as it Can contain
	 * 
	 * @return the length of the field
	 */
	Double getLength();

	/**
	 * Is the field editable on the host screen (protected)
	 * 
	 * @return is the field editable
	 */
	boolean isEditable();

	/**
	 * Is the field modified. Used to determine whether to send the value to the host
	 * 
	 * @return is the field modified
	 */
	boolean isModified();

	/**
	 * Is the field password. A password is always an editable field
	 * 
	 * @return is the field password
	 */
	boolean isPassword();

	/**
	 * The field matching Java type. Used mostly to generate Java class field code from the field type
	 * 
	 * @return the field Java type
	 */
	Class<?> getType();

	/**
	 * Holds the fields visual
	 * 
	 * @return visual value from the host
	 */
	String getVisualValue();

	/**
	 * Get access to the underlying implementation
	 * 
	 * @return the underlying implementation
	 */
	Object getDelegate();

	/**
	 * Weather the field is displayed from right to left
	 * 
	 * @return is the field is displayed from right to left
	 */
	boolean isRightToLeft();

	/**
	 * Clones the field
	 * 
	 * @return a cloned copy of the field
	 */
	RpcField clone();

	Direction getDirection();
}
