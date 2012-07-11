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
package org.openlegacy.modules.table.drilldown;

/**
 * A table stop conditions definition. Determine if to stop based on the given entity, or by comparing the entity before to the
 * entity after the scroll
 * 
 */
public interface TableScrollStopConditions<T> {

	boolean shouldStop(T entity);

	boolean shouldStop(T beforeScrollEntity, T afterScrollEntity);
}
