package org.openlegacy.designtime.terminal.generators;

import freemarker.template.TemplateException;

import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

import javax.inject.Inject;

/**
 * A generator which generates an html help file for a given screen entity
 */
@Component
public class HelpGenerator {

	@Inject
	private GenerateUtil generateUtil;

	public void generate(ScreenEntityDefinition screenEntityDefinition, OutputStream out) throws TemplateException, IOException {

		generateUtil.generate(screenEntityDefinition, out, "ScreenEntityHelpPage.html.template");

	}
}
