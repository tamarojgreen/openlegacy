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
package org.openlegacy.layout;

import org.openlegacy.EntityDefinition;
import org.openlegacy.definitions.FieldDefinition;

/**
 * An interface for page builder, which creates a page layout model from a given entity definition. Used for creating layout for
 * screen entity to a nice layout page. Done in designtime, but can be used for runtime layout generation as well (via JSTL,
 * Freemarker, etc)
 * 
 * @author Roi Mor
 * 
 * @param <D>
 *            The entity definition concrete type
 * @param <F>
 *            The entity field definition concrete type
 */
public interface PageBuilder<D extends EntityDefinition<F>, F extends FieldDefinition> {

	PageDefinition build(D entityDefinition);
}
