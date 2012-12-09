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
package org.openlegacy;

import java.io.Serializable;
import java.util.Date;
import java.util.Properties;

/**
 * A container for all session properties. Common usage: IP, device
 * 
 * @author Roi Mor
 * 
 */
public interface SessionProperties extends Serializable {

	String getId();

	Date getStartedOn();

	Date getLastActivity();

	Object getProperty(String propertyName);

	Properties getProperties();
}
