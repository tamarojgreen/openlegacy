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
package org.openlegacy.terminal.render;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.persistance.SnapshotPersistanceDTO;
import org.openlegacy.terminal.persistance.TerminalPersistedSnapshot;
import org.openlegacy.utils.XmlSerializationUtil;

import java.io.OutputStream;

import javax.xml.bind.JAXBException;

public class DefaultTerminalSnapshotXmlRenderer implements TerminalSnapshotXmlRenderer {

	@Override
	public void render(TerminalSnapshot terminalSnapshot, OutputStream outputStream) {
		TerminalPersistedSnapshot persistedSnapshot = null;
		if (terminalSnapshot instanceof TerminalPersistedSnapshot) {
			persistedSnapshot = (TerminalPersistedSnapshot)terminalSnapshot;
		} else {
			persistedSnapshot = SnapshotPersistanceDTO.transformSnapshot(terminalSnapshot);
		}
		try {
			XmlSerializationUtil.serialize(TerminalPersistedSnapshot.class, persistedSnapshot, outputStream);
		} catch (JAXBException e) {
			throw (new OpenLegacyRuntimeException(e));
		}
	}

	@Override
	public String getFileFormat() {
		return "xml";
	}

}
