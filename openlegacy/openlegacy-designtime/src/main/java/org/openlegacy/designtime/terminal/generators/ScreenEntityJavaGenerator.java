package org.openlegacy.designtime.terminal.generators;

import freemarker.template.TemplateException;

import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.io.IOException;
import java.io.OutputStream;

public class ScreenEntityJavaGenerator {

	public void generate(ScreenEntityDefinition screenEntityDefinition, OutputStream out) throws TemplateException, IOException {
		GenerateUtil.generate(screenEntityDefinition, out, "ScreenEntity.java.template");
	}
}
