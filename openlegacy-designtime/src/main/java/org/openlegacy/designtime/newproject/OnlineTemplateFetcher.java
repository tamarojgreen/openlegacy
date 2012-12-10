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
package org.openlegacy.designtime.newproject;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

public class OnlineTemplateFetcher extends AbstractTemplateFetcher {

	private String templatesUrl;

	public OnlineTemplateFetcher(String templatesUrl) {
		this.templatesUrl = templatesUrl;
	}

	@Override
	protected File extractZip(String templateName, File baseDir) throws IOException {
		URL zipFile = new URL(MessageFormat.format("{0}/{1}.{2}", this.templatesUrl, templateName, "zip"));

		File targetZip = new File(baseDir, templateName + ".zip");
		FileOutputStream targetZipOutputStream = new FileOutputStream(targetZip);
		IOUtils.copy(zipFile.openStream(), targetZipOutputStream);
		targetZipOutputStream.close();
		return targetZip;
	}

}
