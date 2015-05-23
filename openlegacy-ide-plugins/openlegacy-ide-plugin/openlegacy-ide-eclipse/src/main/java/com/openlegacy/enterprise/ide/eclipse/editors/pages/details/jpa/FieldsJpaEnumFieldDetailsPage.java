/*******************************************************************************
 * Copyright (c) 2015 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.enums.EnumEntryModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaEnumFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaFieldModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator.JAVA_DOCUMENTATION_TYPE;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.EnumFieldEntryTextCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.jpa.JpaEnumValuesTableContentProvider;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.openlegacy.designtime.generators.AnnotationConstants;

import java.net.MalformedURLException;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Aleksey Yeremeyev
 * 
 */
public class FieldsJpaEnumFieldDetailsPage extends AbstractJpaFieldDetailsPage {

	private JpaEnumFieldModel fieldModel;
	private TableViewer tableViewer;

	/**
	 * @param master
	 */
	public FieldsJpaEnumFieldDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	@Override
	protected JpaFieldModel getFieldModel() {
		return fieldModel;
	}

	@Override
	protected void addTopContent(FormToolkit toolkit, Composite client) {
		// create row for displaying java type name
		FormRowCreator.createLabelRow(toolkit, client, mapLabels, Messages.getString("jpa.field.java.type"), "",//$NON-NLS-1$ //$NON-NLS-2$
				Constants.JAVA_TYPE_NAME, JAVA_DOCUMENTATION_TYPE.JAVA_BASICS, "datatypes", "");//$NON-NLS-1$ //$NON-NLS-2$
	}

	@Override
	protected void addBottomContent(FormToolkit toolkit, Composite client) {
		// create row for selecting Enum type
		Text enumControl = FormRowCreator.createStringRowWithBrowseButton(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("EnumField.enum"), "", Constants.JAVA_TYPE, null, IJavaSearchConstants.ENUM, false,//$NON-NLS-1$ //$NON-NLS-2$
				null, JAVA_DOCUMENTATION_TYPE.JAVA_BASICS, "datatypes", "");//$NON-NLS-1$ //$NON-NLS-2$

		enumControl.addModifyListener(getEnumModifyListener());

		createEnumValuesSection(toolkit, client);
	}

