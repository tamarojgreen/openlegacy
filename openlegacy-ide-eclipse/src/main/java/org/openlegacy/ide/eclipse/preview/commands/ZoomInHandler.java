package org.openlegacy.ide.eclipse.preview.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.openlegacy.ide.eclipse.preview.ScreenPreview;

public class ZoomInHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart activePart = HandlerUtil.getActivePart(event);
		if (activePart instanceof ScreenPreview){
			ScreenPreview screenPreview = (ScreenPreview) activePart;
			screenPreview.showEnlargedImage();
		}
		return null;
	}

}
