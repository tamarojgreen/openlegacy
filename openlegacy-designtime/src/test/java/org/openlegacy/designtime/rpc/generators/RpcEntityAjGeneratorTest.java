package org.openlegacy.designtime.rpc.generators;

import freemarker.template.TemplateException;

import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openlegacy.TestUtils;
import org.openlegacy.test.utils.AssertUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;
import japa.parser.ast.body.BodyDeclaration;
import japa.parser.ast.body.ClassOrInterfaceDeclaration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

@ContextConfiguration("/test-designtime-rpc-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class RpcEntityAjGeneratorTest {

	@Inject
	private RpcPojosAjGenerator rpcPojosAjGenerator;

	@Test
	public void testSimple() throws Exception {
		testGenerate();
	}

	@Test
	public void testStaticFinalProperties() throws Exception {
		testGenerate();
	}

	@Test
	public void testRpcSample() throws Exception {
		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream("testRpcSample.java.resource"));
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ClassOrInterfaceDeclaration mainType = getMainType(compilationUnit);
		rpcPojosAjGenerator.generateEntity(compilationUnit, mainType, baos);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("rpcSample_Aspect.aj.expected"));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());

		List<BodyDeclaration> members = mainType.getMembers();
		for (BodyDeclaration bodyDeclaration : members) {
			// look for inner classes
			baos = new ByteArrayOutputStream();
			if (bodyDeclaration instanceof ClassOrInterfaceDeclaration) {
				rpcPojosAjGenerator.generatePart(compilationUnit, (ClassOrInterfaceDeclaration)bodyDeclaration, baos,
						mainType.getName());
				String expected = ((ClassOrInterfaceDeclaration)bodyDeclaration).getName() + "_Aspect.aj.expected";
				expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream(expected));
				AssertUtils.assertContent(expectedBytes, baos.toByteArray());

			}
		}
	}

	private static ClassOrInterfaceDeclaration getMainType(CompilationUnit compilationUnit) {
		return (ClassOrInterfaceDeclaration)compilationUnit.getTypes().get(0);
	}

	private void testGenerate() throws Exception {
		String testMethodName = TestUtils.getTestMethodName();
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
