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

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.render.TerminalSnapshotRenderer;

public class TerminalSessionSnapshotsImageDumper implements TerminalSnapshotDumper {

	private TerminalSnapshotRenderer imageRenderer;

	public byte[] getDumpContent(TerminalSnapshot snapshot) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		imageRenderer.render(snapshot, baos);
		return baos.toByteArray();
	}

	public String getDumpFileExtension() {
		return "jpg";
	}

	public void setImageRenderer(TerminalSnapshotRenderer imageRenderer) {
		this.imageRenderer = imageRenderer;
	}
}
