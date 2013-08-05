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
package org.openlegacy.terminal.render;

import java.io.OutputStream;

/**
 * An RPC renderer interface for rendering into an image. Defined as an empty interface for easier access from Spring .
 * 
 * @author Roi Mor
 * 
 */
public interface RpcImageRenderer {

	void render(String source, OutputStream output);

}
