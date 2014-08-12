package org.openlegacy.ide.eclipse.components;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.openlegacy.FieldType.General;
import org.openlegacy.definitions.FieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleBooleanFieldTypeDefinition;
import org.openlegacy.definitions.support.SimpleDateFieldTypeDefinition;
import org.openlegacy.ide.eclipse.Activator;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.components.providers.FieldsTableContentProvider;
import org.openlegacy.ide.eclipse.components.providers.IdentifiersTableContentProvider;
import org.openlegacy.ide.eclipse.components.screen.SnapshotComposite;
import org.openlegacy.ide.eclipse.components.support.ComboBoxEditingSupport;
import org.openlegacy.ide.eclipse.components.support.DialogEditingSupport;
import org.openlegacy.ide.eclipse.components.support.TextEditingSupport;
import org.openlegacy.ide.eclipse.preview.screen.SelectedObject;
import org.openlegacy.ide.eclipse.util.PopupUtil;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalPositionContainer;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenFieldDefinition;
import org.openlegacy.terminal.render.DefaultTerminalSnapshotImageRenderer;
import org.openlegacy.terminal.services.ScreenIdentifier;
import org.openlegacy.terminal.support.SimpleScreenIdentifier;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Ivan Bort
 * 
 */
public class TablesComposite extends Composite {

	private static final int TABLE_WIDTH = 400;
	private static final int TABLE_HEIGHT = 110;

	private ScreenEntityDefinition screenEntityDefinition;

	private TableViewer fieldsTableViewer;
	private TableViewer identifiersTableViewer;

	private Control paintedControl = null;

	public TablesComposite(Composite parent, int style, ScreenEntityDefinition screenEntityDefinition) {
		super(parent, style);
		this.screenEntityDefinition = screenEntityDefinition;
		createLayout();
	}

	public void setPaintedControl(Control control) {
		if (control == null) {
			return;
		}
		this.paintedControl = control;
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
		fieldsTableViewer.addSelectionChangedListener(getDefaultSelectionChangedListener());
	}

	private void createButtonsPanelForFieldsTable(Composite parent) {
		Composite mainComposite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.marginBottom = 0;
		gl.marginHeight = 0;
		gl.marginLeft = 0;
		gl.marginRight = 0;
		gl.marginTop = 0;
		gl.marginWidth = 0;
		mainComposite.setLayout(gl);
		GridData gd = new GridData();
		gd.horizontalIndent = 0;
		gd.verticalIndent = 0;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		mainComposite.setLayoutData(gd);

		Composite leftComposite = new Composite(mainComposite, SWT.NONE);
		RowLayout rl = new RowLayout(SWT.HORIZONTAL);
		rl.marginTop = 0;
		leftComposite.setLayout(rl);
		gd = new GridData();
		gd.horizontalAlignment = GridData.BEGINNING;
		gd.verticalIndent = 0;
		gd.grabExcessHorizontalSpace = true;
		leftComposite.setLayoutData(gd);

		Button btnString = new Button(leftComposite, SWT.PUSH);
		btnString.setImage(Activator.getDefault().getImage(Activator.ICON_STRING));
		btnString.setToolTipText(Messages.getString("btn_tooltip_add_string"));
		btnString.addSelectionListener(getNewStringFieldSelectionListener());

		Button btnInteger = new Button(leftComposite, SWT.PUSH);
		btnInteger.setImage(Activator.getDefault().getImage(Activator.ICON_INTEGER));
		btnInteger.setToolTipText(Messages.getString("btn_tooltip_add_integer"));
		btnInteger.addSelectionListener(getNewIntegerFieldSelectionListener());

		Button btnBoolean = new Button(leftComposite, SWT.PUSH);
		btnBoolean.setImage(Activator.getDefault().getImage(Activator.ICON_BOOLEAN));
		btnBoolean.setToolTipText(Messages.getString("btn_tooltip_add_boolean"));
		btnBoolean.addSelectionListener(getNewBooleanFieldSelectionListener());

		Button btnDate = new Button(leftComposite, SWT.PUSH);
		btnDate.setImage(Activator.getDefault().getImage(Activator.ICON_DATE));
		btnDate.setToolTipText(Messages.getString("btn_tooltip_add_date"));
		btnDate.addSelectionListener(getNewDateFieldSelectionListener());

		Composite rightComposite = new Composite(mainComposite, SWT.NONE);
		rl = new RowLayout(SWT.HORIZONTAL);
		rl.marginTop = 0;
		rightComposite.setLayout(rl);
		gd = new GridData();
		gd.horizontalAlignment = GridData.END;
		gd.verticalIndent = 0;
		gd.grabExcessHorizontalSpace = true;
		rightComposite.setLayoutData(gd);

		Button btnRemove = new Button(rightComposite, SWT.PUSH);
		btnRemove.setImage(Activator.getDefault().getImage(Activator.ICON_DELETE));
		btnRemove.setToolTipText(Messages.getString("btn_tooltip_remove"));
		btnRemove.addSelectionListener(getRemoveFieldSelectionListener());
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
		identifiersTableViewer.addSelectionChangedListener(getDefaultSelectionChangedListener());
	}

