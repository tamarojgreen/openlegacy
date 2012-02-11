package org.openlegacy.web;

import org.openlegacy.OpenLegacyProperties;

public class OpenLegacyWebProperties implements OpenLegacyProperties {

	public static final String TRAIL_FOLDER_PATH = "org.openlegacy.trail.path";

	public String getProperty(String propertyName) {
		return System.getProperty(propertyName);
	}

}
