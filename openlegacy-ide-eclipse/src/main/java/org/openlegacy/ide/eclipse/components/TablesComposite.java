package org.openlegacy.ide.eclipse.components;

import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.openlegacy.ide.eclipse.Activator;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.components.providers.FieldsTableContentProvider;
import org.openlegacy.ide.eclipse.components.providers.IdentifiersTableContentProvider;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.support.SimpleScreenIdentifier;

/**
 * @author Ivan Bort
 * 
 */
public class TablesComposite extends Composite {

	private static final int TABLE_WIDTH = 400;
	private static final int TABLE_HEIGHT = 210;

	private ScreenEntityDefinition screenEntityDefinition;

	private TableViewer fieldsTableViewer;
	private TableViewer identifiersTableViewer;

	public TablesComposite(Composite parent, int style, ScreenEntityDefinition screenEntityDefinition) {
		super(parent, style);
		this.screenEntityDefinition = screenEntityDefinition;
		createLayout();
	}

	private void createLayout() {
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.marginTop = 0;
		gl.makeColumnsEqualWidth = true;
		setLayout(gl);

		GridData gd = new GridData(SWT.FILL | SWT.BORDER);
		gd.grabExcessHorizontalSpace = true;
		gd.verticalIndent = 0;
		setLayoutData(gd);

		createFieldsColumn(this);
		createIdentifiersColumn(this);
	}

	private void createFieldsColumn(Composite parent) {
		Composite composite = getColumnComposite(parent);
		createButtonsPanelForFieldsTable(composite);

		Table table = new Table(composite, SWT.FULL_SELECTION);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.heightHint = TABLE_HEIGHT;
		gd.widthHint = TABLE_WIDTH;
		table.setLayoutData(gd);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		fieldsTableViewer = new TableViewer(table);
		createFieldsTableColumns(fieldsTableViewer);
		fieldsTableViewer.setContentProvider(new FieldsTableContentProvider());
		fieldsTableViewer.setInput(screenEntityDefinition);
	}

	private void createButtonsPanelForFieldsTable(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		RowLayout rl = new RowLayout(SWT.HORIZONTAL);
		rl.marginTop = 0;
		composite.setLayout(rl);
		GridData gd = new GridData(SWT.FILL);
		gd.horizontalAlignment = GridData.END;
		gd.verticalIndent = 0;
		composite.setLayoutData(gd);

		Button btnString = new Button(composite, SWT.PUSH);
		btnString.setImage(Activator.getDefault().getImage(Activator.ICON_STRING));
		btnString.setToolTipText(Messages.getString("btn_tooltip_add_string"));

		Button btnInteger = new Button(composite, SWT.PUSH);
		btnInteger.setImage(Activator.getDefault().getImage(Activator.ICON_INTEGER));
		btnInteger.setToolTipText(Messages.getString("btn_tooltip_add_integer"));

		Button btnBoolean = new Button(composite, SWT.PUSH);
		btnBoolean.setImage(Activator.getDefault().getImage(Activator.ICON_BOOLEAN));
		btnBoolean.setToolTipText(Messages.getString("btn_tooltip_add_boolean"));

		Button btnDate = new Button(composite, SWT.PUSH);
		btnDate.setImage(Activator.getDefault().getImage(Activator.ICON_DATE));
		btnDate.setToolTipText(Messages.getString("btn_tooltip_add_date"));

		Button btnEnum = new Button(composite, SWT.PUSH);
		btnEnum.setImage(Activator.getDefault().getImage(Activator.ICON_ENUM));
		btnEnum.setToolTipText(Messages.getString("btn_tooltip_add_enum"));

	}

	private void createIdentifiersColumn(Composite parent) {
		Composite composite = getColumnComposite(parent);
		createButtonsPanelForIdentifiersTable(composite);

		Table table = new Table(composite, SWT.FULL_SELECTION);
		GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
		gd.heightHint = TABLE_HEIGHT;
		gd.widthHint = TABLE_WIDTH;
		table.setLayoutData(gd);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		identifiersTableViewer = new TableViewer(table);
		createIdentifiersTableColumns(identifiersTableViewer);
		identifiersTableViewer.setContentProvider(new IdentifiersTableContentProvider());
		identifiersTableViewer.setInput(screenEntityDefinition);
	}

	private void createButtonsPanelForIdentifiersTable(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		RowLayout rl = new RowLayout(SWT.HORIZONTAL);
		rl.marginTop = 0;
		composite.setLayout(rl);
		GridData gd = new GridData(SWT.FILL);
		gd.horizontalAlignment = GridData.END;
		gd.verticalIndent = 0;
		composite.setLayoutData(gd);

		Button btnIdentifier = new Button(composite, SWT.PUSH);
		btnIdentifier.setImage(Activator.getDefault().getImage(Activator.ICON_ANNOTATION));
		btnIdentifier.setToolTipText(Messages.getString("btn_tooltip_add_identifier"));
	}

