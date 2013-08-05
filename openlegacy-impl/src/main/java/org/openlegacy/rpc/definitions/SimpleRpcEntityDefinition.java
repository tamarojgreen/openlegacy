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
package org.openlegacy.rpc.definitions;

import org.openlegacy.annotations.rpc.Languages;
import org.openlegacy.definitions.support.AbstractEntityDefinition;

public class SimpleRpcEntityDefinition extends AbstractEntityDefinition<RpcFieldDefinition> implements RpcEntityDefinition {

	private Languages language;

	public SimpleRpcEntityDefinition() {
		super(null, null);
	}

	public SimpleRpcEntityDefinition(String entityName, Class<?> entityClass) {
		super(entityName, entityClass);
	}

	public Languages getLanguage() {
		return language;
	}

	public void setLanguage(Languages language) {
		this.language = language;
	}

}
