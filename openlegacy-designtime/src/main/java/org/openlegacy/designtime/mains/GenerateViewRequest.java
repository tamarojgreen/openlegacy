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

import org.openlegacy.designtime.UserInteraction;

import java.io.File;

public class GenerateViewRequest extends AbstractGenerateRequest {

	private File entitySourceFile;
	private boolean generateHelp;
	private UserInteraction userInteraction;

	public File getEntitySourceFile() {
		return entitySourceFile;
	}

	public void setEntitySourceFile(File entitySourceFile) {
		this.entitySourceFile = entitySourceFile;
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

}
