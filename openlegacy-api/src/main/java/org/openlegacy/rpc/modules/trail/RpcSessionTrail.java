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
package org.openlegacy.rpc.modules.trail;

import org.openlegacy.modules.trail.SessionTrail;
import org.openlegacy.rpc.RpcSnapshot;

/**
 * Defines an rpc session trail for a rpc session . Compound of {@link RpcSnapshot}
 * 
 * @author Roi Mor
 * 
 */
public interface RpcSessionTrail extends SessionTrail<RpcSnapshot> {

}
