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
package org.openlegacy.designtime.newproject.organized;

import java.io.File;
import java.io.IOException;

public interface ITemplateFetcher {

	public File fetchZip(String templateName, String projectName, File baseDir) throws IOException;

	public boolean deleteZip();
}
