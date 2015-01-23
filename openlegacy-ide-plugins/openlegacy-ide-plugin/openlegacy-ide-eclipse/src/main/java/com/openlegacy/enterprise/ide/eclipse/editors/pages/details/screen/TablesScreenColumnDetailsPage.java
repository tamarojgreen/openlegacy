package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen;

import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
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
import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.ScreenSize;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenColumnModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenNamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenTableModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator.JAVA_DOCUMENTATION_TYPE;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.TextValidator;

/**
 * @author Ivan Bort
 * 
 */
public class TablesScreenColumnDetailsPage extends AbstractScreenDetailsPage {

	private ScreenColumnModel columnModel;
	private TextValidator startColumnValidator;
	private TextValidator endColumnValidator;

	private Button editableButton = null;

	public TablesScreenColumnDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	@Override
	public Class<?> getDetailsModel() {
		return ScreenColumnModel.class;
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
		section.setText(Messages.getString("ScreenColumnDetailsPage.name")); //$NON-NLS-1$
		section.setDescription(Messages.getString("ScreenColumnDetailsPage.desc")); //$NON-NLS-1$
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

		// create row for displaying java type name
		FormRowCreator.createLabelRow(toolkit, client, mapLabels,
				Messages.getString("ScreenColumn.javaType"), "", Constants.JAVA_TYPE_NAME, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenColumn");//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "fieldName"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenColumn.fieldName"), "", Constants.FIELD_NAME, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenColumn");//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "key"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenColumn.key"), false, AnnotationConstants.KEY, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenColumn");//$NON-NLS-1$
		// create row for "selectionField"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getSelectionFieldListener(),
				Messages.getString("ScreenColumn.selectionField"), false, ScreenAnnotationConstants.SELECTION_FIELD, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenColumn");//$NON-NLS-1$
		// create row for "mainDisplayField"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenColumn.mainDisplayField"), false, AnnotationConstants.MAIN_DISPLAY_FIELD, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenColumn");//$NON-NLS-1$
		// create row for "startColumn"
		Text startColumnControl = FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				getDefaultVerifyListener(),
				Messages.getString("ScreenColumn.startColumn"), -1, ScreenAnnotationConstants.START_COLUMN, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenColumn");//$NON-NLS-1$
		startColumnValidator = new TextValidator(master, managedForm, startColumnControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateColumnControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return columnModel;
			}

		};

		// create row for "endColumn"
		Text endColumnControl = FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				getDefaultVerifyListener(),
				Messages.getString("ScreenColumn.endColumn"), -1, ScreenAnnotationConstants.END_COLUMN, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenColumn");//$NON-NLS-1$
		endColumnValidator = new TextValidator(master, managedForm, endColumnControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateEndColumnControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return columnModel;
			}

		};

		// create row for "editable"
		editableButton = FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenColumn.editable"), false, AnnotationConstants.EDITABLE, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenColumn");//$NON-NLS-1$
		// create row for "displayName"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenColumn.displayName"), "", AnnotationConstants.DISPLAY_NAME, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenColumn");//$NON-NLS-1$
		// create row for "sampleValue"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenColumn.sampleValue"), "", AnnotationConstants.SAMPLE_VALUE, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenColumn");//$NON-NLS-1$
		// create row for "rowsOffset"
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("ScreenColumn.rowsOffset"), 0, ScreenAnnotationConstants.ROWS_OFFSET, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenColumn");//$NON-NLS-1$
		// create row for "helpText"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenColumn.helpText"), "", AnnotationConstants.HELP_TEXT, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenColumn");//$NON-NLS-1$
		// create row for "colSpan"
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("ScreenColumn.colSpan"), 1, ScreenAnnotationConstants.COL_SPAN, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenColumn");//$NON-NLS-1$
		// create row for "sortIndex"
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("ScreenColumn.sortIndex"), -1, ScreenAnnotationConstants.SORT_INDEX, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenColumn");//$NON-NLS-1$
		// create row for "attribute"
		FormRowCreator.createComboBoxRow(toolkit, client, mapCombos, getDefaultModifyListener(), getDefaultComboBoxKeyListener(),
				Messages.getString("ScreenColumn.attribute"), getFieldAttributes(), 0, AnnotationConstants.ATTRIBUTE, false, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenColumn");//$NON-NLS-1$
		// create row for "targetEntity"
		FormRowCreator.createStringRowWithBrowseButton(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenColumn.targetEntity"), "", ScreenAnnotationConstants.TARGET_ENTITY, null, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenColumn");
		// create row for "expression"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenColumn.expression"), "", ScreenAnnotationConstants.EXPRESSION, JAVA_DOCUMENTATION_TYPE.SCREEN, "ScreenColumn");//$NON-NLS-1$ //$NON-NLS-2$

		toolkit.paintBordersFor(section);
		section.setClient(client);
	}

	@Override
	public ScreenNamedObject getPageScreenNamedObject() {
		return null;
	}

	/**
	 * @param validator
	 * @param uuid
	 * @return
	 */
	private boolean validateColumnControl(TextValidator validator, UUID uuid) {
		String text = validator.getControl().getText();
		boolean isValid = true;
		// check if empty or contains any character that is not a digit
		isValid = text.isEmpty() ? false : !text.matches("\\D");//$NON-NLS-1$
		// if previous condition is valid then check range
		int maxCols = ScreenSize.DEFAULT_COLUMN < getEntity().getEntityModel().getColumns() ? getEntity().getEntityModel().getColumns()
				: ScreenSize.DEFAULT_COLUMN;
		isValid = isValid ? (Integer.parseInt(text) >= Constants.MIN_ROW_COLUMN) && (Integer.parseInt(text) <= maxCols) : false;
		if (!isValid) {
			validator.addMessage(MessageFormat.format("{0} {1}-{2}", Messages.getString("validation.is.out.of.range"),//$NON-NLS-1$//$NON-NLS-2$
					Constants.MIN_ROW_COLUMN, maxCols), IMessageProvider.ERROR, uuid);
			return isValid;
		}

		return isValid;
	}

	private boolean validateEndColumnControl(TextValidator validator, UUID uuid) {
		boolean isValid = validateColumnControl(validator, uuid);
		if (isValid) {
			// end column can't be smaller then start column
			String text = validator.getControl().getText();
			// check if empty or contains any character that is not a digit
			isValid = text.isEmpty() ? false : !text.matches("\\D");//$NON-NLS-1$
			if (columnModel == null || !isValid) {
				return isValid;
			}
			int maxCols = ScreenSize.DEFAULT_COLUMN < getEntity().getEntityModel().getColumns() ? getEntity().getEntityModel().getColumns()
					: ScreenSize.DEFAULT_COLUMN;
			isValid = isValid ? (Integer.parseInt(text) >= columnModel.getStartColumn()) && (Integer.parseInt(text) <= maxCols)
					: false;
			if (!isValid) {
				validator.addMessage(MessageFormat.format("{0} {1}-{2}", Messages.getString("validation.is.out.of.range"),//$NON-NLS-1$ //$NON-NLS-2$
						columnModel.getStartColumn(), maxCols), IMessageProvider.ERROR, uuid);
				return isValid;
			}
		}
		return isValid;
	}

	private static String[] getFieldAttributes() {
		List<String> list = new ArrayList<String>();
		list.add(FieldAttributeType.Value.toString());
		list.add(FieldAttributeType.Editable.toString());
		list.add(FieldAttributeType.Color.toString());
		return list.toArray(new String[] {});
	}

	@Override
	public UUID getModelUUID() {
		return columnModel != null ? columnModel.getUUID() : null;
	}

	@Override
	public void revalidate() {
		if (startColumnValidator != null) {
			startColumnValidator.revalidate(getModelUUID());
			endColumnValidator.revalidate(getModelUUID());
		}
	}

	@Override
	protected void updateControls() {
		ControlsUpdater.updateScreenColumnDetailsControls(columnModel, mapTexts, mapCheckBoxes, mapLabels, mapCombos);
		revalidate();
	}

	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateScreenColumnModel(getEntity(), columnModel, key, (String)map.get(Constants.TEXT_VALUE),
				(Boolean)map.get(Constants.BOOL_VALUE), (String)map.get(Constants.FULLY_QUALIFIED_NAME_VALUE));
	}

	@Override
	protected void afterDoUpdateModel() {
		setScreenPreviewDrawingRectangle(columnModel);
		setDirty(getEntity().isDirty());
	}

	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			columnModel = (ScreenColumnModel)selection.getFirstElement();
		} else {
			columnModel = null;
		}
		if (columnModel != null) {
			setScreenPreviewDrawingRectangle(columnModel);
		}
	}

	@Override
	protected void updateValidators(UUID uuid) {
		if (uuid != null && startColumnValidator != null) {
			startColumnValidator.setModelUUID(uuid);
			endColumnValidator.setModelUUID(uuid);
		}
	}

	private static void setScreenPreviewDrawingRectangle(ScreenColumnModel model) {
		ScreenPreview screenPreview = (ScreenPreview)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
				ScreenPreview.ID);
		if (screenPreview != null) {
			screenPreview.addFieldRectangle(
					new FieldRectangle(((ScreenTableModel)model.getParent()).getStartRow(),
							((ScreenTableModel)model.getParent()).getEndRow(), model.getStartColumn(), model.getEndColumn(), ""), SWT.COLOR_YELLOW, true);//$NON-NLS-1$
		}
	}

	@Override
	public void removeValidationMarkers() {
		if (startColumnValidator != null) {
			startColumnValidator.removeValidationMarker();
			endColumnValidator.removeValidationMarker();
		}
	}

	private SelectionListener getSelectionFieldListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				SelectionListener defaultSelectionListener = getDefaultSelectionListener();
				defaultSelectionListener.widgetSelected(e);
				if (((Button)e.widget).getSelection() && editableButton != null && !updatingControls) {
					editableButton.setSelection(true);
					editableButton.notifyListeners(SWT.Selection, new Event());
				}
			}

		};
	}

}
