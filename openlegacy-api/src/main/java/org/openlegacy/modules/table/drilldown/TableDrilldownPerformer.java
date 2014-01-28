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
package org.openlegacy.modules.table.drilldown;

import org.openlegacy.Session;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.DrilldownDefinition;

/**
 * Performs a drill down action on the given session from sourceEntityClass to targetEntityClass using drill down action and the
 * given row keys
 * 
 */
public interface TableDrilldownPerformer<S extends Session> {

	<T> T drilldown(DrilldownDefinition drilldownDefinition, S session, Class<?> sourceEntityClass, Class<T> targetEntityClass,
			DrilldownAction<?> drilldownAction, Object... rowKeys);

}
