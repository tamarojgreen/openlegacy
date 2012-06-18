package org.openlegacy.ide.eclipse.util;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.openlegacy.ide.eclipse.PluginConstants;
import org.osgi.service.prefs.BackingStoreException;

public class Prefrences {

	private final static Logger logger = Logger.getLogger(Prefrences.class);

	public static String get(String key, String defaultValue) {
		IEclipsePreferences preferences = new ConfigurationScope().getNode(PluginConstants.PLUGIN_ID);
		return preferences.get(key, defaultValue);

	}

	public static void put(String key, String value) {
		IEclipsePreferences preferences = new ConfigurationScope().getNode(PluginConstants.PLUGIN_ID);
		preferences.put(key, value);
		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			logger.fatal(e);
		}

	}
}
