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
package org.openlegacy.designtime.rpc.generators.support;

import org.openlegacy.annotations.rpc.RpcEntity;
import org.openlegacy.annotations.rpc.RpcEntitySuperClass;
import org.openlegacy.annotations.rpc.RpcField;
import org.openlegacy.annotations.rpc.RpcPart;

public class RpcAnnotationConstants {

	public static final String RPC_ENTITY_ANNOTATION = RpcEntity.class.getSimpleName();
	public static final String RPC_ENTITY_SUPER_CLASS_ANNOTATION = RpcEntitySuperClass.class.getSimpleName();
	public static final String RPC_PART_ANNOTATION = RpcPart.class.getSimpleName();
	public static final String DISPLAY_NAME = "displayName";
	public static final String NAME = "name";
	public static final String RPC_TYPE = "rpcType";
	public static final String RPC_FIELD_ANNOTATION = RpcField.class.getSimpleName();
	public static final String RPC_BOOLEAN_FIELD_ANNOTATION = null;
}
