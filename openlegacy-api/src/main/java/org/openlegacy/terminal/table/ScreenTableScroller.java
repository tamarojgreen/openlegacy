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
package org.openlegacy.terminal.table;

import org.openlegacy.modules.table.drilldown.TableScroller;
import org.openlegacy.terminal.TerminalSession;

/**
 * Defines a table scroller on a terminal session and a given screen entity
 * 
 * @param <T>
 *            The screen entity class
 */
public interface ScreenTableScroller<T> extends TableScroller<TerminalSession, T> {

}
