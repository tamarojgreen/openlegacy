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

package org.openlegacy.designtime;

/**
 * @author Ivan Bort
 * 
 */
public interface DesigntimeConstants {

	public static final String SERVICE_CONTEXT_RELATIVE_PATH = "src/main/resources/META-INF/spring/serviceContext.xml";
	public static final String DEFAULT_SPRING_CONTEXT_FILE = "/src/main/resources/META-INF/spring/applicationContext.xml";

	public static final String SERVICE_IMPL_SUFFIX = "ServiceImpl.java";
	public static final String SERVICE_SUFFIX = "Service.java";
	public static final String TEST_SUFFIX = "Test.java";
	public static final String CONTROLLER_SUFFIX = "Controller.java";
	public static final String INIT_ACTION = "InitAction.java";
	public static final String CLEANUP_ACTION = "CleanupAction.java";
	public static final String KEEP_ALIVE_ACTION = "KeepAliveAction.java";

}
