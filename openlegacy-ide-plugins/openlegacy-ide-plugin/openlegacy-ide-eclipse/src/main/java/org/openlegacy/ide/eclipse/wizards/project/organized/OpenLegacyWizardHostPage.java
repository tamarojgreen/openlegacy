package org.openlegacy.ide.eclipse.wizards.project.organized;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.IWizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.openlegacy.designtime.mains.DesignTimeExecuter;
import org.openlegacy.designtime.newproject.organized.NewProjectMetadataRetriever;
import org.openlegacy.designtime.newproject.organized.model.HostType;
import org.openlegacy.ide.eclipse.Messages;

/**
 * @author Ivan Bort
 * 
 */
public class OpenLegacyWizardHostPage extends AbstractOpenLegacyWizardPage {

	private List<HostType> hostTypes = new ArrayList<HostType>();
	private Combo hostTypeCombo;
	private Text hostText;
	private Text hostPortText;
	private Text codePageText;
	private Label hostTypeDescriptionLabel;
	private String m_fileName;
    private String trailFileName;
	private Text trailFileText;
	
	protected OpenLegacyWizardHostPage() {
		super("wizardProviderPage"); //$NON-NLS-1$
		setTitle(Messages.getString("title_ol_project_wizard"));
		setDescription(Messages.getString("info_ol_project_wizard"));
	}

	@Override
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout();
		container.setLayout(layout);
		layout.numColumns = 2;
		layout.verticalSpacing = 9;

		Label label = new Label(container, SWT.NONE);
		label.setText(Messages.getString("label_host_type"));

		hostTypeCombo = new Combo(container, SWT.SINGLE | SWT.READ_ONLY);
		GridData gd = new GridData();
		gd.widthHint = 100;
		hostTypeCombo.setLayoutData(gd);
		hostTypeCombo.setItems(new String[] { "Pending..." });
		hostTypeCombo.select(0);
		hostTypeCombo.addSelectionListener(getDefaultSelectionListener());

		// stub first column in grid layout
		label = new Label(container, SWT.NONE);

		
		hostTypeDescriptionLabel = new Label(container, SWT.NONE);
		gd = new GridData();
		gd.horizontalAlignment = SWT.FILL;
		gd.grabExcessHorizontalSpace = true;
		hostTypeDescriptionLabel.setLayoutData(gd);

		createFileChooser(container);

