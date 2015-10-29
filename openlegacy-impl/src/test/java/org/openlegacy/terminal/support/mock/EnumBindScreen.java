/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.terminal.support.mock;

import org.openlegacy.annotations.screen.Identifier;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.annotations.screen.ScreenField;
import org.openlegacy.annotations.screen.ScreenIdentifiers;
import org.openlegacy.definitions.EnumGetValue;
import org.openlegacy.terminal.definitions.TerminalActionDefinition;

import java.util.Collections;
import java.util.List;

/**
 * @author Ivan Bort
 */
@ScreenEntity
@ScreenIdentifiers(identifiers = { @Identifier(row = 1, column = 30, value = "Enum bind title") })
public class EnumBindScreen implements org.openlegacy.terminal.ScreenEntity {

	@ScreenField(row = 3, column = 33, endColumn = 35, labelColumn = 2, editable = true, displayName = "Stock Group",
			sampleValue = "SG", helpText = "SG=Standard group CG=Custom Group")
	private StockGroup stockGroup;

	public StockGroup getStockGroup() {
		return stockGroup;
	}

	public void setStockGroup(StockGroup stockGroup) {
		this.stockGroup = stockGroup;
	}

	@Override
	public String getFocusField() {
		return null;
	}

	@Override
	public void setFocusField(String focusField) {}

	@Override
	public List<TerminalActionDefinition> getActions() {
		return Collections.emptyList();
	}

	public enum StockGroup implements EnumGetValue {
		Custom("CG", "Custom"),
		Standard("SG", "Standard");
		private String value;
		private String display;

		StockGroup(String value, String display) {
			this.value = value;
			this.display = display;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return display;
		}
	}

}
