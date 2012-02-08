package org.openlegacy.designtime.terminal.generators;

import freemarker.template.TemplateException;

import org.openlegacy.layout.PageDefinition;

import java.io.IOException;
import java.io.OutputStream;

public class ScreenEntityMvcGenerator {

	public void generateJspx(PageDefinition pageDefinition, OutputStream output) throws TemplateException, IOException {

		GenerateUtil.generate(pageDefinition, output, "ScreenEntityMvcPage.jspx.template");
	}

	public void generateController(PageDefinition pageDefinition, OutputStream output) throws TemplateException, IOException {

		GenerateUtil.generate(pageDefinition, output, "ScreenEntityMvcController.java.template");
	}

	public void generateControllerAspect(PageDefinition pageDefinition, OutputStream output) throws TemplateException,
			IOException {

		GenerateUtil.generate(pageDefinition, output, "ScreenEntityMvcController.aj.template");
	}
}
