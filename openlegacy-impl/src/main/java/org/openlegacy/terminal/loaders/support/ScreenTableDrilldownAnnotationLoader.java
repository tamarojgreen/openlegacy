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
package org.openlegacy.terminal.loaders.support;

import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenTableDrilldown;
import org.openlegacy.loaders.support.AbstractClassAnnotationLoader;
import org.openlegacy.terminal.definitions.ScreenTableDefinition;
import org.openlegacy.terminal.definitions.ScreenTableDefinition.DrilldownDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;

@Component
public class ScreenTableDrilldownAnnotationLoader extends AbstractClassAnnotationLoader {

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenTableDrilldown.class;
	}

	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;
		ScreenTableDrilldown drilldownAnnotation = (ScreenTableDrilldown)annotation;

		ScreenTableDefinition tableDefinition = screenEntitiesRegistry.getTable(containingClass);
		Assert.notNull(tableDefinition, MessageFormat.format(
				"@ScreenTableDrilldown for class {0} should be define along @ScreenTable annotation", containingClass));

		DrilldownDefinition drilldownDefinition = tableDefinition.getDrilldownDefinition();

		drilldownDefinition.setDrilldownPerformer(drilldownAnnotation.drilldownPerfomer());
		drilldownDefinition.setTableScroller(drilldownAnnotation.tableScroller());
		drilldownDefinition.setTableScrollStopConditions(drilldownAnnotation.stopConditions());

		drilldownDefinition.setRowFinder(drilldownAnnotation.rowFinder());
		drilldownDefinition.setRowSelector(drilldownAnnotation.rowSelector());
		drilldownDefinition.setRowComparator(drilldownAnnotation.rowComparator());

	}

}
