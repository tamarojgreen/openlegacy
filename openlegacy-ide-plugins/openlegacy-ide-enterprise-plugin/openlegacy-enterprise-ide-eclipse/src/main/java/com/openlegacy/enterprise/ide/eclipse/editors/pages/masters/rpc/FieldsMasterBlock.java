package com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.rpc;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.ActionType;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcBigIntegerFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcBooleanFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcIntegerFieldAction;
import com.openlegacy.enterprise.ide.eclipse.editors.actions.rpc.RpcPartAction;
import com.openlegacy.enterprise.ide.eclipse.editors.button.SplitButton;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcBigIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcBooleanFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcIntegerFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcNamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcPartModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.IOpenLegacyDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.rpc.FieldsRpcBigIntegerFieldDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.rpc.FieldsRpcBooleanFieldDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.rpc.FieldsRpcIntegerFieldDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.rpc.FieldsRpcStringFieldDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.rpc.PartsRpcPartDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.dnd.rpc.FieldsTreeViewerDragListener;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.dnd.rpc.FieldsTreeViewerDropHelper;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.dnd.rpc.FieldsTreeViewerDropListener;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.rpc.FieldsMasterBlockContentProvider;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.rpc.FieldsMasterBlockLabelProvider;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.rpc.RpcEntityUtils;

import org.eclipse.jdt.core.dom.ASTNode;
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
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

import java.text.MessageFormat;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * * @author Ivan Bort
 * 
 */
public class FieldsMasterBlock extends AbstractRpcEntityMasterBlock {

	private TreeViewer treeViewer;
	private Button btnRemove;

	public FieldsMasterBlock(AbstractPage page) {
		super(page);
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
		treeViewer.setInput(getEntity());
		treeViewer.expandAll();
		treeViewerSetSelection(0);
	}

