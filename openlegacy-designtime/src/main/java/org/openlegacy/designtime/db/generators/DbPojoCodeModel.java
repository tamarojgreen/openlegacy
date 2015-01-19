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
package org.openlegacy.designtime.db.generators;

import org.openlegacy.db.definitions.DbNavigationDefinition;
import org.openlegacy.db.definitions.DbTableDefinition;
import org.openlegacy.designtime.db.generators.support.DefaultDbPojoCodeModel.Action;
import org.openlegacy.designtime.db.generators.support.DefaultDbPojoCodeModel.ColumnField;
import org.openlegacy.designtime.db.generators.support.DefaultDbPojoCodeModel.Field;
import org.openlegacy.designtime.generators.PojoCodeModel;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * An interface which model the code model of DB classes annotation with @Entity
 * 
 * 
 */
public interface DbPojoCodeModel extends PojoCodeModel {

	Collection<Field> getFields();

	List<Action> getActions();

	String getName();

	String getPluralName();

	DbTableDefinition getTableDefinition();

	Map<String, ColumnField> getColumnFields();

	DbNavigationDefinition getNavigationDefinition();

	boolean isWindow();

	boolean isChild();
}
