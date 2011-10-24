package org.openlegacy.designtime.generators;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import japa.parser.JavaParser;
import japa.parser.ParseException;
import japa.parser.ast.CompilationUnit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class ScreenEntityAjGenerator {

	public void generate(File javaFile) throws IOException, TemplateException, ParseException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		FileInputStream input = new FileInputStream(javaFile);
		ScreenEntityCodeModel screenEntityCodeModel = generate(input, baos);

		if (screenEntityCodeModel.isRelevant()) {
			File outputFolder = javaFile.getParentFile().getAbsoluteFile();
			File outputFile = new File(outputFolder, screenEntityCodeModel.getClassName() + "_Aspect.aj");
			FileOutputStream fos = new FileOutputStream(outputFile);
			fos.write(baos.toByteArray());
			fos.close();
		}
	}

	public ScreenEntityCodeModel generate(InputStream input, OutputStream out) throws IOException, TemplateException,
			ParseException {

		ScreenEntityCodeModel screenEntityCodeModel = buildModel(input);

		if (!screenEntityCodeModel.isRelevant()) {
			return screenEntityCodeModel;
		}

		Configuration configuration = new Configuration();
		configuration.setClassForTemplateLoading(getClass(), "/");

		Template template = configuration.getTemplate("Screen_Aspect.aj.template");

		OutputStreamWriter output = new OutputStreamWriter(out);

		template.process(screenEntityCodeModel, output);

		return screenEntityCodeModel;
	}

	private static ScreenEntityCodeModel buildModel(InputStream input) throws FileNotFoundException, ParseException {
		CompilationUnit compilationUnit = JavaParser.parse(input);
		ScreenEntityCodeModel screenEntityCodeModel = new ScreenEntityCodeModel(compilationUnit);
		return screenEntityCodeModel;
	}

}
