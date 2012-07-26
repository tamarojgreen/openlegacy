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
package org.openlegacy.modules.trail;

import org.openlegacy.Snapshot;

import java.io.OutputStream;

/**
 * Defines a session trail writer. Write the given session trail to the given output stream. Default implementations are in XML,
 * but can be saved in other formats.
 * 
 * @author Roi Mor
 */
public interface TrailWriter {

	void write(SessionTrail<? extends Snapshot> trail, OutputStream out);
}
