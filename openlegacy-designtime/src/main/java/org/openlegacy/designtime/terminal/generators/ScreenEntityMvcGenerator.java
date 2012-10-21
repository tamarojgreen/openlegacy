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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntityDefinition;
import org.openlegacy.definitions.page.support.SimplePageDefinition;
import org.openlegacy.designtime.UserInteraction;
import org.openlegacy.designtime.mains.GeneratePageRequest;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.layout.support.DefaultScreenPageBuilder;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.MessageFormat;
import java.util.List;

import javax.inject.Inject;

/**
 * Generates all Spring MVC web related content
 * 
 * @author RoiM
 * 
 */
@Component
public class ScreenEntityMvcGenerator implements ScreenEntityWebGenerator {

	private static final String WEB_VIEWS_DIR = "src/main/webapp/WEB-INF/web/views/";
	private static final String MOBILE_VIEWS_DIR = "src/main/webapp/WEB-INF/mobile/views/";
	private static final String HELP_DIR = "src/main/webapp/help/";

	private static final String TILES_WEB_VIEWS_FILE = WEB_VIEWS_DIR + "/views.xml";
	private static final String TILES_MOBILE_VIEWS_FILE = MOBILE_VIEWS_DIR + "/views.xml";
	private static final String TILES_VIEW_PLACEHOLDER_START = "<!-- Marker for code generation start";
	private static final String TILES_VIEW_PLACEHOLDER_END = "Marker for code generation end -->";

	private static final String VIEW_TOKEN = "VIEW-NAME";
	private static final String TEMPLATE_TOKEN = "TEMPLATE-NAME";

	private static final String DEFAULT_TEMPLATE = "template";
	private static final String PUBLIC_TEMPLATE = "public";
	private static final String VIEW_ONLY_TEMPLATE = "view";
	private static final String INNER_VIEW_MOBILE_TEMPLATE = "innerView";
	
	private static final String COMPOSITE_SUFFIX = "Composite";
	private static final String COMPOSITE_TEMPLATE = "compositeTemplate";
	private static final String MENU_TEMPLATE = "menu";

	private static final CharSequence TILES_VIEW_PLACEHOLDER = "<!-- Place holder for code generation -->";

	// must ends with slash
	public static final String TEMPLATE_WEB_DIR_PREFIX = "web/";
	public static final String TEMPLATE_MOBILE_DIR_PREFIX = "mobile/";

	@Inject
	private GenerateUtil generateUtil;

	@Inject
	private HelpGenerator helpGenerator;

	private final static Log logger = LogFactory.getLog(ScreenEntityMvcGenerator.class);

	public void generatePage(PageDefinition pageDefinition, OutputStream output, String templateDirectoryPrefix) {
		String typeName = MessageFormat.format("{0}{1}", templateDirectoryPrefix,
				pageDefinition.getEntityDefinition().getTypeName());
		generateUtil.generate(pageDefinition, output, "ScreenEntityMvcPage.jspx.template", typeName);
	}

	public void generateController(PageDefinition pageDefinition, OutputStream output) {
		String typeName = pageDefinition.getEntityDefinition().getTypeName();
		generateUtil.generate(pageDefinition, output, "ScreenEntityMvcController.java.template", typeName);
	}

	public void generateControllerAspect(PageDefinition pageDefinition, OutputStream output) {
		String typeName = pageDefinition.getEntityDefinition().getTypeName();
		generateUtil.generate(pageDefinition, output, "ScreenEntityMvcController.aj.template", typeName);
	}

	/**
	 * Generate all web page related content: jspx, controller, controller aspect file, and views.xml file
	 */
	public void generateAll(GeneratePageRequest generatePageRequest, ScreenEntityDefinition screenEntityDefinition)
			throws GenerationException {

		if (screenEntityDefinition.isChild()) {
			logger.warn("Skipping generation of child entity" + screenEntityDefinition.getEntityClassName());
			return;
		}

		generateAll(generatePageRequest, screenEntityDefinition, false);
	}

