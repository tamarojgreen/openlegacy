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
package org.openlegacy;

import org.openlegacy.exceptions.OpenLegacyException;

/**
 * A provider for session properties. Provider is typically aware of the running environment (web, etc) and can provide
 * information such as IP, user, etc
 * 
 * @author Roi Mor
 * 
 */
public interface SessionPropertiesProvider {

	SessionProperties getSessionProperties() throws OpenLegacyException;
}
