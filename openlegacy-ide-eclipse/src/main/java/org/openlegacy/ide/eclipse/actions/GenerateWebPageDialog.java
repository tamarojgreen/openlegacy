package org.openlegacy.ide.eclipse.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
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
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.openlegacy.designtime.PerfrencesConstants;
import org.openlegacy.ide.eclipse.util.JavaUtils;

import java.io.File;
import java.text.MessageFormat;

public class GenerateWebPageDialog extends AbstractGenerateDialog {

	private final static Logger logger = Logger.getLogger(GenerateWebPageDialog.class);
	private Button generateHelpBtn;

	protected GenerateWebPageDialog(Shell shell, ISelection selection) {
		super(shell, selection);
	}

	@Override
	protected void executeGenerate() {

		final boolean generateHelp = generateHelpBtn.getSelection();
		Job job = new Job("Generating web page") {

			@Override
			protected IStatus run(final IProgressMonitor monitor) {

				final TreePath[] pathElements = ((TreeSelection)getSelection()).getPaths();

				monitor.beginTask("Generating", pathElements.length + 1);

				for (TreePath treePath : pathElements) {
					if (treePath.getLastSegment() instanceof ICompilationUnit) {
						final IFile screenEntitySourceFile = (IFile)((ICompilationUnit)treePath.getLastSegment()).getResource();

						EclipseDesignTimeExecuter.instance().generateWebPage(screenEntitySourceFile, getSourceFolder(),
								getPackageValue(), GenerateWebPageDialog.this, generateHelp);

						monitor.worked(1);

					} else {
						logger.warn(treePath.getLastSegment() + " is not a valid Java source selection");
					}
				}

				Display.getDefault().syncExec(new Runnable() {

					public void run() {
						try {
							monitor.worked(1);
							ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, monitor);
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
	protected void createDialogSpecific(Composite parent) {
		generateHelpBtn = new Button(parent, SWT.CHECK);
		generateHelpBtn.setText("Generate Help");
		generateHelpBtn.setSelection(true);
	}

	@Override
	protected void loadPrefrences() {
		ICompilationUnit selectionFile = (ICompilationUnit)((IStructuredSelection)getSelection()).getFirstElement();

		EclipseDesignTimeExecuter designtimeExecuter = EclipseDesignTimeExecuter.instance();
		IProject project = selectionFile.getResource().getProject();

		String prefrenceSourceFolderPath = designtimeExecuter.getPerference(project, PerfrencesConstants.API_SOURCE_FOLDER);
		getSourceFolderPathText().setText(
				MessageFormat.format("{0}{1}{2}", project.getName(), File.separator, prefrenceSourceFolderPath));

		IJavaProject javaProject = JavaUtils.getJavaProjectFromIProject(project);
		setSourceFolder(javaProject.getPackageFragmentRoot(prefrenceSourceFolderPath));

		String prefrencePackage = designtimeExecuter.getPerference(project, PerfrencesConstants.WEB_PACKAGE);
		getPackageText().setText(prefrencePackage);
	}

	@Override
	protected void savePreferences() {
		ICompilationUnit selectionFile = (ICompilationUnit)((IStructuredSelection)getSelection()).getFirstElement();

		String sourceFolderOnly = getSourceFolderPathText().getText().substring(
				getSourceFolder().getJavaProject().getProject().getName().length() + 1);

		IProject project = selectionFile.getResource().getProject();
		EclipseDesignTimeExecuter designtimeExecuter = EclipseDesignTimeExecuter.instance();

		designtimeExecuter.savePerference(project, PerfrencesConstants.API_SOURCE_FOLDER, sourceFolderOnly);
		designtimeExecuter.savePerference(project, PerfrencesConstants.WEB_PACKAGE, getPackageText().getText());
	}
}
