package org.openlegacy.designtime.mains;

import java.io.File;

public class GeneratePageRequest {

	private File projectDir;
	private File screenEntitySourceFile;
	private File sourceDirectory;
	private String packageDirectoryName;
	private File templatesDir;
	private OverrideConfirmer overrideConfirmer;

	public File getProjectDir() {
		return projectDir;
	}

	public void setProjectDir(File projectDir) {
		this.projectDir = projectDir;
	}

	public File getScreenEntitySourceFile() {
		return screenEntitySourceFile;
	}

	public void setScreenEntitySourceFile(File screenEntitySourceFile) {
		this.screenEntitySourceFile = screenEntitySourceFile;
	}

	public File getSourceDirectory() {
		return sourceDirectory;
	}

	public void setSourceDirectory(File sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	public String getPackageDirectoryName() {
		return packageDirectoryName;
	}

	public void setPackageDirectoryName(String packageDirectoryName) {
		this.packageDirectoryName = packageDirectoryName;
	}

	public File getTemplatesDir() {
		return templatesDir;
	}

	public void setTemplatesDir(File templatesDir) {
		this.templatesDir = templatesDir;
	}

	public OverrideConfirmer getOverrideConfirmer() {
		return overrideConfirmer;
	}

	public void setOverrideConfirmer(OverrideConfirmer overrideConfirmer) {
		this.overrideConfirmer = overrideConfirmer;
	}

}
