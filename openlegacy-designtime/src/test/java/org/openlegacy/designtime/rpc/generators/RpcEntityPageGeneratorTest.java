package org.openlegacy.designtime.rpc.generators;

import freemarker.template.TemplateException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.openlegacy.designtime.generators.GenerateUtil;
import org.openlegacy.designtime.rpc.generators.support.RpcCodeBasedDefinitionUtils;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.layout.support.DefaultRpcPageBuilder;
import org.openlegacy.test.utils.AssertUtils;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

public class RpcEntityPageGeneratorTest {

	@Test
	public void testMvcGenerateJspxByCodeModel() throws Exception {
		String javaSource = "/org/openlegacy/designtime/rpc/generators/RpcForPage.java.resource";
		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream(javaSource));

		RpcEntityDefinition entityDefinition = RpcCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, null);
		assertPageGeneration(entityDefinition, "web/RpcEntityMvcPage.jspx.template", "RpcForPage.jspx.expected");
	}

	@Test
	public void testMvcGenerateJspxByCodeModel2() throws Exception {
		String javaSource = "/org/openlegacy/designtime/rpc/generators/ItemDetails.java.resource";
		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream(javaSource));

		RpcEntityDefinition entityDefinition = RpcCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, null);
		assertPageGeneration(entityDefinition, "web/RpcEntityMvcPage.jspx.template", "ItemDetails.jspx.expected");
	}

	@Test
	public void testGenerateHelp() throws TemplateException, IOException, ParseException {
		String javaSource = "/org/openlegacy/designtime/rpc/generators/ItemDetails.java.resource";
		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream(javaSource));

		RpcEntityDefinition entityDefinition = RpcCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, null);
		assertPageGeneration(entityDefinition, "RpcEntityHelpPage.html.template", "ItemDetailsHelp.html.expected");

	}

	@Test
	public void testSpaGenerateHtmlByCodeModel() throws Exception {
		String javaSource = "/org/openlegacy/designtime/rpc/generators/RpcForPage.java.resource";
		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream(javaSource));

		RpcEntityDefinition entityDefinition = RpcCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, null);
		assertPageGeneration(entityDefinition, "RpcEntitySpaPage.html.template", "RpcForPage.html.expected");
	}

	@Test
	public void testSpaGenerateMasterDetailsHtml() throws Exception {
		String javaSource = "/org/openlegacy/designtime/rpc/generators/MasterDummyEntity.java";
		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream(javaSource));

		File packageDir = new File(getClass().getResource("").getFile());
		RpcEntityDefinition entityDefinition = RpcCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, packageDir);
		assertPageGeneration(entityDefinition, "MasterDetailsEntityRpcEntitySpaPage.html.template",
				"MasterDummyEntity.html.expected");
	}

	private void assertPageGeneration(RpcEntityDefinition entityDefinition, String templateName, String expectedPageResultResource)
			throws TemplateException, IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		GenerateUtil generateUtil = new GenerateUtil();

		DefaultRpcPageBuilder rpcPageBuilder = new DefaultRpcPageBuilder();
		generateUtil.generate(rpcPageBuilder.build(entityDefinition), baos, templateName);
		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream(expectedPageResultResource));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}
}
