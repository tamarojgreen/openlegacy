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

package com.openlegacy.enterprise.ide.eclipse.editors.models.jpa;

import org.apache.commons.lang.StringUtils;
import org.openlegacy.annotations.db.DbNavigation;
import org.openlegacy.db.definitions.DbNavigationDefinition;
import org.openlegacy.designtime.db.generators.support.CodeBasedDbEntityDefinition;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class JpaNavigationModel extends JpaNamedObject {

	// annotation attributes
	private String category = "";

	public JpaNavigationModel() {
		super(DbNavigation.class.getSimpleName());
	}

	public JpaNavigationModel(UUID uuid) {
		super(DbNavigation.class.getSimpleName());
		this.uuid = uuid;
	}

	@Override
	public void init(CodeBasedDbEntityDefinition dbEntityDefinition) {
		DbNavigationDefinition navigationDefinition = dbEntityDefinition.getNavigationDefinition();
		if (navigationDefinition == null) {
			return;
		}
		category = navigationDefinition.getCategory();
	}

	@Override
	public JpaNavigationModel clone() {
		JpaNavigationModel model = new JpaNavigationModel(uuid);
		model.setCategory(category);
		return model;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public boolean isDefaultDbEntityAttrs() {
		return StringUtils.isEmpty(category);
	}

	public boolean equalsDbEntityAttrs(JpaNavigationModel model) {
		return StringUtils.equals(category, model.getCategory());
	}

}
