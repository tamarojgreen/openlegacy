/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
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

/**
 * @author Aleksey Yeremeyev
 * 
 */
public class FileTemplateFetcher extends AbstractTemplateFetcher {

	private String templatesUrl;

	public FileTemplateFetcher(String templatesUrl) {
		templatesUrl = templatesUrl.replace("file://", "");
		if (!templatesUrl.startsWith("/")) {
			templatesUrl = "/" + templatesUrl;
		}

		this.templatesUrl = templatesUrl;
	}

	@Override
	protected File extractZip(String templateName, File baseDir) throws IOException {
		File zipFile = new File(templatesUrl, templateName + ".zip");
		return zipFile;
	}

}
