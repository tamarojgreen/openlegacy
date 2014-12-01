package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenNamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.screen.ScreenPartModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.screen.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.TextValidator;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
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

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class PartsScreenPartDetailsPage extends AbstractScreenDetailsPage {

	private ScreenPartModel partModel;
	private TextValidator classNameValidator;

	public PartsScreenPartDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	@Override
	public Class<?> getDetailsModel() {
		return ScreenPartModel.class;
	}

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
		section.setText(Messages.getString("PartsScreenPartDetailsPage.name")); //$NON-NLS-1$
		section.setDescription(Messages.getString("PartsScreenPartDetailsPage.desc")); //$NON-NLS-1$
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
				Messages.getString("ScreenPart.className"), "", Constants.JAVA_TYPE_NAME);
		classNameValidator = new TextValidator(master, managedForm, classNameControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateTextControls(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return partModel;
			}

		};
		// create row for 'name'
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenPart.name"), "", AnnotationConstants.NAME);//$NON-NLS-1$ //$NON-NLS-2$
		// create row for 'displayName'
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("ScreenPart.displayName"), "", AnnotationConstants.DISPLAY_NAME);//$NON-NLS-1$ //$NON-NLS-2$
		// create row for 'supportTerminalData'
		FormRowCreator.createBooleanRow(toolkit, client, mapCheckBoxes, getDefaultSelectionListener(),
				Messages.getString("ScreenPart.supportTerminalData"), false, ScreenAnnotationConstants.SUPPORT_TERMINAL_DATA);//$NON-NLS-1$
		// create empty row
		FormRowCreator.createSpacer(toolkit, client, 2);
		// create section for @PartPosition
		createPartPostitionSection(toolkit, topClient);

		toolkit.paintBordersFor(section);
		section.setClient(topClient);
	}

	@Override
	public UUID getModelUUID() {
		return partModel != null ? partModel.getUUID() : null;
	}

	@Override
	public void revalidate() {}

	@Override
	protected void updateControls() {
		ControlsUpdater.updateScreenPartDetailsControls(partModel, mapTexts, mapCheckBoxes);
	}

	@Override
	protected void doUpdateModel(String key) {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateScreenPartModel(getEntity(), partModel, key, (String)map.get(Constants.TEXT_VALUE),
				(Boolean)map.get(Constants.BOOL_VALUE));
	}

	@Override
	protected void afterDoUpdateModel() {
		setScreenPreviewDrawingRectangle(partModel);
		setDirty(getEntity().isDirty());
	}

	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			partModel = (ScreenPartModel)selection.getFirstElement();
		} else {
			partModel = null;
		}

		if (partModel != null) {
			setScreenPreviewDrawingRectangle(partModel);
		}
	}

	@Override
	public ScreenNamedObject getPageScreenNamedObject() {
		return null;
	}

	private void createPartPostitionSection(FormToolkit toolkit, Composite parent) {
		Section section = toolkit.createSection(parent, ExpandableComposite.EXPANDED);

		toolkit.createCompositeSeparator(section);

		Composite client = toolkit.createComposite(section);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		client.setLayout(layout);

		// create row for 'row'
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("PartPosition.row"), 0, ScreenAnnotationConstants.ROW);
		// create row for 'column'
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("PartPosition.column"), 0, ScreenAnnotationConstants.COLUMN);
		// create row for 'width'
		FormRowCreator.createIntRow(toolkit, client, mapTexts, getDefaultModifyListener(), getDefaultVerifyListener(),
				Messages.getString("PartPosition.width"), 80, ScreenAnnotationConstants.WIDTH);

		section.setText(Messages.getString("PartPosition.sectionDesc"));//$NON-NLS-1$
		section.setClient(client);

		GridData gd = new GridData(GridData.FILL_BOTH);
		section.setLayoutData(gd);
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
	protected void updateValidators(UUID uuid) {
		if (uuid != null) {
			classNameValidator.setModelUUID(uuid);
		}
	}

	private static void setScreenPreviewDrawingRectangle(ScreenPartModel model) {
		ScreenPreview screenPreview = (ScreenPreview)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(
				ScreenPreview.ID);
		if (screenPreview != null) {
			List<ScreenFieldModel> sortedFields = model.getSortedFields();
			int startRow = sortedFields.size() > 0 ? sortedFields.get(0).getRow() : 0;
			for (ScreenFieldModel field : sortedFields) {
				if (startRow > field.getRow()) {
					startRow = field.getRow();
				}
			}
			int endRow = sortedFields.size() > 0 ? sortedFields.get(0).getEndRow() : 0;
			for (ScreenFieldModel field : sortedFields) {
				if (endRow < field.getEndRow()) {
					endRow = field.getEndRow();
				}
			}
			int startColumn = sortedFields.size() > 0 ? sortedFields.get(0).getColumn() : 0;
			for (ScreenFieldModel field : sortedFields) {
				if (startColumn > field.getColumn()) {
					startColumn = field.getColumn();
				}
			}
			int endColumn = sortedFields.size() > 0 ? sortedFields.get(0).getEndColumn() : 0;
			for (ScreenFieldModel field : sortedFields) {
				if (endColumn < field.getEndColumn()) {
					endColumn = field.getEndColumn();
				}
			}
			screenPreview.addFieldRectangle(
					new FieldRectangle(startRow, endRow, startColumn, endColumn, ""), SWT.COLOR_YELLOW, true);//$NON-NLS-1$
		}
	}

}
