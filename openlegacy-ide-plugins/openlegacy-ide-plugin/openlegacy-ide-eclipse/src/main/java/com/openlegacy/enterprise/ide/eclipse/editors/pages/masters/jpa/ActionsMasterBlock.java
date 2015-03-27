/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.jpa;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaActionsModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.IOpenLegacyDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa.ActionsActionDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.jpa.ActionsMasterBlockContentProvider;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.jpa.ActionsMasterBlockLabelProvider;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.jpa.JpaEntityUtils;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import java.util.UUID;

/**
 * @author Ivan Bort
 */
public class ActionsMasterBlock extends AbstractJpaEntityMasterBlock {

	private JpaActionsModel actionsModel;

	public ActionsMasterBlock(AbstractPage page) {
		super(page);
	}

	@Override
	public void refresh() {
		actionsModel = getEntity().getActionsModel().clone();
		for (IDetailsPage page : detailsPages) {
			page.refresh();
		}
		IStructuredSelection structuredSelection = (IStructuredSelection) tableViewer.getSelection();
		tableViewer.setInput(actionsModel);
		if (structuredSelection.size() == 1) {
			tableViewerSetSelectionByLabel(((ActionModel) structuredSelection.getFirstElement()).getActionName());
		} else {
			tableViewerSetSelection(0);
		}
	}

	@Override
	protected void createMasterPart(final IManagedForm managedForm, Composite parent) {
		actionsModel = getEntity().getActionsModel().clone();

		FormToolkit toolkit = managedForm.getToolkit();
		Section section = toolkit.createSection(parent, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR);
		section.setText(Messages.getString("jpa.actions.page.masterPartName")); //$NON-NLS-1$
		section.setDescription(Messages.getString("jpa.actions.page.masterPartDesc")); //$NON-NLS-1$
		section.marginWidth = 10;
		section.marginHeight = 5;

		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		client.setLayout(layout);

		Table t = toolkit.createTable(client, SWT.NULL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 20;
		gd.widthHint = 100;
		t.setLayoutData(gd);
		toolkit.paintBordersFor(client);

		section.setClient(client);
		final SectionPart spart = new SectionPart(section);
		managedForm.addPart(spart);
		tableViewer = new TableViewer(t);
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged(spart, event.getSelection());
			}
		});
		tableViewer.setContentProvider(new ActionsMasterBlockContentProvider());
		tableViewer.setLabelProvider(new ActionsMasterBlockLabelProvider());
		tableViewer.setInput(actionsModel);

		// create Add/Remove buttons
		Composite composite = toolkit.createComposite(client);
		layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = 2;
		layout.marginHeight = 0;
		composite.setLayout(layout);
		gd = new GridData(GridData.FILL_VERTICAL);
		gd.widthHint = 100;
		composite.setLayoutData(gd);

		createAddButton(toolkit, composite);
		createRemoveButton(toolkit, composite);
	}

	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPages.add(new ActionsActionDetailsPage(this));
		for (IDetailsPage page : detailsPages) {
			detailsPart.registerPage(((IOpenLegacyDetailsPage) page).getDetailsModel(), page);
		}
	}

	@Override
	public void createContent(IManagedForm managedForm) {
		super.createContent(managedForm);
		this.sashForm.setWeights(new int[] { 30, 70 });
		tableViewerSetSelection(0);
	}

	private void reassignMasterBlockViewerInput(UUID selectUUID) {
		tableViewer.setInput(getActionsModel());
		if (selectUUID != null) {
			tableViewerSetSelectionByUUID(selectUUID);
		} else {
			tableViewerSetSelection(0);
		}
		page.getEditor().editorDirtyStateChanged();
	}

	private void createAddButton(FormToolkit toolkit, Composite parent) {
		Button btn = toolkit.createButton(parent, Messages.getString("Button.add"), SWT.PUSH);//$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		btn.setLayoutData(gd);
		btn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ActionModel model = new ActionModel("", "", true, "", "");
				actionsModel.getActions().add(model);
				JpaEntityUtils.ActionGenerator.generateJpaActionsAction(getEntity(), actionsModel);
				reassignMasterBlockViewerInput(model.getUUID());
			}

		});
	}

	private void createRemoveButton(FormToolkit toolkit, Composite parent) {
		Button removeButton = toolkit.createButton(parent, Messages.getString("Button.remove"), SWT.PUSH);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		removeButton.setLayoutData(gd);
		removeButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				if (selection.size() > 0) {
					ActionModel model = (ActionModel) selection.getFirstElement();
					actionsModel.getActions().remove(model);

					// remove all validation markers for this model
					getEntity().removeActionsSet(model.getUUID());
					page.getEntityEditor().removeValidationMarkers(model.getUUID());

					JpaEntityUtils.ActionGenerator.generateJpaActionsAction(getEntity(), actionsModel);
					int selectionIndex = tableViewer.getTable().getSelectionIndex();
					if (tableViewer.getTable().getItemCount() > 1) {
						TableItem item = tableViewer.getTable().getItem(selectionIndex > 0 ? selectionIndex - 1 : 1);
						reassignMasterBlockViewerInput(((NamedObject) item.getData()).getUUID());
					} else {
						reassignMasterBlockViewerInput(null);
					}
				}
			}
		});
	}

	public JpaActionsModel getActionsModel() {
		return actionsModel;
	}

	private void tableViewerSetSelectionByLabel(String label) {
		int itemCount = this.tableViewer.getTable().getItemCount();
		if (itemCount > 0) {
			for (int i = 0; i < itemCount; i++) {
				TableItem item = this.tableViewer.getTable().getItem(i);
				if (item.getText().equals(label)) {
					this.tableViewer.getTable().select(i);
					this.tableViewer.getTable().showSelection();
					ISelection selection = this.tableViewer.getSelection();
					this.tableViewer.setSelection(selection);
					return;
				}
			}
		}
	}

	private void tableViewerSetSelectionByUUID(UUID uuid) {
		int itemCount = this.tableViewer.getTable().getItemCount();
		if (itemCount > 0) {
			for (int i = 0; i < itemCount; i++) {
				TableItem item = this.tableViewer.getTable().getItem(i);
				if (((NamedObject) item.getData()).getUUID().equals(uuid)) {
					this.tableViewer.getTable().select(i);
					this.tableViewer.getTable().showSelection();
					ISelection selection = this.tableViewer.getSelection();
					this.tableViewer.setSelection(selection);
					return;
				}
			}
		}
	}

	private void tableViewerSetSelection(int index) {
		int itemCount = this.tableViewer.getTable().getItemCount();
		int i = itemCount > index ? index : 0;
		this.tableViewer.getTable().select(i);
		this.tableViewer.getTable().showSelection();
		ISelection selection = this.tableViewer.getSelection();
		this.tableViewer.setSelection(selection);
	}

}
