/*******************************************************************************
 * Copyright (c) 2014 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/

package org.openlegacy.ide.eclipse.actions;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.openlegacy.ide.eclipse.Messages;

/**
 * @author Ivan Bort
 * 
 */
public class DeployToServerDialog extends TitleAreaDialog {

	private static final int DIALOG_HEIGHT = 170;

	private static final int DEFAULT_LABEL_WIDTH = 100;
	private static final int DEFAULT_WIDTH = 200;

	// private IProject project;

	private JSONArray serversList = new JSONArray();

	private CCombo snCombo;

	private Text spText;

	private Text uText;

	private Text pText;

	private boolean updatingControls = false;

	private String serverName;

	private String serverPort;

	private String userName;

	private String password;

	@SuppressWarnings("unchecked")
	protected DeployToServerDialog(Shell parentShell, JSONArray serversList) {
		super(parentShell);
		// this.project = project;
		// serversList.addAll(loadServersList(project));
		this.serversList.addAll(serversList);
	}

	@Override
	public void create() {
		super.create();
		setTitle(Messages.getString("title_ol_deploy_to_server"));
		setMessage(Messages.getString("title_ol_deploy_to_server_message"));
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, Messages.getString("btn_deploy"), true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	public boolean isHelpAvailable() {
		return false;
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite)super.createDialogArea(parent);

		Composite container = new Composite(area, SWT.NONE);

		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.marginLeft = 85;
		gl.marginTop = 30;
		container.setLayout(gl);
		GridData gd = new GridData();
		gd.heightHint = DIALOG_HEIGHT;
		container.setLayoutData(gd);

		createPropertiesControls(container);

		return area;
	}

