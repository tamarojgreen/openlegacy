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

package com.openlegacy.enterprise.ide.eclipse.editors.actions.jpa;

import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaManyToOneFieldModel;

import java.util.UUID;

import javax.persistence.ManyToOne;

/**
 * @author Ivan Bort
 * 
 */
public class JpaManyToOneFieldAction extends JpaFieldAction {

	public JpaManyToOneFieldAction(UUID uuid, JpaManyToOneFieldModel namedObject, ActionType actionType, int kind, String key,
			Object value) {
		super(uuid, namedObject, actionType, kind, key, value);
	}

	@Override
	public Class<?> getAnnotationClass() {
		return ManyToOne.class;
	}

}
