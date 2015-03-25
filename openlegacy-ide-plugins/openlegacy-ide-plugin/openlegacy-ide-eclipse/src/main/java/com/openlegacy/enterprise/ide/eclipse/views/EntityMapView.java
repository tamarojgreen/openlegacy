package com.openlegacy.enterprise.ide.eclipse.views;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.zest.core.viewers.AbstractZoomableViewer;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.core.viewers.IZoomableWorkbenchPart;
import org.eclipse.zest.core.widgets.ZestStyles;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.CompositeLayoutAlgorithm;
import org.openlegacy.designtime.enums.BackendSolution;
import org.openlegacy.designtime.mains.DesignTimeExecuter;
import org.openlegacy.designtime.mains.DesignTimeExecuterImpl;
import org.openlegacy.ide.eclipse.util.PathsUtil;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.EntitiesFetcher;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractEntityModel;

public class EntityMapView extends ViewPart implements IZoomableWorkbenchPart {

	private GraphViewer viewer;
	private static final int SCREEN_HEIGHT = 80;
	private List<AbstractEntityModel> entities;

	private MapLabelProvider mapLabelProvider;
	private MapScreenEntityContentProvider screenContentProvider;
	private ViewScreenFromMapAction viewScreenFromMapAction;
	private IProject project;

	@Override
	public void createPartControl(Composite parent) {
		viewer = new GraphViewer(parent, SWT.NONE);

		viewer.setConnectionStyle(ZestStyles.CONNECTIONS_DIRECTED);
		viewer.setNodeStyle(SCREEN_HEIGHT);

		screenContentProvider = new MapScreenEntityContentProvider();
		viewer.setContentProvider(screenContentProvider);

		mapLabelProvider = new MapLabelProvider();
		viewer.setLabelProvider(mapLabelProvider);

		viewer.setLayoutAlgorithm(new CompositeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING,
				new LayoutAlgorithm[] { new EntityMapSpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING) }));
		
		viewScreenFromMapAction = new ViewScreenFromMapAction(this);
		viewer.addDoubleClickListener(viewScreenFromMapAction);
	}

	@Override
	public void setFocus() {
		//
	}

	public void setProject(final IProject project) {
		this.project = project;
		refresh(project);
	}
	
	public IProject getProject() {
		return project;
	}

	private void refresh(final IProject project) {
		File projectPath = PathsUtil.toOsLocation(project);
		DesignTimeExecuter executer = new DesignTimeExecuterImpl();
		final BackendSolution serviceType = executer.getServiceType(projectPath);
		setPartName(project.getName() + " Entity Map");
		Job job = new Job(Messages.getString("job.processing.entities")) {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				try {
					int length = project.members().length;
					monitor.beginTask(Messages.getString("task.processing"), length);
				} catch (CoreException e) {
					e.printStackTrace();
				}
				EntitiesFetcher fetcher = new EntitiesFetcher();
				entities = fetcher.fetch(project, monitor, serviceType);
				runOnDisplayThread(new Runnable() {

					@Override
					public void run() {
						refresh();
					}
				});
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

	public static void runOnDisplayThread(Runnable runnable) {
		Display display = getDisplay();
		if (display == null) {
			if (PlatformUI.isWorkbenchRunning()) {
			}
			return;
		}
		display.asyncExec(runnable);
	}

	private static Display getDisplay() {
		Display display = null;
		if (PlatformUI.isWorkbenchRunning()) {
			display = PlatformUI.getWorkbench().getDisplay();
		}
		return display;
	}

	public void refresh() {
		viewer.setInput(entities.toArray());
	}

	@Override
	public AbstractZoomableViewer getZoomableViewer() {
		return viewer;
	}
	
	public Object[] getSelectedObjects() {
		Object[] selectedObjects = new Object[((IStructuredSelection) viewer.getSelection()).size()];
		if (((IStructuredSelection) viewer.getSelection()).size() > 0) {
			Iterator<Object> iter = ((IStructuredSelection) viewer.getSelection()).iterator();
			for (int i = 0; iter.hasNext(); i++) {
				selectedObjects[i] = iter.next();
			}
		}
		return selectedObjects;
	}
	

}
