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
import org.eclipse.jdt.core.ICompilationUnit;
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

public class GenerateWebPageDialog extends AbstractGenerateDialog {

	private final static Logger logger = Logger.getLogger(GenerateWebPageDialog.class);

	protected GenerateWebPageDialog(Shell shell, ISelection selection) {
		super(shell, selection);
	}

	@Override
	protected void executeGenerate() {

		Object firstElement = ((TreeSelection)getSelection()).getFirstElement();
		if (!(firstElement instanceof ICompilationUnit)) {
			MessageDialog.openError(getShell(), PluginConstants.TITLE, "Invalid Java source selection");
		}
		final IFile screenEntitySourceFile = (IFile)((ICompilationUnit)firstElement).getResource();

		Job job = new Job("Generating web page") {

			@Override
			protected IStatus run(final IProgressMonitor monitor) {

				monitor.beginTask("Generating", 3);

				monitor.worked(2);
				EclipseDesignTimeExecuter.instance().createWebPage(screenEntitySourceFile, getSourceFolder(), getPackageValue(),
						GenerateWebPageDialog.this);

				monitor.worked(1);
				Display.getDefault().syncExec(new Runnable() {

					public void run() {
						try {
							monitor.worked(1);
							screenEntitySourceFile.getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
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
		ICompilationUnit selectionJavaSource = (ICompilationUnit)((IStructuredSelection)getSelection()).getFirstElement();
		String prefrenceSourceFolderPath = Prefrences.get(PluginConstants.DEFAULT_SOURCE_FOLDER_ID,
				PluginConstants.DEFAULT_SOURCE_FOLDER);
		IProject project = selectionJavaSource.getResource().getProject();
		getSourceFolderPathText().setText(project.getName() + "\\" + prefrenceSourceFolderPath);
		IJavaProject javaProject = JavaUtils.getJavaProjectFromIProject(project);
		setSourceFolder(javaProject.getPackageFragmentRoot(prefrenceSourceFolderPath));

		String prefrencePackage = Prefrences.get(PluginConstants.DEFAULT_PACKAGE_JAVA, "");
		// default web package should with ".web" at the end
		String prefrenceWebPackage = Prefrences.get(PluginConstants.DEFAULT_PACKAGE_WEB, prefrencePackage + ".web");
		getPackageText().setText(prefrenceWebPackage);
	}

	@Override
	protected void savePreferences() {
		String sourceFolderOnly = getSourceFolderPathText().getText().substring(
				getSourceFolder().getJavaProject().getProject().getName().length() + 1);
		Prefrences.put(PluginConstants.DEFAULT_SOURCE_FOLDER_ID, sourceFolderOnly);
		Prefrences.put(PluginConstants.DEFAULT_PACKAGE_WEB, getPackageText().getText());
	}
}
