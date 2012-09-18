package org.openlegacy.ide.eclipse.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.services.ScreenIdentifier;
import org.openlegacy.terminal.support.SimpleScreenIdentifier;

import java.util.List;
import java.util.Map;

public abstract class AbstractTablesComposite extends Composite {

	public final static String ITEM_DATA_KEY = "data_key";

	private Table fieldsTable;
	private Table identifiersTable;

	private int biggestColWidth;
	private int smallestColWidth = 40;

	private int compositeWidth;
	private int compositeHeight;

	protected Object field = null;

	private List<ScreenFieldDefinition> editableFields = null;

	public AbstractTablesComposite(Composite parent, int style, int width, int height) {
		super(parent, style);
		this.compositeWidth = width;
		this.compositeHeight = height;
		initialize();
	}

	protected abstract void handleFieldsTableSelectionEvent(SelectionEvent e);

	protected abstract void handleIdentifiersTableSelectionEvent(SelectionEvent e);

	protected abstract FocusListener getFocusListener();

	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		setLayout(gridLayout);

		int tableWidth = this.compositeWidth - gridLayout.marginWidth * 2;
		int tableHeight = (this.compositeHeight - (gridLayout.marginHeight * 2 + gridLayout.verticalSpacing)) / 2;

		this.biggestColWidth = tableWidth - this.smallestColWidth * 2 - 21;// 21 - for ScrollBar

		GridData gd = new GridData(GridData.FILL_HORIZONTAL, GridData.FILL_VERTICAL, true, true);
		gd.widthHint = this.compositeWidth;
		gd.heightHint = this.compositeHeight;
		setLayoutData(gd);

		// GridData for tables
		gd = new GridData(GridData.FILL_HORIZONTAL, GridData.FILL_VERTICAL, true, true);
		gd.widthHint = tableWidth;
		gd.heightHint = tableHeight;

		// create table of fields
		this.fieldsTable = new Table(this, SWT.SINGLE | SWT.BORDER);
		this.fieldsTable.setLinesVisible(true);
		this.fieldsTable.setHeaderVisible(true);
		this.fieldsTable.setLayoutData(gd);

		this.fieldsTable.addSelectionListener(this.getSelectionListener());
		this.fieldsTable.addSelectionListener(this.getTableEditor(this.fieldsTable));

		gd = new GridData(GridData.FILL_HORIZONTAL, GridData.FILL_VERTICAL, true, true);
		gd.widthHint = tableWidth;
		gd.heightHint = tableHeight;
		// create table of identifiers
		this.identifiersTable = new Table(this, SWT.SINGLE | SWT.BORDER);
		this.identifiersTable.setLinesVisible(true);
		this.identifiersTable.setHeaderVisible(true);
		this.identifiersTable.setLayoutData(gd);

