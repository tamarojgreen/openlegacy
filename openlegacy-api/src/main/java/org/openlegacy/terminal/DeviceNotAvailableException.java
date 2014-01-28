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
package org.openlegacy.terminal;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;

public class DeviceNotAvailableException extends OpenLegacyRuntimeException {

	private static final long serialVersionUID = 1L;

	public DeviceNotAvailableException(String s) {
		super(s);
	}

	public DeviceNotAvailableException(Exception e) {
		super(e);
	}

}
