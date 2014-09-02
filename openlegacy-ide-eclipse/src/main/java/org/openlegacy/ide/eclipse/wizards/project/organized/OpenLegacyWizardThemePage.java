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
package org.openlegacy.ide.eclipse.wizards.project.organized;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.openlegacy.designtime.newproject.model.ProjectTheme;
import org.openlegacy.designtime.newproject.organized.NewProjectMetadataRetriever;
import org.openlegacy.ide.eclipse.Messages;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenLegacyWizardThemePage extends AbstractOpenLegacyWizardPage {

	private String[] themeNames;

	private TableViewer tableViewer;

	private Canvas canvas;

	private List<ProjectTheme> projectThemes = null;

	protected OpenLegacyWizardThemePage() {
		super("wizardThemePage");//$NON-NLS-1$
		setTitle(Messages.getString("title_ol_project_wizard"));//$NON-NLS-1$
		setDescription(Messages.getString("info_ol_project_wizard"));//$NON-NLS-1$
	}

	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.horizontalSpacing = 9;

		tableViewer = new TableViewer(container);
		tableViewer.setContentProvider(new ArrayContentProvider());
		tableViewer.setInput(new String[] { "Pending..." });//$NON-NLS-1$

		GridData data = new GridData(GridData.FILL_HORIZONTAL, SWT.FILL, false, false);
		data.horizontalSpan = 1;
		tableViewer.getTable().setLayoutData(data);

		canvas = new Canvas(container, SWT.NONE);
		canvas.setLayoutData(new GridData(GridData.FILL_BOTH));
		canvas.addPaintListener(this.getCanvasPaintListener());

		setControl(container);
		setPageComplete(false);

		refreshTable();
	}

	@Override
	public void updateControlsData(NewProjectMetadataRetriever retriever) {
		if (getControl().isDisposed()) {
			return;
		}
		projectThemes = retriever.getThemes();
		if (projectThemes == null || projectThemes.isEmpty()) {
			getControl().getDisplay().syncExec(new Runnable() {

				public void run() {
					updateStatus(Messages.getString("error_new_project_metadata_not_found"));//$NON-NLS-1$
				}
			});
			return;
		}

		List<String> themes = new ArrayList<String>();
		for (ProjectTheme theme : projectThemes) {
			themes.add(theme.getDisplayName());
		}
		themeNames = themes.toArray(new String[] {});

		getControl().getDisplay().syncExec(new Runnable() {

			public void run() {
				tableViewer.setInput(themeNames);
				tableViewer.getTable().select(0);
				tableViewer.addSelectionChangedListener(getTableSelectionListener());
				// to trigger SelectionChangedListener
				tableViewer.setSelection(tableViewer.getSelection());
			}
		});
	}

	private PaintListener getCanvasPaintListener() {
		return new PaintListener() {

			public void paintControl(PaintEvent e) {
				if (projectThemes == null) {
					return;
				}
				int index = tableViewer.getTable().getSelectionIndex();

				BufferedInputStream in = new BufferedInputStream(
						new ByteArrayInputStream(projectThemes.get(index).getImageData()));
				ImageData imageData = new ImageData(in);
				Image image = new Image(e.widget.getDisplay(), imageData);
				int x = (e.width - image.getBounds().width) / 2;
				e.gc.drawImage(image, x, 0);
				try {
					in.close();
				} catch (IOException e1) {
				}
			}
		};
	}

	private ISelectionChangedListener getTableSelectionListener() {
		return new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent e) {
				if (projectThemes != null) {
					canvas.redraw();
					getWizardModel().setProjectTheme(projectThemes.get(tableViewer.getTable().getSelectionIndex()));
					setPageComplete(true);
				}
			}
		};
	}

	private void refreshTable() {
		tableViewer.refresh();
		tableViewer.getTable().select(0);
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	public String getThemeName() {
		if (this.projectThemes != null) {
			return this.projectThemes.get(tableViewer.getTable().getSelectionIndex()).getDisplayName().toLowerCase();
		}
		return null;
	}
}
