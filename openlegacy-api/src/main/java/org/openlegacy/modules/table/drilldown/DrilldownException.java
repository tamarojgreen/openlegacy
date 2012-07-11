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
package org.openlegacy.modules.table.drilldown;

import org.openlegacy.exceptions.OpenLegacyRuntimeException;

/**
 * This exception is thrown typically when unable to drill-down a table in a session
 * 
 */
public class DrilldownException extends OpenLegacyRuntimeException {

	private static final long serialVersionUID = 1L;

	public DrilldownException(String s) {
		super(s);
	}
}
