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

package com.openlegacy.enterprise.ide.eclipse.editors.pages.jpa;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.AbstractEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.JpaEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaActionsModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.ActionsComboBoxCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.ActionsDialogCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.ActionsTextCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.jpa.ActionsPageTableContentProvider;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.openlegacy.db.definitions.DbActionDefinition;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.ide.eclipse.Activator;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;

/**
 * @author Ivan Bort
 * 
 */
public class ActionsPape extends AbstractPage {

	private final static String PAGE_ID = "org.openlegacy.enterprise.ide.eclipse.jpa.pages.actions"; //$NON-NLS-1$

	private TableViewer tableViewer;

	private JpaActionsModel actionsModel;

	public ActionsPape(AbstractEditor editor) {
		super(editor, PAGE_ID, Messages.getString("jpa.actions.page.title"));//$NON-NLS-1$
	}

	@Override
	public void createFormContent() {
		actionsModel = ((JpaEntityEditor)getEntityEditor()).getEntity().getActionsModel().clone();

		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();

		form.setImage(Activator.getDefault().getImage(Activator.IMG_TRANSPARENT));
		form.setText(Messages.getString("jpa.actions.page.title"));//$NON-NLS-1$
		form.setBackgroundImage(Activator.getDefault().getImage(Activator.IMG_FORM_BG));

		TableWrapLayout layout = new TableWrapLayout();
		layout.leftMargin = 10;
		layout.rightMargin = 10;
		form.getBody().setLayout(layout);

		// create section
		Section section = toolkit.createSection(form.getBody(), Section.DESCRIPTION | ExpandableComposite.TITLE_BAR);
		// section.marginWidth = 10;
		section.setText(Messages.getString("jpa.actions.page.section.text")); //$NON-NLS-1$
		section.setDescription(Messages.getString("jpa.actions.page.section.desc")); //$NON-NLS-1$
		TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
		td.grabHorizontal = true;
		section.setLayoutData(td);
		// create composite
		Composite client = toolkit.createComposite(section);
		GridLayout gl = new GridLayout();
		gl.marginWidth = gl.marginHeight = 0;
		gl.numColumns = 2;
		client.setLayout(gl);

		ScrolledComposite scrolledComposite = new ScrolledComposite(client, SWT.NONE);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.heightHint = 200;
		Point size = client.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		gd.widthHint = size.x;
		scrolledComposite.setLayoutData(gd);

		Table t = toolkit.createTable(scrolledComposite, SWT.FULL_SELECTION);
		t.setHeaderVisible(true);
		t.setLinesVisible(true);

		scrolledComposite.setContent(t);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setMinSize(t.computeSize(SWT.DEFAULT, SWT.DEFAULT));

		// add buttons
		addPanelWithButtons(toolkit, client);

		// create table
		tableViewer = new TableViewer(t);
		createColumns(tableViewer, ((JpaEntityEditor)getEntityEditor()).getEntity());

		tableViewer.setContentProvider(new ActionsPageTableContentProvider());
		tableViewer.setInput(actionsModel);
		toolkit.paintBordersFor(client);
		// set client to section
		section.setClient(client);
	}

	@Override
	public void refresh() {
		actionsModel = ((JpaEntityEditor)getEntityEditor()).getEntity().getActionsModel().clone();
		if (tableViewer != null) {
			tableViewer.setInput(actionsModel);
		}
	}

