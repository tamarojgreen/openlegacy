package org.openlegacy.designtime.mains;

import java.io.File;

public abstract class AbstractGenerateRequest {

	private File sourceDirectory;
	private String packageDirectory;
	private File TemplatesDirectory;
	private File projectPath;

	public File getSourceDirectory() {
		return sourceDirectory;
	}

	public void setSourceDirectory(File sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	public String getPackageDirectory() {
		return packageDirectory;
	}

	public void setPackageDirectory(String packageDirectory) {
		this.packageDirectory = packageDirectory;
	}

	public File getTemplatesDirectory() {
		return TemplatesDirectory;
	}

	public void setTemplatesDirectory(File templatesDirectory) {
		TemplatesDirectory = templatesDirectory;
	}

	public File getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(File projectPath) {
		this.projectPath = projectPath;
	}

}
