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
import org.openlegacy.terminal.definitions.FieldAssignDefinition;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

public class FieldValuesFact implements ScreenFact {

	private FieldAssignDefinition fieldAssignDefinition;
	private ScreenEntityDefinition lookupWindowScreenDefinition;

	public FieldValuesFact(FieldAssignDefinition fieldAssignDefinition, ScreenEntityDefinition lookupWindowScreenDefinition) {
		this.fieldAssignDefinition = fieldAssignDefinition;
		this.lookupWindowScreenDefinition = lookupWindowScreenDefinition;
	}

	public FieldAssignDefinition getFieldAssignDefinition() {
		return fieldAssignDefinition;
	}

	public ScreenEntityDefinition getLookupWindowScreenDefinition() {
		return lookupWindowScreenDefinition;
	}
}
