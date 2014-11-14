package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen;

import com.openlegacy.enterprise.ide.eclipse.Activator;
import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters.TerminalActionViewerFilter;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenNavigationModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.AssignedFieldsCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen.AssignedFieldsTableContentProvider;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.TextValidator;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.Utils;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
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
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.terminal.actions.TerminalAction.AdditionalKey;
import org.openlegacy.terminal.actions.TerminalActions;
import org.openlegacy.terminal.definitions.FieldAssignDefinition;
import org.openlegacy.terminal.definitions.SimpleFieldAssignDefinition;

import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class GeneralScreenNavigationDetailsPage extends AbstractScreenDetailsPage {

	private ScreenNavigationModel navigationModel;
	private TableViewer tableViewer;
	private Text accessedFromText;
	private CCombo terminalActionCombo;
	private CCombo exitActionCombo;

	private TextValidator accessedFromValidator;

	/**
	 * 
	 */
	public GeneralScreenNavigationDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.IDetailsPage#createContents(org.eclipse.swt.widgets.Composite)
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
		section.setText(Messages.getString("ScreenNavigationDetailsPage.name")); //$NON-NLS-1$
		section.setDescription(Messages.getString("ScreenNavigationDetailsPage.desc")); //$NON-NLS-1$
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

		// create row for "accessedFrom"
		accessedFromText = FormRowCreator.createStringRowWithBrowseButton(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenNavigation.accessedFrom"), "", ScreenAnnotationConstants.ACCESSED_FROM, null, true,//$NON-NLS-1$ //$NON-NLS-2$
				getAccessedFromClearListener());
		accessedFromValidator = new TextValidator(master, managedForm, accessedFromText, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateAccessedFromControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return navigationModel;
			}

		};
		// create row for "terminalAction"
		terminalActionCombo = FormRowCreator.createComboBoxRowWithBrowseButton(toolkit, client, mapCombos,
				getDefaultModifyListener(), getDefaultComboBoxKeyListener(),
				Messages.getString("ScreenNavigation.terminalAction"), getTerminalActions(), 0,//$NON-NLS-1$
				ScreenAnnotationConstants.TERMINAL_ACTION, new TerminalActionViewerFilter(), true,
				getTerminalActionClearListener());
		// create row for "additionalKey"
		FormRowCreator.createComboBoxRow(toolkit, client, mapCombos, getDefaultModifyListener(), getDefaultComboBoxKeyListener(),
				Messages.getString("ScreenNavigation.additionalKey"), getAdditionalKeys(), 0,//$NON-NLS-1$
				ScreenAnnotationConstants.ADDITIONAL_KEY, false);
		// create row for "exitAction"
		exitActionCombo = FormRowCreator.createComboBoxRowWithBrowseButton(toolkit, client, mapCombos,
				getDefaultModifyListener(), getDefaultComboBoxKeyListener(),
				Messages.getString("ScreenNavigation.exitAction"),//$NON-NLS-1$
				getTerminalActions(), 6, ScreenAnnotationConstants.EXIT_ACTION, new TerminalActionViewerFilter(), true,
				getExitActionClearListener());
		// create row for "exitAdditionalKey"
		FormRowCreator.createComboBoxRow(toolkit, client, mapCombos, getDefaultModifyListener(), getDefaultComboBoxKeyListener(),
				Messages.getString("ScreenNavigation.exitAdditionalKey"), getAdditionalKeys(), 0,//$NON-NLS-1$
				ScreenAnnotationConstants.EXIT_ADDITIONAL_KEY, false);
		// create row for "requiresParameters"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenNavigation.requiresParameter"), false, ScreenAnnotationConstants.REQUIRES_PARAMETERS);//$NON-NLS-1$
		// create row for "drilldownValue"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenNavigation.drilldownValue"), "", ScreenAnnotationConstants.DRILLDOWN_VALUE);//$NON-NLS-1$ $NON-NLS-2$
		// create section for "assignedFields"
		createAssignedFieldsSection(managedForm.getForm(), toolkit, topClient, ScreenAnnotationConstants.ASSIGNED_FIELDS);

		toolkit.paintBordersFor(section);
		section.setClient(topClient);
	}

	@Override
	public Class<?> getDetailsModel() {
		return ScreenNavigationModel.class;
	}

	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateScreenNavigationModel(getEntity(), navigationModel, key, (String)map.get(Constants.TEXT_VALUE),
				(Boolean)map.get(Constants.BOOL_VALUE), (String)map.get(Constants.FULLY_QUALIFIED_NAME_VALUE));
	}

	@Override
	protected void afterDoUpdateModel() {
		setDirty(getEntity().isDirty());
	}

	@Override
	public UUID getModelUUID() {
		return navigationModel != null ? navigationModel.getUUID() : null;
	}

	@Override
	public void revalidate() {}

	@Override
	protected void updateControls() {
		ControlsUpdater.updateScreenNavigationDetailsControls(navigationModel, mapTexts, mapCombos, mapCheckBoxes, tableViewer);
	}

	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			navigationModel = (ScreenNavigationModel)selection.getFirstElement();
		} else {
			navigationModel = null;
		}
	}

	@Override
	protected void updateValidators(UUID uuid) {
		super.updateValidators(uuid);
		if (accessedFromValidator != null) {
			accessedFromValidator.setModelUUID(uuid);
		}
	}

	private void createAssignedFieldsSection(final ScrolledForm form, FormToolkit toolkit, Composite parent, final String key) {
		Section section = toolkit.createSection(parent, ExpandableComposite.TWISTIE | Section.DESCRIPTION);
		section.setActiveToggleColor(toolkit.getHyperlinkGroup().getActiveForeground());
		section.setToggleColor(toolkit.getColors().getColor(IFormColors.SEPARATOR));
		toolkit.createCompositeSeparator(section);
		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;

		client.setLayout(layout);

		Table t = createTable(toolkit, client);
		tableViewer = createTableViewer(toolkit, t, key);
		createColumns(toolkit, tableViewer);

		Button b = toolkit.createButton(client, Messages.getString("Button.add"), SWT.PUSH); //$NON-NLS-1$
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		b.setLayoutData(gd);
		b.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				navigationModel.getAssignedFields().add(new SimpleFieldAssignDefinition("", ""));//$NON-NLS-1$ //$NON-NLS-2$
				updatingControls = true;
				ControlsUpdater.updateScreenNavigationDetailsControls(navigationModel, mapTexts, mapCombos, mapCheckBoxes,
						tableViewer);
				updatingControls = false;
				updateModel(key);
				tableViewer.getTable().select(tableViewer.getTable().getItemCount() - 1);
			}

		});

		section.setText(Messages.getString("ScreenNavigation.assignedFields"));//$NON-NLS-1$
		section.setClient(client);
		section.setExpanded(true);
		section.addExpansionListener(new ExpansionAdapter() {

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(false);
			}
		});
		gd = new GridData(GridData.FILL_BOTH);
		section.setLayoutData(gd);
	}

	private static Table createTable(FormToolkit toolkit, Composite parent) {
		Table t = toolkit.createTable(parent, SWT.FULL_SELECTION);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.heightHint = 200;
		gd.widthHint = 100;

		t.setLayoutData(gd);
		t.setHeaderVisible(true);
		t.setLinesVisible(true);

		toolkit.paintBordersFor(parent);
		return t;
	}

	private TableViewer createTableViewer(FormToolkit toolkit, Table t, String key) {
		TableViewer v = new TableViewer(t);
		v.setContentProvider(new AssignedFieldsTableContentProvider());
		v.setInput(navigationModel);
		v.setData(FormRowCreator.ID_KEY, key);
		return v;
	}

	private void createColumns(FormToolkit toolkit, final TableViewer viewer) {
		TableViewerColumn vcol = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tcol = vcol.getColumn();
		tcol.setResizable(false);
		tcol.setWidth(150);
		tcol.setText(Messages.getString("ScreenNavigationDetailsPage.col.name"));//$NON-NLS-1$

		vcol.setEditingSupport(new AssignedFieldsCellEditingSupport(viewer, ScreenAnnotationConstants.FIELD));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				FieldAssignDefinition field = (FieldAssignDefinition)cell.getElement();
				cell.setText(field.getName());
				if (!updatingControls) {
					updateModel((String)viewer.getData(FormRowCreator.ID_KEY));
				}
			}
		});

		vcol = new TableViewerColumn(viewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setResizable(false);
		tcol.setWidth(50);
		tcol.setText(Messages.getString("ScreenNavigationDetailsPage.col.val"));//$NON-NLS-1$

		vcol.setEditingSupport(new AssignedFieldsCellEditingSupport(viewer, ScreenAnnotationConstants.VALUE));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				FieldAssignDefinition field = (FieldAssignDefinition)cell.getElement();
				cell.setText(field.getValue());
			}
		});
	}

	private static String[] getAdditionalKeys() {
		List<String> list = new ArrayList<String>();
		list.add(AdditionalKey.NONE.toString());
		list.add(AdditionalKey.ALT.toString());
		list.add(AdditionalKey.CTRL.toString());
		list.add(AdditionalKey.SHIFT.toString());
		return list.toArray(new String[] {});
	}

	private static String[] getTerminalActions() {
		List<String> list = new ArrayList<String>();
		list.add(TerminalActions.ENTER.class.getSimpleName());
		list.add(TerminalActions.ESCAPE.class.getSimpleName());
		list.add(TerminalActions.PAGE_DOWN.class.getSimpleName());
		list.add(TerminalActions.PAGE_UP.class.getSimpleName());
		list.add(TerminalActions.F1.class.getSimpleName());
		list.add(TerminalActions.F2.class.getSimpleName());
		list.add(TerminalActions.F3.class.getSimpleName());
		list.add(TerminalActions.F4.class.getSimpleName());
		list.add(TerminalActions.F5.class.getSimpleName());
		list.add(TerminalActions.F6.class.getSimpleName());
		list.add(TerminalActions.F7.class.getSimpleName());
		list.add(TerminalActions.F8.class.getSimpleName());
		list.add(TerminalActions.F9.class.getSimpleName());
		list.add(TerminalActions.F10.class.getSimpleName());
		list.add(TerminalActions.F11.class.getSimpleName());
		list.add(TerminalActions.F12.class.getSimpleName());
		return list.toArray(new String[] {});
	}

	private SelectionListener getAccessedFromClearListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				accessedFromText.setText("");//$NON-NLS-1$
				try {
					ModelUpdater.updateScreenNavigationModel(getEntity(), navigationModel,
							ScreenAnnotationConstants.ACCESSED_FROM, "", false, null);
					setDirty(getEntity().isDirty());
				} catch (Exception ex) {
					ErrorDialog.openError(((IEditorPart)managedForm.getContainer()).getSite().getShell(),
							Messages.getString("error.problem.occurred"), ex.getMessage(), new Status(//$NON-NLS-1$
									IStatus.ERROR, Activator.PLUGIN_ID, ex.getMessage()));
				}
			}
		};
	}

	private SelectionListener getTerminalActionClearListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				terminalActionCombo.setText("");//$NON-NLS-1$
				try {
					ModelUpdater.updateScreenNavigationModel(getEntity(), navigationModel,
							ScreenAnnotationConstants.TERMINAL_ACTION, TerminalActions.ENTER.class.getSimpleName(), false, null);
					setDirty(getEntity().isDirty());
				} catch (Exception ex) {
					ErrorDialog.openError(((IEditorPart)managedForm.getContainer()).getSite().getShell(),
							Messages.getString("error.problem.occurred"), ex.getMessage(), new Status(//$NON-NLS-1$
									IStatus.ERROR, Activator.PLUGIN_ID, ex.getMessage()));
				}
			}
		};
	}

	private SelectionListener getExitActionClearListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				exitActionCombo.setText("");//$NON-NLS-1$
				try {
					ModelUpdater.updateScreenNavigationModel(getEntity(), navigationModel, ScreenAnnotationConstants.EXIT_ACTION,
							TerminalActions.F3.class.getSimpleName(), false, null);
					setDirty(getEntity().isDirty());
				} catch (Exception ex) {
					ErrorDialog.openError(((IEditorPart)managedForm.getContainer()).getSite().getShell(),
							Messages.getString("error.problem.occurred"), ex.getMessage(), new Status(//$NON-NLS-1$
									IStatus.ERROR, Activator.PLUGIN_ID, ex.getMessage()));
				}
			}
		};
	}

	private boolean validateAccessedFromControl(TextValidator validator, UUID uuid) {
		boolean isValid = true;
		String text = validator.getControl().getText();
		String fullyQuailifiedName = (String)validator.getControl().getData(FormRowCreator.ID_FULLY_QUALIFIED_NAME);
		if (StringUtils.isEmpty(fullyQuailifiedName) || StringUtils.isEmpty(text)) {
			return isValid;
		}
		if (getEntity().getEntityFullyQualifiedName().equals(fullyQuailifiedName)) {
			isValid = false;
		}
		if (!isValid) {
			validator.addMessage(
					Messages.getString("validation.accessed.from.equal.to.current.entity"), IMessageProvider.ERROR, uuid);//$NON-NLS-1$
		}
		boolean isScreenEntity = false;
		try {
			Class<?> clazz = Utils.getClazz(fullyQuailifiedName);
			for (Annotation annotation : clazz.getDeclaredAnnotations()) {
				if (annotation.annotationType().getName().equals(ScreenEntity.class.getName())) {
					isScreenEntity = true;
					break;
				}
			}
		} catch (MalformedURLException e) {
		} catch (CoreException e) {
		}
		if (!isScreenEntity) {
			isValid = false;
			validator.addMessage(
					Messages.getString("validation.selected.class.is.not.screen.entity"), IMessageProvider.ERROR, uuid);//$NON-NLS-1$
		}
		return isValid;
	}
}
