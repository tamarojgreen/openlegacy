package com.openlegacy.enterprise.ide.eclipse.editors.button;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;

/**
 * @author Ivan Bort
 * 
 */
public class SplitButton extends Button {

	private static final int SPLIT_LINE_MARGIN = 4;
	private static final int DROP_DOWN_BUTTON_WIDTH = 20;

	private Point leftTop;
	private Point rightBottom;

	private Menu menu;

	/**
	 * @param parent
	 * @param style
	 */
	public SplitButton(Composite parent) {
		super(parent, SWT.PUSH);
		setText("");//$NON-NLS-1$
		menu = new Menu(getShell(), SWT.POP_UP);
		addListeners();
	}

	/**
	 * 
	 */
	private void addListeners() {
		// add paint listener to draw split part
		super.addPaintListener(new PaintListener() {

			// draw the split line and arrow
			public void paintControl(PaintEvent e) {
				// get previous foreground & background
				Color prevForeground = e.gc.getForeground();
				Color prevBackground = e.gc.getBackground();
				// get current bounds
				Rectangle bounds = getBounds();
				// determining space for drop down button
				leftTop = new Point(e.x + bounds.width - DROP_DOWN_BUTTON_WIDTH, e.y);
				rightBottom = new Point(e.x + bounds.width, e.y + bounds.height);
				// clipping
				e.gc.setClipping(e.x, e.y, e.width, e.height);
				// draw split line
				e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
				e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW));
				e.gc.setLineWidth(1);
				e.gc.drawLine(e.x + bounds.width - DROP_DOWN_BUTTON_WIDTH, e.y + SPLIT_LINE_MARGIN, e.x + bounds.width
						- DROP_DOWN_BUTTON_WIDTH, e.y + bounds.height - SPLIT_LINE_MARGIN);

				e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
				e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW));
				e.gc.drawLine(e.x + bounds.width + 1 - DROP_DOWN_BUTTON_WIDTH, e.y + SPLIT_LINE_MARGIN, e.x + bounds.width + 1
						- DROP_DOWN_BUTTON_WIDTH, e.y + bounds.height - SPLIT_LINE_MARGIN);
				// draw triangle
				e.gc.setForeground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
				e.gc.setBackground(Display.getDefault().getSystemColor(SWT.COLOR_BLACK));
				int[] coords = new int[] { e.x + bounds.width - 15, e.y + bounds.height / 2 - 1, e.x + bounds.width - 8,
						e.y + bounds.height / 2 - 1, e.x + bounds.width - 12, e.y + bounds.height / 2 + 3 };
				e.gc.fillPolygon(coords);
				// set previous foreground & background
				e.gc.setForeground(prevForeground);
				e.gc.setBackground(prevBackground);
			}
		});
		// add listener to handle mouse down action and display drop down list if available
		super.addListener(SWT.MouseDown, new Listener() {

			public void handleEvent(Event event) {
				if (isShowDropDown(event.x, event.y)) {
					Button button = (Button)event.widget;
					Rectangle bounds = button.getBounds();
					Point p = button.toDisplay(bounds.x, bounds.y + bounds.height);
					getMenu().setLocation(p.x - bounds.x, p.y - bounds.y);
					getMenu().setVisible(true);
				}
			}
		});
	}

	@Override
	protected void checkSubclass() {
		// disable check to subclasses
	}

	@Override
	public void setText(String text) {
		if (text != null) {
			super.setText(text + getEmptySpace());
		}
	}

	@Override
	public Menu getMenu() {
		return menu;
	}

	@Override
	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	private static String getEmptySpace() {
		GC gc = new GC(Display.getDefault());
		int advanceWidth = gc.getAdvanceWidth(' ');
		gc.dispose();
		int spaces = (DROP_DOWN_BUTTON_WIDTH + 1) / advanceWidth;
		StringBuilder sb = new StringBuilder();
		while (spaces-- > 0) {
			sb.append(" ");
		}
		return sb.toString();
	}

	private boolean isShowDropDown(int x, int y) {
		return x >= leftTop.x && x <= rightBottom.x && y >= leftTop.y && y <= rightBottom.y;
	}
}
