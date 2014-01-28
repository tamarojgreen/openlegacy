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
package org.openlegacy.terminal.table;

import org.openlegacy.modules.table.TableCollector;
import org.openlegacy.terminal.TerminalSession;

/**
 * Define a collector for tables based on multiple screen entities
 * 
 * @author IL505100
 */
public interface ScreenTableCollector<T> extends TableCollector<TerminalSession, T> {

}
