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
package org.openlegacy.designtime;

import org.openlegacy.EntityDefinition;

/**
 * Interface for communicate with the user interface during code generation of all entities, which allows user to customize the
 * generated code
 * 
 * @author Roi Mor
 * 
 */
public interface EntityUserInteraction<E extends EntityDefinition<?>> extends UserInteraction {

	boolean customizeEntity(E entityDefinition, GenerateModelRequest generateModelRequest);

}
