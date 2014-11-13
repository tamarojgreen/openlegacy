package org.openlegacy.ide.eclipse.actions;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IProject;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.openlegacy.EntityDefinition;
import org.openlegacy.designtime.PreferencesConstants;
import org.openlegacy.designtime.UserInteraction;
import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.PluginConstants;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.MessageFormat;

public class ImportSourceDialog extends Dialog implements UserInteraction {

	private static String LEGACY_FILE_PATH = "\\src\\main\\resources\\legacy\\";
	private Text userNameText;
	private Text userPwdText;
	private Text legacyFilenmaeText;
	private Text hostText;
	private IProject project;
	private String workspasePath;
	private String newFileName;

	public String getLocalName() {
		if (newFileName != null) {
			return LEGACY_FILE_PATH + newFileName;
		}
		return null;
	}

	public ImportSourceDialog(Shell parentShell, IProject project, String workspasePath) {
		super(parentShell);
		this.project = project;
		this.workspasePath = workspasePath;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite)super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		parent.getShell().setText(PluginConstants.TITLE);
		container.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout(2, false);
		container.setLayout(layout);

		createUserName(container);
		createUserPwd(container);
		createLegacy(container);
		createHost(container);
		loadPreferences();
		return area;
	}

	private void loadPreferences() {

		EclipseDesignTimeExecuter designtimeExecuter = EclipseDesignTimeExecuter.instance();

		File rpcPropertiesFile = new File(workspasePath + project.getFullPath().toOSString(),
				"/src/main/resources/rpc.properties");
		if (rpcPropertiesFile.exists()) {
			try {
				String rpcPropertiesFileContent = IOUtils.toString(new FileInputStream(rpcPropertiesFile));
				String lines[] = rpcPropertiesFileContent.split("\n");
				for (String line : lines) {
					String parts[] = line.split("=");
					if (parts[0].equals("rpc.host.name")) {
						setHost(parts[1]);
					}
					if (parts[0].equals("rpc.user")) {
						setUserName(parts[1]);
					}
					if (parts[0].equals("rpc.password")) {
						setUserPwd(parts[1]);
					}

				}
			} catch (FileNotFoundException e) {

			} catch (IOException e) {

			}

		} else

		{
			setUserName(designtimeExecuter.getPreference(project, PreferencesConstants.USER_NAME));
			setHost(designtimeExecuter.getPreference(project, PreferencesConstants.HOST_NAME));

		}
		setLegacyFilename(designtimeExecuter.getPreference(project, PreferencesConstants.LEGACY_FILENAME));
	}

	private void savePreferences() {

		EclipseDesignTimeExecuter designtimeExecuter = EclipseDesignTimeExecuter.instance();

		designtimeExecuter.savePreference(project, PreferencesConstants.USER_NAME, getUserName());
		designtimeExecuter.savePreference(project, PreferencesConstants.HOST_NAME, getHost());
		designtimeExecuter.savePreference(project, PreferencesConstants.LEGACY_FILENAME, getLegacyFilename());
	}

	private void createUserName(Composite container) {
		Label lable = new Label(container, SWT.NONE);
		lable.setText(Messages.getString("lable_user_name"));

		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;

		userNameText = new Text(container, SWT.BORDER);
		userNameText.setLayoutData(data);

	}

	private void createUserPwd(Composite container) {
		Label label = new Label(container, SWT.NONE);
		label.setText(Messages.getString("label_password"));

		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		userPwdText = new Text(container, SWT.PASSWORD | SWT.BORDER);
		userPwdText.setLayoutData(data);

	}

	private void createLegacy(Composite container) {
		Label label = new Label(container, SWT.NONE);
		label.setText(Messages.getString("label_legacy_file"));

		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		legacyFilenmaeText = new Text(container, SWT.BORDER);
		legacyFilenmaeText.setLayoutData(data);
	}

	private void createHost(Composite container) {
		Label label = new Label(container, SWT.NONE);
		label.setText(Messages.getString("label_host"));

		GridData data = new GridData();
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		hostText = new Text(container, SWT.BORDER);
		hostText.setLayoutData(data);

	}

	private void importFile() throws OpenLegacyException {

		String dir = workspasePath + project.getFullPath().toOSString() + LEGACY_FILE_PATH;
		newFileName = EclipseDesignTimeExecuter.instance().importFile(dir, getHost(), getUserName(), getUserPwd(),
				getLegacyFilename(), this);
	}

	public String getUserName() {
		return userNameText.getText();
	}

	public void setUserName(String userName) {
		if (userName != null) {
			userNameText.setText(userName);
		}
	}

	public String getUserPwd() {
		return userPwdText.getText();
	}

	public void setUserPwd(String userPwd) {
		if (userPwd != null) {
			userPwdText.setText(userPwd);
		}
	}

	public String getLegacyFilename() {
		return legacyFilenmaeText.getText();
	}

	public void setLegacyFilename(String legacyFilename) {
		if (legacyFilename != null) {
			legacyFilenmaeText.setText(legacyFilename);
		}
	}

	public String getHost() {
		return hostText.getText();
	}

	public void setHost(String host) {
		if (host != null) {
			hostText.setText(host);
		}
	}

	@Override
	protected void okPressed() {
		savePreferences();
		try {
			importFile();
		} catch (OpenLegacyException e) {
			e.printStackTrace();
			MessageDialog.openError(getShell(), PluginConstants.TITLE, Messages.getString("Faild to import file"));
		}
		super.okPressed();
	}

	@Override
	public boolean isOverride(final File file) {
		final Object[] result = new Object[1];
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				result[0] = MessageDialog.openQuestion(getShell(), PluginConstants.TITLE,
						MessageFormat.format(Messages.getString("question_override_file"), file.getName()));
			}
		});

		return (Boolean)result[0];
	}

	@Override
	public void open(File file) {
		// TODO Auto-generated method stub
	}

	@Override
	public void open(File file, EntityDefinition<?> entityDefinition) {
		// TODO Auto-generated method stub
	}

}