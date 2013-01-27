package org.openlegacy.ide.eclipse.util;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

public class EclipseUtils {

	private final static Logger logger = Logger.getLogger(EclipseUtils.class);

	public static boolean isEditorOpen(final IFile file) {
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();

		if (windows != null && windows.length > 0) {
			IEditorReference[] editors = windows[0].getPages()[0].getEditorReferences();
			for (IEditorReference iEditorReference : editors) {
				try {
					if (iEditorReference.getEditorInput() instanceof FileEditorInput) {
						FileEditorInput fileEditorInput = (FileEditorInput)iEditorReference.getEditorInput();
						if (fileEditorInput.getName().equals(file.getName())) {
							return true;
						}
					}
				} catch (PartInitException e) {
					logger.fatal(e);
				}
			}
		}
		return false;
	}
}
