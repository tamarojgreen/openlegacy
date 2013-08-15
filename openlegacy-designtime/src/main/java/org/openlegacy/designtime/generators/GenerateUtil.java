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
package org.openlegacy.designtime.generators;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.CharEncoding;
import org.apache.commons.lang.SystemUtils;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Locale;

@Component
public class GenerateUtil {

	private File templatesDir;

	public void setTemplateDirectory(File templatesDir) {
		this.templatesDir = templatesDir;
	}

	public void generate(Object model, OutputStream out, String templateName) throws GenerationException {
		generate(model, out, templateName, "");
	}

	public static String generate(Object model, StringReader templateString) throws GenerationException {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			OutputStreamWriter output = new OutputStreamWriter(baos, CharEncoding.UTF_8);
			Template template = new Template("Template", templateString, new Configuration());
			template.process(model, output);
			byte[] bytes = baos.toByteArray();
			return new String(bytes);
		} catch (TemplateException e) {
			throw (new GenerationException(e));
		} catch (IOException e) {
			throw (new GenerationException(e));
		}
	}

	public static void replicateTemplate(File file, Object model, String placeHolderStart, String placeHolderEnd,
			String existingCodeplaceHolderStart, String existingCodePlaceHolderEnd) {
		try {
			StringBuilder fileContent = new StringBuilder(FileUtils.readFileToString(file));

			int placeHolderReplaceMarkerStart = fileContent.indexOf(existingCodeplaceHolderStart);
			int placeHolderReplaceMarkerEnd = fileContent.indexOf(existingCodePlaceHolderEnd)
					+ existingCodePlaceHolderEnd.length();

			if (placeHolderReplaceMarkerStart > 0 && placeHolderReplaceMarkerStart > 0) {
				fileContent.delete(placeHolderReplaceMarkerStart, placeHolderReplaceMarkerEnd);
			}

			int templateMarkerStart = fileContent.indexOf(placeHolderStart);
			int templateMarkerEnd = fileContent.indexOf(placeHolderEnd) - 1;

			if (templateMarkerStart < 0 || templateMarkerEnd < 0) {
				return;
			}
			// replace tokens within the place holder tag
			String definitionTemplate = fileContent.substring(templateMarkerStart + placeHolderStart.length(), templateMarkerEnd);

			String definitionTemplateNew = generate(model, new StringReader(definitionTemplate));
			fileContent = fileContent.insert(templateMarkerStart, MessageFormat.format("{0}{1}{2}{3}",
					existingCodeplaceHolderStart, definitionTemplateNew, existingCodePlaceHolderEnd, SystemUtils.LINE_SEPARATOR));

			FileUtils.write(file, fileContent);

		} catch (IOException e) {
			throw (new GenerationException(e));
		}
	}

	/**
	 * 
	 * @param model
	 * @param out
	 * @param templateName
	 * @param templatePrefix
	 *            allows to work with template with prefix if exists. e.g: MenuScreenEntityMvcPage.jspx.template /
	 *            ScreenEntityMvcPage.jspx.template for generating a different page type for Menu screens
	 * @throws GenerationException
	 * @throws IOException
	 */
	public void generate(Object model, OutputStream out, String templateName, String templatePrefix) throws GenerationException {

		try {

			Configuration configuration = new Configuration();
			if (isUseCustomTemplates()) {
				configuration.setDirectoryForTemplateLoading(templatesDir);
			} else {
				configuration.setClassForTemplateLoading(GenerateUtil.class, "/");
			}
			configuration.setWhitespaceStripping(true);
			configuration.setEncoding(Locale.getDefault(), CharEncoding.UTF_8);

			Template template = null;
			try {
				if (isUseCustomTemplates()) {
					template = findTemplate(templateName, templatePrefix, configuration);
				}
			} catch (FileNotFoundException e) {
				// OK
			}
			// pick default template if couldn't find custom template
			if (template == null) {
				URL resource = GenerateUtil.class.getResource(MessageFormat.format("/{0}{1}", templatePrefix, templateName));
				configuration.setClassForTemplateLoading(GenerateUtil.class, "/");
				if (resource != null) {
					template = configuration.getTemplate(templatePrefix + templateName);
				} else {
					if (templatePrefix.contains("/")) {
						templateName = templatePrefix.substring(0, templatePrefix.lastIndexOf("/")) + "/" + templateName;
					}
					template = configuration.getTemplate(templateName, CharEncoding.UTF_8);
				}
			}

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			OutputStreamWriter output = new OutputStreamWriter(baos, CharEncoding.UTF_8);
			template.process(model, output);

			// only write the file if it has content (sometimes using empty template)
			byte[] bytes = baos.toByteArray();
			if (bytes.length > 0) {
				out.write(bytes);
			}
		} catch (TemplateException e) {
			throw (new GenerationException(e));
		} catch (IOException e) {
			throw (new GenerationException(e));
		}
	}

	private boolean isUseCustomTemplates() {
		return templatesDir != null && templatesDir.exists();
	}

	private Template findTemplate(String templateName, String templatePrefix, Configuration configuration) throws IOException {
		Template template = null;
		if (isUseCustomTemplates()) {
			File templateFile = new File(templatesDir, MessageFormat.format("/{0}{1}", templatePrefix, templateName));
			if (templateFile.exists()) {
				template = configuration.getTemplate(templatePrefix + templateName);
			} else {
				if (templatePrefix.contains("/")) {
					templateName = templatePrefix.substring(0, templatePrefix.lastIndexOf("/")) + "/" + templateName;
				}
				template = configuration.getTemplate(templateName);
			}
		} else {
			URL resource = GenerateUtil.class.getResource(MessageFormat.format("/{0}{1}", templatePrefix, templateName));
			if (resource != null) {
				template = configuration.getTemplate(templatePrefix + templateName);
			} else {
				template = configuration.getTemplate(templateName);
			}
		}
		return template;
	}

	public static void setPackageName(Collection<ScreenEntityDefinition> screenDefinitions, String packageName) {
		for (ScreenEntityDefinition screenEntityDefinition : screenDefinitions) {
			((ScreenEntityDesigntimeDefinition)screenEntityDefinition).setPackageName(packageName);
		}

	}

	public static void writeToFile(File javaFile, java.io.ByteArrayOutputStream baos, PojoCodeModel pojoCodeModel,
			String parentClassName) throws FileNotFoundException, IOException {
		if (pojoCodeModel != null && pojoCodeModel.isRelevant()) {
			File outputFolder = javaFile.getParentFile().getAbsoluteFile();
			String formattedClassName = pojoCodeModel.getFormattedClassName();
			// append the parentClassName to aspect file name if it's not the parent class is not the generated one
			String classFileName = !formattedClassName.equals(parentClassName) ? (parentClassName + formattedClassName)
					: formattedClassName;
			File outputFile = new File(outputFolder, classFileName + "_Aspect.aj");
			FileOutputStream fos = new FileOutputStream(outputFile);
			fos.write(baos.toByteArray());
			fos.close();
		}
	}
}