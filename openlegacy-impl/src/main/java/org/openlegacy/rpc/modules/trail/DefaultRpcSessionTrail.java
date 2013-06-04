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

import org.openlegacy.modules.support.trail.AbstractSessionTrail;
import org.openlegacy.rpc.RpcSnapshot;

import java.io.Serializable;

public class DefaultRpcSessionTrail extends AbstractSessionTrail<RpcSnapshot> implements RpcSessionTrail, Serializable {

	private static final long serialVersionUID = 1L;
}
