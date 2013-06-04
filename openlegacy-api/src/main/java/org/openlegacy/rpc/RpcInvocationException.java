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
package org.openlegacy.rpc;

import org.openlegacy.exceptions.EntityNotAccessibleException;

/**
 * An exception indicating a problem with a terminal action execution
 * 
 * @author Roi Mor
 * 
 */
public class RpcInvocationException extends EntityNotAccessibleException {

	private static final long serialVersionUID = 1L;

	public RpcInvocationException(Exception e) {
		super(e);
	}

	public RpcInvocationException(String s) {
		super(s);
	}

}
