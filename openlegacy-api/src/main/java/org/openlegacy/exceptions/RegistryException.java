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
 * Indicates a problem in the registry definition. e.g: 2 entities/fields with the same name, etc
 * 
 */
public class RegistryException extends OpenLegacyRuntimeException {

	private static final long serialVersionUID = 1L;

	public RegistryException(Exception e) {
		super(e);
	}

	public RegistryException(String s) {
		super(s);
	}

	public RegistryException(String s, Exception e) {
		super(s, e);
	}
}
