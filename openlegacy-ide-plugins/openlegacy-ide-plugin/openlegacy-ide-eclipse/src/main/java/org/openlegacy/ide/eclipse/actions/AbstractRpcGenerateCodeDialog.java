package org.openlegacy.ide.eclipse.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.PluginConstants;

public abstract class AbstractRpcGenerateCodeDialog extends AbstractGenerateModelDialog {

	protected AbstractRpcGenerateCodeDialog(Shell shell, IFile file) {
		super(shell, file);
	}

	private Text navigationText;
	private String navigationValue = "<SIDE MENU NAME TO ACCESS THE PROGRAM>";
	private Text actionText;
	private String actionValue = "<PATH TO INVOKE THE PROGRAM.PGM>";

	@Override
	protected Control createDialogArea(Composite parent) {

		parent = new Composite(parent, SWT.NONE);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;

		GridData gd = new GridData();
		gd.widthHint = 400;
		parent.setLayoutData(gd);
		parent.setLayout(gridLayout);

		parent.getShell().setText(PluginConstants.TITLE);

		Label label = new Label(parent, SWT.NULL);
		label.setText(Messages.getString("label_source_folder"));

		setSourceFolderPathText(new Text(parent, SWT.SINGLE | SWT.BORDER));

		getSourceFolderPathText().setEditable(false);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		getSourceFolderPathText().setLayoutData(gd);
		Button sourceFolderButton = new Button(parent, SWT.NONE);
		sourceFolderButton.setText(Messages.getString("label_browse"));
		sourceFolderButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				handleSourceFolderButtonSelected();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		Label labelPackage = new Label(parent, SWT.NULL);
		labelPackage.setText(Messages.getString("label_package"));
		packageText = new Text(parent, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		getPackageText().setLayoutData(gd);

		getPackageText().addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent paramModifyEvent) {
				packageValue = getPackageText().getText();
			}
		});

		new Label(parent, SWT.NONE);

		Label labelNavigation = new Label(parent, SWT.NULL);
		labelNavigation.setText(Messages.getString("label_navigation"));
		navigationText = new Text(parent, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		getNavigationText().setLayoutData(gd);
		getNavigationText().addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent arg0) {
				navigationValue = getNavigationText().getText();

			}
		});
		new Label(parent, SWT.NONE);

		Label labelAction = new Label(parent, SWT.NULL);
		labelAction.setText(Messages.getString("label_action"));
		actionText = new Text(parent, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		getActionText().setLayoutData(gd);
		getActionText().addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent arg0) {
				actionValue = getActionText().getText();

			}
		});
		new Label(parent, SWT.NONE);
		loadPrefrences();

		// disabled until RPC will support no aspect
		// Button useAjButton = new Button(parent, SWT.CHECK);
		// useAjButton.setText(Messages.getString("label_use_aj"));
		//
		// gd = new GridData(GridData.FILL_HORIZONTAL);
		// gd.widthHint = 400;
		// useAjButton.setLayoutData(gd);
		//
		// useAjButton.addSelectionListener(new SelectionListener() {
		//
		// public void widgetDefaultSelected(SelectionEvent arg0) {
		// setUseAj(true);
		//
		// }
		//
		// public void widgetSelected(SelectionEvent arg0) {
		// setUseAj(!isUseAj());
		// }
		// });
		// useAjButton.setSelection(isUseAj() && isSupportAjGeneration());
		// useAjButton.setEnabled(isSupportAjGeneration());

		// disabled until RPC will generate auto test.
		// Button generateTest = new Button(parent, SWT.CHECK);
		// generateTest.setSelection(isSupportTestGeneration() && isGenerateTest());
		// generateTest.setText(Messages.getString("label_generate_test"));
		//
		// generateTest.setEnabled(isSupportTestGeneration());
		//
		// gd = new GridData(GridData.FILL_HORIZONTAL);
		// gd.widthHint = 400;
		// generateTest.setLayoutData(gd);
		//
		// generateTest.addSelectionListener(new SelectionListener() {
		//
		// public void widgetDefaultSelected(SelectionEvent arg0) {
		// setGenerateTest(true);
		// }
		//
		// public void widgetSelected(SelectionEvent arg0) {
		// setGenerateTest(!isGenerateTest());
		// }
		// });

		// setUseAj(false);
		createDialogSpecific(parent);
		return parent;
	}

	@Override
	protected void createDialogSpecific(Composite parent) {
		// empty to allow override
	}

	public String getNavigationValue() {
		return navigationValue;
	}

	public Text getNavigationText() {
		return navigationText;
	}

	public Text getActionText() {
		return actionText;
	}

	public String getActionValue() {
		return actionValue;
	}

}
