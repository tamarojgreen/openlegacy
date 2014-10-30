package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters.FieldTypeViewerFilter;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.TextValidator;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.openlegacy.FieldType;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.terminal.generators.support.ScreenAnnotationConstants;
import org.openlegacy.ide.eclipse.preview.screen.FieldRectangle;
import org.openlegacy.ide.eclipse.preview.screen.ScreenPreview;
import org.openlegacy.modules.login.Login.PasswordField;
import org.openlegacy.terminal.FieldAttributeType;
import org.openlegacy.terminal.ScreenSize;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.support.SimpleTerminalPosition;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractScreenFieldDetailsPage extends AbstractScreenDetailsPage {

	private TextValidator rowValidator;
	private TextValidator columnValidator;
	private TextValidator endRowValidator;
	private TextValidator endColumnValidator;
	private TextValidator fieldNameValidator;
	private TextValidator descRowValidator;
	private TextValidator descColumnValidator;
	private TextValidator descEndColumnValidator;

	private Button passwordButton = null;

	public AbstractScreenFieldDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	protected abstract ScreenFieldModel getFieldModel();

	private static String[] getFieldAttributes() {
		List<String> list = new ArrayList<String>();
		list.add(FieldAttributeType.Value.toString());
		list.add(FieldAttributeType.Editable.toString());
		list.add(FieldAttributeType.Color.toString());
		return list.toArray(new String[] {});
	}

	private static String[] getFieldTypes() {
		Collection<Class<? extends FieldType>> collection = ScreenFieldModel.mapFieldTypes.values();
		List<String> list = new ArrayList<String>();
		for (Class<? extends FieldType> clazz : collection) {
			list.add(clazz.getSimpleName());
		}
		return list.toArray(new String[] {});
	}

	private static boolean validateFieldNameControl(TextValidator validator, UUID uuid) {
		String text = validator.getControl().getText();
		boolean isValid = !text.isEmpty();
		if (!isValid) {
			validator.addMessage(Messages.getString("validation.should.not.be.empty"), IMessageProvider.ERROR, uuid);//$NON-NLS-1$
			return isValid;
		}
		return isValid;
	}

	protected static void setScreenPreviewDrawingRectangle(ScreenFieldModel model) {
		ScreenPreview screenPreview = (ScreenPreview)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
				ScreenPreview.ID);
		if (screenPreview != null) {
			TerminalPosition endPosition = null;
			if (model.isRectangle()) {
				endPosition = new SimpleTerminalPosition((model.getEndRow() != null) ? model.getEndRow() : 0,
						(model.getEndColumn() != null) ? model.getEndColumn() : 0);
			} else {
				endPosition = new SimpleTerminalPosition(model.getRow(), (model.getEndColumn() == null) ? 0
						: model.getEndColumn());
			}

			screenPreview.addFieldRectangle(new FieldRectangle(model.getRow(), endPosition.getRow(), model.getColumn(),
					endPosition.getColumn(), ""), SWT.COLOR_YELLOW, true);//$NON-NLS-1$ 
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
				screenPreview.addFieldRectangle(new FieldRectangle(row, row, model.getDescriptionFieldModel().getColumn(),
						endColumn, ""), SWT.COLOR_YELLOW);//$NON-NLS-1$ 
			}
		}
	}

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

	private boolean validateDescriptionColumnControl(TextValidator validator, UUID uuid) {
		String text = validator.getControl().getText().trim();
		boolean isValid = true;
		if (text.isEmpty() || text.equals("0")) {//$NON-NLS-1$
			return true;
		}
		isValid = !text.isEmpty() && text.matches("\\d*");//$NON-NLS-1$
		isValid = isValid ? (Integer.valueOf(text) >= 0) && (Integer.valueOf(text) <= getEntity().getEntityModel().getColumns())
				: false;
		if (!isValid) {
			validator.addMessage(MessageFormat.format("{0} {1}-{2}", Messages.getString("validation.is.out.of.range"),//$NON-NLS-1$//$NON-NLS-2$
					Constants.MIN_ROW_COLUMN, getEntity().getEntityModel().getColumns()), IMessageProvider.ERROR, uuid);
			return false;
		}
		return true;
	}

	private boolean validateDescriptionEndColumnControl(TextValidator validator, UUID uuid) {
		String columnText = mapTexts.get(Constants.DESC_COLUMN).getText().trim();
		String text = validator.getControl().getText().trim();
		boolean isValid = true;
		// check if empty or contains any character that is not a digit
		isValid = !text.isEmpty() && text.matches("\\d*");//$NON-NLS-1$
		if ((columnText.isEmpty() || columnText.equals("0")) && isValid) {//$NON-NLS-1$
			validator.addMessage(Messages.getString("validation.description.field.column.should.be.specified"),//$NON-NLS-1$
					IMessageProvider.ERROR, uuid);
			return false;
		}
		if (text.isEmpty() || text.equals("0")) {//$NON-NLS-1$
			return true;
		}
		// if previous condition is valid then check range
		isValid = isValid ? (Integer.valueOf(text) >= 0) && (Integer.valueOf(text) <= getEntity().getEntityModel().getColumns())
				: false;
		if (!isValid) {
			validator.addMessage(MessageFormat.format("{0} {1}-{2}", Messages.getString("validation.is.out.of.range"),//$NON-NLS-1$//$NON-NLS-2$
					Constants.MIN_ROW_COLUMN, getEntity().getEntityModel().getColumns()), IMessageProvider.ERROR, uuid);
			return isValid;
		}
		return isValid;
	}

	private boolean validateDescriptionRowControl(TextValidator validator, UUID uuid) {
		String columnText = mapTexts.get(Constants.DESC_COLUMN).getText().trim();
		String text = validator.getControl().getText().trim();
		boolean isValid = true;
		// check if empty or contains any character that is not a digit
		isValid = !text.isEmpty() && text.matches("\\d*");//$NON-NLS-1$
		if ((columnText.isEmpty() || columnText.equals("0")) && isValid) {//$NON-NLS-1$
			validator.addMessage(Messages.getString("validation.description.field.column.should.be.specified"),//$NON-NLS-1$
					IMessageProvider.ERROR, uuid);
			return false;
		}
		if (text.isEmpty() || text.equals("0")) {//$NON-NLS-1$
			return true;
		}
		// if previous condition is valid then check range
		isValid = isValid ? (Integer.valueOf(text) >= 0) && (Integer.valueOf(text) <= getEntity().getEntityModel().getRows())
				: false;
		if (!isValid) {
			validator.addMessage(MessageFormat.format("{0} {1}-{2}", Messages.getString("validation.is.out.of.range"),//$NON-NLS-1$//$NON-NLS-2$
					Constants.MIN_ROW_COLUMN, getEntity().getEntityModel().getRows()), IMessageProvider.ERROR, uuid);
			return isValid;
		}
		return isValid;
	}

	private boolean validateEndColumnControl(TextValidator validator, UUID uuid) {
		String text = validator.getControl().getText();
		if (text.isEmpty() || text.trim().isEmpty() || (Integer.valueOf(text.trim()) == 0)) {
			return true;
		}
		return validateColumnControl(validator, uuid);
	}

	private boolean validateEndRowControl(TextValidator validator, UUID uuid) {
		String text = validator.getControl().getText();
		if (text.isEmpty() || text.trim().isEmpty() || (Integer.valueOf(text.trim()) == 0)) {
			return true;
		}
		return validateRowControl(validator, uuid);
	}

	private boolean validateRowControl(TextValidator validator, UUID uuid) {
		String text = validator.getControl().getText();
		boolean isValid = true;
		// check if empty or contains any character that is not a digit
		isValid = text.isEmpty() ? false : !text.matches("\\D");//$NON-NLS-1$
		// if previous condition is valid then check range
		int maxRows = ScreenSize.DEFAULT_ROWS < getEntity().getEntityModel().getRows() ? getEntity().getEntityModel().getRows()
				: ScreenSize.DEFAULT_ROWS;
		isValid = isValid ? (Integer.parseInt(text) >= Constants.MIN_ROW_COLUMN) && (Integer.parseInt(text) <= maxRows) : false;
		if (!isValid) {
			validator.addMessage(MessageFormat.format("{0} {1}-{2}", Messages.getString("validation.is.out.of.range"),//$NON-NLS-1$//$NON-NLS-2$
					Constants.MIN_ROW_COLUMN, maxRows), IMessageProvider.ERROR, uuid);
			return isValid;
		}
		return isValid;
	}

	/**
	 * Allows the child class to add specific content
	 * 
	 * @param client
	 * @param toolkit
	 */
	protected abstract void addContent(FormToolkit toolkit, Composite client);

	protected void addScreenDecriptionFieldSection(FormToolkit toolkit, Composite client) {
		Section section = toolkit.createSection(client, ExpandableComposite.TWISTIE | Section.DESCRIPTION);
		section.setActiveToggleColor(toolkit.getHyperlinkGroup().getActiveForeground());
		section.setToggleColor(toolkit.getColors().getColor(IFormColors.SEPARATOR));
		toolkit.createCompositeSeparator(section);

		Composite composite = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;

		composite.setLayout(layout);
		// "row"
		Text descRowControl = FormRowCreator.createIntRow(toolkit, composite, mapTexts, getDefaultModifyListener(),
				getDefaultVerifyListener(), Messages.getString("ScreenField.row"), 0, Constants.DESC_ROW);
		descRowValidator = new TextValidator(master, managedForm, descRowControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateDescriptionRowControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return getFieldModel();
			}

		};
		// "column"
		Text descColumnControl = FormRowCreator.createIntRow(toolkit, composite, mapTexts, getDefaultModifyListener(),
				getDefaultVerifyListener(), Messages.getString("ScreenField.column"), 0, Constants.DESC_COLUMN);
		descColumnValidator = new TextValidator(master, managedForm, descColumnControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				descRowValidator.revalidate(uuid);
				descEndColumnValidator.revalidate(uuid);
				return validateDescriptionColumnControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return getFieldModel();
			}
		};
		// "end column"
		Text descEndColumnControl = FormRowCreator.createIntRow(toolkit, composite, mapTexts, getDefaultModifyListener(),
				getDefaultVerifyListener(), Messages.getString("ScreenField.endColumn"), 0, Constants.DESC_END_COLUMN);
		descEndColumnValidator = new TextValidator(master, managedForm, descEndColumnControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateDescriptionEndColumnControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return getFieldModel();
			}
		};

		section.setText(Messages.getString("ScreenDescriptionField.section.name"));//$NON-NLS-1$
		section.setClient(composite);
		section.setExpanded(true);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		section.setLayoutData(gd);
	}

	@Override
	protected void updateValidators(UUID uuid) {
		if (uuid != null) {
			rowValidator.setModelUUID(uuid);
			columnValidator.setModelUUID(uuid);
			endRowValidator.setModelUUID(uuid);
			endColumnValidator.setModelUUID(uuid);
			fieldNameValidator.setModelUUID(uuid);
			if (descRowValidator != null) {
				descRowValidator.setModelUUID(uuid);
				descColumnValidator.setModelUUID(uuid);
				descEndColumnValidator.setModelUUID(uuid);
			}
		}
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
		section.setText(Messages.getString("FieldsScreenFieldDetailsPage.name")); //$NON-NLS-1$
		section.setDescription(Messages.getString("FieldsScreenFieldDetailsPage.desc")); //$NON-NLS-1$
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
				Messages.getString("ScreenField.javaType"), "", Constants.JAVA_TYPE_NAME);//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "fieldName"
		Text fieldNameControl = FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenField.fieldName"), "", Constants.FIELD_NAME);//$NON-NLS-1$ //$NON-NLS-2$
		fieldNameValidator = new TextValidator(master, managedForm, fieldNameControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateFieldNameControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return getFieldModel();
			}
		};
		// create row for "row"
		Text rowControl = FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				getDefaultVerifyListener(), Messages.getString("ScreenField.row"), -1,//$NON-NLS-1$
				ScreenAnnotationConstants.ROW);
		rowValidator = new TextValidator(master, managedForm, rowControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateRowControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return getFieldModel();
			}
		};
		// create row for "column"
		// add validator
		Text columnControl = FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				getDefaultVerifyListener(), Messages.getString("ScreenField.column"), -1,//$NON-NLS-1$
				ScreenAnnotationConstants.COLUMN);
		columnValidator = new TextValidator(master, managedForm, columnControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateColumnControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return getFieldModel();
			}
		};
		// create row for "endRow"
		Text endRowControl = FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				getDefaultVerifyListener(), Messages.getString("ScreenField.endRow"), null,//$NON-NLS-1$
				ScreenAnnotationConstants.END_ROW);
		endRowValidator = new TextValidator(master, managedForm, endRowControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateEndRowControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return getFieldModel();
			}
		};
		// create row for "endColumn"
		Text endColumnControl = FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				getDefaultVerifyListener(), Messages.getString("ScreenField.endColumn"), null,//$NON-NLS-1$
				ScreenAnnotationConstants.END_COLUMN);
		endColumnValidator = new TextValidator(master, managedForm, endColumnControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateEndColumnControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return getFieldModel();
			}
		};
		// create row for "rectangle"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenField.rectangle"),//$NON-NLS-1$
				false, ScreenAnnotationConstants.RECTANGLE);
		// create row for "editable"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenField.editable"),//$NON-NLS-1$
				false, AnnotationConstants.EDITABLE);
		// create row for "password"
		passwordButton = FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenField.password"),//$NON-NLS-1$
				false, AnnotationConstants.PASSWORD);
		// create row for "fieldType"
		FormRowCreator.createComboBoxRowWithBrowseButton(toolkit, client, mapCombos, getFieldTypeModifyListener(),
				getDefaultComboBoxKeyListener(), Messages.getString("ScreenField.fieldType"), getFieldTypes(), 0,//$NON-NLS-1$
				AnnotationConstants.FIELD_TYPE, new FieldTypeViewerFilter());
		// create row for "displayName"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenField.displayName"),//$NON-NLS-1$
				org.openlegacy.annotations.screen.AnnotationConstants.NULL, AnnotationConstants.DISPLAY_NAME);
		// create row for "sampleValue"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenField.sampleValue"), "",//$NON-NLS-1$ //$NON-NLS-2$
				AnnotationConstants.SAMPLE_VALUE);
		// create row for "defaultValue"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenField.defaultValue"), "",//$NON-NLS-1$ //$NON-NLS-2$
				AnnotationConstants.DEFAULT_VALUE);
		// create row for "labelColumn"
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("ScreenField.labelColumn"), 0,//$NON-NLS-1$
				ScreenAnnotationConstants.LABEL_COLUMN);
		// create row for "key"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenField.key"), false,//$NON-NLS-1$
				AnnotationConstants.KEY);
		// create row for "helpText"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenField.helpText"), "", AnnotationConstants.HELP_TEXT);//$NON-NLS-1$
		// create row for "rightToLeft"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenField.rightToLeft"), false, AnnotationConstants.RIGHT_TO_LEFT);//$NON-NLS-1$
		// create row for "attribute"
		FormRowCreator.createComboBoxRow(toolkit, client, mapCombos, getDefaultModifyListener(), getDefaultComboBoxKeyListener(),
				Messages.getString("ScreenField.attribute"), getFieldAttributes(), 0, AnnotationConstants.ATTRIBUTE, false);//$NON-NLS-1$
		// create row for "when"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenField.when"), "", ScreenAnnotationConstants.WHEN);//$NON-NLS-1$
		// create row for "unless"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenField.unless"), "", ScreenAnnotationConstants.UNLESS);//$NON-NLS-1$
		// create row for "keyIndex"
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("ScreenField.keyIndex"), -1, ScreenAnnotationConstants.KEY_INDEX);//$NON-NLS-1$
		// create row for "internal"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenField.internal"), false, ScreenAnnotationConstants.INTERNAL);
		// create row for "global"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenField.global"), false, ScreenAnnotationConstants.GLOBAL);
		// create row for "nullValue"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenField.nullValue"), "", ScreenAnnotationConstants.NULL_VALUE);//$NON-NLS-1$
		// create row for "tableKey"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenField.tableKey"), false, ScreenAnnotationConstants.TABLE_KEY);//$NON-NLS-1$
		// create row for "forceUpdate"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenField.forceUpdate"), false, ScreenAnnotationConstants.FORCE_UPDATE);//$NON-NLS-1$
		// create row for "expression"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenField.expression"), "", ScreenAnnotationConstants.EXPRESSION);

		addContent(toolkit, client);

		toolkit.paintBordersFor(section);
		section.setClient(client);
	}

	@Override
	public void removeValidationMarkers() {
		super.removeValidationMarkers();
		// we need to check that validators are not null
		if (rowValidator != null) {
			rowValidator.removeValidationMarker();
			columnValidator.removeValidationMarker();
			endRowValidator.removeValidationMarker();
			endColumnValidator.removeValidationMarker();
			fieldNameValidator.removeValidationMarker();
		}
		if (descRowValidator != null) {
			descRowValidator.removeValidationMarker();
			descColumnValidator.removeValidationMarker();
			descEndColumnValidator.removeValidationMarker();
		}
	}

	public void revalidate() {
		// we need to check that validators are not null
		if (rowValidator != null) {
			rowValidator.revalidate(getModelUUID());
			columnValidator.revalidate(getModelUUID());
			endRowValidator.revalidate(getModelUUID());
			endColumnValidator.revalidate(getModelUUID());
			fieldNameValidator.revalidate(getModelUUID());
		}
		if (descRowValidator != null) {
			descRowValidator.revalidate(getModelUUID());
			descColumnValidator.revalidate(getModelUUID());
			descEndColumnValidator.revalidate(getModelUUID());
		}
	}

	private ModifyListener getFieldTypeModifyListener() {
		return new ModifyListener() {

			public void modifyText(ModifyEvent e) {
				getDefaultModifyListener().modifyText(e);
				String text = ((CCombo)e.widget).getText();
				if (!StringUtils.isEmpty(text) && PasswordField.class.getSimpleName().equals(text) && !updatingControls) {
					passwordButton.setSelection(true);
					passwordButton.notifyListeners(SWT.Selection, new Event());
				}
			}
		};
	}

}
