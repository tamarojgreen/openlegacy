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
package org.openlegacy.designtime.generators;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntityDefinition;
import org.openlegacy.designtime.UserInteraction;
import org.openlegacy.designtime.mains.GenerateControllerRequest;
import org.openlegacy.designtime.mains.GenerateViewRequest;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.layout.PageDefinition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;

import javax.inject.Inject;

/**
 * Abstract class for generating SPA (Single Page Application) related content
 * 
 * @author roi
 * 
 */
public abstract class AbstractEntitySpaGenerator implements EntityPageGenerator {

	private static final String CONTROLLER_CODE_PLACE_HOLDER_START = "/* Controller code place-holder start";
	private static final String CONTROLLER_CODE_PLACE_HOLDER_END = "Controller code place-holder end */";

	private static final String EXISTING_CONTROLLER_CODE_PLACE_HOLDER_START = "// auto generated controller start - ";
	private static final String EXISTING_CONTROLLER_CODE_PLACE_HOLDER_END = "// auto generated controller end - ";

	private static final String REGISTER_CONTROLLER_PLACE_HOLDER_START = "/* Register controller place-holder start";
	private static final String REGISTER_CONTROLLER_PLACE_HOLDER_END = "Register controller place-holder end */";

	private static final String EXISTING_REGISTER_CONTROLLER_PLACE_HOLDER_START = "// auto generated register start - ";
	private static final String EXISTING_REGISTER_CONTROLLER_PLACE_HOLDER_END = "// auto generated register end - ";
	private static final String APP_JS = "app.js";
	private static final String CONTROLLERS_JS = "controllers.js";

	private final static Log logger = LogFactory.getLog(AbstractEntitySpaGenerator.class);

	@Inject
	private GenerateUtil generateUtil;

