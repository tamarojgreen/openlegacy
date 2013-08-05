package org.openlegacy.designtime.rpc.generators;

import freemarker.template.TemplateException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.openlegacy.designtime.generators.GenerateUtil;
import org.openlegacy.designtime.rpc.generators.support.RpcCodeBasedDefinitionUtils;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.test.utils.AssertUtils;

import japa.parser.JavaParser;
import japa.parser.ast.CompilationUnit;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class RpcEntityMvcGeneratorTest {

	@Test
	public void testGenerateJspxByCodeModel() throws Exception {
		String javaSource = "/org/openlegacy/designtime/rpc/generators/RpcForPage.java.resource";
		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream(javaSource));

		RpcEntityDefinition entityDefinition = RpcCodeBasedDefinitionUtils.getEntityDefinition(compilationUnit, null);
		assertPageGeneration(entityDefinition, "RpcForPage.jspx.expected");
	}

	private void assertPageGeneration(RpcEntityDefinition entityDefinition, String expectedPageResultResource)
			throws TemplateException, IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		GenerateUtil generateUtil = new GenerateUtil();

		generateUtil.generate(entityDefinition, baos, "web/RpcEntityMvcPage.jspx.template");
		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream(expectedPageResultResource));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}
}
