package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters.TerminalDrilldownActionViewerFilter;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.TableActionModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.TextValidator;
import com.openlegacy.enterprise.ide.eclipse.editors.utils.Utils;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.openlegacy.annotations.screen.ScreenEntity;
import org.openlegacy.designtime.generators.AnnotationConstants;

import java.lang.annotation.Annotation;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class TablesTableActionDetailsPage extends AbstractScreenDetailsPage {

	private TableActionModel actionModel;
	private TextValidator displayNameValidator;
	private TextValidator targetEntityValidator;

	public TablesTableActionDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	@Override
	public Class<?> getDetailsModel() {
		return TableActionModel.class;
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
		section.setText(Messages.getString("TableActionDetailsPage.name")); //$NON-NLS-1$
		section.setDescription(Messages.getString("TableActionDetailsPage.desc")); //$NON-NLS-1$
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
		// create row for "action"
		FormRowCreator.createStringRowWithBrowseButton(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("TableAction.action"), "", AnnotationConstants.ACTION,//$NON-NLS-1$ //$NON-NLS-2$
				new TerminalDrilldownActionViewerFilter());
		// create row for "defaultAction"
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("TableAction.defaultAction"), false, AnnotationConstants.DEFAULT_ACTION);//$NON-NLS-1$
		// create row for "actionValue"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("TableAction.actionValue"), " ", AnnotationConstants.ACTION_VALUE);//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "displayName"
		Text displayNameControl = FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("TableAction.displayName"), " ", AnnotationConstants.DISPLAY_NAME);//$NON-NLS-1$ //$NON-NLS-2$
		displayNameValidator = new TextValidator(master, managedForm, displayNameControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateTextControls(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return actionModel;
			}

		};
		// create row for "alias"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("TableAction.alias"),//$NON-NLS-1$
				"", AnnotationConstants.ALIAS);
		// create row for "targetEntity"
		Text targetEntityControl = FormRowCreator.createStringRowWithBrowseButton(toolkit, client, mapTexts,
				getDefaultModifyListener(), Messages.getString("TableAction.targetEntity"), "",
				AnnotationConstants.TARGET_ENTITY, null);
		targetEntityValidator = new TextValidator(master, managedForm, targetEntityControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateTargetEntityControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return actionModel;
			}

		};

		toolkit.paintBordersFor(section);
		section.setClient(client);
	}

	@Override
	public UUID getModelUUID() {
		return actionModel != null ? actionModel.getUUID() : null;
	}

	@Override
	public void revalidate() {
		if (displayNameValidator != null) {
			displayNameValidator.revalidate(getModelUUID());
			targetEntityValidator.revalidate(getModelUUID());
		}
	}

	@Override
	protected void updateControls() {
		ControlsUpdater.updateTableActionDetailsPage(actionModel, mapTexts, mapCheckBoxes);
		revalidate();
	}

	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateTableActionModel(getEntity(), actionModel, key, (String)map.get(Constants.TEXT_VALUE),
				(Boolean)map.get(Constants.BOOL_VALUE), (String)map.get(Constants.FULLY_QUALIFIED_NAME_VALUE));
	}

	@Override
	protected void afterDoUpdateModel() {
		setDirty(getEntity().isDirty());
	}

	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			actionModel = (TableActionModel)selection.getFirstElement();
		} else {
			actionModel = null;
		}
	}

	@Override
	protected void updateValidators(UUID uuid) {
		if (uuid != null && displayNameValidator != null) {
			displayNameValidator.setModelUUID(uuid);
			targetEntityValidator.setModelUUID(uuid);
		}
	}

	@Override
	public void removeValidationMarkers() {
		if (displayNameValidator != null) {
			displayNameValidator.removeValidationMarker();
			targetEntityValidator.removeValidationMarker();
		}
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

	private static boolean validateTargetEntityControl(TextValidator validator, UUID uuid) {
		boolean isValid = true;
		String text = validator.getControl().getText();
		String fullyQuailifiedName = (String)validator.getControl().getData(FormRowCreator.ID_FULLY_QUALIFIED_NAME);
		if (StringUtils.isEmpty(fullyQuailifiedName) || StringUtils.isEmpty(text)) {
			return isValid;
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
