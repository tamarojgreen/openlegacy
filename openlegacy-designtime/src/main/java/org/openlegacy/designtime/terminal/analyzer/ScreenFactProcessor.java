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
package org.openlegacy.designtime.terminal.analyzer;

import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;

/**
 * A fact processor process a fact found the screen analyzer rules, and apply the fact into the given screen entity definitions
 * 
 */
public interface ScreenFactProcessor {

	boolean accept(ScreenFact screenFact);

	void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact);
}
