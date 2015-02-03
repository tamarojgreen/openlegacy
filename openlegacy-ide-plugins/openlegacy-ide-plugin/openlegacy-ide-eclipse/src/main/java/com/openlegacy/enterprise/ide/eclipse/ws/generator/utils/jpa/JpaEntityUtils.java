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

package com.openlegacy.enterprise.ide.eclipse.ws.generator.utils.jpa;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractNamedModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.jpa.JpaBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.jpa.JpaByteFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.jpa.JpaDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.jpa.JpaFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.jpa.JpaIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.jpa.JpaListFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.jpa.JpaManyToOneFieldModel;

import org.openlegacy.db.definitions.DbFieldDefinition;
import org.openlegacy.db.definitions.SimpleDbColumnFieldDefinition;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Ivan Bort
 * 
 */
public class JpaEntityUtils {

	public static List<JpaFieldModel> getColumns(Map<String, DbFieldDefinition> columnFieldsDefinitions, AbstractNamedModel parent) {

		List<JpaFieldModel> list = new ArrayList<JpaFieldModel>();

		for (DbFieldDefinition definition : columnFieldsDefinitions.values()) {
			JpaFieldModel model = null;
			SimpleDbColumnFieldDefinition fieldDefinition = (SimpleDbColumnFieldDefinition)definition;
			// skip static fields
			if (fieldDefinition.isStaticField()) {
				continue;
			}
			String javaTypeName = fieldDefinition.getJavaTypeName();
			if (javaTypeName.equalsIgnoreCase(Boolean.class.getSimpleName())) {
				model = new JpaBooleanFieldModel(definition, parent);
			} else if (javaTypeName.equalsIgnoreCase("byte[]")) {
				model = new JpaByteFieldModel(definition, parent);
			} else if (javaTypeName.equalsIgnoreCase(Date.class.getSimpleName())) {
				model = new JpaDateFieldModel(definition, parent);
			} else if (javaTypeName.equalsIgnoreCase(Integer.class.getSimpleName())) {
				model = new JpaIntegerFieldModel(definition, parent);
			} else if (javaTypeName.equalsIgnoreCase(List.class.getSimpleName())) {
				model = new JpaListFieldModel(definition, parent);
			} else if (fieldDefinition.getManyToOneDefinition() != null) {
				model = new JpaManyToOneFieldModel(definition, parent);
			} else {
				model = new JpaFieldModel(definition, parent);
			}

			if (model != null) {
				list.add(model);
			}
		}
		return list;
	}

}
