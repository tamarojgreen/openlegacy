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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntityDefinition;
import org.openlegacy.db.definitions.DbEntityDefinition;
import org.openlegacy.db.layout.DbPageBuilder;
import org.openlegacy.designtime.UserInteraction;
import org.openlegacy.designtime.generators.AbstractEntitySpaGenerator;
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
		String typeListName = MessageFormat.format("{0}{1}", templateDirectoryPrefix,
				((DbEntityDefinition)pageDefinition.getEntityDefinition()).getPluralName().trim().replace(" ", ""));
		getGenerateUtil().generate(pageDefinition, output, "DbEntitySpaPage.html.list.template", typeListName);
		String typeEditName = MessageFormat.format("{0}{1}", templateDirectoryPrefix,
				pageDefinition.getEntityDefinition().getEntityClassName());
		getGenerateUtil().generate(pageDefinition, output, "DbEntitySpaPage.html.edit.template", typeEditName);

	}

	@Override
	protected PageDefinition buildPage(EntityDefinition<?> entityDefinition) {
		return pageBuilder.build((DbEntityDefinition)entityDefinition);
	}

	@Override
	public void generateView(GenerateViewRequest generateViewRequest, EntityDefinition<?> entityDefinition)
			throws GenerationException {

		getGenerateUtil().setTemplateDirectory(generateViewRequest.getTemplatesDirectory());

		UserInteraction userInteraction = generateViewRequest.getUserInteraction();
		FileOutputStream fos = null;
		try {

			PageDefinition pageDefinition = buildPage(entityDefinition);
			DbEntityDefinition dbEntityDefinition = (DbEntityDefinition)entityDefinition;

			// generate web view
			generateView(generateViewRequest, pageDefinition, SpaGenerateUtil.VIEWS_DIR, userInteraction, false,
					dbEntityDefinition.getEntityClassName());

			generateEclipseEncodingSettings(generateViewRequest, entityDefinition, dbEntityDefinition.getEntityClassName());

			generateView(generateViewRequest, pageDefinition, SpaGenerateUtil.VIEWS_DIR, userInteraction, false,
					dbEntityDefinition.getPluralName().trim().replace(" ", ""));

			generateEclipseEncodingSettings(generateViewRequest, entityDefinition,
					dbEntityDefinition.getPluralName().trim().replace(" ", ""));

		} catch (Exception e) {
			throw (new GenerationException(e));
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	private void generateView(GenerateViewRequest generatePageRequest, PageDefinition pageDefinition, String viewsDir,
			UserInteraction userInteraction, boolean isComposite, String fileName) throws IOException {

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
				generatePage(pageDefinition, fos, "");
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

	private static void generateEclipseEncodingSettings(GenerateViewRequest generateViewRequest,
			EntityDefinition<?> entityDefinition, String fileName) throws IOException {
		File encodingFile = new File(generateViewRequest.getProjectPath(), SETTINGS_ORG_ECLIPSE_CORE_RESOURCES_PREFS);
		String fileContent = FileUtils.readFileToString(encodingFile, CharEncoding.UTF_8);
		if (!fileContent.contains(encodingFile.getName())) {
			fileContent = MessageFormat.format("{0}\nencoding//src/main/webapp/app/views/{1}.html=UTF8", fileContent, fileName);
		}
		FileUtils.write(encodingFile, fileContent);
	}

	@Override
	public void generateView(GenerateViewRequest generateViewRequest,
			EntityDefinition<?> entityDefinition, String fileName)
			throws GenerationException {
		getGenerateUtil().setTemplateDirectory(generateViewRequest.getTemplatesDirectory());

		UserInteraction userInteraction = generateViewRequest.getUserInteraction();
		FileOutputStream fos = null;
		try {

			PageDefinition pageDefinition = buildPage(entityDefinition);

			// generate web view
			generateView(generateViewRequest, pageDefinition, SpaGenerateUtil.VIEWS_DIR, userInteraction, false,
					fileName);

			generateEclipseEncodingSettings(generateViewRequest, entityDefinition,
					fileName);

		} catch (Exception e) {
			throw (new GenerationException(e));
		} finally {
			IOUtils.closeQuietly(fos);
		}
		
	}
}
