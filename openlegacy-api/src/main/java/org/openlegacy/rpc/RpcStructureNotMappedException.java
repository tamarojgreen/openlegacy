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

import org.openlegacy.exceptions.EntityNotAccessibleException;

public class RpcStructureNotMappedException extends EntityNotAccessibleException {

	private static final long serialVersionUID = 1L;

	public RpcStructureNotMappedException(Exception e) {
		super(e);
	}

	public RpcStructureNotMappedException(String s) {
		super(s);

	}

}
