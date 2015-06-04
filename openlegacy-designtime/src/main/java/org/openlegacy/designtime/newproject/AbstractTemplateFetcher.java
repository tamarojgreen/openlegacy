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
package org.openlegacy.designtime.newproject;

import org.openlegacy.designtime.newproject.organized.ITemplateFetcher;
import org.openlegacy.utils.ZipUtil;

import java.io.File;
import java.io.IOException;

public abstract class AbstractTemplateFetcher implements ITemplateFetcher {

	private File targetZip;

	protected abstract File extractZip(String templateName, File baseDir) throws IOException;

	@Override
	public File fetchZip(String templateName, String projectName, File baseDir) throws IOException {
		targetZip = extractZip(templateName, baseDir);
		return unzipTemplate(baseDir, projectName, targetZip);
	}

	private static File unzipTemplate(File baseDir, String projectName, File targetZip) throws IOException {
		File targetPath = new File(baseDir, projectName);
		ZipUtil.unzip(targetZip.getAbsolutePath(), targetPath.getAbsolutePath());
		return targetPath;
	}

	@Override
	public boolean deleteZip() {
		return this.targetZip.delete();
	}

}
