package org.openlegacy.designtime.rpc.generators;

import freemarker.template.TemplateException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.test.utils.AssertUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

@ContextConfiguration("/test-designtime-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RpcEntityAjGeneratorTest {

	@Inject
	private RpcPojosAjGenerator rpcPojosAjGenerator;

	@Test
	public void testSimple() throws Exception {
		testGenerate();
	}

	private static ClassOrInterfaceDeclaration getMainType(CompilationUnit compilationUnit) {
		return (ClassOrInterfaceDeclaration)compilationUnit.getTypes().get(0);
	}

	private void testGenerate() throws Exception {
		String testMethodName = AssertUtils.getTestMethodName();
		testGenerate(testMethodName + ".java.resource", testMethodName + "_Aspect.aj.expected");
	}

	private void testGenerate(String javaSource, String expectAspect) throws IOException, TemplateException, ParseException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream(javaSource));

		rpcPojosAjGenerator.generateEntity(compilationUnit, getMainType(compilationUnit), baos);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream(expectAspect));

		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}

}
