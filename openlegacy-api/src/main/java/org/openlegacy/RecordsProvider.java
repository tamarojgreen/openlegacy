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

import java.util.Map;

/**
 * Define a provider interface for structured records provided from a session. Records are classes of certain type. A record is
 * commonly used for table row structure.
 * 
 * @author Roi Mor
 * 
 * @param <S>
 *            The type of session
 * @param <T>
 *            The record type
 */
public interface RecordsProvider<S extends Session, T> {

	Map<Object, T> getRecords(S session, Class<?> entityClass, Class<T> rowClass, boolean collectAll, String searchText);

	DisplayItem toDisplayItem(T record);
}
