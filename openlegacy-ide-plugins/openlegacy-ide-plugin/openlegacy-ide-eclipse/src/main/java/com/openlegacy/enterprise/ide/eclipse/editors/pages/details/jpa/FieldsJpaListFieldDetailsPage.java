package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaListFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator.JAVA_DOCUMENTATION_TYPE;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.TextValidator;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
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
import org.openlegacy.designtime.db.generators.support.DbAnnotationConstants;
import org.openlegacy.ide.eclipse.Activator;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;

/**
 * @author Ivan Bort
 * 
 */
public class FieldsJpaListFieldDetailsPage extends AbstractJpaFieldDetailsPage {

	private JpaListFieldModel fieldModel;
	private Text targetEntityControl;
	private Text listParameterControl;

	private TextValidator listParameterValidator;

	public FieldsJpaListFieldDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	@Override
	protected JpaFieldModel getFieldModel() {
		return fieldModel;
	}

	@Override
	protected void addTopContent(FormToolkit toolkit, Composite client) {
		// create row for displaying java type name
		FormRowCreator.createLabelRow(toolkit, client, mapLabels,
				Messages.getString("jpa.field.java.type"), "", Constants.JAVA_TYPE_NAME, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		// create row for displaying List<?> parameter
		listParameterControl = FormRowCreator.createStringRowWithBrowseButton(toolkit, client, mapTexts,
				getDefaultModifyListener(), Messages.getString("jpa.field.list.arg"), "", Constants.LIST_TYPE_ARG, null,//$NON-NLS-1$ //$NON-NLS-2$
				JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$
		listParameterValidator = new TextValidator(master, managedForm, listParameterControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateListParameterControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return fieldModel;
			}
		};
	}

	@Override
	protected void addBottomContent(FormToolkit toolkit, Composite client) {
		// create section for @OneToMany annotation
		Section section = toolkit.createSection(client, ExpandableComposite.TWISTIE | Section.DESCRIPTION);
		section.setActiveToggleColor(toolkit.getHyperlinkGroup().getActiveForeground());
		section.setToggleColor(toolkit.getColors().getColor(IFormColors.SEPARATOR));
		toolkit.createCompositeSeparator(section);

		Composite composite = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;

		composite.setLayout(layout);

		// create row for "cascade"
		FormRowCreator.createComboBoxRow(toolkit, composite, mapCombos, getDefaultModifyListener(),
				getDefaultComboBoxKeyListener(), Messages.getString("jpa.field.list.cascade"), getCascadeTypeItems(), 0,//$NON-NLS-1$
				DbAnnotationConstants.CASCADE, false, JAVA_DOCUMENTATION_TYPE.JPA, "Column");
		// create row for "fetchType"
		FormRowCreator.createComboBoxRow(toolkit, composite, mapCombos, getDefaultModifyListener(),
				getDefaultComboBoxKeyListener(), Messages.getString("jpa.field.list.fetch.type"), getFetchTypeItems(), 0,//$NON-NLS-1$
				DbAnnotationConstants.FETCH, false, JAVA_DOCUMENTATION_TYPE.JPA, "Column");
		// create row for "mappedBy"
		FormRowCreator.createStringRow(
				toolkit,
				composite,
				mapTexts,
				getDefaultModifyListener(),
				Messages.getString("jpa.field.list.mapped.by"), "", DbAnnotationConstants.MAPPED_BY, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "orphalRemoval"
		FormRowCreator.createBooleanRow(
				toolkit,
				composite,
				mapCheckBoxes,
				getDefaultSelectionListener(),
				Messages.getString("jpa.field.list.orphan.removal"), false, DbAnnotationConstants.ORPHAN_REMOVAL, JAVA_DOCUMENTATION_TYPE.JPA, "Column");//$NON-NLS-1$
		// create row for "targetEntity"
		targetEntityControl = FormRowCreator.createStringRowWithBrowseButton(toolkit, composite, mapTexts,
				getDefaultModifyListener(), Messages.getString("jpa.field.list.target.entity"), "",//$NON-NLS-1$ //$NON-NLS-2$
				DbAnnotationConstants.TARGET_ENTITY, null, true, getTargetEntityClearListener(), JAVA_DOCUMENTATION_TYPE.JPA,
				"Column");

		section.setText(Messages.getString("jpa.fields.page.one.to.many.section.desc"));//$NON-NLS-1$
		section.setClient(composite);
		section.setExpanded(true);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		section.setLayoutData(gd);
	}

	@Override
	public Class<?> getDetailsModel() {
		return JpaListFieldModel.class;
	}

	@Override
	public UUID getModelUUID() {
		return fieldModel != null ? fieldModel.getUUID() : null;
	}

	@Override
	protected void updateControls() {
		if (fieldModel == null) {
			return;
		}
		ControlsUpdater.updateJpaFieldDetailsControls(fieldModel, mapTexts, mapCheckBoxes, mapLabels);
		ControlsUpdater.updateJpaListFieldDetailsControls(fieldModel, mapTexts, mapCheckBoxes, mapCombos);
		revalidate();
	}

	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateJpaFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE),
				(Boolean)map.get(Constants.BOOL_VALUE));
		ModelUpdater.updateJpaListFieldModel(getEntity(), fieldModel, key, (String)map.get(Constants.TEXT_VALUE),
				(Boolean)map.get(Constants.BOOL_VALUE), (String)map.get(Constants.FULLY_QUALIFIED_NAME_VALUE));
	}

	@Override
	protected void afterDoUpdateModel() {
		setDirty(getEntity().isDirty());
	}

	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			fieldModel = (JpaListFieldModel)selection.getFirstElement();
		} else {
			fieldModel = null;
		}
	}

	@Override
	protected void updateValidators(UUID uuid) {
		super.updateValidators(uuid);
		if (uuid != null) {
			listParameterValidator.setModelUUID(uuid);
		}
	}

	@Override
	public void revalidate() {
		super.revalidate();
		if (listParameterValidator != null) {
			listParameterValidator.revalidate(getModelUUID());
		}
	}

	@Override
	public void removeValidationMarkers() {
		super.removeValidationMarkers();
		if (listParameterValidator != null) {
			listParameterValidator.removeValidationMarker();
		}
	}

	private static String[] getCascadeTypeItems() {
		List<String> list = new ArrayList<String>();
		list.add(CascadeType.ALL.toString());
		list.add(CascadeType.MERGE.toString());
		list.add(CascadeType.PERSIST.toString());
		list.add(CascadeType.REFRESH.toString());
		list.add(CascadeType.REMOVE.toString());
		return list.toArray(new String[] {});
	}

	private static String[] getFetchTypeItems() {
		List<String> list = new ArrayList<String>();
		list.add(FetchType.EAGER.toString());
		list.add(FetchType.LAZY.toString());
		return list.toArray(new String[] {});
	}

	private SelectionListener getTargetEntityClearListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				targetEntityControl.setText("");//$NON-NLS-1$
				try {
					ModelUpdater.updateJpaFieldModel(getEntity(), fieldModel, DbAnnotationConstants.TARGET_ENTITY, "", null);
					setDirty(getEntity().isDirty());
				} catch (Exception ex) {
					ErrorDialog.openError(((IEditorPart)managedForm.getContainer()).getSite().getShell(),
							Messages.getString("error.problem.occurred"), ex.getMessage(), new Status(//$NON-NLS-1$
									IStatus.ERROR, Activator.PLUGIN_ID, ex.getMessage()));
				}
			}

		};
	}

	private boolean validateListParameterControl(TextValidator validator, UUID uuid) {
		boolean isValid = true;
		String text = validator.getControl().getText();
		String fullyQuailifiedName = (String)validator.getControl().getData(FormRowCreator.ID_FULLY_QUALIFIED_NAME);
		if (StringUtils.isEmpty(text)) {
			isValid = false;
		}
		if (isValid && StringUtils.equalsIgnoreCase(text, Object.class.getSimpleName())) {
			isValid = false;
		}
		if (!isValid) {
			validator.addMessage(Messages.getString("validation.list.param.must.be.specified"), IMessageProvider.ERROR, uuid);//$NON-NLS-1$
			return isValid;
		}
		if (getEntity().getEntityFullyQualifiedName().equals(fullyQuailifiedName)) {
			isValid = false;
		}
		if (!isValid) {
			validator.addMessage(
					Messages.getString("validation.list.param.equal.to.current.entity"), IMessageProvider.ERROR, uuid);//$NON-NLS-1$
		}
		return isValid;
	}

}
