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
 * Perform a scroll on the given session and the given entity class. The row keys are provided as a helper in order to help decide
 * on the scrolling direction
 * 
 */
public interface TableScroller<S extends Session, T> {

	T scroll(S session, Class<T> entityClass, TableScrollStopConditions<T> tableScrollStopConditions, Object... rowKeys);
}
