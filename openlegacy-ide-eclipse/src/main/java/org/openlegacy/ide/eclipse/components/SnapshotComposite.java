package org.openlegacy.ide.eclipse.components;

import org.eclipse.swt.SWT;
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
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.render.DefaultTerminalSnapshotImageRenderer;
import org.openlegacy.terminal.render.TerminalSnapshotImageRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class SnapshotComposite extends Canvas {

	private TerminalSnapshot terminalSnapshot;

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
		setBackground(new Color(Display.getCurrent(), new RGB(0x00, 0x00, 0x00)));
		GridData gd = new GridData(GridData.FILL_HORIZONTAL, GridData.FILL_VERTICAL, true, true);
		gd.widthHint = 850;
		gd.heightHint = 480;
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;

		setLayoutData(gd);
		setLayout(gridLayout);

		addPaintListener(new PaintListener() {

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
			}
		});

	}

	public void setSnapshot(TerminalSnapshot terminalSnapshot) {
		this.terminalSnapshot = terminalSnapshot;
		redraw();
	}

}
