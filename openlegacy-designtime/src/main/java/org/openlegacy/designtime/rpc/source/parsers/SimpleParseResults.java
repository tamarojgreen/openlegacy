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
package org.openlegacy.designtime.rpc.source.parsers;

import org.openlegacy.rpc.definitions.RpcEntityDefinition;

import java.util.ArrayList;
import java.util.List;

public class SimpleParseResults implements ParseResults {

	private List<String> errors = new ArrayList<String>();
	private List<String> warnings = new ArrayList<String>();
	private RpcEntityDefinition entityDefinition;

	public SimpleParseResults(RpcEntityDefinition entityDefinition) {
		this.entityDefinition = entityDefinition;
	}

	@Override
	public List<String> getErrors() {
		return errors;
	}

	@Override
	public List<String> getWarnings() {
		return warnings;
	}

	@Override
	public RpcEntityDefinition getEntityDefinition() {
		return entityDefinition;
	}

}
