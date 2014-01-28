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

import org.openlegacy.terminal.TerminalSnapshot;

import java.io.OutputStream;

/**
 * Defines a renderer interface for rendering a {@link TerminalSnapshot} to some {@link OutputStream} Known Implementations are
 * text, image, XML.
 * 
 * @author Roi Mor
 * 
 */
public interface TerminalSnapshotRenderer {

	void render(TerminalSnapshot terminalSnapshot, OutputStream outputStream);

	String getFileFormat();
}