package com.openlegacy.enterprise.ide.eclipse.editors.pages.screen;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.ScreenEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenActionsModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ActionsComboBoxCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ActionsDialogCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ActionsTextCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen.ActionsPageTableContentProvider;

import org.apache.commons.lang.StringUtils;
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.openlegacy.annotations.screen.Action.ActionType;
import org.openlegacy.definitions.ActionDefinition;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.ide.eclipse.Activator;
import org.openlegacy.ide.eclipse.preview.screen.FieldRectangle;
import org.openlegacy.ide.eclipse.preview.screen.ScreenPreview;
import org.openlegacy.ide.eclipse.preview.screen.SelectedObject;
import org.openlegacy.terminal.actions.TerminalAction;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.utils.StringConstants;
import org.openlegacy.utils.StringUtil;

import java.lang.annotation.Annotation;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class ActionsPage extends AbstractPage {

	private final static String PAGE_ID = "org.openlegacy.enterprise.ide.eclipse.screen.pages.actions"; //$NON-NLS-1$

	private TableViewer tableViewer;

	private ScreenActionsModel actionsModel;

	/**
	 * @param editor
	 */
	public ActionsPage(ScreenEntityEditor editor) {
		super(editor, PAGE_ID, Messages.getString("ActionsPage.title"));//$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage#createFormContent()
	 */
	@Override
	public void createFormContent() {
		actionsModel = ((ScreenEntityEditor)getEntityEditor()).getEntity().getActionsModel().clone();

		FormToolkit toolkit = managedForm.getToolkit();
		ScrolledForm form = managedForm.getForm();

		form.setImage(Activator.getDefault().getImage(Activator.IMG_TRANSPARENT));
		form.setText(Messages.getString("ActionsPage.title") + "\n ");//$NON-NLS-1$ //$NON-NLS-2$
		form.setBackgroundImage(Activator.getDefault().getImage(Activator.IMG_FORM_BG));

		TableWrapLayout layout = new TableWrapLayout();
		layout.leftMargin = 10;
		layout.rightMargin = 10;
		form.getBody().setLayout(layout);

		// create section
		Section section = toolkit.createSection(form.getBody(), Section.DESCRIPTION | ExpandableComposite.TITLE_BAR);
		// section.marginWidth = 10;
		section.setText(Messages.getString("ActionsPageSection.name")); //$NON-NLS-1$
		section.setDescription(Messages.getString("ActionsPageSection.desc")); //$NON-NLS-1$
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
		gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.heightHint = 300;
		t.setLayoutData(gd);
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
		createColumns(tableViewer, ((ScreenEntityEditor)getEntityEditor()).getEntity());

		tableViewer.setContentProvider(new ActionsPageTableContentProvider());
		tableViewer.setInput(actionsModel);
		toolkit.paintBordersFor(client);
		// set client to section
		section.setClient(client);
	}

	@Override
	public void refresh() {
		actionsModel = ((ScreenEntityEditor)getEntityEditor()).getEntity().getActionsModel().clone();
		if (tableViewer != null) {
			tableViewer.setInput(actionsModel);
		}
	}

	@Override
	public void performSubscription() {
		ScreenEntityEditor editor = (ScreenEntityEditor)getEntityEditor();

		GeneralPage generalPage = (GeneralPage)editor.findPage(GeneralPage.PAGE_ID);
		generalPage.addSubscriber(ScreenAnnotationConstants.COLUMNS, this);
		generalPage.addSubscriber(ScreenAnnotationConstants.ROWS, this);
	}

	@Override
	public void revalidatePage(String key) {
		if (actionsModel == null) {
			return;
		}
		for (ActionModel actionModel : actionsModel.getActions()) {
			if (StringUtils.equals(key, ScreenAnnotationConstants.COLUMNS)) {
				validateColumns(actionModel);
			} else if (StringUtils.equals(key, ScreenAnnotationConstants.ROWS)) {
				validateRows(actionModel);
			}
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
				actionsModel.getActions().add(createActionModel());
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

					// remove validation markers
					ScreenEntityEditor editor = (ScreenEntityEditor)getEntityEditor();
					String validationMarkerKey = MessageFormat.format("{0}-{1}", model.getUuid(), "targetEntity");
					managedForm.getMessageManager().removeMessage(validationMarkerKey);
					editor.removeValidationMarker(validationMarkerKey);

					validationMarkerKey = MessageFormat.format("{0}-{1}", model.getUuid(), "attributes");
					managedForm.getMessageManager().removeMessage(validationMarkerKey);
					editor.removeValidationMarker(validationMarkerKey);

					validationMarkerKey = MessageFormat.format("{0}-{1}", model.getUuid(), "screenBoundsColumns");
					managedForm.getMessageManager().removeMessage(validationMarkerKey);
					editor.removeValidationMarker(validationMarkerKey);

					validationMarkerKey = MessageFormat.format("{0}-{1}", model.getUuid(), "screenBoundsRows");
					managedForm.getMessageManager().removeMessage(validationMarkerKey);
					editor.removeValidationMarker(validationMarkerKey);
				}
			}

		});

		// reset button
		Button resetButton = toolkit.createButton(panel, Messages.getString("Button.reset"), SWT.PUSH);//$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		resetButton.setLayoutData(gd);
		resetButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection structuredSelection = (IStructuredSelection)tableViewer.getSelection();
				if (structuredSelection.size() == 1) {
					ActionModel model = (ActionModel)structuredSelection.getFirstElement();
					model.setAction(null);
					model.setActionName("");
					model.setKeyboardKey(model.getDefaultKeyboardKey());
					model.setKeyboardKeyName(model.getDefaultKeyboardKey().getSimpleName());
					tableViewer.setInput(actionsModel);
					updateModel();
					tableViewer.setSelection(structuredSelection);
				}
			}

		});

	}

	private void createColumns(TableViewer viewer, ScreenEntity entity) {
		// "action" column
		TableViewerColumn vcol = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tcol = vcol.getColumn();
		tcol.setText(Messages.getString("ActionsPage.col.action"));//$NON-NLS-1$
		tcol.setResizable(true);
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

		// "additionalKey" column
		vcol = new TableViewerColumn(viewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("ActionsPage.col.additionalKey"));//$NON-NLS-1$
		tcol.setResizable(true);
		tcol.setWidth(100);

		List<String> items = new ArrayList<String>();
		items.add(AdditionalKey.NONE.toString());
		items.add(AdditionalKey.SHIFT.toString());
		items.add(AdditionalKey.CTRL.toString());
		items.add(AdditionalKey.ALT.toString());
		vcol.setEditingSupport(new ActionsComboBoxCellEditingSupport(viewer, ScreenAnnotationConstants.ADDITIONAL_KEY, items));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ActionModel action = (ActionModel)cell.getElement();
				cell.setText(action.getAdditionalKey().toString());
				updateModel();
			}
		});

		// "displayName" column
		vcol = new TableViewerColumn(viewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("ActionsPage.col.displayName"));//$NON-NLS-1$
		tcol.setResizable(true);
		tcol.setWidth(100);

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
		tcol.setText(Messages.getString("ActionsPage.col.alias"));//$NON-NLS-1$
		tcol.setResizable(true);
		tcol.setWidth(100);

		vcol.setEditingSupport(new ActionsTextCellEditingSupport(viewer, AnnotationConstants.ALIAS));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ActionDefinition action = (ActionDefinition)cell.getElement();
				cell.setText(action.getAlias());
				updateModel();
			}
		});

		// "row" column
		vcol = new TableViewerColumn(viewer, SWT.FILL);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("ActionsPage.col.row"));//$NON-NLS-1$
		tcol.setResizable(true);
		tcol.setWidth(100);

		vcol.setEditingSupport(new ActionsTextCellEditingSupport(viewer, AnnotationConstants.ROW));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ActionModel action = (ActionModel)cell.getElement();
				cell.setText(Integer.toString(action.getRow()));
				updateModel();
				validateAttributes(action);
				validateRows(action);
			}
		});

		// "column" column
		vcol = new TableViewerColumn(viewer, SWT.FILL);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("ActionsPage.col.column"));//$NON-NLS-1$
		tcol.setResizable(true);
		tcol.setWidth(100);

		vcol.setEditingSupport(new ActionsTextCellEditingSupport(viewer, AnnotationConstants.COLUMN));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ActionModel action = (ActionModel)cell.getElement();
				cell.setText(Integer.toString(action.getColumn()));
				updateModel();
				validateAttributes(action);
				validateColumns(action);
			}
		});

		// "length" column
		vcol = new TableViewerColumn(viewer, SWT.FILL);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("ActionsPage.col.length"));//$NON-NLS-1$
		tcol.setResizable(true);
		tcol.setWidth(100);

		vcol.setEditingSupport(new ActionsTextCellEditingSupport(viewer, AnnotationConstants.LENGTH));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ActionModel action = (ActionModel)cell.getElement();
				cell.setText(Integer.toString(action.getLength()));
				updateModel();
				validateAttributes(action);
				validateColumns(action);
			}
		});

		// "when" column
		vcol = new TableViewerColumn(viewer, SWT.FILL);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("ActionsPage.col.when"));//$NON-NLS-1$
		tcol.setResizable(true);
		tcol.setWidth(100);

		vcol.setEditingSupport(new ActionsTextCellEditingSupport(viewer, AnnotationConstants.WHEN));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ActionModel action = (ActionModel)cell.getElement();
				cell.setText(action.getWhen() == null ? "" : action.getWhen());
				updateModel();
				validateAttributes(action);
			}
		});
		// "focusField" column
		vcol = new TableViewerColumn(viewer, SWT.FILL);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("ActionsPage.col.focusField"));//$NON-NLS-1$
		tcol.setResizable(true);
		tcol.setWidth(100);

		vcol.setEditingSupport(new ActionsTextCellEditingSupport(viewer, ScreenAnnotationConstants.FOCUS_FIELD));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ActionModel action = (ActionModel)cell.getElement();
				cell.setText(action.getFocusField() == null ? "" : action.getFocusField());
				updateModel();
			}
		});
		// "type" column
		vcol = new TableViewerColumn(viewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("ActionsPage.col.type"));//$NON-NLS-1$
		tcol.setResizable(true);
		tcol.setWidth(100);

		items = new ArrayList<String>();
		items.add(ActionType.GENERAL.toString());
		items.add(ActionType.LOGICAL.toString());
		items.add(ActionType.NAVIGATION.toString());
		items.add(ActionType.WINDOW.toString());
		vcol.setEditingSupport(new ActionsComboBoxCellEditingSupport(viewer, ScreenAnnotationConstants.TYPE, items));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ActionModel action = (ActionModel)cell.getElement();
				cell.setText(action.getType().toString());
				updateModel();
			}
		});
		// "targetEntity" column
		vcol = new TableViewerColumn(viewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("ActionsPage.col.targetEntity"));//$NON-NLS-1$
		tcol.setResizable(true);
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
		// "sleep" column
		vcol = new TableViewerColumn(viewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("ActionsPage.col.sleep"));//$NON-NLS-1$
		tcol.setResizable(true);
		tcol.setWidth(100);

		vcol.setEditingSupport(new ActionsTextCellEditingSupport(viewer, ScreenAnnotationConstants.SLEEP));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ActionModel action = (ActionModel)cell.getElement();
				cell.setText(Integer.toString(action.getSleep()));
				updateModel();
			}
		});
		// "global" column
		vcol = new TableViewerColumn(viewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("ActionsPage.col.global"));//$NON-NLS-1$
		tcol.setResizable(true);
		tcol.setWidth(100);

		items = new ArrayList<String>();
		items.add(StringConstants.TRUE);
		items.add(StringConstants.FALSE);
		vcol.setEditingSupport(new ActionsComboBoxCellEditingSupport(viewer, ScreenAnnotationConstants.GLOBAL, items));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ActionModel action = (ActionModel)cell.getElement();
				cell.setText(action.isGlobal() ? StringConstants.TRUE : StringConstants.FALSE);
				updateModel();
			}
		});
		// "keyboardKey" column
		vcol = new TableViewerColumn(viewer, SWT.FILL);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("ActionsPage.col.keyboardKey"));//$NON-NLS-1$
		tcol.setResizable(true);
		tcol.setWidth(100);
		vcol.setEditingSupport(new ActionsDialogCellEditingSupport(viewer, AnnotationConstants.KEYBOARD_KEY));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ActionModel action = (ActionModel)cell.getElement();
				cell.setText(TerminalActions.NONE.class.getSimpleName().equals(action.getKeyboardKeyName()) ? ""
						: action.getKeyboardKeyName());
				updateModel();
			}
		});
	}

	private static ActionModel createActionModel() {
		ActionModel model = new ActionModel("", "", "", AdditionalKey.NONE, 0, 0, 0, null, "", ActionType.GENERAL,//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
				org.openlegacy.terminal.ScreenEntity.NONE.class.getSimpleName(), 0, true,
				TerminalActions.NONE.class.getSimpleName());

		ScreenPreview screenPreview = (ScreenPreview)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
				ScreenPreview.ID);
		if (screenPreview != null) {
			SelectedObject selectedObject = screenPreview.getSelectedObject();

			if (selectedObject != null) {
				FieldRectangle fieldRectangle = selectedObject.getFieldRectangle();
				String selectedText = fieldRectangle.getValue();

				if (selectedText != null && !(selectedText = selectedText.trim()).isEmpty()) {
					String[] selectedTextParts = selectedText.split("-|=");

					if (selectedTextParts.length == 2) {
						String key = selectedTextParts[0];
						String label = selectedTextParts[1];

						TerminalAction terminalAction = TerminalActions.newAction(key);
						if (terminalAction != null) {
							model.setAction(terminalAction);
							model.setActionName(terminalAction.getClass().getSimpleName());
						} else if (key.matches("^(F1[3-9])|(F2[0-4])$")) {
							// F13 -> SHIFT + F1 etc
							Integer fKey = Integer.parseInt(key.substring(1)) - 12;
							terminalAction = TerminalActions.newAction("F" + fKey);
							model.setAction(terminalAction);
							model.setAdditionalKey(AdditionalKey.SHIFT);
							model.setActionName(terminalAction.getClass().getSimpleName());
						}

						if (StringUtil.containsRTLChar(label)) {
							label = StringUtils.reverse(label);
						}

						model.setDisplayName(label);
						model.setAlias(StringUtil.toJavaFieldName(label));

						model.setRow(fieldRectangle.getRow());
						model.setColumn(fieldRectangle.getColumn() + fieldRectangle.getValue().indexOf(selectedText));
						model.setLength(selectedText.length());
						model.setWhen(".*" + key + ".*");
					}
				}
			}
		}

		return model;
	}

	private void updateModel() {
		try {
			ModelUpdater.updateScreenActionsModel(((ScreenEntityEditor)getEntityEditor()).getEntity(), actionsModel);
		} catch (Exception e) {
			ErrorDialog.openError(((IEditorPart)managedForm.getContainer()).getSite().getShell(),
					Messages.getString("error.problem.occurred"), e.getStackTrace().toString(), new Status(//$NON-NLS-1$
							IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage()));
		}
		setDirty(((ScreenEntityEditor)getEntityEditor()).getEntity().isDirty());
	}

	private void validateTargetEntityColumn(ActionModel model) {
		Class<?> targetEntity = model.getTargetEntity();
		boolean isScreenEntity = false;
		for (Annotation annotation : targetEntity.getDeclaredAnnotations()) {
			if (annotation.annotationType().getName().equals(org.openlegacy.annotations.screen.ScreenEntity.class.getName())) {
				isScreenEntity = true;
				break;
			}
		}
		String validationMarkerKey = MessageFormat.format("{0}-{1}", model.getUuid(), "targetEntity");//$NON-NLS-1$ //$NON-NLS-2$
		ScreenEntityEditor editor = (ScreenEntityEditor)getEntityEditor();
		if (isScreenEntity || targetEntity.getName().equals(org.openlegacy.terminal.ScreenEntity.NONE.class.getName())) {
			// remove validation marker
			managedForm.getMessageManager().removeMessage(validationMarkerKey);
			editor.removeValidationMarker(validationMarkerKey);
		} else {
			// add validation marker
			String message = MessageFormat.format("Target entity: {0} \n {1}", targetEntity.getName(),//$NON-NLS-1$
					Messages.getString("validation.selected.class.is.not.screen.entity"));//$NON-NLS-1$
			managedForm.getMessageManager().addMessage(validationMarkerKey, message, null, IMessageProvider.ERROR);
			editor.addValidationMarker(validationMarkerKey, message);
		}
	}

	// refs #599 - Row, Column, Length and When attributes must be set together. If one of them is missing, should be an
	// appropriate error.
	private void validateAttributes(ActionModel model) {
		String validationMarkerKey = MessageFormat.format("{0}-{1}", model.getUuid(), "attributes");//$NON-NLS-1$ //$NON-NLS-2$
		ScreenEntityEditor editor = (ScreenEntityEditor)getEntityEditor();
		if ((model.getRow() != 0 && model.getColumn() != 0 && model.getLength() != 0 && !StringUtils.isEmpty(model.getWhen()))
				|| (model.getRow() == 0 && model.getColumn() == 0 && model.getLength() == 0 && StringUtils.isEmpty(model.getWhen()))) {
			// remove validation marker
			managedForm.getMessageManager().removeMessage(validationMarkerKey);
			editor.removeValidationMarker(validationMarkerKey);
		} else {
			// add validation marker
			managedForm.getMessageManager().addMessage(validationMarkerKey,
					Messages.getString("validation.action.attributes.must.be.specified.together"), null, IMessageProvider.ERROR);//$NON-NLS-1$
			editor.addValidationMarker(validationMarkerKey,
					Messages.getString("validation.action.attributes.must.be.specified.together"));//$NON-NLS-1$
		}
	}

	private void validateColumns(ActionModel model) {
		if (managedForm == null) {
			// NOTE: managedForm can equal to NULL if user not visited Identifiers page yet
			return;
		}
		String validationMarkerKey = MessageFormat.format("{0}-{1}", model.getUuid(), "screenBoundsColumns");//$NON-NLS-1$ //$NON-NLS-2$
		ScreenEntityEditor editor = (ScreenEntityEditor)getEntityEditor();

		GeneralPage generalPage = (GeneralPage)editor.findPage(GeneralPage.PAGE_ID);
		ScreenEntityModel entityModel = (ScreenEntityModel)generalPage.getEditableNamedObject(ScreenEntityModel.class);

		if (model.getColumn() + model.getLength() > entityModel.getColumns()) {
			// add validation marker
			managedForm.getMessageManager().addMessage(validationMarkerKey,
					Messages.getString("validation.action.exceeds.screen.bounds"), null, IMessageProvider.ERROR);//$NON-NLS-1$
			editor.addValidationMarker(validationMarkerKey, Messages.getString("validation.action.exceeds.screen.bounds"));//$NON-NLS-1$
		} else {
			// remove validation markers
			managedForm.getMessageManager().removeMessage(validationMarkerKey);
			editor.removeValidationMarker(validationMarkerKey);
		}
	}

	private void validateRows(ActionModel model) {
		if (managedForm == null) {
			// NOTE: managedForm can equal to NULL if user not visited Identifiers page yet
			return;
		}
		String validationMarkerKey = MessageFormat.format("{0}-{1}", model.getUuid(), "screenBoundsRows");//$NON-NLS-1$ //$NON-NLS-2$
		ScreenEntityEditor editor = (ScreenEntityEditor)getEntityEditor();

		GeneralPage generalPage = (GeneralPage)editor.findPage(GeneralPage.PAGE_ID);
		ScreenEntityModel entityModel = (ScreenEntityModel)generalPage.getEditableNamedObject(ScreenEntityModel.class);

		if (model.getRow() > entityModel.getRows()) {
			// add validation marker
			managedForm.getMessageManager().addMessage(validationMarkerKey,
					Messages.getString("validation.action.exceeds.screen.bounds"), null, IMessageProvider.ERROR);//$NON-NLS-1$
			editor.addValidationMarker(validationMarkerKey, Messages.getString("validation.action.exceeds.screen.bounds"));//$NON-NLS-1$
		} else {
			// remove validation markers
			managedForm.getMessageManager().removeMessage(validationMarkerKey);
			editor.removeValidationMarker(validationMarkerKey);
		}
	}

}
