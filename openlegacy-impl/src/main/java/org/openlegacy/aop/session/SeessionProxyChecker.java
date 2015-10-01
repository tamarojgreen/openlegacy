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

package org.openlegacy.aop.session;

import org.apache.commons.lang.ClassUtils;
import org.openlegacy.Session;
import org.openlegacy.aop.BeanProxyChecker;

public class SeessionProxyChecker implements BeanProxyChecker {

	@Override
	public boolean needToProxy(Class<?> clazz) {
		return ClassUtils.getAllInterfaces(clazz).contains(Session.class);
	}

}
