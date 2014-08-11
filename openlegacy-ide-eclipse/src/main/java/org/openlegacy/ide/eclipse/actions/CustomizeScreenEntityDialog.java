/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.ide.eclipse.actions;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.ui.util.BusyIndicatorRunnableContext;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.openlegacy.EntityType;
import org.openlegacy.designtime.terminal.model.ScreenEntityDesigntimeDefinition;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.PluginConstants;
import org.openlegacy.ide.eclipse.components.TablesComposite;
import org.openlegacy.ide.eclipse.components.screen.SnapshotComposite;
import org.openlegacy.ide.eclipse.dialogs.TypesSelectionDialog;
import org.openlegacy.ide.eclipse.dialogs.filters.ScreenTypeViewerFilter;
import org.openlegacy.ide.eclipse.util.PopupUtil;
import org.openlegacy.modules.login.Login.LoginEntity;
import org.openlegacy.modules.menu.Menu.MenuEntity;
import org.openlegacy.modules.messages.Messages.MessagesEntity;
import org.openlegacy.modules.table.LookupEntity;
import org.openlegacy.modules.table.RecordSelectionEntity;
import org.openlegacy.terminal.ScreenEntityType;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomizeScreenEntityDialog extends Dialog {

	// map of screen types
	private static Map<String, Class<? extends EntityType>> mapScreenTypes = Collections.unmodifiableMap(new HashMap<String, Class<? extends EntityType>>() {

		private static final long serialVersionUID = 1401084746820724548L;

		{
			put(ScreenEntityType.General.class.getSimpleName(), ScreenEntityType.General.class);
			put(LoginEntity.class.getSimpleName(), LoginEntity.class);
			put(LookupEntity.class.getSimpleName(), LookupEntity.class);
			put(MenuEntity.class.getSimpleName(), MenuEntity.class);
			put(MessagesEntity.class.getSimpleName(), MessagesEntity.class);
			put(RecordSelectionEntity.class.getSimpleName(), RecordSelectionEntity.class);
		}
	});

	private static final String ID_FULLY_QUALIFIED_NAME = "id.fullyQualifiedName";//$NON-NLS-1$

	private static final int DIALOG_WIDTH = 825;
	private static final int DIALOG_HEIGHT = 800;

	private Text entityNameTxt;
	private ScreenEntityDefinition screenEntityDefinition;
	private SnapshotComposite snapshotComposite;

	public CustomizeScreenEntityDialog(Shell parentShell, ScreenEntityDefinition screenEntityDefinition) {
		super(parentShell);
		this.screenEntityDefinition = screenEntityDefinition;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		parent = new Composite(parent, SWT.NONE);

		parent.getShell().setText(
				MessageFormat.format(Messages.getString("title_ol_generate_screens_api"), PluginConstants.TITLE));

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		GridData gd = new GridData();
		gd.widthHint = DIALOG_WIDTH;
		gd.heightHint = DIALOG_HEIGHT;
		parent.setLayoutData(gd);
		parent.setLayout(gridLayout);

		// entity level
		createEntityLevelControls(parent);

		// insert separator
		Label separator = new Label(parent, SWT.SEPARATOR | SWT.HORIZONTAL);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.verticalIndent = gd.horizontalIndent = 0;
		separator.setLayoutData(gd);

		// fields/identifiers level
		TablesComposite tablesComposite = new TablesComposite(parent, SWT.NONE, screenEntityDefinition);
		// image level
		snapshotComposite = new SnapshotComposite(parent, screenEntityDefinition.getOriginalSnapshot());
		snapshotComposite.setIsScalable(true);

		tablesComposite.setPaintedControl(snapshotComposite);

		return parent;
	}

	@Override
	protected void okPressed() {
		if (entityNameTxt.getText().length() == 0) {
			PopupUtil.error(Messages.getString("error_entity_name_not_specified"));
			return;
		}
		((ScreenEntityDesigntimeDefinition)screenEntityDefinition).setEntityName(entityNameTxt.getText());
		super.okPressed();
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		super.createButtonsForButtonBar(parent);
		// Set the size of the parent shell
		parent.getShell().setSize(DIALOG_WIDTH + 5, DIALOG_HEIGHT + 75);
		// Set the dialog position in the middle of the monitor
		setDialogLocation();
	}

	/**
	 * Method used to set the dialog in the centre of the monitor
	 */
	private void setDialogLocation() {
		Rectangle monitorArea = getShell().getDisplay().getPrimaryMonitor().getBounds();
		Rectangle shellArea = getShell().getBounds();
		int x = monitorArea.x + (monitorArea.width - shellArea.width) / 2;
		int y = monitorArea.y + (monitorArea.height - shellArea.height) / 2;
		getShell().setLocation(x, y);
	}

	private void createEntityLevelControls(Composite parent) {
		Label labelPackage = new Label(parent, SWT.NULL);
		labelPackage.setText(Messages.getString("field_entity_name"));
		entityNameTxt = new Text(parent, SWT.SINGLE | SWT.BORDER);

		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		entityNameTxt.setLayoutData(gd);
		entityNameTxt.setText(screenEntityDefinition.getEntityName());

		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.marginWidth = gl.marginHeight = 0;
		gl.numColumns = 6;
		composite.setLayout(gl);
		// Label for CCombo
		Label label = new Label(composite, SWT.NONE);
		label.setText(Messages.getString("label_screen_type"));
		// CCombo
		CCombo combo = new CCombo(composite, SWT.FLAT | SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
		gd = new GridData();
		gd.widthHint = 210;
		combo.setLayoutData(gd);
		combo.setItems(getComboItems());
		combo.setText(screenEntityDefinition.getTypeName());

		// Browse button
		Button browse = new Button(composite, SWT.PUSH);
		browse.setText(Messages.getString("btn_browse"));
		browse.addSelectionListener(getBrowseButtonSelectionListener(combo));

		Label emptylabel = new Label(composite, SWT.NONE);
		gd = new GridData();
		gd.widthHint = 50;
		emptylabel.setLayoutData(gd);
		emptylabel.setText(" ");
		// label for Checkbox
		Label checkboxLabel = new Label(composite, SWT.NONE);
		checkboxLabel.setText(Messages.getString("label_is_window"));
		// checkbox
		Button checkbox = new Button(composite, SWT.CHECK);
		checkbox.setSelection(screenEntityDefinition.isWindow());
	}

	private static String[] getComboItems() {
		Collection<Class<? extends EntityType>> collection = mapScreenTypes.values();
		List<String> list = new ArrayList<String>();
		for (Class<? extends EntityType> clazz : collection) {
			list.add(clazz.getSimpleName());
		}
		return list.toArray(new String[] {});
	}

	private static SelectionListener getBrowseButtonSelectionListener(final CCombo combo) {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IRunnableContext context = new BusyIndicatorRunnableContext();
				IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
				TypesSelectionDialog dialog = new TypesSelectionDialog(e.widget.getDisplay().getActiveShell(), false, context,
						scope, IJavaSearchConstants.CLASS);
				ScreenTypeViewerFilter filter = new ScreenTypeViewerFilter();
				dialog.addListFilter(filter);
				dialog.setTitle(Messages.getString("title_select_class_dialog"));//$NON-NLS-1$
				if (dialog.open() == Window.OK) {
					IType res = (IType)dialog.getResult()[0];
					combo.setData(ID_FULLY_QUALIFIED_NAME, res.getFullyQualifiedName());
					combo.setText(res.getElementName());
				}
			}

		};
	}

}
