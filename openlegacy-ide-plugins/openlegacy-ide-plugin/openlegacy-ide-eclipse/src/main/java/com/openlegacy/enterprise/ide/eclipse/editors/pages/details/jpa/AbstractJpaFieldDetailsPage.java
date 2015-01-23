package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator.JAVA_DOCUMENTATION_TYPE;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.TextValidator;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.openlegacy.designtime.db.generators.support.DbAnnotationConstants;
import org.openlegacy.ide.eclipse.Activator;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractJpaFieldDetailsPage extends AbstractJpaDetailsPage {

	private TextValidator fieldNameValidator;
	private Text targetEntityControl;

	public AbstractJpaFieldDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	protected abstract JpaFieldModel getFieldModel();

	protected abstract boolean isAddManyToOneSection();

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
				Messages.getString("jpa.field.java.type"), "", Constants.JAVA_TYPE_NAME, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "fieldName"
		Text fieldNameControl = FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.field.field.name"), "", Constants.FIELD_NAME, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$ //$NON-NLS-2$
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
		FormRowCreator.createBooleanRow(
				toolkit,
				client,
				mapCheckBoxes,
				getDefaultSelectionListener(),
				Messages.getString("jpa.field.key"), false, DbAnnotationConstants.DB_ID_ANNOTATION, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$
		// create row for "name"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.field.name"), "", DbAnnotationConstants.NAME, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "unique"
		FormRowCreator.createBooleanRow(
				toolkit,
				client,
				mapCheckBoxes,
				getDefaultSelectionListener(),
				Messages.getString("jpa.field.unique"), false, DbAnnotationConstants.UNIQUE, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$
		// create row for "nullable"
		FormRowCreator.createBooleanRow(
				toolkit,
				client,
				mapCheckBoxes,
				getDefaultSelectionListener(),
				Messages.getString("jpa.field.nullable"), true, DbAnnotationConstants.NULLABLE, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$
		// create row for "insertable"
		FormRowCreator.createBooleanRow(
				toolkit,
				client,
				mapCheckBoxes,
				getDefaultSelectionListener(),
				Messages.getString("jpa.field.insertable"), true, DbAnnotationConstants.INSERTABLE, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$
		// create row for "updateble"
		FormRowCreator.createBooleanRow(
				toolkit,
				client,
				mapCheckBoxes,
				getDefaultSelectionListener(),
				Messages.getString("jpa.field.updatable"), true, DbAnnotationConstants.UPDATABLE, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$
		// create row for "columnDefinition"
		FormRowCreator.createStringRow(
				toolkit,
				client,
				mapTexts,
				getDefaultModifyListener(),
				Messages.getString("jpa.field.column.definition"), "", DbAnnotationConstants.COLUMN_DEFINITION, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "table"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.field.table"), "", DbAnnotationConstants.TABLE, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "length"
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("jpa.field.length"), 255, DbAnnotationConstants.LENGTH, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$
		// create row for "precision"
		FormRowCreator.createIntRow(
				toolkit,
				client,
				mapTexts,
				getDefaultModifyListener(),
				getDefaultVerifyListener(),
				Messages.getString("jpa.field.precision"), 0, DbAnnotationConstants.PRECISION, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$
		// create row for "scale"
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("jpa.field.scale"), 0, DbAnnotationConstants.SCALE, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$

		// ----------------- @DbColumn ---------------------
		// create row for "displayName"
		FormRowCreator.createStringRow(
				toolkit,
				client,
				mapTexts,
				getDefaultModifyListener(),
				Messages.getString("jpa.field.display.name"), "", DbAnnotationConstants.DISPLAY_NAME, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "password"
		FormRowCreator.createBooleanRow(
				toolkit,
				client,
				mapCheckBoxes,
				getDefaultSelectionListener(),
				Messages.getString("jpa.field.password"), false, DbAnnotationConstants.PASSWORD, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$ 
		// create row for "sampleValue"
		FormRowCreator.createStringRow(
				toolkit,
				client,
				mapTexts,
				getDefaultModifyListener(),
				Messages.getString("jpa.field.sample.value"), "", DbAnnotationConstants.SAMPLE_VALUE, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "defaultValue"
		FormRowCreator.createStringRow(
				toolkit,
				client,
				mapTexts,
				getDefaultModifyListener(),
				Messages.getString("jpa.field.default.value"), "", DbAnnotationConstants.DEFAULT_VALUE, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "helpText"
		FormRowCreator.createStringRow(
				toolkit,
				client,
				mapTexts,
				getDefaultModifyListener(),
				Messages.getString("jpa.field.help.text"), "", DbAnnotationConstants.HELP_TEXT, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "rightToLeft"
		FormRowCreator.createBooleanRow(
				toolkit,
				client,
				mapCheckBoxes,
				getDefaultSelectionListener(),
				Messages.getString("jpa.field.right.to.left"), false, DbAnnotationConstants.RIGHT_TO_LEFT, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$
		// create row for "internal"
		FormRowCreator.createBooleanRow(
				toolkit,
				client,
				mapCheckBoxes,
				getDefaultSelectionListener(),
				Messages.getString("jpa.field.internal"), false, DbAnnotationConstants.INTERNAL, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$
		// create row for "mainDisplayField"
		FormRowCreator.createBooleanRow(
				toolkit,
				client,
				mapCheckBoxes,
				getDefaultSelectionListener(),
				Messages.getString("jpa.field.main.display.field"), false, DbAnnotationConstants.MAIN_DISPLAY_FIELD, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$

		if (isAddManyToOneSection()) {
			// create section for @ManyToOne annotation
			Section mtoSection = toolkit.createSection(client, ExpandableComposite.TWISTIE | Section.DESCRIPTION);
			mtoSection.setActiveToggleColor(toolkit.getHyperlinkGroup().getActiveForeground());
			mtoSection.setToggleColor(toolkit.getColors().getColor(IFormColors.SEPARATOR));
			toolkit.createCompositeSeparator(mtoSection);

			Composite composite = toolkit.createComposite(mtoSection, SWT.WRAP);
			glayout = new GridLayout();
			glayout.numColumns = 2;

			composite.setLayout(glayout);
			// create row for "targetEntity"
			targetEntityControl = FormRowCreator.createStringRowWithBrowseButton(toolkit, composite, mapTexts,
					getDefaultModifyListener(), Messages.getString("jpa.field.list.target.entity"), "",//$NON-NLS-1$ //$NON-NLS-2$
					DbAnnotationConstants.TARGET_ENTITY, null, true, getTargetEntityClearListener(), JAVA_DOCUMENTATION_TYPE.JPA,
					"Column");
			// create row for "cascade"
			FormRowCreator.createComboBoxRow(toolkit, composite, mapCombos, getDefaultModifyListener(),
					getDefaultComboBoxKeyListener(), Messages.getString("jpa.field.list.cascade"), getCascadeItems(), 0,
					DbAnnotationConstants.CASCADE, true, JAVA_DOCUMENTATION_TYPE.JPA, "Column");
			// create row for "fetch"
			FormRowCreator.createComboBoxRow(toolkit, composite, mapCombos, getDefaultModifyListener(),
					getDefaultComboBoxKeyListener(), Messages.getString("jpa.field.list.fetch.type"), getFetchTypeItems(), 0,//$NON-NLS-1$
					DbAnnotationConstants.FETCH, false, JAVA_DOCUMENTATION_TYPE.JPA, "Column");
			// create row for "optional"
			FormRowCreator.createBooleanRow(
					toolkit,
					composite,
					mapCheckBoxes,
					getDefaultSelectionListener(),
					Messages.getString("jpa.field.optional"), true, DbAnnotationConstants.OPTIONAL, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$

			mtoSection.setText(Messages.getString("jpa.fields.page.many.to.one.section.desc"));//$NON-NLS-1$
			mtoSection.setClient(composite);
			mtoSection.setExpanded(true);
			GridData gd = new GridData(GridData.FILL_BOTH);
			gd.horizontalSpan = 2;
			mtoSection.setLayoutData(gd);

			// add @JoinColumn section
			Section jcSection = toolkit.createSection(client, ExpandableComposite.TWISTIE | Section.DESCRIPTION);
			jcSection.setActiveToggleColor(toolkit.getHyperlinkGroup().getActiveForeground());
			jcSection.setToggleColor(toolkit.getColors().getColor(IFormColors.SEPARATOR));
			toolkit.createCompositeSeparator(jcSection);

			Composite jcComposite = toolkit.createComposite(jcSection, SWT.WRAP);
			glayout = new GridLayout();
			glayout.numColumns = 2;

			jcComposite.setLayout(glayout);

			// NOTE: keys for controls must be unique

			// create row for "name"
			FormRowCreator.createStringRow(toolkit, jcComposite, mapTexts, getDefaultModifyListener(),
					Messages.getString("jpa.field.name"), "", Constants.JC_NAME, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$ //$NON-NLS-2$
			// create row for "referencedColumnName"
			FormRowCreator.createStringRow(
					toolkit,
					jcComposite,
					mapTexts,
					getDefaultModifyListener(),
					Messages.getString("jpa.field.referenced.column.name"), "", Constants.JC_REFERENCED_COLUMN_NAME, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$ //$NON-NLS-2$
			// create row for "unique"
			FormRowCreator.createBooleanRow(toolkit, jcComposite, mapCheckBoxes, getDefaultSelectionListener(),
					Messages.getString("jpa.field.unique"), false, Constants.JC_UNIQUE, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$
			// create row for "nullable"
			FormRowCreator.createBooleanRow(toolkit, jcComposite, mapCheckBoxes, getDefaultSelectionListener(),
					Messages.getString("jpa.field.nullable"), true, Constants.JC_NULLABLE, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$
			// create row for "insertable"
			FormRowCreator.createBooleanRow(
					toolkit,
					jcComposite,
					mapCheckBoxes,
					getDefaultSelectionListener(),
					Messages.getString("jpa.field.insertable"), true, Constants.JC_INSERTABLE, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$
			// create row for "updatable"
			FormRowCreator.createBooleanRow(
					toolkit,
					jcComposite,
					mapCheckBoxes,
					getDefaultSelectionListener(),
					Messages.getString("jpa.field.updatable"), true, Constants.JC_UPDATABLE, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$
			// create row for "columnDefinition"
			FormRowCreator.createStringRow(
					toolkit,
					jcComposite,
					mapTexts,
					getDefaultModifyListener(),
					Messages.getString("jpa.field.column.definition"), "", Constants.JC_COLUMN_DEFINITION, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$ //$NON-NLS-2$
			// create row for "table"
			FormRowCreator.createStringRow(toolkit, jcComposite, mapTexts, getDefaultModifyListener(),
					Messages.getString("jpa.field.table"), "", Constants.JC_TABLE, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$ //$NON-NLS-2$

			jcSection.setText(Messages.getString("jpa.fields.page.join.column.section.desc"));//$NON-NLS-1$
			jcSection.setClient(jcComposite);
			jcSection.setExpanded(true);
			gd = new GridData(GridData.FILL_BOTH);
			gd.horizontalSpan = 2;
			jcSection.setLayoutData(gd);

		}

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

	private SelectionListener getTargetEntityClearListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				targetEntityControl.setText("");//$NON-NLS-1$
				try {
					ModelUpdater.updateJpaManyToOneModel(getEntity(), getFieldModel().getManyToOneModel(),
							DbAnnotationConstants.TARGET_ENTITY, "", null, "");
					setDirty(getEntity().isDirty());
				} catch (Exception ex) {
					ErrorDialog.openError(((IEditorPart)managedForm.getContainer()).getSite().getShell(),
							Messages.getString("error.problem.occurred"), ex.getMessage(), new Status(//$NON-NLS-1$
									IStatus.ERROR, Activator.PLUGIN_ID, ex.getMessage()));
				}
			}

		};
	}

	private static String[] getCascadeItems() {
		List<String> list = new ArrayList<String>();
		list.add(CascadeType.ALL.toString());
		list.add(CascadeType.PERSIST.toString());
		list.add(CascadeType.MERGE.toString());
		list.add(CascadeType.REMOVE.toString());
		list.add(CascadeType.REFRESH.toString());
		list.add(CascadeType.DETACH.toString());
		return list.toArray(new String[] {});
	}

	private static String[] getFetchTypeItems() {
		List<String> list = new ArrayList<String>();
		list.add(FetchType.EAGER.toString());
		list.add(FetchType.LAZY.toString());
		return list.toArray(new String[] {});
	}

}
