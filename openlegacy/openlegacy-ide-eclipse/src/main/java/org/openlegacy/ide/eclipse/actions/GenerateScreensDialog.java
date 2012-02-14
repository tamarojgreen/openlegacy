package org.openlegacy.ide.eclipse.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.openlegacy.ide.eclipse.PluginConstants;
import org.openlegacy.ide.eclipse.util.JavaUtils;
import org.openlegacy.ide.eclipse.util.Prefrences;

import java.io.File;

public class GenerateScreensDialog extends AbstractGenerateDialog {

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
		String prefrenceSourceFolderPath = Prefrences.get(PluginConstants.DEFAULT_SOURCE_FOLDER_ID,
				PluginConstants.DEFAULT_SOURCE_FOLDER);
		getSourceFolderPathText().setText(selectionFile.getProject().getName() + "\\" + prefrenceSourceFolderPath);
		IJavaProject javaProject = JavaUtils.getJavaProjectFromIProject(selectionFile.getProject());
		setSourceFolder(javaProject.getPackageFragmentRoot(prefrenceSourceFolderPath));

		String prefrencePackage = Prefrences.get(PluginConstants.DEFAULT_PACKAGE_JAVA, "");
		getPackageText().setText(prefrencePackage);
	}

}
