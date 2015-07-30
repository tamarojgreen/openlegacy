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
package org.openlegacy.rpc.definitions;

import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.PartEntityDefinition;

import java.util.List;
import java.util.Map;

public interface RpcPartEntityDefinition extends PartEntityDefinition<RpcFieldDefinition> {

	int getOrder();

	int getCount();

	Map<String, RpcPartEntityDefinition> getInnerPartsDefinitions();

	String getRuntimeName();

	List<ActionDefinition> getActions();

	List<RpcFieldDefinition> getKeys();

	String getHelpText();

	String getLegacyContainerName();

	Boolean isVirtual();

	public String getPartFullName();

	String[] getExpandedElements();

	String getOriginalName();

	String getOriginalNameForList();

}
