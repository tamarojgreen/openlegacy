package org.openlegacy.terminal.mock;

import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionFactory;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.persistance.TerminalPersistedSnapshot;
import org.openlegacy.utils.JaxbUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MockTerminalConnectionFactory implements TerminalConnectionFactory {

	private List<String> files = null;
	private List<TerminalSnapshot> snapshots = null;

	public TerminalConnection getConnection() {
		initSnapshots();
		return new MockTerminalConnection(snapshots);
	}

	private void initSnapshots() {
		if (snapshots != null) {
			return;
		}
		snapshots = new ArrayList<TerminalSnapshot>();
		for (String resourceName : files) {
			try {
				TerminalPersistedSnapshot persistedSnapshot = JaxbUtil.unmarshal(TerminalPersistedSnapshot.class,
						getClass().getResourceAsStream(resourceName));
				snapshots.add(persistedSnapshot);
			} catch (Exception e) {
				throw (new IllegalStateException(MessageFormat.format("Faild reading XML trail:{0}", resourceName), e));
			}
		}

	}

	public void disconnect(TerminalConnection terminalConnection) {
		// do nothing
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}
}
