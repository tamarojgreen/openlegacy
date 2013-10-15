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
package org.openlegacy.mvc.web;

import org.springframework.web.servlet.view.UrlBasedViewResolver;

public class MvcConstants {

	public static final String MOBILE_VIEW_SUFFIX = "_m";

	public static final String DEVICE_TYPE_ATTRIBUTE = "deviceType";

	public static final String COMPOSITE_SUFFIX = "Composite";

	public static final String VIEW_SUFFIX = "View";

	public static final String COMPOSITE = "composite";

	public static final String GENERIC_VIEW = "genericView";

	public static final String GENERIC = "generic";

	public static final String GENERIC_WINDOW = "genericWindow";

	public static final String MENU = "mainMenu";

	public static final String ROOTMENU_VIEW = "rootMenu";

	public static final String LOGIN_VIEW = "login";

	public static final String LOGIN_MODEL = "login";

	public static final String REDIRECT = UrlBasedViewResolver.REDIRECT_URL_PREFIX + "/";

}
