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
package org.openlegacy.modules.table.drilldown;

import org.openlegacy.Session;

/**
 * Row selector performs a row selection on the given entity and session
 * 
 */
public interface RowSelector<S extends Session, T> {

	<D extends DrilldownAction<?>> void selectRow(S session, T entity, D drilldownAction, int row);
}
