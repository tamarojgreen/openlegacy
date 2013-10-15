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

import org.openlegacy.mvc.web.MvcConstants;
import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.mobile.device.DeviceType;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OpenLegacyMobileViewSuffixingDeviceResolverHandlerInterceptor extends DeviceResolverHandlerInterceptor {

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {

		if ((modelAndView == null) || !modelAndView.isReference()
				|| modelAndView.getViewName().startsWith(UrlBasedViewResolver.REDIRECT_URL_PREFIX)) {
			super.postHandle(request, response, handler, modelAndView);
			return;
		}

		SitePreference sitePreference = SitePreferenceUtils.getCurrentSitePreference(request);
		Device device = (Device)request.getAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE);

		if (!(modelAndView.getViewName().startsWith("redirect"))) {
			modelAndView.addObject(MvcConstants.DEVICE_TYPE_ATTRIBUTE, getDeviceType(device));
		}

		if ((sitePreference == SitePreference.MOBILE)) {
			modelAndView.setViewName(MessageFormat.format("{0}{1}", modelAndView.getViewName(), MvcConstants.MOBILE_VIEW_SUFFIX));
			super.postHandle(request, response, handler, modelAndView);
			return;
		}
		super.postHandle(request, response, handler, modelAndView);
	}

	private static DeviceType getDeviceType(Device device) {
		if (device.isMobile()) {
			return DeviceType.MOBILE;
		}
		if (device.isTablet()) {
			return DeviceType.TABLET;
		}
		return DeviceType.NORMAL;
	}
}
