package org.openlegacy.terminal.rest;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.modules.menu.Menu;
import org.openlegacy.modules.menu.MenuItem;
import org.openlegacy.modules.navigation.Navigation;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.support.SimpleScreenEntityWrapper;
import org.openlegacy.utils.ProxyUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

@Controller
public class DefaultRestController {

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private ScreenEntitiesRegistry screenEntitiesRegistry;

	@Inject
	private ScreenEntitySerializer jsonScreenEntitySerializer;

	@RequestMapping(value = "/menu", method = RequestMethod.GET, consumes = { "application/json", "application/xml" })
	public Object getMenu(ModelMap model) {
		MenuItem menus = terminalSession.getModule(Menu.class).getMenuTree();
		return menus;
	}

	@RequestMapping(value = "/{screen}", method = RequestMethod.GET)
	public ModelAndView getScreenEntity(@PathVariable("screen") String screenEntityName) {

		ScreenEntity screenEntity = (ScreenEntity)terminalSession.getEntity(screenEntityName);
		screenEntity = ProxyUtil.getTargetObject(screenEntity);
		ScreenEntityDefinition entityDefinitions = screenEntitiesRegistry.get(screenEntity.getClass());
		SimpleScreenEntityWrapper wrapper = new SimpleScreenEntityWrapper(screenEntity, terminalSession.getModule(
				Navigation.class).getPaths(), entityDefinitions.getActions());
		return new ModelAndView("screenModel", "screenModel", wrapper);

	}

	@RequestMapping(value = "/{screen}", method = RequestMethod.POST, consumes = "application/json")
	public void postScreenEntityJson(@PathVariable("screen") String screenEntityName, @RequestParam("action") String action,
			@RequestBody String json, HttpServletResponse response) throws IOException {

		Class<?> entityClass = screenEntitiesRegistry.getEntityClass(screenEntityName);
		ScreenEntity screenEntity = (ScreenEntity)jsonScreenEntitySerializer.deserialize(json, entityClass);

		postScreenEntity(action, screenEntity);
		response.setStatus(HttpServletResponse.SC_OK);
	}

	@RequestMapping(value = "/{screen}", method = RequestMethod.POST, consumes = "application/xml")
	public void postScreenEntityXml(@PathVariable("screen") String screenEntityName, @RequestParam("action") String action,
			@RequestBody String xml, HttpServletResponse response) throws JAXBException, MarshalException, ValidationException {

		Class<?> entityClass = screenEntitiesRegistry.getEntityClass(screenEntityName);
		InputSource inputSource = new InputSource(new ByteArrayInputStream(xml.getBytes()));
		ScreenEntity screenEntity = (ScreenEntity)Unmarshaller.unmarshal(entityClass, inputSource);

		postScreenEntity(action, screenEntity);
		response.setStatus(HttpServletResponse.SC_OK);
	}

	private void postScreenEntity(String action, ScreenEntity screenEntity) {
		ScreenEntityDefinition entityDefinitions = screenEntitiesRegistry.get(screenEntity.getClass());
		TerminalAction sessionAction = null;
		if (action == null) {
			sessionAction = TerminalActions.ENTER();
		} else {
			List<ActionDefinition> actions = entityDefinitions.getActions();
			for (ActionDefinition actionDefinition : actions) {
				if (actionDefinition.getAlias().equals(action)) {
					sessionAction = (TerminalAction)actionDefinition.getAction();
				}
			}
		}
		Assert.notNull(sessionAction, MessageFormat.format("Alias for session action {0} not found", action));
		terminalSession.doAction(sessionAction, screenEntity);
	}

}
