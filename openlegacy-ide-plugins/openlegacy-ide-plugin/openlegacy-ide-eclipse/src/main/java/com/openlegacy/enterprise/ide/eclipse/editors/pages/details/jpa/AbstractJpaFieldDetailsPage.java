package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator.JAVA_DOCUMENTATION_TYPE;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.TextValidator;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.openlegacy.designtime.db.generators.support.DbAnnotationConstants;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractJpaFieldDetailsPage extends AbstractJpaDetailsPage {

	private TextValidator fieldNameValidator;
	private Button autoGeneretedKeyControl;

	public AbstractJpaFieldDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	protected abstract JpaFieldModel getFieldModel();

	/**
	 * Allows the child class to add specific content on the top of main area
	 * 
	 * @param client
	 * @param toolkit
	 */
	protected abstract void addTopContent(FormToolkit toolkit, Composite client);

	/**
	 * Allows the child class to add specific content on the bottom of main area
	 * 
	 * @param client
	 * @param toolkit
	 */
	protected abstract void addBottomContent(FormToolkit toolkit, Composite client);

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
		section.setText(Messages.getString("jpa.fields.page.details.name")); //$NON-NLS-1$
		section.setDescription(Messages.getString("jpa.fields.page.details.desc")); //$NON-NLS-1$
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

		addTopContent(toolkit, client);

		// create row for "fieldName"
		Text fieldNameControl = FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.field.field.name"), "", Constants.FIELD_NAME, JAVA_DOCUMENTATION_TYPE.JAVA_BASICS,//$NON-NLS-1$ //$NON-NLS-2$
				"variables", "naming");//$NON-NLS-1$ //$NON-NLS-2$
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
		// create row for "key"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getIsKeySelectionListener(),
				Messages.getString("jpa.field.key"), false, DbAnnotationConstants.DB_ID_ANNOTATION, JAVA_DOCUMENTATION_TYPE.JPA,//$NON-NLS-1$
				"Id", "");//$NON-NLS-1$ //$NON-NLS-2$
		autoGeneretedKeyControl = FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.field.generated.value"), false, DbAnnotationConstants.DB_GENERATED_VALUE_ANNOTATION,//$NON-NLS-1$
				JAVA_DOCUMENTATION_TYPE.JPA, "GeneratedValue", "");//$NON-NLS-1$ //$NON-NLS-2$
		autoGeneretedKeyControl.setEnabled(false);
		// create row for "name"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.field.name"), "", DbAnnotationConstants.NAME, JAVA_DOCUMENTATION_TYPE.JPA, "Column",//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				DbAnnotationConstants.NAME);
		// create row for "unique"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.field.unique"), false, DbAnnotationConstants.UNIQUE, JAVA_DOCUMENTATION_TYPE.JPA,//$NON-NLS-1$
				"Column", DbAnnotationConstants.UNIQUE);//$NON-NLS-1$
		// create row for "nullable"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.field.nullable"), true, DbAnnotationConstants.NULLABLE, JAVA_DOCUMENTATION_TYPE.JPA,//$NON-NLS-1$
				"Column", DbAnnotationConstants.NULLABLE);//$NON-NLS-1$
		// create row for "insertable"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.field.insertable"), true, DbAnnotationConstants.INSERTABLE, JAVA_DOCUMENTATION_TYPE.JPA,//$NON-NLS-1$
				"Column", DbAnnotationConstants.INSERTABLE);//$NON-NLS-1$
		// create row for "updateble"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.field.updatable"), true, DbAnnotationConstants.UPDATABLE, JAVA_DOCUMENTATION_TYPE.JPA,//$NON-NLS-1$
				"Column", DbAnnotationConstants.UPDATABLE);//$NON-NLS-1$
		// create row for "columnDefinition"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.field.column.definition"), "", DbAnnotationConstants.COLUMN_DEFINITION,//$NON-NLS-1$ //$NON-NLS-2$
				JAVA_DOCUMENTATION_TYPE.JPA, "Column", DbAnnotationConstants.COLUMN_DEFINITION);//$NON-NLS-1$
		// create row for "table"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.field.table"), "", DbAnnotationConstants.TABLE, JAVA_DOCUMENTATION_TYPE.JPA, "Column", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				DbAnnotationConstants.TABLE);
		// create row for "length"
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("jpa.field.length"), 255, DbAnnotationConstants.LENGTH, JAVA_DOCUMENTATION_TYPE.JPA, "Column",//$NON-NLS-1$ //$NON-NLS-2$
				DbAnnotationConstants.LENGTH);
		// create row for "precision"
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("jpa.field.precision"), 0, DbAnnotationConstants.PRECISION, JAVA_DOCUMENTATION_TYPE.JPA,//$NON-NLS-1$
				"Column", DbAnnotationConstants.PRECISION);//$NON-NLS-1$
		// create row for "scale"
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("jpa.field.scale"), 0, DbAnnotationConstants.SCALE, JAVA_DOCUMENTATION_TYPE.JPA, "Column",//$NON-NLS-1$ //$NON-NLS-2$
				DbAnnotationConstants.SCALE);

		// ----------------- @DbColumn ---------------------
		// create row for "displayName"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.field.display.name"), "", DbAnnotationConstants.DISPLAY_NAME, JAVA_DOCUMENTATION_TYPE.DB,//$NON-NLS-1$ //$NON-NLS-2$
				"DbColumn", DbAnnotationConstants.DISPLAY_NAME);//$NON-NLS-1$
		// create row for "password"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.field.password"), false, DbAnnotationConstants.PASSWORD, JAVA_DOCUMENTATION_TYPE.DB,//$NON-NLS-1$
				"DbColumn", DbAnnotationConstants.PASSWORD);//$NON-NLS-1$
		// create row for "sampleValue"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.field.sample.value"), "", DbAnnotationConstants.SAMPLE_VALUE,//$NON-NLS-1$ //$NON-NLS-2$
				JAVA_DOCUMENTATION_TYPE.DB, "DbColumn", DbAnnotationConstants.SAMPLE_VALUE);//$NON-NLS-1$
		// create row for "defaultValue"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.field.default.value"), "", DbAnnotationConstants.DEFAULT_VALUE,//$NON-NLS-1$ //$NON-NLS-2$
				JAVA_DOCUMENTATION_TYPE.DB, "DbColumn", DbAnnotationConstants.DEFAULT_VALUE);//$NON-NLS-1$
		// create row for "helpText"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.field.help.text"), "", DbAnnotationConstants.HELP_TEXT, JAVA_DOCUMENTATION_TYPE.DB,//$NON-NLS-1$ //$NON-NLS-2$
				"DbColumn", DbAnnotationConstants.HELP_TEXT);//$NON-NLS-1$
		// create row for "rightToLeft"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.field.right.to.left"), false, DbAnnotationConstants.RIGHT_TO_LEFT,//$NON-NLS-1$
				JAVA_DOCUMENTATION_TYPE.DB, "DbColumn", DbAnnotationConstants.RIGHT_TO_LEFT);//$NON-NLS-1$
		// create row for "internal"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.field.internal"), false, DbAnnotationConstants.INTERNAL, JAVA_DOCUMENTATION_TYPE.DB,//$NON-NLS-1$
				"DbColumn", DbAnnotationConstants.INTERNAL);//$NON-NLS-1$
		// create row for "mainDisplayField"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.field.main.display.field"), false, DbAnnotationConstants.MAIN_DISPLAY_FIELD,//$NON-NLS-1$
				JAVA_DOCUMENTATION_TYPE.DB, "DbColumn", DbAnnotationConstants.MAIN_DISPLAY_FIELD);//$NON-NLS-1$

		addBottomContent(toolkit, client);

		toolkit.paintBordersFor(section);
		section.setClient(client);

	}

	@Override
	protected void updateValidators(UUID uuid) {
		if (uuid != null) {
			fieldNameValidator.setModelUUID(uuid);
		}
	}

	@Override
	public void revalidate() {
		if (fieldNameValidator != null) {
			fieldNameValidator.revalidate(getModelUUID());
		}
	}

	@Override
	public void removeValidationMarkers() {
		super.removeValidationMarkers();
		if (fieldNameValidator != null) {
			fieldNameValidator.removeValidationMarker();
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

	private SelectionListener getIsKeySelectionListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				if (!updatingControls) {
					String key = (String)event.widget.getData(FormRowCreator.ID_KEY);
					updateModel(key);
				}

				autoGeneretedKeyControl.setEnabled(((Button)event.widget).getSelection());
			}
		};
	}
}
