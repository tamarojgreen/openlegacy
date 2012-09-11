package org.openlegacy.ide.eclipse.preview;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class PreviewCanvas extends Canvas {

	private Image image;
	private boolean stayClosed = false;
	protected boolean showingEnlargedImage = false;

	public PreviewCanvas(Composite parent, int style) {
		super(parent, style);
		initialize();
	}

	private void initialize() {
		addPaintListener(new org.eclipse.swt.events.PaintListener() {

			@Override
			public void paintControl(org.eclipse.swt.events.PaintEvent e) {
				if (isVisible()) {
					drawImage(e.gc);
				}
			}
		});
		// enlarge image on mouse click
		addListener(SWT.MouseDown, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (isVisible()) {
					if (getDrawingBounds().contains(event.x, event.y)) {
						showEnlargedImage();
					}
				}
			}
		});
	}

	private void drawImage(GC gc) {
		if (image == null) {
			return;
		}
		Rectangle imageBounds = image.getBounds();
		Rectangle drawingBounds = getDrawingBounds();

		gc.drawImage(image, 0, 0, imageBounds.width, imageBounds.height, drawingBounds.x, drawingBounds.y, drawingBounds.width,
				drawingBounds.height);
	}

	private Rectangle getDrawingBounds() {
		Rectangle imageBounds = image.getBounds();
		Rectangle canvasBounds = getBounds();

		double hScale = (double)canvasBounds.width / imageBounds.width;
		double vScale = (double)canvasBounds.height / imageBounds.height;

		double scale = Math.min(1.0d, Math.min(hScale, vScale));

		int width = (int)(imageBounds.width * scale);
		int height = (int)(imageBounds.height * scale);

		int x = (canvasBounds.width - width) / 2;
		int y = (canvasBounds.height - height) / 2;

		return new Rectangle(x, y, width, height);
	}

	private void showEnlargedImage() {
		if (showingEnlargedImage) {
			return;
		}
		if (stayClosed) {
			return;
		}
		if (image == null) {
			return;
		}

		final Rectangle imageBounds = image.getBounds();
		Rectangle canvasBounds = getBounds();

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

			@Override
			public void paintControl(PaintEvent e) {
				if (isVisible()) {
					e.gc.drawImage(image, 1, 1);
					e.gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_BLACK));
					e.gc.drawRectangle(0, 0, imageBounds.width + 1, imageBounds.height + 1);
				}
			}
		});
		// minimize image on mouse clicked
		shell.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDown(MouseEvent e) {
				if (isVisible()) {
					stayClosed = false;
					showingEnlargedImage = false;
					shell.close();
				}
			}
		});
		// minimize image when mouse leaves image bounds
		shell.addListener(SWT.MouseExit, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (isVisible()) {
					stayClosed = false;
					showingEnlargedImage = false;
					shell.close();
				}
			}
		});
		shell.open();
	}

	public void setImage(Image image) {
		this.image = image;
		redraw();
	}
}