	private void createPropertiesControls(Composite container) {
		// create row for "Server name/IP"
		Label snLabel = new Label(container, SWT.NONE);
		snLabel.setText(Messages.getString("label_server_name"));
		GridData gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		gd.widthHint = DEFAULT_LABEL_WIDTH;
		snLabel.setLayoutData(gd);

		snCombo = new CCombo(container, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = DEFAULT_WIDTH + 7;
		snCombo.setLayoutData(gd);

		for (Object obj : serversList) {
			JSONObject jsonObject = (JSONObject)obj;
			snCombo.add((String)jsonObject.get("serverName"));
		}

		snCombo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				CCombo combo = (CCombo)e.widget;
				for (Object obj : serversList) {
					JSONObject jsonObject = (JSONObject)obj;
					if (StringUtils.equals((String)jsonObject.get("serverName"), combo.getText())) {
						updatingControls = true;
						spText.setText((String)jsonObject.get("serverPort"));
						uText.setText((String)jsonObject.get("userName"));
						updatingControls = false;
						break;
					}
				}
			}

		});

		// create row for "Server port"
		Label spLabel = new Label(container, SWT.NONE);
		spLabel.setText(Messages.getString("label_server_port"));
		gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		gd.widthHint = DEFAULT_LABEL_WIDTH;
		spLabel.setLayoutData(gd);

		spText = new Text(container, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = DEFAULT_WIDTH;
		spText.setLayoutData(gd);
		spText.addVerifyListener(new VerifyListener() {

			@Override
			public void verifyText(VerifyEvent e) {
				if (updatingControls) {
					return;
				}
				switch (e.keyCode) {
					case SWT.BS:
					case SWT.DEL:
					case SWT.HOME:
					case SWT.END:
					case SWT.ARROW_LEFT:
					case SWT.ARROW_RIGHT:
						return;
				}

				if (!Character.isDigit(e.character)) {
					e.doit = false; // disallow the action
				}
			}
		});

		// create row for "User"
		Label uLabel = new Label(container, SWT.NONE);
		uLabel.setText(Messages.getString("lable_user_name"));
		gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		gd.widthHint = DEFAULT_LABEL_WIDTH;
		uLabel.setLayoutData(gd);

		uText = new Text(container, SWT.BORDER);
		gd = new GridData();
		gd.widthHint = DEFAULT_WIDTH;
		uText.setLayoutData(gd);

		// create row for "Password"
		Label pLabel = new Label(container, SWT.NONE);
		pLabel.setText(Messages.getString("label_password"));
		gd = new GridData();
		gd.horizontalAlignment = SWT.RIGHT;
		gd.widthHint = DEFAULT_LABEL_WIDTH;
		pLabel.setLayoutData(gd);

		pText = new Text(container, SWT.PASSWORD | SWT.BORDER);
		gd = new GridData();
		gd.widthHint = DEFAULT_WIDTH;
		pText.setLayoutData(gd);
	}

	@Override
	protected void okPressed() {
		serverName = snCombo.getText();
		serverPort = spText.getText();
		userName = uText.getText();
		password = pText.getText();

		// performDirectDeploy(project, serverName, serverPort, userName, password);

		super.okPressed();
	}

	public String getServerName() {
		return serverName;
	}

	public String getServerPort() {
		return serverPort;
	}

	public String getUserName() {
		return userName;
	}

	public String getPassword() {
		return password;
	}

	// public void performDirectDeploy(final IProject project, final String serverName, final String serverPort,
	// final String userName, final String password) {
	// Job job = new Job(MessageFormat.format(Messages.getString("job_deploying_to_server"), serverName)) {
	//
	// @SuppressWarnings("resource")
	// @Override
	// protected IStatus run(IProgressMonitor monitor) {
	// monitor.beginTask(Messages.getString("deploying"), 2);
	// monitor.worked(1);
	// String warFileName = MessageFormat.format("{0}.war", project.getName());
	// File warFile = new File(project.getFile("target/" + warFileName).getLocationURI());
	// if (!warFile.exists()) {
	// monitor.done();
	// return Status.CANCEL_STATUS;
	// }
	//
	// try {
	// String serverURL = MessageFormat.format("http://{0}:{1}", serverName, serverPort);
	// URL url = new URL(MessageFormat.format("{0}/manager/text/deploy?path=/{1}&update=true", serverURL,
	// project.getName()));
	// HttpURLConnection connection = (HttpURLConnection)url.openConnection();
	// connection.setDoInput(true);
	// connection.setDoOutput(true);
	// connection.setRequestMethod("PUT");
	// connection.setRequestProperty("Content-Type", "multipart/form-data");
	//
	// String authString = MessageFormat.format("{0}:{1}", userName, password);
	// byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
	// connection.setRequestProperty("Authorization", "Basic " + new String(authEncBytes));
	//
	// BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
	// BufferedInputStream bis = new BufferedInputStream(new FileInputStream(warFile));
	// int i;
	// // read byte by byte until end of stream
	// while ((i = bis.read()) >= 0) {
	// bos.write(i);
	// }
	// bos.flush();
	// bos.close();
	//
	// int responseCode = connection.getResponseCode();
	// if (responseCode == 200) {
	// saveServer(project, serverName, serverPort, userName);
	// PopupUtil.message(MessageFormat.format(Messages.getString("message_success_deploy_to_server"),
	// warFileName, serverURL));
	// } else if (responseCode == 401) {
	// PopupUtil.warn(Messages.getString("warn_user_not_authorized"));
	// }
	// } catch (MalformedURLException e) {
	// e.printStackTrace();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// monitor.done();
	// return Status.OK_STATUS;
	// }
	// };
	// job.schedule();
	// }

	// @SuppressWarnings("unchecked")
	// public static JSONArray loadServersList(IProject project) {
	// JSONArray serversList = new JSONArray();
	// // get servers list
	// String jsonServersList = EclipseDesignTimeExecuter.instance().getPreference(project, "SERVERS_LIST");
	// if (!StringUtils.isEmpty(jsonServersList)) {
	// Object obj = JSONValue.parse(jsonServersList);
	// serversList.clear();
	// serversList.addAll((JSONArray)obj);
	// } else {
	// serversList.clear();
	// }
	// return serversList;
	// }

	// @SuppressWarnings("unchecked")
	// public void saveServer(IProject project, String serverName, String serverPort, String userName) {
	// JSONArray serversList = loadServersList(project);
	// // modify existing
	// for (Object obj : serversList) {
	// JSONObject jsonObject = (JSONObject)obj;
	// if (StringUtils.equals((String)jsonObject.get("serverName"), serverName)) {
	// jsonObject.put("serverPort", serverPort);
	// jsonObject.put("userName", userName);
	// EclipseDesignTimeExecuter.instance().savePreference(project, "SERVERS_LIST", serversList.toJSONString());
	// return;
	// }
	// }
	// JSONObject jsonObject = new JSONObject();
	// jsonObject.put("serverName", serverName);
	// jsonObject.put("serverPort", serverPort);
	// jsonObject.put("userName", userName);
	// serversList.add(jsonObject);
	// EclipseDesignTimeExecuter.instance().savePreference(project, "SERVERS_LIST", serversList.toJSONString());
	// }

}
