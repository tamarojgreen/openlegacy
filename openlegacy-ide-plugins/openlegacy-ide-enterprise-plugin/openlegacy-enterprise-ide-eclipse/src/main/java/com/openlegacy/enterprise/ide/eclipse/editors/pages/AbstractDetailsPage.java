package com.openlegacy.enterprise.ide.eclipse.editors.pages;

import com.openlegacy.enterprise.ide.eclipse.Activator;
import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IFormPart;
import org.eclipse.ui.forms.IManagedForm;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractDetailsPage implements IOpenLegacyDetailsPage, IDetailsPage {

	protected IManagedForm managedForm;

	protected Map<String, Text> mapTexts = new HashMap<String, Text>();

	protected Map<String, Button> mapCheckBoxes = new HashMap<String, Button>();

	protected Map<String, CCombo> mapCombos = new HashMap<String, CCombo>();

	protected Map<String, Label> mapLabels = new HashMap<String, Label>();

	protected boolean isDirty = false;

	protected boolean updatingControls = false;

	protected AbstractMasterBlock master;

	public AbstractDetailsPage(AbstractMasterBlock master) {
		this.master = master;
	}

	@Override
	public abstract Class<?> getDetailsModel();

	@Override
	public abstract void createContents(Composite parent);

	@Override
	public abstract UUID getModelUUID();

	protected abstract void updateControls();

	protected abstract void doUpdateModel(String key) throws MalformedURLException, CoreException;

	protected abstract void afterDoUpdateModel();

	protected abstract void selectionChanged(IStructuredSelection selection);

	@Override
	public void removeValidationMarkers() {
		// if the inheritor is using validators, he can override this method to remove validation markers
	}

	protected void updateValidators(UUID uuid) {
		// if the inheritor is using validators, he can override this method to update his validators
	};

	@Override
	public void selectionChanged(IFormPart part, ISelection selection) {
		IStructuredSelection structuredSelection = (IStructuredSelection)selection;
		selectionChanged(structuredSelection);
		refresh();
	}

	@Override
	public void commit(boolean onSave) {}

	@Override
	public void initialize(IManagedForm form) {
		this.managedForm = form;
	}

	@Override
	public void dispose() {};

	@Override
	public boolean isDirty() {
		return isDirty;
	}

	@Override
	public boolean setFormInput(Object input) {
		return false;
	}

	@Override
	public void setFocus() {}

	@Override
	public boolean isStale() {
		return false;
	}

	@Override
	public void refresh() {
		updatingControls = true;
		updateValidators(getModelUUID());
		updateControls();
		updatingControls = false;
	}

	public void setDirty(boolean b) {
		this.isDirty = b;
		managedForm.dirtyStateChanged();
	}

	public Map<String, Object> getValuesOfControlsForKey(String key) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (mapTexts.containsKey(key)) {
			map.put(Constants.TEXT_VALUE, mapTexts.get(key).getText().trim());
			map.put(Constants.FULLY_QUALIFIED_NAME_VALUE, mapTexts.get(key).getData(FormRowCreator.ID_FULLY_QUALIFIED_NAME));
		}
		if (mapCheckBoxes.containsKey(key)) {
			map.put(Constants.BOOL_VALUE, mapCheckBoxes.get(key).getSelection());
		}
		if (mapCombos.containsKey(key)) {
			map.put(Constants.TEXT_VALUE, mapCombos.get(key).getText());
			map.put(Constants.FULLY_QUALIFIED_NAME_VALUE, mapCombos.get(key).getData(FormRowCreator.ID_FULLY_QUALIFIED_NAME));
		}
		return map;
	}

	protected void updateModel(String key) {
		try {
			doUpdateModel(key);
		} catch (Exception e) {
			ErrorDialog.openError(((IEditorPart)managedForm.getContainer()).getSite().getShell(),
					Messages.getString("error.problem.occurred"), e.getMessage(), new Status(//$NON-NLS-1$
							IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage()));
		}
		afterDoUpdateModel();
	}

	protected ModifyListener getDefaultModifyListener() {
		return new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent event) {
				if (!updatingControls) {
					String key = (String)event.widget.getData(FormRowCreator.ID_KEY);
					updateModel(key);
				}
			}
		};
	}

	protected VerifyListener getDefaultVerifyListener() {
		return new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {
				if (updatingControls) {
					return;
				}
				switch (e.keyCode) {
					case SWT.BS:
					case SWT.DEL:
					case SWT.HOME:
					case SWT.END:
					case SWT.ARROW_LEFT:
					case SWT.ARROW_RIGHT:
						return;
				}

				if (!Character.isDigit(e.character)) {
					e.doit = false; // disallow the action
				}
			}
		};
	}

	protected VerifyListener getDefaultDoubleVerifyListener() {
		return new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {
				if (updatingControls) {
					return;
				}
				switch (e.keyCode) {
					case SWT.BS:
					case SWT.DEL:
					case SWT.HOME:
					case SWT.END:
					case SWT.ARROW_LEFT:
					case SWT.ARROW_RIGHT:
						return;
				}

				if (!Character.isDigit(e.character)) {
					e.doit = false; // disallow the action
				}
				if ((((Text)e.widget).getText().length() >= 1) && !((Text)e.widget).getText().contains(".")
						&& (e.character == '.') && (e.end > 0) && !((Text)e.widget).getText().equals("-")) {
					e.doit = true;
				}
				if (!((Text)e.widget).getText().startsWith("-") && (e.character == '-') && (e.end == 0)) {
					e.doit = true;
				}
			}
		};
	}

	protected SelectionListener getDefaultSelectionListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent event) {
				if (!updatingControls) {
					String key = (String)event.widget.getData(FormRowCreator.ID_KEY);
					updateModel(key);
				}
			}
		};
	}

	protected KeyListener getDefaultComboBoxKeyListener() {
		return new KeyListener() {

			@Override
			public void keyReleased(KeyEvent e) {}

			@Override
			public void keyPressed(KeyEvent e) {
				switch (e.keyCode) {
					case SWT.BS:
					case SWT.DEL:
						setDefaultValue((CCombo)e.widget);
						return;
				}
			}

			private void setDefaultValue(CCombo combo) {
				combo.setText("");
				String key = (String)combo.getData(FormRowCreator.ID_KEY);
				updateModel(key);
			}
		};
	}

}
