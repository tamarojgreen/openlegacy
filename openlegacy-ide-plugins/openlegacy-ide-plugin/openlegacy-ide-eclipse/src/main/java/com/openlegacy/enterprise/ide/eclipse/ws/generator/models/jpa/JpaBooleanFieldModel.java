/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package com.openlegacy.enterprise.ide.eclipse.ws.generator.models.jpa;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractNamedModel;

import org.openlegacy.db.definitions.DbFieldDefinition;

/**
 * @author Ivan Bort
 * 
 */
public class JpaBooleanFieldModel extends JpaFieldModel {

	public JpaBooleanFieldModel(DbFieldDefinition definition, AbstractNamedModel parent) {
		super(definition, parent);
	}

}
