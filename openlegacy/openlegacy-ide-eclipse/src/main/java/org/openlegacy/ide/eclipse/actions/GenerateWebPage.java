package org.openlegacy.ide.eclipse.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IActionDelegate;

public class GenerateWebPage extends AbstractAction {

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		GenerateWebPageDialog dialog = new GenerateWebPageDialog(getShell(), getSelection());
		dialog.open();

	}

}
