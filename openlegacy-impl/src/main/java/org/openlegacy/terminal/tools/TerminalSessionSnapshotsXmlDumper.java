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

import org.openlegacy.exceptions.OpenLegacyRuntimeException;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.persistance.SnapshotPersistanceDTO;
import org.openlegacy.terminal.persistance.TerminalPersistedSnapshot;
import org.openlegacy.utils.XmlSerializationUtil;

import java.io.ByteArrayOutputStream;

import javax.xml.bind.JAXBException;

public class TerminalSessionSnapshotsXmlDumper implements TerminalSnapshotDumper {

	@Override
	public byte[] getDumpContent(TerminalSnapshot snapshot) {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		TerminalPersistedSnapshot persistedSnapshot = SnapshotPersistanceDTO.transformIncomingSnapshot(snapshot);
		try {
			XmlSerializationUtil.serialize(TerminalPersistedSnapshot.class, persistedSnapshot, baos);
		} catch (JAXBException e) {
			throw (new OpenLegacyRuntimeException(e));
		}
		return baos.toByteArray();
	}

	@Override
	public String getDumpFileExtension() {
		return "xml";
	}
}
