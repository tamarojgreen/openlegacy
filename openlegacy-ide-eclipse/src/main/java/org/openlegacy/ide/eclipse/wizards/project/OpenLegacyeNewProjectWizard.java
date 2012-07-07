package org.openlegacy.ide.eclipse.wizards.project;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.openlegacy.designtime.mains.ProjectCreationRequest;
import org.openlegacy.ide.eclipse.actions.EclipseDesignTimeExecuter;
import org.openlegacy.ide.eclipse.util.PathsUtil;

public class OpenLegacyeNewProjectWizard extends BasicNewResourceWizard {

	private final static Logger logger = Logger.getLogger(OpenLegacyNewWizardPage.class);

	private OpenLegacyNewWizardPage page;
	private ISelection selection;

	public OpenLegacyeNewProjectWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		page = new OpenLegacyNewWizardPage(selection);
		addPage(page);
	}

	@Override
	public boolean performFinish() {
		final String templateName = page.getTemplateName();
		final String projectName = page.getProjectName();
		String defaultPackageName = page.getDefaultPackageName();
		String providerName = page.getProvider();
		String hostName = page.getHostName();
		String hostPort = page.getHostPort();

		IPath workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation();

		IWorkspace w = ResourcesPlugin.getWorkspace();
		final IProject projHandle = w.getRoot().getProject(projectName);
		final IProjectDescription desc = w.newProjectDescription(projHandle.getName());

		WorkspaceModifyOperation op = new WorkspaceModifyOperation() {

			@Override
			protected void execute(IProgressMonitor monitor) throws CoreException {
				try {
					projHandle.create(desc, monitor);
					projHandle.open(IResource.BACKGROUND_REFRESH, new SubProgressMonitor(monitor, 2000));
				} finally {
					monitor.done();
				}

			}
		};
		try {
			getContainer().run(true, true, op);

			ProjectCreationRequest projectCreationRequest = new ProjectCreationRequest();
			projectCreationRequest.setTemplateName(templateName);
			projectCreationRequest.setBaseDir(PathsUtil.toOsLocation(workspacePath));
			projectCreationRequest.setProjectName(projectName);
			projectCreationRequest.setProvider(providerName);
			projectCreationRequest.setDefaultPackageName(defaultPackageName);
			projectCreationRequest.setHostName(hostName);
			projectCreationRequest.setHostPort(Integer.parseInt(hostPort));
			EclipseDesignTimeExecuter.instance().createProject(projectCreationRequest);
		} catch (Exception e) {
			logger.fatal(e.getMessage(), e);
			return false;
		}

		return true;
	}

	@Override
	public boolean canFinish() {
		return page.isPageComplete();
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;
	}

}