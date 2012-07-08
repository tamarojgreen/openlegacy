package org.openlegacy.designtime.mains;

import java.io.File;

public class ProjectCreationRequest {

	private String templateName;
	private File baseDir;
	private String projectName;
	private String provider;
	private String defaultPackageName;

	private String hostName;
	private int hostPort;
	private String codePage;

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public File getBaseDir() {
		return baseDir;
	}

	public void setBaseDir(File baseDir) {
		this.baseDir = baseDir;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProvider() {
		return provider;
	}

	public void setProvider(String provider) {
		this.provider = provider;
	}

	public String getDefaultPackageName() {
		return defaultPackageName;
	}

	public void setDefaultPackageName(String defaultPackageName) {
		this.defaultPackageName = defaultPackageName;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getHostPort() {
		return hostPort;
	}

	public void setHostPort(int hostPort) {
		this.hostPort = hostPort;
	}

	public boolean isDemo() {
		return templateName.endsWith("sample") || templateName.endsWith("demo");
	}

	public String getCodePage() {
		return codePage;
	}

	public void setCodePage(String codePage) {
		this.codePage = codePage;
	}
}
