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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.PartPosition;
import org.openlegacy.loaders.support.AbstractClassAnnotationLoader;
import org.openlegacy.terminal.PositionedPart;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;

@Component
public class PartPositionAnnotationLoader extends AbstractClassAnnotationLoader {

	private final static Log logger = LogFactory.getLog(PartPositionAnnotationLoader.class);

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == PartPosition.class;
	}

	@SuppressWarnings({ "rawtypes" })
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {
		PartPosition partPosition = (PartPosition)annotation;
		ScreenEntitiesRegistry screenEntityRegistry = (ScreenEntitiesRegistry)entitiesRegistry;
		// check if the annotation is over a screen part
		PositionedPart positionedPart = (PositionedPart)screenEntityRegistry.getPart(containingClass);

		// check if the annotation is over a screen table
		if (positionedPart == null) {
			positionedPart = (PositionedPart)screenEntityRegistry.getTable(containingClass);
		}
		positionedPart.setWidth(partPosition.width());

		if (partPosition.row() > 0 && partPosition.column() > 0) {
			TerminalPosition position = SimpleTerminalPosition.newInstance(partPosition.row(), partPosition.column());
			logger.debug(MessageFormat.format("Position {0} was loaded for screen part:{1}", position, containingClass));
			positionedPart.setPartPosition(position);
		}
	}
}
