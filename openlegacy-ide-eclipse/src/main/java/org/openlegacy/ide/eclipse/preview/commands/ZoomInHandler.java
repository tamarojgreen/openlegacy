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
package org.openlegacy.ide.eclipse.preview.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.openlegacy.ide.eclipse.preview.AbstractEntityPreview;

public class ZoomInHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart activePart = HandlerUtil.getActivePart(event);
		if (activePart instanceof AbstractEntityPreview) {
			AbstractEntityPreview entityPreview = (AbstractEntityPreview)activePart;
			entityPreview.showEnlargedImage();
		}
		return null;
	}

}
