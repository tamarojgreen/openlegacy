package org.openlegacy.ide.eclipse.wizards.project.organized.model;

import org.openlegacy.designtime.newproject.model.ProjectTheme;
import org.openlegacy.designtime.newproject.organized.model.HostType;
import org.openlegacy.designtime.newproject.organized.model.ProjectType;

/**
 * @author Ivan Bort
 * 
 */
public class WizardModel {

	// project type
	private String templateName;
	private boolean demo;
	private boolean projectSupportTheme;
	private String zipFile;
	private String projectName;
	private String defaultPackageName;
	private boolean rightToLeft;
	// host type
	private String hostTypeName;
	private String host;
	private int hostPort;
	private String codePage;
	// theme
	private ProjectTheme projectTheme;

	private String backendSolution;
	private String frontendSolution;

	public boolean isDemo() {
		return demo;
	}

	public boolean isProjectSupportTheme() {
		return projectSupportTheme;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getTemplateName() {
		return templateName;
	}

	public String getHostTypeName() {
		return hostTypeName;
	}

	public String getDefaultPackageName() {
		return defaultPackageName;
	}

	public String getHost() {
		return host;
	}

	public int getHostPort() {
		return hostPort;
	}

	public String getCodePage() {
		return codePage;
	}

	public ProjectTheme getProjectTheme() {
		return projectTheme;
	}

	public String getZipFile() {
		return zipFile;
	}

	public boolean isRightToLeft() {
		return rightToLeft;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public void setDefaultPackageName(String defaultPackageName) {
		this.defaultPackageName = defaultPackageName;
	}

	public void setRightToLeft(boolean rightToLeft) {
		this.rightToLeft = rightToLeft;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public void setHostPort(int hostPort) {
		this.hostPort = hostPort;
	}

	public void setCodePage(String codePage) {
		this.codePage = codePage;
	}

	public void setProjectTheme(ProjectTheme projectTheme) {
		this.projectTheme = projectTheme;
	}

	public String getBackendSolution() {
		return backendSolution;
	}

	public String getFrontendSolution() {
		return frontendSolution;
	}

	public void update(ProjectType projectType) {
		if (projectType == null) {
			clear();
		} else {
			templateName = projectType.getTemplateName();
			demo = projectType.isDemo();
			projectSupportTheme = projectType.isSupportTheme();
			zipFile = projectType.getZipFile();
			backendSolution = projectType.getBackendSolution();
			frontendSolution = projectType.getFrontendSolution();
			if (demo) {
				projectName = templateName;
			}
		}
	}

	public void update(HostType hostType) {
		if (hostType == null) {
			clear();
		} else {
			hostTypeName = hostType.getName();
		}
	}

	public void clear() {
		templateName = null;
		demo = false;
		projectSupportTheme = false;
		zipFile = null;
		projectName = null;
		defaultPackageName = null;
		rightToLeft = false;
		hostTypeName = null;
		host = null;
		hostPort = 0;
		codePage = null;
		projectTheme = null;
		backendSolution = null;
		frontendSolution = null;
	}

}
