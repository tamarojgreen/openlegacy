package org.openlegacy.designtime.terminal.generators;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Collection;

public class GenerateUtil {

	public static void generate(ScreenEntityDefinition screenEntityDefinition, OutputStream out, String templateName)
			throws TemplateException, IOException {

		Configuration configuration = new Configuration();
		configuration.setClassForTemplateLoading(GenerateUtil.class, "/");

		Template template = configuration.getTemplate(templateName);

		OutputStreamWriter output = new OutputStreamWriter(out);

		template.process(screenEntityDefinition, output);
	}

	public static void setPackageName(Collection<ScreenEntityDefinition> screenDefinitions, String packageName) {
		for (ScreenEntityDefinition screenEntityDefinition : screenDefinitions) {
			((ScreenEntityDesigntimeDefinition)screenEntityDefinition).setPackageName("com.test");
		}

	}
}
