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
package org.openlegacy.modules.table;

import org.openlegacy.Session;

import java.util.List;

/**
 * Define a table collector over multiple entities
 * 
 * @param <S>
 *            The session type
 * @param <T>
 *            The row class type
 */
public interface TableCollector<S extends Session, T> {

	public List<T> collectAll(S session, Class<?> entityClass, Class<T> rowClass);

	public List<T> collectOne(S session, Class<?> entityClass, Class<T> rowClass);
}
