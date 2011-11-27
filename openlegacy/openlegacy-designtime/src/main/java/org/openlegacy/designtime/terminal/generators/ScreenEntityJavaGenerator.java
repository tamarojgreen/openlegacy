package org.openlegacy.designtime.terminal.generators;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ScreenEntityJavaGenerator {

	public void generate(ScreenEntityDefinition screenEntityDefinition, OutputStream out) throws TemplateException, IOException {

		Configuration configuration = new Configuration();
		configuration.setClassForTemplateLoading(getClass(), "/");

		Template template = configuration.getTemplate("ScreenEntity.java.template");

		OutputStreamWriter output = new OutputStreamWriter(out);

		template.process(screenEntityDefinition, output);
	}
}
