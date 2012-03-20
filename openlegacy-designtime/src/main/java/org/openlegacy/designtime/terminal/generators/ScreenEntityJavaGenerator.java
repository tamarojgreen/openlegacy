package org.openlegacy.designtime.terminal.generators;

import freemarker.template.TemplateException;

import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;

@Component
public class ScreenEntityJavaGenerator {

	@Inject
	private GenerateUtil generateUtil;

	public void generate(ScreenEntityDefinition screenEntityDefinition, OutputStream out) throws TemplateException, IOException {
		String typeName = screenEntityDefinition.getTypeName();
		generateUtil.generate(screenEntityDefinition, out, "ScreenEntity.java.template", typeName);
	}
}
