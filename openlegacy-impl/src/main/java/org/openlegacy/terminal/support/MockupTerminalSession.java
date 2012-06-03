package org.openlegacy.terminal.support;

import org.openlegacy.exceptions.EntityNotFoundException;
import org.openlegacy.terminal.ScreenEntity;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.mock.MockTerminalConnection;
import org.openlegacy.utils.ProxyUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A mock-up implementation for a terminal session. Main purpose is to be able to navigate between entities relatively easily, and
 * sync with the terminal connection. Coupled by design to the mock terminal connection.
 * 
 * TODO: <br/>
 * - Design is not ideal and may be error prone.<br/>
 * - Actions are NOT sync immediately (Next on demo)
 * 
 */
public class MockupTerminalSession extends DefaultTerminalSession {

	private Map<Class<?>, SnapshotsList> snapshotsMap = new HashMap<Class<?>, SnapshotsList>();

	private TerminalSnapshot terminalSnapshot;

	@Override
	public TerminalSnapshot getSnapshot() {
		if (terminalSnapshot == null) {
			terminalSnapshot = getTerminalConnection().getSnapshot();
		}
		return terminalSnapshot;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S> S getEntity(Class<S> screenEntityClass) throws EntityNotFoundException {
		screenEntityClass = (Class<S>)ProxyUtil.getOriginalClass(screenEntityClass);
		SnapshotInfo snapshotInfo = snapshotsMap.get(screenEntityClass).getCurrent();
		terminalSnapshot = snapshotInfo.getTerminalSnapshot();
		getTerminalConnection().setCurrentIndex(snapshotInfo.getIndexInSession());
		return super.getEntity(screenEntityClass);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <S extends ScreenEntity, R extends ScreenEntity> R doAction(TerminalAction terminalAction, S screenEntity,
			java.lang.Class<R> expectedEntity) {
		terminalSnapshot = null;
		ScreenEntity result = super.doAction(terminalAction, screenEntity);
		syncSnapshot(result);
		return (R)result;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public <S extends ScreenEntity, R extends ScreenEntity> R doAction(TerminalAction terminalAction, S screenEntity) {
		terminalSnapshot = null;
		ScreenEntity result = super.doAction(terminalAction, screenEntity);
		syncSnapshot(result);
		return (R)result;
	}

	/**
	 * Sync the result screen entity with the current terminalSnapshot
	 * 
	 * @param result
	 */
	private void syncSnapshot(ScreenEntity result) {
		Class<?> screenEntityClass = ProxyUtil.getOriginalClass(result.getClass());
		SnapshotsList snapshotsList = snapshotsMap.get(screenEntityClass);
		snapshotsList.next();
		terminalSnapshot = snapshotsList.getCurrent().getTerminalSnapshot();
	}

	@Override
	protected void notifyModulesAfterSend() {
		terminalSnapshot = null;
		super.notifyModulesAfterSend();
		terminalSnapshot = null;
	}

	public void setTerminalConnection(MockTerminalConnection terminalConnection) {
		super.setTerminalConnection(terminalConnection);

		preserveSnapshots(terminalConnection);
	}

	private void preserveSnapshots(MockTerminalConnection terminalConnection) {
		List<TerminalSnapshot> snapshots = terminalConnection.getSnapshots();
		int count = 0;
		for (TerminalSnapshot terminalSnapshot : snapshots) {
			Class<?> matchedClass = getScreensRecognizer().match(terminalSnapshot);
			SnapshotsList snapshotsList = snapshotsMap.get(matchedClass);
			if (snapshotsMap.get(matchedClass) == null) {
				snapshotsList = new SnapshotsList();
				snapshotsMap.put(matchedClass, snapshotsList);
			}
			snapshotsList.add(terminalSnapshot, count++);
		}
	}

	@Override
	public void disconnect() {
		Collection<SnapshotsList> snapshotLists = snapshotsMap.values();
		for (SnapshotsList snapshotsList : snapshotLists) {
			snapshotsList.setCurrent(0);
		}
		super.disconnect();
	}

	@Override
	protected MockTerminalConnection getTerminalConnection() {
		return (MockTerminalConnection)super.getTerminalConnection();
	}

	/**
	 * A mock-up helper class which manage a list of snapshots and knows always what is the current one
	 * 
	 */
	private static class SnapshotsList {

		private List<SnapshotInfo> terminalSnapshots = new ArrayList<SnapshotInfo>();
		private int current;

		public void add(TerminalSnapshot terminalSnapshot, int indexInSession) {
			terminalSnapshots.add(new SnapshotInfo(terminalSnapshot, indexInSession));
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

	private static class SnapshotInfo {

		private TerminalSnapshot terminalSnapshot;
		private int indexInSession;

		public SnapshotInfo(TerminalSnapshot terminalSnapshot, int indexInSession) {
			this.terminalSnapshot = terminalSnapshot;
			this.indexInSession = indexInSession;
		}

		public int getIndexInSession() {
			return indexInSession;
		}

		public TerminalSnapshot getTerminalSnapshot() {
			return terminalSnapshot;
		}
	}
}
