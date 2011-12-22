package org.openlegacy.terminal.mock;

import org.openlegacy.terminal.TerminalConnection;
import org.openlegacy.terminal.TerminalConnectionFactory;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.persistance.TerminalPersistedSnapshot;
import org.openlegacy.utils.XmlSerializationUtil;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMockTerminalConnectionFactory implements TerminalConnectionFactory {

	private List<String> files = null;
	private String root;
	private List<TerminalSnapshot> snapshots = null;

	public void disconnect(TerminalConnection terminalConnection) {
		// do nothing
	}

	/**
	 * Loads all snapshots from the listed files NOTE: Currently All files are re-load from disk on every get connection, since
	 * snapshots terminal fields gets "dirty" by usage. Future implementation should probably clone the snapshots in-memory
	 * 
	 */
	protected List<TerminalSnapshot> fetchSnapshots() {
		if (snapshots != null) {
			return snapshots;
		}

		snapshots = new ArrayList<TerminalSnapshot>();
		for (String resourceName : files) {
			try {
				TerminalPersistedSnapshot persistedSnapshot = XmlSerializationUtil.deserialize(TerminalPersistedSnapshot.class,
						getClass().getResourceAsStream(MessageFormat.format("{0}/{1}", root, resourceName)));
				snapshots.add(persistedSnapshot);
			} catch (Exception e) {
				throw (new IllegalArgumentException(MessageFormat.format("Faild reading XML trail:{0}", resourceName), e));
			}
		}
		return snapshots;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}
}
