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
package org.openlegacy.ide.eclipse.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
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
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.render.DefaultTerminalSnapshotImageRenderer;
import org.openlegacy.terminal.render.TerminalSnapshotImageRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class SnapshotComposite extends Composite {

	private enum RectangleDrawType {
		FILL,
		DRAW
	}

	private class RectangleDrawAction {

		protected RectangleDrawType type;
		protected Rectangle rectangle;
		protected Color background;
		protected Color foregraund;

		public RectangleDrawAction(RectangleDrawType type, Rectangle rectangle, Color background, Color foregraund) {
			this.type = type;
			this.rectangle = rectangle;
			this.background = background;
			this.foregraund = foregraund;
		}
	}

	private TerminalSnapshot terminalSnapshot;
	private TerminalSnapshot terminalSnapshotCopy;
	private int cursorRow = 1;
	private int cursorCol = 1;
	private int maxRowCount = 0;
	private int maxColCount = 0;
	private Image defaultImage = null;
	private Label cursorLabel = null;
	private Canvas canvas;
	private boolean isScalable = false;
	private double scale = 1.0d;
	private RectangleDrawAction lastRectangleDrawAction = null;
	private boolean showingEnlarged = false;

	public SnapshotComposite(Composite parent) {
		super(parent, SWT.NONE);
		initialize();

	}

	public SnapshotComposite(Composite parent, TerminalSnapshot terminalSnapshot) {
		super(parent, SWT.NONE);
		this.terminalSnapshot = terminalSnapshot;
		this.terminalSnapshotCopy = terminalSnapshot;
		this.generateDefaultImage();
		initialize();
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
		this.maxRowCount = renderer.getMaxImageRow() - 2;
		this.maxColCount = renderer.getMaxImageColumn() - renderer.getLeftColumnsOffset();

		this.canvas.addFocusListener(this.getFocusListener());
		this.canvas.addListener(SWT.MouseDown, this.getMouseDownListener());
		this.canvas.addKeyListener(this.getKeyListener());

		GridData labelGd = new GridData();
		labelGd.verticalAlignment = GridData.BEGINNING;
		this.cursorLabel = new Label(this, SWT.NONE);
		this.cursorLabel.setLayoutData(labelGd);
		this.displayCursorPosition();

		this.canvas.addPaintListener(this.getRectanglePaintListener());
		this.canvas.addPaintListener(this.getCursorPaintListener());
	}

	public void setSnapshot(TerminalSnapshot terminalSnapshot) {
		this.terminalSnapshot = terminalSnapshot;
		if ((terminalSnapshot != null) && (!terminalSnapshot.equals(this.terminalSnapshotCopy))) {
			this.terminalSnapshotCopy = terminalSnapshot;
			this.generateDefaultImage();
			this.clearCursorLabel();
		}
		if (terminalSnapshot != null) {
			this.lastRectangleDrawAction = null;
		}
		this.canvas.redraw();
	}

	public Canvas getCanvas() {
		return this.canvas;
	}

	public void setDrawingRectangle(Rectangle rectangle) {
		if (rectangle == null) {
			this.lastRectangleDrawAction = null;
			if ((this.terminalSnapshot == null) || (!this.terminalSnapshot.equals(this.terminalSnapshotCopy))) {
				this.setSnapshot(this.terminalSnapshotCopy);
			}
			return;
		}
		this.lastRectangleDrawAction = new RectangleDrawAction(RectangleDrawType.DRAW, rectangle, null,
				getDisplay().getSystemColor(SWT.COLOR_YELLOW));
		this.setSnapshot(null);
	}

	public void setIsScalable(boolean isScalable) {
		this.isScalable = isScalable;
	}

	public void initDoubleClickEnlargeListener() {
		this.canvas.addListener(SWT.MouseDoubleClick, this.getEnlargeListener());
	}

	private void generateDefaultImage() {
		if (SnapshotComposite.this.terminalSnapshot == null) {
			return;
		}
		TerminalSnapshotImageRenderer renderer = new DefaultTerminalSnapshotImageRenderer();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		renderer.render(SnapshotComposite.this.terminalSnapshot, baos);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		SnapshotComposite.this.defaultImage = new Image(getShell().getDisplay(), bais);
	}

	private Listener getMouseDownListener() {
		return new Listener() {

			public void handleEvent(Event e) {
				SnapshotComposite.this.setCursorPosition(e.x, e.y);
				SnapshotComposite.this.displayCursorPosition();
				SnapshotComposite.this.calcCursorRectangle();
				SnapshotComposite.this.setSnapshot(null);
			}
		};
	}

	private FocusListener getFocusListener() {
		return new FocusListener() {

			public void focusLost(FocusEvent e) {
				if (!SnapshotComposite.this.showingEnlarged) {
					SnapshotComposite.this.lastRectangleDrawAction = null;
				}
			}

			public void focusGained(FocusEvent e) {}
		};
	}

	private KeyListener getKeyListener() {
		return new KeyListener() {

			public void keyReleased(KeyEvent e) {
				return;
			}

			public void keyPressed(KeyEvent e) {
				switch (e.keyCode) {
					case SWT.ARROW_UP:
						if (SnapshotComposite.this.cursorRow > 1) {
							SnapshotComposite.this.cursorRow--;
						}
						break;
					case SWT.ARROW_DOWN:
						if (SnapshotComposite.this.cursorRow < SnapshotComposite.this.maxRowCount) {
							SnapshotComposite.this.cursorRow++;
						}
						break;
					case SWT.ARROW_LEFT:
						if (SnapshotComposite.this.cursorCol > 1) {
							SnapshotComposite.this.cursorCol--;
						}
						break;
					case SWT.ARROW_RIGHT:
						if (SnapshotComposite.this.cursorCol < SnapshotComposite.this.maxColCount) {
							SnapshotComposite.this.cursorCol++;
						}
						break;
					case SWT.HOME:
						if (SnapshotComposite.this.cursorCol == 1) {
							return;
						}
						SnapshotComposite.this.cursorCol = 1;
						break;
					case SWT.END:
						if (SnapshotComposite.this.cursorCol == SnapshotComposite.this.maxColCount) {
							return;
						}
						SnapshotComposite.this.cursorCol = SnapshotComposite.this.maxColCount;
						break;
					default:
						return;
				}
				SnapshotComposite.this.calcCursorRectangle();
				SnapshotComposite.this.displayCursorPosition();
				SnapshotComposite.this.setSnapshot(null);
			}
		};
	}

	private void setCursorPosition(int x, int y) {
		DefaultTerminalSnapshotImageRenderer renderer = new DefaultTerminalSnapshotImageRenderer();

		int row = (int)(renderer.fromHeight(y) / scale) + 1; // screen start from 1
		int col = (int)(renderer.fromWidth(x) / scale) - 1; // consider also the 2 chars from the number

		if (col > 0 && col <= SnapshotComposite.this.maxColCount && row > 0 && row <= SnapshotComposite.this.maxRowCount) {
			this.cursorRow = (row);
			this.cursorCol = (col);
		}
	}

	private void clearCursorLabel() {
		this.cursorLabel.setText("");
		this.cursorLabel.pack(true);
	}

	private void displayCursorPosition() {
		if (this.cursorLabel != null) {

			if (SnapshotComposite.this.terminalSnapshot == null
					|| SnapshotComposite.this.terminalSnapshot.getSnapshotType() == TerminalSnapshot.SnapshotType.INCOMING) {
				this.cursorLabel.setText(Messages.label_col_row + ": " + this.cursorRow + " " + Messages.label_col_column + ": "
						+ this.cursorCol);
			} else {

				String tmp = SnapshotComposite.this.terminalSnapshot.getCommand();
				this.cursorLabel.setText(Messages.label_col_row + ": " + this.cursorRow + " " + Messages.label_col_column + ": "
						+ this.cursorCol + " " + Messages.label_command + ": " + tmp);
			}
			this.cursorLabel.pack(true);
		}
	}

	private void calcCursorRectangle() {
		DefaultTerminalSnapshotImageRenderer renderer = new DefaultTerminalSnapshotImageRenderer();

		int x = renderer.toWidth(SnapshotComposite.this.cursorCol - 1 + renderer.getLeftColumnsOffset());
		int y = renderer.toHeight(SnapshotComposite.this.cursorRow) + renderer.getTopPixelsOffset();
		int width = renderer.toWidth(1);
		int height = 3;
		this.lastRectangleDrawAction = new RectangleDrawAction(RectangleDrawType.FILL, new Rectangle(x, y, width, height),
				getDisplay().getSystemColor(SWT.COLOR_WHITE), null);
	}

	private PaintListener getCursorPaintListener() {
		return new PaintListener() {

			public void paintControl(PaintEvent e) {
				if ((SnapshotComposite.this.lastRectangleDrawAction == null)
						|| (SnapshotComposite.this.lastRectangleDrawAction.type.equals(RectangleDrawType.DRAW))) {
					return;
				}
				if (SnapshotComposite.this.defaultImage != null) {
					drawImage(SnapshotComposite.this.defaultImage, e.gc);
				}

				e.gc.setBackground(SnapshotComposite.this.lastRectangleDrawAction.background);
				fillRectangle(SnapshotComposite.this.lastRectangleDrawAction.rectangle, e.gc);
			}
		};
	}

	private PaintListener getTerminalPaintListener() {
		return new PaintListener() {

			public void paintControl(PaintEvent e) {
				drawImage(SnapshotComposite.this.defaultImage, e.gc);
				// SnapshotComposite.this.defaultImage = image;
			}
		};
	}

	private PaintListener getRectanglePaintListener() {
		return new PaintListener() {

			public void paintControl(PaintEvent e) {
				if ((SnapshotComposite.this.lastRectangleDrawAction == null)
						|| (SnapshotComposite.this.lastRectangleDrawAction.type.equals(RectangleDrawType.FILL))) {
					return;
				}
				if (SnapshotComposite.this.defaultImage != null) {
					drawImage(SnapshotComposite.this.defaultImage, e.gc);
				}

				e.gc.setForeground(SnapshotComposite.this.lastRectangleDrawAction.foregraund);

				drawRectangle(SnapshotComposite.this.lastRectangleDrawAction.rectangle, e.gc);
			}
		};
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

	private Listener getEnlargeListener() {
		return new Listener() {

			public void handleEvent(Event e) {
				if (!isVisible()) {
					return;
				}
				SnapshotComposite.this.showEnlargedImage();
			}
		};
	}

	private void showEnlargedImage() {
		if (this.defaultImage == null) {
			return;
		}

		this.showingEnlarged = true;

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
					e.gc.drawImage(SnapshotComposite.this.defaultImage, 1, 1);
					e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
					e.gc.drawRectangle(0, 0, imageBounds.width + 1, imageBounds.height + 1);

					if (SnapshotComposite.this.lastRectangleDrawAction != null) {
						switch (SnapshotComposite.this.lastRectangleDrawAction.type) {
							case DRAW:
								e.gc.setForeground(SnapshotComposite.this.lastRectangleDrawAction.foregraund);
								e.gc.drawRectangle(SnapshotComposite.this.lastRectangleDrawAction.rectangle);
								break;
							case FILL:
								e.gc.setBackground(SnapshotComposite.this.lastRectangleDrawAction.background);
								e.gc.fillRectangle(SnapshotComposite.this.lastRectangleDrawAction.rectangle);
								break;
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

	private Listener getShellMouseListener(final Shell shell) {
		return new Listener() {

			public void handleEvent(Event e) {
				if (shell.isVisible()) {
					shell.close();
					shell.dispose();
					SnapshotComposite.this.showingEnlarged = false;
					SnapshotComposite.this.canvas.redraw();
				}
			}
		};
	}
}
