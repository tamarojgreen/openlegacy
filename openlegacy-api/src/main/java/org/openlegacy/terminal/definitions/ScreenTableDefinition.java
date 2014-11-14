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
package org.openlegacy.terminal.definitions;

import org.openlegacy.annotations.screen.ScreenColumn;
import org.openlegacy.annotations.screen.ScreenTable;
import org.openlegacy.annotations.screen.ScreenTableDrilldown;
import org.openlegacy.definitions.TableDefinition;
import org.openlegacy.modules.table.TableCollector;
import org.openlegacy.modules.table.drilldown.RowComparator;
import org.openlegacy.modules.table.drilldown.RowFinder;
import org.openlegacy.modules.table.drilldown.RowSelector;
import org.openlegacy.modules.table.drilldown.TableDrilldownPerformer;
import org.openlegacy.modules.table.drilldown.TableScrollStopConditions;
import org.openlegacy.modules.table.drilldown.TableScroller;
import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.ScreenColumnDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;

import java.util.List;

/**
 * Defines all the information required to manage terminal session tables including drill-down definitions Typically loaded from
 * 
 * {@link ScreenTable}, {@link ScreenTableDrilldown} & {@link ScreenColumn} annotations and stored within
 * {@link ScreenEntitiesRegistry}
 * 
 * @author Roi Mor
 */
@SuppressWarnings("rawtypes")
public interface ScreenTableDefinition extends TableDefinition<ScreenColumnDefinition> {

	int getStartRow();

	int getEndRow();

	int getRowGaps();

	TerminalAction getNextScreenAction();

	TerminalAction getPreviousScreenAction();

	DrilldownDefinition getDrilldownDefinition();

	Class<? extends TableCollector> getTableCollector();

	void setTableCollector(Class<? extends TableCollector> tableCollector);

	String getRowSelectionField();

	ScreenColumnDefinition getSelectionColumn();

	int getScreensCount();

	List<ScreenTableReferenceDefinition> getTableReferenceDefinitions();

	/**
	 * A spring expression to define filter on current row
	 * 
	 * @return
	 */
	String getFilterExpression();

	boolean isRtlDirection();

	public interface ScreenColumnDefinition extends TableDefinition.ColumnDefinition {

		boolean isSelectionField();

		int getStartColumn();

		int getEndColumn();

		int getLength();

		FieldAttributeType getAttribute();

		/**
		 * Calculate the value of the column field using a Spring Expression. This makes the field read only. The entity class is
		 * the root context, so you can refer to other fields by their names. Info about the current field is available in the
		 * #field variable. So, you can refer to the value of the current field with #field.value
		 * 
		 * If the expression starts and ends with a /, the expression is processed as a regular expression- not a spring
		 * expression. This expression takes the form of /&lt;expression>/&lt;replacement>/
		 * 
		 * @see <a
		 *      href="http://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/expressions.html">Spring
		 *      Expression Reference</a>
		 * 
		 * @see {@link java.util.regex.Pattern.compile}
		 * @return the Spring expression or regular expression used for generating the value of the field
		 */

		String getExpression();
	}

	public interface ScreenTableReferenceDefinition {

		String getFieldName();

		Class<?> getRelatedTableRecord();

		Class<?> getRelatedScreen();
	}

	public interface DrilldownDefinition {

		Class<? extends RowFinder> getRowFinder();

		Class<? extends RowSelector> getRowSelector();

		Class<? extends RowComparator> getRowComparator();

		Class<? extends TableScroller> getTableScroller();

		Class<? extends TableScrollStopConditions> getTableScrollStopCondition();

		Class<? extends TableDrilldownPerformer> getDrilldownPerformer();

		void setRowFinder(Class<? extends RowFinder> rowFinder);

		void setRowComparator(Class<? extends RowComparator> rowComparator);

		void setRowSelector(Class<? extends RowSelector> rowSelector);

		void setTableScroller(Class<? extends TableScroller> tableScroller);

		void setTableScrollStopConditions(Class<? extends TableScrollStopConditions> tableScrollStopConditions);

		void setDrilldownPerformer(Class<? extends TableDrilldownPerformer> drilldownPerformer);
	}

}
