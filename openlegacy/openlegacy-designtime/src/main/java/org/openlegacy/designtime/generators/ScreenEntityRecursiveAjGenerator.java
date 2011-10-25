package org.openlegacy.designtime.generators;

import freemarker.template.TemplateException;

import org.openlegacy.utils.FileCommand;
import org.openlegacy.utils.FileCommandExecuter;

import japa.parser.ParseException;

import java.io.File;
import java.io.IOException;

public class ScreenEntityRecursiveAjGenerator {

	public static void main(String[] args) throws ParseException, IOException, TemplateException {
		if (args.length == 0) {
			System.out.println("Usage:\nScreenEntityRecursiveAjGenerator java-source-folder");
			return;
		}
		String root = args[0];

		new ScreenEntityRecursiveAjGenerator().generateAll(new File(root));
	}

	public void generateAll(File root) throws IOException, TemplateException {
		FileCommandExecuter.execute(root, new AspectGeneratorCommand());
	}

	private static class AspectGeneratorCommand implements FileCommand {

		public boolean accept(File file) {
			if (file.getName().endsWith("aj")) {
				return false;
			}
			if (file.isDirectory()) {
				return true;
			}
			if (file.getName().endsWith("java")) {
				return true;
			}

			return false;
		}

		public void doCommand(File file) {
			try {
				new ScreenEntityAjGenerator().generate(file);
			} catch (Exception e) {
				throw (new RuntimeException(e));
			}

		}

	}

}
