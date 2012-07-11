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
package org.openlegacy.modules.table;

import org.openlegacy.modules.SessionModule;
import org.openlegacy.modules.table.drilldown.DrilldownAction;

import java.util.List;

/**
 * Defines a table module. Performs actions on multiple entities (e.g: screens) like collecting multiple entities, drill down, etc
 * 
 */
public interface Table extends SessionModule {

	<T> List<T> collectAll(Class<?> entityClass, Class<T> rowClass);

	<T> List<T> collectOne(Class<?> entityClass, Class<T> rowClass);

	<T> T drillDown(Class<T> targetClass, DrilldownAction<?> drilldownAction, Object... rowKeys);

	<T> T drillDown(Class<?> entityClass, Class<T> targetClass, DrilldownAction<?> drilldownAction, Object... rowKeys);

}
