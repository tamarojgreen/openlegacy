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

import java.util.List;

/**
 * Find a row within the given list which matches the given row keys Implementation may use RowComparator to determine if a given
 * row matches the given keys values
 * 
 * @author Roi Mor
 */
public interface RowFinder<T> {

	Integer findRow(RowComparator<T> rowComparator, List<T> tableRows, Object... rowKeys);

}
