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
package org.openlegacy.utils;

import java.io.File;

public class FileCommandExecuter {

	public static void execute(File root, FileCommand command) {
		File[] files = root.listFiles();
		if (files == null) {
			return;
		}
		for (File file : files) {
			if (!command.accept(file)) {
				continue;
			}

			if (file.isDirectory()) {
				execute(file, command);
			} else {
				command.doCommand(file);
			}
		}
	}
}