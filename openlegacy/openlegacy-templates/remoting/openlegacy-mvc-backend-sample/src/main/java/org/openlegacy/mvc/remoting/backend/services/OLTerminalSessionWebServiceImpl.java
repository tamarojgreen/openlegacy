/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.mvc.remoting.backend.services;

import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.modules.SessionModule;
import org.openlegacy.modules.table.Table;
import org.openlegacy.modules.table.drilldown.DrilldownAction;
import org.openlegacy.mvc.remoting.services.OLTerminalSession;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.exceptions.ScreenEntityNotAccessibleException;

/**
 * @author Imivan
 * 
 */
// @WebService(serviceName = "TerminalSessionWebService", targetNamespace = "TerminalSessionWebServiceNamespace")
public class OLTerminalSessionWebServiceImpl implements OLTerminalSession {

	private TerminalSession terminalSession;

	public OLTerminalSessionWebServiceImpl(TerminalSession terminalSession) {
		this.terminalSession = terminalSession;
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.Session#disconnect()
	 */
	@Override
	// @WebMethod(exclude = true)
	public void disconnect() {
		terminalSession.disconnect();
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.terminal.TerminalSession#doAction(org.openlegacy.terminal.actions.TerminalAction)
	 */
	@Override
	// @WebMethod(exclude = true)
	public <R extends ScreenEntity> R doAction(TerminalAction action) {
		return terminalSession.doAction(action);
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.terminal.TerminalSession#doAction(org.openlegacy.terminal.actions.TerminalAction, org.openlegacy.terminal.ScreenEntity)
	 */
	@Override
	// @WebMethod(exclude = true)
	public <S extends ScreenEntity, R extends ScreenEntity> R doAction(TerminalAction action, S screenEntity) {
		return terminalSession.doAction(action, screenEntity);
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.terminal.TerminalSession#doAction(org.openlegacy.terminal.actions.TerminalAction, org.openlegacy.terminal.ScreenEntity, java.lang.Class)
	 */
	@Override
	// @WebMethod(exclude = true)
	public <S extends ScreenEntity, R extends ScreenEntity> R doAction(TerminalAction action, S screenEntity,
			Class<R> expectedScreenEntity) throws ScreenEntityNotAccessibleException {
		return terminalSession.doAction(action, screenEntity, expectedScreenEntity);
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.terminal.TerminalSession#doAction(org.openlegacy.terminal.TerminalSendAction)
	 */
	@Override
	// @WebMethod(exclude = true)
	public void doAction(TerminalSendAction terminalSendAction) {
		terminalSession.doAction(terminalSendAction);
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.StatefullSession#fetchSnapshot()
	 */
	@Override
	// @WebMethod(exclude = true)
	public TerminalSnapshot fetchSnapshot() {
		return terminalSession.fetchSnapshot();
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.Session#getDelegate()
	 */
	@Override
	// @WebMethod(exclude = true)
	public Object getDelegate() {
		return terminalSession.getDelegate();
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.terminal.TerminalSession#getEntity()
	 */
	@Override
	// @WebMethod(exclude = true)
	public <S extends ScreenEntity> S getEntity() {
		return terminalSession.getEntity();
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.Session#getEntity(java.lang.Class, java.lang.Object[])
	 */
	@Override
	// @WebMethod
	public <T> T getEntity(Class<T> entityClass, Object... keys) throws EntityNotFoundException {
		return terminalSession.getEntity(entityClass, keys);
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.Session#getEntity(java.lang.String, java.lang.Object[])
	 */
	@Override
	// @WebMethod
	public Object getEntity(String entityName, Object... keys) throws EntityNotFoundException {
		return terminalSession.getEntity(entityName, keys);
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.Session#getModule(java.lang.Class)
	 */
	@Override
	// @WebMethod(exclude = true)
	public <M extends SessionModule> M getModule(Class<M> module) {
		return terminalSession.getModule(module);
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.terminal.TerminalSession#getSequence()
	 */
	@Override
	// @WebMethod(exclude = true)
	public Integer getSequence() {
		return terminalSession.getSequence();
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.terminal.TerminalSession#getSessionId()
	 */
	@Override
	// @WebMethod(exclude = true)
	public String getSessionId() {
		return terminalSession.getSessionId();
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.StatefullSession#getSnapshot()
	 */
	@Override
	// @WebMethod(exclude = true)
	public TerminalSnapshot getSnapshot() {
		return terminalSession.getSnapshot();
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.Session#isConnected()
	 */
	@Override
	// @WebMethod(exclude = true)
	public boolean isConnected() {
		return terminalSession.isConnected();
	}

	/* (non-Javadoc)
	 * @see org.openlegacy.mvc.remoting.services.OLTerminalSession#doTableDrillDown(java.lang.Class, org.openlegacy.modules.table.drilldown.DrilldownAction, java.lang.Object[])
	 */
	@Override
	public <T> void doTableDrillDown(Class<T> targetClass, DrilldownAction<?> drilldownAction, Object... rowKeys) {
		terminalSession.getModule(Table.class).drillDown(targetClass, drilldownAction, rowKeys);
	}

}
