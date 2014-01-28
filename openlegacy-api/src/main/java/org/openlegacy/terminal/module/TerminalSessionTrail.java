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
package org.openlegacy.terminal.module;

import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.terminal.TerminalSnapshot;

/**
 * Defines a terminal session trail for a terminal session . Compound of {@link TerminalSnapshot}
 * 
 * @author Roi Mor
 * 
 */
public interface TerminalSessionTrail extends SessionTrail<TerminalSnapshot> {

}
