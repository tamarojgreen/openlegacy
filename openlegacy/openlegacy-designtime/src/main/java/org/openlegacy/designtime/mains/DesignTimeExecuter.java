package org.openlegacy.designtime.mains;

import org.apache.commons.io.IOUtils;
import org.openlegacy.utils.ZipUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

public class DesignTimeExecuter {

	public void createProject(String templateName, String location, String projectName) throws IOException {
		URL zipFile = getClass().getResource(MessageFormat.format("/templates/{0}.zip", templateName));

		File targetZip = new File(location, templateName + ".zip");
		FileOutputStream targetZipOutputStream = new FileOutputStream(targetZip);
		IOUtils.copy(zipFile.openStream(), targetZipOutputStream);
		targetZipOutputStream.close();
		File targetPath = new File(location, projectName);
		ZipUtil.unzip(targetZip.getAbsolutePath(), targetPath.getAbsolutePath());

		File projectFile = new File(targetPath, ".project");
		String projectFileContent = IOUtils.toString(new FileInputStream(projectFile));

		// NOTE assuming all project templates starts with "openlegacy-"
		projectFileContent = projectFileContent.replaceAll("<name>openlegacy-.*</name>",
				MessageFormat.format("<name>{0}</name>", projectName));
		FileOutputStream fos = new FileOutputStream(projectFile);
		IOUtils.write(projectFileContent, fos);
	}
}
