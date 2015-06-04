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
package org.openlegacy.ide.eclipse.ui.preferences;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.openlegacy.ide.eclipse.Activator;
import org.openlegacy.ide.eclipse.PluginConstants;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.MessageFormat;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();

		String templatesUrl = PluginConstants.TEMPLATES_URL;

		String ideInstallationPath = getIdeInstallationPath();
		if (ideInstallationPath != null) {
			templatesUrl = MessageFormat.format("file://{0}/edition", ideInstallationPath);
		}
		store.setDefault(PreferenceConstants.P_TEMPLATES_URL, templatesUrl);
		store.setDefault(PreferenceConstants.P_ANALYZE_NEW_TRAILS, true);
	}

	private static String getIdeInstallationPath() {
		URL url = Platform.getInstallLocation().getURL();
		String path = url.getPath();
		File installationDir = null;
		if (path.contains("%20")) {
			try {
				installationDir = new File(url.toURI());
			} catch (URISyntaxException e) {
				return null;
			}
		} else {
			installationDir = new File(path);
		}
		if (installationDir.exists() && installationDir.isDirectory()) {
			return installationDir.getAbsolutePath();
		}
		return null;
	}
}
