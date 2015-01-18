package org.openlegacy.designtime.generators;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.CharEncoding;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

public class ProjectUtil {

	private static final String SETTINGS_ORG_ECLIPSE_CORE_RESOURCES_PREFS = ".settings/org.eclipse.core.resources.prefs";

	public static void generatEclipseEncodingSettings(File projectPath, String fileName) throws IOException {
		File encodingFile = new File(projectPath, SETTINGS_ORG_ECLIPSE_CORE_RESOURCES_PREFS);
		String fileContent = FileUtils.readFileToString(encodingFile, CharEncoding.UTF_8);
		if (!fileContent.contains(encodingFile.getName())) {
			fileContent = MessageFormat.format("{0}\nencoding/{1}=UTF8", fileContent, fileName);
		}
		FileUtils.write(encodingFile, fileContent);
	}

}
