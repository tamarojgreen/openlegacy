package org.openlegacy.ide.eclipse.actions;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.openlegacy.ide.eclipse.PluginConstants;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;
import org.openlegacy.terminal.definitions.SimpleScreenEntityDefinition;
import org.openlegacy.terminal.render.DefaultTerminalSnapshotImageRenderer;
import org.openlegacy.terminal.render.TerminalSnapshotImageRenderer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class CustomizeScreenEntityDialog extends Dialog {

	private Text entityNameTxt;
	private ScreenEntityDefinition screenEntityDefinition;

	protected CustomizeScreenEntityDialog(Shell parentShell, ScreenEntityDefinition screenEntityDefinition) {
		super(parentShell);
		this.screenEntityDefinition = screenEntityDefinition;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		parent = new Composite(parent, SWT.NONE);

		parent.getShell().setText(PluginConstants.TITLE + "- Generate screens API");

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		GridData gd = new GridData();
		gd.widthHint = 850;
		gd.heightHint = 480;
		parent.setLayoutData(gd);
		parent.setLayout(gridLayout);

		Label labelPackage = new Label(parent, SWT.NULL);
		labelPackage.setText("Entity Name:");
		entityNameTxt = new Text(parent, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		entityNameTxt.setLayoutData(gd);

		entityNameTxt.setText(screenEntityDefinition.getEntityName());

		// space
		Label label = new Label(parent, SWT.NONE);
		label.setText(" ");

		TerminalSnapshotImageRenderer renderer = new DefaultTerminalSnapshotImageRenderer();
		final ByteArrayOutputStream baos = new ByteArrayOutputStream();
		renderer.render(screenEntityDefinition.getSnapshot(), baos);
		Canvas canvas = new Canvas(parent, SWT.NONE);
		gd = new GridData(GridData.FILL_HORIZONTAL, GridData.FILL_VERTICAL, true, true);
		gd.widthHint = 850;
		gd.heightHint = 480;
		gridLayout = new GridLayout();
		gridLayout.numColumns = 2;

		canvas.setLayoutData(gd);
		canvas.setLayout(gridLayout);

		canvas.addPaintListener(new PaintListener() {

			public void paintControl(PaintEvent e) {
				ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
				Image image = new Image(getShell().getDisplay(), bais);
				e.gc.drawImage(image, 0, 0);
			}
		});

		entityNameTxt.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent paramModifyEvent) {
				((SimpleScreenEntityDefinition)screenEntityDefinition).setEntityName(entityNameTxt.getText());
			}
		});

		new Label(parent, SWT.NONE);
		return parent;
	}
}
