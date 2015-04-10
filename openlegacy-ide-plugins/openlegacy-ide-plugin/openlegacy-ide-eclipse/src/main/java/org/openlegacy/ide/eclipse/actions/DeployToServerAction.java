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
import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.ide.eclipse.Messages;
import org.openlegacy.ide.eclipse.util.PopupUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * @author Ivan Bort
 * 
 */
public class DeployToServerAction extends AbstractAction {

	private static final String MANAGEMENT_PLUGIN_GROUPID = "com.openlegacy.enterprise.plugins";
	private static final String MANAGEMENT_PLUGIN_ARTIFACTID = "management-plugin";

	@Override
	public void run(IAction action) {
		final IProject project = (IProject) ((TreeSelection) getSelection()).getFirstElement();

		try {
			isDeploymentAvailable(project);
		} catch (OpenLegacyException e) {
			PopupUtil.warn(MessageFormat.format("{0}\n{1}", Messages.getString("warn_direct_deployment_not_supported"),
					e.getMessage()));
			return;
		}

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
				if (StringUtils.equals(configuration.getName(), ".build-light-war") && file != null
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
								DebugPlugin.getDefault().removeDebugEventListener(this);
							}
						}
					}
				});
			} catch (CoreException e) {
				e.printStackTrace();
			}
		} else {
			PopupUtil.warn(Messages.getString("error_cannot_build_light_war"));
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

				String serverURL = MessageFormat.format("http://{0}:{1}", serverName, serverPort);
				try {
					URL url = new URL(MessageFormat.format("{0}/manager/text/deploy?path=/{1}&update=true", serverURL,
							project.getName()));
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
					} else {
						PopupUtil.error(MessageFormat.format(Messages.getString("error_deploying_to_server"), warFileName,
								serverURL, connection.getResponseMessage()));
					}
				} catch (MalformedURLException e) {
					PopupUtil.error(MessageFormat.format(Messages.getString("error_deploying_to_server"), warFileName, serverURL,
							e.getMessage()));
				} catch (IOException e) {
					PopupUtil.error(MessageFormat.format(Messages.getString("error_deploying_to_server"), warFileName, serverURL,
							e.getMessage()));
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
			JSONObject jsonObject = (JSONObject) obj;
			if (StringUtils.equals((String) jsonObject.get("serverName"), serverName)) {
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
			serversList.addAll((JSONArray) obj);
		} else {
			serversList.clear();
		}
		return serversList;
	}

	private boolean isDeploymentAvailable(IProject project) throws OpenLegacyException {
		// check if src\main\webapp\WEB-INF\openlegacy.management.license file exist
		IFile licenseFile = project.getFile("src/main/webapp/WEB-INF/openlegacy.management.license");
		if (!licenseFile.exists()) {
			throw new OpenLegacyException("License file doesn't exist at src/main/webapp/WEB-INF");
		}
		// check if management-plugin dependency exist in pom.xml
		IFile pomFile = project.getFile("pom.xml");
		if (!pomFile.exists()) {
			throw new OpenLegacyException("pom.xml file doesn't exist at root of project");
		}
		try {
			DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = builder.parse(pomFile.getLocation().toFile());
			document.getDocumentElement().normalize();

			boolean isManagementPluginDependencyExist = false;

			NodeList list = document.getElementsByTagName("dependency");
			for (int i = 0; i < list.getLength(); i++) {
				Node item = list.item(i);
				if (item.getNodeType() == Node.ELEMENT_NODE) {
					Element el = (Element) item;
					String groupId = el.getElementsByTagName("groupId").item(0).getTextContent();
					String artifactId = el.getElementsByTagName("artifactId").item(0).getTextContent();
					if (StringUtils.equals(MANAGEMENT_PLUGIN_GROUPID, groupId)
							&& StringUtils.equals(MANAGEMENT_PLUGIN_ARTIFACTID, artifactId)) {
						isManagementPluginDependencyExist = true;
						break;
					}
				}
			}
			if (!isManagementPluginDependencyExist) {
				throw new OpenLegacyException("pom.xml file doesn't contain management-plugin dependency");
			}
		} catch (IOException e) {
			throw new OpenLegacyException(e);
		} catch (ParserConfigurationException e) {
			throw new OpenLegacyException(e);
		} catch (SAXException e) {
			throw new OpenLegacyException(e);
		}
		return true;
	}
}
