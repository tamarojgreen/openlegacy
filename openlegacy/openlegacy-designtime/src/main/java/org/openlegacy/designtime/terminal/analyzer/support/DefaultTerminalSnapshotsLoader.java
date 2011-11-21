package org.openlegacy.designtime.terminal.analyzer.support;

import org.openlegacy.designtime.analyzer.SnapshotsLoader;
import org.openlegacy.exceptions.UnableToLoadSnapshotException;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.persistance.TerminalPersistedSnapshot;
import org.openlegacy.utils.JaxbUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

@Component
public class DefaultTerminalSnapshotsLoader implements SnapshotsLoader<TerminalSnapshot> {

	public List<TerminalSnapshot> loadAll(String root) throws UnableToLoadSnapshotException {
		File rootFolder = new File(root);
		File[] files = rootFolder.listFiles(new FilenameFilter() {

			public boolean accept(File dir, String name) {
				return name.endsWith(".xml");
			}
		});

		List<TerminalSnapshot> snapshots = new ArrayList<TerminalSnapshot>();
		for (File file : files) {
			TerminalSnapshot terminalSnapshot;
			try {
				terminalSnapshot = JaxbUtil.unmarshal(TerminalPersistedSnapshot.class, new FileInputStream(file));
			} catch (FileNotFoundException e) {
				throw (new UnableToLoadSnapshotException(e));
			} catch (JAXBException e) {
				throw (new UnableToLoadSnapshotException(e));
			}
			snapshots.add(terminalSnapshot);
		}
		return snapshots;
	}
}
