/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.ide.eclipse.actions;

import org.apache.commons.lang.ArrayUtils;
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
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.openlegacy.designtime.UserInteraction;
import org.openlegacy.ide.eclipse.EclipseUtil;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.PluginConstants;

import java.io.File;
import java.text.MessageFormat;

public class GenerateViewDialog extends Dialog implements UserInteraction {

	private final static Logger logger = Logger.getLogger(GenerateViewDialog.class);

	private Combo projectName;

	private Button generateHelpBtn;
	private Button generateMobilePageBtn;
	private ISelection selection;

	private IFile file;

	protected GenerateViewDialog(Shell shell, ISelection selection) {
		super(shell);
		this.selection = selection;
		this.file = (IFile)((ICompilationUnit)((TreeSelection)selection).getFirstElement()).getResource();
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		parent = new Composite(parent, SWT.NONE);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		GridData gd = new GridData();
		gd.widthHint = 400;
		parent.setLayoutData(gd);
		parent.setLayout(gridLayout);

		parent.getShell().setText(PluginConstants.TITLE);

		Label label = new Label(parent, SWT.NULL);
		label.setText(Messages.getString("label_project_name"));

		Composite composite = new Composite(parent, SWT.NONE);

		gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gd = new GridData(GridData.FILL_HORIZONTAL, GridData.FILL_VERTICAL, true, true);
		gd.horizontalSpan = 3;
		composite.setLayoutData(gd);
		composite.setLayout(gridLayout);

		projectName = new Combo(composite, SWT.BORDER | SWT.SINGLE | SWT.READ_ONLY);
		String[] projectNames = EclipseUtil.getProjectNames();
		projectName.setItems(projectNames);
		projectName.select(ArrayUtils.indexOf(projectNames, getProject().getName()));

		generateHelpBtn = new Button(composite, SWT.CHECK);
		generateHelpBtn.setText(Messages.getString("label_generate_help"));
		generateHelpBtn.setSelection(true);
		// "Generate mobile page" option (checkbox)
		generateMobilePageBtn = new Button(composite, SWT.CHECK);
		generateMobilePageBtn.setText(Messages.getString("label_generate_mobile_page"));
		generateMobilePageBtn.setSelection(true);

		loadPrefrences();

		return parent;
	}

	@Override
	protected void okPressed() {
		if (!validate()) {
			return;
		}

		savePreferences();
		executeGenerate();

		close();
	}

	private static boolean validate() {
		return true;
	}

	protected void executeGenerate() {

		final boolean generateHelp = generateHelpBtn.getSelection();
		final boolean generateMobilePage = generateMobilePageBtn.getSelection();
		final String projectNameText = projectName.getText();
		Job job = new Job(Messages.getString("job_generating_view")) {

			@Override
			protected IStatus run(final IProgressMonitor monitor) {

				final TreePath[] pathElements = ((TreeSelection)selection).getPaths();

				monitor.beginTask(Messages.getString("task_generating"), pathElements.length + 1);

				for (TreePath treePath : pathElements) {
					if (treePath.getLastSegment() instanceof ICompilationUnit) {
						final IFile screenEntitySourceFile = (IFile)((ICompilationUnit)treePath.getLastSegment()).getResource();
						IProject project = EclipseUtil.getProject(projectNameText);
						EclipseDesignTimeExecuter.instance().generateView(screenEntitySourceFile, project,
								GenerateViewDialog.this, generateHelp, generateMobilePage);

						monitor.worked(1);

					} else {
						logger.warn(MessageFormat.format(Messages.getString("warn_java_source_not_valid_selection"),
								treePath.getLastSegment()));
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

	protected void loadPrefrences() {

		// EclipseDesignTimeExecuter designtimeExecuter = EclipseDesignTimeExecuter.instance();
		// IProject project = getProject();

		// TODO impl. pref.
		// String prefrenceSourceFolderPath = designtimeExecuter.getPreference(project, PreferencesConstants.API_SOURCE_FOLDER);
		// getSourceFolderPathText().setText(
		//		MessageFormat.format("{0}{1}{2}", project.getName(), File.separator, prefrenceSourceFolderPath)); //$NON-NLS-1$

	}

	protected void savePreferences() {
		// TODO impl. pref.
		// EclipseDesignTimeExecuter designtimeExecuter = EclipseDesignTimeExecuter.instance();
		// designtimeExecuter.savePreference(getProject(), PreferencesConstants.WEB_PACKAGE, getPackageText().getText());
	}

	public IProject getProject() {
		return file.getProject();
	}

	public boolean isOverride(final File file) {
		final Object[] result = new Object[1];
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				result[0] = MessageDialog.openQuestion(getShell(), PluginConstants.TITLE,
						MessageFormat.format(Messages.getString("question_override_file"), file.getName()));
			}
		});

		return (Boolean)result[0];
	}

	public void open(File file) {
		// TODO Auto-generated method stub

	}
}
