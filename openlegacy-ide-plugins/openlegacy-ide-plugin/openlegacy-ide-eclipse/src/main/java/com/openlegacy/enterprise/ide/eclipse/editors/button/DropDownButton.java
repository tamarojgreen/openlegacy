package com.openlegacy.enterprise.ide.eclipse.editors.button;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;

/**
 * @author Ivan Bort
 * 
 */
public class DropDownButton extends Button {

	private Menu menu;

	public DropDownButton(Composite parent) {
		super(parent, SWT.PUSH);
		setText("");//$NON-NLS-1$
		menu = new Menu(getShell(), SWT.POP_UP);
		super.addSelectionListener(getDefaultSelectionListener());
	}

	@Override
	protected void checkSubclass() {
		// disable check to subclasses
	}

	@Override
	public Menu getMenu() {
		return menu;
	}

	@Override
	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	/**
	 * Disabled. Cannot override default selection listener.
	 */
	@Deprecated
	@Override
	public void addSelectionListener(SelectionListener listener) {}

	private SelectionListener getDefaultSelectionListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				Rectangle bounds = getBounds();
				Point p = toDisplay(bounds.x, bounds.y + bounds.height);
				getMenu().setLocation(p.x - bounds.x, p.y - bounds.y);
				getMenu().setVisible(true);
			}

		};
	}

}
