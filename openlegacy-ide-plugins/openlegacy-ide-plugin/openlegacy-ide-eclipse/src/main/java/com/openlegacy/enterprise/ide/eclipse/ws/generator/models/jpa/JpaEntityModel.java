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

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractEntityModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.utils.jpa.JpaEntityUtils;

import org.eclipse.core.resources.IFile;
import org.openlegacy.db.definitions.DbFieldDefinition;
import org.openlegacy.designtime.db.generators.support.CodeBasedDbEntityDefinition;

import java.util.Map;

/**
 * @author Ivan Bort
 * 
 */
public class JpaEntityModel extends AbstractEntityModel {

	private CodeBasedDbEntityDefinition definition;

	public JpaEntityModel(CodeBasedDbEntityDefinition definition, IFile resourceFile) {
		super(definition.getEntityName(), resourceFile);
		this.definition = definition;

		// populate children
		Map<String, DbFieldDefinition> columnFieldsDefinitions = definition.getColumnFieldsDefinitions();
		if (columnFieldsDefinitions != null && !columnFieldsDefinitions.isEmpty()) {
			children.addAll(JpaEntityUtils.getColumns(columnFieldsDefinitions, this));
		}
	}

	public CodeBasedDbEntityDefinition getDefinition() {
		return definition;
	}

}
