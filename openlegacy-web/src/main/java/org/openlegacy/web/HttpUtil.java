package org.openlegacy.web;

import javax.servlet.http.HttpServletRequest;

public class HttpUtil {

	public static boolean isAjaxRequest(HttpServletRequest request) {
		String contentType = request.getContentType();
		if (contentType != null && contentType.equals("application/x-www-form-urlencoded")) {
			return true;
		}
		return false;

	}
}
