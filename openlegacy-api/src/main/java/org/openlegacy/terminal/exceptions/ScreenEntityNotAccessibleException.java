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
package org.openlegacy.terminal.exceptions;

import org.openlegacy.exceptions.EntityNotAccessibleException;

/**
 * This exception is typically thrown when a session is unable to access the requested screen entity
 * 
 * @author Roi Mor
 * 
 */
public class ScreenEntityNotAccessibleException extends EntityNotAccessibleException {

	private static final long serialVersionUID = 1L;
	private String targetEntity;

	public ScreenEntityNotAccessibleException(Exception e, String targetEntity) {
		super(e);
		this.targetEntity = targetEntity;
	}

	public ScreenEntityNotAccessibleException(String s, String targetEntity) {
		super(s);
		this.targetEntity = targetEntity;
	}

	public String getTargetEntity() {
		return targetEntity;
	}

}
