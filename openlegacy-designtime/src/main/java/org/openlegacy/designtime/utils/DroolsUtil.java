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
package org.openlegacy.designtime.utils;

import org.drools.KnowledgeBase;
import org.drools.KnowledgeBaseFactory;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.io.ResourceFactory;

public class DroolsUtil {

	private static final String FILE_PREFIX = "file:";

	public static KnowledgeBase createKnowledgeBase(String... droolFiles) {
		KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
		for (String droolFile : droolFiles) {
			ResourceType resourceType = ResourceType.determineResourceType(droolFile);
			if (droolFile.startsWith(FILE_PREFIX)) {
				builder.add(ResourceFactory.newFileResource(droolFile.substring(FILE_PREFIX.length())), resourceType);
			} else {
				builder.add(ResourceFactory.newClassPathResource(droolFile), resourceType);
			}
		}

		if (builder.hasErrors()) {
			throw new RuntimeException(builder.getErrors().toString());
		}
		KnowledgeBase knowledgeBase = KnowledgeBaseFactory.newKnowledgeBase();
		knowledgeBase.addKnowledgePackages(builder.getKnowledgePackages());
		return knowledgeBase;
	}
}
