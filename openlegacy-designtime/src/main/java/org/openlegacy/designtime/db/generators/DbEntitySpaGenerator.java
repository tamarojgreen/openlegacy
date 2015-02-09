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

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntityDefinition;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.layout.DbPageBuilder;
import org.openlegacy.designtime.UserInteraction;
import org.openlegacy.designtime.generators.AbstractEntitySpaGenerator;
import org.openlegacy.designtime.generators.ProjectUtil;
import org.openlegacy.designtime.generators.SpaGenerateUtil;
import org.openlegacy.designtime.mains.GenerateViewRequest;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.layout.PageDefinition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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

	private final static Log logger = LogFactory.getLog(DbEntitySpaGenerator.class);

	@Override
	public void generatePage(PageDefinition pageDefinition, OutputStream output, String templateDirectoryPrefix)
			throws GenerationException {
		String typeEditName = MessageFormat.format("{0}{1}", templateDirectoryPrefix,
				pageDefinition.getEntityDefinition().getEntityClassName());
		getGenerateUtil().generate(pageDefinition, output, "DbEntitySpaPage.html.template", typeEditName);

	}

	@Override
	public void generatePage(PageDefinition pageDefinition, OutputStream output, String templateDirectoryPrefix, String mode)
			throws GenerationException {
		if (mode == LIST_MODE) {
			String typeListName = MessageFormat.format("{0}{1}", templateDirectoryPrefix,
					((DbEntityDefinition)pageDefinition.getEntityDefinition()).getPluralName().trim().replace(" ", ""));
			getGenerateUtil().generate(pageDefinition, output, "DbEntitySpaPage.html.list.template", typeListName);
		} else if (mode == EDIT_MODE) {
			String typeEditName = MessageFormat.format("{0}{1}", templateDirectoryPrefix,
					pageDefinition.getEntityDefinition().getEntityClassName());
			getGenerateUtil().generate(pageDefinition, output, "DbEntitySpaPage.html.edit.template", typeEditName);
		}
	}

	@Override
	protected PageDefinition buildPage(EntityDefinition<?> entityDefinition) {
		return pageBuilder.build((DbEntityDefinition)entityDefinition);
	}

	private void generateView(GenerateViewRequest generatePageRequest, PageDefinition pageDefinition, String viewsDir,
			UserInteraction userInteraction, boolean isComposite, String fileName, String mode) throws IOException {

		EntityDefinition<?> entityDefinition = pageDefinition.getEntityDefinition();
		FileOutputStream fos = null;

		File pageFile = new File(generatePageRequest.getProjectPath(), MessageFormat.format("{0}{1}.html", viewsDir, fileName));
		boolean pageFileExists = pageFile.exists();
		boolean generateView = true;
		if (pageFileExists) {
			boolean override = userInteraction.isOverride(pageFile);
			if (!override) {
				generateView = false;
			}
		}
		if (generateView) {
			pageFile.getParentFile().mkdirs();
			fos = new FileOutputStream(pageFile);
			try {
				generatePage(pageDefinition, fos, "", mode);
				logger.info(MessageFormat.format("Generated html file: {0}", pageFile.getAbsoluteFile()));
			} finally {
				IOUtils.closeQuietly(fos);
				org.openlegacy.utils.FileUtils.deleteEmptyFile(pageFile);
			}
		}

		if (pageFile.exists()) {
			userInteraction.open(pageFile, entityDefinition);
		}
	}

	@Override
	public void generateView(GenerateViewRequest generateViewRequest, EntityDefinition<?> entityDefinition, String fileName,
			String mode) throws GenerationException {
		getGenerateUtil().setTemplateDirectory(generateViewRequest.getTemplatesDirectory());

		UserInteraction userInteraction = generateViewRequest.getUserInteraction();
		FileOutputStream fos = null;
		try {

			PageDefinition pageDefinition = buildPage(entityDefinition);

			// generate web view
			generateView(generateViewRequest, pageDefinition, SpaGenerateUtil.VIEWS_DIR, userInteraction, false, fileName, mode);

			ProjectUtil.generatEclipseEncodingSettings(generateViewRequest.getProjectPath(),
					MessageFormat.format("/src/main/webapp/app/views/{0}.html", fileName));

		} catch (Exception e) {
			throw (new GenerationException(e));
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	@Override
	protected PageDefinition getPageDefinition(EntityDefinition<?> entityDefinition) {
		return pageBuilder.build((DbEntityDefinition)entityDefinition);
	}

	@Override
	protected void generateRestController(PageDefinition pageDefinition, OutputStream output) {
		String typeName = pageDefinition.getEntityDefinition().getTypeName();
		getGenerateUtil().generate(pageDefinition, output, "rest/DbEntityRestController.java.template", typeName);
	}

}
