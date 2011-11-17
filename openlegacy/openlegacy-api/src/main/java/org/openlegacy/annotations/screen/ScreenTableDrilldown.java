package org.openlegacy.annotations.screen;

import org.openlegacy.modules.table.drilldown.RowComparator;
import org.openlegacy.modules.table.drilldown.RowFinder;
import org.openlegacy.modules.table.drilldown.RowSelector;
import org.openlegacy.modules.table.drilldown.TableScrollStopConditions;
import org.openlegacy.terminal.table.ScreenTableDrilldownPerformer;
import org.openlegacy.terminal.table.ScreenTableScroller;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Specifies that the class is a screen entity. Screens defined as @ScreenEntity are scanned and put into ScreenEntitiesRegistry <br/>
 * <br/>
 * 
 * @ScreenEntity </code>
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@SuppressWarnings("rawtypes")
public @interface ScreenTableDrilldown {

	Class<? extends ScreenTableDrilldownPerformer> drilldownPerfomer() default ScreenTableDrilldownPerformer.class;

	Class<? extends RowFinder> rowFinder() default RowFinder.class;

	Class<? extends RowComparator> rowComparator() default RowComparator.class;

	Class<? extends ScreenTableScroller> tableScroller() default ScreenTableScroller.class;

	Class<? extends RowSelector> rowSelector() default RowSelector.class;

	Class<? extends TableScrollStopConditions> stopConditions() default TableScrollStopConditions.class;
}
