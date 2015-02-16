package org.openlegacy.ide.eclipse.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;

import java.net.MalformedURLException;
import java.net.URL;

public class RunApplicationAction extends AbstractAction {

	@Override
	public void run(IAction arg0) {
		final IProject project = (IProject)((TreeSelection)getSelection()).getFirstElement();
		ILaunchManager manager = DebugPlugin.getDefault().getLaunchManager();
		IFile configFile = project.getFile("run-application.launch");
		ILaunchConfiguration configuration = manager.getLaunchConfiguration(configFile);
		DebugUITools.launch(configuration, ILaunchManager.RUN_MODE);
		try {
			IWorkbenchBrowserSupport browserSupport = PlatformUI.getWorkbench().getBrowserSupport();
			IWebBrowser browser = browserSupport.createBrowser("openlegacy");
			browser.openURL(new URL("http://localhost:8080/" + project.getName()));
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