	private void createButtonsPanelForIdentifiersTable(Composite parent) {
		Composite mainComposite = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.marginBottom = 0;
		gl.marginHeight = 0;
		gl.marginLeft = 0;
		gl.marginRight = 0;
		gl.marginTop = 0;
		gl.marginWidth = 0;
		mainComposite.setLayout(gl);
		GridData gd = new GridData();
		gd.horizontalIndent = 0;
		gd.verticalIndent = 0;
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		mainComposite.setLayoutData(gd);

		Composite leftComposite = new Composite(mainComposite, SWT.NONE);
		RowLayout rl = new RowLayout(SWT.HORIZONTAL);
		rl.marginTop = 0;
		leftComposite.setLayout(rl);
		gd = new GridData();
		gd.horizontalAlignment = GridData.BEGINNING;
		gd.verticalIndent = 0;
		gd.grabExcessHorizontalSpace = true;
		leftComposite.setLayoutData(gd);

		Button btnIdentifier = new Button(leftComposite, SWT.PUSH);
		btnIdentifier.setImage(Activator.getDefault().getImage(Activator.ICON_PLUS));
		btnIdentifier.setToolTipText(Messages.getString("btn_tooltip_add_identifier"));
		btnIdentifier.addSelectionListener(getNewIdentifierSelectionListener());

		Composite rightComposite = new Composite(mainComposite, SWT.NONE);
		rl = new RowLayout(SWT.HORIZONTAL);
		rl.marginTop = 0;
		rightComposite.setLayout(rl);
		gd = new GridData();
		gd.horizontalAlignment = GridData.END;
		gd.verticalIndent = 0;
		gd.grabExcessHorizontalSpace = true;
		rightComposite.setLayoutData(gd);

		Button btnRemove = new Button(rightComposite, SWT.PUSH);
		btnRemove.setImage(Activator.getDefault().getImage(Activator.ICON_DELETE));
		btnRemove.setToolTipText(Messages.getString("btn_tooltip_remove"));
		btnRemove.addSelectionListener(getRemoveIdentifierSelectionListener());
	}

