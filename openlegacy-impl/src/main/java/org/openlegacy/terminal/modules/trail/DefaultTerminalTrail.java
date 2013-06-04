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
package org.openlegacy.terminal.modules.trail;

import org.openlegacy.modules.support.trail.AbstractSessionTrail;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.module.TerminalSessionTrail;

import java.io.Serializable;

public class DefaultTerminalTrail extends AbstractSessionTrail<TerminalSnapshot> implements TerminalSessionTrail, Serializable {

	private static final long serialVersionUID = 1L;
}
