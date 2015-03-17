package com.openlegacy.enterprise.ide.eclipse.editors.pages.details.jpa;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.jpa.JpaTableModel;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractMasterBlock;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.FormRowCreator.JAVA_DOCUMENTATION_TYPE;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.ControlsUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.ModelUpdater;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers.jpa.UniqueConstraintsCellEditingSupport;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.jpa.UniqueConstraintsTableContentProvider;

import org.apache.commons.lang.StringUtils;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.openlegacy.db.definitions.DbTableDefinition.UniqueConstraintDefinition;
import org.openlegacy.db.definitions.SimpleDbTableUniqueConstraintDefinition;
import org.openlegacy.designtime.db.generators.support.DbAnnotationConstants;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.UUID;

/**
 * @author Ivan Bort
 * 
 */
public class GeneralJpaTableDetailsPage extends AbstractJpaDetailsPage {

	private JpaTableModel model;
	private TableViewer tableViewer;

	public GeneralJpaTableDetailsPage(AbstractMasterBlock master) {
		super(master);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#getDetailsModel()
	 */
	@Override
	public Class<?> getDetailsModel() {
		return JpaTableModel.class;
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
		section.setText(Messages.getString("jpa.general.page.details.name")); //$NON-NLS-1$
		section.setDescription(Messages.getString("jpa.general.page.details.desc")); //$NON-NLS-1$
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
		// create row for "name"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.table.name"), "", DbAnnotationConstants.NAME, JAVA_DOCUMENTATION_TYPE.JPA, "Table",//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				DbAnnotationConstants.NAME);
		// create row for "catalog"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.table.catalog"), "", DbAnnotationConstants.CATALOG, JAVA_DOCUMENTATION_TYPE.JPA, "Table",//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				DbAnnotationConstants.CATALOG);
		// create row for "name"
		FormRowCreator.createStringRow(toolkit, client, mapTexts, getDefaultModifyListener(),
				Messages.getString("jpa.table.schema"), "", DbAnnotationConstants.SCHEMA, JAVA_DOCUMENTATION_TYPE.JPA, "Table",//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				DbAnnotationConstants.SCHEMA);
		// create section for "uniqueConstraints"

		createUniqueConstraintsSection(managedForm.getForm(), toolkit, topClient, DbAnnotationConstants.UNIQUE_CONSTRAINTS);

		toolkit.paintBordersFor(section);
		section.setClient(topClient);
	}

	@Override
	public void revalidate() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#getModelUUID()
	 */
	@Override
	public UUID getModelUUID() {
		return model != null ? model.getUUID() : null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#updateControls()
	 */
	@Override
	protected void updateControls() {
		ControlsUpdater.updateJpaTableDetailsControls(model, mapTexts, tableViewer);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractDetailsPage#doUpdateModel(java.lang.String)
	 */
	@Override
	protected void doUpdateModel(String key) throws MalformedURLException, CoreException {
		Map<String, Object> map = getValuesOfControlsForKey(key);
		ModelUpdater.updateJpaTableModel(getEntity(), model, key, (String)map.get(Constants.TEXT_VALUE));
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
			model = (JpaTableModel)selection.getFirstElement();
		} else {
			model = null;
		}
	}

	private void createUniqueConstraintsSection(final ScrolledForm form, FormToolkit toolkit, Composite parent, final String key) {
		Section section = toolkit.createSection(parent, ExpandableComposite.TWISTIE | Section.DESCRIPTION);
		section.setActiveToggleColor(toolkit.getHyperlinkGroup().getActiveForeground());
		section.setToggleColor(toolkit.getColors().getColor(IFormColors.SEPARATOR));
		toolkit.createCompositeSeparator(section);
		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;

		client.setLayout(layout);

		Table t = createTable(toolkit, client);
		tableViewer = createTableViewer(toolkit, t, key);
		createColumns(toolkit, tableViewer);

		Button b = toolkit.createButton(client, Messages.getString("Button.add"), SWT.PUSH); //$NON-NLS-1$
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		b.setLayoutData(gd);
		b.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				model.getConstraints().add(new SimpleDbTableUniqueConstraintDefinition());
				updatingControls = true;
				ControlsUpdater.updateJpaTableDetailsControls(model, mapTexts, tableViewer);
				updatingControls = false;
				updateModel(key);
				tableViewer.getTable().select(tableViewer.getTable().getItemCount() - 1);
			}

		});

		section.setText(Messages.getString("jpa.table.unique.constraints"));//$NON-NLS-1$
		section.setClient(client);
		section.setExpanded(true);
		section.addExpansionListener(new ExpansionAdapter() {

			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(false);
			}
		});
		gd = new GridData(GridData.FILL_BOTH);
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

	private TableViewer createTableViewer(FormToolkit toolkit, Table t, String key) {
		TableViewer v = new TableViewer(t);
		v.setContentProvider(new UniqueConstraintsTableContentProvider());
		v.setInput(model);
		v.setData(FormRowCreator.ID_KEY, key);
		return v;
	}

	private void createColumns(FormToolkit toolkit, final TableViewer viewer) {
		TableViewerColumn vcol = new TableViewerColumn(viewer, SWT.NONE);
		TableColumn tcol = vcol.getColumn();
		tcol.setResizable(false);
		tcol.setWidth(300);
		tcol.setText(Messages.getString("jpa.table.unique.constraints.column.names"));//$NON-NLS-1$

		vcol.setEditingSupport(new UniqueConstraintsCellEditingSupport(viewer, DbAnnotationConstants.COLUMN_NAMES));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				UniqueConstraintDefinition definition = (UniqueConstraintDefinition)cell.getElement();
				cell.setText(StringUtils.join(definition.getColumnNames(), ","));
				if (!updatingControls) {
					updateModel((String)viewer.getData(FormRowCreator.ID_KEY));
				}
			}
		});

		vcol = new TableViewerColumn(viewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setResizable(false);
		tcol.setWidth(150);
		tcol.setText(Messages.getString("jpa.table.unique.constraints.name"));//$NON-NLS-1$

		vcol.setEditingSupport(new UniqueConstraintsCellEditingSupport(viewer, DbAnnotationConstants.NAME));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				UniqueConstraintDefinition definition = (UniqueConstraintDefinition)cell.getElement();
				cell.setText(definition.getName());
			}
		});
	}

}
