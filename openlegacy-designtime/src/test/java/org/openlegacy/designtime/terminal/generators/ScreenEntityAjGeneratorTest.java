package org.openlegacy.designtime.terminal.generators;

import freemarker.template.TemplateException;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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
import java.io.InputStream;
import java.util.List;

import javax.inject.Inject;

@ContextConfiguration("/test-designtime-context.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class ScreenEntityAjGeneratorTest {

	@Inject
	private ScreenPojosAjGenerator screenPojosAjGenerator;

	@Test
	public void testSimple() throws Exception {
		testGenerate();
	}

	@Test
	public void testLightWeight() throws Exception {
		testGenerate();
	}

	@Test
	public void testHasGetter() throws Exception {
		testGenerate();
	}

	@Test
	public void testAbstract() throws Exception {
		testGenerate();
	}

	@Test
	public void testHasValues() throws Exception {
		testGenerate();
	}

	@Test
	public void testDescriptionField() throws Exception {
		testGenerate();
	}

	@Test
	public void testTable() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream("testTable.java.resource"));

		ClassOrInterfaceDeclaration mainType = getMainType(compilationUnit);
		List<BodyDeclaration> members = mainType.getMembers();
		BodyDeclaration lastMember = members.get(members.size() - 1);
		screenPojosAjGenerator.generateScreenTable(compilationUnit, (ClassOrInterfaceDeclaration)lastMember, baos, "TestClass");
		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("testTable_Aspect.aj.expected"));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}

	@Test
	public void testPart() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream("testPart.java.resource"));

		ClassOrInterfaceDeclaration mainType = getMainType(compilationUnit);
		List<BodyDeclaration> members = mainType.getMembers();
		BodyDeclaration lastMember = members.get(members.size() - 1);
		screenPojosAjGenerator.generateScreenPart(compilationUnit, (ClassOrInterfaceDeclaration)lastMember, baos, "PartScreen");
		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream("testPart_Aspect.aj.expected"));
		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}

	@Test
	public void testNotScreenEntity() throws Exception {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		InputStream input = getClass().getResourceAsStream("testNotScreenEntity.java.resource");
		CompilationUnit compilationUnit = JavaParser.parse(input);

		screenPojosAjGenerator.generateEntity(compilationUnit, getMainType(compilationUnit), baos);

		Assert.assertEquals(0, baos.toByteArray().length);
	}

	private static ClassOrInterfaceDeclaration getMainType(CompilationUnit compilationUnit) {
		return (ClassOrInterfaceDeclaration)compilationUnit.getTypes().get(0);
	}

	@Test
	public void testNonJavaFile() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			InputStream input = getClass().getResourceAsStream("testNonJavaFile.txt");
			CompilationUnit compilationUnit = JavaParser.parse(input);
			screenPojosAjGenerator.generateEntity(compilationUnit, getMainType(compilationUnit), baos);
			Assert.fail("Parsing should have failed");
		} catch (ParseException e) {
			// good!
		}

	}

	private void testGenerate() throws Exception {
		String testMethodName = AssertUtils.getTestMethodName();
		testGenerate(testMethodName + ".java.resource", testMethodName + "_Aspect.aj.expected");
	}

	private void testGenerate(String javaSource, String expectAspect) throws IOException, TemplateException, ParseException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		CompilationUnit compilationUnit = JavaParser.parse(getClass().getResourceAsStream(javaSource));

		screenPojosAjGenerator.generateEntity(compilationUnit, getMainType(compilationUnit), baos);

		byte[] expectedBytes = IOUtils.toByteArray(getClass().getResourceAsStream(expectAspect));

		AssertUtils.assertContent(expectedBytes, baos.toByteArray());
	}

}