	@Override
	public void createContent(IManagedForm managedForm) {
		super.createContent(managedForm);
		this.sashForm.setWeights(new int[] { 30, 70 });
		treeViewerSetSelection(0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock#registerPages(org.eclipse.ui.forms.DetailsPart)
	 */
	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPages.add(new FieldsRpcStringFieldDetailsPage(this));
		detailsPages.add(new FieldsRpcBooleanFieldDetailsPage(this));
		detailsPages.add(new FieldsRpcIntegerFieldDetailsPage(this));
		detailsPages.add(new FieldsRpcBigIntegerFieldDetailsPage(this));
		detailsPages.add(new PartsRpcPartDetailsPage(this));
		for (IDetailsPage page : detailsPages) {
			detailsPart.registerPage(((IOpenLegacyDetailsPage)page).getDetailsModel(), page);
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
		section.setText(Messages.getString("rpc.fields.page.master.block.text")); //$NON-NLS-1$
		section.setDescription(Messages.getString("rpc.fields.page.master.block.desc")); //$NON-NLS-1$
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

			public void selectionChanged(SelectionChangedEvent event) {
				if (!event.getSelection().isEmpty()) {
					btnRemove.setEnabled(true);
				}
				if (((IStructuredSelection)event.getSelection()).size() == 1) {
					managedForm.fireSelectionChanged(spart, event.getSelection());
				} else {
					managedForm.fireSelectionChanged(spart, null);
				}
			}
		});
		// unselect tree item if user click on empty space
		treeViewer.getTree().addMouseListener(new MouseAdapter() {

			@Override
			public void mouseUp(MouseEvent e) {
				if (treeViewer.getTree().getItem(new Point(e.x, e.y)) == null) {
					treeViewer.getTree().deselectAll();
					btnRemove.setEnabled(false);
				}
			}

		});

		Transfer[] transfers = new Transfer[] { LocalSelectionTransfer.getTransfer() };
		FieldsTreeViewerDropListener dropListener = new FieldsTreeViewerDropListener(treeViewer, page);
		treeViewer.addDragSupport(DND.DROP_MOVE, transfers, new FieldsTreeViewerDragListener(treeViewer, dropListener));
		treeViewer.addDropSupport(DND.DROP_MOVE, transfers, dropListener);

		treeViewer.setContentProvider(new FieldsMasterBlockContentProvider());
		treeViewer.setLabelProvider(new FieldsMasterBlockLabelProvider());
		treeViewer.setInput(getEntity());
		treeViewer.expandAll();
		// add context menu
		MenuManager menuMgr = new MenuManager();
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {

			public void menuAboutToShow(IMenuManager manager) {
				if (treeViewer.getSelection().isEmpty()) {
					return;
				}
				manager.add(getCreateActionOfRpcPartBasedOnSelection());
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
		addNewIntegerFieldMenuItem(splitButton.getMenu());
		addNewBigIntegerFieldMenuItem(splitButton.getMenu());
		addNewRpcPartMenuItem(splitButton.getMenu());

		createRemoveButton(toolkit, composite);
	}

	private void createRemoveButton(FormToolkit toolkit, Composite composite) {
		btnRemove = toolkit.createButton(composite, Messages.getString("Button.remove"), SWT.PUSH);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		btnRemove.setLayoutData(gd);
		btnRemove.addSelectionListener(new SelectionAdapter() {

			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection)treeViewer.getSelection();
				if (!selection.isEmpty()) {
					Iterator<RpcNamedObject> iterator = selection.iterator();
					RpcEntity entity = getEntity();
					while (iterator.hasNext()) {
						RpcNamedObject model = iterator.next();
						if (model instanceof RpcFieldModel) {
							// remove validation markers
							for (IDetailsPage detailsPage : detailsPages) {
								if (((IOpenLegacyDetailsPage)detailsPage).getModelUUID() != null
										&& ((IOpenLegacyDetailsPage)detailsPage).getModelUUID().equals(model.getUUID())) {
									((IOpenLegacyDetailsPage)detailsPage).removeValidationMarkers();
									break;
								}
							}
							NamedObject parent = model.getParent();
							if (parent instanceof RpcEntityModel) {
								entity.removeRpcFieldModel((RpcFieldModel)model);
							} else if (parent instanceof RpcPartModel) {
								entity.removeRpcFieldModelFromPart((RpcFieldModel)model);
							}
							// try to remove action for new field
							entity.removeAction(model.getUUID(), ((RpcFieldModel)model).getFieldName());
							entity.addAction(new RpcFieldAction(model.getUUID(), model, ActionType.REMOVE,
									ASTNode.FIELD_DECLARATION, null, null));
						} else if (model instanceof RpcPartModel) {
							// remove validation markers
							for (IDetailsPage detailsPage : detailsPages) {
								if (detailsPage instanceof PartsRpcPartDetailsPage) {
									((IOpenLegacyDetailsPage)detailsPage).removeValidationMarkers();
								}
							}
							NamedObject parent = model.getParent();
							if (parent instanceof RpcEntityModel) {
								entity.removeRpcPartModel((RpcPartModel)model);
							} else if (parent instanceof RpcPartModel) {
								entity.removeRpcPartModelFromPart((RpcPartModel)model);
							}
							// try to remove action for new part
							entity.removeAction(model.getUUID(), ((RpcPartModel)model).getClassName());
							entity.addAction(new RpcPartAction(model.getUUID(), model, ActionType.REMOVE,
									ASTNode.TYPE_DECLARATION, null, null));
						}
					}
					reassignTreeViewerInput(null);
				}
			}

		});
	}

