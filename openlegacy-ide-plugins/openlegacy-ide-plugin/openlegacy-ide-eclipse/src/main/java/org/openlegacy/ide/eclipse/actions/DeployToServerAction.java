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
import org.drools.util.codec.Base64;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IDebugEventSetListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.window.Window;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.util.PopupUtil;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

/**
 * @author Ivan Bort
 * 
 */
public class DeployToServerAction extends AbstractAction {

	@Override
	public void run(IAction action) {
		final IProject project = (IProject)((TreeSelection)getSelection()).getFirstElement();

		final DeployToServerDialog dialog = new DeployToServerDialog(getShell(), loadServersList(project));
		int returnCode = dialog.open();
		if (returnCode != Window.OK) {
			return;
		}

		// check <packaging> value in pom.xml
		if (!EclipseDesignTimeExecuter.instance().isSupportDirectDeployment(project)) {
			PopupUtil.warn(Messages.getString("warn_direct_deployment_not_supported"));
			return;
		}

		ILaunchConfiguration launchConfiguration = null;
		ILaunchManager lm = DebugPlugin.getDefault().getLaunchManager();
		ILaunchConfigurationType type = lm.getLaunchConfigurationType("org.eclipse.m2e.Maven2LaunchConfigurationType");
		try {
			ILaunchConfiguration[] configurations = lm.getLaunchConfigurations(type);
			for (ILaunchConfiguration configuration : configurations) {
				IFile file = configuration.getFile();
				if (StringUtils.equals(configuration.getName(), "build-light-war") && file != null
						&& file.getProject().equals(project)) {
					launchConfiguration = configuration;
					break;
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
		}

		if (launchConfiguration != null) {
			try {
				ILaunch launch = launchConfiguration.launch(ILaunchManager.RUN_MODE, null);
				final IProcess launchProcess = launch.getProcesses()[0];

				DebugPlugin.getDefault().addDebugEventListener(new IDebugEventSetListener() {

					@Override
					public void handleDebugEvents(DebugEvent[] events) {
						for (DebugEvent debugEvent : events) {
							if (debugEvent.getKind() == DebugEvent.TERMINATE && launchProcess.isTerminated()) {
								performDirectDeploy(project, dialog.getServerName(), dialog.getServerPort(),
										dialog.getUserName(), dialog.getPassword());
							}
						}
					}
				});
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}

	}

	private static void performDirectDeploy(final IProject project, final String serverName, final String serverPort,
			final String userName, final String password) {
		Job job = new Job(MessageFormat.format(Messages.getString("job_deploying_to_server"), serverName)) {

			@SuppressWarnings("resource")
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask(Messages.getString("deploying"), 2);
				monitor.worked(1);
				String warFileName = MessageFormat.format("{0}.war", project.getName());
				File warFile = new File(project.getFile("target/" + warFileName).getLocationURI());
				if (!warFile.exists()) {
					monitor.done();
					return Status.CANCEL_STATUS;
				}

				try {
					String serverURL = MessageFormat.format("http://{0}:{1}", serverName, serverPort);
					URL url = new URL(MessageFormat.format("{0}/manager/text/deploy?path=/{1}&update=true", serverURL,
							project.getName()));
					HttpURLConnection connection = (HttpURLConnection)url.openConnection();
					connection.setDoInput(true);
					connection.setDoOutput(true);
					connection.setRequestMethod("PUT");
					connection.setRequestProperty("Content-Type", "multipart/form-data");

					String authString = MessageFormat.format("{0}:{1}", userName, password);
					byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
					connection.setRequestProperty("Authorization", "Basic " + new String(authEncBytes));

					BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
					BufferedInputStream bis = new BufferedInputStream(new FileInputStream(warFile));
					int i;
					// read byte by byte until end of stream
					while ((i = bis.read()) >= 0) {
						bos.write(i);
					}
					bos.flush();
					bos.close();

					int responseCode = connection.getResponseCode();
					if (responseCode == 200) {
						saveServer(project, serverName, serverPort, userName);
						PopupUtil.message(MessageFormat.format(Messages.getString("message_success_deploy_to_server"),
								warFileName, serverURL));
					} else if (responseCode == 401) {
						PopupUtil.warn(Messages.getString("warn_user_not_authorized"));
					}
				} catch (MalformedURLException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

				monitor.done();
				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

	@SuppressWarnings("unchecked")
	private static void saveServer(IProject project, String serverName, String serverPort, String userName) {
		JSONArray serversList = loadServersList(project);
		// modify existing
		for (Object obj : serversList) {
			JSONObject jsonObject = (JSONObject)obj;
			if (StringUtils.equals((String)jsonObject.get("serverName"), serverName)) {
				jsonObject.put("serverPort", serverPort);
				jsonObject.put("userName", userName);
				EclipseDesignTimeExecuter.instance().savePreference(project, "SERVERS_LIST", serversList.toJSONString());
				return;
			}
		}
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("serverName", serverName);
		jsonObject.put("serverPort", serverPort);
		jsonObject.put("userName", userName);
		serversList.add(jsonObject);
		EclipseDesignTimeExecuter.instance().savePreference(project, "SERVERS_LIST", serversList.toJSONString());
	}

	@SuppressWarnings("unchecked")
	private static JSONArray loadServersList(IProject project) {
		JSONArray serversList = new JSONArray();
		// get servers list
		String jsonServersList = EclipseDesignTimeExecuter.instance().getPreference(project, "SERVERS_LIST");
		if (!StringUtils.isEmpty(jsonServersList)) {
			Object obj = JSONValue.parse(jsonServersList);
			serversList.clear();
			serversList.addAll((JSONArray)obj);
		} else {
			serversList.clear();
		}
		return serversList;
	}

}
