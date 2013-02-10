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
package org.openlegacy.loaders.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntitiesRegistry;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.terminal.definitions.SimpleScreenEntityDefinition;
import org.openlegacy.terminal.persistance.TerminalPersistedSnapshot;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.SimpleScreenSize;
import org.openlegacy.utils.StringUtil;
import org.openlegacy.utils.XmlSerializationUtil;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.text.MessageFormat;

@Component
@Order(1)
public class ScreenEntityAnnotationLoader extends AbstractClassAnnotationLoader {

	private final static Log logger = LogFactory.getLog(ScreenEntityAnnotationLoader.class);

	public boolean match(Annotation annotation) {
		return annotation.annotationType() == ScreenEntity.class;
	}

	@SuppressWarnings("rawtypes")
	public void load(EntitiesRegistry entitiesRegistry, Annotation annotation, Class<?> containingClass) {

		ScreenEntity screenEntity = (ScreenEntity)annotation;
		ScreenEntitiesRegistry screenEntitiesRegistry = (ScreenEntitiesRegistry)entitiesRegistry;

		String screenEntityName = screenEntity.name().length() > 0 ? screenEntity.name() : containingClass.getSimpleName();
		String displayName = screenEntity.displayName().length() > 0 ? screenEntity.displayName()
				: StringUtil.toDisplayName(screenEntityName);

		SimpleScreenEntityDefinition screenEntityDefinition = new SimpleScreenEntityDefinition(screenEntityName, containingClass);
		screenEntityDefinition.setEntityName(screenEntityName);
		screenEntityDefinition.setDisplayName(displayName);
		screenEntityDefinition.setType(screenEntity.screenType());
		screenEntityDefinition.setWindow(screenEntity.window());
		screenEntityDefinition.setChild(screenEntity.child());

		screenEntityDefinition.setScreenSize(new SimpleScreenSize(screenEntity.rows(), screenEntity.columns()));

		loadSnapshot(containingClass, screenEntityName, screenEntityDefinition);

		logger.info(MessageFormat.format("Screen \"{0}\" was added to the screen registry ({1})", screenEntityName,
				containingClass.getName()));

		screenEntitiesRegistry.add(screenEntityDefinition);
	}

	private static void loadSnapshot(Class<?> containingClass, String screenEntityName,
			SimpleScreenEntityDefinition screenEntityDefinition) {
		String snapshotXmlResourceName = MessageFormat.format("{0}-resources/{1}.xml", screenEntityName,
				containingClass.getSimpleName());
		URL snapshotXmlResource = containingClass.getResource(snapshotXmlResourceName);
		if (snapshotXmlResource != null) {
			try {
				TerminalPersistedSnapshot snapshot = XmlSerializationUtil.deserialize(TerminalPersistedSnapshot.class,
						snapshotXmlResource.openStream());
				screenEntityDefinition.setSnapshot(snapshot);
			} catch (Exception e) {
				logger.warn("Failed to load snapshot for " + screenEntityName);
			}
		}
	}
}