	private static Composite getColumnComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		gl.marginTop = 0;
		gl.marginLeft = 0;
		gl.marginRight = 0;
		composite.setLayout(gl);
		GridData gd = new GridData(SWT.FILL);
		gd.grabExcessHorizontalSpace = true;
		gd.verticalIndent = 0;
		composite.setLayoutData(gd);
		return composite;
	}

	private void createFieldsTableColumns(TableViewer tableViewer) {
		// "Fields"
		TableViewerColumn vcol = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tcol = vcol.getColumn();
		tcol.setText(Messages.getString("label_col_fields"));
		tcol.setResizable(false);
		tcol.setWidth(115);
		// vcol.setEditingSupport(null);
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ScreenFieldDefinition definition = (ScreenFieldDefinition)cell.getElement();
				cell.setText(definition.getName());
			}
		});
		// "Row"
		vcol = new TableViewerColumn(tableViewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("label_col_row"));
		tcol.setResizable(false);
		tcol.setWidth(40);
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ScreenFieldDefinition definition = (ScreenFieldDefinition)cell.getElement();
				cell.setText(String.valueOf(definition.getPosition().getRow()));
			}
		});
		// "Column"
		vcol = new TableViewerColumn(tableViewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("label_col_column"));
		tcol.setResizable(false);
		tcol.setWidth(40);
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ScreenFieldDefinition definition = (ScreenFieldDefinition)cell.getElement();
				cell.setText(String.valueOf(definition.getPosition().getColumn()));
			}
		});
		// "Java Type"
		vcol = new TableViewerColumn(tableViewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("label_col_java_type"));
		tcol.setResizable(false);
		tcol.setWidth(70);
		// vcol.setEditingSupport(null);
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ScreenFieldDefinition definition = (ScreenFieldDefinition)cell.getElement();
				cell.setText(definition.getJavaType().getSimpleName());
			}
		});
		// "Field Type"
		vcol = new TableViewerColumn(tableViewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("label_col_field_type"));
		tcol.setResizable(false);
		tcol.setWidth(70);
		// vcol.setEditingSupport(null);
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ScreenFieldDefinition definition = (ScreenFieldDefinition)cell.getElement();
				cell.setText(definition.getJavaType().getSimpleName());
			}
		});
		// Delete button
		vcol = new TableViewerColumn(tableViewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setText("");
		tcol.setResizable(false);
		tcol.setWidth(35);
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				TableItem item = (TableItem)cell.getItem();

				Button btn = new Button(item.getParent(), SWT.NONE);
				btn.setSize(35, item.getBounds().height);
				btn.setImage(Activator.getDefault().getImage(Activator.ICON_DELETE));
				btn.pack();

				TableEditor editor = new TableEditor(item.getParent());
				editor.minimumWidth = btn.getSize().x;
				editor.horizontalAlignment = SWT.CENTER;
				editor.setEditor(btn, item, 5);
			}
		});
	}

	private void createIdentifiersTableColumns(TableViewer tableViewer) {
		// "Fields"
		TableViewerColumn vcol = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tcol = vcol.getColumn();
		tcol.setText(Messages.getString("label_col_identifiers"));
		tcol.setResizable(false);
		tcol.setWidth(200);
		// vcol.setEditingSupport(null);
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				SimpleScreenIdentifier identifier = (SimpleScreenIdentifier)cell.getElement();
				cell.setText(identifier.getText());
			}
		});
		// "Row"
		vcol = new TableViewerColumn(tableViewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("label_col_row"));
		tcol.setResizable(false);
		tcol.setWidth(60);
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				SimpleScreenIdentifier identifier = (SimpleScreenIdentifier)cell.getElement();
				cell.setText(String.valueOf(identifier.getPosition().getRow()));
			}
		});
		// "Column"
		vcol = new TableViewerColumn(tableViewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setText(Messages.getString("label_col_column"));
		tcol.setResizable(false);
		tcol.setWidth(60);
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				SimpleScreenIdentifier identifier = (SimpleScreenIdentifier)cell.getElement();
				cell.setText(String.valueOf(identifier.getPosition().getColumn()));
			}
		});
		// Delete button
		vcol = new TableViewerColumn(tableViewer, SWT.NONE);
		tcol = vcol.getColumn();
		tcol.setText("");
		tcol.setResizable(false);
		tcol.setWidth(35);
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				TableItem item = (TableItem)cell.getItem();

				Button btn = new Button(item.getParent(), SWT.NONE);
				btn.setSize(35, item.getBounds().height);
				btn.setImage(Activator.getDefault().getImage(Activator.ICON_DELETE));
				btn.pack();

				TableEditor editor = new TableEditor(item.getParent());
				editor.minimumWidth = btn.getSize().x;
				editor.horizontalAlignment = SWT.CENTER;
				editor.setEditor(btn, item, 3);
			}
		});
	}

}
