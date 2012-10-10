package org.openlegacy.designtime.generators;

import apps.inventory.screens.ItemDetails1;
import apps.inventory.screens.ItemsList;
import apps.inventory.screens.SignOn;
import freemarker.template.TemplateException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.designtime.generators.mock.CompositeScreenForPage;
import org.openlegacy.designtime.generators.mock.MenuScreenForPage;
import org.openlegacy.designtime.generators.mock.ScreenForPage;
import org.openlegacy.designtime.terminal.generators.ScreenEntityMvcGenerator;
import org.openlegacy.designtime.terminal.generators.support.CodeBasedDefinitionUtils;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.layout.ScreenPageBuilder;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.test.utils.AssertUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

@ContextConfiguration("ScreenEntityMvcGeneratorTest-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ScreenEntityMvcGeneratorTest {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Inject
	private ScreenPageBuilder screenPageBuilder;

	@Inject
	ScreenEntityMvcGenerator screenEntityMvcGenerator;

	@Test
	public void testGenerateJspx() throws Exception {

		assertPageGeneration(screenEntitiesRegistry.get(ScreenForPage.class), "ScreenForPage.jspx.expected");
	}

	@Test
	public void testGenerateJspxByType() throws Exception {

		ScreenEntityDefinition screenDefinition = screenEntitiesRegistry.get(MenuScreenForPage.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PageDefinition pageDefinition = screenPageBuilder.build(screenDefinition);
		screenEntityMvcGenerator.generatePage(pageDefinition, baos, "web/");

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("MenuScreenForPage.jspx.expected"));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}

	@Test
	public void testGenerateJspxByCodeModel() throws Exception {
		String javaSource = "/org/openlegacy/designtime/generators/mock/ScreenForPage.java.resource";
		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream(javaSource));

		ScreenEntityDefinition screenDefinition = CodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, null);

		assertPageGeneration(screenDefinition, "ScreenForPage.jspx.expected");
	}

	@Test
	public void testGenerateCompositeJspx() throws Exception {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		screenEntityMvcGenerator.generateCompositePage(screenEntitiesRegistry.get(CompositeScreenForPage.class), baos);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("ScreenForPageComposite.jspx.expected"));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}

	@Test
	public void testGenerateContollerAspectByCodeModel() throws Exception {
		String javaSource = "/org/openlegacy/designtime/generators/mock/ScreenForPage.java.resource";
		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream(javaSource));

		ScreenEntityDefinition screenDefinition = CodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, null);
		assertControllerAspectGeneration(screenDefinition);
	}

	@Test
	public void testGenerateController() throws Exception {

		ScreenEntityDefinition screenDefinition = screenEntitiesRegistry.get(ScreenForPage.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PageDefinition pageDefinition = screenPageBuilder.build(screenDefinition);
		screenEntityMvcGenerator.generateController(pageDefinition, baos);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("ScreenForPageController.java.expected"));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}

	@Test
	public void testGenerateControllerAspect() throws Exception {

		ScreenEntityDefinition screenDefinition = screenEntitiesRegistry.get(ScreenForPage.class);

		assertControllerAspectGeneration(screenDefinition);
	}

	private void assertControllerAspectGeneration(ScreenEntityDefinition screenDefinition) throws TemplateException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PageDefinition pageDefinition = screenPageBuilder.build(screenDefinition);
		screenEntityMvcGenerator.generateControllerAspect(pageDefinition, baos);
		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("ScreenForPageController.aj.expected"));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}

	@Test
	public void testGenerateInventoryApp() throws Exception {
		assertPageGeneration(screenEntitiesRegistry.get(SignOn.class), "SignOn.jspx.expected");

		assertPageGeneration(screenEntitiesRegistry.get(ItemsList.class), "ItemsList.jspx.expected");

		assertPageGeneration(screenEntitiesRegistry.get(ItemDetails1.class), "ItemDetails1.jspx.expected");
	}

	private void assertPageGeneration(ScreenEntityDefinition screenEntityDefinition, String expectedPageResultResource)
			throws TemplateException, IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PageDefinition pageDefinition = screenPageBuilder.build(screenEntityDefinition);
		screenEntityMvcGenerator.generatePage(pageDefinition, baos, "web/");

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream(expectedPageResultResource));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}
}
