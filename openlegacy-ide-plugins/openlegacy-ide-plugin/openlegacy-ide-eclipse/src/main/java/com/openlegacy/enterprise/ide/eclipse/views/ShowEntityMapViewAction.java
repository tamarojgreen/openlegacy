package com.openlegacy.enterprise.ide.eclipse.views;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.actions.AbstractAction;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

public class ShowEntityMapViewAction extends AbstractAction {

	private static final String MAP_VIEW_ID = "com.openlegacy.enterprise.ide.eclipse.views.EntityMapView";

	@Override
	public void run(IAction arg0) {
		EntityMapView.runOnDisplayThread(new Runnable() {

			@Override
			public void run() {
				try {
					EntityMapView mapView = null;
					IProject project = getProject();

					IViewReference ref = getMapViewReference(project.getName());
					if (ref != null) {
						IViewPart view = ref.getView(true);
						if (view != null) {
							mapView = (EntityMapView)view;
							mapView.setProject(project);
							PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(MAP_VIEW_ID,
									project.getName(), IWorkbenchPage.VIEW_ACTIVATE);
						}
					}

					if (mapView == null) {
						mapView = (EntityMapView)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(
								MAP_VIEW_ID, project.getName(), IWorkbenchPage.VIEW_VISIBLE);
						mapView.setProject(project);
					}

				} catch (PartInitException ex) {
					// TODO LOG
					System.out.println();
				}
			}
		});
	}

	public IViewReference getMapViewReference(String projectName) {
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
		if (windows != null && windows.length > 0) {
			return windows[0].getActivePage().findViewReference(MAP_VIEW_ID, projectName);
		}
		return null;
	}

	private IProject getProject() {
		IProject project = null;
		IStructuredSelection structSelecetion = (IStructuredSelection)getSelection();
		Object firstElement = structSelecetion.getFirstElement();
		if (firstElement instanceof IAdaptable) {
			project = (IProject)((IAdaptable)firstElement).getAdapter(IProject.class);
		}
		return project;
	}
}