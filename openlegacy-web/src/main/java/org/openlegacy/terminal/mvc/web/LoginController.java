package org.openlegacy.terminal.mvc.web;

import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.login.Login.LoginEntity;
import org.openlegacy.modules.login.LoginException;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.mvc.OpenLegacyWebProperties;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.ScreenPojoFieldAccessor;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.utils.ScreenEntityUtils;
import org.openlegacy.terminal.utils.SimpleScreenPojoFieldAccessor;
import org.openlegacy.utils.ProxyUtil;
import org.openlegacy.utils.ReflectionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.Assert;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(value = "/")
public class LoginController {

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Inject
	private OpenLegacyWebProperties openlegacyWebProperties;

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private ScreenEntityUtils screenEntityUtils;

	@RequestMapping(method = RequestMethod.GET)
	public String home(Model uiModel) {

		if (terminalSession.isConnected()) {
			MenuItem mainMenu = terminalSession.getModule(Menu.class).getMenuTree();
			if (mainMenu != null) {
				Class<?> mainMenuEntity = mainMenu.getTargetEntity();
				return MvcConstants.REDIRECT + screenEntitiesRegistry.get(mainMenuEntity).getEntityClassName();
			} else {
				Assert.notNull(openlegacyWebProperties.getFallbackUrl(), "No fallback URL defined");
				return MvcConstants.REDIRECT + openlegacyWebProperties.getFallbackUrl();
			}
		}

		ScreenEntityDefinition loginEntityDefinition = screenEntitiesRegistry.getSingleEntityDefinition(LoginEntity.class);
		if (loginEntityDefinition != null) {
			Object loginEntity = ReflectionUtil.newInstance(loginEntityDefinition.getEntityClass());
			uiModel.addAttribute(MvcConstants.LOGIN_MODEL, loginEntity);
			return MvcConstants.LOGIN_VIEW;
		}
		return MvcConstants.REDIRECT + openlegacyWebProperties.getFallbackUrl();
	}

	@RequestMapping(value = "Login", method = RequestMethod.POST)
	public String login(Model uiModel, HttpServletRequest request,
			@RequestParam(value = "partial", required = false) String partial) {

		ScreenEntityDefinition loginEntityDefinition = screenEntitiesRegistry.getSingleEntityDefinition(LoginEntity.class);

		terminalSession.getModule(Login.class).logoff();

		ScreenEntity loginEntity = (ScreenEntity)terminalSession.getEntity(loginEntityDefinition.getEntityClass());
		ServletRequestDataBinder binder = new ServletRequestDataBinder(loginEntity);
		binder.bind(request);

		try {
			terminalSession.getModule(Login.class).login(loginEntity);
		} catch (LoginException e) {
			terminalSession.getModule(Login.class).logoff();
			ScreenPojoFieldAccessor fieldAccessor = new SimpleScreenPojoFieldAccessor(loginEntity);
			fieldAccessor.setFieldValue(Login.ERROR_FIELD_NAME, e.getMessage());
			uiModel.addAttribute(MvcConstants.LOGIN_MODEL, loginEntity);
			return MvcConstants.LOGIN_VIEW;
		}

		ScreenEntity screenEntity = terminalSession.getEntity();

		if (screenEntity != null) {
			String resultEntityName = ProxyUtil.getOriginalClass(screenEntity.getClass()).getSimpleName();
			if (partial != null) {
				return MvcConstants.ROOTMENU_VIEW;
			} else {
				return MvcConstants.REDIRECT + resultEntityName;
			}
		}
		return MvcConstants.REDIRECT + openlegacyWebProperties.getFallbackUrl();
	}
}