	private static Composite getColumnComposite(Composite parent) {
		Composite composite = new Composite(parent, SWT.BORDER);
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

	private static void createFieldsTableColumns(TableViewer tableViewer) {
		// "Fields"
		TableViewerColumn vcol = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tcol = vcol.getColumn();
		tcol.setText(Messages.getString("label_col_fields"));
		tcol.setResizable(false);
		tcol.setWidth(115);
		vcol.setEditingSupport(new TextEditingSupport(tableViewer));
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
		tcol.setWidth(38);
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
		tcol.setWidth(88);
		vcol.setEditingSupport(new ComboBoxEditingSupport(tableViewer, getJavaTypeItems()));
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
		tcol.setWidth(88);
		vcol.setEditingSupport(new DialogEditingSupport(tableViewer));
		vcol.setLabelProvider(new CellLabelProvider() {

			@Override
			public void update(ViewerCell cell) {
				ScreenFieldDefinition definition = (ScreenFieldDefinition)cell.getElement();
				cell.setText(definition.getType() == null ? "" : definition.getType().getSimpleName());
			}
		});
	}

	private static void createIdentifiersTableColumns(TableViewer tableViewer) {
		// "Fields"
		TableViewerColumn vcol = new TableViewerColumn(tableViewer, SWT.NONE);
		TableColumn tcol = vcol.getColumn();
		tcol.setText(Messages.getString("label_col_identifiers"));
		tcol.setResizable(false);
		tcol.setWidth(240);
		vcol.setEditingSupport(new TextEditingSupport(tableViewer));
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
	}

	private static Rectangle getRectangleForDrawing(Object element) {
		TerminalPosition position = ((TerminalPositionContainer)element).getPosition();
		if (position == null) {
			return null;
		}

		int length = 0;
		if (element instanceof ScreenFieldDefinition) {
			length = ((ScreenFieldDefinition)element).getLength();
		} else if (element instanceof SimpleScreenIdentifier) {
			length = ((SimpleScreenIdentifier)element).getText().length();
		} else {
			return null;
		}
		DefaultTerminalSnapshotImageRenderer renderer = new DefaultTerminalSnapshotImageRenderer();

		int x = renderer.toWidth(position.getColumn() - 1 + renderer.getLeftColumnsOffset());
		int y = renderer.toHeight(position.getRow() - 1) + renderer.getTopPixelsOffset();
		int width = renderer.toWidth(length);
		int height = renderer.toHeight(1);
		return new Rectangle(x, y, width, height);
	}

	private void redrawPaintedControl(Object element) {
		if (this.paintedControl == null) {
			return;
		}
		if (this.paintedControl instanceof SnapshotComposite) {
			((SnapshotComposite)this.paintedControl).addDrawingRectangle(getRectangleForDrawing(element), SWT.COLOR_YELLOW, true);
			((SnapshotComposite)this.paintedControl).setSnapshot(null);
		} else {
			this.paintedControl.redraw();
		}
	}

	private ISelectionChangedListener getDefaultSelectionChangedListener() {
		return new ISelectionChangedListener() {

			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				if (!selection.isEmpty()) {
					redrawPaintedControl(selection.getFirstElement());
				}
			}
		};
	}

	private SelectionListener getRemoveFieldSelectionListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection)fieldsTableViewer.getSelection();
				if (!selection.isEmpty()) {
					ScreenFieldDefinition definition = (ScreenFieldDefinition)selection.getFirstElement();
					Map<String, ScreenFieldDefinition> map = screenEntityDefinition.getFieldsDefinitions();
					if (map.containsValue(definition)) {
						map.values().remove(definition);
						fieldsTableViewer.setInput(screenEntityDefinition);
					}
				}

			}

		};
	}

	private SelectionListener getRemoveIdentifierSelectionListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection)identifiersTableViewer.getSelection();
				if (!selection.isEmpty()) {
					ScreenIdentifier identifier = (ScreenIdentifier)selection.getFirstElement();
					List<ScreenIdentifier> list = screenEntityDefinition.getScreenIdentification().getScreenIdentifiers();
					if (list.contains(identifier)) {
						list.remove(identifier);
						identifiersTableViewer.setInput(screenEntityDefinition);
					}
				}
			}
		};
	}

	/** Returns <b>true</b> if selection exist */
	private SelectedObject getSelectedObject() {
		if (paintedControl != null && paintedControl instanceof SnapshotComposite) {
			SnapshotComposite snapshotComposite = (SnapshotComposite)paintedControl;
			if (snapshotComposite.getSelectedObject() != null) {
				return snapshotComposite.getSelectedObject();
			}
		}
		PopupUtil.error(Messages.getString("error_selection_not_specified"));
		return null;
	}

	private static String generateNewFieldName(ScreenEntityDefinition entityDefinition, String baseFieldName) {
		List<ScreenFieldDefinition> sortedFields = entityDefinition.getSortedFields();
		String fieldNamePrefix = StringUtils.isEmpty(baseFieldName) ? Messages.getString("new_field_name_prefix") : baseFieldName;
		int seed = 0;
		for (ScreenFieldDefinition fieldDefinition : sortedFields) {
			String fieldName = fieldDefinition.getName();
			if (!StringUtils.isEmpty(fieldName)) {
				Pattern p = Pattern.compile(fieldNamePrefix + "(\\d+)$");
				Matcher matcher = p.matcher(fieldName);
				if (matcher.find()) {
					String stringSeed = matcher.group(1);
					if (Integer.parseInt(stringSeed) > seed) {
						seed = Integer.parseInt(stringSeed);
					}
				}
			}
		}
		return MessageFormat.format("{0}{1}", fieldNamePrefix, ++seed);
	}

	private void addNewField(Class<?> javaType) {
		SelectedObject selectedObject = getSelectedObject();
		if (selectedObject != null) {
			String fieldName = selectedObject.getFieldName();
			if (screenEntityDefinition.getFieldsDefinitions().containsKey(fieldName)) {
				fieldName = generateNewFieldName(screenEntityDefinition, fieldName);
			}
			if (StringUtils.isEmpty(fieldName)) {
				fieldName = generateNewFieldName(screenEntityDefinition, null);
			}

			SimpleScreenFieldDefinition definition = new SimpleScreenFieldDefinition(fieldName, General.class);
			definition.setJavaType(javaType);
			definition.setPosition(selectedObject.getFieldRectangle().getStartPosition());
			definition.setEndPosition(selectedObject.getFieldRectangle().getEndPosition());
			definition.setDisplayName(selectedObject.getDisplayName());
			definition.setLength(selectedObject.getFieldRectangle().getEndColumn()
					- selectedObject.getFieldRectangle().getColumn() + 1);

			FieldTypeDefinition fieldTypeDefinition = null;
			if (javaType.isAssignableFrom(Boolean.class)) {
				fieldTypeDefinition = new SimpleBooleanFieldTypeDefinition("", "", false);
			} else if (javaType.isAssignableFrom(Date.class)) {
				fieldTypeDefinition = new SimpleDateFieldTypeDefinition(0, 0, 0, "");
			}
			if (fieldTypeDefinition != null) {
				definition.setFieldTypeDefinition(fieldTypeDefinition);
			}

			screenEntityDefinition.getFieldsDefinitions().put(definition.getName(), definition);
			fieldsTableViewer.setInput(screenEntityDefinition);
		}
	}

	private SelectionListener getNewStringFieldSelectionListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewField(String.class);
			}

		};
	}

	private SelectionListener getNewIntegerFieldSelectionListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewField(Integer.class);
			}

		};
	}

	private SelectionListener getNewBooleanFieldSelectionListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewField(Boolean.class);
			}

		};
	}

	private SelectionListener getNewDateFieldSelectionListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				addNewField(Date.class);
			}

		};
	}

	private SelectionListener getNewIdentifierSelectionListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				SelectedObject selectedObject = getSelectedObject();
				if (selectedObject != null) {
					SimpleScreenIdentifier identifier = new SimpleScreenIdentifier(
							selectedObject.getFieldRectangle().getStartPosition(), selectedObject.getFieldRectangle().getValue(),
							screenEntityDefinition.isRightToLeft());
					screenEntityDefinition.getScreenIdentification().addIdentifier(identifier);
					identifiersTableViewer.setInput(screenEntityDefinition);
				}
			}

		};
	}

	private static List<String> getJavaTypeItems() {
		List<String> list = new ArrayList<String>();
		list.add(String.class.getSimpleName());
		list.add(Integer.class.getSimpleName());
		list.add(Boolean.class.getSimpleName());
		list.add(Date.class.getSimpleName());
		return list;
	}

}
