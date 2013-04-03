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

import java.io.File;

public abstract class AbstractGenerateRequest {

	private File sourceDirectory;
	private String packageDirectory;
	private File TemplatesDirectory;
	private File projectPath;
	private boolean generateAspectJ;

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

	public boolean isGenerateAspectJ() {
		return generateAspectJ;
	}

	public void setGenerateAspectJ(boolean generateAspectJ) {
		this.generateAspectJ = generateAspectJ;
	}

}
