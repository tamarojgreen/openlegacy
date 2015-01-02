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

package com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters;

import org.openlegacy.db.actions.DbAction;

/**
 * @author Ivan Bort
 * 
 */
public class JpaActionViewerFilter extends AbstractViewerFilter {

	public JpaActionViewerFilter() {
		super(DbAction.class);
	}

}
