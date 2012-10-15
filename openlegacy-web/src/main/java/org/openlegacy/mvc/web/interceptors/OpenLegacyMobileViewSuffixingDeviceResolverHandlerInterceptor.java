package org.openlegacy.mvc.web.interceptors;

import org.springframework.mobile.device.Device;
import org.springframework.mobile.device.DeviceResolverHandlerInterceptor;
import org.springframework.mobile.device.DeviceUtils;
import org.springframework.mobile.device.site.SitePreference;
import org.springframework.mobile.device.site.SitePreferenceUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class OpenLegacyMobileViewSuffixingDeviceResolverHandlerInterceptor extends DeviceResolverHandlerInterceptor {

	private static final String MOBILE_VIEW_SUFFIX = "_m";

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {

		if ((modelAndView == null) || !modelAndView.isReference() || isRedirectToHome(modelAndView.getViewName())) {
			super.postHandle(request, response, handler, modelAndView);
			return;
		}

		SitePreference sitePreference = SitePreferenceUtils.getCurrentSitePreference(request);
		Device device = (Device)request.getAttribute(DeviceUtils.CURRENT_DEVICE_ATTRIBUTE);

		if (!device.isNormal() || (sitePreference == SitePreference.MOBILE)) {
			modelAndView.setViewName(MessageFormat.format("{0}{1}", modelAndView.getViewName(), MOBILE_VIEW_SUFFIX));
			super.postHandle(request, response, handler, modelAndView);
			return;
		}
		super.postHandle(request, response, handler, modelAndView);
	}

	private static boolean isRedirectToHome(String viewName) {
		String redirectUrl = "";
		if (viewName.startsWith(UrlBasedViewResolver.REDIRECT_URL_PREFIX)) {
			redirectUrl = viewName.substring(UrlBasedViewResolver.REDIRECT_URL_PREFIX.length());
		}
		if (redirectUrl.equals("/")) {
			return true;
		}
		return false;
	}

}
