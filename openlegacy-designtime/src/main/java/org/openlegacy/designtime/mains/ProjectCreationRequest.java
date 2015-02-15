/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.designtime.mains;

import org.openlegacy.designtime.newproject.ITemplateFetcher;
import org.openlegacy.designtime.newproject.model.ProjectTheme;

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

	private boolean supportTheme;
	private ProjectTheme projectTheme;
	private String zipFile;

	private ITemplateFetcher templateFetcher;
	private boolean rightToLeft;

	private String backendSolution;

	private String dbDriver;
	private String dbUrl;
	private String dbUser;
	private String dbPass;
	private String dbDriverMavenDependency;
	private String dbDdlAuto;
	private String dbDialect;

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

	public boolean isSupportTheme() {
		return supportTheme;
	}

	public void setSupportTheme(boolean supportTheme) {
		this.supportTheme = supportTheme;
	}

	public String getZipFile() {
		return zipFile;
	}

	public void setZipFile(String zipFile) {
		this.zipFile = zipFile;
	}

	public ITemplateFetcher getTemplateFetcher() {
		return templateFetcher;
	}

	public void setTemplateFetcher(ITemplateFetcher templateFetcher) {
		this.templateFetcher = templateFetcher;
	}

	public ProjectTheme getProjectTheme() {
		return projectTheme;
	}

	public void setProjectTheme(ProjectTheme projectTheme) {
		this.projectTheme = projectTheme;
	}

	public boolean isRightToLeft() {
		return rightToLeft;
	}

	public void setRightToLeft(boolean rightToLeft) {
		this.rightToLeft = rightToLeft;
	}

	public void setBackendSolution(String backendSolution) {
		this.backendSolution = backendSolution;
	}

	public String getBackendSolution() {
		return backendSolution;
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

	public void setDbDriverMavenDependency(String dbDriverMavenDependency) {
		this.dbDriverMavenDependency = dbDriverMavenDependency;
	}

	public String getDbDriverMavenDependency() {
		return dbDriverMavenDependency;
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

}
