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
package org.openlegacy.rpc;

import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.rpc.definitions.OrderedField;

import java.io.Serializable;

/**
 * Defines an rpc field to send
 * 
 */
public interface RpcField extends Serializable, Cloneable, OrderedField {

	/**
	 * The name of the field
	 * 
	 * @return name of the field
	 */
	String getName();

	/**
	 * The length of the field. Double as it Can contain
	 * 
	 * @return the length of the field
	 */
	Integer getLength();

	/**
	 * Get access to the underlying implementation
	 * 
	 * @return the underlying implementation
	 */
	Object getDelegate();

	/**
	 * Clones the field
	 * 
	 * @return a cloned copy of the field
	 */
	RpcField clone();

	Direction getDirection();

	int getOrder();

	int depth(int level, int maxDef) throws RpcStructureNotMappedException;

	Class<?> getType();

	String getLegacyContainerName();

	boolean isContainer();

	boolean isVirtual();

	void setVirtualGroup(String virtaulGroup);

	String getVirtualGroup();

}
