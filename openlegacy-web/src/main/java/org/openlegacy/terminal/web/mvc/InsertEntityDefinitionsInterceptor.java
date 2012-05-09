package org.openlegacy.terminal.web.mvc;

import org.openlegacy.EntityDescriptor;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.modules.navigation.Navigation;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.spi.ScreenEntitiesRegistry;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Intercepter class for spring MVC. Injects commonly used beans into the page context so they can be accessed via the web page
 * 
 * @author RoiM
 * 
 */
public class InsertEntityDefinitionsInterceptor extends HandlerInterceptorAdapter {

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private ScreenEntitiesRegistry entitiesRegistry;

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		if (modelAndView == null) {
			return;
		}

		if (!terminalSession.isConnected()) {
			return;
		}

		ScreenEntity entity = terminalSession.getEntity();

		if (entity != null) {
			ScreenEntityDefinition definitions = entitiesRegistry.get(entity.getClass());
			modelAndView.addObject("definitions", definitions);
		}
		Menu menuModule = terminalSession.getModule(Menu.class);
		if (menuModule != null) {
			MenuItem menuRoot = menuModule.getMenuTree();
			modelAndView.addObject("menu", menuRoot);
		}

		Navigation navigationModule = terminalSession.getModule(Navigation.class);
		if (navigationModule != null) {
			List<EntityDescriptor> breadCrumb = navigationModule.getPaths();
			if (breadCrumb != null) {
				modelAndView.addObject("breadCrumb", breadCrumb);
			}
		}

	}
}
