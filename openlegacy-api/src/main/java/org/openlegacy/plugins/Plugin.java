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
package org.openlegacy.plugins;

import java.util.List;
import java.util.Map;

public interface Plugin {

	public static final String PATH_KEY = "path";

	public static final String MENU_CATEGORY = "category";
	public static final String MENU_TEXT = "text";
	public static final String MENU_ACTION = "action";

	public String getCreator();

	public List<String> getCssItems();

	public String getCssPath();

	public String getDescription();

	public String getExcludedViewPathPart();

	public List<String> getJsItems();

	public String getJsPath();

	public List<Map<String, String>> getMenuItems();

	public String getName();

	public List<String> getSpringContextResources();

	public List<String> getSpringWebContextResources();

	public String getVersion();

	public List<String> getViewDeclarations();

	public List<String> getViews();

	public boolean isViewExtractedToParent();

}
