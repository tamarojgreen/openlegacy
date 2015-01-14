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
package org.openlegacy.support;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.inject.Inject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openlegacy.Snapshot;
import org.openlegacy.SnapshotsOrganizer;
import org.openlegacy.SnapshotsSimilarityChecker;
import org.openlegacy.terminal.SnapshotPickerStrategy;

public abstract class AbstractSnapshotsOrganizer<S extends Snapshot> implements SnapshotsOrganizer<S> {

	@Inject
	private SnapshotsSimilarityChecker<S> snapshotsSimilarityChecker;

	private Collection<Set<S>> snapshotGroups = new ArrayList<Set<S>>();

	private int matchingPercent = 99;

	private final static Log logger = LogFactory.getLog(AbstractSnapshotsOrganizer.class);

	@Override
	public Collection<Set<S>> getGroups() {
		return Collections.unmodifiableCollection(snapshotGroups);
	}

	@Override
	public Collection<S> getGroupsRepresenters(SnapshotPickerStrategy<S> snapshotPickerStrategy) {
		Collection<Set<S>> groups = getGroups();
		List<S> result = new ArrayList<S>();
		for (Set<S> group : groups) {
			S snapshot = snapshotPickerStrategy.pickRepresenter(group);
			if (snapshot != null) {
				result.add(snapshot);
			}
		}
		return result;
	}

	@Override
	public void add(Collection<S> snapshots) {
		for (S snapshot : snapshots) {
			addSnapshotToGroup(snapshot);
		}

	}

	private void addSnapshotToGroup(S snapshot) {
		Collection<Set<S>> groups = getGroups();
		boolean found = false;
		for (Set<S> group : groups) {
			S groupFirstSnapshot = group.iterator().next();
			int snapshotsSimilarityPercent = snapshotsSimilarityChecker.similarityPercent(snapshot, groupFirstSnapshot);
			if (snapshotsSimilarityPercent > matchingPercent) {
				if (logger.isTraceEnabled() && snapshotsSimilarityPercent < 100) {
					logger.trace("\n*************************************************************************************");
					logger.trace("Matched snapshot to group by " + snapshotsSimilarityPercent);
					logger.trace("Snapshot:\n" + snapshot);
					logger.trace("Group representive snapshot:\n" + groupFirstSnapshot);
				}
				group.add(snapshot);
				found = true;
			}
		}
		if (!found) {
			Set<S> group = new HashSet<S>();
			group.add(snapshot);
			snapshotGroups.add(group);
		}
	}

	public void setMatchingPercent(int matchingPercent) {
		this.matchingPercent = matchingPercent;
	}

	@Override
	public void clear() {
		snapshotGroups.clear();
	}
}
