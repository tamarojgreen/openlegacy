package com.openlegacy.enterprise.ide.eclipse.editors.pages.rpc;

import com.openlegacy.enterprise.ide.eclipse.Activator;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.RpcEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcActionsModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc.ActionsComboBoxCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc.ActionsDialogCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc.ActionsTextCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.rpc.ActionsPageTableContentProvider;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
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
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.rpc.generators.support.RpcAnnotationConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * * @author Ivan Bort
 * 
 */
public class ActionsPage extends AbstractPage {

	private final static String PAGE_ID = "org.openlegacy.enterprise.ide.eclipse.rpc.pages.actions"; //$NON-NLS-1$

	private TableViewer tableViewer;

	private RpcActionsModel actionsModel;

	public ActionsPage(RpcEntityEditor editor) {
		super(editor, PAGE_ID, Messages.getString("rpc.actions.page.title"));//$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage#createFormContent()
	 */
	@Override
	public void createFormContent() {
		actionsModel = ((RpcEntityEditor)getEntityEditor()).getEntity().getActionsModel().clone();

		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();

		form.setImage(Activator.getDefault().getImage(Activator.IMG_TRANSPARENT));
		form.setText(Messages.getString("rpc.actions.page.title"));//$NON-NLS-1$
		form.setBackgroundImage(Activator.getDefault().getImage(Activator.IMG_FORM_BG));

		TableWrapLayout layout = new TableWrapLayout();
		layout.leftMargin = 10;
		layout.rightMargin = 10;
		form.getBody().setLayout(layout);

		// create section
		Section section = toolkit.createSection(form.getBody(), Section.DESCRIPTION | ExpandableComposite.TITLE_BAR);
		// section.marginWidth = 10;
		section.setText(Messages.getString("rpc.actions.page.section.text")); //$NON-NLS-1$
		section.setDescription(Messages.getString("rpc.actions.page.section.desc")); //$NON-NLS-1$
		TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
		td.grabHorizontal = true;
		section.setLayoutData(td);
		// create composite
		Composite client = toolkit.createComposite(section);
		GridLayout gl = new GridLayout();
		gl.marginWidth = gl.marginHeight = 0;
		gl.numColumns = 2;
		client.setLayout(gl);

		Table t = toolkit.createTable(client, SWT.FULL_SELECTION);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.heightHint = 300;
		t.setLayoutData(gd);
		t.setHeaderVisible(true);
		t.setLinesVisible(true);
		// add buttons
		addPanelWithButtons(toolkit, client);

		// create table
		tableViewer = new TableViewer(t);
		createColumns(tableViewer, ((RpcEntityEditor)getEntityEditor()).getEntity());

		tableViewer.setContentProvider(new ActionsPageTableContentProvider());
		tableViewer.setInput(actionsModel);
		toolkit.paintBordersFor(client);
		// set client to section
		section.setClient(client);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage#refresh()
	 */
	@Override
	public void refresh() {
		actionsModel = ((RpcEntityEditor)getEntityEditor()).getEntity().getActionsModel().clone();
		if (tableViewer != null) {
			tableViewer.setInput(actionsModel);
		}
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
				actionsModel.getActions().add(new ActionModel("", "", "", "", true));//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
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

	private void createColumns(TableViewer viewer, RpcEntity entity) {
		// "action" column
		TableViewerColumn vcol = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tcol = vcol.getColumn();
		tcol.setText(Messages.getString("rpc.actions.page.col.action.label"));//$NON-NLS-1$
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
		tcol.setText(Messages.getString("rpc.actions.page.col.display.name.label"));//$NON-NLS-1$
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
		tcol.setText(Messages.getString("rpc.actions.page.col.alias.label"));//$NON-NLS-1$
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

		// "path" column
		vcol = new TableViewerColumn(viewer, SWT.FILL);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("rpc.actions.page.col.path.label"));//$NON-NLS-1$
		tcol.setResizable(false);
		tcol.setWidth(300);

		vcol.setEditingSupport(new ActionsTextCellEditingSupport(viewer, RpcAnnotationConstants.PATH));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				RpcActionDefinition action = (RpcActionDefinition)cell.getElement();
				cell.setText(action.getProgramPath());
				updateModel();
			}
		});

		// "is global" column
		vcol = new TableViewerColumn(viewer, SWT.FILL);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("rpc.actions.page.col.global.label"));//$NON-NLS-1$
		tcol.setResizable(false);
		tcol.setWidth(100);

		List<String> items = new ArrayList<String>();
		items.add(Boolean.FALSE.toString().toLowerCase());
		items.add(Boolean.TRUE.toString().toLowerCase());

		vcol.setEditingSupport(new ActionsComboBoxCellEditingSupport(viewer, AnnotationConstants.GLOBAL, items));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				RpcActionDefinition action = (RpcActionDefinition)cell.getElement();
				cell.setText(String.valueOf(action.isGlobal()));
				updateModel();
			}
		});
	}

	private void updateModel() {
		try {
			ModelUpdater.updateRpcActionsModel(((RpcEntityEditor)getEntityEditor()).getEntity(), actionsModel);
		} catch (Exception e) {
			ErrorDialog.openError(((IEditorPart)managedForm.getContainer()).getSite().getShell(),
					Messages.getString("error.problem.occurred"), e.getStackTrace().toString(), new Status(//$NON-NLS-1$
							IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage()));
		}
		setDirty(((RpcEntityEditor)getEntityEditor()).getEntity().isDirty());
	}

}
