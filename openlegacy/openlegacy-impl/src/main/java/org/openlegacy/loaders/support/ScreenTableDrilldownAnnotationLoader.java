package org.openlegacy.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenTableDrilldown;
import org.openlegacy.terminal.definitions.TableDefinition;
import org.openlegacy.terminal.definitions.TableDefinition.DrilldownDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;

@Component
public class ScreenTableDrilldownAnnotationLoader extends AbstractClassAnnotationLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenTableDrilldown.class;
	}

	public void load(EntitiesRegistry<?, ?> entitiesRegistry, Annotation annotation, Class<?> containingClass) {
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;
		ScreenTableDrilldown drilldownAnnotation = (ScreenTableDrilldown)annotation;

		TableDefinition tableDefinition = screenEntitiesRegistry.getTable(containingClass);
		Assert.notNull(tableDefinition, "@ScreenTableDrilldown must be define along @ScreenTable annotation");

		DrilldownDefinition drilldownDefinition = tableDefinition.getDrilldownDefinition();

		drilldownDefinition.setDrilldownPerformer(drilldownAnnotation.drilldownPerfomer());
		drilldownDefinition.setTableScroller(drilldownAnnotation.tableScroller());
		drilldownDefinition.setTableScrollStopConditions(drilldownAnnotation.stopConditions());

		drilldownDefinition.setRowFinder(drilldownAnnotation.rowFinder());
		drilldownDefinition.setRowSelector(drilldownAnnotation.rowSelector());
		drilldownDefinition.setRowComparator(drilldownAnnotation.rowComparator());

	}

}
