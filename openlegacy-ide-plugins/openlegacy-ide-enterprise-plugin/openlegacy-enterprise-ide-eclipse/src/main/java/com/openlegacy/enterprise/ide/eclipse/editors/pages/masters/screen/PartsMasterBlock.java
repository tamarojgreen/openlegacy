package com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.screen;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.button.DropDownButton;
import com.openlegacy.enterprise.ide.eclipse.editors.button.SplitButton;
import com.openlegacy.enterprise.ide.eclipse.editors.models.AbstractEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldValuesModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenPartModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.IOpenLegacyDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen.FieldsScreenBooleanFieldDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen.FieldsScreenDateFieldDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen.FieldsScreenEnumFieldDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen.FieldsScreenFieldValuesDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen.FieldsScreenIntegerFieldDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen.FieldsScreenStringFieldDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen.PartsScreenPartDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.dnd.screen.FieldsTreeViewerDragListener;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.dnd.screen.FieldsTreeViewerDropHelper;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.dnd.screen.FieldsTreeViewerDropListener;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.screen.helpers.FieldsConverter;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.screen.helpers.FieldsCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.screen.helpers.IPartsMasterBlockCallback;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen.PartsMasterBlockContentProvider;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen.PartsMasterBlockLabelProvider;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.openlegacy.ide.eclipse.preview.screen.FieldRectangle;
import org.openlegacy.ide.eclipse.preview.screen.ScreenPreview;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.SimpleTerminalPosition;

import java.util.Iterator;
import java.util.UUID;

/**
 * 
 * @author Ivan Bort
 * 
 */
public class PartsMasterBlock extends AbstractScreenEntityMasterBlock implements IPartsMasterBlockCallback {

	private TreeViewer treeViewer;
	private FieldsCreator creator;
	private FieldsConverter converter;

	public PartsMasterBlock(AbstractPage page) {
		super(page);
		creator = new FieldsCreator(this);
		converter = new FieldsConverter(this);
	}