	public void generateView(GenerateViewRequest generateViewRequest, EntityDefinition<?> entityDefinition)
			throws GenerationException {

		getGenerateUtil().setTemplateDirectory(generateViewRequest.getTemplatesDirectory());

		UserInteraction userInteraction = generateViewRequest.getUserInteraction();
		FileOutputStream fos = null;
		try {

			PageDefinition pageDefinition = buildPage(entityDefinition);

			// generate web view
			generateView(generateViewRequest, pageDefinition, SpaGenerateUtil.VIEWS_DIR, userInteraction, false);

		} catch (Exception e) {
			throw (new GenerationException(e));
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	private void generateView(GenerateViewRequest generatePageRequest, PageDefinition pageDefinition, String viewsDir,
			UserInteraction userInteraction, boolean isComposite) throws IOException {

		EntityDefinition<?> entityDefinition = pageDefinition.getEntityDefinition();
		String entityClassName = entityDefinition.getEntityClassName();
		FileOutputStream fos = null;

		File pageFile = new File(generatePageRequest.getProjectPath(), MessageFormat.format("{0}{1}.html", viewsDir,
				entityClassName));
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

	protected abstract PageDefinition buildPage(EntityDefinition<?> entityDefinition);

	public void generateController(GenerateControllerRequest generateControllerRequest, EntityDefinition<?> entityDefinition)
			throws GenerationException {
		updateAppJs(generateControllerRequest, entityDefinition);
		updateControllersJs(generateControllerRequest, entityDefinition);
	}

	private static void generateControllerFromView(GenerateControllerRequest generateControllerRequest,
			EntityDefinition<?> entityDefinition) throws GenerationException {
		// updateAppJs(generateControllerRequest, entityDefinition);
		// updateControllersJs(generateControllerRequest, entityDefinition);
	}

	private static void updateControllersJs(GenerateControllerRequest generateControllerRequest,
			EntityDefinition<?> entityDefinition) {
		File controllersJsFile = new File(generateControllerRequest.getProjectPath(), SpaGenerateUtil.JS_APP_DIR + CONTROLLERS_JS);

		String name = entityDefinition.getEntityName();
		GenerateUtil.replicateTemplate(controllersJsFile, entityDefinition, CONTROLLER_CODE_PLACE_HOLDER_START,
				CONTROLLER_CODE_PLACE_HOLDER_END, EXISTING_CONTROLLER_CODE_PLACE_HOLDER_START + name,
				EXISTING_CONTROLLER_CODE_PLACE_HOLDER_END + name);
	}

	private static void updateAppJs(GenerateControllerRequest generateControllerRequest, EntityDefinition<?> entityDefinition) {
		File appJsFile = new File(generateControllerRequest.getProjectPath(), SpaGenerateUtil.JS_APP_DIR + APP_JS);
		String name = entityDefinition.getEntityName();
		GenerateUtil.replicateTemplate(appJsFile, entityDefinition, REGISTER_CONTROLLER_PLACE_HOLDER_START,
				REGISTER_CONTROLLER_PLACE_HOLDER_END, EXISTING_REGISTER_CONTROLLER_PLACE_HOLDER_START + name,
				EXISTING_REGISTER_CONTROLLER_PLACE_HOLDER_END + name);
	}

	public GenerateUtil getGenerateUtil() {
		return generateUtil;
	}

	public boolean isSupportControllerGeneration() {
		return true;
	}

	public void renameViews(String fileNoExtension, String newName, File projectPath) {
		// views
		File viewsDir = new File(projectPath, SpaGenerateUtil.VIEWS_DIR);
		if (viewsDir.exists()) {
			File[] listFiles = viewsDir.listFiles();
			for (File file : listFiles) {
				if (file.getAbsolutePath().endsWith(MessageFormat.format("{0}.{1}", fileNoExtension, "html"))) {
					file.renameTo(new File(file.getParentFile(), MessageFormat.format("{0}.{1}", newName, "html")));
					break;
				}
				if (file.getAbsolutePath().endsWith(
						MessageFormat.format("{0}.{1}", StringUtils.uncapitalize(fileNoExtension), "html"))) {
					file.renameTo(new File(file.getParentFile(), MessageFormat.format("{0}.{1}",
							StringUtils.uncapitalize(newName), "html")));
					break;
				}
			}
		}
		// app.js
		File appJsFile = new File(projectPath, SpaGenerateUtil.JS_APP_DIR + APP_JS);
		FileOutputStream fos = null;
		try {
			String appJsFileContent = FileUtils.readFileToString(appJsFile);
			// replace e.g.: /items -> /newItems
			appJsFileContent = appJsFileContent.replaceAll(
					MessageFormat.format("/{0}", StringUtils.uncapitalize(fileNoExtension)),
					MessageFormat.format("/{0}", StringUtils.uncapitalize(newName)));
			// replace e.g.: itemsController -> newItemsController
			appJsFileContent = appJsFileContent.replaceAll(
					MessageFormat.format("{0}Controller", StringUtils.uncapitalize(fileNoExtension)),
					MessageFormat.format("{0}Controller", StringUtils.uncapitalize(newName)));
			fos = new FileOutputStream(appJsFile);
			IOUtils.write(appJsFileContent, fos);
		} catch (IOException e) {
			logger.info(e.getMessage());
		} finally {
			IOUtils.closeQuietly(fos);
		}
		// controller.js
		File controllersJsFile = new File(projectPath, SpaGenerateUtil.JS_APP_DIR + CONTROLLERS_JS);
		fos = null;
		try {
			String controllerJsFileContent = FileUtils.readFileToString(controllersJsFile);
			// replace e.g.: itemsController -> newItemsController
			controllerJsFileContent = controllerJsFileContent.replaceAll(
					MessageFormat.format("{0}Controller", StringUtils.uncapitalize(fileNoExtension)),
					MessageFormat.format("{0}Controller", StringUtils.uncapitalize(newName)));
			// replace e.g.: Items -> NewItems
			controllerJsFileContent = controllerJsFileContent.replaceAll(fileNoExtension, newName);
			fos = new FileOutputStream(controllersJsFile);
			IOUtils.write(controllerJsFileContent, fos);
		} catch (IOException e) {
			logger.info(e.getMessage());
		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

}
