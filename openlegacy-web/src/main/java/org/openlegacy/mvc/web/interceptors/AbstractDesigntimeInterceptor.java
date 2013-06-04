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
package org.openlegacy.mvc.web.interceptors;

import org.openlegacy.OpenLegacyProperties;
import org.openlegacy.Session;
import org.openlegacy.modules.support.trail.AbstractSessionTrail;
import org.openlegacy.modules.trail.Trail;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AbstractDesigntimeInterceptor extends HandlerInterceptorAdapter {

	@Inject
	private OpenLegacyProperties openLegacyProperties;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		if (!getSession().isConnected()) {
			return true;
		}

		// check out if configured to save trail - if so, define unlimited recording size (design-time)
		String trailPath = openLegacyProperties.getTrailPath();
		if (trailPath != null) {
			AbstractSessionTrail<?> trail = (AbstractSessionTrail<?>)getSession().getModule(Trail.class).getSessionTrail();
			trail.setHistoryCount(null);
		}

		return true;

	}

	protected abstract Session getSession();
}
