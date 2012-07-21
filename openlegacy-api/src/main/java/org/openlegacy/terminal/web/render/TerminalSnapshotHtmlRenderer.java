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
package org.openlegacy.terminal.web.render;

import org.openlegacy.terminal.TerminalSnapshot;

/**
 * An interface for HTML rendering of a {@link TerminalSnapshot}
 * 
 * @author Roi Mor
 * 
 */
public interface TerminalSnapshotHtmlRenderer {

	String render(TerminalSnapshot terminalSnapshot);
}