		label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("label_host_ip"));//$NON-NLS-1$

		hostText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData();
		gd.widthHint = 200;
		hostText.setLayoutData(gd);
		hostText.addModifyListener(getDefaultModifyListener());

		label = new Label(container, SWT.NONE);
		label.setText(Messages.getString("label_host_port"));//$NON-NLS-1$

		hostPortText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData();
		gd.widthHint = 40;
		hostPortText.setLayoutData(gd);
		hostPortText.setText(String.valueOf(DesignTimeExecuter.DEFAULT_PORT));
		hostPortText.addModifyListener(getDefaultModifyListener());

		label = new Label(container, SWT.NULL);
		label.setText(Messages.getString("label_code_page"));

		codePageText = new Text(container, SWT.BORDER | SWT.SINGLE);
		gd = new GridData();
		gd.widthHint = 40;
		codePageText.setLayoutData(gd);
		codePageText.setText(String.valueOf(DesignTimeExecuter.DEFAULT_CODE_PAGE));
		codePageText.addModifyListener(getDefaultModifyListener());

		setControl(container);
		setPageComplete(false);
	}

	private void createFileChooser(Composite parent) {
		Composite chooser = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(3, false);
        chooser.setLayout(layout);		
        GridData layoutData = new GridData(SWT.FILL, SWT.NONE, false, false, 2, 1);
        chooser.setLayoutData(layoutData);
        Label label = new Label(chooser, SWT.NONE);
        
        GridData gd2 = new GridData(SWT.FILL, SWT.CENTER, false, false);
        label.setLayoutData(gd2);
        gd2.horizontalIndent = -5;
        gd2.widthHint = 50;

        label.setText("Trail file:");
        GridData gd1 = new GridData(SWT.FILL, SWT.CENTER, false, false);
        trailFileText = new Text(chooser, SWT.BORDER); //$NON-NLS-1$
        trailFileText.setLayoutData(gd1);
        gd1.widthHint = 200;
        final Button m_changeButton = new Button(chooser, SWT.PUSH);
        m_changeButton.setText("Browse");
        m_changeButton.setSize(IDialogConstants.BUTTON_WIDTH, m_changeButton.getSize().x);
        GridData gd = new GridData(SWT.BEGINNING, SWT.CENTER, false, false);
        int widthHint = 100;
        gd.widthHint = widthHint;
        m_changeButton.setLayoutData(gd);
        m_changeButton.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent evt) {
                String newValue = changePressed(trailFileText);
                if (newValue != null) {
                	trailFileText.setText(newValue);
                }
            }
        });
	}
	
    protected String changePressed(Text m_text) {
    	String defPathStr = m_text.getText();
    	File defPath = new File(defPathStr);
    	String defFileName = null;
        File selectedFile = getFile(defPath, defFileName);

        m_fileName = selectedFile == null ? "" : selectedFile.getAbsolutePath(); //$NON-NLS-1$
        if (selectedFile == null) {
        	m_fileName = ""; //$NON-NLS-1$
			return null;
		}
        return m_fileName;
    }
    
    private File getFile(File startingDirectory, String defFileName) {
    	
    	FileDialog dialog = new FileDialog(getShell(), SWT.NONE);
    	
    	dialog.setText("Select Trail File");

        if (startingDirectory != null) {
			dialog.setFilterPath(startingDirectory.getPath());
		}
        if (defFileName != null) {
        	dialog.setFileName(defFileName);
        }
        String[] extentions = new String[]{"*.xml"};
		dialog.setFilterExtensions(extentions);
        String file = dialog.open();
        if (file != null) {
            file = file.trim();
            if (file.length() > 0) {
				return new File(file);
			}
        }

        return null;
    }
    

	@Override
	public IWizardPage getNextPage() {
		return getWizard().getPage("wizardThemePage");
	}

	@Override
	public boolean canFlipToNextPage() {
		return isPageComplete() && getWizardModel().isProjectSupportTheme();
	}

	@Override
	public void updateControlsData(NewProjectMetadataRetriever retriever) {
		// if dialog was closed before coming here
		if (getControl().isDisposed()) {
			return;
		}
		hostTypes.clear();
		hostTypes.addAll(retriever.getHostTypes());
		checkHostTypes();
	}

	/**
	 * Must be called only from General Page
	 */
	public void updateControlsData(String backendSolution, final String codePage) {
		if (!StringUtils.isEmpty(backendSolution)) {
			checkHostTypes();
			if (!hostTypes.isEmpty()) {
				final List<String> hostNames = new ArrayList<String>();
				for (HostType hostType : hostTypes) {
					if (hostType.getBackendSolution().equals(backendSolution)) {
						hostNames.add(hostType.getDisplayName());
					}
				}
				getControl().getDisplay().syncExec(new Runnable() {

					@Override
					public void run() {
						hostTypeCombo.setItems(hostNames.toArray(new String[] {}));
						hostTypeCombo.select(0);
						hostTypeCombo.notifyListeners(SWT.Selection, new Event());
					}
				});
			}
		}

		if (!StringUtils.isEmpty(codePage)) {
			getControl().getDisplay().syncExec(new Runnable() {

				@Override
				public void run() {
					codePageText.setText(codePage);
				}
			});
		}
	}

	private void updateStatus(String message) {
		setErrorMessage(message);
		setPageComplete(message == null);
	}

	private void checkHostTypes() {
		if (!hostTypes.isEmpty()) {
			getControl().getDisplay().syncExec(new Runnable() {

				@Override
				public void run() {
					updateStatus(Messages.getString("error_new_project_metadata_not_found"));
				}
			});
		}
	}

	private SelectionListener getDefaultSelectionListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				HostType hostType = getSelectedHostType();
				if (hostType != null) {
					hostTypeDescriptionLabel.setText(hostType.getDescription());
				} else {
					hostTypeDescriptionLabel.setText("");
				}
				getWizardModel().update(hostType);
				if (hostTypeCombo.getText().contains("mock-up")) {
					updateStatus(null);
					setControlsEnabled(false);
					return;
				}
				setControlsEnabled(true);
				validateControls();
			}

		};
	}

	private ModifyListener getDefaultModifyListener() {
		return new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (validateControls()) {
					getWizardModel().setHost(hostText.getText());
					getWizardModel().setHostPort(Integer.valueOf(hostPortText.getText()));
					getWizardModel().setCodePage(codePageText.getText());
					getWizardModel().setTrailFilePath(trailFileText.getText());
				} else {
					getWizardModel().setHost("");
					getWizardModel().setHostPort(0);
					getWizardModel().setCodePage("");
					getWizardModel().setTrailFilePath("");
				}
			}
		};
	}

	private void setControlsEnabled(boolean enabled) {
		hostText.setEnabled(enabled);
		hostPortText.setEnabled(enabled);
		codePageText.setEnabled(enabled);
	}

	private boolean validateControls() {
		if (hostText.getText().length() == 0) {
			updateStatus(Messages.getString("error_host_name_not_specified"));
			return false;
		}
		if (hostPortText.getText().length() == 0) {
			updateStatus(Messages.getString("errror_host_port_not_specified"));
			return false;
		}
		if (!NumberUtils.isNumber(hostPortText.getText())) {
			updateStatus(Messages.getString("error_port_not_numeric"));
			return false;
		}
		if (codePageText.getText().length() == 0) {
			updateStatus(Messages.getString("error_code_page_not_specified"));
			return false;
		}
		updateStatus(null);
		return true;
	}

	private HostType getSelectedHostType() {
		for (HostType hostType : hostTypes) {
			if (hostType.getBackendSolution().equals(getWizardModel().getBackendSolution())
					&& hostType.getDisplayName().equals(hostTypeCombo.getText())) {
				return hostType;
			}
		}
		return null;
	}

	/**
	 * @return the text
	 */
	public String getText() {
		return trailFileName;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.trailFileName = text;
	}
}
