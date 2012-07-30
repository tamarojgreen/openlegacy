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
package org.openlegacy.terminal.exceptions;

import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.exceptions.EntityNotFoundException;

/**
 * This exception is thrown when the request screen entity class is not found in the registry. May happen if component scan is not
 * defined properly or the class is not marked with {@link ScreenEntity} annotation
 * 
 * @author Roi Mor
 */
public class ScreenEntityNotFoundException extends EntityNotFoundException {

	private static final long serialVersionUID = 1L;

	public ScreenEntityNotFoundException(Exception e) {
		super(e);
	}

	public ScreenEntityNotFoundException(String s) {
		super(s);
	}

}
