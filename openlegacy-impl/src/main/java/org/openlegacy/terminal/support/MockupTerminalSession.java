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
package org.openlegacy.terminal.support;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.loaders.support.PartPositionAnnotationLoader;
import org.openlegacy.terminal.mock.MockTerminalConnection;
import org.openlegacy.terminal.wait_conditions.WaitCondition;
import org.openlegacy.utils.ProxyUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A mock-up implementation for a terminal session. Main purpose is to be able to navigate between entities relatively easily, and
 * sync with the terminal connection. Coupled by design to the mock terminal connection.
 * 
 */
public class MockupTerminalSession extends DefaultTerminalSession {

	private static final long serialVersionUID = 1L;

	private final static Log logger = LogFactory.getLog(PartPositionAnnotationLoader.class);

	private Map<Class<?>, SnapshotsList> snapshotsMap = new HashMap<Class<?>, SnapshotsList>();

	@Override
	public TerminalSnapshot getSnapshot() {
		return super.getSnapshot();
	}

	@Override
	public <S> S getEntity(Class<S> screenEntityClass, Object... keys) throws EntityNotFoundException {
		setupMockup(screenEntityClass);
		return super.getEntity(screenEntityClass, keys);
	}

	@SuppressWarnings("unchecked")
	private <S> void setupMockup(Class<S> screenEntityClass) {
		if (getScreenEntitiesRegistry().isDirty()) {
			preserveSnapshots(getConnection());
		}
		screenEntityClass = (Class<S>)ProxyUtil.getOriginalClass(screenEntityClass);
		SnapshotsList snapshotsList = snapshotsMap.get(screenEntityClass);
		if (snapshotsList == null) {
			throw (new EntityNotFoundException("The entity " + screenEntityClass.getSimpleName()
					+ "was not found in the recorded trail"));
		}
		SnapshotInfo snapshotInfo = snapshotsList.getCurrent();
		getConnection().setCurrentIndex(snapshotInfo.getIndexInSession());
	}

	@Override
	protected void doTerminalAction(TerminalSendAction sendAction, WaitCondition... waitConditions) {
		ScreenEntity currentEntity = getEntity();
		super.doTerminalAction(sendAction, waitConditions);
		progressSnapshot(currentEntity);
	}

	private void progressSnapshot(ScreenEntity result) {
		if (result == null) {
			return;
		}
		Class<?> screenEntityClass = ProxyUtil.getOriginalClass(result.getClass());
		SnapshotsList snapshotsList = snapshotsMap.get(screenEntityClass);
		snapshotsList.next();
	}

	public void setTerminalConnection(MockTerminalConnection terminalConnection) {
		super.setConnection(terminalConnection);

		preserveSnapshots(terminalConnection);
	}

	private void preserveSnapshots(MockTerminalConnection terminalConnection) {
		List<TerminalSnapshot> snapshots = terminalConnection.getSnapshots();
		int count = 0;
		snapshotsMap.clear();
		for (TerminalSnapshot terminalSnapshot : snapshots) {
			Class<?> matchedClass = getScreensRecognizer().match(terminalSnapshot);
			if (matchedClass == null) {
				logger.warn("An unrecognized snapshot was found in the trail:");
				logger.warn(terminalSnapshot);
			} else {
				SnapshotsList snapshotsList = snapshotsMap.get(matchedClass);
				if (snapshotsMap.get(matchedClass) == null) {
					snapshotsList = new SnapshotsList();
					snapshotsMap.put(matchedClass, snapshotsList);
				}
				snapshotsList.add(terminalSnapshot, count);
			}
			count++;
		}
	}

	@Override
	public void disconnect() {
		super.disconnect();
		Collection<SnapshotsList> snapshotLists = snapshotsMap.values();
		for (SnapshotsList snapshotsList : snapshotLists) {
			snapshotsList.setCurrent(0);
		}
	}

	@Override
	protected MockTerminalConnection getConnection() {
		return (MockTerminalConnection)super.getConnection();
	}

	/**
	 * A mock-up helper class which manage a list of snapshots and knows always what is the current one
	 * 
	 */
	private static class SnapshotsList implements Serializable {

		private static final long serialVersionUID = 1L;

		private List<SnapshotInfo> terminalSnapshots = new ArrayList<SnapshotInfo>();
		private int current;

		public void add(TerminalSnapshot terminalSnapshot, int indexInSession) {
			// don't put duplicates
			if (!terminalSnapshots.contains(terminalSnapshot)) {
				terminalSnapshots.add(new SnapshotInfo(terminalSnapshot, indexInSession));
			}
		}

		public void setCurrent(int current) {
			this.current = current;
		}

		public SnapshotInfo getCurrent() {
			return terminalSnapshots.get(current);
		}

		public void next() {
			if (current < terminalSnapshots.size() - 1) {
				current++;
			} else {
				current = 0;
			}
		}
	}

	private static class SnapshotInfo implements Serializable {

		private static final long serialVersionUID = 1L;

		private TerminalSnapshot terminalSnapshot;
		private int indexInSession;

		public SnapshotInfo(TerminalSnapshot terminalSnapshot, int indexInSession) {
			this.terminalSnapshot = terminalSnapshot;
			this.indexInSession = indexInSession;
		}

		public int getIndexInSession() {
			return indexInSession;
		}

		@SuppressWarnings("unused")
		public TerminalSnapshot getTerminalSnapshot() {
			return terminalSnapshot;
		}
	}
}
