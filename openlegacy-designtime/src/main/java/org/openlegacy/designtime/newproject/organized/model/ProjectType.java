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
package org.openlegacy.designtime.newproject.organized.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "project-type")
public class ProjectType {

	private String templateName;
	private boolean isDemo;
	private boolean supportTheme;
	private String zipFile;
	private String backendSolution;
	private String frontendSolution;

	public ProjectType() {}

	public String getTemplateName() {
		return templateName;
	}

	@XmlElement(name = "template-name")
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public boolean isDemo() {
		return isDemo;
	}

	@XmlElement(name = "is-demo")
	public void setDemo(boolean isDemo) {
		this.isDemo = isDemo;
	}

	public boolean isSupportTheme() {
		return supportTheme;
	}

	@XmlElement(name = "support-theme")
	public void setSupportTheme(boolean supportTheme) {
		this.supportTheme = supportTheme;
	}

	public String getZipFile() {
		return zipFile;
	}

	@XmlElement(name = "zip-file")
	public void setZipFile(String zipFile) {
		this.zipFile = zipFile;
	}

	public String getBackendSolution() {
		return backendSolution;
	}

	@XmlElement(name = "backend-solution")
	public void setBackendSolution(String backendSolution) {
		this.backendSolution = backendSolution;
	}

	public String getFrontendSolution() {
		return frontendSolution;
	}

	@XmlElement(name = "frontend-solution")
	public void setFrontendSolution(String frontendSolution) {
		this.frontendSolution = frontendSolution;
	}

}
