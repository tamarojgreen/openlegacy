/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.ide.eclipse.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.openlegacy.ide.eclipse.Activator;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.PluginConstants;

import java.text.MessageFormat;

/**
 * This class represents a preference page that is contributed to the Preferences dialog. By subclassing
 * <samp>FieldEditorPreferencePage</samp>, we can use the field support built into JFace that allows us to create a page that is
 * small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the preference store that belongs to the main plug-in class.
 * That way, preferences can be accessed directly via the preference store.
 */

public class OpenLegacyPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	private StringFieldEditor urlEditor;

	public OpenLegacyPreferencePage() {
		super(GRID);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
		setDescription("OpenLegacy preferences");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common GUI blocks needed to manipulate various types of
	 * preferences. Each field editor knows how to save and restore itself.
	 */
	@Override
	public void createFieldEditors() {
		this.urlEditor = new StringFieldEditor(PreferenceConstants.P_TEMPLATES_URL, Messages.label_templates_url_preference,
				getFieldEditorParent());
		addField(this.urlEditor);
	}

	@Override
	protected void checkState() {
		super.checkState();
		if (this.urlEditor.getStringValue() == null || this.urlEditor.getStringValue().equals("")) {
			this.urlEditor.setStringValue(PluginConstants.TEMPLATES_URL);
		}
		String url = this.urlEditor.getStringValue();
		if (!url.startsWith("http")) {//$NON-NLS-1$
			url = MessageFormat.format("http://{0}", url);//$NON-NLS-1$
		}
		// remove last "/"
		while (url.lastIndexOf("/") == (url.length() - 1)) {
			url = url.substring(0, url.length() - 1);
		}
		this.urlEditor.setStringValue(url);
		setValid(true);
	}

	@Override
	public boolean performOk() {
		checkState();
		return super.performOk();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
	 */
	public void init(IWorkbench workbench) {}

}