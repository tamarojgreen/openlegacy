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
import org.openlegacy.SessionAction;

/**
 * A drill down action represent a drill-down action on a table The row number is typically calculated by <code>RowFinder</code>
 * 
 */
public interface DrilldownAction<S extends Session> extends SessionAction<S> {

	Object getActionValue();

	void setActionValue(Object actionValue);
}
