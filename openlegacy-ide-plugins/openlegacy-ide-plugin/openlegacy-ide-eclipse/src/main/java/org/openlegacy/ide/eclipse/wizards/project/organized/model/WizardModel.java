package org.openlegacy.ide.eclipse.wizards.project.organized.model;

import org.openlegacy.designtime.newproject.model.ProjectTheme;
import org.openlegacy.designtime.newproject.organized.model.DbType;
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
	// db type
	private String dbTypeName;
	private String dbUser;
	private String dbPass;
	private String dbDriver;
	private String dbUrl;
	private String dbMavenDependency;
	private String dbDdlAuto;
	private String dbDialect;

	private String backendSolution;
	private String frontendSolution;

	private String trailFilePath;
	
	private boolean restFulService;

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

	public String getDbTypeName() {
		return dbTypeName;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPass() {
		return dbPass;
	}

	public void setDbPass(String dbPass) {
		this.dbPass = dbPass;
	}

	public String getDbDriver() {
		return dbDriver;
	}

	public void setDbDriver(String dbDriver) {
		this.dbDriver = dbDriver;
	}

	public String getDbUrl() {
		return dbUrl;
	}

	public void setDbUrl(String dbUrl) {
		this.dbUrl = dbUrl;
	}

	public String getDbMavenDependency() {
		return dbMavenDependency;
	}

	public String getDbDdlAuto() {
		return dbDdlAuto;
	}

	public void setDbDdlAuto(String dbDdlAuto) {
		this.dbDdlAuto = dbDdlAuto;
	}

	public String getDbDialect() {
		return dbDialect;
	}

	public void setDbDialect(String dbDialect) {
		this.dbDialect = dbDialect;
	}

	public boolean isRestFulService() {
		return restFulService;
	}

	public void setRestFulService(boolean restFulService) {
		this.restFulService = restFulService;
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

	public void update(DbType dbType) {
		if (dbType == null) {
			clear();
		} else {
			dbTypeName = dbType.getName();
			dbDriver = dbType.getDatabaseDriver();
			dbMavenDependency = dbType.getMavenDependency();
			dbDdlAuto = dbType.getDdlAuto();
			dbDialect = dbType.getDialect();

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
		dbTypeName = null;
		dbUser = null;
		dbPass = null;
		dbDriver = null;
		dbUrl = null;
		dbMavenDependency = null;
		dbDdlAuto = null;
		dbDialect = null;
		restFulService = false;
	}

	/**
	 * @return the trailFilePath
	 */
	public String getTrailFilePath() {
		return trailFilePath;
	}

	/**
	 * @param trailFilePath the trailFilePath to set
	 */
	public void setTrailFilePath(String trailFilePath) {
		this.trailFilePath = trailFilePath;
	}
}