	@Override
	public Class<?> getDetailsModel() {
		return JpaEnumFieldModel.class;
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
		ControlsUpdater.updateJpaEnumFieldDetailsControls(fieldModel, mapTexts, tableViewer);
		revalidate();
	}

	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateJpaFieldModel(getEntity(), fieldModel, key, (String) map.get(Constants.TEXT_VALUE),
				(Boolean) map.get(Constants.BOOL_VALUE));
		ModelUpdater.updateJpaEnumFieldModel(getEntity(), fieldModel, key, (String) map.get(Constants.TEXT_VALUE),
				(String) map.get(Constants.FULLY_QUALIFIED_NAME_VALUE));

	}

	@Override
	protected void afterDoUpdateModel() {
		setDirty(getEntity().isDirty());
	}

	@Override
	protected void selectionChanged(IStructuredSelection selection) {
		if (selection.size() == 1) {
			fieldModel = (JpaEnumFieldModel) selection.getFirstElement();
		} else {
			fieldModel = null;
		}
	}

	private ModifyListener getEnumModifyListener() {
		// try to remove action that responsible for creating a new enum class
		return new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (!updatingControls) {
					getEntity().removeAction(fieldModel.getUUID(), Constants.ENUM_FIELD_NEW_TYPE_DECLARATION);
				}
			}
		};
	}

	private void createEnumValuesSection(FormToolkit toolkit, Composite parent) {
		Section section = toolkit.createSection(parent, ExpandableComposite.TWISTIE | Section.DESCRIPTION);
		section.setActiveToggleColor(toolkit.getHyperlinkGroup().getActiveForeground());
		section.setToggleColor(toolkit.getColors().getColor(IFormColors.SEPARATOR));
		toolkit.createCompositeSeparator(section);
		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;

		client.setLayout(layout);

		// create table
		Table t = createTable(toolkit, client);
		tableViewer = new TableViewer(t);
		tableViewer.setContentProvider(new JpaEnumValuesTableContentProvider());
		tableViewer.setInput(fieldModel);
		tableViewer.setData(FormRowCreator.ID_KEY, Constants.ENUM_FIELD_ENTRIES);

		createColumns(toolkit, tableViewer);
		// create buttons
		Composite panel = toolkit.createComposite(client);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		gl.marginWidth = 2;
		gl.marginHeight = 0;
		panel.setLayout(gl);
		GridData gd = new GridData(GridData.FILL_VERTICAL);
		gd.widthHint = 100;
		panel.setLayoutData(gd);

		Button b = toolkit.createButton(panel, Messages.getString("Button.add"), SWT.PUSH); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		b.setLayoutData(gd);
		b.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				EnumEntryModel enumEntryModel = new EnumEntryModel(fieldModel);
				enumEntryModel.setName(MessageFormat.format("{0}{1}", fieldModel.getFieldName(), getSeedForNewEnumEntry()));
				fieldModel.getEntries().add(enumEntryModel);
				tableViewer.setInput(fieldModel);
				updateModel(Constants.ENUM_FIELD_ENTRIES);
				tableViewer.getTable().select(tableViewer.getTable().getItemCount() - 1);
			}

		});
		b = toolkit.createButton(panel, Messages.getString("Button.remove"), SWT.PUSH); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING);
		b.setLayoutData(gd);
		b.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
				if (selection.size() == 1) {
					EnumEntryModel model = (EnumEntryModel) selection.getFirstElement();
					fieldModel.getEntries().remove(model);
					tableViewer.setInput(fieldModel);
					updateModel(Constants.ENUM_FIELD_ENTRIES);
					tableViewer.getTable().select(tableViewer.getTable().getItemCount() - 1);
				}
			}

		});

		section.setText(Messages.getString("EnumField.properties"));//$NON-NLS-1$
		section.setClient(client);
		section.setExpanded(true);
		gd = new GridData(GridData.FILL_BOTH);
		gd.horizontalSpan = 2;
		section.setLayoutData(gd);
	}

	private static Table createTable(FormToolkit toolkit, Composite parent) {
		Table t = toolkit.createTable(parent, SWT.FULL_SELECTION);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.heightHint = 200;
		gd.widthHint = 100;

		t.setLayoutData(gd);
		t.setHeaderVisible(true);
		t.setLinesVisible(true);

		toolkit.paintBordersFor(parent);
		return t;
	}

	private void createColumns(FormToolkit toolkit, final TableViewer viewer) {
		TableViewerColumn vcol = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tcol = vcol.getColumn();
		tcol.setResizable(false);
		tcol.setWidth(150);
		tcol.setText(Messages.getString("EnumField.col.name"));//$NON-NLS-1$

		vcol.setEditingSupport(new EnumFieldEntryTextCellEditingSupport(viewer, AnnotationConstants.NAME));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				EnumEntryModel model = (EnumEntryModel) cell.getElement();
				cell.setText(model.getName());
				if (!updatingControls) {
					updateModel((String) viewer.getData(FormRowCreator.ID_KEY));
				}
			}
		});

		vcol = new TableViewerColumn(viewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setResizable(false);
		tcol.setWidth(50);
		tcol.setText(Messages.getString("EnumField.col.value"));//$NON-NLS-1$

		vcol.setEditingSupport(new EnumFieldEntryTextCellEditingSupport(viewer, AnnotationConstants.VALUE));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				EnumEntryModel model = (EnumEntryModel) cell.getElement();
				cell.setText(model.getValue());
			}
		});

		vcol = new TableViewerColumn(viewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setResizable(false);
		tcol.setWidth(200);
		tcol.setText(Messages.getString("EnumField.col.display"));//$NON-NLS-1$

		vcol.setEditingSupport(new EnumFieldEntryTextCellEditingSupport(viewer, AnnotationConstants.DISPLAY_NAME));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				EnumEntryModel model = (EnumEntryModel) cell.getElement();
				cell.setText(model.getDisplayName());
			}
		});
	}

	private int getSeedForNewEnumEntry() {
		int seed = 1;
		List<EnumEntryModel> entries = fieldModel.getEntries();
		if (entries.isEmpty()) {
			return seed;
		}
		Pattern p = Pattern.compile(fieldModel.getFieldName() + "(\\d+)$");
		for (EnumEntryModel enumEntryModel : entries) {
			Matcher matcher = p.matcher(enumEntryModel.getName());
			if (matcher.find()) {
				String stringSeed = matcher.group(1);
				if (Integer.parseInt(stringSeed) > seed) {
					seed = Integer.parseInt(stringSeed);
				}
			}
		}
		return ++seed;
	}

}
