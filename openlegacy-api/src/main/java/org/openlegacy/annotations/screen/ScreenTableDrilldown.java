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
package org.openlegacy.annotations.screen;

import org.openlegacy.modules.table.Table;
import org.openlegacy.modules.table.drilldown.RowComparator;
import org.openlegacy.modules.table.drilldown.RowFinder;
import org.openlegacy.modules.table.drilldown.RowSelector;
import org.openlegacy.modules.table.drilldown.TableDrilldownPerformer;
import org.openlegacy.modules.table.drilldown.TableScrollStopConditions;
import org.openlegacy.modules.table.drilldown.TableScroller;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies custom table drill down implementation for {@link ScreenTable} entity<br/>
 * Allows customized drill down logic into tables by specifying various drill-down logic implementations
 * 
 * @author Roi Mor
 * @see Table
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SuppressWarnings("rawtypes")
public @interface ScreenTableDrilldown {

	/**
	 * Defines a drill-down performer. Performer is the most high-level drill-down component and allows customization of the
	 * entire drill-down logic. It is recommended to avoid implementing it, and use the other components.
	 * 
	 * @return
	 */
	Class<? extends TableDrilldownPerformer> drilldownPerfomer() default TableDrilldownPerformer.class;

	/**
	 * Defines how to find a row in a given screen
	 * 
	 * @return {@link RowFinder} implementation
	 */
	Class<? extends RowFinder> rowFinder() default RowFinder.class;

	/**
	 * Defines how to compare row to a row key
	 * 
	 * @return {@link RowComparator} implementation
	 */
	Class<? extends RowComparator> rowComparator() default RowComparator.class;

	/**
	 * Defines how to scroll within a table
	 * 
	 * @return {@link TableScroller} implementation
	 */
	Class<? extends TableScroller> tableScroller() default TableScroller.class;

	/**
	 * Defines how to select a given row within a table
	 * 
	 * @return {@link RowSelector} implementation
	 */
	Class<? extends RowSelector> rowSelector() default RowSelector.class;

	/**
	 * Defines wehn to stop a table scrolling
	 * 
	 * @return {@link TableScrollStopConditions} implementation
	 */
	Class<? extends TableScrollStopConditions> stopConditions() default TableScrollStopConditions.class;
}
