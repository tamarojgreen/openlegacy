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

package org.openlegacy.mvc.remoting.services;

import org.openlegacy.modules.table.drilldown.DrilldownAction;
import org.openlegacy.terminal.TerminalSession;

/**
 * @author Imivan
 * 
 */
public interface OLTerminalSession extends TerminalSession {

	public <T> void doTableDrillDown(Class<T> targetClass, DrilldownAction<?> drilldownAction, Object... rowKeys);
}
