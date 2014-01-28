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
package org.openlegacy.designtime.analyzer;

import org.openlegacy.EntityDefinition;
import org.openlegacy.Snapshot;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalSnapshot;

import java.util.Collection;
import java.util.Map;

public interface SnapshotsAnalyzerContext<S extends Snapshot, D extends EntityDefinition<?>> {

	Collection<S> getActiveSnapshots();

	void setActiveSnapshots(Collection<S> snapshots);

	void addEntityDefinition(String desiredEntityName, ScreenEntityDesigntimeDefinition screenEntityDefinition);

	void finalizeEntitiesDefinitions();

	Map<String, D> getEntitiesDefinitions();

	Collection<TerminalSnapshot> getAccessedFromSnapshots(Collection<TerminalSnapshot> incomingSnapshots);

	TerminalSnapshot getOutgoingSnapshot(TerminalSnapshot incomingSnapshot);
}
