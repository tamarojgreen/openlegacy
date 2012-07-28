package org.openlegacy.web.mvc;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/reload")
public class ReloadContextController {

	private String reloadPassword = null;

	@Inject
	private ApplicationContext applicationContext;

	@RequestMapping(method = RequestMethod.GET)
	public void reload(@RequestParam("password") String password, HttpServletResponse response) {
		if (reloadPassword == null) {
			throw (new RuntimeException("Reload password not configured. Reload is not supported"));
		}
		if (!StringUtils.equals(reloadPassword, password)) {
			throw (new RuntimeException("Password dont match reload password"));
		}
		((ConfigurableApplicationContext)applicationContext.getParent()).refresh();
		((ConfigurableApplicationContext)applicationContext).refresh();
		response.setStatus(HttpServletResponse.SC_OK);
	}

	public void setReloadPassword(String reloadPassword) {
		this.reloadPassword = reloadPassword;
	}
}
