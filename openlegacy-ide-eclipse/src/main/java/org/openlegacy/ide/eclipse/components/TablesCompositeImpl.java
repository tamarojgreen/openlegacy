package org.openlegacy.ide.eclipse.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalPositionContainer;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.render.DefaultTerminalSnapshotImageRenderer;
import org.openlegacy.terminal.services.ScreenIdentifier;
import org.openlegacy.terminal.support.SimpleScreenIdentifier;

import java.util.ArrayList;

public class TablesCompositeImpl extends AbstractTablesComposite {

	private Image copyOfImage = null;
	private Control paintedControl = null;

	public TablesCompositeImpl(Composite parent, int style, int width, int height) {
		super(parent, style, width, height);
	}

	@Override
	protected void handleFieldsTableSelectionEvent(SelectionEvent e) {
		TableItem item = (TableItem)e.item;
		if (item == null) {
			return;
		}
		ScreenFieldDefinition field = (ScreenFieldDefinition)item.getData(ITEM_DATA_KEY);
		this.setField(field);
	}

	@Override
	protected void handleIdentifiersTableSelectionEvent(SelectionEvent e) {
		TableItem item = (TableItem)e.item;
		if (item == null) {
			return;
		}
		SimpleScreenIdentifier field = (SimpleScreenIdentifier)item.getData(ITEM_DATA_KEY);
		this.setField(field);
	}

	private void setField(Object field) {
		this.field = field;
		this.redrawPaintedControl();
	}

	private void redrawPaintedControl() {
		if (this.paintedControl == null) {
			return;
		}
		if (this.paintedControl instanceof SnapshotComposite) {
			((SnapshotComposite)this.paintedControl).setSnapshot(null);
		} else {
			this.paintedControl.redraw();
		}
	}

	private Rectangle getRectangleForDrawing(Object field) {
		TerminalPosition position = ((TerminalPositionContainer)this.field).getPosition();
		if (position == null) {
			return null;
		}

		int length = 0;
		if (this.field instanceof ScreenFieldDefinition) {
			length = ((ScreenFieldDefinition)this.field).getLength();
		} else if (this.field instanceof SimpleScreenIdentifier) {
			length = ((SimpleScreenIdentifier)this.field).getText().length();
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

	public void setPaintedControl(Control control) {
		if (control == null) {
			return;
		}
		this.paintedControl = control;

		this.paintedControl.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				if (TablesCompositeImpl.this.copyOfImage == null) {
					Rectangle rect = TablesCompositeImpl.this.paintedControl.getBounds();
					TablesCompositeImpl.this.copyOfImage = new Image(e.gc.getDevice(), rect);
					e.gc.copyArea(TablesCompositeImpl.this.copyOfImage, 0, 0);
				}
				if (TablesCompositeImpl.this.field == null) {
					return;
				}
				Rectangle rect = TablesCompositeImpl.this.getRectangleForDrawing(TablesCompositeImpl.this.field);
				if (rect == null) {
					return;
				}

				if (TablesCompositeImpl.this.copyOfImage != null) {
					e.gc.drawImage(TablesCompositeImpl.this.copyOfImage, 0, 0);
				}

				e.gc.setForeground(e.display.getSystemColor(SWT.COLOR_YELLOW));

				e.gc.drawRectangle(rect);
			}
		});
	}

	@Override
	protected FocusListener getFocusListener() {
		return new FocusListener() {

			public void focusLost(FocusEvent arg0) {
				TablesCompositeImpl.this.field = null;
			}

			public void focusGained(FocusEvent arg0) {}
		};
	}

	@Override
	protected void handleFieldsTableDeleteButtonSelectionEvent(TableItem item, Table table) {
		if (!(item.getData(ITEM_DATA_KEY) instanceof ScreenFieldDefinition)) {
			return;
		}
		if (this.removedFieldsDefinitions == null) {
			this.removedFieldsDefinitions = new ArrayList<ScreenFieldDefinition>();
		}
		this.removedFieldsDefinitions.add((ScreenFieldDefinition)item.getData(ITEM_DATA_KEY));
		table.remove(table.indexOf(item));
	}

	@Override
	protected void handleIdentifiersTableDeleteButtonSelectionEvent(TableItem item, Table table) {
		if (!(item.getData(ITEM_DATA_KEY) instanceof ScreenIdentifier)) {
			return;
		}
		if (this.removedScreenIdentifiers == null) {
			this.removedScreenIdentifiers = new ArrayList<ScreenIdentifier>();
		}
		this.removedScreenIdentifiers.add((ScreenIdentifier)item.getData(ITEM_DATA_KEY));
		table.remove(table.indexOf(item));
	}
}
