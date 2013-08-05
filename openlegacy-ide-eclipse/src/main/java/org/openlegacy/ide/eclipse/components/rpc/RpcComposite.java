package org.openlegacy.ide.eclipse.components.rpc;

import org.eclipse.swt.SWT;
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
import org.eclipse.swt.widgets.Shell;
import org.openlegacy.ide.eclipse.components.ImageComposite;
import org.openlegacy.terminal.render.DefaultRpcImageRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class RpcComposite extends ImageComposite {

	private Canvas canvas;
	private Image defaultImage = null;
	private String source;

	private boolean isScalable = false;
	private double scale = 1.0d;

	private DefaultRpcImageRenderer renderer = new DefaultRpcImageRenderer();

	public RpcComposite(Composite parent, String source) {
		super(parent);
		this.source = source;
		this.generateDefaultImage();
		initialize();
	}

	public RpcComposite(Composite parent) {
		this(parent, null);
	}

	private void generateDefaultImage() {
		if (source == null) {
			return;
		}
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		renderer.render(source, baos);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		defaultImage = new Image(getShell().getDisplay(), bais);

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

		GridData labelGd = new GridData();
		labelGd.verticalAlignment = GridData.BEGINNING;

		this.canvas.addPaintListener(this.getRectanglePaintListener());
	}

	private PaintListener getRectanglePaintListener() {
		return new PaintListener() {

			public void paintControl(PaintEvent event) {
				drawImage(defaultImage, event.gc);
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

	public void setIsScalable(boolean isScalable) {
		this.isScalable = isScalable;
	}

	public void setSource(String source) {

		if ((source != null) && (!source.equals(this.source))) {
			this.source = source;
			this.generateDefaultImage();
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

				}
			}
		});
		shell.open();
	}
}
