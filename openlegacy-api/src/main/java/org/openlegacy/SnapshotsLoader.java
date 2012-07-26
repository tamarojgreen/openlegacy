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
package org.openlegacy;

import org.openlegacy.exceptions.UnableToLoadSnapshotException;

import java.util.List;

/**
 * A testing and mock-up mostly interface designed to design a snapshot loader single/multiple snapshot from file/s
 * 
 * @author Roi Mor
 */
public interface SnapshotsLoader<S extends Snapshot> {

	/**
	 * Load snapshots from the specified root. If no fileNames are specified, all files are loaded
	 * 
	 * @throws UnableToLoadSnapshotException
	 */
	List<S> loadSnapshots(String root, String... fileNames) throws UnableToLoadSnapshotException;

	S load(String path) throws UnableToLoadSnapshotException;
}
