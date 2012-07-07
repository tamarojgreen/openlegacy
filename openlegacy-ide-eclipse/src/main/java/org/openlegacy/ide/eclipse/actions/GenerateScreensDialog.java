package org.openlegacy.ide.eclipse.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.openlegacy.designtime.EntityUserInteraction;
import org.openlegacy.designtime.PerfrencesConstants;
import org.openlegacy.ide.eclipse.PluginConstants;
import org.openlegacy.ide.eclipse.util.JavaUtils;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.io.File;
import java.text.MessageFormat;

public class GenerateScreensDialog extends AbstractGenerateDialog implements EntityUserInteraction<ScreenEntityDefinition> {

	protected GenerateScreensDialog(Shell shell, ISelection selection) {
		super(shell, selection);
	}

	private final static Logger logger = Logger.getLogger(GenerateScreensDialog.class);

	@Override
	protected void executeGenerate() {

		Object firstElement = ((TreeSelection)getSelection()).getFirstElement();
		if (!(firstElement instanceof IFile)) {
			MessageDialog.openError(getShell(), PluginConstants.TITLE, "Invalid trail file selection");
		}
		final IFile trailPath = (IFile)firstElement;

		Job job = new Job("Generating screens") {

			@Override
			protected IStatus run(final IProgressMonitor monitor) {

				int fileSize = (int)(new File(trailPath.getLocation().toOSString()).length() / 1000);
				monitor.beginTask("Activating Analyzer", fileSize);

				monitor.worked(2);
				EclipseDesignTimeExecuter.instance().generateScreens(trailPath, getSourceFolder(), getPackageValue(),
						GenerateScreensDialog.this);

				monitor.worked(fileSize - 4);
				Display.getDefault().syncExec(new Runnable() {

					public void run() {
						try {
							monitor.worked(1);
							trailPath.getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
							monitor.done();
						} catch (CoreException e) {
							logger.fatal(e);
						}
					}
				});

				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

	@Override
	protected void loadPrefrences() {
		IFile selectionFile = (IFile)((IStructuredSelection)getSelection()).getFirstElement();

		EclipseDesignTimeExecuter designtimeExecuter = EclipseDesignTimeExecuter.instance();
		IProject project = selectionFile.getProject();

		String prefrenceSourceFolderPath = designtimeExecuter.getPerference(project, PerfrencesConstants.API_SOURCE_FOLDER);
		getSourceFolderPathText().setText(
				MessageFormat.format("{0}{1}{2}", project.getName(), File.separator, prefrenceSourceFolderPath));

		IJavaProject javaProject = JavaUtils.getJavaProjectFromIProject(project);
		setSourceFolder(javaProject.getPackageFragmentRoot(prefrenceSourceFolderPath));

		String prefrencePackage = designtimeExecuter.getPerference(project, PerfrencesConstants.API_PACKAGE);
		getPackageText().setText(prefrencePackage);
	}

	@Override
	protected void savePreferences() {
		IFile selectionFile = (IFile)((IStructuredSelection)getSelection()).getFirstElement();

		String sourceFolderOnly = getSourceFolderPathText().getText().substring(
				getSourceFolder().getJavaProject().getProject().getName().length() + 1);

		IProject project = selectionFile.getProject();
		EclipseDesignTimeExecuter designtimeExecuter = EclipseDesignTimeExecuter.instance();

		designtimeExecuter.savePerference(project, PerfrencesConstants.API_SOURCE_FOLDER, sourceFolderOnly);
		designtimeExecuter.savePerference(project, PerfrencesConstants.API_PACKAGE, getPackageText().getText());
	}

	public boolean customizeEntity(final ScreenEntityDefinition screenEntityDefinition) {

		final BooleanContainer generate = new BooleanContainer();
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				CustomizeScreenEntityDialog customizeDialog = new CustomizeScreenEntityDialog(getShell(), screenEntityDefinition);
				int result = customizeDialog.open();
				if (result == Window.CANCEL) {
					generate.setBooleanValue(false);
				}
			}
		});
		return generate.getBooleanValue();
	}

	private static class BooleanContainer {

		boolean booleanValue = true;

		public void setBooleanValue(boolean booleanValue) {
			this.booleanValue = booleanValue;
		}

		public boolean getBooleanValue() {
			return booleanValue;

		}
	}
}
