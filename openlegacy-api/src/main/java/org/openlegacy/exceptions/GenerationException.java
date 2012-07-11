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
package org.openlegacy.exceptions;

/**
 * This exception is thrown when unable to load a persisted snapshot
 * 
 */
public class GenerationException extends OpenLegacyRuntimeException {

	private static final long serialVersionUID = 1L;

	public GenerationException(String s) {
		super(s);
	}

	public GenerationException(String s, Exception e) {
		super(s, e);
	}

	public GenerationException(Exception e) {
		super(e);
	}
}
