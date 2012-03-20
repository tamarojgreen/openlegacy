package org.openlegacy.designtime.terminal.generators;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Collection;

@Component
public class GenerateUtil {

	private static File templatesDir;

	public void setTemplateDirectory(File templatesDir) {
		GenerateUtil.templatesDir = templatesDir;
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
			if (templatesDir != null && templatesDir.exists()) {
				configuration.setDirectoryForTemplateLoading(templatesDir);
			} else {
				configuration.setClassForTemplateLoading(GenerateUtil.class, "/");
			}
			configuration.setWhitespaceStripping(true);
			Template template = null;
			try {
				if (templatesDir != null && templatesDir.exists()) {
					template = findTemplate(templateName, templatePrefix, configuration);
				}
			} catch (FileNotFoundException e) {
				// OK
			}
			// pick default template if couldn't find custom template
			if (template == null) {
				template = findTemplate(templateName, templatePrefix, configuration);
			}

			OutputStreamWriter output = new OutputStreamWriter(out);
			template.process(model, output);
		} catch (TemplateException e) {
			throw (new GenerationException(e));
		} catch (IOException e) {
			throw (new GenerationException(e));
		}
	}

	private static Template findTemplate(String templateName, String templatePrefix, Configuration configuration)
			throws IOException {
		Template template;
		URL resource = GenerateUtil.class.getResource(MessageFormat.format("/{0}{1}", templatePrefix, templateName));
		if (resource != null) {
			template = configuration.getTemplate(templatePrefix + templateName);
		} else {
			template = configuration.getTemplate(templateName);
		}
		return template;
	}

	public static void setPackageName(Collection<ScreenEntityDefinition> screenDefinitions, String packageName) {
		for (ScreenEntityDefinition screenEntityDefinition : screenDefinitions) {
			((ScreenEntityDesigntimeDefinition)screenEntityDefinition).setPackageName(packageName);
		}

	}
}
