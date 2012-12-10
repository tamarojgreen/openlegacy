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
package org.openlegacy.designtime;

public class DesigntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public DesigntimeException(Exception e) {
		super(e);
	}

	public DesigntimeException(String message, Exception e) {
		super(message, e);
	}

	public DesigntimeException(String message) {
		super(message);
	}

}
