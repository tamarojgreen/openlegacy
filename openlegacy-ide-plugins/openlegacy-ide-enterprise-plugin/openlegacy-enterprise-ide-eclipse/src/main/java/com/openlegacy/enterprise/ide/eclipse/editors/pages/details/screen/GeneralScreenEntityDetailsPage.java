package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters.ScreenTypeViewerFilter;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenNamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.IOpenLegacyPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.RolesCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.holders.screen.ScreenEntityRole;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen.RolesTableContentProvider;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.TextValidator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IMessageProvider;
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
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.openlegacy.EntityType;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.terminal.ScreenSize;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class GeneralScreenEntityDetailsPage extends AbstractScreenDetailsPage {

	private ScreenEntityModel entityModel;
	private TextValidator columnsValidator;
	private TextValidator rowsValidator;
	private TableViewer tableViewer;

	/**
	 * 
	 */
	public GeneralScreenEntityDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

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
		section.setText(Messages.getString("ScreenEntityDetailsPage.name")); //$NON-NLS-1$
		section.setDescription(Messages.getString("ScreenEntityDetailsPage.desc")); //$NON-NLS-1$
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
		// create row for "name"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenEntity.name"), "", AnnotationConstants.NAME);//$NON-NLS-1$ //$NON-NLS-2$
		// crate row for "displayName"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenEntity.displayName"), "", AnnotationConstants.DISPLAY_NAME);//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "supportTerminalData"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenEntity.supportTerminalData"), false, ScreenAnnotationConstants.SUPPORT_TERMINAL_DATA);//$NON-NLS-1$
		// create row for "window"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenEntity.window"), false, ScreenAnnotationConstants.WINDOW);//$NON-NLS-1$
		// create row for "screenType"
		FormRowCreator.createComboBoxRowWithBrowseButton(toolkit, client, mapCombos, getDefaultModifyListener(),
				getDefaultComboBoxKeyListener(), Messages.getString("ScreenEntity.screenType"), getComboItems(), 0,//$NON-NLS-1$
				ScreenAnnotationConstants.SCREEN_TYPE, new ScreenTypeViewerFilter());
		// create row for "columns"
		Text columnsControl = FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				getDefaultVerifyListener(),
				Messages.getString("ScreenEntity.columns"), ScreenSize.DEFAULT_COLUMN, ScreenAnnotationConstants.COLUMNS);//$NON-NLS-1$
		// add validator
		columnsValidator = new TextValidator(master, managedForm, columnsControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateColumnsText(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return entityModel;
			}

		};
		// create row for "rows"
		Text rowsControl = FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				getDefaultVerifyListener(),
				Messages.getString("ScreenEntity.rows"), ScreenSize.DEFAULT_ROWS, ScreenAnnotationConstants.ROWS);//$NON-NLS-1$
		// add validator
		rowsValidator = new TextValidator(master, managedForm, rowsControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateRowsText(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return entityModel;
			}

		};
		// create row for "child"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenEntity.child"), false, ScreenAnnotationConstants.CHILD);//$NON-NLS-1$
		// create row for "validateKeys"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenEntity.validateKeys"), true, ScreenAnnotationConstants.VALIDATE_KEYS);//$NON-NLS-1$
		// create row for "rightToLeft"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenEntity.rightToLeft"), false, ScreenAnnotationConstants.RIGHT_TO_LEFT);//$NON-NLS-1$
		// create row for "autoMapKeyboardActions"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenEntity.autoMapKeyboardActions"), false,//$NON-NLS-1$
				ScreenAnnotationConstants.AUTO_MAP_KEYBOARD_ACTIONS);
		// create section for "roles"
		createRolesSection(managedForm.getForm(), toolkit, topClient, ScreenAnnotationConstants.ROLES);

		toolkit.paintBordersFor(section);
		section.setClient(topClient);
	}

	@Override
	public Class<?> getDetailsModel() {
		return ScreenEntityModel.class;
	}

	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateScreenEntityModel(getEntity(), entityModel, key, (String)map.get(Constants.TEXT_VALUE),
				(Boolean)map.get(Constants.BOOL_VALUE), (String)map.get(Constants.FULLY_QUALIFIED_NAME_VALUE));
		// revalidate all subscribers
		List<IOpenLegacyPage> subscribers = getPage().getSubscribers(key);
		for (IOpenLegacyPage subscriber : subscribers) {
			subscriber.revalidatePage(key);
		}
	}

	@Override
	protected void afterDoUpdateModel() {
		setDirty(getEntity().isDirty());
	}

	@Override
	public UUID getModelUUID() {
		return entityModel != null ? entityModel.getUUID() : null;
	}

	@Override
	protected void updateControls() {
		ControlsUpdater.updateScreenEntityDetailsControls(entityModel, mapTexts, mapCombos, mapCheckBoxes, tableViewer);
	}

	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			entityModel = (ScreenEntityModel)selection.getFirstElement();
		} else {
			entityModel = null;
		}
	}

	@Override
	protected void updateValidators(UUID uuid) {
		if (uuid != null) {
			columnsValidator.setModelUUID(uuid);
			rowsValidator.setModelUUID(uuid);
		}
	}

	@Override
	public void revalidate() {}

	@Override
	public ScreenNamedObject getPageScreenNamedObject() {
		return entityModel;
	}

	// *************************** PRIVATE **************************************

	private static boolean validateColumnsText(TextValidator validator, UUID uuid) {
		String text = validator.getControl().getText();
		boolean isValid = true;
		// check if empty or contains any character that is not a digit
		isValid = text.isEmpty() ? false : !text.matches("\\D");//$NON-NLS-1$
		// if previous condition is valid then check range
		isValid = isValid ? (new Integer(text).intValue() > 0) : false;
		if (!isValid) {
			validator.addMessage(Messages.getString("validation.should.be.positive"), IMessageProvider.ERROR, uuid);//$NON-NLS-1$
			return isValid;
		}
		return isValid;
	}

	private static boolean validateRowsText(TextValidator validator, UUID uuid) {
		String text = validator.getControl().getText();
		boolean isValid = true;
		// check if empty or contains any character that is not a digit
		isValid = text.isEmpty() ? false : !text.matches("\\D");//$NON-NLS-1$
		// if previous condition is valid then check range
		isValid = isValid ? (new Integer(text).intValue() > 0) : false;
		if (!isValid) {
			validator.addMessage(Messages.getString("validation.should.be.positive"), IMessageProvider.ERROR, uuid);//$NON-NLS-1$
			return isValid;
		}
		return isValid;
	}

	private static String[] getComboItems() {
		Collection<Class<? extends EntityType>> collection = ScreenEntityModel.mapScreenTypes.values();
		List<String> list = new ArrayList<String>();
		for (Class<? extends EntityType> clazz : collection) {
			list.add(clazz.getSimpleName());
		}
		return list.toArray(new String[] {});
	}

	private void createRolesSection(final ScrolledForm form, FormToolkit toolkit, Composite parent, final String key) {
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
				entityModel.getRoles().add(new ScreenEntityRole(""));//$NON-NLS-1$
				updatingControls = true;
				ControlsUpdater.updateScreenEntityDetailsControls(entityModel, mapTexts, mapCombos, mapCheckBoxes, tableViewer);
				updatingControls = false;
				updateModel(key);
				tableViewer.getTable().select(tableViewer.getTable().getItemCount() - 1);
			}

		});

		section.setText(Messages.getString("ScreenEntity.roles"));//$NON-NLS-1$
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
		v.setContentProvider(new RolesTableContentProvider());
		v.setInput(entityModel);
		v.setData(FormRowCreator.ID_KEY, key);
		return v;
	}

	private void createColumns(FormToolkit toolkit, final TableViewer viewer) {
		TableViewerColumn vcol = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tcol = vcol.getColumn();
		tcol.setResizable(false);
		tcol.setWidth(250);
		tcol.setText(Messages.getString("ScreenEntityDetailsPage.col.role"));//$NON-NLS-1$

		vcol.setEditingSupport(new RolesCellEditingSupport(viewer, AnnotationConstants.ROLE));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ScreenEntityRole role = (ScreenEntityRole)cell.getElement();
				cell.setText(role.getRole());
				if (!updatingControls) {
					updateModel((String)viewer.getData(FormRowCreator.ID_KEY));
				}
			}
		});
	}
}
