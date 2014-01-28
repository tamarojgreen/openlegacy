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
package org.openlegacy.support;

import org.openlegacy.SessionProperties;
import org.openlegacy.SessionPropertiesProvider;
import org.openlegacy.support.SimpleSessionProperties;

import java.io.Serializable;

public class SimpleSessionPropertiesProvider implements SessionPropertiesProvider, Serializable {

	private static final long serialVersionUID = 1L;

	public SessionProperties getSessionProperties() {
		return new SimpleSessionProperties();
	}

}
