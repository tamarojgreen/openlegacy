package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.rpc;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcPartModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.rpc.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.TextValidator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.openlegacy.designtime.generators.AnnotationConstants;
import org.openlegacy.designtime.rpc.generators.support.RpcAnnotationConstants;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class PartsRpcPartDetailsPage extends AbstractRpcDetailsPage {

	private RpcPartModel partModel;
	private TextValidator classNameValidator;
	private TextValidator countValidator;

	public PartsRpcPartDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#getDetailsModel()
	 */
	@Override
	public Class<?> getDetailsModel() {
		return RpcPartModel.class;
	}

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
		section.setText(Messages.getString("rpc.fields.page.part.details.text")); //$NON-NLS-1$
		section.setDescription(Messages.getString("rpc.fields.page.details.desc")); //$NON-NLS-1$
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
		// create row for "className"
		Text classNameControl = FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("rpc.field.part.class.name.label"), "", Constants.JAVA_TYPE_NAME);
		classNameValidator = new TextValidator(master, managedForm, classNameControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateClassNameControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return partModel;
			}

		};
		// create row for 'name'
		// XXX Ivan: comment row creation of 'name' for better time
		// FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
		//				Messages.getString("rpc.field.part.name.label"), "", AnnotationConstants.NAME);//$NON-NLS-1$ //$NON-NLS-2$
		// create row for 'displayName'
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("rpc.field.part.display.name.label"), "", AnnotationConstants.DISPLAY_NAME);//$NON-NLS-1$ //$NON-NLS-2$

		// @RpcPartList section
		addRpcPartListSection(toolkit, client);

		toolkit.paintBordersFor(section);
		section.setClient(topClient);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#getModelUUID()
	 */
	@Override
	public UUID getModelUUID() {
		return partModel != null ? partModel.getUUID() : null;
	}

	@Override
	public void revalidate() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#updateControls()
	 */
	@Override
	protected void updateControls() {
		ControlsUpdater.updateRpcPartDetailsControls(partModel, mapTexts);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#doUpdateModel(java.lang.String)
	 */
	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateRpcPartModel(getEntity(), partModel, key, (String)map.get(Constants.TEXT_VALUE));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#afterDoUpdateModel()
	 */
	@Override
	protected void afterDoUpdateModel() {
		setDirty(getEntity().isDirty());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#selectionChanged(org.eclipse.jface.viewers.
	 * IStructuredSelection)
	 */
	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			partModel = (RpcPartModel)selection.getFirstElement();
		} else {
			partModel = null;
		}
	}

	@Override
	public void removeValidationMarkers() {
		super.removeValidationMarkers();
		if (classNameValidator != null) {
			classNameValidator.removeValidationMarker();
			countValidator.removeValidationMarker();
		}
	}

	@Override
	protected void updateValidators(UUID uuid) {
		super.updateValidators(uuid);
		if (uuid != null) {
			classNameValidator.setModelUUID(uuid);
			countValidator.setModelUUID(uuid);
		}
	}

	private void addRpcPartListSection(FormToolkit toolkit, Composite client) {
		Section section = toolkit.createSection(client, ExpandableComposite.TWISTIE | Section.DESCRIPTION);
		section.setActiveToggleColor(toolkit.getHyperlinkGroup().getActiveForeground());
		section.setToggleColor(toolkit.getColors().getColor(IFormColors.SEPARATOR));
		toolkit.createCompositeSeparator(section);

		Composite composite = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;

		composite.setLayout(layout);

		// 'count' row
		Text countControl = FormRowCreator.createIntRow(toolkit, composite, mapTexts, getDefaultModifyListener(),
				getDefaultVerifyListener(), Messages.getString("rpc.part.list.count.label"), 0, RpcAnnotationConstants.COUNT);//$NON-NLS-1$
		countValidator = new TextValidator(master, managedForm, countControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateCountControl(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return partModel;
			}

		};

		section.setText(Messages.getString("rpc.part.list.section.text"));//$NON-NLS-1$
		section.setClient(composite);
		section.setExpanded(true);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		section.setLayoutData(gd);
	}

	private static boolean validateClassNameControl(TextValidator validator, UUID uuid) {
		String text = validator.getControl().getText();
		boolean isValid = !text.isEmpty();
		if (!isValid) {
			validator.addMessage(Messages.getString("validation.should.not.be.empty"), IMessageProvider.ERROR, uuid);//$NON-NLS-1$
			return isValid;
		}
		return isValid;
	}

	private static boolean validateCountControl(TextValidator validator, UUID uuid) {
		String text = validator.getControl().getText();
		boolean isValid = text.isEmpty();
		int value = isValid ? 1 : Integer.valueOf(text).intValue();
		isValid = isValid ? true : value > 0;
		if (!isValid) {
			validator.addMessage(
					Messages.getString("validation.should.be.null.or.greater.than.one"), IMessageProvider.ERROR, uuid);//$NON-NLS-1$
		}
		return isValid;
	}

}
