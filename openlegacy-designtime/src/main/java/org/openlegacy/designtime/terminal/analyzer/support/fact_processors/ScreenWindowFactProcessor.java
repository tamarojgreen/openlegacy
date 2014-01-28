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
package org.openlegacy.designtime.terminal.analyzer.support.fact_processors;

import org.openlegacy.designtime.terminal.analyzer.ScreenFact;
import org.openlegacy.designtime.terminal.analyzer.ScreenFactProcessor;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalRectangle;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.terminal.support.SimpleTerminalRectangle;

import java.util.List;

public class ScreenWindowFactProcessor implements ScreenFactProcessor {

	public boolean accept(ScreenFact screenFact) {
		return screenFact instanceof ScreenWindowFact;
	}

	public void process(ScreenEntityDesigntimeDefinition screenEntityDefinition, ScreenFact screenFact) {
		ScreenWindowFact screenWindowFact = (ScreenWindowFact)screenFact;
		TerminalField buttomBorderField = screenWindowFact.getButtomBorderField();
		TerminalField topBorderField = screenWindowFact.getTopBorderField();

		TerminalPosition buttomLeftPosition = new SimpleTerminalPosition(buttomBorderField.getPosition().getRow(),
				buttomBorderField.getPosition().getColumn() + buttomBorderField.getLength());
		TerminalRectangle borders = new SimpleTerminalRectangle(topBorderField.getPosition(), buttomLeftPosition);
		screenEntityDefinition.setSnapshotBorders(borders);

		// remove the fields from the snapshot to avoid re-recognize by other rules & endless loop
		List<TerminalField> fields = screenEntityDefinition.getSnapshot().getFields();
		fields.remove(topBorderField);
		fields.remove(buttomBorderField);

	}

}
