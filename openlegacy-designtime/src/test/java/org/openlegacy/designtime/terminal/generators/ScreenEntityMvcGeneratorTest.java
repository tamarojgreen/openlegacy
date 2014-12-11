package org.openlegacy.designtime.terminal.generators;

import apps.inventory.screens.ItemDetails1;
import apps.inventory.screens.ItemsList;
import apps.inventory.screens.SignOn;
import freemarker.template.TemplateException;

import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.designtime.generators.MvcGenerateUtil;
import org.openlegacy.designtime.terminal.generators.mock.CompositeScreenForPage;
import org.openlegacy.designtime.terminal.generators.mock.MenuScreenForPage;
import org.openlegacy.designtime.terminal.generators.mock.ScreenForPage;
import org.openlegacy.designtime.terminal.generators.support.ScreenCodeBasedDefinitionUtils;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.layout.support.DefaultScreenPageBuilder;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.test.utils.AssertUtils;
import org.springframework.context.ApplicationContext;
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
	private ScreenEntityMvcGenerator screenEntityMvcGenerator;

	@Inject
	private ApplicationContext applicationContext;

	@Test
	public void testGenerateJspx() throws Exception {

		assertPageGeneration(screenEntitiesRegistry.get(ScreenForPage.class), "ScreenForPage.jspx.expected");
	}

	@Test
	public void testGenerateJspxByColumns() throws Exception {

		DefaultScreenPageBuilder screenPageBuilder = applicationContext.getBean(DefaultScreenPageBuilder.class);
		screenPageBuilder.setDefaultColumns(3);

		ScreenEntityDefinition screenDefinition = screenEntitiesRegistry.get(ScreenForPage.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PageDefinition pageDefinition = screenPageBuilder.build(screenDefinition);
		screenEntityMvcGenerator.generatePage(pageDefinition, baos, "web/");

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("ScreenForPageWithColumns.jspx.expected"));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}

	@After
	public void after() {
		DefaultScreenPageBuilder screenPageBuilder = applicationContext.getBean(DefaultScreenPageBuilder.class);
		screenPageBuilder.setDefaultColumns(null);
	}

	@Test
	public void testGenerateJspxByType() throws Exception {

		DefaultScreenPageBuilder screenPageBuilder = applicationContext.getBean(DefaultScreenPageBuilder.class);

		ScreenEntityDefinition screenDefinition = screenEntitiesRegistry.get(MenuScreenForPage.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PageDefinition pageDefinition = screenPageBuilder.build(screenDefinition);
		screenEntityMvcGenerator.generatePage(pageDefinition, baos, "web/");

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("MenuScreenForPage.jspx.expected"));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}

	// TODO - move to different test class
	@Test
	public void testNavigationFromCodeModel() throws Exception {
		String javaSource = "/org/openlegacy/designtime/terminal/generators/mock/ScreenForPage.java.resource";
		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream(javaSource));

		ScreenEntityDefinition screenDefinition = ScreenCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, null);

		Assert.assertNotNull(screenDefinition);
		Assert.assertNotNull(screenDefinition.getNavigationDefinition());
		Assert.assertEquals("ScreenForPageWithKey", screenDefinition.getNavigationDefinition().getAccessedFromEntityName());
	}

	@Test
	public void testGenerateJspxByCodeModel() throws Exception {
		String javaSource = "/org/openlegacy/designtime/terminal/generators/mock/ScreenForPage.java.resource";
		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream(javaSource));

		ScreenEntityDefinition screenDefinition = ScreenCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, null);
		assertPageGeneration(screenDefinition, "ScreenForPage2.jspx.expected");
	}

	@Test
	public void testGenerateWebCompositeJspx() throws Exception {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		screenEntityMvcGenerator.generateCompositePage(screenEntitiesRegistry.get(CompositeScreenForPage.class), baos,
				MvcGenerateUtil.TEMPLATE_WEB_DIR_PREFIX);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("ScreenForPageComposite.jspx.expected"));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}

	@Test
	public void testGenerateContollerByCodeModel() throws Exception {
		String javaSource = "/org/openlegacy/designtime/terminal/generators/mock/ScreenForPage.java.resource";
		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream(javaSource));

		ScreenEntityDefinition screenDefinition = ScreenCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, null);
		assertControllerGeneration(screenDefinition);
	}

	@Test
	public void testGenerateContollerByCodeModelWithKey() throws Exception {
		DefaultScreenPageBuilder screenPageBuilder = applicationContext.getBean(DefaultScreenPageBuilder.class);

		String javaSource = "/org/openlegacy/designtime/terminal/generators/mock/ScreenForPageWithKey.java.resource";
		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream(javaSource));

		ScreenEntityDefinition screenDefinition = ScreenCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, null);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PageDefinition pageDefinition = screenPageBuilder.build(screenDefinition);
		screenEntityMvcGenerator.generateController(pageDefinition, baos);
		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("ScreenForPageWithKeyController.java.expected"));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}

	@Test
	public void testGenerateController() throws Exception {
		DefaultScreenPageBuilder screenPageBuilder = applicationContext.getBean(DefaultScreenPageBuilder.class);

		ScreenEntityDefinition screenDefinition = screenEntitiesRegistry.get(ScreenForPage.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PageDefinition pageDefinition = screenPageBuilder.build(screenDefinition);
		screenEntityMvcGenerator.generateController(pageDefinition, baos);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("ScreenForPageController.java.expected"));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}

	private void assertControllerGeneration(ScreenEntityDefinition screenDefinition) throws TemplateException, IOException {
		DefaultScreenPageBuilder screenPageBuilder = applicationContext.getBean(DefaultScreenPageBuilder.class);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PageDefinition pageDefinition = screenPageBuilder.build(screenDefinition);
		screenEntityMvcGenerator.generateController(pageDefinition, baos);
		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("ScreenForPageController.java.expected"));
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
		DefaultScreenPageBuilder screenPageBuilder = applicationContext.getBean(DefaultScreenPageBuilder.class);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PageDefinition pageDefinition = screenPageBuilder.build(screenEntityDefinition);
		screenEntityMvcGenerator.generatePage(pageDefinition, baos, "web/");

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream(expectedPageResultResource));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}
}
