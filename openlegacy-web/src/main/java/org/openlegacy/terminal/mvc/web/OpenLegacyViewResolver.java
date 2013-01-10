package org.openlegacy.terminal.mvc.web;

import org.apache.commons.lang.StringUtils;
import org.springframework.js.ajax.AjaxUrlBasedViewResolver;
import org.springframework.web.servlet.View;

import java.text.MessageFormat;
import java.util.Locale;

/**
 * Check for view name existence as JSPX file, and if not exists returns generic / composite view
 * 
 * @author Roi Mor
 * 
 */
public class OpenLegacyViewResolver extends AjaxUrlBasedViewResolver {

	private String webViewsPath;
	private String mobileViewsPath;
	private String viewsSuffix = ".jspx";

	@Override
	public View resolveViewName(String viewName, Locale locale) throws Exception {

		if (viewName.startsWith("redirect:")) {
			return super.resolveViewName(viewName, locale);

		}

		String viewsPath = viewName.endsWith(MvcConstants.MOBILE_VIEW_SUFFIX) ? mobileViewsPath : webViewsPath;
		String viewFile = viewName.endsWith(MvcConstants.MOBILE_VIEW_SUFFIX) ? viewName.substring(0,
				viewName.indexOf(MvcConstants.MOBILE_VIEW_SUFFIX)) : viewName;

		viewFile = viewFile.endsWith(MvcConstants.VIEW_SUFFIX) ? viewName.substring(0, viewName.indexOf(MvcConstants.VIEW_SUFFIX))
				: viewFile;

		String deviceSuffix = viewName.endsWith(MvcConstants.MOBILE_VIEW_SUFFIX) ? MvcConstants.MOBILE_VIEW_SUFFIX
				: StringUtils.EMPTY;

		if (getServletContext().getResource(MessageFormat.format("{0}/{1}{2}", viewsPath, viewFile, viewsSuffix)) == null) {
			// may end with Composite or Composite_m
			if (viewName.contains(MvcConstants.COMPOSITE_SUFFIX)) {
				viewName = MvcConstants.COMPOSITE + deviceSuffix;
			} else if (viewName.contains(MvcConstants.VIEW_SUFFIX)) {
				viewName = MvcConstants.GENERIC_VIEW + deviceSuffix;
			} else {
				viewName = MvcConstants.GENERIC + deviceSuffix;
			}
		} else {
			// View is used only for determine the generic view type
			viewName = viewName.replace(MvcConstants.VIEW_SUFFIX, "");
		}

		return super.resolveViewName(viewName, locale);
	}

	public void setWebViewsPath(String webViewsPath) {
		this.webViewsPath = webViewsPath;
	}

	public void setMobileViewsPath(String mobileViewsPath) {
		this.mobileViewsPath = mobileViewsPath;
	}

	public void setViewsSuffix(String viewsSuffix) {
		this.viewsSuffix = viewsSuffix;
	}
}
