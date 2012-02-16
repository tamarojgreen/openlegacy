package org.openlegacy.designtime.terminal.generators;

import freemarker.template.TemplateException;

import org.openlegacy.layout.PageDefinition;

import java.io.IOException;
import java.io.OutputStream;

public interface ScreenEntityWebGenerator {

	void generatePage(PageDefinition pageDefinition, OutputStream output) throws TemplateException, IOException;

	public void generateController(PageDefinition pageDefinition, OutputStream output) throws TemplateException, IOException;

	public void generateControllerAspect(PageDefinition pageDefinition, OutputStream output) throws TemplateException,
			IOException;
}