	private void addNewRpcPartMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH | SWT.ITALIC);
		item.setText(Messages.getString("rpc.button.add.part"));//$NON-NLS-1$
		item.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				RpcPartModel newModel = createNewRpcPartModel();
				reassignTreeViewerInput(newModel.getUUID());
			}

		});
	}

	private int getSeedForNewPart(String baseName, RpcNamedObject parent) {
		int seed = 0;
		List<RpcPartModel> parts = getEntity().getSortedParts();
		if (parent instanceof RpcPartModel) {
			parts = getEntity().getSortedPartByUUID(parent.getUUID()).getSortedParts();
		}
		Pattern p = Pattern.compile("(\\d+)$");
		for (RpcPartModel part : parts) {
			if (!part.getClassName().startsWith(baseName)) {
				continue;
			}
			Matcher matcher = p.matcher(part.getClassName());
			if (matcher.find()) {
				String stringSeed = matcher.group(1);
				if (Integer.parseInt(stringSeed) > seed) {
					seed = Integer.parseInt(stringSeed);
				}
			}
		}
		return ++seed;
	}

	private void addNewIntegerFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Button.add.integer.field"));//$NON-NLS-1$
		item.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				NamedObject parent = getParentOfSelectedModel();
				RpcIntegerFieldModel newModel = new RpcIntegerFieldModel((RpcNamedObject)parent);
				newModel.setNew(true);

				RpcEntity entity = getEntity();
				if (parent instanceof RpcEntityModel) {
					entity.addRpcFieldModel(newModel);
				} else if (parent instanceof RpcPartModel) {
					entity.addRpcFieldModelToPart((RpcPartModel)parent, newModel);
				}
				fillNewModel(newModel);
				entity.addAction(new RpcIntegerFieldAction(newModel.getUUID(), newModel, ActionType.ADD,
						ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
				RpcEntityUtils.ActionGenerator.generateRpcFieldActions(entity, newModel);
				RpcEntityUtils.ActionGenerator.generateRpcNumericFieldActions(entity, newModel);

				reassignTreeViewerInput(newModel.getUUID());
			}

		});
	}

	private void addNewBigIntegerFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Button.add.big.integer.field"));//$NON-NLS-1$
		item.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				NamedObject parent = getParentOfSelectedModel();
				RpcIntegerFieldModel newModel = new RpcBigIntegerFieldModel((RpcNamedObject)parent);
				newModel.setNew(true);

				RpcEntity entity = getEntity();
				if (parent instanceof RpcEntityModel) {
					entity.addRpcFieldModel(newModel);
				} else if (parent instanceof RpcPartModel) {
					entity.addRpcFieldModelToPart((RpcPartModel)parent, newModel);
				}
				fillNewModel(newModel);
				entity.addAction(new RpcBigIntegerFieldAction(newModel.getUUID(), newModel, ActionType.ADD,
						ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
				RpcEntityUtils.ActionGenerator.generateRpcFieldActions(entity, newModel);
				RpcEntityUtils.ActionGenerator.generateRpcNumericFieldActions(entity, newModel);

				reassignTreeViewerInput(newModel.getUUID());
			}

		});
	}

	private void addNewBooleanFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Button.add.boolean.field"));//$NON-NLS-1$
		item.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				NamedObject parent = getParentOfSelectedModel();
				RpcBooleanFieldModel newModel = new RpcBooleanFieldModel((RpcNamedObject)parent);
				newModel.setNew(true);

				RpcEntity entity = getEntity();
				if (parent instanceof RpcEntityModel) {
					entity.addRpcFieldModel(newModel);
				} else if (parent instanceof RpcPartModel) {
					entity.addRpcFieldModelToPart((RpcPartModel)parent, newModel);
				}
				fillNewModel(newModel);
				entity.addAction(new RpcBooleanFieldAction(newModel.getUUID(), newModel, ActionType.ADD,
						ASTNode.FIELD_DECLARATION, newModel.getFieldName(), null));
				RpcEntityUtils.ActionGenerator.generateRpcFieldActions(entity, newModel);
				RpcEntityUtils.ActionGenerator.generateRpcBooleanFieldActions(entity, newModel);

				reassignTreeViewerInput(newModel.getUUID());
			}

		});
	}

	private void addNewStringFieldMenuItem(Menu menu) {
		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(Messages.getString("Button.add.string.field"));
		item.addSelectionListener(getMenuItemSelectionListener());
		menu.setDefaultItem(item);
	}

	private SelectionListener getMenuItemSelectionListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				NamedObject parent = getParentOfSelectedModel();
				RpcFieldModel newModel = new RpcFieldModel((RpcNamedObject)parent);
				newModel.setNew(true);

				RpcEntity entity = getEntity();
				if (parent instanceof RpcEntityModel) {
					entity.addRpcFieldModel(newModel);
				} else if (parent instanceof RpcPartModel) {
					entity.addRpcFieldModelToPart((RpcPartModel)parent, newModel);
				}
				fillNewModel(newModel);
				entity.addAction(new RpcFieldAction(newModel.getUUID(), newModel, ActionType.ADD, ASTNode.FIELD_DECLARATION,
						newModel.getFieldName(), null));
				RpcEntityUtils.ActionGenerator.generateRpcFieldActions(entity, newModel);

				reassignTreeViewerInput(newModel.getUUID());
			}

		};
	}

	private NamedObject getParentOfSelectedModel() {
		NamedObject parent = getEntity().getEntityModel();
		IStructuredSelection selection = (IStructuredSelection)treeViewer.getSelection();
		if (selection.size() == 1) {
			NamedObject namedObject = (NamedObject)selection.getFirstElement();
			if (namedObject instanceof RpcPartModel) {
				parent = namedObject;
			} else if (namedObject instanceof RpcFieldModel) {
				parent = ((RpcFieldModel)namedObject).getParent();
			}
		}
		return parent;
	}

	private static void fillNewModel(RpcFieldModel model) {
		model.setFieldName(Messages.getString("Field.new"));//$NON-NLS-1$
		if (model instanceof RpcBooleanFieldModel) {
			((RpcBooleanFieldModel)model).setTrueValue("");//$NON-NLS-1$
			((RpcBooleanFieldModel)model).setFalseValue("");//$NON-NLS-1$
		}
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

	private void treeViewerSetSelectionByModelUUID(UUID uuid) {
		int itemCount = treeViewer.getTree().getItemCount();
		if (itemCount > 0) {
			for (int i = 0; i < itemCount; i++) {
				TreeItem item = treeViewer.getTree().getItem(i);
				if (((NamedObject)item.getData()).getUUID().equals(uuid)) {
					setViewerSelection(item);
					return;
				}
				item = findTreeItem(item, uuid);
				if (item != null) {
					setViewerSelection(item);
					return;
				}
			}
		}
	}

	private TreeItem findTreeItem(TreeItem parent, UUID uuid) {
		TreeItem item = null;
		int count = parent.getItemCount();
		if (count > 0) {
			for (int j = 0; j < count; j++) {
				TreeItem childItem = parent.getItem(j);
				if (((NamedObject)childItem.getData()).getUUID().equals(uuid)) {
					return childItem;
				}
				item = findTreeItem(childItem, uuid);
				if (item != null) {
					return item;
				}
			}
		}
		return item;
	}

	private void reassignTreeViewerInput(UUID selectUUID) {
		// before set new Input for viewer we must reorder fields and parts (reassign treeLevel and treeBranch)
		RpcEntityUtils.reorderFieldsAndParts(getEntity());
		treeViewer.setInput(getEntity());
		treeViewer.expandAll();
		if (selectUUID != null) {
			treeViewerSetSelectionByModelUUID(selectUUID);
		} else {
			treeViewerSetSelection(0);
		}
		page.getEditor().editorDirtyStateChanged();
	}

	private IAction getCreateActionOfRpcPartBasedOnSelection() {
		return new Action(Messages.getString("context.menu.action.create.rpc.part")) {

			@SuppressWarnings("unchecked")
			@Override
			public void run() {
				IStructuredSelection selection = (IStructuredSelection)treeViewer.getSelection();
				if (selection.isEmpty()) {
					return;
				}
				RpcPartModel newPartModel = createNewRpcPartModel();
				Iterator<RpcNamedObject> iterator = selection.iterator();
				while (iterator.hasNext()) {
					RpcNamedObject namedObject = iterator.next();
					// emulate drop
					FieldsTreeViewerDropHelper.performDrop(getEntity(), namedObject, newPartModel);
				}
				reassignTreeViewerInput(newPartModel.getUUID());
			}

		};
	}

	private RpcPartModel createNewRpcPartModel() {
		NamedObject parent = getParentOfSelectedModel();
		RpcEntity entity = getEntity();

		String baseName = entity.getEntityModel().getClassName() + Messages.getString("rpc.part.new.name");//$NON-NLS-1$
		if (parent instanceof RpcPartModel) {
			baseName = ((RpcPartModel)parent).getClassName() + Messages.getString("rpc.part.new.name");//$NON-NLS-1$
		}
		int seed = getSeedForNewPart(baseName, (RpcNamedObject)parent);
		RpcPartModel newModel = new RpcPartModel(MessageFormat.format("{0}{1}", baseName, seed), (RpcNamedObject)parent);//$NON-NLS-1$
		newModel.setNew(true);
		if (parent instanceof RpcPartModel) {
			entity.addRpcPartModelToPart((RpcPartModel)parent, newModel);
		} else {
			entity.addRpcPartModel(newModel);
		}
		entity.addAction(new RpcPartAction(newModel.getUUID(), newModel, ActionType.ADD, ASTNode.TYPE_DECLARATION,
				newModel.getClassName(), null));
		entity.addAction(new RpcPartAction(newModel.getUUID(), newModel, ActionType.ADD, ASTNode.FIELD_DECLARATION,
				Messages.getString("Field.new"), null));//$NON-NLS-1$
		return newModel;
	}
}
