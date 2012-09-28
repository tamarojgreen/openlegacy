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
package org.openlegacy.designtime.newproject.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "project-type")
public class ProjectType {

	private String templateName;
	private String description;
	private boolean isDemo;
	private boolean supportTheme;
	private String zipFile;

	public ProjectType() {}

	public ProjectType(String templateName, String description, boolean isDemo, boolean supportTheme, String zipFile) {
		this.templateName = templateName;
		this.description = description;
		this.isDemo = isDemo;
		this.supportTheme = supportTheme;
		this.zipFile = zipFile;
	}

	public String getTemplateName() {
		return templateName;
	}

	@XmlElement(name = "template-name")
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
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
}
