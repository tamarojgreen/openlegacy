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
package org.openlegacy.terminal.tools;

import org.openlegacy.terminal.TerminalSnapshot;

public class TerminalSessionSnapshotsTextDumper implements TerminalSnapshotDumper {

	@Override
	public byte[] getDumpContent(TerminalSnapshot snapshot) {
		return snapshot.toString().getBytes();
	}

	@Override
	public String getDumpFileExtension() {
		return "txt";
	}

}
