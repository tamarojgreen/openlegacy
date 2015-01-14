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
package org.openlegacy;

import java.util.Collection;
import java.util.Set;

import org.openlegacy.Snapshot;
import org.openlegacy.terminal.SnapshotPickerStrategy;

public interface SnapshotsOrganizer<S extends Snapshot> {

	void add(Collection<S> snapshots);

	Collection<Set<S>> getGroups();

	Collection<S> getGroupsRepresenters(SnapshotPickerStrategy<S> snapshotPickerStrategy);

	void clear();

}
