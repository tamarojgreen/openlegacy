package org.openlegacy.designtime.terminal.generators;

import freemarker.template.TemplateException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.designtime.terminal.generators.support.ScreenCodeBasedDefinitionUtils;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.layout.support.DefaultBidiScreenPageBuilder;
import org.openlegacy.test.utils.AssertUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

@ContextConfiguration("ScreenEntitySpaBidiGeneratorTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ScreenEntitySpaBidiGeneratorTest {

	@Inject
	private DefaultBidiScreenPageBuilder screenPageBuilder;

	@Inject
	private ScreenEntitySpaGenerator screenEntitySpaGenerator;

	@Test
	public void testGenerateHtmlByCodeModel() throws Exception {
		String javaSource = "/org/openlegacy/designtime/terminal/generators/mock/ScreenForPage.java.resource";
		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream(javaSource));

		ScreenEntityDefinition screenDefinition = ScreenCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, null);
		assertPageGeneration(screenDefinition, "ScreenForPageBidi2.html.expected");
	}

	private void assertPageGeneration(ScreenEntityDefinition screenEntityDefinition, String expectedPageResultResource)
			throws TemplateException, IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PageDefinition pageDefinition = screenPageBuilder.build(screenEntityDefinition);
		screenEntitySpaGenerator.generatePage(pageDefinition, baos, "");

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream(expectedPageResultResource));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}
}
