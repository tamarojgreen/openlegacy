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

import java.util.List;

public interface ParseResults {

	List<String> getErrors();

	List<String> getWarnings();

	RpcEntityDefinition getEntityDefinition();

	String getEntityName(String defaultName);

}
