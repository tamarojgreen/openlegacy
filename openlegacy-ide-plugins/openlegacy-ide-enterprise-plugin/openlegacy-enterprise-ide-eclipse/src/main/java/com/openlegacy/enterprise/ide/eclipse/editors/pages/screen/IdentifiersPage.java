package com.openlegacy.enterprise.ide.eclipse.editors.pages.screen;

import com.openlegacy.enterprise.ide.eclipse.Activator;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.ScreenEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.IdentifierModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenIdentifiersModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.AttributesComboBoxCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.IdentifiersCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen.IdentifiersPageTableContentProvider;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.ide.eclipse.preview.screen.FieldRectangle;
import org.openlegacy.ide.eclipse.preview.screen.ScreenPreview;
import org.openlegacy.ide.eclipse.preview.screen.SelectedObject;
import org.openlegacy.terminal.FieldAttributeType;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class IdentifiersPage extends AbstractPage {

	private final static String PAGE_ID = "org.openlegacy.enterprise.ide.eclipse.screen.pages.identifiers"; //$NON-NLS-1$

	private TableViewer tableViewer;

	private ScreenIdentifiersModel identifiersModel;

	public IdentifiersPage(ScreenEntityEditor editor) {
		super(editor, PAGE_ID, Messages.getString("IdentifiersPage.title"));//$NON-NLS-1$
	}

	@Override
	public void createFormContent() {
		identifiersModel = ((ScreenEntityEditor)getEntityEditor()).getEntity().getIdentifiersModel().clone();

		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();

		form.setImage(Activator.getDefault().getImage(Activator.IMG_TRANSPARENT));
		form.setText(Messages.getString("IdentifiersPage.title"));//$NON-NLS-1$
		form.setBackgroundImage(Activator.getDefault().getImage(Activator.IMG_FORM_BG));

		TableWrapLayout layout = new TableWrapLayout();
		layout.leftMargin = 10;
		layout.rightMargin = 10;
		form.getBody().setLayout(layout);

		// create section
		Section section = toolkit.createSection(form.getBody(), Section.DESCRIPTION | ExpandableComposite.TITLE_BAR);
		// section.marginWidth = 10;
		section.setText(Messages.getString("IdentifiersPageSection.name")); //$NON-NLS-1$
		section.setDescription(Messages.getString("IdentifiersPageSection.desc")); //$NON-NLS-1$
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
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				if (selection.size() == 1) {
					setScreenPreviewDrawingRectangle((IdentifierModel)selection.getFirstElement());
				}
			}
		});
		createColumns(tableViewer, ((ScreenEntityEditor)getEntityEditor()).getEntity());

		tableViewer.setContentProvider(new IdentifiersPageTableContentProvider());
		tableViewer.setInput(identifiersModel);
		toolkit.paintBordersFor(client);
		// set client to section
		section.setClient(client);
	}

	@Override
	public void refresh() {
		identifiersModel = ((ScreenEntityEditor)getEntityEditor()).getEntity().getIdentifiersModel().clone();
		if (tableViewer != null) {
			tableViewer.setInput(identifiersModel);
		}
	}

	@Override
	public void revalidatePage(String key) {
		if (identifiersModel == null) {
			return;
		}
		for (IdentifierModel identifierModel : identifiersModel.getIdentifiers()) {
			validateModel(identifierModel);
		}

	}

	@Override
	public void performSubscription() {
		ScreenEntityEditor editor = (ScreenEntityEditor)getEntityEditor();

		GeneralPage generalPage = (GeneralPage)editor.findPage(GeneralPage.PAGE_ID);
		generalPage.addSubscriber(ScreenAnnotationConstants.COLUMNS, this);
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
				IdentifierModel identifier = new IdentifierModel(0, 0, "");//$NON-NLS-1$
				fillNewIdentifier(identifier);
				identifiersModel.getIdentifiers().add(identifier);
				tableViewer.setInput(identifiersModel);
				updateModel();
				tableViewer.getTable().select(tableViewer.getTable().getItemCount() - 1);
				validateModel(identifier);
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
					IdentifierModel model = (IdentifierModel)structuredSelection.getFirstElement();
					identifiersModel.getIdentifiers().remove(model);
					tableViewer.setInput(identifiersModel);
					updateModel();
					tableViewer.getTable().select(tableViewer.getTable().getItemCount() - 1);
				}
			}

		});
	}

	private void createColumns(TableViewer viewer, ScreenEntity entity) {
		// "row" column
		TableViewerColumn vcol = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tcol = vcol.getColumn();
		tcol.setText(Messages.getString("IdentifiersPage.col.row"));//$NON-NLS-1$
		tcol.setResizable(true);
		tcol.setWidth(50);

		vcol.setEditingSupport(new IdentifiersCellEditingSupport(viewer, ScreenAnnotationConstants.ROW, entity));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				IdentifierModel identifier = (IdentifierModel)cell.getElement();
				cell.setText(Integer.toString(identifier.getRow()));
				updateModel();
			}
		});

		// "column" column
		vcol = new TableViewerColumn(viewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("IdentifiersPage.col.column"));//$NON-NLS-1$
		tcol.setResizable(true);
		tcol.setWidth(100);

		vcol.setEditingSupport(new IdentifiersCellEditingSupport(viewer, ScreenAnnotationConstants.COLUMN, entity));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				IdentifierModel identifier = (IdentifierModel)cell.getElement();
				cell.setText(Integer.toString(identifier.getColumn()));
				updateModel();
				validateModel(identifier);
			}
		});

		// "value" column
		vcol = new TableViewerColumn(viewer, SWT.FILL);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("IdentifiersPage.col.value"));//$NON-NLS-1$
		tcol.setResizable(true);
		tcol.setWidth(200);

		vcol.setEditingSupport(new IdentifiersCellEditingSupport(viewer, ScreenAnnotationConstants.VALUE, entity));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				IdentifierModel identifier = (IdentifierModel)cell.getElement();
				cell.setText(identifier.getText());
				updateModel();
				validateModel(identifier);
			}
		});

		// "attribute" column
		vcol = new TableViewerColumn(viewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("IdentifiersPage.col.attribute"));//$NON-NLS-1$
		tcol.setResizable(true);
		tcol.setWidth(100);

		List<String> items = new ArrayList<String>();
		items.add(FieldAttributeType.Value.toString());
		items.add(FieldAttributeType.Editable.toString());
		items.add(FieldAttributeType.Color.toString());
		vcol.setEditingSupport(new AttributesComboBoxCellEditingSupport(viewer, ScreenAnnotationConstants.ATTRIBUTE, items));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				IdentifierModel identifier = (IdentifierModel)cell.getElement();
				cell.setText(identifier.getAttribute() != null
						&& !identifier.getAttribute().equals(identifier.getDefaultAttribute()) ? identifier.getAttribute().toString()
						: "");
				updateModel();
			}
		});
	}

	private void updateModel() {
		try {
			ModelUpdater.updateScreenIdentifiersModel(((ScreenEntityEditor)getEntityEditor()).getEntity(), identifiersModel);
		} catch (Exception e) {
			ErrorDialog.openError(((IEditorPart)managedForm.getContainer()).getSite().getShell(),
					Messages.getString("error.problem.occurred"), e.getStackTrace().toString(), new Status(//$NON-NLS-1$
							IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage()));
		}
		setDirty(((ScreenEntityEditor)getEntityEditor()).getEntity().isDirty());
	}

	private static void setScreenPreviewDrawingRectangle(IdentifierModel model) {
		ScreenPreview screenPreview = (ScreenPreview)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
				ScreenPreview.ID);
		if (screenPreview != null) {
			screenPreview.addFieldRectangle(new FieldRectangle(model.getRow(), 0, model.getColumn(), 0, model.getText()),
					SWT.COLOR_YELLOW, true);
		}
	}

	private static void fillNewIdentifier(IdentifierModel model) {
		ScreenPreview screenPreview = (ScreenPreview)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
				ScreenPreview.ID);
		if (screenPreview != null) {
			SelectedObject selectedObject = screenPreview.getSelectedObject();
			if ((selectedObject != null) && !selectedObject.isEditable()) {
				if (selectedObject.getFieldRectangle() != null) {
					model.setRow(selectedObject.getFieldRectangle().getRow());
					model.setColumn(selectedObject.getFieldRectangle().getColumn());
					model.setText(selectedObject.getFieldRectangle().getValue());
				}
			}
		}
	}

	private void validateModel(IdentifierModel model) {
		if (managedForm == null) {
			// NOTE: managedForm can equal to NULL if user not visited Identifiers page yet
			return;
		}
		String validationMarkerKey = MessageFormat.format("{0}-{1}", model.getUuid(), "screenBounds");//$NON-NLS-1$ //$NON-NLS-2$
		ScreenEntityEditor editor = (ScreenEntityEditor)getEntityEditor();

		GeneralPage generalPage = (GeneralPage)editor.findPage(GeneralPage.PAGE_ID);
		ScreenEntityModel entityModel = (ScreenEntityModel)generalPage.getEditableNamedObject(ScreenEntityModel.class);

		if (model.getColumn() + model.getText().length() > entityModel.getColumns()) {
			// add validation marker
			managedForm.getMessageManager().addMessage(validationMarkerKey,
					Messages.getString("validation.identifier.exceeds.screen.bounds"), null, IMessageProvider.ERROR);//$NON-NLS-1$
			editor.addValidationMarker(validationMarkerKey, Messages.getString("validation.identifier.exceeds.screen.bounds"));//$NON-NLS-1$
		} else {
			// remove validation markers
			managedForm.getMessageManager().removeMessage(validationMarkerKey);
			editor.removeValidationMarker(validationMarkerKey);
		}
	}
}
