/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.designtime.mains;

import org.openlegacy.designtime.UserInteraction;

import java.io.File;

public class GeneratePageRequest {

	private File projectDir;
	private File screenEntitySourceFile;
	private File sourceDirectory;
	private String packageDirectoryName;
	private File templatesDir;
	private boolean generateHelp;
	private UserInteraction userInteraction;
	private boolean generateMobilePage;

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

	public UserInteraction getUserInteraction() {
		return userInteraction;
	}

	public void setUserInteraction(UserInteraction userInteraction) {
		this.userInteraction = userInteraction;
	}

	public boolean isGenerateHelp() {
		return generateHelp;
	}

	public void setGenerateHelp(boolean generateHelp) {
		this.generateHelp = generateHelp;
	}

	public boolean isGenerateMobilePage() {
		return generateMobilePage;
	}

	public void setGenerateMobilePage(boolean generateMobilePage) {
		this.generateMobilePage = generateMobilePage;
	}
}
