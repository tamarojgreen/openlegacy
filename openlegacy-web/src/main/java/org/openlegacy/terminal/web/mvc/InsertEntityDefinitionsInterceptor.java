package org.openlegacy.terminal.web.mvc;

import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

public class InsertEntityDefinitionsInterceptor extends HandlerInterceptorAdapter{

	@Inject
	private TerminalSession terminalSession;
	
	@Inject
	private ScreenEntitiesRegistry entitiesRegistry;
	
	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (modelAndView == null){
			return;
		}
		
		ScreenEntity entity = terminalSession.getEntity();

		ScreenEntityDefinition definitions = entitiesRegistry.get(entity.getClass());
		modelAndView.addObject("definitions", definitions);
		MenuItem menuRoot = terminalSession.getModule(Menu.class).getMenuTree();
		modelAndView.addObject("menu", menuRoot);

		List<EntityDescriptor> breadCrumb = terminalSession.getModule(Navigation.class).getPathFromRoot();
		modelAndView.addObject("breadCrumb", breadCrumb);
		
	}
}
