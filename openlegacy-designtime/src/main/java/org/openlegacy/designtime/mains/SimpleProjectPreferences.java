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

import org.openlegacy.designtime.PerfrencesConstants;
import org.openlegacy.utils.ReflectionUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SimpleProjectPreferences implements ProjectPreferences {

	private Properties properties;
	private File file;

	public SimpleProjectPreferences(File file) {
		this.file = file;
		properties = new Properties();
		load(file);
	}

	public String get(String key) {
		Object value = properties.get(key);
		if (value == null) {
			String defaultValue = (String)ReflectionUtil.getStaticFieldValue(PerfrencesConstants.class, key + "_DEFAULT");
			return defaultValue;
		}
		return (String)value;
	}

	public void put(String key, String value) {
		properties.put(key, value);
		save();
	}

	private void save() {
		try {
			properties.store(new FileOutputStream(file), "");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void load(File prefFile) {
		if (!prefFile.exists()) {
			return;
		}
		try {
			properties.load(new FileInputStream(prefFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
