package com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.screen;

import com.openlegacy.enterprise.ide.eclipse.Activator;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenColumnAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenTableAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.ScreenTableActionsAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.screen.TableActionAction;
import com.openlegacy.enterprise.ide.eclipse.editors.button.SplitButton;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenColumnModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenTableModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.TableActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.IOpenLegacyDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen.TablesScreenColumnDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen.TablesScreenTableDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen.TablesTableActionDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen.TableActionsMasterBlockContentProvider;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen.TableActionsMasterBlockLabelProvider;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen.TableColumnsMasterBlockContentProvider;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen.TableColumnsMasterBlockLabelProvider;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen.TablesMasterBlockContentProvider;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen.TablesMasterBlockLabelProvider;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.screen.ScreenEntityUtils;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.openlegacy.ide.eclipse.preview.screen.ScreenPreview;
import org.openlegacy.ide.eclipse.preview.screen.SelectedObject;

import java.text.MessageFormat;
import java.util.List;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class TablesMasterBlock extends AbstractScreenEntityMasterBlock {

	private OLTableViewer columnsTableViewer;
	private OLTableViewer actionsTableViewer;

	public TablesMasterBlock(AbstractPage page) {
		super(page);
	}

	@Override
	public void refresh() {
		for (IDetailsPage page : detailsPages) {
			page.refresh();
		}
		IStructuredSelection structuredSelection = (IStructuredSelection)tableViewer.getSelection();
		tableViewer.setInput(getEntity());
		if (structuredSelection.size() == 1) {
			tableViewerSetSelection(tableViewer, 0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.MasterDetailsBlock#createMasterPart(org.eclipse.ui.forms.IManagedForm,
	 * org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createMasterPart(final IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		// create parent section for table viewers sections
		Section parentSection = toolkit.createSection(parent, ExpandableComposite.NO_TITLE);
		parentSection.marginWidth = 0;
		parentSection.marginHeight = 0;

		Composite client = toolkit.createComposite(parentSection, SWT.WRAP);

		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		gl.marginTop = gl.marginBottom = 0;

		client.setLayout(gl);
		toolkit.paintBordersFor(client);

		parentSection.setClient(client);

		createTablesSection(toolkit, parentSection);
		createColumnsSection(toolkit, parentSection);
		createActionsSection(toolkit, parentSection);

		final SectionPart spart = new SectionPart(parentSection);
		managedForm.addPart(spart);

		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				columnsTableViewer.setInput(event.getSelection());
				actionsTableViewer.setInput(event.getSelection());
				managedForm.fireSelectionChanged(spart, event.getSelection());
			}
		});
		tableViewer.setContentProvider(new TablesMasterBlockContentProvider());
		tableViewer.setLabelProvider(new TablesMasterBlockLabelProvider());
		tableViewer.setInput(getEntity());

		columnsTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged(spart, event.getSelection());
			}
		});
		columnsTableViewer.setContentProvider(new TableColumnsMasterBlockContentProvider());
		columnsTableViewer.setLabelProvider(new TableColumnsMasterBlockLabelProvider());

		actionsTableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged(spart, event.getSelection());
			}
		});
		actionsTableViewer.setContentProvider(new TableActionsMasterBlockContentProvider());
		actionsTableViewer.setLabelProvider(new TableActionsMasterBlockLabelProvider());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.MasterDetailsBlock#registerPages(org.eclipse.ui.forms.DetailsPart)
	 */
	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPages.add(new TablesScreenTableDetailsPage(this));
		detailsPages.add(new TablesScreenColumnDetailsPage(this));
		detailsPages.add(new TablesTableActionDetailsPage(this));
		for (IDetailsPage page : detailsPages) {
			detailsPart.registerPage(((IOpenLegacyDetailsPage)page).getDetailsModel(), page);
		}
	}

	@Override
	public void createContent(IManagedForm managedForm) {
		super.createContent(managedForm);
		this.sashForm.setWeights(new int[] { 40, 60 });
		int itemCount = this.tableViewer.getTable().getItemCount();
		if (itemCount > 0) {
			this.tableViewer.getTable().select(0);
			this.tableViewer.getTable().showSelection();
			ISelection selection = this.tableViewer.getSelection();
			this.tableViewer.setSelection(selection);
		}
	}

	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		// disable toolbar
	}

	@Override
	public void updateLabels() {
		tableViewer.refresh();
		columnsTableViewer.refresh();
		actionsTableViewer.refresh();
	}

	private Section createTablesSection(FormToolkit toolkit, Section parent) {
		Section section = toolkit.createSection((Composite)parent.getClient(), Section.DESCRIPTION
				| ExpandableComposite.TITLE_BAR);
		section.setText(Messages.getString("TablesPage.TablesSection.name")); //$NON-NLS-1$
		section.setDescription(Messages.getString("TablesPage.TablesSection.desc")); //$NON-NLS-1$
		section.marginWidth = 10;
		section.marginHeight = 0;
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.verticalSpan = 1;
		section.setLayoutData(gd);

		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		layout.marginHeight = 2;
		client.setLayout(layout);

		Table t = toolkit.createTable(client, SWT.NULL);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		t.setLayoutData(gd);
		toolkit.paintBordersFor(client);

		section.setClient(client);

		tableViewer = new OLTableViewer(t, null);

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

		createAddTableButton(toolkit, composite);
		createRemoveTableButton(toolkit, composite, (OLTableViewer)tableViewer);
		return section;
	}

	private Section createColumnsSection(FormToolkit toolkit, Section parent) {
		Section section = toolkit.createSection((Composite)parent.getClient(), Section.DESCRIPTION | ExpandableComposite.TWISTIE
				| ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
		section.setText(Messages.getString("TablesPage.ColumnsSection.name")); //$NON-NLS-1$
		section.setDescription(Messages.getString("TablesPage.ColumnsSection.desc")); //$NON-NLS-1$
		section.marginWidth = 10;
		section.marginHeight = 5;
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.verticalSpan = 2;
		section.setLayoutData(gd);

		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		layout.marginHeight = 2;
		client.setLayout(layout);

		Table t = toolkit.createTable(client, SWT.NULL);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		t.setLayoutData(gd);
		toolkit.paintBordersFor(client);

		section.setClient(client);

		columnsTableViewer = new OLTableViewer(t, tableViewer);

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

		SplitButton addColumnButton = new SplitButton(composite);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		addColumnButton.setLayoutData(gd);
		addColumnButton.setText(Messages.getString("Button.add"));//$NON-NLS-1$
		addColumnButton.addSelectionListener(getStringColumnSelectionListener());

		addNewStringColumnMenuItem(addColumnButton.getMenu());
		addNewIntegerColumnMenuItem(addColumnButton.getMenu());

		columnsTableViewer.setAddButton(addColumnButton);

		createRemoveColumnButton(toolkit, composite, columnsTableViewer);
		return section;
	}

	private Section createActionsSection(final FormToolkit toolkit, Section parent) {
		final Section section = toolkit.createSection((Composite)parent.getClient(), Section.DESCRIPTION
				| ExpandableComposite.TWISTIE | ExpandableComposite.TITLE_BAR | ExpandableComposite.EXPANDED);
		section.setText(Messages.getString("TablesPage.ActionsSection.name")); //$NON-NLS-1$
		section.setDescription(Messages.getString("TablesPage.ActionsSection.desc")); //$NON-NLS-1$
		section.marginWidth = 10;
		section.marginHeight = 5;
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.verticalSpan = 2;
		section.setLayoutData(gd);

		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 0;
		layout.marginHeight = 2;
		client.setLayout(layout);

		Table t = toolkit.createTable(client, SWT.NULL);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		t.setLayoutData(gd);
		toolkit.paintBordersFor(client);

		section.setClient(client);

		actionsTableViewer = new OLTableViewer(t, tableViewer);

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

		createAddTableActionButton(toolkit, composite, actionsTableViewer);
		createRemoveTableActionButton(toolkit, composite, actionsTableViewer);
		return section;
	}

	private void addNewColumn(Class<?> javaTypeClass) {
		IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
		if (selection.size() != 1) {
			ErrorDialog.openError(page.getEditorSite().getShell(), Messages.getString("error.problem.occurred"),//$NON-NLS-1$
					Messages.getString("error.add.new.column.parent.not.selected"), new Status(IStatus.ERROR,//$NON-NLS-1$
							Activator.PLUGIN_ID, Messages.getString("error.add.new.column.parent.not.selected")));//$NON-NLS-1$
		}
		ScreenTableModel tableModel = (ScreenTableModel)selection.getFirstElement();
		ScreenEntity entity = getEntity();
		ScreenColumnModel newModel = new ScreenColumnModel(tableModel);
		// note: we should add model to the entity before filling it
		entity.addScreenColumnModel(newModel);
		entity.addAction(new ScreenColumnAction(newModel.getUUID(), newModel, ActionType.ADD, ASTNode.FIELD_DECLARATION,
				Messages.getString("Column.new"), null));//$NON-NLS-1$

		newModel.setFieldName(getUniqueTableColumnName(tableModel, 1, Messages.getString("Column.new")));//$NON-NLS-1$
		newModel.setJavaTypeName(javaTypeClass.getSimpleName());
		// fill column from selection
		ScreenPreview screenPreview = (ScreenPreview)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
				ScreenPreview.ID);
		if (screenPreview != null) {
			SelectedObject selectedObject = screenPreview.getSelectedObject();
			if (selectedObject != null && selectedObject.getFieldRectangle() != null) {
				newModel.setStartColumn(selectedObject.getFieldRectangle().getColumn());
				newModel.setEndColumn(selectedObject.getFieldRectangle().getEndColumn());
			}
		}

		ScreenEntityUtils.ActionGenerator.generateScreenColumnActions(entity, newModel, false);

		tableViewer.setInput(entity);
		tableViewerSetSelectionByModelUUID(tableViewer, tableModel.getUUID());
		tableViewerSetSelectionByModelUUID(columnsTableViewer, newModel.getUUID());
		page.getEditor().editorDirtyStateChanged();
	}

	private SelectionListener getStringColumnSelectionListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewColumn(String.class);
			}

		};
	}

	/**
	 * @param menu
	 */
	private void addNewIntegerColumnMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Button.add.integer.column"));
		item.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewColumn(Integer.class);
			}

		});
	}

	/**
	 * @param menu
	 */
	private void addNewStringColumnMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Button.add.string.column"));
		item.addSelectionListener(getStringColumnSelectionListener());
		menu.setDefaultItem(item);
	}

	/**
	 * @param toolkit
	 * @param viewer
	 * @param composite
	 */
	private void createRemoveColumnButton(FormToolkit toolkit, Composite parent, OLTableViewer viewer) {
		Button removeColumnButton = toolkit.createButton(parent, Messages.getString("Button.remove"), SWT.PUSH);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		removeColumnButton.setLayoutData(gd);
		removeColumnButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection)columnsTableViewer.getSelection();
				if (selection.size() > 0) {
					ScreenColumnModel model = (ScreenColumnModel)selection.getFirstElement();
					if (model != null) {
						// remove validation markers
						for (IDetailsPage detailsPage : detailsPages) {
							if (detailsPage instanceof TablesScreenColumnDetailsPage) {
								((IOpenLegacyDetailsPage)detailsPage).removeValidationMarkers();
								// need to reset model in detaild page
								detailsPage.selectionChanged(null, new StructuredSelection());
							}
						}
						ScreenTableModel tableModel = (ScreenTableModel)model.getParent();
						ScreenEntity entity = getEntity();
						entity.removeScreenColumnModel(model);

						// try to remove actions
						entity.removeActionsSet(model.getUUID());

						if (model.isInitialized()) {
							entity.addAction(new ScreenColumnAction(model.getUUID(), model, ActionType.REMOVE,
									ASTNode.FIELD_DECLARATION, null, null));
						}

						tableViewer.setInput(entity);
						tableViewerSetSelectionByModelUUID(tableViewer, tableModel.getUUID());
						tableViewerSetSelection(columnsTableViewer, 0);
						page.getEditor().editorDirtyStateChanged();
					}
				}

			}

		});
		viewer.setRemoveButton(removeColumnButton);
	}

	/**
	 * @param toolkit
	 * @param viewer
	 * @param composite
	 */
	private void createAddTableActionButton(FormToolkit toolkit, Composite parent, OLTableViewer viewer) {
		Button addActionButton = toolkit.createButton(parent, Messages.getString("Button.add"), SWT.PUSH);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		addActionButton.setLayoutData(gd);
		addActionButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
				if (selection.size() != 1) {
					ErrorDialog.openError(page.getEditorSite().getShell(), Messages.getString("error.problem.occurred"),//$NON-NLS-1$
							Messages.getString("error.add.new.table.action.parent.not.selected"), new Status(IStatus.ERROR,//$NON-NLS-1$
									Activator.PLUGIN_ID, Messages.getString("error.add.new.table.action.parent.not.selected")));//$NON-NLS-1$
				}
				ScreenTableModel tableModel = (ScreenTableModel)selection.getFirstElement();
				ScreenEntity entity = getEntity();
				TableActionModel newModel = new TableActionModel(tableModel);
				newModel.setActionValue("0");//$NON-NLS-1$

				if (tableModel.getActions().isEmpty()) {
					entity.addAction(new ScreenTableActionsAction(newModel.getUUID(), newModel, ActionType.ADD,
							ASTNode.NORMAL_ANNOTATION, null, null));
				}
				entity.addTableActionModel(newModel);
				entity.addAction(new TableActionAction(newModel.getUUID(), newModel, ActionType.ADD, ASTNode.NORMAL_ANNOTATION
						| ASTNode.MODIFIER, Messages.getString("TableAction.new"), null));//$NON-NLS-1$

				tableViewer.setInput(entity);
				tableViewerSetSelectionByModelUUID(tableViewer, tableModel.getUUID());
				tableViewerSetSelectionByModelUUID(actionsTableViewer, newModel.getUUID());
				page.getEditor().editorDirtyStateChanged();
			}

		});
		viewer.setAddButton(addActionButton);
	}

	/**
	 * @param toolkit
	 * @param viewer
	 * @param composite
	 */
	private void createRemoveTableActionButton(FormToolkit toolkit, Composite parent, OLTableViewer viewer) {
		Button removeActionButton = toolkit.createButton(parent, Messages.getString("Button.remove"), SWT.PUSH);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		removeActionButton.setLayoutData(gd);
		removeActionButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection)actionsTableViewer.getSelection();
				if (selection.size() > 0) {
					TableActionModel model = (TableActionModel)selection.getFirstElement();
					if (model != null) {
						// remove validation markers
						for (IDetailsPage detailsPage : detailsPages) {
							if (detailsPage instanceof TablesTableActionDetailsPage) {
								((IOpenLegacyDetailsPage)detailsPage).removeValidationMarkers();
								// need to reset model in detaild page
								detailsPage.selectionChanged(null, new StructuredSelection());
							}
						}
						ScreenTableModel tableModel = (ScreenTableModel)model.getParent();
						ScreenEntity entity = getEntity();
						entity.removeTableActionModel(model);

						// try to remove action for new action model
						entity.removeActionsSet(model.getUUID());

						if (model.isInitialized()) {
							entity.addAction(new TableActionAction(model.getUUID(), model, ActionType.REMOVE,
									ASTNode.NORMAL_ANNOTATION, null, null));
						}

						tableViewer.setInput(entity);
						tableViewerSetSelectionByModelUUID(tableViewer, tableModel.getUUID());
						tableViewerSetSelection(actionsTableViewer, 0);
						page.getEditor().editorDirtyStateChanged();
					}
				}
			}
		});
		viewer.setRemoveButton(removeActionButton);
	}

	private void tableViewerSetSelection(TableViewer tableViewer, int index) {
		int itemCount = tableViewer.getTable().getItemCount();
		if (itemCount > 0) {
			tableViewer.getTable().select(index);
			tableViewer.getTable().showSelection();
			ISelection selection = tableViewer.getSelection();
			tableViewer.setSelection(selection);
		}
	}

	private void tableViewerSetSelectionByModelUUID(TableViewer tableViewer, UUID uuid) {
		int itemCount = tableViewer.getTable().getItemCount();
		if (itemCount > 0) {
			for (int i = 0; i < itemCount; i++) {
				TableItem item = tableViewer.getTable().getItem(i);
				if (((NamedObject)item.getData()).getUUID().equals(uuid)) {
					tableViewer.getTable().select(i);
					tableViewer.getTable().showSelection();
					tableViewer.setSelection(tableViewer.getSelection());
					return;
				}
			}
		}
	}

	/**
	 * @param toolkit
	 * @param parent
	 */
	private void createAddTableButton(FormToolkit toolkit, Composite parent) {
		Button btn = toolkit.createButton(parent, Messages.getString("Button.add"), SWT.PUSH);//$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		btn.setLayoutData(gd);
		btn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ScreenEntity entity = getEntity();

				// we need to create new table with unique name
				String newTableName = MessageFormat.format("{0}{1}", entity.getEntityModel().getClassName(),//$NON-NLS-1$
						Messages.getString("ScreenTable.new"));//$NON-NLS-1$
				newTableName = getUniqueTableClassName(entity, 1, newTableName);

				ScreenTableModel newModel = new ScreenTableModel(newTableName);

				entity.addScreenTableModel(newModel);
				// fill after adding to entity
				fillNewModel(newModel);

				entity.addAction(new ScreenTableAction(newModel.getUUID(), newModel, ActionType.ADD, ASTNode.TYPE_DECLARATION,
						newModel.getClassName(), null));

				// generate actions
				ScreenEntityUtils.ActionGenerator.generateScreenTableActions(entity, newModel);

				tableViewer.setInput(entity);
				tableViewerSetSelectionByModelUUID(tableViewer, newModel.getUUID());
				page.getEditor().editorDirtyStateChanged();
			}

		});
	}

	/**
	 * @param toolkit
	 * @param parent
	 * @param tableViewer
	 */
	private void createRemoveTableButton(FormToolkit toolkit, Composite parent, OLTableViewer viewer) {
		Button removeTableButton = toolkit.createButton(parent, Messages.getString("Button.remove"), SWT.PUSH);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		removeTableButton.setLayoutData(gd);
		removeTableButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection)tableViewer.getSelection();
				if (selection.size() > 0) {
					ScreenTableModel model = (ScreenTableModel)selection.getFirstElement();
					if (model != null) {
						// remove validation markers
						for (IDetailsPage detailsPage : detailsPages) {
							if (detailsPage instanceof TablesScreenTableDetailsPage) {
								((IOpenLegacyDetailsPage)detailsPage).removeValidationMarkers();
								// need to reset model in detaild page
								detailsPage.selectionChanged(null, new StructuredSelection());
							}
						}

						ScreenEntity entity = getEntity();
						entity.removeScreenTableModel(model);

						// try to remove action for new action model
						entity.removeActionsSet(model.getUUID());

						if (model.isInitialized()) {
							entity.addAction(new ScreenTableAction(model.getUUID(), model, ActionType.REMOVE,
									ASTNode.TYPE_DECLARATION, null, null));
						}

						tableViewer.setInput(entity);
						tableViewerSetSelection(tableViewer, 0);
						page.getEditor().editorDirtyStateChanged();
					}
				}
			}
		});
		viewer.setRemoveButton(removeTableButton);
	}

	private String getUniqueTableClassName(ScreenEntity entity, int index, String baseClassName) {
		List<ScreenTableModel> sortedTables = entity.getSortedTables();
		String futureName = MessageFormat.format("{0}{1}", baseClassName, (index == 1 ? "" : index));
		for (ScreenTableModel tableModel : sortedTables) {
			if (tableModel.getClassName().equals(futureName)) {
				return getUniqueTableClassName(entity, ++index, baseClassName);
			}
		}
		return futureName;
	}

	private String getUniqueTableColumnName(ScreenTableModel tableModel, int index, String baseColumnName) {
		String futureName = MessageFormat.format("{0}{1}", baseColumnName, (index == 1 ? "" : index));
		List<ScreenColumnModel> sortedColumns = tableModel.getSortedColumns();
		for (ScreenColumnModel columnModel : sortedColumns) {
			if (columnModel.getFieldName().equals(futureName)) {
				return getUniqueTableColumnName(tableModel, ++index, baseColumnName);
			}
		}
		return futureName;
	}

	private class OLTableViewer extends TableViewer {

		private TableViewer master;
		private Button addButton;
		private Button removeButton;

		public OLTableViewer(Table table, TableViewer master) {
			super(table);
			this.master = master;
		}

		@Override
		protected void inputChanged(Object input, Object oldInput) {
			super.inputChanged(input, oldInput);
			if (master != null && addButton != null) {
				IStructuredSelection selection = (IStructuredSelection)master.getSelection();
				addButton.setEnabled(!selection.isEmpty());
			}
			if (removeButton != null) {
				removeButton.setEnabled(getTable().getItemCount() > 0);
			}
		}

		public void setAddButton(Button addButton) {
			this.addButton = addButton;
		}

		public void setRemoveButton(Button removeButton) {
			this.removeButton = removeButton;
		}

	}

	private static void fillNewModel(ScreenTableModel model) {
		ScreenPreview screenPreview = (ScreenPreview)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
				ScreenPreview.ID);
		if (screenPreview != null) {
			SelectedObject selectedObject = screenPreview.getSelectedObject();
			if ((selectedObject != null) && (selectedObject.getFieldRectangle() != null)) {
				model.setStartRow(selectedObject.getFieldRectangle().getRow());
				model.setEndRow(selectedObject.getFieldRectangle().getEndRow());
			}
		}
	}

}
