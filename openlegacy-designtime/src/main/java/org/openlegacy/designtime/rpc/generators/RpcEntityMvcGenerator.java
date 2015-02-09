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
package org.openlegacy.designtime.rpc.generators;

import freemarker.template.TemplateException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntityDefinition;
import org.openlegacy.definitions.page.support.SimplePageDefinition;
import org.openlegacy.designtime.UserInteraction;
import org.openlegacy.designtime.generators.AbstractEntityMvcGenerator;
import org.openlegacy.designtime.generators.ProjectUtil;
import org.openlegacy.designtime.mains.GenerateControllerRequest;
import org.openlegacy.designtime.mains.GenerateViewRequest;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.layout.RpcPageBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;

import javax.inject.Inject;

/**
 * Generates all Spring MVC web related content
 * 
 * @author Roi Mor
 * 
 */
public class RpcEntityMvcGenerator extends AbstractEntityMvcGenerator implements RpcEntityPageGenerator {

	private final static Log logger = LogFactory.getLog(RpcEntityMvcGenerator.class);

	@Inject
	private RpcPageBuilder pageBuilder;

	@Inject
	private RpcHelpGenerator helpGenerator;

	@Override
	public void generatePage(PageDefinition pageDefinition, OutputStream output, String templateDirectoryPrefix) {
		getGenerateUtil().generate(pageDefinition, output, "RpcEntityMvcPage.jspx.template", templateDirectoryPrefix);
	}

	public void generateController(PageDefinition pageDefinition, OutputStream output) {
		String typeName = pageDefinition.getEntityDefinition().getTypeName();
		getGenerateUtil().generate(pageDefinition, output, "rpcEntityMvcController.java.template", typeName);
	}

	public void generateControllerAspect(PageDefinition pageDefinition, OutputStream output) {
		// TODO implement
	}

	/**
	 * Generate all web page related content: jspx, controller, controller aspect file, and views.xml file
	 */
	@Override
	public void generateView(GenerateViewRequest generatePageRequest, EntityDefinition<?> entityDefinition)
			throws GenerationException {
		generateView(generatePageRequest, entityDefinition, false);
	}

	@Override
	public void generateCompositePage(EntityDefinition<?> entityDefinition, OutputStream output, String templateDirectoryPrefix) {
		// TODO implement
	}

	@Override
	public void generateController(GenerateControllerRequest generateControllerRequest, EntityDefinition<?> entityDefinition)
			throws GenerationException {

		getGenerateUtil().setTemplateDirectory(generateControllerRequest.getTemplatesDirectory());

		UserInteraction userInteraction = generateControllerRequest.getUserInteraction();
		FileOutputStream fos = null;
		try {

			File packageDir = new File(generateControllerRequest.getSourceDirectory(),
					generateControllerRequest.getPackageDirectory());
			String entityClassName = entityDefinition.getEntityClassName();

			File contollerFile = new File(packageDir, entityClassName + "Controller.java");
			boolean generateController = true;
			if (contollerFile.exists()) {
				boolean override = userInteraction.isOverride(contollerFile);
				if (!override) {
					generateController = false;
				}
			}

			SimplePageDefinition pageDefinition = (SimplePageDefinition)pageBuilder.build((RpcEntityDefinition)entityDefinition);
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

		} catch (Exception e) {
			throw (new GenerationException(e));
		} finally {
			IOUtils.closeQuietly(fos);
		}

	}

	@Override
	protected PageDefinition buildPage(EntityDefinition<?> entityDefinition) {
		return new SimplePageDefinition(entityDefinition);
	}

	@Override
	public void generateHelp(PageDefinition pageDefinition, OutputStream out, File projectPath) throws TemplateException,
			IOException {
		helpGenerator.generate(pageDefinition, out);
		ProjectUtil.generatEclipseEncodingSettings(projectPath,
				MessageFormat.format("/src/main/webapp/help/{0}.html", pageDefinition.getEntityDefinition().getEntityName()));
	}

	@Override
	public boolean isSupportRestControllerGeneration() {
		return false;
	}

	@Override
	public void generateRestController(GenerateControllerRequest generateControllerRequest, EntityDefinition<?> entityDefinition) {
		// not supported
	}

}
