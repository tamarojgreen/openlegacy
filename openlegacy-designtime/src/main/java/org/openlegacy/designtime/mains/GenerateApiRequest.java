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

import org.openlegacy.designtime.EntityUserInteraction;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.io.File;

public class GenerateApiRequest {

	private File trailFile;
	private File sourceDirectory;
	private String packageDirectory;
	private File codeGenerationTemplatesDirectory;
	private EntityUserInteraction<ScreenEntityDefinition> entityUserInteraction;
	private File analyzerContextFile;
	private File projectPath;

	public File getTrailFile() {
		return trailFile;
	}

	public void setTrailFile(File trailFile) {
		this.trailFile = trailFile;
	}

	public File getSourceDirectory() {
		return sourceDirectory;
	}

	public void setSourceDirectory(File sourceDirectory) {
		this.sourceDirectory = sourceDirectory;
	}

	public String getPackageDirectory() {
		return packageDirectory;
	}

	public void setPackageDirectory(String packageDir) {
		this.packageDirectory = packageDir;
	}

	public File getCodeGenerationTemplatesDirectory() {
		return codeGenerationTemplatesDirectory;
	}

	public void setCodeGenerationTemplatesDirectory(File codeGenerationTemplatesDirectory) {
		this.codeGenerationTemplatesDirectory = codeGenerationTemplatesDirectory;
	}

	public EntityUserInteraction<ScreenEntityDefinition> getEntityUserInteraction() {
		return entityUserInteraction;
	}

	public void setEntityUserInteraction(EntityUserInteraction<ScreenEntityDefinition> entityUserInteraction) {
		this.entityUserInteraction = entityUserInteraction;
	}

	public File getProjectPath() {
		return projectPath;
	}

	public void setProjectPath(File projectPath) {
		this.projectPath = projectPath;
	}

}
