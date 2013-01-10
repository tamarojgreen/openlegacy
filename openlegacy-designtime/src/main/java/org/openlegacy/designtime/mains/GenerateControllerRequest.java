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

public class GenerateControllerRequest extends AbstractGenerateRequest {

	private File screenEntitySourceFile;
	private UserInteraction userInteraction;

	public File getScreenEntitySourceFile() {
		return screenEntitySourceFile;
	}

	public void setScreenEntitySourceFile(File screenEntitySourceFile) {
		this.screenEntitySourceFile = screenEntitySourceFile;
	}

	public UserInteraction getUserInteraction() {
		return userInteraction;
	}

	public void setUserInteraction(UserInteraction userInteraction) {
		this.userInteraction = userInteraction;
	}

}
