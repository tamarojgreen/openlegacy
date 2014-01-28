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
package org.openlegacy.rpc.exceptions;

import org.openlegacy.exceptions.EntityNotAccessibleException;

/**
 * An exception indicating a problem with a rpc action execution
 * 
 * @author Ivan Bort
 * 
 */
public class RpcActionException extends EntityNotAccessibleException {

	private static final long serialVersionUID = 1L;

	public RpcActionException(Exception e) {
		super(e);
	}

	public RpcActionException(String s) {
		super(s);
	}

}
