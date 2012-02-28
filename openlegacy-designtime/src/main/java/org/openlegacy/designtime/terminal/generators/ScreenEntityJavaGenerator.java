package org.openlegacy.designtime.terminal.generators;

import freemarker.template.TemplateException;

import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.io.IOException;
import java.io.OutputStream;

public class ScreenEntityJavaGenerator {

	public void generate(ScreenEntityDefinition screenEntityDefinition, OutputStream out) throws TemplateException, IOException {
		String typeName = screenEntityDefinition.getTypeName();
		GenerateUtil.generate(screenEntityDefinition, out, "ScreenEntity.java.template", typeName);
	}
}
