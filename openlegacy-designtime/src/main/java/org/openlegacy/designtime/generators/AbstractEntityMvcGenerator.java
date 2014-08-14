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

import static org.openlegacy.designtime.generators.MvcGenerateUtil.COMPOSITE_SUFFIX;
import static org.openlegacy.designtime.generators.MvcGenerateUtil.COMPOSITE_TEMPLATE;
import static org.openlegacy.designtime.generators.MvcGenerateUtil.TEMPLATE_WEB_DIR_PREFIX;
import freemarker.template.TemplateException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntityDefinition;
import org.openlegacy.designtime.UserInteraction;
import org.openlegacy.designtime.mains.GenerateViewRequest;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.layout.PageDefinition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.List;

import javax.inject.Inject;

public abstract class AbstractEntityMvcGenerator implements EntityPageGenerator {

	public String webViewsDir = "src/main/webapp/WEB-INF/web/views/";
	public String helpDir = "src/main/webapp/help/";

	public String viewsFile = "views.xml";

	private final static Log logger = LogFactory.getLog(AbstractEntityMvcGenerator.class);

	@Inject
	private GenerateUtil generateUtil;

	public abstract void generateHelp(PageDefinition pageDefinition, OutputStream out) throws TemplateException, IOException;

	/**
	 * Generate all web page related content: jspx, controller, controller aspect file, and views.xml file
	 */
	protected void generateView(GenerateViewRequest generateViewRequest, EntityDefinition<?> entityDefinition, boolean isChild)
			throws GenerationException {

		getGenerateUtil().setTemplateDirectory(generateViewRequest.getTemplatesDirectory());

		// Whether to generate a simple or composite page
		boolean isComposite = !isChild && entityDefinition.getChildEntitiesDefinitions().size() > 0;

		UserInteraction userInteraction = generateViewRequest.getUserInteraction();
		FileOutputStream fos = null;
		try {

			String entityClassName = entityDefinition.getEntityClassName();

			PageDefinition pageDefinition = buildPage(entityDefinition);

			if (generateViewRequest.isGenerateHelp()) {
				boolean generateHelp = true;
				File helpFile = new File(generateViewRequest.getProjectPath(), MessageFormat.format("{0}{1}.html", helpDir,
						entityClassName));
				if (helpFile.exists()) {
					boolean override = userInteraction.isOverride(helpFile);
					if (!override) {
						generateHelp = false;
					}
				}
				if (generateHelp) {
					helpFile.getParentFile().mkdirs();
					OutputStream out = new FileOutputStream(helpFile);
					try {
						generateHelp(pageDefinition, out);

					} finally {
						IOUtils.closeQuietly(out);
						org.openlegacy.utils.FileUtils.deleteEmptyFile(helpFile);
					}
				}
			}

			// generate web view
			String mvcTemplateType = MvcGenerateUtil.getMvcTemplateType(entityDefinition, isComposite, isChild);
			generateView(generateViewRequest, pageDefinition, webViewsDir, TEMPLATE_WEB_DIR_PREFIX, userInteraction, isComposite,
					mvcTemplateType, COMPOSITE_TEMPLATE);

		} catch (Exception e) {
			throw (new GenerationException(e));
		} finally {
			IOUtils.closeQuietly(fos);
		}

	}

	protected abstract PageDefinition buildPage(EntityDefinition<?> entityDefinition);

