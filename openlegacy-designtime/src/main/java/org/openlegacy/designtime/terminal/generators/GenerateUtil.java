package org.openlegacy.designtime.terminal.generators;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.exceptions.GenerationException;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;

public class GenerateUtil {

	/**
	 * 
	 * @param model
	 * @param out
	 * @param templateName
	 * @param templatePrefix
	 *            allows to work with template with prefix if exists. e.g: MenuScreenEntityMvcPage.jspx.template /
	 *            ScreenEntityMvcPage.jspx.template
	 * @throws GenerationException
	 */
	public static void generate(Object model, OutputStream out, String templateName, String templatePrefix)
			throws GenerationException {

		Configuration configuration = new Configuration();
		configuration.setClassForTemplateLoading(GenerateUtil.class, "/");

		Template template;
		try {
			if (GenerateUtil.class.getResource(templatePrefix + templateName) != null) {
				template = configuration.getTemplate(templatePrefix + templateName);
			} else {
				template = configuration.getTemplate(templateName);
			}
			OutputStreamWriter output = new OutputStreamWriter(out);
			template.process(model, output);
		} catch (TemplateException e) {
			throw (new GenerationException(e));
		} catch (IOException e) {
			throw (new GenerationException(e));
		}
	}

	public static void setPackageName(Collection<ScreenEntityDefinition> screenDefinitions, String packageName) {
		for (ScreenEntityDefinition screenEntityDefinition : screenDefinitions) {
			((ScreenEntityDesigntimeDefinition)screenEntityDefinition).setPackageName(packageName);
		}

	}
}
