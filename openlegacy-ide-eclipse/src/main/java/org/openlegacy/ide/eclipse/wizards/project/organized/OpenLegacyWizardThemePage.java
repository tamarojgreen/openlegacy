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
import org.eclipse.swt.widgets.TableItem;
import org.openlegacy.designtime.newproject.model.ProjectTheme;
import org.openlegacy.designtime.newproject.organized.NewProjectMetadataRetriever;
import org.openlegacy.ide.eclipse.Messages;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenLegacyWizardThemePage extends AbstractOpenLegacyWizardPage {

	public static final String PAGE_ID = "wizardThemePage";

	private String[] themeNames;

	private TableViewer tableViewer;

	private Canvas canvas;

	private List<ProjectTheme> projectThemes = null;

	private List<ProjectTheme> allThemes = null;

	protected OpenLegacyWizardThemePage() {
		super(PAGE_ID);
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

	public void updateThemesData(String frontendSolution) {
		if (getControl().isDisposed()) {
			return;
		}

		projectThemes = getFilteredThemesByFrontendSolution(allThemes, frontendSolution);

		if (projectThemes == null || projectThemes.isEmpty()) {
			getControl().getDisplay().syncExec(new Runnable() {

				public void run() {
					updateStatus(Messages.getString("error_new_project_metadata_not_found"));//$NON-NLS-1$
				}
			});
			return;
		} else {
			String errorMsg = getErrorMessage();
			if (errorMsg != null && errorMsg == Messages.getString("error_new_project_metadata_not_found")) {
				updateStatus(null);
			}

			List<String> themes = new ArrayList<String>();
			for (ProjectTheme projectTheme : projectThemes) {
				themes.add(projectTheme.getDisplayName());
			}

			themeNames = themes.toArray(new String[] {});

			getControl().getDisplay().syncExec(new Runnable() {

				public void run() {
					tableViewer.setInput(themeNames);
					TableItem item = tableViewer.getTable().getItem(0);
					tableViewer.getTable().setSelection(item);
					tableViewer.getTable().showSelection();
					tableViewer.setSelection(tableViewer.getSelection());
				}
			});

		}

	}

	@Override
	public void updateControlsData(NewProjectMetadataRetriever retriever) {
		if (getControl().isDisposed()) {
			return;
		}
		allThemes = retriever.getThemes();
		if (allThemes == null || allThemes.isEmpty()) {
			getControl().getDisplay().syncExec(new Runnable() {

				public void run() {
					updateStatus(Messages.getString("error_new_project_metadata_not_found"));//$NON-NLS-1$
				}
			});
			return;
		}

		List<String> themes = new ArrayList<String>();
		for (ProjectTheme theme : allThemes) {
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
					if (tableViewer.getTable().getSelectionIndex() != -1) {
						getWizardModel().setProjectTheme(projectThemes.get(tableViewer.getTable().getSelectionIndex()));
					}

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

	private List<ProjectTheme> getFilteredThemesByFrontendSolution(List<ProjectTheme> themes, String frontendSolution) {
		List<ProjectTheme> filteredThemes = new ArrayList<ProjectTheme>();
		for (ProjectTheme projectTheme : themes) {
			if (frontendSolution.equals(projectTheme.getFrontendSolution())) {
				filteredThemes.add(projectTheme);
			}
		}

		return filteredThemes;
	}
}
