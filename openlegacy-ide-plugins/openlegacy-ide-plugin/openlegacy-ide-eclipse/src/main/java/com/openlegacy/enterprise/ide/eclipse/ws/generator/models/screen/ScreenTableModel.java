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
package com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractNamedModel;

import org.openlegacy.designtime.generators.CodeBasedScreenTableDefinition;

/**
 * @author Aleksey Yeremeyev
 * 
 */
public class ScreenTableModel extends AbstractNamedModel {

	private CodeBasedScreenTableDefinition definition = null;

	/**
	 * @param name
	 * @param parent
	 */
	public ScreenTableModel(CodeBasedScreenTableDefinition screenTableDefinition, AbstractNamedModel parent) {
		super(screenTableDefinition.getTableEntityName(), parent);
		this.definition = screenTableDefinition;
	}

	public CodeBasedScreenTableDefinition getDefinition() {
		return this.definition;
	}

}
