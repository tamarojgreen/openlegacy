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
package com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.rpc;

import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.ActionModel;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.LabelProvider;

/**
 * @author Ivan Bort
 *
 */
public class ActionsMasterBlockLabelProvider extends LabelProvider {

	@Override
	public String getText(Object element) {
		ActionModel model = (ActionModel) element;
		return StringUtils.isEmpty(model.getActionName()) ? "undefined" : model.getActionName();
	}

}