		this.identifiersTable.addSelectionListener(this.getSelectionListener());
		this.identifiersTable.addFocusListener(this.getFocusListener());
	}

	public void cleanupFieldsDefinitions(Map<String, ScreenFieldDefinition> map) {
		for (ScreenFieldDefinition field : this.editableFields) {
			if (field.getName().length() > 0) {
				continue;
			}
			if (map.containsValue(field)) {
				map.values().remove(field);
			}
		}
	}

	public void fillTables(List<ScreenFieldDefinition> fields, List<ScreenIdentifier> identifiers) {
		this.editableFields = fields;
		this.fillColumnFieldsTableHeaders();
		this.fillColumnIdentifiersTableHeaders();

		this.fillFieldsTable(fields);
		this.fillIdentifiersTable(identifiers);
	}

	private void fillColumnFieldsTableHeaders() {
		this.fieldsTable.setRedraw(false);
		String[] titles = { Messages.label_col_field_name, Messages.label_col_row, Messages.label_col_column };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(this.fieldsTable, SWT.NONE);
			column.setText(titles[i]);
			column.setWidth(i == 0 ? this.biggestColWidth : this.smallestColWidth);
			column.setResizable(false);
		}
		this.fieldsTable.setRedraw(true);
	}

	private void fillColumnIdentifiersTableHeaders() {
		this.identifiersTable.setRedraw(false);
		String[] titles = { Messages.label_col_identifier, Messages.label_col_row, Messages.label_col_column };
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(this.identifiersTable, SWT.NONE);
			column.setText(titles[i]);
			column.setWidth(i == 0 ? this.biggestColWidth : this.smallestColWidth);
			column.setResizable(false);
		}
		this.identifiersTable.setRedraw(true);
	}

	private void fillFieldsTable(List<ScreenFieldDefinition> fields) {
		this.fieldsTable.setRedraw(false);
		for (ScreenFieldDefinition field : fields) {
			TableItem item = new TableItem(this.fieldsTable, SWT.NONE);
			item.setText(0, field.getName());
			item.setText(1, new Integer(field.getPosition().getRow()).toString());
			item.setText(2, new Integer(field.getPosition().getColumn()).toString());
			item.setData(ITEM_DATA_KEY, field);
		}
		this.fieldsTable.setRedraw(true);
	}

	private void fillIdentifiersTable(List<ScreenIdentifier> identifiers) {
		this.identifiersTable.setRedraw(false);
		for (ScreenIdentifier identifier : identifiers) {
			TableItem item = new TableItem(this.identifiersTable, SWT.NONE);
			SimpleScreenIdentifier ident = ((SimpleScreenIdentifier)identifier);
			item.setText(0, ident.getText());
			item.setText(1, new Integer(ident.getPosition().getRow()).toString());
			item.setText(2, new Integer(ident.getPosition().getColumn()).toString());
			item.setData(ITEM_DATA_KEY, identifier);
		}
		this.identifiersTable.setRedraw(true);
	}

	private SelectionListener getSelectionListener() {
		return new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				AbstractTablesComposite.this.handleSelectionEvent(e);
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				AbstractTablesComposite.this.handleSelectionEvent(e);
			}
		};
	}

	private void handleSelectionEvent(SelectionEvent e) {
		if (e.getSource().equals(AbstractTablesComposite.this.fieldsTable)) {
			AbstractTablesComposite.this.handleFieldsTableSelectionEvent(e);
		} else if (e.getSource().equals(AbstractTablesComposite.this.identifiersTable)) {
			AbstractTablesComposite.this.handleIdentifiersTableSelectionEvent(e);
		} else {
			return;
		}
	}

	private SelectionAdapter getTableEditor(final Table table) {
		final TableEditor editor = new TableEditor(table);
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// The editor must have the same size as the cell and must
				// not be any smaller than 50 pixels.
				editor.horizontalAlignment = SWT.LEFT;
				editor.grabHorizontal = true;
				editor.minimumWidth = 50;
				final int EDITABLECOLUMN = 0;
				// Identify the selected row
				TableItem item = (TableItem)e.item;
				if (item == null) {
					return;
				}
				// The control that will be the editor must be a child of the Table
				Text newEditor = new Text(table, SWT.NONE);
				newEditor.setText(item.getText(EDITABLECOLUMN));
				newEditor.addModifyListener(new ModifyListener() {

					public void modifyText(ModifyEvent me) {
						Text text = (Text)editor.getEditor();
						editor.getItem().setText(EDITABLECOLUMN, text.getText());
					}
				});
				newEditor.selectAll();
				newEditor.setFocus();
				newEditor.addFocusListener(new FocusListener() {

					public void focusLost(FocusEvent e) {
						ScreenFieldDefinition field = (ScreenFieldDefinition)editor.getItem().getData(ITEM_DATA_KEY);
						int index = AbstractTablesComposite.this.editableFields.indexOf(field);
						if (index > -1) {
							ScreenFieldDefinition editableField = AbstractTablesComposite.this.editableFields.get(index);
							((SimpleScreenFieldDefinition)editableField).setName(editor.getItem().getText());
						}

						// Clean up any previous editor control
						Control oldEditor = editor.getEditor();
						if (oldEditor != null) {
							oldEditor.dispose();
						}
						AbstractTablesComposite.this.field = null;
					}

					public void focusGained(FocusEvent arg0) {
						return;
					}
				});
				editor.setEditor(newEditor, item, EDITABLECOLUMN);
			}
		};
	}
}
