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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.EntityDefinition;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.MessageFormat;

public class MvcGenerateUtil {

	public static final String TILES_VIEW_PLACEHOLDER_START = "<!-- Marker for code generation start:";
	public static final String TILES_VIEW_PLACEHOLDER_END = "Marker for code generation end -->";

	public static final String VIEW_TOKEN = "VIEW-NAME";
	public static final String TEMPLATE_TOKEN = "TEMPLATE-NAME";

	public static final String DEFAULT_TEMPLATE = "template";
	public static final String VIEW_ONLY_TEMPLATE = "view";
	public static final String INNER_VIEW_MOBILE_TEMPLATE = "innerView";
	public static final String WINDOW_TEMPLATE = "window";

	public static final String COMPOSITE_SUFFIX = "Composite";
	public static final String COMPOSITE_TEMPLATE = "compositeTemplate";
	public static final String COMPOSITE_VIEW = "compositeView";

	public static final CharSequence TILES_VIEW_PLACEHOLDER = "<!-- Place holder for code generation -->";

	// must ends with slash
	public static final String TEMPLATE_WEB_DIR_PREFIX = "web/";
	public static final String TEMPLATE_MOBILE_DIR_PREFIX = "mobile/";

	private final static Log logger = LogFactory.getLog(MvcGenerateUtil.class);

	public static String getViewTemplate(EntityDefinition<?> entityDefinition, File viewsFile, String viewsFileContent) {
		// check for marker with typeName
		int tokenLength = TILES_VIEW_PLACEHOLDER_START.length() + entityDefinition.getTypeName().length();
		int templateMarkerStart = viewsFileContent.indexOf(TILES_VIEW_PLACEHOLDER_START + entityDefinition.getTypeName())
				+ tokenLength;
		// use default marker
		if (templateMarkerStart < tokenLength) {
			templateMarkerStart = viewsFileContent.indexOf(TILES_VIEW_PLACEHOLDER_START) + TILES_VIEW_PLACEHOLDER_START.length();
		}
		// use default marker
		int templateMarkerEnd = viewsFileContent.indexOf(entityDefinition.getTypeName() + ":" + TILES_VIEW_PLACEHOLDER_END) - 1;
		if (templateMarkerEnd < 0) {
			templateMarkerEnd = viewsFileContent.indexOf(TILES_VIEW_PLACEHOLDER_END) - 1;
		}
		if (templateMarkerStart < 0 || templateMarkerEnd < 0) {
			return null;
		}
		// replace tokens within the place holder tag
		String definitionTemplate = viewsFileContent.substring(templateMarkerStart, templateMarkerEnd);
		return definitionTemplate;
	}

	public static String getMvcTemplateType(EntityDefinition<?> entityDefinition, boolean isComposite, boolean isChild,
			boolean isMobile) {
		String mvcTemplateType = null;
		if (isMobile) {
			// in mobile - generate pages as views by default. composite (main screen) and it's child entities - generate as inner
			// views (child of view)
			mvcTemplateType = (isComposite || isChild) ? INNER_VIEW_MOBILE_TEMPLATE : VIEW_ONLY_TEMPLATE;
		} else {
			if (entityDefinition.isWindow()) {
				return WINDOW_TEMPLATE;
			}
			// in web - generate pages as template by default. composite (main screen) and it's child entities - generate as views
			mvcTemplateType = (isComposite || isChild) ? VIEW_ONLY_TEMPLATE : DEFAULT_TEMPLATE;
		}

		return mvcTemplateType;
	}

	public static void updateViewsFile(File projectDir, EntityDefinition<?> entityDefinition, String viewName,
			String mcvTemplateType, String tilesViewsFile) throws IOException {

		File viewsFile = new File(projectDir, tilesViewsFile);
		if (!viewsFile.exists()) {
			logger.warn(MessageFormat.format("Views file {0} not found in project directory:{1}", tilesViewsFile, projectDir));
		}

		FileOutputStream fos = null;
		try {
			// Find a marker block within Spring MVC tiles views.xml file
			String viewsFileContent = FileUtils.readFileToString(viewsFile);
			String definitionTemplate = MvcGenerateUtil.getViewTemplate(entityDefinition, viewsFile, viewsFileContent);
			if (definitionTemplate == null) {
				logger.warn(MessageFormat.format("Could not find template markers within views file: {0}",
						viewsFile.getAbsolutePath()));
				return;
			}
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
}
