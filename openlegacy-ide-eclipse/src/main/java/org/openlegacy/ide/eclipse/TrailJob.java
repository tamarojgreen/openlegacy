package org.openlegacy.ide.eclipse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.actions.GlobalBuildAction;
import org.openlegacy.ide.eclipse.ui.preferences.PreferenceConstants;
import org.openlegacy.ide.eclipse.util.PathsUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TrailJob extends Job {

	private final static Log logger = LogFactory.getLog(TrailJob.class);

	private Map<String, File[]> currentTrails = new HashMap<String, File[]>();

	public TrailJob() {
		super(Messages.TrailJob_job_name);
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();

		if (isAnalyzeNewTrails()) {

			for (final IProject project : projects) {
				final IFolder folder = project.getFolder("/src/test/resources/trails"); //$NON-NLS-1$
				File[] trails = PathsUtil.toOsLocation(folder).listFiles();

				File[] projectKnownTrails = currentTrails.get(project.getName());
				if (projectKnownTrails == null) {
					currentTrails.put(project.getName(), trails);
				} else {
					if (projectKnownTrails.length != trails.length) {
						try {
							currentTrails.put(project.getName(), trails);
							logger.info(Messages.TrailJob_message_found_new_trail);
							folder.refreshLocal(1, monitor);
							Display.getDefault().asyncExec(new Runnable() {

								public void run() {
									(new GlobalBuildAction(Activator.getActiveWorkbenchWindow(),
											IncrementalProjectBuilder.INCREMENTAL_BUILD)).run();
								}
							});
						} catch (CoreException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		schedule(5000);
		monitor.done();
		return Status.OK_STATUS;

	}

	private boolean isAnalyzeNewTrails() {
		return Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.P_ANALYZE_NEW_TRAILS);
	}
}
