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
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchWizard;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.openlegacy.designtime.mains.ProjectCreationRequest;
import org.openlegacy.designtime.newproject.NewProjectMetadataRetriever;
import org.openlegacy.ide.eclipse.Activator;
import org.openlegacy.ide.eclipse.actions.EclipseDesignTimeExecuter;
import org.openlegacy.ide.eclipse.ui.preferences.PreferenceConstants;
import org.openlegacy.ide.eclipse.util.PathsUtil;

public class OpenLegacyNewProjectWizard extends BasicNewResourceWizard {

	private final static Logger logger = Logger.getLogger(OpenLegacyNewWizardGeneralPage.class);

	private OpenLegacyNewWizardGeneralPage generalPage;
	private OpenLegacyNewWizardProviderPage providerPage;
	private OpenLegacyNewWizardThemePage themePage;
	private ISelection selection;

	private NewProjectMetadataRetriever retriever;

	public OpenLegacyNewProjectWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		generalPage = new OpenLegacyNewWizardGeneralPage(selection);
		addPage(generalPage);

		providerPage = new OpenLegacyNewWizardProviderPage("");
		addPage(providerPage);

		themePage = new OpenLegacyNewWizardThemePage("");
		addPage(themePage);
	}

	@Override
	public boolean performFinish() {
		boolean retVal = true;
		final String projectName = generalPage.getProjectName();

		IPath workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation();

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProject projHandle = workspace.getRoot().getProject(projectName);
		final IProjectDescription desc = workspace.newProjectDescription(projHandle.getName());

		WorkspaceModifyOperation modifyOperation = new WorkspaceModifyOperation() {

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
			ProjectCreationRequest projectCreationRequest = new ProjectCreationRequest();
			projectCreationRequest.setTemplateName(generalPage.getTemplateName());
			projectCreationRequest.setBaseDir(PathsUtil.toOsLocation(workspacePath));
			projectCreationRequest.setProjectName(projectName);
			projectCreationRequest.setProvider(providerPage.getProvider());
			projectCreationRequest.setDefaultPackageName(generalPage.getDefaultPackageName());
			projectCreationRequest.setHostName(providerPage.getHostName());
			projectCreationRequest.setHostPort(Integer.parseInt(providerPage.getHostPort()));
			projectCreationRequest.setCodePage(providerPage.getCodePage());
			projectCreationRequest.setSupportTheme(generalPage.isProjectSupportTheme());
			projectCreationRequest.setThemeName(themePage.getThemeName());
			projectCreationRequest.setZipFile(generalPage.getZipFile());
			projectCreationRequest.setTemplateFetcher(retriever.getTemplateFetcher());

			EclipseDesignTimeExecuter.instance().createProject(projectCreationRequest);
		} catch (Exception e) {
 			throw(new RuntimeException(e));
		}

		try {
			getContainer().run(true, true, modifyOperation);
		} catch (Exception e) {
 			throw(new RuntimeException(e));
		}

		return retVal;
	}

	@Override
	public boolean canFinish() {
		return generalPage.isDemo() || (generalPage.isPageComplete() && providerPage.isPageComplete() && themePage.isPageComplete());
	}

	/**
	 * We will accept the selection in the workbench to see if we can initialize from it.
	 * 
	 * @see IWorkbenchWizard#init(IWorkbench, IStructuredSelection)
	 */
	@Override
	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.selection = selection;

		this.retrieveNewProjectMetadata();
	}

	private void retrieveNewProjectMetadata() {

		String templateUrl = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_TEMPLATES_URL);

		this.retriever = new NewProjectMetadataRetriever(templateUrl);

		Job job = new Job("Retrieving new project metadata") {//$NON-NLS-1$

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					retriever.retrieveMetadata();
					return Status.OK_STATUS;
				} catch (Exception e) {
					return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Cannot retrieve new project metadata", e);
				}
			}
		};

		job.addJobChangeListener(new JobChangeAdapter() {

			@Override
			public void done(IJobChangeEvent event) {
				generalPage.updateControls(retriever.getProjectTypes());
				providerPage.updateControls(retriever.getProviders());
				themePage.updateControls(retriever.getThemes());
			}

		});

		job.setPriority(Job.LONG);
		job.schedule();
	}
}