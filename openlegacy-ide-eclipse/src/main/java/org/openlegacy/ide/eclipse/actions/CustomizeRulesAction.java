package org.openlegacy.ide.eclipse.actions;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.IActionDelegate;
import org.openlegacy.designtime.mains.DesignTimeExecuter;

import java.text.MessageFormat;

public class CustomizeRulesAction extends AbstractAction {

	/**
	 * @see IActionDelegate#run(IAction)
	 */
	public void run(IAction action) {
		boolean cont = MessageDialog.openConfirm(
				getShell(),
				"OpenLegacy",
				MessageFormat.format(
						"Customize rules will create a a spring context under your project at: {0}, and will allow you to customize OpenLegacy analyzer rules. Continue? ",
						DesignTimeExecuter.CUSTOM_DESIGNTIME_CONTEXT_RELATIVE_PATH));
		if (cont) {
			IProject project = (IProject)((TreeSelection)getSelection()).getFirstElement();
			EclipseDesignTimeExecuter.instance().copyDesigntimeContext(project);
		}

	}

}
