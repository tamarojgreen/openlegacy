package com.openlegacy.enterprise.ide.eclipse.editors.pages.validators;

import com.openlegacy.enterprise.ide.eclipse.editors.ScreenEntityEditor;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.IOpenLegacyMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;

import java.text.MessageFormat;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public abstract class TextValidator {

	private static final String DEFAULT_MESSAGE_KEY = "default.key";

	private Text control;
	private ModifyListener modifyListener;
	private boolean isValid;
	private IManagedForm managedForm;
	private String currentText;
	private ScreenEntityEditor editor;
	private UUID uuid;
	private String validationText = "";
	private IOpenLegacyMasterBlock masterBlock;

	public TextValidator(IOpenLegacyMasterBlock masterBlock, IManagedForm managedForm, Text control, UUID modelUuid) {
		this.masterBlock = masterBlock;
		this.managedForm = managedForm;
		this.control = control;
		this.uuid = modelUuid;
		init();
	}

	protected abstract boolean validateControl(TextValidator validator, UUID uuid);

	protected abstract NamedObject getModel();

	private void init() {
		isValid = true;

		Object container = managedForm.getContainer();
		if (container instanceof FormPage) {
			FormEditor formEditor = ((FormPage)container).getEditor();
			if (formEditor instanceof ScreenEntityEditor) {
				this.editor = (ScreenEntityEditor)formEditor;
			}
		}

		currentText = control.getText();

		createListners();
		addListeners();
	}

	private void createListners() {
		modifyListener = new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				handleModifyTextEvent(e);
			}
		};
	}

	protected void handleModifyTextEvent(ModifyEvent e) {
		// Validation is not required if the current text contents is the
		// same as the new text contents
		String newText = control.getText();
		if (newText.equals(currentText)) {
			return;
		}
		// Save the current contents of the Text field
		currentText = newText;
		// Perform validation
		try {
			validate(uuid);
		} catch (CoreException e1) {
		}
	}

	private boolean validate(UUID uuid) throws CoreException {
		// Validate the control
		isValid = validateControl(this, uuid);
		// If the control is valid, remove all the messages associated with
		// the control (in case they were not individually removed by the
		// child class)
		if (isValid) {
			removeValidationMarker();
		}
		if ((editor != null) && !isValid && (uuid != null)) {
			editor.addValidationMarker(MessageFormat.format("{0}-{1}", uuid.toString(), control.getData(FormRowCreator.ID_KEY)),//$NON-NLS-1$
					this.validationText);
		}
		return isValid;
	}

	private void addListeners() {
		control.addModifyListener(modifyListener);
	}

	private String getMessageKey() {
		return uuid != null ? uuid.toString() : DEFAULT_MESSAGE_KEY;
	}

	public Text getControl() {
		return control;
	}

	public void setModelUUID(UUID uuid) {
		this.uuid = uuid;
		managedForm.getMessageManager().removeMessages(control);
	}

	/**
	 * Adds a message to ManagedForm
	 */
	public void addMessage(String messageText, int messageType, UUID uuid) {
		this.validationText = messageText;
		// Delegate to message manager
		if (this.uuid != null && uuid != null && this.uuid.equals(uuid)) {
			managedForm.getMessageManager().addMessage(getMessageKey(), messageText, null, messageType, control);
			addRemoveValidationMessageToModel(false, (String)control.getData(FormRowCreator.ID_KEY), messageText, uuid);
		} else {
			managedForm.getMessageManager().removeMessages(control);
			addRemoveValidationMessageToModel(true, (String)control.getData(FormRowCreator.ID_KEY), null, uuid);
		}
	}

	public void removeValidationMarker() {
		if ((managedForm != null) && (control != null)) {
			managedForm.getMessageManager().removeMessage(getMessageKey(), control);
			addRemoveValidationMessageToModel(true, (String)control.getData(FormRowCreator.ID_KEY), null, uuid);
			// managedForm.getMessageManager().removeMessages(control);
			this.validationText = "";
			if ((editor != null) && (uuid != null)) {
				editor.removeValidationMarker(MessageFormat.format("{0}-{1}", uuid.toString(),//$NON-NLS-1$
						control.getData(FormRowCreator.ID_KEY)));
			}
		}
	}

	public void revalidate(UUID uuid) {
		// Perform validation
		try {
			validate(uuid);
		} catch (CoreException e1) {
		}
	}

	private void addRemoveValidationMessageToModel(boolean isValid, String key, String message, UUID uuid) {
		NamedObject model = getModel();
		if (model != null) {
			if (!model.getUUID().equals(uuid)) {
				return;
			}
			if (isValid) {
				model.getValidationMessages().remove(key);
			} else {
				model.getValidationMessages().put(key, message);
			}
			masterBlock.updateLabels();
		}
	}

}
