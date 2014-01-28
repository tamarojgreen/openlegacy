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
package org.openlegacy.exceptions;

/**
 * This exception is typically thrown when a given entity is not found in the registry
 * 
 */
public class EntityNotFoundException extends OpenLegacyRuntimeException {

	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(String s) {
		super(s);
	}

	public EntityNotFoundException(Exception e) {
		super(e);
	}
}
