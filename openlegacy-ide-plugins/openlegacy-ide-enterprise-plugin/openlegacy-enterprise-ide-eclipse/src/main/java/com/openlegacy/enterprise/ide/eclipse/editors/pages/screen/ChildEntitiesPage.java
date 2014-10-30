package com.openlegacy.enterprise.ide.eclipse.editors.pages.screen;

import com.openlegacy.enterprise.ide.eclipse.Activator;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.ScreenEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ChildEntityAction;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.TypesSelectionDialog;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ChildEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen.ChildEntitiesPageTableContentProvider;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.ui.util.BusyIndicatorRunnableContext;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.jface.window.Window;
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

import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.Iterator;

/**
 * @author Ivan Bort
 * 
 */
public class ChildEntitiesPage extends AbstractPage {

	private final static String PAGE_ID = "org.openlegacy.enterprise.ide.eclipse.screen.pages.child.entities"; //$NON-NLS-1$

	private TableViewer tableViewer;

	/**
	 * @param editor
	 * @param id
	 * @param title
	 */
	public ChildEntitiesPage(ScreenEntityEditor editor) {
		super(editor, PAGE_ID, Messages.getString("ChildEntitiesPage.title"));//$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage#createFormContent()
	 */
	@Override
	public void createFormContent() {
		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();

		form.setImage(Activator.getDefault().getImage(Activator.IMG_TRANSPARENT));
		form.setText(Messages.getString("ChildEntitiesPage.title") + "\n ");//$NON-NLS-1$ //$NON-NLS-2$
		form.setBackgroundImage(Activator.getDefault().getImage(Activator.IMG_FORM_BG));

		TableWrapLayout layout = new TableWrapLayout();
		layout.leftMargin = 10;
		layout.rightMargin = 10;
		form.getBody().setLayout(layout);

		// create section
		Section section = toolkit.createSection(form.getBody(), Section.DESCRIPTION | ExpandableComposite.TITLE_BAR);
		// section.marginWidth = 10;
		section.setText(Messages.getString("ChildEntitiesPage.name")); //$NON-NLS-1$
		section.setDescription(Messages.getString("ChildEntitiesPage.desc")); //$NON-NLS-1$
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
		createColumns(tableViewer);

		tableViewer.setContentProvider(new ChildEntitiesPageTableContentProvider());
		tableViewer.setInput(((ScreenEntityEditor)getEntityEditor()).getEntity());
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
		if (tableViewer != null) {
			tableViewer.setInput(((ScreenEntityEditor)getEntityEditor()).getEntity());
		}
	}

	/**
	 * @param toolkit
	 * @param parent
	 */
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
				IRunnableContext context = new BusyIndicatorRunnableContext();
				IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
				TypesSelectionDialog dialog = new TypesSelectionDialog(e.widget.getDisplay().getActiveShell(), false, context,
						scope, IJavaSearchConstants.CLASS);
				dialog.setTitle(Messages.getString("Dialog.selectClass.title"));//$NON-NLS-1$
				if (dialog.open() == Window.OK) {
					IType res = (IType)dialog.getResult()[0];

					ChildEntityModel newModel = new ChildEntityModel();
					updateModel(newModel, res.getFullyQualifiedName());

					ScreenEntity entity = ((ScreenEntityEditor)getEntityEditor()).getEntity();
					entity.getChildEntities().put(newModel.getUUID(), newModel.clone());
					entity.getSortedChildEntities().add(newModel);

					entity.addAction(new ChildEntityAction(newModel.getUUID(), newModel, ActionType.ADD,
							ASTNode.FIELD_DECLARATION, newModel.getClassName(), null));

					setDirty(((ScreenEntityEditor)getEntityEditor()).getEntity().isDirty());

					tableViewer.setInput(entity);
					tableViewer.getTable().select(tableViewer.getTable().getItemCount() - 1);
				}
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
					ChildEntityModel model = (ChildEntityModel)structuredSelection.getFirstElement();
					ScreenEntity entity = ((ScreenEntityEditor)getEntityEditor()).getEntity();
					entity.getChildEntities().remove(model.getUUID());
					Iterator<ChildEntityModel> iterator = entity.getSortedChildEntities().iterator();
					while (iterator.hasNext()) {
						if (iterator.next().getUUID().equals(model.getUUID())) {
							iterator.remove();
							break;
						}
					}
					// try to remove action for new child entity
					entity.removeAction(model.getUUID(), model.getClassName());
					// add remove action
					if (model.getPreviousClassName().equals(model.getClassName())) {
						entity.addAction(new ChildEntityAction(model.getUUID(), model, ActionType.REMOVE,
								ASTNode.FIELD_DECLARATION, null, null));
					}
					removeValidationMarker(model);

					setDirty(((ScreenEntityEditor)getEntityEditor()).getEntity().isDirty());

					tableViewer.setInput(entity);
					tableViewer.getTable().select(tableViewer.getTable().getItemCount() - 1);
				}
			}

		});
	}

	/**
	 * @param viewer
	 */
	private void createColumns(TableViewer viewer) {
		// "className" column
		TableViewerColumn vcol = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tcol = vcol.getColumn();
		tcol.setText(Messages.getString("ChildEntitiesPage.col.name"));//$NON-NLS-1$
		tcol.setResizable(false);
		tcol.setWidth(250);
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				cell.setText(((ChildEntityModel)cell.getElement()).getClassName());
				validateTargetEntityColumn((ChildEntityModel)cell.getElement());
			}
		});
	}

	private void updateModel(ChildEntityModel model, String fullyQualifiedName) {
		try {
			ModelUpdater.updateChildEntityModel(model, fullyQualifiedName);
		} catch (Exception e) {
			ErrorDialog.openError(((IEditorPart)managedForm.getContainer()).getSite().getShell(),
					Messages.getString("error.problem.occurred"), e.getStackTrace().toString(), new Status(//$NON-NLS-1$
							IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage()));
		}
		setDirty(((ScreenEntityEditor)getEntityEditor()).getEntity().isDirty());
	}

	private void validateTargetEntityColumn(ChildEntityModel model) {
		Class<?> targetEntity = model.getClazz();
		boolean isScreenEntity = false;
		// NOTE: targetEntity variable can be null in case when model was initialized and user doesn't create it via editor
		if (targetEntity != null) {
			for (Annotation annotation : targetEntity.getDeclaredAnnotations()) {
				if (annotation.annotationType().getName().equals(org.openlegacy.annotations.screen.ScreenEntity.class.getName())) {
					isScreenEntity = true;
					break;
				}
			}
		}
		String validationMarkerKey = MessageFormat.format("{0}-{1}", model.getUUID(), "className");//$NON-NLS-1$ //$NON-NLS-2$
		ScreenEntityEditor editor = (ScreenEntityEditor)getEntityEditor();
		if (isScreenEntity || targetEntity == null
				|| targetEntity.getName().equals(org.openlegacy.terminal.ScreenEntity.NONE.class.getName())) {
			// remove validation marker
			removeValidationMarker(model);
		} else {
			// add validation marker
			String message = MessageFormat.format("Class name: {0} \n {1}", targetEntity.getName(),//$NON-NLS-1$
					Messages.getString("validation.selected.class.is.not.screen.entity"));//$NON-NLS-1$
			managedForm.getMessageManager().addMessage(validationMarkerKey, message, null, IMessageProvider.ERROR);
			editor.addValidationMarker(validationMarkerKey, message);
		}
	}

	private void removeValidationMarker(ChildEntityModel model) {
		String validationMarkerKey = MessageFormat.format("{0}-{1}", model.getUUID(), "className");//$NON-NLS-1$ //$NON-NLS-2$
		ScreenEntityEditor editor = (ScreenEntityEditor)getEntityEditor();
		managedForm.getMessageManager().removeMessage(validationMarkerKey);
		editor.removeValidationMarker(validationMarkerKey);
	}

}
