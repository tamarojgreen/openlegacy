package org.openlegacy.designtime.generators;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.designtime.terminal.generators.HelpGenerator;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedDefinitionUtils;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.layout.support.DefaultScreenPageBuilder;
import org.openlegacy.test.utils.AssertUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.ByteArrayOutputStream;

import javax.inject.Inject;

@ContextConfiguration("/test-designtime-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class HelpGeneratorTest {

	@Inject
	private HelpGenerator helpGenerator;

	@Test
	public void testGenerateHelp() throws Exception {

		String javaSource = "/org/openlegacy/designtime/generators/mock/ScreenForPage.java.resource";
		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream(javaSource));
		ScreenEntityDefinition screenDefinition = CodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, null);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PageDefinition pageDefinition = new DefaultScreenPageBuilder().build(screenDefinition);
		helpGenerator.generate(pageDefinition, baos);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("ScreenForPageHelp.html.expected"));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());

	}
}
