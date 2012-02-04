package org.openlegacy.designtime.terminal.generators;

import freemarker.template.TemplateException;

import org.openlegacy.layout.PageDefinition;

import java.io.IOException;
import java.io.OutputStream;

public class ScreenEntityMvcGenerator {

	public void generateJspx(PageDefinition pageDefinition, OutputStream output) throws TemplateException, IOException {

		GenerateUtil.generate(pageDefinition, output, "ScreenEntityMvc.jspx.template");
	}

}
