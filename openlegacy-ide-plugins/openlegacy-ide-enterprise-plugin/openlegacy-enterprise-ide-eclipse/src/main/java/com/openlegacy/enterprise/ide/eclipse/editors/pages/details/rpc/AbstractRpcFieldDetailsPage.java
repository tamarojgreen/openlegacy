package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.rpc;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters.FieldTypeViewerFilter;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.TextValidator;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.openlegacy.FieldType;
import org.openlegacy.annotations.rpc.Direction;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.rpc.generators.support.RpcAnnotationConstants;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractRpcFieldDetailsPage extends AbstractRpcDetailsPage {

	private TextValidator fieldNameValidator;
	private TextValidator lengthValidator;

	public AbstractRpcFieldDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	/**
	 * Allows the child class to add specific content
	 * 
	 * @param client
	 * @param toolkit
	 */
	protected abstract void addContent(FormToolkit toolkit, Composite client);

	protected abstract RpcFieldModel getFieldModel();

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
		section.setText(Messages.getString("rpc.fields.page.details.text")); //$NON-NLS-1$
		section.setDescription(Messages.getString("rpc.fields.page.details.desc")); //$NON-NLS-1$
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
				Messages.getString("rpc.field.java.type.label"), "", Constants.JAVA_TYPE_NAME);//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "fieldName"
		Text fieldNameControl = FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("rpc.field.field.name.label"), "", Constants.FIELD_NAME);//$NON-NLS-1$ //$NON-NLS-2$
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

		// "originalName"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("rpc.field.original.name.label"), "", RpcAnnotationConstants.ORIGINAL_NAME);//$NON-NLS-1$ //$NON-NLS-2$
		// "displayName"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("rpc.field.display.name.label"), "", AnnotationConstants.DISPLAY_NAME);//$NON-NLS-1$ //$NON-NLS-2$
		// "int length"
		Text lengthControl = FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				getDefaultVerifyListener(), Messages.getString("rpc.field.length.label"), 0, AnnotationConstants.LENGTH);//$NON-NLS-1$ 
		lengthValidator = new TextValidator(master, managedForm, lengthControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateLengthControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return getFieldModel();
			}

		};
		// "key"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("rpc.field.key.label"), false, AnnotationConstants.KEY);//$NON-NLS-1$
		// "editable"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("rpc.field.editable.label"), false, AnnotationConstants.EDITABLE);//$NON-NLS-1$
		// "Direction direction"
		FormRowCreator.createComboBoxRow(toolkit, client, mapCombos, getDefaultModifyListener(), getDefaultComboBoxKeyListener(),
				Messages.getString("rpc.field.direction.label"), getDirectionItems(), 0, RpcAnnotationConstants.DIRECTION, false);//$NON-NLS-1$
		// "Class<? extends FieldType> fieldType"
		FormRowCreator.createComboBoxRowWithBrowseButton(toolkit, client, mapCombos, getDefaultModifyListener(),
				getDefaultComboBoxKeyListener(), Messages.getString("rpc.field.field.type.label"), getFieldTypes(), 0,//$NON-NLS-1$
				AnnotationConstants.FIELD_TYPE, new FieldTypeViewerFilter());
		// "String sampleValue"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("rpc.field.sample.value.label"), "", AnnotationConstants.SAMPLE_VALUE);//$NON-NLS-1$ //$NON-NLS-2$
		// "String helpText"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("rpc.field.help.text.label"), "", AnnotationConstants.HELP_TEXT);//$NON-NLS-1$ //$NON-NLS-2$
		// "String defaultValue"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("rpc.field.default.value.label"), "", RpcAnnotationConstants.DEFAULT_VALUE);//$NON-NLS-1$ //$NON-NLS-2$
		// "String expression"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("rpc.field.expression"), "", RpcAnnotationConstants.EXPRESSION);//$NON-NLS-1$ //$NON-NLS-2$

		addContent(toolkit, client);

		toolkit.paintBordersFor(section);
		section.setClient(client);
	}

	private static String[] getFieldTypes() {
		Collection<Class<? extends FieldType>> collection = RpcFieldModel.mapFieldTypes.values();
		List<String> list = new ArrayList<String>();
		for (Class<? extends FieldType> clazz : collection) {
			list.add(clazz.getSimpleName());
		}
		return list.toArray(new String[] {});
	}

	@Override
	public void removeValidationMarkers() {
		super.removeValidationMarkers();
		if (fieldNameValidator != null) {
			fieldNameValidator.removeValidationMarker();
			lengthValidator.removeValidationMarker();
		}
	}

	public void revalidate() {
		if (fieldNameValidator != null) {
			fieldNameValidator.revalidate(getModelUUID());
			lengthValidator.revalidate(getModelUUID());
		}
	}

	@Override
	protected void updateValidators(UUID uuid) {
		if (uuid != null) {
			fieldNameValidator.setModelUUID(uuid);
			lengthValidator.setModelUUID(uuid);
		}
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

	private static boolean validateLengthControl(TextValidator validator, UUID uuid) {
		String text = validator.getControl().getText();
		boolean isValid = true;
		// check if empty or contains any character that is not a digit
		isValid = text.isEmpty() ? false : !text.matches("\\D");//$NON-NLS-1$
		// if previous condition is valid then check range
		isValid = isValid ? Integer.valueOf(text) >= 0 : false;
		if (!isValid) {
			validator.addMessage(Messages.getString("validation.should.be.positive"), IMessageProvider.ERROR, uuid);//$NON-NLS-1$
		}
		return isValid;
	}

	private static String[] getDirectionItems() {
		List<String> list = new ArrayList<String>();
		list.add(Direction.INPUT_OUTPUT.name().toUpperCase());
		list.add(Direction.INPUT.name().toUpperCase());
		list.add(Direction.OUTPUT.name().toUpperCase());
		return list.toArray(new String[] {});
	}
}
