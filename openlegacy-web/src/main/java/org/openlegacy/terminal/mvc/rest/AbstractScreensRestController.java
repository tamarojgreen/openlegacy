package org.openlegacy.terminal.mvc.rest;

import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.modules.login.Login;
import org.openlegacy.modules.roles.Roles;
import org.openlegacy.mvc.AbstractRestController;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;
import org.openlegacy.terminal.services.ScreenEntitiesRegistry;
import org.openlegacy.terminal.utils.ScreenEntityUtils;

import java.util.List;

import javax.inject.Inject;

public abstract class AbstractScreensRestController extends AbstractRestController {

	@Inject
	private TerminalSession terminalSession;

	@Inject
	private ScreenEntitiesRegistry entitiesRegistry;

	@Inject
	private ScreenEntityUtils screenEntityUtils;

	@Override
	protected TerminalSession getSession() {
		return terminalSession;
	}

	@Override
	protected ScreenEntitiesRegistry getEntitiesRegistry() {
		return entitiesRegistry;
	}

	@Override
	protected List<ActionDefinition> getActions(Object entity) {
		// actions for screen exists on the entity. No need to fetch from registry
		return null;
	}

	@Override
	protected Object sendEntity(Object entity, String action) {
		TerminalActionDefinition actionDefinition = screenEntityUtils.findAction((ScreenEntity) entity, action);
		Roles rolesModule = terminalSession.getModule(Roles.class);
		if (rolesModule != null) {
			rolesModule.populateEntity(entity, terminalSession.getModule(Login.class));
		}
		return terminalSession.doAction((TerminalAction) actionDefinition.getAction(), (ScreenEntity) entity);
	}
}
