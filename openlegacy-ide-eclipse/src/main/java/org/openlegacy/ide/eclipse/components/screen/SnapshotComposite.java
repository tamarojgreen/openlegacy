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
package org.openlegacy.ide.eclipse.components.screen;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.components.ImageComposite;
import org.openlegacy.ide.eclipse.preview.screen.FieldRectangle;
import org.openlegacy.ide.eclipse.preview.screen.SelectedObject;
import org.openlegacy.terminal.RowPart;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.render.DefaultTerminalSnapshotImageRenderer;
import org.openlegacy.terminal.support.SimpleRowPart;
import org.openlegacy.terminal.support.SimpleTerminalPosition;
import org.openlegacy.utils.StringUtil;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class SnapshotComposite extends ImageComposite {

	private class RectangleDrawAction {

		protected Color background;
		protected Color foregraund;
		protected Rectangle rectangle;
		protected RectangleDrawType type;

		public RectangleDrawAction(RectangleDrawType type, Rectangle rectangle, Color background, Color foregraund) {
			this.type = type;
			this.rectangle = rectangle;
			this.background = background;
			this.foregraund = foregraund;
		}
	}

	private enum RectangleDrawType {
		DRAW,
		FILL
	}

	private static final String CURSOR_TEXT_ID = "cursor.text";
	private static final int DND_END = 3;
	private static final int DND_MOVE = 2;
	private static final int DND_NONE = 0;
	private static final int DND_START = 1;
	private static final int LEFT_START_OFFSET = 1; // screen start from 1
	private static final int LEFT_COLUMN_OFFSET = -1; // consider also the 2 chars from the number

	private Canvas canvas;
	private int cursorCol = 1;
	private Label cursorLabel = null;
	private int cursorRow = 1;
	private Image defaultImage = null;
	private boolean isDragging = false;
	private boolean isScalable = false;
	private List<RectangleDrawAction> rectangleDrawActions = new ArrayList<RectangleDrawAction>();
	private int maxColCount = 0;
	private int maxRowCount = 0;
	private DefaultTerminalSnapshotImageRenderer renderer = new DefaultTerminalSnapshotImageRenderer();
	private double scale = 1.0d;
	private SelectedObject selectedObject;
	private TerminalSnapshot terminalSnapshot;

	private TerminalSnapshot terminalSnapshotCopy;

	public SnapshotComposite(Composite parent) {
		super(parent);
		initialize();

	}

	public SnapshotComposite(Composite parent, TerminalSnapshot terminalSnapshot) {
		super(parent);
		this.terminalSnapshot = terminalSnapshot;
		this.terminalSnapshotCopy = terminalSnapshot;
		this.generateDefaultImage();
		initialize();
	}

	private void calcCursorRectangle() {
		int x = renderer.toWidth(cursorCol + LEFT_COLUMN_OFFSET + renderer.getLeftColumnsOffset());
		int y = renderer.toHeight(cursorRow) + renderer.getTopPixelsOffset();
		int width = renderer.toWidth(1);
		int height = 3;
		this.rectangleDrawActions.clear();
		this.rectangleDrawActions.add(new RectangleDrawAction(RectangleDrawType.FILL, new Rectangle(x, y, width, height),
				getDisplay().getSystemColor(SWT.COLOR_WHITE), null));
	}

	private void calcSelectedRectangle(int startX, int startY, int endX, int endY, int dragState) {
		// if dragState == DND_NONE then it means that user perform double click action
		if (dragState == DND_NONE) {
			if (this.terminalSnapshotCopy == null) {
				return;
			}
			// calculate rectangle for mouse double click
			// fetch row & column for terminal position
			int row = (int)(renderer.fromHeight(startY) / scale) + LEFT_START_OFFSET;
			int col = (int)(renderer.fromWidth(startX) / scale) + LEFT_COLUMN_OFFSET;
			// if row or column less then cursorRow or cursorColumn then that means that mouse click was out of range of rectangle
			if (row < this.cursorRow || col < this.cursorCol) {
				return;
			}
			SimpleTerminalPosition terminalPosition = new SimpleTerminalPosition(row, col);
			TerminalField field = terminalSnapshotCopy.getField(terminalPosition);
			if ((field != null) && !field.isHidden()) {
				TerminalPosition startPosition = field.getPosition();
				TerminalPosition endPosition = field.getEndPosition();
				this.selectedObject = new SelectedObject();
				this.selectedObject.setFieldRectangle(new FieldRectangle(startPosition.getRow(), endPosition.getRow(),
						startPosition.getColumn(), endPosition.getColumn(), terminalSnapshotCopy.getText(startPosition,
								field.getLength())));
				this.selectedObject.setEditable(field.isEditable());
			}
		} else {
			int row = (int)(renderer.fromHeight(startY) / scale) + LEFT_START_OFFSET;
			int col = (int)(renderer.fromWidth(startX) / scale) + LEFT_COLUMN_OFFSET;
			int endRow = (int)(renderer.fromHeight(endY) / scale) + LEFT_START_OFFSET;
			int endCol = (int)(renderer.fromWidth(endX) / scale) + LEFT_COLUMN_OFFSET;
			// check if dragging starts outside the image
			if ((dragState == DND_START) && (col < 1 || col > this.maxColCount || row < 1 || row > this.maxRowCount)) {
				isDragging = false;
				return;
			} else if (dragState == DND_START) {
				this.selectedObject = new SelectedObject();
				this.selectedObject.setFieldRectangle(new FieldRectangle(row, 0, col, 0, ""));
			} else if ((dragState == DND_MOVE) || (dragState == DND_END)) {
				// in DND_MOVE & DND_END states, selectedRectangle must always exist
				row = this.selectedObject.getFieldRectangle().getRow();
				col = this.selectedObject.getFieldRectangle().getColumn();
				// check top
				if (endRow < 1) {
					endRow = 1;
				}
				// check right
				if (endCol > this.maxColCount) {
					endCol = this.maxColCount;
				}
				// check bottom
				if (endRow > this.maxRowCount) {
					endRow = this.maxRowCount;
				}
				// check left
				if (endCol < 1) {
					endCol = 1;
				}
				this.selectedObject.setFieldRectangle(new FieldRectangle(row, endRow, col, endCol, ""));
			}
		}
	}

	private void clearCursorLabel() {
		this.cursorLabel.setText("");
		this.cursorLabel.pack(true);
	}

	private void displayCursorPositionText() {
		if (this.cursorLabel != null) {

			if (terminalSnapshot == null || terminalSnapshot.getSnapshotType() == TerminalSnapshot.SnapshotType.INCOMING) {
				this.cursorLabel.setText(MessageFormat.format("{0}:{1} {2}:{3}", Messages.getString("label_col_row"),
						this.cursorRow, Messages.getString("label_col_column"), this.cursorCol));
			} else {

				String tmp = terminalSnapshot.getCommand();
				this.cursorLabel.setText(MessageFormat.format("{0}:{1} {2}:{3} {4}:{5}", Messages.getString("label_col_row"),
						this.cursorRow, Messages.getString("label_col_column"), this.cursorCol,
						Messages.getString("label_command"), tmp));
			}
			this.cursorLabel.setData(CURSOR_TEXT_ID, this.cursorLabel.getText());
			this.cursorLabel.pack(true);
		}
	}

	private void displaySelectedRectangleLabel() {
		if ((this.cursorLabel != null) && (this.selectedObject != null)) {
			String prevText = (String)this.cursorLabel.getData(CURSOR_TEXT_ID);
			this.cursorLabel.setText(MessageFormat.format("{0}    Selection -> {1}", prevText,//$NON-NLS-1$
					this.selectedObject.getFieldRectangle().toCoordsString()));
			this.cursorLabel.pack(true);
		}
	}

	private void drawFieldRectangle(FieldRectangle fieldRectangle) {
		if (fieldRectangle == null) {
			return;
		}
		int x = 0;
		int y = 0;
		int width = 0;
		int height = 0;
		if (fieldRectangle.getEndColumn() >= fieldRectangle.getColumn()) {
			x = renderer.toWidth(fieldRectangle.getColumn() + LEFT_COLUMN_OFFSET + renderer.getLeftColumnsOffset());
			width = renderer.toWidth(fieldRectangle.getEndColumn() - fieldRectangle.getColumn() + LEFT_START_OFFSET);
		} else {
			x = renderer.toWidth(fieldRectangle.getColumn() + renderer.getLeftColumnsOffset());
			width = renderer.toWidth(fieldRectangle.getEndColumn() - fieldRectangle.getColumn() + LEFT_COLUMN_OFFSET);
		}
		if (fieldRectangle.getEndRow() >= fieldRectangle.getRow()) {
			y = renderer.toHeight(fieldRectangle.getRow() + LEFT_COLUMN_OFFSET) + renderer.getTopPixelsOffset();
			height = renderer.toHeight(fieldRectangle.getEndRow() - fieldRectangle.getRow() + LEFT_START_OFFSET);
		} else {
			y = renderer.toHeight(fieldRectangle.getRow()) + renderer.getTopPixelsOffset();
			height = renderer.toHeight(fieldRectangle.getEndRow() - fieldRectangle.getRow() + LEFT_COLUMN_OFFSET);
		}
		this.addDrawingRectangle(new Rectangle(x, y, width, height), SWT.COLOR_YELLOW, true);
		displaySelectedRectangleLabel();
	}

	private void drawFieldRectangleBasedOnTerminalField(FieldRectangle fieldRectangle) {
		if ((fieldRectangle == null) || (this.terminalSnapshotCopy == null)) {
			return;
		}
		TerminalField field = this.terminalSnapshotCopy.getField(fieldRectangle.getRow(), fieldRectangle.getColumn());
		if ((field != null) && !field.isHidden()) {
			int length = field.getLength() > 0 ? field.getLength() : fieldRectangle.getEndColumn() - fieldRectangle.getColumn()
					+ LEFT_START_OFFSET;
			int x = renderer.toWidth(fieldRectangle.getColumn() + LEFT_COLUMN_OFFSET + renderer.getLeftColumnsOffset());
			int y = renderer.toHeight(fieldRectangle.getRow() + LEFT_COLUMN_OFFSET) + renderer.getTopPixelsOffset();
			int width = renderer.toWidth(length);
			int height = renderer.toHeight(1);
			if (fieldRectangle.getEndRow() >= fieldRectangle.getRow()) {
				height = renderer.toHeight(fieldRectangle.getEndRow() - fieldRectangle.getRow() + LEFT_START_OFFSET);
			}
			this.addDrawingRectangle(new Rectangle(x, y, width, height), SWT.COLOR_YELLOW, true);
			displaySelectedRectangleLabel();
		}
	}

	private void drawImage(Image image, GC gc) {
		if (image == null) {
			return;
		}
		Rectangle imageBounds = image.getBounds();
		Rectangle drawingBounds = getDrawingBounds(image.getBounds(), true);

		gc.drawImage(image, 0, 0, imageBounds.width, imageBounds.height, drawingBounds.x, drawingBounds.y, drawingBounds.width,
				drawingBounds.height);
	}

	private void drawRectangle(Rectangle rectangle, GC gc) {
		if (rectangle == null) {
			return;
		}
		Rectangle drawingBounds = getDrawingBounds(rectangle, false);
		gc.drawRectangle(drawingBounds);
	}

	private void fillRectangle(Rectangle rectangle, GC gc) {
		if (rectangle == null) {
			return;
		}
		Rectangle drawingBounds = getDrawingBounds(rectangle, false);
		gc.fillRectangle(drawingBounds);
	}

	private void fillSelectedObject() {
		if ((terminalSnapshotCopy != null) && selectedObject.isEditable()) {
			List<RowPart> rowParts = terminalSnapshotCopy.getRow(selectedObject.getFieldRectangle().getRow()).getRowParts();
			SimpleRowPart rowPart = null;
			for (RowPart rowPartItem : rowParts) {
				if (!(rowPartItem instanceof SimpleRowPart)) {
					continue;
				}
				int fieldColumn = selectedObject.getFieldRectangle().getColumn();
				int rightBorder = rowPartItem.getPosition().getColumn() + ((SimpleRowPart)rowPartItem).getLength();
				if ((fieldColumn >= rowPartItem.getPosition().getColumn()) && (fieldColumn <= rightBorder)) {
					int index = rowParts.indexOf(rowPartItem);
					if (index > 0) {
						--index;
					}
					rowPart = (SimpleRowPart)rowParts.get(index);
					break;
				}
			}
			if (rowPart != null) {
				List<TerminalField> fields = rowPart.getFields();
				for (int i = fields.size() - 1; i >= 0; i--) {
					TerminalField field = fields.get(i);
					if ((field.getValue() != null) && !field.getValue().trim().isEmpty()) {
						selectedObject.setLabelColumn(field.getPosition().getColumn());
						selectedObject.setDisplayName(StringUtil.toDisplayName(field.getValue().trim()));
						selectedObject.setFieldName(StringUtil.toJavaFieldName(field.getValue().trim()));
						break;
					}
				}
			}
		}
	}

	private void generateDefaultImage() {
		if (terminalSnapshot == null) {
			return;
		}
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		renderer.render(terminalSnapshot, baos);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		defaultImage = new Image(getShell().getDisplay(), bais);
	}

	private PaintListener getCursorPaintListener() {
		return new PaintListener() {

			public void paintControl(PaintEvent e) {
				if (rectangleDrawActions.isEmpty()) {
					return;
				}
				RectangleDrawAction drawAction = null;
				for (RectangleDrawAction rectangleDrawAction : rectangleDrawActions) {
					if (rectangleDrawAction.type.equals(RectangleDrawType.FILL)) {
						drawAction = rectangleDrawAction;
						break;
					}
				}
				if (drawAction == null) {
					return;
				}
				if (defaultImage != null) {
					drawImage(defaultImage, e.gc);
				}

				e.gc.setBackground(drawAction.background);
				fillRectangle(drawAction.rectangle, e.gc);
			}
		};
	}

	private DragDetectListener getDragDetectListener() {
		return new DragDetectListener() {

			public void dragDetected(DragDetectEvent e) {
				isDragging = true;
				calcSelectedRectangle(e.x, e.y, 0, 0, DND_START);
			}
		};
	}

	private Rectangle getDrawingBounds(Rectangle rect, boolean calcScale) {
		if (!isScalable) {
			return rect;
		}
		Rectangle canvasBounds = this.canvas.getBounds();
		if (calcScale) {
			double hScale = (double)canvasBounds.width / rect.width;
			double vScale = (double)canvasBounds.height / rect.height;

			scale = Math.min(1.0d, Math.min(hScale, vScale));
		}
		int width = (int)(rect.width * scale);
		int height = (int)(rect.height * scale);

		int x = (int)(rect.x * scale);
		int y = (int)(rect.y * scale);

		return new Rectangle(x, y, width, height);
	}

	private KeyListener getKeyListener() {
		return new KeyListener() {

			public void keyPressed(KeyEvent e) {
				switch (e.keyCode) {
					case SWT.ARROW_UP:
						if (cursorRow > 1) {
							cursorRow--;
						}
						break;
					case SWT.ARROW_DOWN:
						if (cursorRow < maxRowCount) {
							cursorRow++;
						}
						break;
					case SWT.ARROW_LEFT:
						if (cursorCol > 1) {
							cursorCol--;
						}
						break;
					case SWT.ARROW_RIGHT:
						if (cursorCol < maxColCount) {
							cursorCol++;
						}
						break;
					case SWT.HOME:
						if (cursorCol == 1) {
							return;
						}
						cursorCol = 1;
						break;
					case SWT.END:
						if (cursorCol == maxColCount) {
							return;
						}
						cursorCol = maxColCount;
						break;
					default:
						return;
				}
				calcCursorRectangle();
				displayCursorPositionText();
				setSnapshot(null);
			}

			public void keyReleased(KeyEvent e) {
				return;
			}
		};
	}

	private MouseListener getMouseListener() {
		return new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				calcSelectedRectangle(e.x, e.y, e.x, e.y, DND_NONE);
				if (selectedObject != null) {
					drawFieldRectangleBasedOnTerminalField(selectedObject.getFieldRectangle());
					fillSelectedObject();
				}
			}

			@Override
			public void mouseDown(MouseEvent e) {
				setCursorPosition(e.x, e.y);
				displayCursorPositionText();
				calcCursorRectangle();
				setSnapshot(null);
				selectedObject = null;
			}

			@Override
			public void mouseUp(MouseEvent e) {
				if (isDragging) {
					isDragging = false;
					calcSelectedRectangle(0, 0, e.x, e.y, DND_END);
					// compare end values with initial values
					int row = selectedObject.getFieldRectangle().getRow();
					int col = selectedObject.getFieldRectangle().getColumn();
					int endRow = selectedObject.getFieldRectangle().getEndRow();
					int endCol = selectedObject.getFieldRectangle().getEndColumn();
					if (row > endRow) {
						row = endRow;
						endRow = selectedObject.getFieldRectangle().getRow();
					}
					if (col > endCol) {
						col = endCol;
						endCol = selectedObject.getFieldRectangle().getColumn();
					}
					String text = "";
					// get text if terminalShapshotCopy is not NULL
					if (terminalSnapshotCopy != null) {
						TerminalPosition position = new SimpleTerminalPosition(row, col);
						TerminalField field = terminalSnapshotCopy.getField(position);
						if (field != null) {
							text = terminalSnapshotCopy.getText(position, endCol - col + renderer.getLeftColumnsOffset()
									+ LEFT_COLUMN_OFFSET);
							selectedObject.setEditable(field.isEditable());
						}
					}
					selectedObject.setFieldRectangle(new FieldRectangle(row, endRow, col, endCol, text));
					fillSelectedObject();
					drawFieldRectangle(selectedObject.getFieldRectangle());
				}
			}

		};
	}

	private MouseMoveListener getMouseMoveListener() {
		return new MouseMoveListener() {

			public void mouseMove(MouseEvent e) {
				if (isDragging) {
					calcSelectedRectangle(0, 0, e.x, e.y, DND_MOVE);
					drawFieldRectangle(selectedObject.getFieldRectangle());
				}
			}
		};
	}

	private PaintListener getRectanglePaintListener() {
		return new PaintListener() {

			public void paintControl(PaintEvent e) {
				if (rectangleDrawActions.isEmpty()) {
					return;
				}
				List<RectangleDrawAction> drawActions = new ArrayList<RectangleDrawAction>();
				for (RectangleDrawAction rectangleDrawAction : rectangleDrawActions) {
					if (rectangleDrawAction.type.equals(RectangleDrawType.DRAW)) {
						drawActions.add(rectangleDrawAction);
					}
				}
				if (drawActions.isEmpty()) {
					return;
				}
				if (defaultImage != null) {
					drawImage(defaultImage, e.gc);
				}

				for (RectangleDrawAction drawAction : drawActions) {
					e.gc.setForeground(drawAction.foregraund);
					drawRectangle(drawAction.rectangle, e.gc);
				}
			}
		};
	}

	private Listener getShellMouseListener(final Shell shell) {
		return new Listener() {

			public void handleEvent(Event e) {
				if (shell.isVisible()) {
					shell.close();
					shell.dispose();
					canvas.redraw();
				}
			}
		};
	}

	private PaintListener getTerminalPaintListener() {
		return new PaintListener() {

			public void paintControl(PaintEvent e) {
				drawImage(defaultImage, e.gc);
			}
		};
	}

	private void initialize() {
		GridData gd = new GridData(GridData.FILL_HORIZONTAL, GridData.FILL_VERTICAL, true, true);
		gd.widthHint = 850;
		gd.heightHint = 450;
		this.setLayoutData(gd);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		this.setLayout(gridLayout);

		gd = new GridData(GridData.FILL_HORIZONTAL, GridData.FILL_VERTICAL, true, true);
		gd.widthHint = 825;
		gd.heightHint = 400;

		this.canvas = new Canvas(this, SWT.NONE);
		this.canvas.setBackground(new Color(Display.getCurrent(), new RGB(0x00, 0x00, 0x00)));
		this.canvas.setLayoutData(gd);
		this.canvas.addPaintListener(this.getTerminalPaintListener());

		DefaultTerminalSnapshotImageRenderer renderer = new DefaultTerminalSnapshotImageRenderer();
		this.maxRowCount = renderer.getMaxImageRow() + LEFT_COLUMN_OFFSET;
		this.maxColCount = renderer.getMaxImageColumn() - renderer.getLeftColumnsOffset();

		this.canvas.addKeyListener(this.getKeyListener());
		// refs assembla #235
		this.canvas.addMouseListener(this.getMouseListener());
		this.canvas.addDragDetectListener(this.getDragDetectListener());
		this.canvas.addMouseMoveListener(this.getMouseMoveListener());

		GridData labelGd = new GridData();
		labelGd.verticalAlignment = GridData.BEGINNING;
		this.cursorLabel = new Label(this, SWT.NONE);
		this.cursorLabel.setLayoutData(labelGd);
		this.displayCursorPositionText();

		this.canvas.addPaintListener(this.getRectanglePaintListener());
		this.canvas.addPaintListener(this.getCursorPaintListener());
	}

	private void setCursorPosition(int x, int y) {
		int row = (int)(renderer.fromHeight(y) / scale) + LEFT_START_OFFSET;
		int col = (int)(renderer.fromWidth(x) / scale) + LEFT_COLUMN_OFFSET;

		if (col > 0 && col <= maxColCount && row > 0 && row <= maxRowCount) {
			this.cursorRow = (row);
			this.cursorCol = (col);
		}
	}

	/**
	 * Add rectangle draw action. Will draw borders only. Default color of borders is yellow. Also this method doesn't clear a
	 * list of draw actions
	 * 
	 * @param rectangle
	 */
	public void addDrawingRectangle(Rectangle rectangle) {
		addDrawingRectangle(rectangle, SWT.COLOR_YELLOW, false);
	}

	public void addDrawingRectangle(Rectangle rectangle, int color) {
		addDrawingRectangle(rectangle, color, false);
	}

	/**
	 * Add rectangle draw action. Will draw borders only.
	 * 
	 * @param rectangle
	 *            - rectangle which will be draw
	 * @param color
	 *            - color of borders (e.g. SWT.COLOR_YELLOW)
	 * @param clearListBeforeAdd
	 *            - clear list of draw action of not
	 */
	public void addDrawingRectangle(Rectangle rectangle, int color, boolean clearListBeforeAdd) {
		if (rectangle == null) {
			this.rectangleDrawActions.clear();
			if ((this.terminalSnapshot == null) || (!this.terminalSnapshot.equals(this.terminalSnapshotCopy))) {
				this.setSnapshot(this.terminalSnapshotCopy);
			}
			return;
		}
		if (clearListBeforeAdd && !this.rectangleDrawActions.isEmpty()) {
			this.rectangleDrawActions.clear();
		}
		this.rectangleDrawActions.add(new RectangleDrawAction(RectangleDrawType.DRAW, rectangle, null,
				getDisplay().getSystemColor(color)));
		this.setSnapshot(null);
	}

	public Canvas getCanvas() {
		return this.canvas;
	}

	public SelectedObject getSelectedObject() {
		return this.selectedObject;
	}

	public void setIsScalable(boolean isScalable) {
		this.isScalable = isScalable;
	}

	public void setSnapshot(TerminalSnapshot terminalSnapshot) {
		this.terminalSnapshot = terminalSnapshot;
		if ((terminalSnapshot != null) && (!terminalSnapshot.equals(this.terminalSnapshotCopy))) {
			this.terminalSnapshotCopy = terminalSnapshot;
			this.generateDefaultImage();
			this.clearCursorLabel();
		}
		if (terminalSnapshot != null) {
			this.rectangleDrawActions.clear();
		}
		this.canvas.redraw();
	}

	@Override
	public void showEnlargedImage() {
		if (this.defaultImage == null) {
			return;
		}

		// this.showingEnlarged = true;

		final Rectangle imageBounds = this.defaultImage.getBounds();
		Rectangle canvasBounds = this.canvas.getBounds();

		// Don't bother if the image is smaller than the canvas.
		if (imageBounds.width < canvasBounds.width & imageBounds.height < canvasBounds.height) {
			return;
		}

		Rectangle displayBounds = getDisplay().getBounds();
		int x = (canvasBounds.width - imageBounds.width) / 2;
		int y = (canvasBounds.height - imageBounds.height) / 2;
		Point where = toDisplay(new Point(x, y));

		// calculate new bounds
		x = Math.max(0, Math.min(where.x, displayBounds.width - imageBounds.width));
		y = Math.max(0, Math.min(where.y, displayBounds.height - imageBounds.height));

		final Shell shell = new Shell(getShell(), SWT.NO_TRIM);
		shell.setBounds(x - 1, y - 1, imageBounds.width + 2, imageBounds.height + 2);
		shell.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				if (isVisible()) {
					e.gc.drawImage(defaultImage, 1, 1);
					e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
					e.gc.drawRectangle(0, 0, imageBounds.width + 1, imageBounds.height + 1);

					if (!rectangleDrawActions.isEmpty()) {
						for (RectangleDrawAction rectangleDrawAction : rectangleDrawActions) {
							switch (rectangleDrawAction.type) {
								case DRAW:
									e.gc.setForeground(rectangleDrawAction.foregraund);
									e.gc.drawRectangle(rectangleDrawAction.rectangle);
									break;
								case FILL:
									e.gc.setBackground(rectangleDrawAction.background);
									e.gc.fillRectangle(rectangleDrawAction.rectangle);
									break;
							}
						}
					}
				}
			}
		});
		// minimize image on mouse double clicked
		shell.addListener(SWT.MouseDoubleClick, getShellMouseListener(shell));
		// minimize image when mouse leaves image bounds
		shell.addListener(SWT.MouseExit, getShellMouseListener(shell));
		shell.open();
	}
}
