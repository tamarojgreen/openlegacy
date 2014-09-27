package org.openlegacy.ide.eclipse.wizards.project.organized;

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
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.actions.WorkspaceModifyOperation;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.openlegacy.designtime.mains.ProjectCreationRequest;
import org.openlegacy.designtime.newproject.organized.NewProjectMetadataRetriever;
import org.openlegacy.ide.eclipse.Activator;
import org.openlegacy.ide.eclipse.actions.EclipseDesignTimeExecuter;
import org.openlegacy.ide.eclipse.ui.preferences.PreferenceConstants;
import org.openlegacy.ide.eclipse.util.PathsUtil;
import org.openlegacy.ide.eclipse.wizards.project.organized.model.WizardModel;

/**
 * @author Ivan Bort
 * 
 */
public class OpenLegacyNewProjectWizard extends BasicNewResourceWizard implements IOpenLegacyWizard {

	private OpenLegacyWizardGeneralPage generalPage;
	private OpenLegacyWizardHostPage hostPage;
	private OpenLegacyWizardThemePage themePage;

	private NewProjectMetadataRetriever retriever;

	private WizardModel wizardModel = new WizardModel();

	public OpenLegacyNewProjectWizard() {
		super();
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		generalPage = new OpenLegacyWizardGeneralPage();
		hostPage = new OpenLegacyWizardHostPage();
		themePage = new OpenLegacyWizardThemePage();
		addPage(generalPage);
		addPage(hostPage);
		addPage(themePage);
	}

	@Override
	public void init(IWorkbench workbench, IStructuredSelection currentSelection) {
		retrieveNewProjectMetadata();
	}

	@Override
	public boolean canFinish() {
		return getWizardModel().isDemo()
				|| (generalPage.isPageComplete() && hostPage.isPageComplete() && themePage.isPageComplete());
	}

	@Override
	public boolean performFinish() {
		IPath workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation();

		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		final IProject projHandle = workspace.getRoot().getProject(wizardModel.getProjectName());
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
			projectCreationRequest.setBaseDir(PathsUtil.toOsLocation(workspacePath));
			projectCreationRequest.setTemplateName(wizardModel.getTemplateName());
			projectCreationRequest.setProjectName(wizardModel.getProjectName());
			projectCreationRequest.setProvider(wizardModel.getHostTypeName());
			projectCreationRequest.setDefaultPackageName(wizardModel.getDefaultPackageName());
			projectCreationRequest.setHostName(wizardModel.getHost());
			projectCreationRequest.setHostPort(wizardModel.getHostPort());
			projectCreationRequest.setCodePage(wizardModel.getCodePage());
			projectCreationRequest.setSupportTheme(wizardModel.isProjectSupportTheme());
			projectCreationRequest.setProjectTheme(wizardModel.getProjectTheme());
			projectCreationRequest.setZipFile(wizardModel.getZipFile());
			projectCreationRequest.setTemplateFetcher(retriever.getTemplateFetcher());
			projectCreationRequest.setRightToLeft(wizardModel.isRightToLeft());

			EclipseDesignTimeExecuter.instance().createProject(projectCreationRequest);
		} catch (Exception e) {
			throw (new RuntimeException(e));
		}

		try {
			getContainer().run(true, true, modifyOperation);
		} catch (Exception e) {
			throw (new RuntimeException(e));
		}
		return true;
	}

	public WizardModel getWizardModel() {
		return wizardModel;
	}

	private void retrieveNewProjectMetadata() {
		String templatesUrl = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.P_TEMPLATES_URL);

		retriever = new NewProjectMetadataRetriever(templatesUrl);
		Job job = new Job("Retrieving new project metadata") {//$NON-NLS-1$

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					retriever.retrieveMetadata();
					return Status.OK_STATUS;
				} catch (Exception e) {
					return new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Cannot retrieve new project metadata", e);//$NON-NLS-1$
				}
			}
		};

		job.addJobChangeListener(new JobChangeAdapter() {

			@Override
			public void done(IJobChangeEvent event) {
				themePage.updateControlsData(retriever);
				generalPage.updateControlsData(retriever);
				hostPage.updateControlsData(retriever);
			}

		});

		job.setPriority(Job.LONG);
		job.schedule();
	}

}
