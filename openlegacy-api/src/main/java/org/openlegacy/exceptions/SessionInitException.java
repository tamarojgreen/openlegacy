/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.exceptions;

/**
 * @author Ivan Bort
 */
public class SessionInitException extends OpenLegacyException {

	private static final long serialVersionUID = 8163361579182080334L;

	public SessionInitException(Exception e) {
		super(e);
	}

	public SessionInitException(String s, Exception e) {
		super(s, e);
	}

	public SessionInitException(String s) {
		super(s);
	}

}
