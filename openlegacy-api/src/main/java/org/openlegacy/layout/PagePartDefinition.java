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
package org.openlegacy.layout;

import org.openlegacy.definitions.TableDefinition;

import java.util.List;

/**
 * Defines a page part definition. A page part represents a part within a page, which contains form columns (1 or more), row parts
 * ({@link PagePartRowDefinition}), and a potentially a table definition.
 * 
 * @author Roi Mor
 * 
 */
public interface PagePartDefinition {

	int getColumns();

	List<PagePartRowDefinition> getPartRows();

	String getDisplayName();

	int getWidth();

	int getLeftMargin();

	int getTopMargin();

	String getTableFieldName();

	TableDefinition<?> getTableDefinition();
}
