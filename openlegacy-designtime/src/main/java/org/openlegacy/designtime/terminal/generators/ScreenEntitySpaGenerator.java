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
package org.openlegacy.designtime.terminal.generators;

import org.openlegacy.EntityDefinition;
import org.openlegacy.designtime.generators.AbstractEntitySpaGenerator;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.layout.ScreenPageBuilder;

import java.io.OutputStream;
import java.text.MessageFormat;

import javax.inject.Inject;

/**
 * Generates all angular Single Page Application web related content
 * 
 * @author Roi Mor
 * 
 */
public class ScreenEntitySpaGenerator extends AbstractEntitySpaGenerator implements ScreenEntityPageGenerator {

	@Inject
	private ScreenPageBuilder screenPageBuilder;

	@Override
	public void generatePage(PageDefinition pageDefinition, OutputStream output, String templateDirectoryPrefix)
			throws GenerationException {
		String typeName = MessageFormat.format("{0}{1}", templateDirectoryPrefix,
				pageDefinition.getEntityDefinition().getTypeName());
		getGenerateUtil().generate(pageDefinition, output, "ScreenEntitySpaPage.html.template", typeName);

	}

	@Override
	protected PageDefinition buildPage(EntityDefinition<?> entityDefinition) {
		return screenPageBuilder.build((ScreenEntityDefinition)entityDefinition);
	}

	@Override
	public void generateJsController(EntityDefinition<?> entityDefinition, OutputStream output, String templateDirectoryPrefix) {
		String typeName = MessageFormat.format("{0}{1}", templateDirectoryPrefix, entityDefinition.getTypeName());
		getGenerateUtil().generate(entityDefinition, output, "ScreenEntitySpaController.js.template", typeName);
	}
	
	@Override
	protected PageDefinition getPageDefinition(EntityDefinition<?> entityDefinition) {
		return screenPageBuilder.build((ScreenEntityDefinition)entityDefinition);
	}

	@Override
	protected void generateRestController(PageDefinition pageDefinition, OutputStream output) {
		String typeName = pageDefinition.getEntityDefinition().getTypeName();
		getGenerateUtil().generate(pageDefinition, output, "rest/ScreenEntityRestController.java.template", typeName);
	}

}
