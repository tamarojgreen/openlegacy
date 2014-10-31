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

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
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
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
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
import org.openlegacy.ide.eclipse.util.Utils;
import org.openlegacy.modules.login.Login.LoginEntity;
import org.openlegacy.modules.menu.Menu.MenuEntity;
import org.openlegacy.modules.messages.Messages.MessagesEntity;
import org.openlegacy.modules.table.LookupEntity;
import org.openlegacy.modules.table.RecordSelectionEntity;
import org.openlegacy.terminal.ScreenEntityType;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenEntityDefinition;

import java.io.File;
import java.net.MalformedURLException;
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
	private static final int DIALOG_HEIGHT = 660;

	private Text entityNameTxt;
	private ScreenEntityDefinition screenEntityDefinition;
	private SnapshotComposite snapshotComposite;

	private File projectPath;

	public CustomizeScreenEntityDialog(Shell parentShell, ScreenEntityDefinition screenEntityDefinition, File projectPath) {
		super(parentShell);
		this.screenEntityDefinition = screenEntityDefinition;
		this.projectPath = projectPath;
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

		// entity panel
		createEntityLevelControls(parent);

		// fields/identifiers level
		TablesComposite tablesComposite = new TablesComposite(parent, SWT.NONE, screenEntityDefinition);
		// image level
		snapshotComposite = new SnapshotComposite(parent, screenEntityDefinition.getOriginalSnapshot(), projectPath);
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
		Composite panelComposite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout(6, true);
		gl.marginBottom = gl.marginHeight = gl.marginLeft = gl.marginRight = gl.marginTop = gl.marginWidth = 0;
		panelComposite.setLayout(gl);
		GridData gd = new GridData(SWT.BEGINNING, SWT.TOP, true, false);
		panelComposite.setLayoutData(gd);

		// the first column
		Composite firstColComposite = new Composite(panelComposite, SWT.NONE);
		RowLayout rl = new RowLayout(SWT.HORIZONTAL);
		rl.center = true;
		rl.marginBottom = rl.marginHeight = rl.marginLeft = rl.marginRight = rl.marginWidth = 0;
		firstColComposite.setLayout(rl);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.horizontalSpan = 2;
		firstColComposite.setLayoutData(gd);

		Label entityNameLabel = new Label(firstColComposite, SWT.NONE);
		entityNameLabel.setText(Messages.getString("field_entity_name"));

		entityNameTxt = new Text(firstColComposite, SWT.SINGLE | SWT.BORDER);
		RowData rd = new RowData();
		rd.width = 150;
		entityNameTxt.setLayoutData(rd);
		entityNameTxt.setText(screenEntityDefinition.getEntityName());

		// the second column
		Composite secondColComposite = new Composite(panelComposite, SWT.NONE);
		rl = new RowLayout(SWT.HORIZONTAL);
		rl.center = true;
		rl.marginBottom = rl.marginHeight = rl.marginLeft = rl.marginRight = rl.marginTop = rl.marginWidth = 0;
		secondColComposite.setLayout(rl);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		gd.horizontalSpan = 3;
		secondColComposite.setLayoutData(gd);

		Label screenTypeLabel = new Label(secondColComposite, SWT.NONE);
		screenTypeLabel.setText(Messages.getString("label_screen_type"));

		CCombo screenTypeCombo = new CCombo(secondColComposite, SWT.FLAT | SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
		rd = new RowData();
		rd.width = 210;
		screenTypeCombo.setLayoutData(rd);
		screenTypeCombo.setItems(getComboItems());
		screenTypeCombo.setText(screenEntityDefinition.getTypeName());
		screenTypeCombo.addModifyListener(new ModifyListener() {

			@SuppressWarnings("unchecked")
			public void modifyText(ModifyEvent e) {
				if (!(screenEntityDefinition instanceof SimpleScreenEntityDefinition)) {
					return;
				}
				String text = ((CCombo)e.widget).getText();
				String fullyQualifiedName = (String)e.widget.getData(ID_FULLY_QUALIFIED_NAME);
				Class<? extends EntityType> screenType = null;
				if (mapScreenTypes.get(text) != null) {
					screenType = mapScreenTypes.get(text);
				} else if (!StringUtils.isEmpty(fullyQualifiedName)) {
					try {
						screenType = (Class<? extends EntityType>)Utils.getClazz(fullyQualifiedName);
					} catch (MalformedURLException e1) {
					} catch (CoreException e1) {
					}
				}

				if (screenType == null) {
					screenType = ScreenEntityType.General.class;
				}

				((SimpleScreenEntityDefinition)screenEntityDefinition).setType(screenType);
			}
		});

		Button browse = new Button(secondColComposite, SWT.PUSH);
		browse.setText(Messages.getString("btn_browse"));
		browse.addSelectionListener(getBrowseButtonSelectionListener(screenTypeCombo));

		// the third column
		Composite thirdColComposite = new Composite(panelComposite, SWT.NONE);
		rl = new RowLayout(SWT.HORIZONTAL);
		rl.center = true;
		rl.marginBottom = rl.marginHeight = rl.marginLeft = rl.marginRight = rl.marginWidth = 0;
		thirdColComposite.setLayout(rl);
		gd = new GridData(SWT.FILL, SWT.FILL, true, false);
		thirdColComposite.setLayoutData(gd);

		Label checkboxLabel = new Label(thirdColComposite, SWT.NONE);
		checkboxLabel.setText(Messages.getString("label_is_window"));

		Button checkbox = new Button(thirdColComposite, SWT.CHECK);
		checkbox.setSelection(screenEntityDefinition.isWindow());
		checkbox.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				boolean selection = ((Button)e.widget).getSelection();
				if (screenEntityDefinition instanceof SimpleScreenEntityDefinition) {
					((SimpleScreenEntityDefinition)screenEntityDefinition).setWindow(selection);
				}
			}

		});
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
