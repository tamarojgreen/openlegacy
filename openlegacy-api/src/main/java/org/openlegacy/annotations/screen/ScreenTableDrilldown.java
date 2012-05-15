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
 * Allows customized drill down logic into tables.
 * 
 * @see Table
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SuppressWarnings("rawtypes")
public @interface ScreenTableDrilldown {

	Class<? extends TableDrilldownPerformer> drilldownPerfomer() default TableDrilldownPerformer.class;

	Class<? extends RowFinder> rowFinder() default RowFinder.class;

	Class<? extends RowComparator> rowComparator() default RowComparator.class;

	Class<? extends TableScroller> tableScroller() default TableScroller.class;

	Class<? extends RowSelector> rowSelector() default RowSelector.class;

	Class<? extends TableScrollStopConditions> stopConditions() default TableScrollStopConditions.class;
}
