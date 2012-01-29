package org.openlegacy.ide.eclipse.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionDelegate;

public class GenerateScreensAction extends AbstractAction {

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		GenerateScreensDialog dialog = new GenerateScreensDialog(getShell(), getSelection());
		dialog.open();

	}

}
