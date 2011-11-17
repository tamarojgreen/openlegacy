package org.openlegacy.annotations.screen;

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
 * Specifies that the class is a screen entity. Screens defined as @ScreenEntity are scanned and put into ScreenEntitiesRegistry <br/>
 * <br/>
 * 
 * @ScreenEntity </code>
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ScreenTableDrilldown {

	@SuppressWarnings("rawtypes")
	Class<? extends TableDrilldownPerformer> drilldownPerfomer() default TableDrilldownPerformer.class;

	Class<? extends RowFinder> rowFinder() default RowFinder.class;

	Class<? extends RowComparator> rowComparator() default RowComparator.class;

	@SuppressWarnings("rawtypes")
	Class<? extends TableScroller> tableScroller() default TableScroller.class;

	@SuppressWarnings("rawtypes")
	Class<? extends RowSelector> rowSelector() default RowSelector.class;

	Class<? extends TableScrollStopConditions> stopConditions() default TableScrollStopConditions.class;
}
