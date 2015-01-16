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
package org.openlegacy.designtime.db.generators;

import org.openlegacy.EntityDefinition;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.layout.DbPageBuilder;
import org.openlegacy.designtime.generators.AbstractEntitySpaGenerator;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.layout.PageDefinition;

import java.io.OutputStream;
import java.text.MessageFormat;

import javax.inject.Inject;

/**
 * Generates all angular Single Page Application web related content
 * 
 * 
 * 
 */
public class DbEntitySpaGenerator extends AbstractEntitySpaGenerator implements DbEntityPageGenerator {

	@Inject
	private DbPageBuilder pageBuilder;

	@Override
	public void generatePage(PageDefinition pageDefinition, OutputStream output, String templateDirectoryPrefix)
			throws GenerationException {
		String typeName = MessageFormat.format("{0}{1}", templateDirectoryPrefix,
				pageDefinition.getEntityDefinition().getTypeName());
		getGenerateUtil().generate(pageDefinition, output, "DbEntitySpaPage.html.template", typeName);

	}

	@Override
	protected PageDefinition buildPage(EntityDefinition<?> entityDefinition) {
		return pageBuilder.build((DbEntityDefinition)entityDefinition);
	}
}
