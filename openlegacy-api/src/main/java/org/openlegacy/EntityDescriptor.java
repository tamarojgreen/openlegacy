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
 * Describe a session entity information: class, display name, entity name. Used for representing a an entity information.
 * 
 * @author Roi Mor
 * 
 */
public interface EntityDescriptor {

	Class<?> getEntityClass();

	String getEntityName();

	String getDisplayName();

	/**
	 * Determine if the entity is the current entity in the session
	 * 
	 * @return is the entity is the current entity in the session
	 */
	boolean isCurrent();
}