	private void createColumns(TableViewer viewer, JpaEntity entity) {
		// "action" column
		TableViewerColumn vcol = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tcol = vcol.getColumn();
		tcol.setText(Messages.getString("jpa.actions.page.col.action.label"));//$NON-NLS-1$
		tcol.setResizable(false);
		tcol.setWidth(150);

		vcol.setEditingSupport(new ActionsDialogCellEditingSupport(viewer, AnnotationConstants.ACTION));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ActionDefinition action = (ActionDefinition)cell.getElement();
				cell.setText(action.getActionName());
				updateModel();
			}
		});

		// "displayName" column
		vcol = new TableViewerColumn(viewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("jpa.actions.page.col.display.name.label"));//$NON-NLS-1$
		tcol.setResizable(false);
		tcol.setWidth(150);

		vcol.setEditingSupport(new ActionsTextCellEditingSupport(viewer, AnnotationConstants.DISPLAY_NAME));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ActionDefinition action = (ActionDefinition)cell.getElement();
				cell.setText(action.getDisplayName());
				updateModel();
			}
		});

		// "alias" column
		vcol = new TableViewerColumn(viewer, SWT.FILL);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("jpa.actions.page.col.alias.label"));//$NON-NLS-1$
		tcol.setResizable(false);
		tcol.setWidth(150);

		vcol.setEditingSupport(new ActionsTextCellEditingSupport(viewer, AnnotationConstants.ALIAS));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ActionDefinition action = (ActionDefinition)cell.getElement();
				cell.setText(action.getAlias());
				updateModel();
			}
		});

		// "is global" column
		vcol = new TableViewerColumn(viewer, SWT.FILL);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("jpa.actions.page.col.global.label"));//$NON-NLS-1$
		tcol.setResizable(false);
		tcol.setWidth(100);

		List<String> items = new ArrayList<String>();
		items.add(Boolean.FALSE.toString().toLowerCase());
		items.add(Boolean.TRUE.toString().toLowerCase());

		vcol.setEditingSupport(new ActionsComboBoxCellEditingSupport(viewer, AnnotationConstants.GLOBAL, items));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				DbActionDefinition action = (DbActionDefinition)cell.getElement();
				cell.setText(String.valueOf(action.isGlobal()));
				updateModel();
			}
		});

		// "target entity" column
		vcol = new TableViewerColumn(viewer, SWT.FILL);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("jpa.actions.page.col.target.entity"));//$NON-NLS-1$
		tcol.setResizable(false);
		tcol.setWidth(150);

		vcol.setEditingSupport(new ActionsDialogCellEditingSupport(viewer, AnnotationConstants.TARGET_ENTITY));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ActionModel action = (ActionModel)cell.getElement();
				cell.setText(action.getTargetEntityClassName());
				updateModel();
				validateTargetEntityColumn(action);
			}
		});
	}

	private void addPanelWithButtons(FormToolkit toolkit, Composite parent) {
		Composite panel = toolkit.createComposite(parent);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		gl.marginWidth = 2;
		gl.marginHeight = 0;
		panel.setLayout(gl);
		GridData gd = new GridData(GridData.FILL_VERTICAL);
		gd.widthHint = 100;
		panel.setLayoutData(gd);
		// add button
		Button addButton = toolkit.createButton(panel, Messages.getString("Button.add"), SWT.PUSH);//$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		addButton.setLayoutData(gd);
		addButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				actionsModel.getActions().add(new ActionModel("", "", true, "", ""));//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				tableViewer.setInput(actionsModel);
				updateModel();
				tableViewer.getTable().select(tableViewer.getTable().getItemCount() - 1);
			}

		});

		// remove button
		Button removeButton = toolkit.createButton(panel, Messages.getString("Button.remove"), SWT.PUSH);//$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		removeButton.setLayoutData(gd);
		removeButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection structuredSelection = (IStructuredSelection)tableViewer.getSelection();
				if (structuredSelection.size() == 1) {
					ActionModel model = (ActionModel)structuredSelection.getFirstElement();
					actionsModel.getActions().remove(model);
					tableViewer.setInput(actionsModel);
					updateModel();
					tableViewer.getTable().select(tableViewer.getTable().getItemCount() - 1);
				}
			}

		});
	}

	private void updateModel() {
		try {
			ModelUpdater.updateJpaActionsModel(((JpaEntityEditor)getEntityEditor()).getEntity(), actionsModel);
		} catch (Exception e) {
			ErrorDialog.openError(((IEditorPart)managedForm.getContainer()).getSite().getShell(),
					Messages.getString("error.problem.occurred"), e.getStackTrace().toString(), new Status(//$NON-NLS-1$
							IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage()));
		}
		setDirty(((JpaEntityEditor)getEntityEditor()).getEntity().isDirty());
	}

	private void validateTargetEntityColumn(ActionModel model) {
		Class<?> targetEntity = model.getTargetEntity();
		boolean isJpaEntity = false;
		for (Annotation annotation : targetEntity.getDeclaredAnnotations()) {
			if (annotation.annotationType().getName().equals(Entity.class.getName())) {
				isJpaEntity = true;
				break;
			}
		}
		String validationMarkerKey = MessageFormat.format("{0}-{1}", model.getUuid(), "targetEntity");//$NON-NLS-1$ //$NON-NLS-2$
		JpaEntityEditor editor = (JpaEntityEditor)getEntityEditor();
		if (isJpaEntity || targetEntity.getName().equals(void.class.getName())) {
			// remove validation marker
			managedForm.getMessageManager().removeMessage(validationMarkerKey);
			editor.removeValidationMarker(validationMarkerKey);
		} else {
			// add validation marker
			String message = MessageFormat.format("Target entity: {0} \n {1}", targetEntity.getName(),//$NON-NLS-1$
					Messages.getString("validation.selected.class.is.not.jpa.entity"));//$NON-NLS-1$
			managedForm.getMessageManager().addMessage(validationMarkerKey, message, null, IMessageProvider.ERROR);
			editor.addValidationMarker(validationMarkerKey, message);
		}
	}

}
