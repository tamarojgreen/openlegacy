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
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
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

	protected final static String ITEM_DATA_KEY = "data_key";
	private final static String DELETE_BUTTON_ICON_FILENAME = "delete.png";

	private Table fieldsTable;
	private Table identifiersTable;

	private int biggestColWidth;
	private int smallestColWidth = 40;
	private int colCount = 4;
	private int scrollBarWidth = 21;

	private int compositeWidth;
	private int compositeHeight;

	protected Object field = null;

	private List<ScreenFieldDefinition> fieldsDefinitions = null;

	protected List<ScreenFieldDefinition> removedFieldsDefinitions = null;
	protected List<ScreenIdentifier> removedScreenIdentifiers = null;

	public AbstractTablesComposite(Composite parent, int style, int width, int height) {
		super(parent, style);
		this.compositeWidth = width;
		this.compositeHeight = height;
		initialize();
	}

	protected abstract void handleFieldsTableSelectionEvent(SelectionEvent e);

	protected abstract void handleIdentifiersTableSelectionEvent(SelectionEvent e);

	protected abstract void handleFieldsTableDeleteButtonSelectionEvent(TableItem item, Table table);

	protected abstract void handleIdentifiersTableDeleteButtonSelectionEvent(TableItem item, Table table);

	protected abstract FocusListener getFocusListener();

	private void initialize() {
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		setLayout(gridLayout);

		int tableWidth = this.compositeWidth - gridLayout.marginWidth * 2;
		int tableHeight = (this.compositeHeight - (gridLayout.marginHeight * 2 + gridLayout.verticalSpacing)) / 2;

		this.biggestColWidth = tableWidth - this.smallestColWidth * (this.colCount - 1) - this.scrollBarWidth;

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

	public void cleanupScreenentityDefinition(Map<String, ScreenFieldDefinition> map, List<ScreenIdentifier> list) {
		for (ScreenFieldDefinition field : this.removedFieldsDefinitions) {
			if (map.containsValue(field)) {
				map.values().remove(field);
			}
		}
		list.removeAll(this.removedScreenIdentifiers);
	}

	public void fillTables(List<ScreenFieldDefinition> fields, List<ScreenIdentifier> identifiers) {
		this.fieldsDefinitions = fields;

		this.fillColumnFieldsTableHeaders();
		this.fillColumnIdentifiersTableHeaders();

		this.fillFieldsTable(fields);
		this.fillIdentifiersTable(identifiers);
	}

	private void fillColumnFieldsTableHeaders() {
		this.fieldsTable.setRedraw(false);
		String[] titles = { Messages.label_col_fields, Messages.label_col_row, Messages.label_col_column };
		for (int i = 0; i <= titles.length; i++) {
			TableColumn column = new TableColumn(this.fieldsTable, SWT.NONE);
			column.setText(i != titles.length ? titles[i] : "");
			column.setWidth(i == 0 ? this.biggestColWidth : this.smallestColWidth);
			column.setResizable(false);
		}
		this.fieldsTable.setRedraw(true);
	}

	private void fillColumnIdentifiersTableHeaders() {
		this.identifiersTable.setRedraw(false);
		String[] titles = { Messages.label_col_identifiers, Messages.label_col_row, Messages.label_col_column };
		for (int i = 0; i <= titles.length; i++) {
			TableColumn column = new TableColumn(this.identifiersTable, SWT.NONE);
			column.setText(i != titles.length ? titles[i] : "");
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

			this.addDeleteBtnToTable(this.fieldsTable, item, 3);
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

			this.addDeleteBtnToTable(this.identifiersTable, item, 3);
		}
		this.identifiersTable.setRedraw(true);
	}

	private void addDeleteBtnToTable(Table table, TableItem item, int colIndex) {
		TableEditor editor = new TableEditor(table);
		Button deleteBtn = new Button(table, SWT.NONE);
		deleteBtn.setSize(table.getItemHeight(), this.smallestColWidth);
		Image image = new Image(getShell().getDisplay(), this.getClass().getClassLoader().getResourceAsStream(
				DELETE_BUTTON_ICON_FILENAME));
		deleteBtn.setImage(image);
		deleteBtn.setData(ITEM_DATA_KEY, item);
		deleteBtn.pack();
		deleteBtn.addSelectionListener(this.getDeleteButtonSelectionListener(colIndex));
		editor.minimumWidth = deleteBtn.getSize().x;
		editor.horizontalAlignment = SWT.CENTER;
		editor.setEditor(deleteBtn, item, colIndex);
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
						// validate
						if (text.getText().matches("^[a-zA-Z]{1}\\w*")) {
							editor.getItem().setText(EDITABLECOLUMN, text.getText());
						} else {
							int caretPos = text.getCaretPosition() - 1;
							text.setText(editor.getItem().getText(EDITABLECOLUMN));
							text.setSelection(caretPos, caretPos);
						}
					}
				});
				newEditor.selectAll();
				newEditor.setFocus();
				newEditor.addFocusListener(new FocusListener() {

					public void focusLost(FocusEvent e) {
						ScreenFieldDefinition field = (ScreenFieldDefinition)editor.getItem().getData(ITEM_DATA_KEY);
						int index = AbstractTablesComposite.this.fieldsDefinitions.indexOf(field);
						if (index > -1) {
							ScreenFieldDefinition editableField = AbstractTablesComposite.this.fieldsDefinitions.get(index);
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

	private SelectionListener getDeleteButtonSelectionListener(final int colIndex) {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				// Identify the selected row
				TableItem item = (TableItem)e.widget.getData(ITEM_DATA_KEY);
				if (item == null) {
					return;
				}

				if (!(((Button)e.widget).getParent() instanceof Table)) {
					return;
				}
				Table table = (Table)((Button)e.widget).getParent();

				if (table.equals(AbstractTablesComposite.this.fieldsTable)) {
					AbstractTablesComposite.this.handleFieldsTableDeleteButtonSelectionEvent(item, table);
				} else if (table.equals(AbstractTablesComposite.this.identifiersTable)) {
					AbstractTablesComposite.this.handleIdentifiersTableDeleteButtonSelectionEvent(item, table);
				}
				// remove button from table
				e.widget.dispose();

				// 'refresh' happens when TableEditor.layout() is called for *all* the tableEditors and it gets called when the
				// table gets any of these events - SWT.KeyDown, SWT.KeyUp, SWT.MouseDown, SWT.MouseUp, SWT.Resize
				table.notifyListeners(SWT.MouseDown, new Event());
			}

		};
	}
}
