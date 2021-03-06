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

package org.openlegacy.db.definitions;

/**
 * @author Ivan Bort
 * 
 */
public interface DbJoinColumnDefinition {

	String getName();

	String getReferencedColumnName();

	boolean isUnique();

	boolean isNullable();

	boolean isInsertable();

	boolean isUpdatable();

	String getColumnDefinition();

	String getTable();
}