	/**
	 * Generate all web page related content: jspx, controller, controller aspect file, and views.xml file
	 */
	private void generateAll(GeneratePageRequest generatePageRequest, EntityDefinition<?> entityDefinition, boolean isChild)
			throws GenerationException {

		generateUtil.setTemplateDirectory(generatePageRequest.getTemplatesDir());

		boolean isComposite = !isChild && entityDefinition.getChildEntitiesDefinitions().size() > 0;

		UserInteraction overrideConfirmer = generatePageRequest.getUserInteraction();
		FileOutputStream fos = null;
		try {

			File packageDir = new File(generatePageRequest.getSourceDirectory(), generatePageRequest.getPackageDirectoryName());
			String entityClassName = entityDefinition.getEntityClassName();

			if (isComposite) {
				File compositeContollerFile = new File(packageDir, entityClassName + "CompositeController.java");
				boolean generateCompositeController = true;
				if (compositeContollerFile.exists()) {
					boolean override = overrideConfirmer.isOverride(compositeContollerFile);
					if (!override) {
						generateCompositeController = false;
					}
				}
				if (generateCompositeController) {
					compositeContollerFile.getParentFile().mkdirs();
					fos = new FileOutputStream(compositeContollerFile);
					generateCompositeContoller(entityDefinition, fos);
					fos.close();
				}
			}

			File contollerFile = new File(packageDir, entityClassName + "Controller.java");
			boolean generateController = true;
			if (contollerFile.exists()) {
				boolean override = overrideConfirmer.isOverride(contollerFile);
				if (!override) {
					generateController = false;
				}
			}

			SimplePageDefinition pageDefinition = (SimplePageDefinition)new DefaultScreenPageBuilder().build((ScreenEntityDefinition)entityDefinition);
			pageDefinition.setPackageName(generatePageRequest.getPackageDirectoryName().replaceAll("/", "."));

			if (generateController) {
				contollerFile.getParentFile().mkdirs();
				fos = new FileOutputStream(contollerFile);
				generateController(pageDefinition, fos);
				fos.close();
				logger.info(MessageFormat.format("Generated controller : {0}", contollerFile.getAbsoluteFile()));
			}

			if (generatePageRequest.isGenerateHelp()) {
				boolean generateHelp = true;
				File helpFile = new File(generatePageRequest.getProjectDir(), MessageFormat.format("{0}{1}.html", HELP_DIR,
						entityClassName));
				if (helpFile.exists()) {
					boolean override = overrideConfirmer.isOverride(helpFile);
					if (!override) {
						generateHelp = false;
					}
				}
				if (generateHelp) {
					helpFile.getParentFile().mkdirs();
					OutputStream out = new FileOutputStream(helpFile);
					helpGenerator.generate(pageDefinition, out);
				}
			}

			if (generateController) {
				File contollerAspectFile = new File(packageDir, entityClassName + "Controller_Aspect.aj");
				fos = new FileOutputStream(contollerAspectFile);
				generateControllerAspect(pageDefinition, fos);
				fos.close();
				logger.info(MessageFormat.format("Generated controller aspect: {0}", contollerAspectFile.getAbsoluteFile()));
			}

			// generate web view
			String mvcTemplateType = getMvcTemplateType(entityDefinition, isComposite, isChild,false);
			generateView(generatePageRequest, entityDefinition, pageDefinition, WEB_VIEWS_DIR, TILES_WEB_VIEWS_FILE,
					TEMPLATE_WEB_DIR_PREFIX, overrideConfirmer, isComposite, mvcTemplateType);
			// generate mobile view
			if (generatePageRequest.isGenerateMobilePage()) {
				mvcTemplateType = getMvcTemplateType(entityDefinition, isComposite, isChild,true);
				generateView(generatePageRequest, entityDefinition, pageDefinition, MOBILE_VIEWS_DIR, TILES_MOBILE_VIEWS_FILE,
						TEMPLATE_MOBILE_DIR_PREFIX, overrideConfirmer, isComposite, mvcTemplateType);
			}

		} catch (Exception e) {
			throw (new GenerationException(e));
		} finally {
			IOUtils.closeQuietly(fos);
		}

	}

	/**
	 * Updates sprint views.xml file which contains all web page views definitions
	 * 
	 * @param projectDir
	 * @param entityDefinition
	 * @throws IOException
	 */
	private static void updateViewsFile(File projectDir, EntityDefinition<?> entityDefinition, String viewName,
			String mcvTemplateType, String tilesViewsFile) throws IOException {

		File viewsFile = new File(projectDir, tilesViewsFile);
		if (!viewsFile.exists()) {
			logger.warn(MessageFormat.format("Views file {0} not found in project directory:{1}", tilesViewsFile, projectDir));
		}

		FileOutputStream fos = null;
		try {
			// Find a marker block within Spring MVC tiles views.xml file
			String viewsFileContent = FileUtils.readFileToString(viewsFile);
			int templateMarkerStart = viewsFileContent.indexOf(TILES_VIEW_PLACEHOLDER_START)
					+ TILES_VIEW_PLACEHOLDER_START.length();
			int templateMarkerEnd = viewsFileContent.indexOf(TILES_VIEW_PLACEHOLDER_END) - 1;
			if (templateMarkerStart < 0 || templateMarkerEnd < 0) {
				logger.warn(MessageFormat.format("Could not find template markers within views file: {0}",
						viewsFile.getAbsolutePath()));
				return;
			}
			// replace tokens within the place holder tag
			String definitionTemplate = viewsFileContent.substring(templateMarkerStart, templateMarkerEnd);
			String newViewDefinition = definitionTemplate.replaceAll(VIEW_TOKEN, viewName);

			newViewDefinition = newViewDefinition.replaceAll(TEMPLATE_TOKEN, mcvTemplateType);

			viewsFileContent = viewsFileContent.replace(TILES_VIEW_PLACEHOLDER, TILES_VIEW_PLACEHOLDER + newViewDefinition);
			fos = new FileOutputStream(viewsFile);
			IOUtils.write(viewsFileContent, fos);

			logger.info(MessageFormat.format("Added view {0} to {1}", viewName, viewsFile.getAbsoluteFile()));

		} finally {
			IOUtils.closeQuietly(fos);
		}
	}

