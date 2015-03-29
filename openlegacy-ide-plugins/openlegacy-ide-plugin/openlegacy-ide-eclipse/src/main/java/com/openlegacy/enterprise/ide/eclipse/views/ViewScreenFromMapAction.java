package com.openlegacy.enterprise.ide.eclipse.views;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.openlegacy.ide.eclipse.util.PathsUtil;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenEntityModel;

public class ViewScreenFromMapAction extends Action implements
		IDoubleClickListener {

	private EntityMapView mapView = null;

	public ViewScreenFromMapAction(EntityMapView mapView) {
		super();
		this.mapView = mapView;
	}

	@Override
	public void run() {
		BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
			public void run() {
				editSelectedScreens();
			}
		});
	}

	public void doubleClick(DoubleClickEvent event) {
		editSelectedScreens();
	}

	protected void editSelectedScreens() {
		mapView.getSelectedObjects();
		Object[] selected = mapView.getSelectedObjects();

		final ScreenEntityModel desc = (ScreenEntityModel) selected[0];

		EntityMapView.runOnDisplayThread(new Runnable() {

			@Override
			public void run() {

				IFile javaFile = mapView.getProject().getFile(
						"/src/main/java/"
								+ PathsUtil.packageToPath(desc.getDefinition()
										.getPackageName()) + "/"
								+ desc.getName() + ".java");

				IWorkbenchPage page = PlatformUI.getWorkbench()
						.getWorkbenchWindows()[0].getActivePage();
				IEditorDescriptor editorDescriptor = PlatformUI.getWorkbench()
						.getEditorRegistry()
						.getDefaultEditor(javaFile.getName());
				IEditorDescriptor descriptor = PlatformUI
						.getWorkbench()
						.getEditorRegistry()
						.findEditor(
								"com.openlegacy.enterprise.ide.eclipse.editors.ScreenEntityEditor");
				if (descriptor != null) {
					editorDescriptor = descriptor;
				}
				try {
					page.openEditor(new FileEditorInput(javaFile),
							editorDescriptor.getId());
				} catch (PartInitException e) {
					e.printStackTrace();
				}

			}

		});

	}

}