	private void generateView(GenerateViewRequest generatePageRequest, PageDefinition pageDefinition, String viewsDir,
			String templateDirectoryPrefix, UserInteraction userInteraction, boolean isComposite, String mvcTemplateType,
			String mvcCompositeTemplateType) throws IOException {

		EntityDefinition<?> entityDefinition = pageDefinition.getEntityDefinition();
		String entityClassName = entityDefinition.getEntityClassName();
		FileOutputStream fos = null;
		File pageCompositeFile = null;

		File pageFile = new File(generatePageRequest.getProjectPath(), MessageFormat.format("{0}{1}.jspx", viewsDir,
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
				generatePage(pageDefinition, fos, templateDirectoryPrefix);
				logger.info(MessageFormat.format("Generated jspx file: {0}", pageFile.getAbsoluteFile()));
			} finally {
				IOUtils.closeQuietly(fos);
				org.openlegacy.utils.FileUtils.deleteEmptyFile(pageFile);
			}

			// generate a composite page (with tabs)
			if (isComposite) {
				pageCompositeFile = new File(generatePageRequest.getProjectPath(), MessageFormat.format("{0}{1}Composite.jspx",
						viewsDir, entityClassName));
				fos = new FileOutputStream(pageCompositeFile);
				try {
					generateCompositePage(entityDefinition, fos, templateDirectoryPrefix);
				} finally {
					IOUtils.closeQuietly(fos);
					org.openlegacy.utils.FileUtils.deleteEmptyFile(pageCompositeFile);
				}
				List<EntityDefinition<?>> childScreens = entityDefinition.getChildEntitiesDefinitions();
				// generate page content for each of the child screens
				for (EntityDefinition<?> childDefinition : childScreens) {
					pageDefinition = buildPage(childDefinition);
					generateView(generatePageRequest, pageDefinition, viewsDir, templateDirectoryPrefix, userInteraction, false,
							mvcTemplateType, mvcCompositeTemplateType);
				}
			}

			// update views file only if web page wasn't exists (if exists, it's probably registered in views.xml)
			if (!pageFileExists) {
				// mvc template type is the name of a template file defined in layouts.xml

				String viewName = entityDefinition.getEntityClassName();

				String tilesViewsFile = viewsDir + viewsFile;
				MvcGenerateUtil.updateViewsFile(generatePageRequest.getProjectPath(), entityDefinition, viewName,
						mvcTemplateType, tilesViewsFile);

				if (isComposite) {
					// add view for composite entity
					MvcGenerateUtil.updateViewsFile(generatePageRequest.getProjectPath(), entityDefinition, viewName
							+ COMPOSITE_SUFFIX, mvcCompositeTemplateType, tilesViewsFile);
				}
			}

			if (pageFile.exists()) {
				userInteraction.open(pageFile, entityDefinition);
			}
			if (pageCompositeFile != null && pageCompositeFile.exists()) {
				userInteraction.open(pageCompositeFile, entityDefinition);
			}
		}
	}

	public abstract void generatePage(PageDefinition pageDefinition, OutputStream out, String typeName);

	/**
	 * Updates sprint views.xml file which contains all web page views definitions
	 * 
	 * @param projectDir
	 * @param entityDefinition
	 * @throws IOException
	 */

	public abstract void generateCompositePage(EntityDefinition<?> entityDefinition, OutputStream output,
			String templateDirectoryPrefix);

	public GenerateUtil getGenerateUtil() {
		return generateUtil;
	}

	public boolean isSupportControllerGeneration() {
		return true;
	}

	public void setWebViewsDir(String webViewsDir) {
		this.webViewsDir = webViewsDir;
	}

	public void setViewsFile(String viewsFile) {
		this.viewsFile = viewsFile;
	}

	public void setHelpDir(String helpDir) {
		this.helpDir = helpDir;
	}

	public void renameViews(String fileNoExtension, String newName, File projectPath) {
		// web
		try {
			renameViews(webViewsDir, viewsFile, fileNoExtension, newName, projectPath);
		} catch (IOException e) {
			logger.info(e.getMessage());
		}
	}

	private static void renameViews(String viewsDir, String viewsFile, String oldName, String newName, File projectPath)
			throws IOException {
		File webDir = new File(projectPath, viewsDir);
		if (webDir.exists()) {
			// rename views
			File[] listFiles = webDir.listFiles();
			for (File file : listFiles) {
				if (file.getAbsolutePath().endsWith(MessageFormat.format("{0}.{1}", oldName, "jspx"))) {
					file.renameTo(new File(file.getParentFile(), MessageFormat.format("{0}.{1}", newName, "jspx")));
					break;
				}
			}
			// rename definition in views description file
			File templateViewsFile = new File(projectPath, MessageFormat.format("{0}/{1}", viewsDir, viewsFile));
			FileOutputStream fos = null;
			try {
				String viewsFileContent = FileUtils.readFileToString(templateViewsFile);
				viewsFileContent = viewsFileContent.replaceAll(MessageFormat.format("name=\"{0}\"", oldName),
						MessageFormat.format("name=\"{0}\"", newName));
				viewsFileContent = viewsFileContent.replaceAll(MessageFormat.format("{0}.jspx", oldName),
						MessageFormat.format("{0}.jspx", newName));
				fos = new FileOutputStream(templateViewsFile);
				IOUtils.write(viewsFileContent, fos);
			} finally {
				IOUtils.closeQuietly(fos);
			}
		}
	}

}
