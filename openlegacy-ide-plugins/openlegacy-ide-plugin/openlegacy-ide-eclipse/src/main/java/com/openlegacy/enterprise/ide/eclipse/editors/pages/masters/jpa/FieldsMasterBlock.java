package com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.jpa;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.button.SplitButton;
import com.openlegacy.enterprise.ide.eclipse.editors.models.AbstractEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaByteFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaDateFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaListFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaManyToOneFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.IOpenLegacyDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa.FieldsJpaBooleanFieldDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa.FieldsJpaByteFieldDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa.FieldsJpaDateFieldDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa.FieldsJpaIntegerFieldDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa.FieldsJpaListFieldDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa.FieldsJpaManyToOneFieldDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa.FieldsJpaStringFieldDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.jpa.helpers.FieldsCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.screen.helpers.IPartsMasterBlockCallback;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.jpa.FieldsMasterBlockContentProvider;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.jpa.FieldsMasterBlockLabelProvider;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
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
 * 
 */
public class FieldsMasterBlock extends AbstractJpaEntityMasterBlock implements IPartsMasterBlockCallback {

	private FieldsCreator creator;

	public FieldsMasterBlock(AbstractPage page) {
		super(page);
		creator = new FieldsCreator(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock#refresh()
	 */
	@Override
	public void refresh() {
		for (IDetailsPage page : detailsPages) {
			page.refresh();
		}
		IStructuredSelection structuredSelection = (IStructuredSelection)tableViewer.getSelection();
		tableViewer.setInput(getEntity());
		if (structuredSelection.size() == 1) {
			tableViewerSetSelectionByLabel(((JpaFieldModel)structuredSelection.getFirstElement()).getFieldName());
		} else {
			tableViewerSetSelection(0);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock#createMasterPart(org.eclipse.ui.forms.IManagedForm,
	 * org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createMasterPart(final IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		Section section = toolkit.createSection(parent, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR);
		section.setText(Messages.getString("jpa.fields.page.master.block.text")); //$NON-NLS-1$
		section.setDescription(Messages.getString("jpa.fields.page.master.block.desc")); //$NON-NLS-1$
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
		tableViewer.setContentProvider(new FieldsMasterBlockContentProvider());
		tableViewer.setLabelProvider(new FieldsMasterBlockLabelProvider());
		tableViewer.setInput(getEntity());

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

		SplitButton splitButton = new SplitButton(composite);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		splitButton.setLayoutData(gd);
		splitButton.setText(Messages.getString("Button.add"));//$NON-NLS-1$

		splitButton.addSelectionListener(getMenuItemSelectionListener());

		addNewStringFieldMenuItem(splitButton.getMenu());
		addNewBooleanFieldMenuItem(splitButton.getMenu());
		addNewByteFieldMenuItem(splitButton.getMenu());
		addNewIntegerFieldMenuItem(splitButton.getMenu());
		addNewDateFieldMenuItem(splitButton.getMenu());
		addNewListFieldMenuItem(splitButton.getMenu());
		addNewManyToOneFieldMenuItem(splitButton.getMenu());

		createRemoveButton(toolkit, composite);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock#registerPages(org.eclipse.ui.forms.DetailsPart)
	 */
	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPages.add(new FieldsJpaBooleanFieldDetailsPage(this));
		detailsPages.add(new FieldsJpaByteFieldDetailsPage(this));
		detailsPages.add(new FieldsJpaDateFieldDetailsPage(this));
		detailsPages.add(new FieldsJpaIntegerFieldDetailsPage(this));
		detailsPages.add(new FieldsJpaListFieldDetailsPage(this));
		detailsPages.add(new FieldsJpaStringFieldDetailsPage(this));
		detailsPages.add(new FieldsJpaManyToOneFieldDetailsPage(this));
		for (IDetailsPage page : detailsPages) {
			detailsPart.registerPage(((IOpenLegacyDetailsPage)page).getDetailsModel(), page);
		}
	}

	@Override
	public void createContent(IManagedForm managedForm) {
		super.createContent(managedForm);
		this.sashForm.setWeights(new int[] { 30, 70 });
		int itemCount = this.tableViewer.getTable().getItemCount();
		if (itemCount > 0) {
			this.tableViewer.getTable().select(0);
			this.tableViewer.getTable().showSelection();
			this.tableViewer.setSelection(this.tableViewer.getSelection());
		}
	}

	@Override
	public AbstractEntity getAbstractEntity() {
		return getEntity();
	}

	@Override
	public void reassignMasterBlockViewerInput(UUID selectUUID) {
		tableViewer.setInput(getEntity());
		if (selectUUID != null) {
			tableViewerSetSelectionByModelUUID(selectUUID);
		} else {
			tableViewerSetSelection(0);
		}
		page.getEditor().editorDirtyStateChanged();
	}

	private void tableViewerSetSelectionByModelUUID(UUID selectUUID) {
		int itemCount = tableViewer.getTable().getItemCount();
		if (itemCount > 0) {
			for (int i = 0; i < itemCount; i++) {
				TableItem item = tableViewer.getTable().getItem(i);
				if (((NamedObject)item.getData()).getUUID().equals(selectUUID)) {
					setViewerSelection(i);
					return;
				}
			}
		}
	}

	private void tableViewerSetSelection(int index) {
		int itemCount = tableViewer.getTable().getItemCount();
		if ((itemCount > 0) && (index <= itemCount)) {
			setViewerSelection(index);
		}
	}

	private void setViewerSelection(int index) {
		tableViewer.getTable().select(index);
		tableViewer.getTable().showSelection();
		tableViewer.setSelection(tableViewer.getSelection());
	}

	@Override
	public IStructuredSelection getMasterBlockViewerSelection() {
		return (IStructuredSelection)tableViewer.getSelection();
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
		// relevant for ScreenEntity editor
	}

	@Override
	public void updateLabels() {
		tableViewer.refresh();
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
			setViewerSelection(0);
		}
	}

	private SelectionListener getMenuItemSelectionListener() {
		return new CreateMenuItemSelectionAdapter(JpaFieldModel.class);
	}

	private void addNewStringFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Button.add.string.field"));
		item.addSelectionListener(getMenuItemSelectionListener());
		menu.setDefaultItem(item);
	}

	private void addNewBooleanFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Button.add.boolean.field"));//$NON-NLS-1$
		item.addSelectionListener(new CreateMenuItemSelectionAdapter(JpaBooleanFieldModel.class));
	}

	private void addNewByteFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Button.add.byte.field"));//$NON-NLS-1$
		item.addSelectionListener(new CreateMenuItemSelectionAdapter(JpaByteFieldModel.class));
	}

	private void addNewIntegerFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Button.add.integer.field"));//$NON-NLS-1$
		item.addSelectionListener(new CreateMenuItemSelectionAdapter(JpaIntegerFieldModel.class));
	}

	private void addNewDateFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Button.add.date.field"));//$NON-NLS-1$
		item.addSelectionListener(new CreateMenuItemSelectionAdapter(JpaDateFieldModel.class));
	}

	private void addNewListFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Button.add.list.field"));//$NON-NLS-1$
		item.addSelectionListener(new CreateMenuItemSelectionAdapter(JpaListFieldModel.class));
	}

	private void addNewManyToOneFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Button.add.many.to.one.field"));//$NON-NLS-1$
		item.addSelectionListener(new CreateMenuItemSelectionAdapter(JpaManyToOneFieldModel.class));
	}

	private void createRemoveButton(FormToolkit toolkit, Composite composite) {
		Button btnRemove = toolkit.createButton(composite, Messages.getString("Button.remove"), SWT.PUSH);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		btnRemove.setLayoutData(gd);
		btnRemove.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				creator.removeSelectedElements();
			}

		});
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
