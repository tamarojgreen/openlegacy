package org.openlegacy.ide.eclipse.components;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.render.DefaultTerminalSnapshotImageRenderer;
import org.openlegacy.terminal.render.TerminalSnapshotImageRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class SnapshotComposite extends Composite {

	private TerminalSnapshot terminalSnapshot;
	private int cursorRow = 1;
	private int cursorCol = 1;
	private int maxRowCount = 0;
	private int maxColCount = 0;
	private Image copyOfImage = null;
	private PaintListener cursorPaintListener = null;
	private Label cursorLabel = null;
	private Canvas canvas;

	public SnapshotComposite(Composite parent) {
		super(parent, SWT.NONE);
		initialize();

	}

	public SnapshotComposite(Composite parent, TerminalSnapshot terminalSnapshot) {
		super(parent, SWT.NONE);
		this.terminalSnapshot = terminalSnapshot;
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
		gd.widthHint = 850;
		gd.heightHint = 425;

		this.canvas = new Canvas(this, SWT.NONE);
		this.canvas.setBackground(new Color(Display.getCurrent(), new RGB(0x00, 0x00, 0x00)));
		this.canvas.setLayoutData(gd);
		this.canvas.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				if (terminalSnapshot == null) {
					return;
				}
				TerminalSnapshotImageRenderer renderer = new DefaultTerminalSnapshotImageRenderer();
				final ByteArrayOutputStream baos = new ByteArrayOutputStream();
				renderer.render(terminalSnapshot, baos);
				ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
				Image image = new Image(getShell().getDisplay(), bais);
				e.gc.drawImage(image, 0, 0);
				SnapshotComposite.this.copyOfImage = image;
			}
		});

		DefaultTerminalSnapshotImageRenderer renderer = new DefaultTerminalSnapshotImageRenderer();
		this.maxRowCount = renderer.getMaxImageRow() - 2;
		this.maxColCount = renderer.getMaxImageColumn() - renderer.getLeftColumnsOffset();

		this.canvas.addFocusListener(this.getFocusListener());
		this.canvas.addKeyListener(this.getKeyListener());

		GridData labelGd = new GridData();
		labelGd.verticalAlignment = GridData.BEGINNING;
		this.cursorLabel = new Label(this, SWT.NONE);
		this.cursorLabel.setLayoutData(labelGd);
		this.displayCursorPosition();
	}

	public void setSnapshot(TerminalSnapshot terminalSnapshot) {
		this.terminalSnapshot = terminalSnapshot;
		this.canvas.redraw();
	}

	public Canvas getCanvas() {
		return this.canvas;
	}

	private FocusListener getFocusListener() {
		return new FocusListener() {

			public void focusLost(FocusEvent e) {
				SnapshotComposite.this.canvas.removePaintListener(SnapshotComposite.this.cursorPaintListener);
				SnapshotComposite.this.cursorPaintListener = null;
			}

			public void focusGained(FocusEvent e) {
				if (SnapshotComposite.this.cursorPaintListener == null) {
					SnapshotComposite.this.cursorPaintListener = SnapshotComposite.this.getCursorPaintListener();
					SnapshotComposite.this.canvas.addPaintListener(SnapshotComposite.this.cursorPaintListener);
				}
				SnapshotComposite.this.displayCursorPosition();
				SnapshotComposite.this.setSnapshot(null);
			}
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
					default:
						return;
				}
				SnapshotComposite.this.displayCursorPosition();
				SnapshotComposite.this.setSnapshot(null);
			}
		};
	}

	private void displayCursorPosition() {
		if (this.cursorLabel != null) {
			this.cursorLabel.setText(Messages.label_col_row + ": " + this.cursorRow + " " + Messages.label_col_column + ": "
					+ this.cursorCol);
			this.cursorLabel.pack(true);
		}
	}

	private PaintListener getCursorPaintListener() {
		return new PaintListener() {

			public void paintControl(PaintEvent e) {
				if (SnapshotComposite.this.cursorPaintListener == null) {
					return;
				}
				if (SnapshotComposite.this.copyOfImage != null) {
					e.gc.drawImage(SnapshotComposite.this.copyOfImage, 0, 0);
				}

				DefaultTerminalSnapshotImageRenderer renderer = new DefaultTerminalSnapshotImageRenderer();

				int x = renderer.toWidth(SnapshotComposite.this.cursorCol - 1 + renderer.getLeftColumnsOffset());
				int y = renderer.toHeight(SnapshotComposite.this.cursorRow) + renderer.getTopPixelsOffset();
				int width = renderer.toWidth(1);
				int height = 3;

				e.gc.setBackground(e.display.getSystemColor(SWT.COLOR_WHITE));
				e.gc.fillRectangle(x, y, width, height);
			}
		};
	}
}
