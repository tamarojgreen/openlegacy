package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.rpc;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.RpcEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcPartModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator.JAVA_DOCUMENTATION_TYPE;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc.ActionsComboBoxCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc.ActionsDialogCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc.ActionsTextCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.rpc.ActionsPageTableContentProvider;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.TextValidator;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.definitions.RpcActionDefinition;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.rpc.generators.support.RpcAnnotationConstants;

import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class PartsRpcPartDetailsPage extends AbstractRpcDetailsPage {

	private RpcPartModel partModel;
	private TextValidator classNameValidator;
	private TextValidator countValidator;

	private TableViewer tableViewer;
	private Button addButton;
	private Button removeButton;

	public PartsRpcPartDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#getDetailsModel()
	 */
	@Override
	public Class<?> getDetailsModel() {
		return RpcPartModel.class;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#createContents(org.eclipse.swt.widgets.Composite)
	 */
	@Override
	public void createContents(Composite parent) {
		TableWrapLayout layout = new TableWrapLayout();
		layout.topMargin = 5;
		layout.leftMargin = 5;
		layout.rightMargin = 2;
		layout.bottomMargin = 2;
		parent.setLayout(layout);

		FormToolkit toolkit = managedForm.getToolkit();
		Section section = toolkit.createSection(parent, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR);
		section.marginWidth = 10;
		section.setText(Messages.getString("rpc.fields.page.part.details.text")); //$NON-NLS-1$
		section.setDescription(Messages.getString("rpc.fields.page.details.desc")); //$NON-NLS-1$
		TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
		td.grabHorizontal = true;
		section.setLayoutData(td);

		Composite topClient = toolkit.createComposite(section);
		GridLayout glayout = new GridLayout();
		glayout.marginWidth = glayout.marginHeight = 0;
		glayout.numColumns = 1;
		topClient.setLayout(glayout);

		Composite client = toolkit.createComposite(topClient);
		glayout = new GridLayout();
		glayout.marginWidth = glayout.marginHeight = 0;
		glayout.numColumns = 2;
		client.setLayout(glayout);

		// create empty row
		FormRowCreator.createSpacer(toolkit, client, 2);
		// create row for "className"
		Text classNameControl = FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("rpc.field.part.class.name.label"), "", Constants.JAVA_TYPE_NAME,//$NON-NLS-1$ //$NON-NLS-2$
				JAVA_DOCUMENTATION_TYPE.JAVA_BASICS, "datatypes", "");//$NON-NLS-1$ //$NON-NLS-2$
		classNameValidator = new TextValidator(master, managedForm, classNameControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateClassNameControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return partModel;
			}

		};
		// create row for 'name'
		// XXX Ivan: comment row creation of 'name' for better time
		// FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
		//				Messages.getString("rpc.field.part.name.label"), "", AnnotationConstants.NAME);//$NON-NLS-1$ //$NON-NLS-2$
		// create row for 'displayName'
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("rpc.field.part.display.name.label"), "", AnnotationConstants.DISPLAY_NAME,//$NON-NLS-1$ //$NON-NLS-2$
				JAVA_DOCUMENTATION_TYPE.RPC, "RpcPart", AnnotationConstants.DISPLAY_NAME);//$NON-NLS-1$

		// @RpcPartList section
		addRpcPartListSection(toolkit, client);

		toolkit.paintBordersFor(section);
		section.setClient(topClient);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#getModelUUID()
	 */
	@Override
	public UUID getModelUUID() {
		return partModel != null ? partModel.getUUID() : null;
	}

	@Override
	public void revalidate() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#updateControls()
	 */
	@Override
	protected void updateControls() {
		ControlsUpdater.updateRpcPartDetailsControls(partModel, mapTexts);
		if (tableViewer != null) {
			tableViewer.setInput(partModel);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#doUpdateModel(java.lang.String)
	 */
	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		if (!StringUtils.equals(key, Constants.RPC_PART_ACTIONS)) {
			Map<String, Object> map = getValuesOfControlsForKey(key);
			ModelUpdater.updateRpcPartModel(getEntity(), partModel, key, (String)map.get(Constants.TEXT_VALUE));
		} else {
			ModelUpdater.updateRpcPartActionsModel(getEntity(), partModel.getActionsModel());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#afterDoUpdateModel()
	 */
	@Override
	protected void afterDoUpdateModel() {
		setDirty(getEntity().isDirty());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#selectionChanged(org.eclipse.jface.viewers.
	 * IStructuredSelection)
	 */
	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			partModel = (RpcPartModel)selection.getFirstElement();
		} else {
			partModel = null;
		}
	}

	@Override
	public void removeValidationMarkers() {
		super.removeValidationMarkers();
		if (classNameValidator != null) {
			classNameValidator.removeValidationMarker();
			countValidator.removeValidationMarker();
		}
	}

	@Override
	protected void updateValidators(UUID uuid) {
		super.updateValidators(uuid);
		if (uuid != null) {
			classNameValidator.setModelUUID(uuid);
			countValidator.setModelUUID(uuid);
		}
	}

	private void addRpcPartListSection(FormToolkit toolkit, Composite client) {
		Section section = toolkit.createSection(client, ExpandableComposite.TWISTIE | Section.DESCRIPTION);
		section.setActiveToggleColor(toolkit.getHyperlinkGroup().getActiveForeground());
		section.setToggleColor(toolkit.getColors().getColor(IFormColors.SEPARATOR));
		toolkit.createCompositeSeparator(section);

		Composite composite = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.horizontalSpacing = 0;
		composite.setLayout(layout);

		// 'count' row
		Text countControl = FormRowCreator.createIntRow(toolkit, composite, mapTexts, getDefaultModifyListener(),
				getDefaultVerifyListener(), Messages.getString("rpc.part.list.count.label"), 0, RpcAnnotationConstants.COUNT,//$NON-NLS-1$
				JAVA_DOCUMENTATION_TYPE.RPC, "RpcPartList", RpcAnnotationConstants.COUNT);//$NON-NLS-1$
		countValidator = new TextValidator(master, managedForm, countControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateCountControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return partModel;
			}

		};

		// create table for managing RpcActions for RpcPart if count more than 1
		createActionsTable(toolkit, composite);

		section.setText(Messages.getString("rpc.part.list.section.text"));//$NON-NLS-1$
		section.setClient(composite);
		section.setExpanded(true);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		section.setLayoutData(gd);
	}

	private static boolean validateClassNameControl(TextValidator validator, UUID uuid) {
		String text = validator.getControl().getText();
		boolean isValid = !text.isEmpty();
		if (!isValid) {
			validator.addMessage(Messages.getString("validation.should.not.be.empty"), IMessageProvider.ERROR, uuid);//$NON-NLS-1$
			return isValid;
		}
		return isValid;
	}

	private boolean validateCountControl(TextValidator validator, UUID uuid) {
		String text = validator.getControl().getText();
		boolean isValid = text.isEmpty();
		int value = isValid ? 1 : Integer.valueOf(text).intValue();
		isValid = isValid ? true : value > 0;
		if (!isValid) {
			validator.addMessage(
					Messages.getString("validation.should.be.null.or.greater.than.one"), IMessageProvider.ERROR, uuid);//$NON-NLS-1$
		}
		if (isValid) {
			setEnabledActionsTableViewer(value > 1);
		}
		return isValid;
	}

	private void createActionsTable(FormToolkit toolkit, Composite parent) {
		Composite client = toolkit.createComposite(parent);
		GridLayout gl = new GridLayout();
		gl.marginWidth = gl.marginHeight = 0;
		gl.numColumns = 2;
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.horizontalSpan = 2;
		client.setLayout(gl);
		client.setLayoutData(gd);

		FormRowCreator.createSpacer(toolkit, client, 2);

		Label descriptionLabel = toolkit.createLabel(client, Messages.getString("rpc.part.actions.table.description"));
		gd = new GridData();
		gd.horizontalSpan = 2;
		descriptionLabel.setLayoutData(gd);

		ScrolledComposite scrolledComposite = new ScrolledComposite(client, SWT.NONE);
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.heightHint = 200;
		gd.widthHint = 600;
		scrolledComposite.setLayoutData(gd);

		Table t = toolkit.createTable(scrolledComposite, SWT.FULL_SELECTION);
		t.setHeaderVisible(true);
		t.setLinesVisible(true);

		scrolledComposite.setContent(t);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setMinSize(500, 200);

		// add buttons
		addPanelWithButtons(toolkit, client);

		// create table
		tableViewer = new TableViewer(t);
		createColumns(tableViewer);

		tableViewer.setContentProvider(new ActionsPageTableContentProvider());
		tableViewer.setInput(partModel);
		toolkit.paintBordersFor(client);
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
		addButton = toolkit.createButton(panel, Messages.getString("Button.add"), SWT.PUSH);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		addButton.setLayoutData(gd);
		addButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				partModel.getActionsModel().getActions().add(new ActionModel("", "", "", "", true));//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				tableViewer.setInput(partModel);
				updateModel(Constants.RPC_PART_ACTIONS);
				tableViewer.getTable().select(tableViewer.getTable().getItemCount() - 1);
			}

		});

		removeButton = toolkit.createButton(panel, Messages.getString("Button.remove"), SWT.PUSH);
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		removeButton.setLayoutData(gd);
		removeButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection structuredSelection = (IStructuredSelection)tableViewer.getSelection();
				if (structuredSelection.size() == 1) {
					ActionModel model = (ActionModel)structuredSelection.getFirstElement();
					partModel.getActionsModel().getActions().remove(model);
					tableViewer.setInput(partModel);
					updateModel(Constants.RPC_PART_ACTIONS);
					tableViewer.getTable().select(tableViewer.getTable().getItemCount() - 1);
				}
			}

		});
	}

	private void createColumns(TableViewer viewer) {
		// "action" column
		TableViewerColumn vcol = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tcol = vcol.getColumn();
		tcol.setText(Messages.getString("rpc.actions.page.col.action.label"));//$NON-NLS-1$
		tcol.setResizable(false);
		tcol.setWidth(100);

		vcol.setEditingSupport(new ActionsDialogCellEditingSupport(viewer, AnnotationConstants.ACTION));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ActionDefinition action = (ActionDefinition)cell.getElement();
				cell.setText(action.getActionName());
				updateModel(Constants.RPC_PART_ACTIONS);
			}
		});

		// "displayName" column
		vcol = new TableViewerColumn(viewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("rpc.actions.page.col.display.name.label"));//$NON-NLS-1$
		tcol.setResizable(false);
		tcol.setWidth(120);

		vcol.setEditingSupport(new ActionsTextCellEditingSupport(viewer, AnnotationConstants.DISPLAY_NAME));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ActionDefinition action = (ActionDefinition)cell.getElement();
				cell.setText(action.getDisplayName());
				updateModel(Constants.RPC_PART_ACTIONS);
			}
		});

		// "alias" column
		vcol = new TableViewerColumn(viewer, SWT.FILL);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("rpc.actions.page.col.alias.label"));//$NON-NLS-1$
		tcol.setResizable(false);
		tcol.setWidth(120);

		vcol.setEditingSupport(new ActionsTextCellEditingSupport(viewer, AnnotationConstants.ALIAS));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ActionDefinition action = (ActionDefinition)cell.getElement();
				cell.setText(action.getAlias());
				updateModel(Constants.RPC_PART_ACTIONS);
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
				updateModel(Constants.RPC_PART_ACTIONS);
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
				updateModel(Constants.RPC_PART_ACTIONS);
			}
		});

		// "target entity" column
		vcol = new TableViewerColumn(viewer, SWT.FILL);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("rpc.actions.page.col.target.entity"));//$NON-NLS-1$
		tcol.setResizable(false);
		tcol.setWidth(150);

		vcol.setEditingSupport(new ActionsDialogCellEditingSupport(viewer, AnnotationConstants.TARGET_ENTITY));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ActionModel action = (ActionModel)cell.getElement();
				cell.setText(action.getTargetEntityClassName());
				updateModel(Constants.RPC_PART_ACTIONS);
				validateTargetEntityColumn(action);
			}
		});
	}

	private void validateTargetEntityColumn(ActionModel model) {
		Class<?> targetEntity = model.getTargetEntity();
		boolean isRpcEntity = false;
		for (Annotation annotation : targetEntity.getDeclaredAnnotations()) {
			if (annotation.annotationType().getName().equals(org.openlegacy.annotations.rpc.RpcEntity.class.getName())) {
				isRpcEntity = true;
				break;
			}
		}
		String validationMarkerKey = MessageFormat.format("{0}-{1}", model.getUuid(), "targetEntity");//$NON-NLS-1$ //$NON-NLS-2$
		RpcEntityEditor editor = (RpcEntityEditor)master.getAbstractPage().getEntityEditor();
		if (isRpcEntity || targetEntity.getName().equals(org.openlegacy.rpc.RpcEntity.NONE.class.getName())) {
			// remove validation marker
			managedForm.getMessageManager().removeMessage(validationMarkerKey);
			editor.removeValidationMarker(validationMarkerKey);
		} else {
			// add validation marker
			String message = MessageFormat.format("Target entity: {0} \n {1}", targetEntity.getName(),//$NON-NLS-1$
					Messages.getString("validation.selected.class.is.not.rpc.entity"));//$NON-NLS-1$
			managedForm.getMessageManager().addMessage(validationMarkerKey, message, null, IMessageProvider.ERROR);
			editor.addValidationMarker(validationMarkerKey, message);
		}
	}

	private void setEnabledActionsTableViewer(boolean enabled) {
		if (tableViewer != null) {
			tableViewer.getTable().setEnabled(enabled);
		}
		if (addButton != null) {
			addButton.setEnabled(enabled);
		}
		if (removeButton != null) {
			removeButton.setEnabled(enabled);
		}
	}

}
