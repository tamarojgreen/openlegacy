package com.openlegacy.enterprise.ide.eclipse.views;

import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenEntityModel;

import org.eclipse.draw2d.IFigure;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.zest.core.viewers.IConnectionStyleProvider;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.render.DefaultTerminalSnapshotImageRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class MapLabelProvider extends LabelProvider implements IConnectionStyleProvider, IEntityStyleProvider {

	@Override
	public Color getNodeHighlightColor(Object entity) {
		return Display.getDefault().getSystemColor(SWT.COLOR_DARK_BLUE);
	}

	@Override
	public Color getBorderColor(Object entity) {
		return new Color(Display.getDefault(), 255, 165, 23);
	}

	@Override
	public Color getBorderHighlightColor(Object entity) {
		return new Color(Display.getDefault(), 255, 165, 23);
	}

	@Override
	public int getBorderWidth(Object entity) {
		return 0;
	}

	@Override
	public Color getBackgroundColour(Object entity) {
		return new Color(Display.getDefault(), 0, 0, 0);
	}

	@Override
	public Color getForegroundColour(Object entity) {
		return Display.getDefault().getSystemColor(SWT.COLOR_WHITE);
	}

	@Override
	public boolean fisheyeNode(Object entity) {
		return true;
	}

	@Override
	public int getConnectionStyle(Object rel) {
		return 1;
	}

	@Override
	public Color getColor(Object rel) {
		return null;
	}

	@Override
	public Color getHighlightColor(Object rel) {
		return null;
	}

	@Override
	public int getLineWidth(Object rel) {
		return 1;
	}

	@Override
	public IFigure getTooltip(Object entity) {
		return null;
	}

	@Override
	public Image getImage(Object element) {
		if (element instanceof ScreenEntityModel) {
			Image image = generateImage(PlatformUI.getWorkbench().getDisplay(),
					((ScreenEntityModel)element).getTerminalSnapshot(), new DefaultTerminalSnapshotImageRenderer());
			return image;
		}

		return null;
	}

	public static Image generateImage(Display display, TerminalSnapshot terminalSnapshot,
			DefaultTerminalSnapshotImageRenderer renderer) {
		if (terminalSnapshot == null) {
			return Display.getDefault().getSystemImage(SWT.ICON_WARNING);
		}
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		renderer.render(terminalSnapshot, baos);
		ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
		Image image = new Image(display, bais);
		double zoom = 1d / 4; // scale to half of the size maintaining aspect
								// ratio
		final int width = image.getBounds().width;
		final int height = image.getBounds().height;
		Image scaledImage = new Image(Display.getDefault(), image.getImageData().scaledTo((int)(width * zoom),
				(int)(height * zoom)));
		return scaledImage;
	}

	@Override
	public String getText(Object element) {
		if (element instanceof ScreenEntityModel) {
			ScreenEntityModel screen = (ScreenEntityModel)element;
			return screen.getName();
		}
		return "";
	}
}
