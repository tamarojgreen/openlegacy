package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel;
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
import org.openlegacy.designtime.db.generators.support.DbAnnotationConstants;

import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractJpaFieldDetailsPage extends AbstractJpaDetailsPage {

	private TextValidator fieldNameValidator;

	public AbstractJpaFieldDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	protected abstract JpaFieldModel getFieldModel();

	/**
	 * Allows the child class to add specific content
	 * 
	 * @param client
	 * @param toolkit
	 */
	protected abstract void addContent(FormToolkit toolkit, Composite client);

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
		// create row for displaying java type name
		FormRowCreator.createLabelRow(toolkit, client, mapLabels,
				Messages.getString("jpa.field.java.type"), "", Constants.JAVA_TYPE_NAME);//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "fieldName"
		Text fieldNameControl = FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.field.field.name"), "", Constants.FIELD_NAME);//$NON-NLS-1$ //$NON-NLS-2$
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
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.field.key"), false, DbAnnotationConstants.DB_ID_ANNOTATION);//$NON-NLS-1$
		// create row for "name"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.field.name"), "", DbAnnotationConstants.NAME);//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "unique"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.field.unique"), false, DbAnnotationConstants.UNIQUE);//$NON-NLS-1$
		// create row for "nullable"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.field.nullable"), true, DbAnnotationConstants.NULLABLE);//$NON-NLS-1$
		// create row for "insertable"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.field.insertable"), true, DbAnnotationConstants.INSERTABLE);//$NON-NLS-1$
		// create row for "updateble"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("jpa.field.updatable"), true, DbAnnotationConstants.UPDATABLE);//$NON-NLS-1$
		// create row for "columnDefinition"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.field.column.definition"), "", DbAnnotationConstants.COLUMN_DEFINITION);//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "table"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.field.table"), "", DbAnnotationConstants.TABLE);//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "length"
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("jpa.field.length"), 255, DbAnnotationConstants.LENGTH);//$NON-NLS-1$
		// create row for "precision"
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("jpa.field.precision"), 0, DbAnnotationConstants.PRECISION);//$NON-NLS-1$
		// create row for "scale"
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("jpa.field.scale"), 0, DbAnnotationConstants.SCALE);//$NON-NLS-1$

		addContent(toolkit, client);

		toolkit.paintBordersFor(section);
		section.setClient(client);
	}

	@Override
	protected void updateValidators(UUID uuid) {
		if (uuid != null) {
			fieldNameValidator.setModelUUID(uuid);
		}
	}

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

}
