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
package org.openlegacy.designtime.rpc.generators;

import org.openlegacy.designtime.generators.PojoCodeModel;
import org.openlegacy.designtime.rpc.generators.support.DefaultRpcPojoCodeModel.Field;

import java.util.Collection;

/**
 * An interface which model the code model of screen classes annotation with @ScreenEntity, @ScreenPart, @ScreenTable
 * 
 * 
 */
public interface RpcPojoCodeModel extends PojoCodeModel {

	Collection<Field> getFields();
}
