package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.rpc;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters.FieldTypeViewerFilter;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntity;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcEntityModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.rpc.RpcPartModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator.JAVA_DOCUMENTATION_TYPE;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.rpc.FieldsMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.validators.TextValidator;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
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
	private TextValidator orderValidator;

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
		FormRowCreator.createLabelRow(
				toolkit,
				client,
				mapLabels,
				Messages.getString("rpc.field.java.type.label"), "", Constants.JAVA_TYPE_NAME, JAVA_DOCUMENTATION_TYPE.RPC, "RpcField");//$NON-NLS-1$ //$NON-NLS-2$
		// create row for "fieldName"
		Text fieldNameControl = FormRowCreator.createStringRow(
				toolkit,
				client,
				mapTexts,
				getDefaultModifyListener(),
				Messages.getString("rpc.field.field.name.label"), "", Constants.FIELD_NAME, JAVA_DOCUMENTATION_TYPE.RPC, "RpcField");//$NON-NLS-1$ //$NON-NLS-2$
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
		FormRowCreator.createStringRow(
				toolkit,
				client,
				mapTexts,
				getDefaultModifyListener(),
				Messages.getString("rpc.field.original.name.label"), "", RpcAnnotationConstants.ORIGINAL_NAME, JAVA_DOCUMENTATION_TYPE.RPC, "RpcField");//$NON-NLS-1$ //$NON-NLS-2$
		// "displayName"
		FormRowCreator.createStringRow(
				toolkit,
				client,
				mapTexts,
				getDefaultModifyListener(),
				Messages.getString("rpc.field.display.name.label"), "", AnnotationConstants.DISPLAY_NAME, JAVA_DOCUMENTATION_TYPE.RPC, "RpcField");//$NON-NLS-1$ //$NON-NLS-2$
		// "int length"
		Text lengthControl = FormRowCreator.createIntRow(
				toolkit,
				client,
				mapTexts,
				getDefaultModifyListener(),
				getDefaultVerifyListener(),
				Messages.getString("rpc.field.length.label"), 0, AnnotationConstants.LENGTH, JAVA_DOCUMENTATION_TYPE.RPC, "RpcField");//$NON-NLS-1$ 
		lengthValidator = new TextValidator(master, managedForm, lengthControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateLessThanZero(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return getFieldModel();
			}

		};
		// "key"
		FormRowCreator.createBooleanRow(
				toolkit,
				client,
				mapCheckBoxes,
				getDefaultSelectionListener(),
				Messages.getString("rpc.field.key.label"), false, AnnotationConstants.KEY, JAVA_DOCUMENTATION_TYPE.RPC, "RpcField");//$NON-NLS-1$
		// "editable"
		FormRowCreator.createBooleanRow(
				toolkit,
				client,
				mapCheckBoxes,
				getDefaultSelectionListener(),
				Messages.getString("rpc.field.editable.label"), false, AnnotationConstants.EDITABLE, JAVA_DOCUMENTATION_TYPE.RPC, "RpcField");//$NON-NLS-1$
		// "Direction direction"
		FormRowCreator.createComboBoxRow(
				toolkit,
				client,
				mapCombos,
				getDefaultModifyListener(),
				getDefaultComboBoxKeyListener(),
				Messages.getString("rpc.field.direction.label"), getDirectionItems(), 0, RpcAnnotationConstants.DIRECTION, false, JAVA_DOCUMENTATION_TYPE.RPC, "RpcField");//$NON-NLS-1$
		// "Class<? extends FieldType> fieldType"
		FormRowCreator.createComboBoxRowWithBrowseButton(toolkit, client, mapCombos, getDefaultModifyListener(),
				getDefaultComboBoxKeyListener(), Messages.getString("rpc.field.field.type.label"), getFieldTypes(), 0,//$NON-NLS-1$
				AnnotationConstants.FIELD_TYPE, new FieldTypeViewerFilter(), JAVA_DOCUMENTATION_TYPE.RPC, "RpcField");
		// "String sampleValue"
		FormRowCreator.createStringRow(
				toolkit,
				client,
				mapTexts,
				getDefaultModifyListener(),
				Messages.getString("rpc.field.sample.value.label"), "", AnnotationConstants.SAMPLE_VALUE, JAVA_DOCUMENTATION_TYPE.RPC, "RpcField");//$NON-NLS-1$ //$NON-NLS-2$
		// "String helpText"
		FormRowCreator.createStringRow(
				toolkit,
				client,
				mapTexts,
				getDefaultModifyListener(),
				Messages.getString("rpc.field.help.text.label"), "", AnnotationConstants.HELP_TEXT, JAVA_DOCUMENTATION_TYPE.RPC, "RpcField");//$NON-NLS-1$ //$NON-NLS-2$
		// "String defaultValue"
		FormRowCreator.createStringRow(
				toolkit,
				client,
				mapTexts,
				getDefaultModifyListener(),
				Messages.getString("rpc.field.default.value.label"), "", RpcAnnotationConstants.DEFAULT_VALUE, JAVA_DOCUMENTATION_TYPE.RPC, "RpcField");//$NON-NLS-1$ //$NON-NLS-2$
		// "String expression"
		FormRowCreator.createStringRow(
				toolkit,
				client,
				mapTexts,
				getDefaultModifyListener(),
				Messages.getString("rpc.field.expression"), "", RpcAnnotationConstants.EXPRESSION, JAVA_DOCUMENTATION_TYPE.RPC, "RpcField");//$NON-NLS-1$ //$NON-NLS-2$
		// int "Order"
		Text orderControl = FormRowCreator.createIntRow(
				toolkit,
				client,
				mapTexts,
				getOrderListener(),
				getDefaultVerifyListener(),
				Messages.getString("rpc.field.order.label"), 0, RpcAnnotationConstants.ORDER, JAVA_DOCUMENTATION_TYPE.RPC, "RpcField");//$NON-NLS-1$ //$NON-NLS-2$
		orderValidator = new TextValidator(master, managedForm, orderControl, null) {

			@Override
			protected boolean validateControl(TextValidator validator, UUID uuid) {
				return validateLessThanZero(validator, uuid);
			}

			@Override
			protected NamedObject getModel() {
				return getFieldModel();
			}

		};

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
			orderValidator.removeValidationMarker();
		}
	}

	@Override
	public void revalidate() {
		if (fieldNameValidator != null) {
			fieldNameValidator.revalidate(getModelUUID());
			lengthValidator.revalidate(getModelUUID());
			orderValidator.revalidate(getModelUUID());
		}
	}

	@Override
	protected void updateValidators(UUID uuid) {
		if (uuid != null) {
			fieldNameValidator.setModelUUID(uuid);
			lengthValidator.setModelUUID(uuid);
			orderValidator.revalidate(getModelUUID());
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

	private static boolean validateLessThanZero(TextValidator validator, UUID uuid) {
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

	private ModifyListener getOrderListener() {
		return new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {
				String text = ((Text)event.widget).getText();

				RpcEntity entity = getEntity();
				RpcFieldModel currentField = getFieldModel();

				NamedObject targetParent = currentField.getParent();
				List<RpcFieldModel> targetParentList = null;
				UUID targetUUID = null;
				if (targetParent instanceof RpcPartModel) {
					RpcPartModel sortedPart = entity.getSortedPartByUUID(currentField.getParent().getUUID());
					if (sortedPart == null) {
						return;
					}
					targetParentList = sortedPart.getSortedFields();
					targetUUID = ((RpcPartModel)targetParent).getUUID();
				} else if (targetParent instanceof RpcEntityModel) {
					targetParentList = entity.getSortedFields();
					targetUUID = entity.getEntityModel().getUUID();
				}

				int currentFieldIdx = targetParentList.indexOf(currentField);
				if (currentFieldIdx == -1) {
					return;
				}
				currentField = targetParentList.get(currentFieldIdx);

				for (int i = 0; i < targetParentList.size(); i++) {
					if (targetParentList.get(i).getOrder() == 0) {
						if (targetParentList.indexOf(targetParentList.get(i)) != 0) {
							int comparerIdx = i;
							targetParentList.get(i).setOrder(i);
							for (int j = comparerIdx + 1; j < targetParentList.size(); j++) {
								if (comparerIdx == targetParentList.get(j).getOrder()) {
									targetParentList.get(j).setOrder(comparerIdx + 1);
									comparerIdx = comparerIdx + 1;
								}
							}
						}
					}
				}

				int currIdx = currentField.getOrder();

				int targetIdx = 0;
				if (!StringUtils.isEmpty(text)) {
					targetIdx = Integer.parseInt(text);
				}
				RpcFieldModel targetField = null;

				for (RpcFieldModel field : targetParentList) {
					if (field.getOrder() == targetIdx) {
						targetField = field;
					}
				}

				if (targetUUID == null || currIdx == targetIdx) {
					return;
				}

				currentField.setOrder(targetIdx);
				if (targetField != null) {
					if (targetIdx > currIdx) {
						targetField.setOrder(targetIdx - 1);
						// recalculate orders for fields that have lesser order value
						if (targetParentList.indexOf((targetField)) - 1 > 0) {
							recalculateOrders(currentField, targetField, targetParentList, targetIdx - 1, false);
						}
					} else {
						targetField.setOrder(targetIdx + 1);
						// recalculate orders for fields that have higher order value
						if (targetParentList.indexOf((targetField)) + 1 < targetParentList.size()) {
							recalculateOrders(currentField, targetField, targetParentList, targetIdx + 1, true);
						}
					}
				}

				String key = (String)event.widget.getData(FormRowCreator.ID_KEY);
				updateModel(key);
				((FieldsMasterBlock)master).reassignMasterBlockViewerInput(targetUUID);
			}
		};
	}

	private static void recalculateOrders(RpcFieldModel currentField, RpcFieldModel targetField,
			List<RpcFieldModel> targetParentList, int comparerIdx, boolean recalcNavigationUp) {

		if (recalcNavigationUp) {
			for (int i = targetParentList.indexOf((targetField)) + 1; i < targetParentList.size(); i++) {
				if (targetParentList.get(i).getOrder() == comparerIdx && i != targetParentList.indexOf(currentField)) {
					targetParentList.get(i).setOrder(comparerIdx + 1);
					comparerIdx = comparerIdx + 1;
				}
			}
		} else {
			for (int i = targetParentList.indexOf((targetField)) - 1; i >= 0; i--) {
				if (targetParentList.get(i).getOrder() == comparerIdx && i != targetParentList.indexOf(currentField)) {
					targetParentList.get(i).setOrder(comparerIdx - 1);
					comparerIdx = comparerIdx - 1;
				}
			}
		}
	}
}