	public void generateCompositePage(EntityDefinition<?> entityDefinition, OutputStream output, String templateDirectoryPrefix) {
		generateUtil.generate(entityDefinition, output, templateDirectoryPrefix+ "ScreenEntityMvcCompositePage.jspx.template");
	}

	private void generateCompositeContoller(EntityDefinition<?> entityDefinition, OutputStream output) {
		generateUtil.generate(entityDefinition, output, "ScreenEntityMvcCompositeController.java.template");

	}

	private void generateView(GeneratePageRequest generatePageRequest, EntityDefinition<?> entityDefinition,
			SimplePageDefinition pageDefinition, String viewsDir, String tilesViewsFile, String templateDirectoryPrefix,
			UserInteraction overrideConfirmer, boolean isComposite, String mvcTemplateType) throws IOException {

		String entityClassName = entityDefinition.getEntityClassName();
		FileOutputStream fos = null;

		File pageFile = new File(generatePageRequest.getProjectDir(), MessageFormat.format("{0}{1}.jspx", viewsDir,
				entityClassName));
		boolean pageFileExists = pageFile.exists();
		boolean generatePage = true;
		if (pageFileExists) {
			boolean override = overrideConfirmer.isOverride(pageFile);
			if (!override) {
				generatePage = false;
			}
		}
		if (generatePage) {
			pageFile.getParentFile().mkdirs();
			fos = new FileOutputStream(pageFile);
			generatePage(pageDefinition, fos, templateDirectoryPrefix);
			fos.close();
			logger.info(MessageFormat.format("Generated jspx file: {0}", pageFile.getAbsoluteFile()));

			// generate a composite page (with tabs)
			if (isComposite) {
				File pageCompositeFile = new File(generatePageRequest.getProjectDir(), MessageFormat.format(
						"{0}{1}Composite.jspx", viewsDir, entityClassName));
				fos = new FileOutputStream(pageCompositeFile);
				generateCompositePage(entityDefinition, fos,templateDirectoryPrefix);
				List<EntityDefinition<?>> childScreens = entityDefinition.getChildEntitiesDefinitions();
				// generate page content for each of the child screens
				for (EntityDefinition<?> childDefinition : childScreens) {
					generateAll(generatePageRequest, childDefinition, true);
				}
			}

			// update views file only if web page wasn't exists (if exists, it's probably registered in views.xml)
			if (!pageFileExists) {
				// mvc template type is the name of a template file defined in layouts.xml

				String viewName = entityDefinition.getEntityClassName();

				updateViewsFile(generatePageRequest.getProjectDir(), entityDefinition, viewName, mvcTemplateType, tilesViewsFile);

				if (isComposite) {
					// add view for composite screen
					updateViewsFile(generatePageRequest.getProjectDir(), entityDefinition, viewName + COMPOSITE_SUFFIX,
							COMPOSITE_TEMPLATE, tilesViewsFile);
				}
			}

		}
	}

	private String getMvcTemplateType(EntityDefinition<?> entityDefinition,
			boolean isComposite, boolean isChild, boolean isMobile) {
		String mvcTemplateType = null;
		if (isMobile){
			// in mobile - generate pages as views by default. composite (main screen) and it's child entities - generate as inner views (child of view
			mvcTemplateType = (isComposite || isChild) ? INNER_VIEW_MOBILE_TEMPLATE : VIEW_ONLY_TEMPLATE;
		}
		else{
			// in web - generate pages as template by default. composite (main screen) and it's child entities - generate as views 
			mvcTemplateType = (isComposite || isChild) ? VIEW_ONLY_TEMPLATE : DEFAULT_TEMPLATE;
		}
		
		if (entityDefinition.getTypeName().equals(Login.LoginEntity.class.getSimpleName())) {
			mvcTemplateType = PUBLIC_TEMPLATE;
		}
		if (entityDefinition.getTypeName().equals(Menu.MenuEntity.class.getSimpleName())) {
			mvcTemplateType = MENU_TEMPLATE;
		}
		return mvcTemplateType;
	}

}
