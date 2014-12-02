package org.openlegacy.designtime.db.generators;

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
import japa.parser.ast.body.ClassOrInterfaceDeclaration;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.inject.Inject;

@ContextConfiguration("/test-designtime-db-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class DbEntityAjGeneratorTest {

	@Inject
	private DbPojosAjGenerator dbPojosAjGenerator;

	@Test
	public void testSimple() throws Exception {
		testGenerate();
	}

	@Test
	public void testStaticFinalProperties() throws Exception {
		testGenerate();
	}

	private void testGenerate() throws Exception {
		String testMethodName = TestUtils.getTestMethodName();
		testGenerate(testMethodName + ".java.resource", testMethodName + "_Aspect.aj.expected");
	}

	private static ClassOrInterfaceDeclaration getMainType(CompilationUnit compilationUnit) {
		return (ClassOrInterfaceDeclaration)compilationUnit.getTypes().get(0);
	}

	private void testGenerate(String javaSource, String expectAspect) throws IOException, TemplateException, ParseException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream(javaSource));

		dbPojosAjGenerator.generateEntity(compilationUnit, getMainType(compilationUnit), baos);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream(expectAspect));

		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}

}
