package org.openlegacy.mvc.web.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ManifestInterceptor extends HandlerInterceptorAdapter {

	@Autowired
	private ServletContext context;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		if (modelAndView == null) {
			return;
		}
		if (request.getRequestURI().contains(".")) {
			return;
		}
		java.util.jar.Manifest manifest = new java.util.jar.Manifest();

		InputStream is = context.getResourceAsStream("/META-INF/MANIFEST.MF");
		if (is != null) {
			manifest.read(is);
			Map<String, String> attributeValues = new HashMap<String, String>();
			Set<Entry<Object, Object>> values = manifest.getMainAttributes().entrySet();
			for (Entry<Object, Object> entry : values) {
				attributeValues.put(entry.getKey().toString(), entry.getValue().toString());
			}
			modelAndView.addObject("manifest", attributeValues);
		}
	}
}
