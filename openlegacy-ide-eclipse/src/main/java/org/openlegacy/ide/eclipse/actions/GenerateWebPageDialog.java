package org.openlegacy.ide.eclipse.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.GlobalBuildAction;
import org.openlegacy.ide.eclipse.Activator;
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

		Job job = new Job("Generating web page") {

			@Override
			protected IStatus run(final IProgressMonitor monitor) {

				final TreePath[] pathElements = ((TreeSelection)getSelection()).getPaths();

				monitor.beginTask("Generating", pathElements.length + 1);

				for (TreePath treePath : pathElements) {
					if (treePath.getLastSegment() instanceof ICompilationUnit) {
						final IFile screenEntitySourceFile = (IFile)((ICompilationUnit)treePath.getLastSegment()).getResource();

						EclipseDesignTimeExecuter.instance().createWebPage(screenEntitySourceFile, getSourceFolder(),
								getPackageValue(), GenerateWebPageDialog.this);

						monitor.worked(1);

					} else {
						logger.warn(treePath.getLastSegment() + " is not a valid Java source selection");
					}
				}

				Display.getDefault().syncExec(new Runnable() {

					public void run() {
						try {
							monitor.worked(1);
							((IProject)pathElements[0].getFirstSegment()).refreshLocal(IResource.DEPTH_INFINITE, monitor);
							(new GlobalBuildAction(Activator.getActiveWorkbenchWindow(),
									IncrementalProjectBuilder.INCREMENTAL_BUILD)).run();
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