	@Override
	protected void createMasterPart(final IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		Section section = toolkit.createSection(parent, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR);
		section.setText(Messages.getString("PartsMasterBlock.masterPartName")); //$NON-NLS-1$
		section.setDescription(Messages.getString("PartsMasterBlock.masterPartDesc")); //$NON-NLS-1$
		section.marginWidth = 10;
		section.marginHeight = 5;

		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		client.setLayout(layout);
		GridData gridData = new GridData();
		gridData.widthHint = 200;
		client.setLayoutData(gridData);

		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.heightHint = 20;
		gd.widthHint = 100;
		treeViewer = new TreeViewer(client, SWT.MULTI | SWT.BORDER);
		treeViewer.getTree().setLayoutData(gd);
		toolkit.paintBordersFor(treeViewer.getTree());

		section.setClient(client);
		final SectionPart spart = new SectionPart(section);
		managedForm.addPart(spart);

		treeViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.getMessageManager().removeAllMessages();
				if (((IStructuredSelection)event.getSelection()).size() == 1) {
					managedForm.fireSelectionChanged(spart, event.getSelection());
				} else {
					managedForm.fireSelectionChanged(spart, null);
				}
				setScreenPreviewDrawingRectangles((IStructuredSelection)event.getSelection());
			}
		});
		Transfer[] transfers = new Transfer[] { LocalSelectionTransfer.getTransfer() };
		FieldsTreeViewerDropListener dropListener = new FieldsTreeViewerDropListener(treeViewer, page);
		treeViewer.addDragSupport(DND.DROP_MOVE, transfers, new FieldsTreeViewerDragListener(treeViewer, dropListener));
		treeViewer.addDropSupport(DND.DROP_MOVE, transfers, dropListener);
		treeViewer.setContentProvider(new PartsMasterBlockContentProvider());
		treeViewer.setLabelProvider(new PartsMasterBlockLabelProvider());
		treeViewer.setInput(getEntity());
		treeViewer.expandAll();
		// add context menu
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {

			@Override
			public void menuAboutToShow(IMenuManager manager) {
				if (treeViewer.getSelection().isEmpty()) {
					return;
				}
				manager.add(getCreateActionOfScreenPartBasedOnSelection());
			}
		});
		Menu contextMenu = menuMgr.createContextMenu(treeViewer.getTree());
		treeViewer.getTree().setMenu(contextMenu);

		// create Add/Remove buttons
		Composite composite = toolkit.createComposite(client);
		layout = new GridLayout();
		layout.numColumns = 1;
		layout.marginWidth = 2;
		layout.marginHeight = 0;
		composite.setLayout(layout);
		gridData = new GridData(GridData.FILL_VERTICAL);
		gridData.widthHint = 100;
		composite.setLayoutData(gridData);

		SplitButton splitButton = new SplitButton(composite);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		splitButton.setLayoutData(gd);
		splitButton.setText(Messages.getString("Button.add"));//$NON-NLS-1$

		splitButton.addSelectionListener(getMenuItemSelectionListener());

		addNewStringFieldMenuItem(splitButton.getMenu());
		addNewBooleanFieldMenuItem(splitButton.getMenu());
		addNewDateFieldMenuItem(splitButton.getMenu());
		addNewIntegerFieldMenuItem(splitButton.getMenu());
		addNewEnumFieldMenuItem(splitButton.getMenu());
		addNewValuesFieldMenuItem(splitButton.getMenu());
		addNewScreenPartMenuItem(splitButton.getMenu());

		createRemoveButton(toolkit, composite);

		createConvertButton(composite);
	}

	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPages.add(new FieldsScreenStringFieldDetailsPage(this));
		detailsPages.add(new FieldsScreenBooleanFieldDetailsPage(this));
		detailsPages.add(new FieldsScreenDateFieldDetailsPage(this));
		detailsPages.add(new FieldsScreenEnumFieldDetailsPage(this));
		detailsPages.add(new FieldsScreenIntegerFieldDetailsPage(this));
		detailsPages.add(new FieldsScreenFieldValuesDetailsPage(this));
		detailsPages.add(new PartsScreenPartDetailsPage(this));
		for (IDetailsPage page : detailsPages) {
			detailsPart.registerPage(((IOpenLegacyDetailsPage)page).getDetailsModel(), page);
		}
	}

	@Override
	public void createContent(IManagedForm managedForm) {
		super.createContent(managedForm);
		this.sashForm.setWeights(new int[] { 30, 70 });
		treeViewerSetSelection(0);
	}

	@Override
	public void refresh() {
		for (IDetailsPage page : detailsPages) {
			page.refresh();
		}
		treeViewer.setInput(getEntity());
		treeViewer.expandAll();
		treeViewerSetSelection(0);
	}

	@Override
	public void updateLabels() {
		treeViewer.refresh();
	}

	/**
	 * @param menu
	 */
	private void addNewBooleanFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Button.add.boolean.field"));//$NON-NLS-1$
		item.addSelectionListener(new CreateMenuItemSelectionAdapter(ScreenBooleanFieldModel.class));
	}

	/**
	 * @param menu
	 */
	private void addNewDateFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Button.add.date.field"));//$NON-NLS-1$
		item.addSelectionListener(new CreateMenuItemSelectionAdapter(ScreenDateFieldModel.class));
	}

	/**
	 * @param menu
	 */
	private void addNewEnumFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Button.add.enum.field"));//$NON-NLS-1$
		item.addSelectionListener(new CreateMenuItemSelectionAdapter(ScreenEnumFieldModel.class));
	}

	/**
	 * @param menu
	 */
	private void addNewIntegerFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Button.add.integer.field"));//$NON-NLS-1$
		item.addSelectionListener(new CreateMenuItemSelectionAdapter(ScreenIntegerFieldModel.class));
	}

	/**
	 * @param menu
	 */
	private void addNewStringFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Button.add.string.field"));
		item.addSelectionListener(getMenuItemSelectionListener());
		menu.setDefaultItem(item);
	}

	/**
	 * @param menu
	 */
	private void addNewValuesFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Button.add.values.field"));//$NON-NLS-1$
		item.addSelectionListener(new CreateMenuItemSelectionAdapter(ScreenFieldValuesModel.class));
	}

	/**
	 * @param menu
	 */
	private void addNewScreenPartMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH | SWT.ITALIC);
		item.setText(Messages.getString("Button.add.screen.part"));//$NON-NLS-1$
		item.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				ScreenPartModel newModel = creator.createNewScreenPartModel();
				reassignMasterBlockViewerInput(newModel.getUUID());
			}

		});
	}

	private SelectionListener getMenuItemSelectionListener() {
		return new CreateMenuItemSelectionAdapter(ScreenFieldModel.class);
	}

	/**
	 * @param toolkit
	 * @param composite
	 */
	private void createRemoveButton(FormToolkit toolkit, Composite parent) {
		Button btnRemove = toolkit.createButton(parent, Messages.getString("Button.remove"), SWT.PUSH);//$NON-NLS-1$
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		btnRemove.setLayoutData(gd);
		btnRemove.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				creator.removeSelectedElements();
			}

		});
	}

	@Override
	public void reassignMasterBlockViewerInput(UUID selectUUID) {
		treeViewer.setInput(getEntity());
		treeViewer.expandAll();
		if (selectUUID != null) {
			treeViewerSetSelectionByModelUUID(selectUUID);
		} else {
			treeViewerSetSelection(0);
		}
		page.getEditor().editorDirtyStateChanged();
	}

	private void setViewerSelection(TreeItem item) {
		treeViewer.getTree().setSelection(item);
		treeViewer.getTree().showSelection();
		treeViewer.setSelection(treeViewer.getSelection());
	}

	private void treeViewerSetSelection(int index) {
		treeViewer.expandAll();
		int itemCount = treeViewer.getTree().getItemCount();
		if ((itemCount > 0) && (index <= itemCount)) {
			setViewerSelection(treeViewer.getTree().getItem(index));
		}
	}

	protected void treeViewerSetSelectionByModelUUID(UUID uuid) {
		int itemCount = treeViewer.getTree().getItemCount();
		if (itemCount > 0) {
			for (int i = 0; i < itemCount; i++) {
				TreeItem item = treeViewer.getTree().getItem(i);
				if (((NamedObject)item.getData()).getUUID().equals(uuid)) {
					setViewerSelection(item);
					return;
				}
				int count = item.getItemCount();
				if (count > 0) {
					for (int j = 0; j < count; j++) {
						TreeItem item2 = item.getItem(j);
						if (((NamedObject)item2.getData()).getUUID().equals(uuid)) {
							setViewerSelection(item2);
							return;
						}
					}
				}
			}
		}
	}

	private IAction getCreateActionOfScreenPartBasedOnSelection() {
		return new Action(Messages.getString("context.menu.action.create.screen.part")) {

			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				IStructuredSelection selection = (IStructuredSelection)treeViewer.getSelection();
				if (selection.isEmpty()) {
					return;
				}
				ScreenPartModel newPartModel = creator.createNewScreenPartModel();
				Iterator<NamedObject> iterator = selection.iterator();
				while (iterator.hasNext()) {
					NamedObject namedObject = iterator.next();
					// emulate drop
					FieldsTreeViewerDropHelper.performDrop(getEntity(), namedObject, newPartModel);
				}
				reassignMasterBlockViewerInput(newPartModel.getUUID());
			}

		};
	}

	@SuppressWarnings("unchecked")
	private static void setScreenPreviewDrawingRectangles(IStructuredSelection selection) {
		ScreenPreview screenPreview = (ScreenPreview)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
				ScreenPreview.ID);
		if (screenPreview != null && !selection.isEmpty()) {
			Iterator<NamedObject> iterator = selection.iterator();
			NamedObject namedObject = iterator.next();
			if (namedObject instanceof ScreenFieldModel) {
				ScreenFieldModel model = (ScreenFieldModel)namedObject;
				addFieldDrawingRectangle(screenPreview, model, true);
			}
			while (iterator.hasNext()) {
				NamedObject no = iterator.next();
				if (no instanceof ScreenFieldModel) {
					ScreenFieldModel model = (ScreenFieldModel)no;
					addFieldDrawingRectangle(screenPreview, model, false);
				}
			}
		}
	}

	private static void addFieldDrawingRectangle(ScreenPreview screenPreview, ScreenFieldModel model, boolean clearListBeforeAdd) {
		if (screenPreview == null) {
			return;
		}
		TerminalPosition endPosition = null;
		if (model.isRectangle()) {
			endPosition = new SimpleTerminalPosition((model.getEndRow() != null) ? model.getEndRow() : 0,
					(model.getEndColumn() != null) ? model.getEndColumn() : 0);
		} else {
			endPosition = new SimpleTerminalPosition(model.getRow(), (model.getEndColumn() == null) ? 0 : model.getEndColumn());
		}

		screenPreview.addFieldRectangle(
				new FieldRectangle(model.getRow(), endPosition.getRow(), model.getColumn(), endPosition.getColumn(), ""),//$NON-NLS-1$
				SWT.COLOR_YELLOW, clearListBeforeAdd);
		// add rectangle for @ScreenDescriptionField
		if ((model.getDescriptionFieldModel().getColumn() != null) && (model.getDescriptionFieldModel().getColumn() > 0)) {
			Integer row = model.getDescriptionFieldModel().getRow();
			if (row == null || row == 0) {
				row = model.getRow();
			}
			Integer endColumn = model.getDescriptionFieldModel().getEndColumn();
			if (endColumn == null) {
				endColumn = 0;
			}
			screenPreview.addFieldRectangle(new FieldRectangle(row, row, model.getDescriptionFieldModel().getColumn(), endColumn,
					""), SWT.COLOR_YELLOW);//$NON-NLS-1$
		}
	}

	private void createConvertButton(Composite parent) {
		DropDownButton button = new DropDownButton(parent);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		button.setLayoutData(gd);
		button.setText(Messages.getString("Button.convert"));//$NON-NLS-1$

		addConvertToStringFieldMenuItem(button.getMenu());
		addConvertToBooleanFieldMenuItem(button.getMenu());
		addConvertToDateFieldMenuItem(button.getMenu());
		addConvertToIntegerFieldMenuItem(button.getMenu());
		addConvertToEnumFieldMenuItem(button.getMenu());
		addConvertToValuesFieldMenuItem(button.getMenu());
	}

	private void addConvertToStringFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Menu.convert.to.string"));//$NON-NLS-1$
		item.addSelectionListener(new ConvertMenuItemSelectionAdapter(ScreenFieldModel.class));
	}

	private void addConvertToBooleanFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Menu.convert.to.boolean"));//$NON-NLS-1$
		item.addSelectionListener(new ConvertMenuItemSelectionAdapter(ScreenBooleanFieldModel.class));
	}

	private void addConvertToDateFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Menu.convert.to.date"));//$NON-NLS-1$
		item.addSelectionListener(new ConvertMenuItemSelectionAdapter(ScreenDateFieldModel.class));
	}

	private void addConvertToIntegerFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Menu.convert.to.integer"));//$NON-NLS-1$
		item.addSelectionListener(new ConvertMenuItemSelectionAdapter(ScreenIntegerFieldModel.class));
	}

	private void addConvertToEnumFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Menu.convert.to.enum"));//$NON-NLS-1$
		item.addSelectionListener(new ConvertMenuItemSelectionAdapter(ScreenEnumFieldModel.class));
	}

	private void addConvertToValuesFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Menu.convert.to.values"));//$NON-NLS-1$
		item.addSelectionListener(new ConvertMenuItemSelectionAdapter(ScreenFieldValuesModel.class));
	}

	@Override
	public AbstractEntity getAbstractEntity() {
		return getEntity();
	}

	@Override
	public IStructuredSelection getMasterBlockViewerSelection() {
		return (IStructuredSelection)treeViewer.getSelection();
	}

	@Override
	public void removeValidationMarkers(UUID uuid) {
		for (IDetailsPage detailsPage : detailsPages) {
			if (((IOpenLegacyDetailsPage)detailsPage).getModelUUID() != null
					&& ((IOpenLegacyDetailsPage)detailsPage).getModelUUID().equals(uuid)) {
				((IOpenLegacyDetailsPage)detailsPage).removeValidationMarkers();
				break;
			}
		}
	}

	@Override
	public void removePartsValidationMarkers() {
		for (IDetailsPage detailsPage : detailsPages) {
			if (detailsPage instanceof PartsScreenPartDetailsPage) {
				((IOpenLegacyDetailsPage)detailsPage).removeValidationMarkers();
			}
		}
	}

	private class ConvertMenuItemSelectionAdapter extends SelectionAdapter {

		private Class<?> targetClass;

		public ConvertMenuItemSelectionAdapter(Class<?> targetClass) {
			this.targetClass = targetClass;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			converter.convertTo(targetClass);
		}

	}

	private class CreateMenuItemSelectionAdapter extends SelectionAdapter {

		private Class<?> targetClass;

		public CreateMenuItemSelectionAdapter(Class<?> targetClass) {
			this.targetClass = targetClass;
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			creator.createNewField(targetClass);
		}

	}

}
