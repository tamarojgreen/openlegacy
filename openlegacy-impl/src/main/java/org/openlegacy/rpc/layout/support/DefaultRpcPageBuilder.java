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
package org.openlegacy.rpc.layout.support;

import org.openlegacy.definitions.page.support.SimplePageDefinition;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.rpc.definitions.RpcEntityDefinition;
import org.openlegacy.rpc.layout.RpcPageBuilder;

public class DefaultRpcPageBuilder implements RpcPageBuilder {

	public PageDefinition build(RpcEntityDefinition entityDefinition) {
		return new SimplePageDefinition(entityDefinition);
	}

}
