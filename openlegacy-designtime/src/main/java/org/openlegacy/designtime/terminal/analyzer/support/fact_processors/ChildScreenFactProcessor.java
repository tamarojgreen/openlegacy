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
package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.designtime.terminal.model.support.SimpleScreenEntityDesigntimeDefinition;

public class ChildScreenFactProcessor implements ScreenFactProcessor {

	public boolean accept(ScreenFact screenFact) {
		return screenFact instanceof ChildScreenFact;
	}

	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {
		ChildScreenFact childScreenFact = (ChildScreenFact)screenFact;

		SimpleScreenEntityDesigntimeDefinition childScreenEntityDefinition = (SimpleScreenEntityDesigntimeDefinition)childScreenFact.getChildScreenEntityDefinition();
		childScreenEntityDefinition.setChild(true);
		screenEntityDefinition.getChildEntitiesDefinitions().add(childScreenEntityDefinition);
	}

}
