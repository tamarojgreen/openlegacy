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
package org.openlegacy.designtime.terminal.generators;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.MessageFormat;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntityDefinition;
import org.openlegacy.definitions.page.support.SimplePageDefinition;
import org.openlegacy.designtime.UserInteraction;
import org.openlegacy.designtime.generators.AbstractEntityMvcGenerator;
import org.openlegacy.designtime.mains.GenerateControllerRequest;
import org.openlegacy.designtime.mains.GenerateViewRequest;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.layout.ScreenPageBuilder;

/**
 * Generates all Spring MVC web related content
 * 
 * @author Roi Mor
 * 
 */
public class ScreenEntityMvcGenerator extends AbstractEntityMvcGenerator implements ScreenEntityPageGenerator {

	private final static Log logger = LogFactory.getLog(ScreenEntityMvcGenerator.class);

	@Inject
	private ScreenPageBuilder pageBuilder;

	@Override
	public void generatePage(PageDefinition pageDefinition, OutputStream output, String templateDirectoryPrefix) {
		String typeName = MessageFormat.format("{0}{1}", templateDirectoryPrefix,
				pageDefinition.getEntityDefinition().getTypeName());
		getGenerateUtil().generate(pageDefinition, output, "ScreenEntityMvcPage.jspx.template", typeName);
	}

	public void generateController(PageDefinition pageDefinition, OutputStream output) {
		String typeName = pageDefinition.getEntityDefinition().getTypeName();
		getGenerateUtil().generate(pageDefinition, output, "ScreenEntityMvcController.java.template", typeName);
	}

	public void generateControllerAspect(PageDefinition pageDefinition, OutputStream output) {
		String typeName = pageDefinition.getEntityDefinition().getTypeName();
		getGenerateUtil().generate(pageDefinition, output, "ScreenEntityMvcController.aj.template", typeName);
	}

	/**
	 * Generate all web page related content: jspx, controller, controller aspect file, and views.xml file
	 */
	public void generateView(GenerateViewRequest generatePageRequest, EntityDefinition<?> screenEntityDefinition)
			throws GenerationException {

		if (((ScreenEntityDefinition)screenEntityDefinition).isChild()) {
			logger.warn("Skipping generation of child entity" + screenEntityDefinition.getEntityClassName());
			return;
		}

		generateView(generatePageRequest, screenEntityDefinition, false);
	}

	/**
	 * Generate all controller related content: controller, controller aspect file
	 */
	public void generateController(GenerateControllerRequest generateControllerRequest, EntityDefinition<?> screenEntityDefinition)
			throws GenerationException {

		if (((ScreenEntityDefinition)screenEntityDefinition).isChild()) {
			logger.warn("Skipping generation of child entity" + screenEntityDefinition.getEntityClassName());
			return;
		}

		generateController(generateControllerRequest, screenEntityDefinition, false);
	}

	/**
	 * Generate all controller related content: controller, controller aspect file
	 */
	private void generateController(GenerateControllerRequest generateControllerRequest, EntityDefinition<?> entityDefinition,
			boolean isChild) throws GenerationException {

		getGenerateUtil().setTemplateDirectory(generateControllerRequest.getTemplatesDirectory());

		// Whether to generate a simple or composite page
		boolean isComposite = !isChild && entityDefinition.getChildEntitiesDefinitions().size() > 0;

		UserInteraction userInteraction = generateControllerRequest.getUserInteraction();
		FileOutputStream fos = null;
		try {

			File packageDir = new File(generateControllerRequest.getSourceDirectory(),
					generateControllerRequest.getPackageDirectory());
			String entityClassName = entityDefinition.getEntityClassName();

			if (isComposite) {
				File compositeContollerFile = new File(packageDir, entityClassName + "CompositeController.java");
				boolean generateCompositeController = true;
				if (compositeContollerFile.exists()) {
					boolean override = userInteraction.isOverride(compositeContollerFile);
					if (!override) {
						generateCompositeController = false;
					}
				}
				if (generateCompositeController) {
					compositeContollerFile.getParentFile().mkdirs();
					fos = new FileOutputStream(compositeContollerFile);
					try {
						generateCompositeContoller(entityDefinition, fos);
					} finally {
						IOUtils.closeQuietly(fos);
						org.openlegacy.utils.FileUtils.deleteEmptyFile(compositeContollerFile);
					}

				}
			}

			File contollerFile = new File(packageDir, entityClassName + "Controller.java");
			boolean generateController = true;
			if (contollerFile.exists()) {
				boolean override = userInteraction.isOverride(contollerFile);
				if (!override) {
					generateController = false;
				}
			}

			SimplePageDefinition pageDefinition = (SimplePageDefinition)pageBuilder.build((ScreenEntityDefinition)entityDefinition);
			pageDefinition.setPackageName(generateControllerRequest.getPackageDirectory().replaceAll("/", "."));

			if (generateController) {
				contollerFile.getParentFile().mkdirs();
				fos = new FileOutputStream(contollerFile);
				try {
					generateController(pageDefinition, fos);
					logger.info(MessageFormat.format("Generated controller : {0}", contollerFile.getAbsoluteFile()));
				} finally {
					IOUtils.closeQuietly(fos);
					org.openlegacy.utils.FileUtils.deleteEmptyFile(contollerFile);
				}
			}

			if (generateController) {
				File contollerAspectFile = new File(packageDir, entityClassName + "Controller_Aspect.aj");
				fos = new FileOutputStream(contollerAspectFile);
				try {
					generateControllerAspect(pageDefinition, fos);
					logger.info(MessageFormat.format("Generated controller aspect: {0}", contollerAspectFile.getAbsoluteFile()));
				} finally {
					IOUtils.closeQuietly(fos);
					org.openlegacy.utils.FileUtils.deleteEmptyFile(contollerAspectFile);
				}
			}

		} catch (Exception e) {
			throw (new GenerationException(e));
		} finally {
			IOUtils.closeQuietly(fos);
		}

	}

	@Override
	public void generateCompositePage(EntityDefinition<?> entityDefinition, OutputStream output, String templateDirectoryPrefix) {
		getGenerateUtil().generate(entityDefinition, output,
				templateDirectoryPrefix + "ScreenEntityMvcCompositePage.jspx.template");
	}

	private void generateCompositeContoller(EntityDefinition<?> entityDefinition, OutputStream output) {
		getGenerateUtil().generate(entityDefinition, output, "ScreenEntityMvcCompositeController.java.template");

	}

	@Override
	protected PageDefinition buildPage(EntityDefinition<?> entityDefinition) {
		return pageBuilder.build((ScreenEntityDefinition)entityDefinition);
	}

}
