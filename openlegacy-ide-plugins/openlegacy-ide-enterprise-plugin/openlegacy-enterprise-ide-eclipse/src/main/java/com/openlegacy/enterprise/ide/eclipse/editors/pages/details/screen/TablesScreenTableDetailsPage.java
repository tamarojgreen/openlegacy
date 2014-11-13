package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters.TableCollectorViewerFilter;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters.TerminalActionViewerFilter;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenColumnModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenTableModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.TextValidator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.ide.eclipse.preview.screen.FieldRectangle;
import org.openlegacy.ide.eclipse.preview.screen.ScreenPreview;
import org.openlegacy.terminal.actions.TerminalActions;

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
public class TablesScreenTableDetailsPage extends AbstractScreenDetailsPage {

	private ScreenTableModel tableModel;
	private TextValidator startRowValidator;
	private TextValidator endRowValidator;
	private TextValidator classNameValidator;

	public TablesScreenTableDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	@Override
	public Class<?> getDetailsModel() {
		return ScreenTableModel.class;
	}

	@Override
	public void createContents(Composite parent) {
		TableWrapLayout layout = new TableWrapLayout();
		layout.topMargin = 9;
		layout.leftMargin = 5;
		layout.rightMargin = 2;
		layout.bottomMargin = 2;
		parent.setLayout(layout);

		FormToolkit toolkit = managedForm.getToolkit();
		Section section = toolkit.createSection(parent, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR);
		section.marginWidth = 10;
		section.marginHeight = 0;
		section.setText(Messages.getString("ScreenTableDetailsPage.name")); //$NON-NLS-1$
		section.setDescription(Messages.getString("ScreenTableDetailsPage.desc")); //$NON-NLS-1$
		TableWrapData td = new TableWrapData(TableWrapData.FILL, TableWrapData.TOP);
		td.grabHorizontal = true;
		section.setLayoutData(td);

		Composite client = toolkit.createComposite(section);
		GridLayout glayout = new GridLayout();
		glayout.marginWidth = glayout.marginHeight = 0;
		glayout.numColumns = 2;
		client.setLayout(glayout);

		// create empty row
		FormRowCreator.createSpacer(toolkit, client, 2);
		// create row for "className"
		Text classNameControl = FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenTable.className"), "", Constants.JAVA_TYPE_NAME);
		classNameValidator = new TextValidator(master, managedForm, classNameControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateTextControls(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return tableModel;
			}

		};
		// create row for "startRow"
		Text startRowControl = FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				getDefaultVerifyListener(), Messages.getString("ScreenTable.startRow"), -1, ScreenAnnotationConstants.START_ROW);//$NON-NLS-1$
		startRowValidator = new TextValidator(master, managedForm, startRowControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateStartEndRowControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return tableModel;
			}

		};
		// create row for "endRow"
		Text endRowControl = FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				getDefaultVerifyListener(), Messages.getString("ScreenTable.endRow"), -1, ScreenAnnotationConstants.END_ROW);//$NON-NLS-1$
		endRowValidator = new TextValidator(master, managedForm, endRowControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateStartEndRowControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return tableModel;
			}

		};
		// create row for "name"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenTable.name"), "", AnnotationConstants.NAME);//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "nextScreenAction"
		FormRowCreator.createComboBoxRowWithBrowseButton(toolkit, client, mapCombos, getDefaultModifyListener(),
				getDefaultComboBoxKeyListener(), Messages.getString("ScreenTable.nextScreenAction"), getTerminalActions(), 2,//$NON-NLS-1$
				ScreenAnnotationConstants.NEXT_SCREEN_ACTION, new TerminalActionViewerFilter());
		// create row for "previousScreenAction"
		FormRowCreator.createComboBoxRowWithBrowseButton(toolkit, client, mapCombos, getDefaultModifyListener(),
				getDefaultComboBoxKeyListener(), Messages.getString("ScreenTable.prevScreenAction"), getTerminalActions(), 3,//$NON-NLS-1$
				ScreenAnnotationConstants.PREV_SCREEN_ACTION, new TerminalActionViewerFilter());
		// create row for "supportTerminalData"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenTable.supportTerminalData"), false, ScreenAnnotationConstants.SUPPORT_TERMINAL_DATA);//$NON-NLS-1$
		// create row for "scrollable"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenTable.scrollable"), true, ScreenAnnotationConstants.SCROLLABLE);//$NON-NLS-1$
		// create row for "tableCollector"
		FormRowCreator.createStringRowWithBrowseButton(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenTable.tableCollector"), "", ScreenAnnotationConstants.TABLE_COLLECTOR,//$NON-NLS-1$ //$NON-NLS-2$
				new TableCollectorViewerFilter());
		// create row for "rowGaps"
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("ScreenTable.rowGaps"), 1, ScreenAnnotationConstants.ROW_GAPS);//$NON-NLS-1$
		// create row for "screensCount"
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("ScreenTable.screensCount"), 1, ScreenAnnotationConstants.SCREENS_COUNT);//$NON-NLS-1$
		// create row for "filterExpression"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenTable.filterExpression"), "", ScreenAnnotationConstants.FILTER_EXPRESSION);//$NON-NLS-1$ //$NON-NLS-2$

		toolkit.paintBordersFor(section);
		section.setClient(client);

	}

	/**
	 * @param validator
	 * @param uuid
	 * @return
	 */
	private boolean validateStartEndRowControl(TextValidator validator, UUID uuid) {
		String text = validator.getControl().getText();
		boolean isValid = true;
		// check if empty or contains any character that is not a digit
		isValid = text.isEmpty() ? false : !text.matches("\\D");//$NON-NLS-1$
		// if previous condition is valid then check range
		isValid = isValid ? (new Integer(text).intValue() >= Constants.MIN_ROW_COLUMN)
				&& (new Integer(text).intValue() <= getEntity().getEntityModel().getRows()) : false;
		if (!isValid) {
			validator.addMessage(MessageFormat.format("{0} {1}-{2}", Messages.getString("validation.is.out.of.range"),//$NON-NLS-1$//$NON-NLS-2$
					Constants.MIN_ROW_COLUMN, getEntity().getEntityModel().getRows()), IMessageProvider.ERROR, uuid);
			return isValid;
		}
		return isValid;
	}

	/**
	 * @param validator
	 * @param uuid
	 * @return
	 */
	private static boolean validateTextControls(TextValidator validator, UUID uuid) {
		String text = validator.getControl().getText();
		boolean isValid = !text.isEmpty();
		if (!isValid) {
			validator.addMessage(Messages.getString("validation.should.not.be.empty"), IMessageProvider.ERROR, uuid);//$NON-NLS-1$
			return isValid;
		}
		return isValid;
	}

	@Override
	public UUID getModelUUID() {
		return tableModel != null ? tableModel.getUUID() : null;
	}

	@Override
	public void revalidate() {
		if (startRowValidator != null) {
			startRowValidator.revalidate(getModelUUID());
			endRowValidator.revalidate(getModelUUID());
			classNameValidator.revalidate(getModelUUID());
		}
	}

	@Override
	protected void updateControls() {
		ControlsUpdater.updateScreenTableDetailsControls(tableModel, mapTexts, mapCombos, mapCheckBoxes);
		revalidate();
	}

	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateScreenTableModel(getEntity(), tableModel, key, (String)map.get(Constants.TEXT_VALUE),
				(Boolean)map.get(Constants.BOOL_VALUE), (String)map.get(Constants.FULLY_QUALIFIED_NAME_VALUE));
	}

	@Override
	protected void afterDoUpdateModel() {
		setScreenPreviewDrawingRectangle(tableModel);
		setDirty(getEntity().isDirty());
	}

	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			tableModel = (ScreenTableModel)selection.getFirstElement();
		} else {
			tableModel = null;
		}
		if (tableModel != null) {
			setScreenPreviewDrawingRectangle(tableModel);
		}
	}

	@Override
	protected void updateValidators(UUID uuid) {
		if (uuid != null && startRowValidator != null) {
			startRowValidator.setModelUUID(uuid);
			endRowValidator.setModelUUID(uuid);
			classNameValidator.setModelUUID(uuid);
		}
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

	@Override
	public void removeValidationMarkers() {
		if (startRowValidator != null) {
			startRowValidator.removeValidationMarker();
			endRowValidator.removeValidationMarker();
			classNameValidator.removeValidationMarker();
		}
	}

	private static void setScreenPreviewDrawingRectangle(ScreenTableModel model) {
		ScreenPreview screenPreview = (ScreenPreview)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
				ScreenPreview.ID);
		if (screenPreview != null) {
			int startRow = model.getStartRow();
			int endRow = model.getEndRow();

			List<ScreenColumnModel> sortedColumns = model.getSortedColumns();
			int startColumn = sortedColumns.size() > 0 ? sortedColumns.get(0).getStartColumn() : 0;
			for (ScreenColumnModel column : sortedColumns) {
				if (startColumn > column.getStartColumn()) {
					startColumn = column.getStartColumn();
				}
			}
			int endColumn = sortedColumns.size() > 0 ? sortedColumns.get(0).getEndColumn() : 0;
			for (ScreenColumnModel column : sortedColumns) {
				if (endColumn < column.getEndColumn()) {
					endColumn = column.getEndColumn();
				}
			}
			screenPreview.addFieldRectangle(
					new FieldRectangle(startRow, endRow, startColumn, endColumn, ""), SWT.COLOR_YELLOW, true);//$NON-NLS-1$
		}
	}

}
