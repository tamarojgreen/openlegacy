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
package org.openlegacy.definitions.page.support;

import org.openlegacy.definitions.FieldDefinition;
import org.openlegacy.layout.PagePartRowDefinition;

import java.util.ArrayList;
import java.util.List;

public class SimplePagePartRowDefinition implements PagePartRowDefinition {

	private List<FieldDefinition> fields = new ArrayList<FieldDefinition>();

	@Override
	public List<FieldDefinition> getFields() {
		return fields;
	}

}
