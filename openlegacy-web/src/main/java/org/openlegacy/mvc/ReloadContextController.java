package org.openlegacy.mvc;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/reload")
public class ReloadContextController {

	private String reloadPassword = null;

	@Inject
	private ApplicationContext applicationContext;

	/**
	 * allow "localhost" to reload by default
	 */
	private String ip = "localhost";

	@RequestMapping(method = RequestMethod.GET)
	public void reload(@RequestParam(value = "password", required = false) String password, HttpServletRequest request,
			HttpServletResponse response) {

		if (request.getServerName().toString().contains(ip)) {
			// OK
		} else {
			if (reloadPassword == null) {
				try {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
							"Reload password not configured. Reload is not supported");
					return;
				} catch (IOException e) {
					throw (new RuntimeException(e));
				}
			}
			if (!StringUtils.equals(reloadPassword, password)) {
				try {
					response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Password dont match reload password");
				} catch (IOException e) {
					throw (new RuntimeException(e));
				}
				return;
			}
		}

		((ConfigurableApplicationContext)applicationContext.getParent()).refresh();
		((ConfigurableApplicationContext)applicationContext).refresh();
		response.setStatus(HttpServletResponse.SC_OK);
		try {
			response.getWriter().write("<html><body>Application context reloaded!</body></html>");
		} catch (IOException e) {
			// do nothing;
		}
	}

	public void setReloadPassword(String reloadPassword) {
		this.reloadPassword = reloadPassword;
	}

	public void setEnableReloadFromIp(String ip) {
		this.ip = ip;
	}
}
