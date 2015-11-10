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
package org.openlegacy.rpc;

import org.openlegacy.definitions.RpcActionDefinition;

import java.util.Collections;
import java.util.List;

public interface RpcEntity {

	List<RpcActionDefinition> getActions();

	public class NONE implements RpcEntity {

		@SuppressWarnings("unchecked")
		@Override
		public List<RpcActionDefinition> getActions() {
			return Collections.EMPTY_LIST;
		}

	}

}
