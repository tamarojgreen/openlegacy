package org.openlegacy.ide.eclipse.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class ImportSourceAction extends AbstractAction {

	public void run(IAction arg0) {
		// TODO Auto-generated method stub
		Object firstElement = ((TreeSelection)getSelection()).getFirstElement();
		if (firstElement instanceof IProject) {
			ImportSourceDialog dialog = new ImportSourceDialog(getShell(), (IProject)firstElement,
					ResourcesPlugin.getWorkspace().getRoot().getLocation().toOSString());
			dialog.open();
			String localFileName = dialog.getLocalName();
			if (localFileName != null) {
				IFile ifile = ((IProject)firstElement).getFile(localFileName);
				IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();

				try {
					IDE.openEditor(page, ifile);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
