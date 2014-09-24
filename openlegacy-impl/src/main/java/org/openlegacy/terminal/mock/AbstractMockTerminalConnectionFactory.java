/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.terminal.mock;

import org.openlegacy.exceptions.UnableToLoadSnapshotException;
import org.openlegacy.terminal.TerminalConnectionFactory;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.TerminalSnapshot.SnapshotType;
import org.openlegacy.terminal.modules.trail.TerminalPersistedTrail;
import org.openlegacy.terminal.persistance.TerminalPersistedSnapshot;
import org.openlegacy.utils.XmlSerializationUtil;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

public abstract class AbstractMockTerminalConnectionFactory implements TerminalConnectionFactory {

	private List<String> files = null;
	private String root;
	private List<TerminalSnapshot> snapshots = null;
	private String trailName;
	private boolean verifySend;

	/**
	 * Loads all snapshots from the listed files NOTE: Currently All files are re-load from disk on every get connection, since
	 * snapshots terminal fields gets "dirty" by usage. Future implementation should probably clone the snapshots in-memory
	 * 
	 */
	protected List<TerminalSnapshot> fetchSnapshots() {
		if (snapshots != null) {
			return snapshots;
		}

		if (trailName != null && files != null) {
			throw (new UnableToLoadSnapshotException("Can't define both trail and files for mock terminal connection factory"));
		}

		snapshots = new ArrayList<TerminalSnapshot>();

		if (trailName != null) {
			loadSnapshotsFromTrailFile();
		} else {
			loadSnapshotsFromFiles();
		}

		return snapshots;
	}

	private void loadSnapshotsFromTrailFile() {
		TerminalPersistedTrail trail;
		try {
			String trailClasspath = MessageFormat.format("{0}/{1}", root, trailName);
			InputStream trailStream = getClass().getResourceAsStream(trailClasspath);
			Assert.notNull(trailStream, String.format(
					"Trail file %s not found. In development, Verify it exist in a src/main/resources%s", trailName, root));
			trail = XmlSerializationUtil.deserialize(TerminalPersistedTrail.class, trailStream);
		} catch (JAXBException e) {
			throw (new IllegalArgumentException(MessageFormat.format("Faild reading XML trail:{0}", trailName), e));
		}

		List<TerminalSnapshot> snapshotsList = trail.getSnapshots();
		for (TerminalSnapshot snapshot : snapshotsList) {
			// if verify send wasn't specified, don't add outgoing snapshots from trail file
			// NOTE: this logic is not activated in loadSnapshotsFromFiles, since the files names are specified
			if (snapshot.getSnapshotType() == SnapshotType.INCOMING || verifySend) {
				snapshots.add(snapshot);
			}
		}
	}

	private void loadSnapshotsFromFiles() {
		for (String resourceName : files) {
			try {
				TerminalPersistedSnapshot persistedSnapshot = XmlSerializationUtil.deserialize(TerminalPersistedSnapshot.class,
						getClass().getResourceAsStream(MessageFormat.format("{0}/{1}", root, resourceName)));
				snapshots.add(persistedSnapshot);
			} catch (Exception e) {
				throw (new IllegalArgumentException(MessageFormat.format("Faild reading XML trail:{0}", resourceName), e));
			}
		}
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public void setFiles(List<String> files) {
		this.files = files;
	}

	public void setTrailName(String trailName) {
		this.trailName = trailName;
	}

	public void setVerifySend(boolean verifySend) {
		this.verifySend = verifySend;
	}
}
