package org.openlegacy.terminal.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.modules.SessionModule;
import org.openlegacy.support.AbstractSession;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalActionMapper;
import org.openlegacy.terminal.TerminalConnectionListener;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;
import org.openlegacy.terminal.spi.ScreensRecognizer;
import org.openlegacy.terminal.spi.SessionNavigator;
import org.openlegacy.terminal.spi.TerminalSendAction;
import org.openlegacy.terminal.support.binders.ScreenEntityBinder;
import org.openlegacy.terminal.utils.ScreenPainter;
import org.openlegacy.utils.ProxyUtil;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.List;

import javax.inject.Inject;

/**
 * A default session class exposes screenEntity building and sending
 * 
 * 
 */
public class DefaultTerminalSession extends AbstractSession implements TerminalSession {

	@Inject
	private List<ScreenEntityBinder> screenEntityBinders;

	private TerminalConnectionDelegator terminalConnection;

	private ScreenEntity entity;

	@Inject
	private SessionNavigator sessionNavigator;

	@Inject
	private ScreensRecognizer screensRecognizer;

	@Inject
	private TerminalActionMapper terminalActionMapper;

	private ScreenEntityMethodInterceptor interceptor;

	private final static Log logger = LogFactory.getLog(DefaultTerminalSession.class);

	@SuppressWarnings("unchecked")
	public <S> S getEntity(Class<S> screenEntityClass) throws EntityNotFoundException {

		// check if the entity matched the cached entity
		if (entity == null || !ProxyUtil.isClassesMatch(entity.getClass(), screenEntityClass)) {
			sessionNavigator.navigate(this, screenEntityClass);

			TerminalSnapshot terminalSnapshot = getSnapshot();

			ScreenEntity screenEntity = (ScreenEntity)ProxyUtil.createPojoProxy(screenEntityClass, ScreenEntity.class,
					interceptor);

			for (ScreenEntityBinder screenEntityBinder : screenEntityBinders) {
				screenEntityBinder.populateEntity(screenEntity, terminalSnapshot);
			}

			entity = screenEntity;
		}
		return (S)entity;
	}

	@SuppressWarnings("unchecked")
	public <S extends ScreenEntity> S getEntity() {
		if (entity == null) {
			TerminalSnapshot terminalSnapshot = getSnapshot();

			Class<?> matchedScreenEntity = screensRecognizer.match(terminalSnapshot);
			if (matchedScreenEntity == null) {
				return null;
			}

			entity = (S)getEntity(matchedScreenEntity);
		}
		return (S)entity;
	}

	public <R extends ScreenEntity> R doAction(TerminalAction action) {
		return doAction(action, null);
	}

	public <S extends ScreenEntity, R extends ScreenEntity> R doAction(TerminalAction terminalAction, S screenEntity,
			Class<R> expectedEntity) {
		try {
			@SuppressWarnings("unchecked")
			R object = (R)doAction(terminalAction, screenEntity);
			return object;
		} catch (ClassCastException e) {
			throw (new ScreenEntityNotAccessibleException(e));
		}

	}

	public <S extends ScreenEntity, R extends ScreenEntity> R doAction(TerminalAction terminalAction, S screenEntity) {

		entity = null;

		Object command = terminalActionMapper.getCommand(terminalAction.getClass());
		if (command == null) {
			terminalAction.perform(this, screenEntity);
		} else {
			SimpleTerminalSendAction sendAction = new SimpleTerminalSendAction(command);

			if (screenEntity != null) {
				for (ScreenEntityBinder screenEntityBinder : screenEntityBinders) {
					screenEntityBinder.populateSendAction(sendAction, getSnapshot(), screenEntity);
				}
			}

			if (logger.isDebugEnabled()) {
				TerminalSnapshot snapshot = getSnapshot();
				logger.debug(MessageFormat.format("\nAction:{0}, Cursor:{1}\n", sendAction.getCommand(),
						sendAction.getCursorPosition()));
				logger.debug("\nScreen before\n(* abc * marks a modified field, [ abc ] mark an input field, # mark cursor):\n\n"
						+ ScreenPainter.paint(snapshot, sendAction, true));
			}

			notifyModulesBeforeSend(sendAction);
			terminalConnection.doAction(sendAction);
			notifyModulesAfterSend();

			if (logger.isDebugEnabled()) {
				logger.debug("\n\nScreen after ([ abc ] indicates a input field):\n\n" + getSnapshot());
			}
		}

		return getEntity();
	}

	private void notifyModulesBeforeSend(TerminalSendAction terminalSendAction) {
		Collection<? extends SessionModule> modulesList = getSessionModules().getModules();
		for (SessionModule sessionModule : modulesList) {
			if (sessionModule instanceof TerminalConnectionListener) {
				((TerminalConnectionListener)sessionModule).beforeSendAction(terminalConnection, terminalSendAction);
			}
		}
	}

	private void notifyModulesAfterSend() {
		Collection<? extends SessionModule> modulesList = getSessionModules().getModules();
		for (SessionModule sessionModule : modulesList) {
			if (sessionModule instanceof TerminalConnectionListener) {
				((TerminalConnectionListener)sessionModule).afterSendAction(terminalConnection);
			}
		}
	}

	public TerminalSnapshot getSnapshot() {
		return terminalConnection.getSnapshot();
	}

	public Object getDelegate() {
		return terminalConnection.getDelegate();
	}

	public void setTerminalConnection(TerminalConnectionDelegator terminalConnection) {
		this.terminalConnection = terminalConnection;
	}

	public void disconnect() {
		terminalConnection.disconnect();
	}

	public boolean isConnected() {
		return terminalConnection.isConnected();
	}

	public void setInterceptor(ScreenEntityMethodInterceptor interceptor) {
		this.interceptor = interceptor;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		super.afterPropertiesSet();
		interceptor.setTerminalSession(this);
	}
}
