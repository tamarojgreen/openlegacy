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
package org.openlegacy.tools.maven;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.mortbay.jetty.Handler;
import org.mortbay.jetty.Request;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import org.openlegacy.modules.trail.Trail;
import org.openlegacy.modules.trail.TrailUtil;
import org.openlegacy.terminal.TerminalSendAction;
import org.openlegacy.terminal.TerminalSession;
import org.openlegacy.terminal.modules.trail.DefaultTerminalTrail;
import org.openlegacy.terminal.web.render.TerminalSnapshotHtmlRenderer;
import org.openlegacy.terminal.web.render.support.DefaultHttpPostSendActionBuilder;
import org.openlegacy.terminal.web.render.support.DefaultTerminalSnapshotHtmlRenderer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * An embedded terminal session HTML runner. Runs an HTML session within an embedded Jetty and serves ALL requests: get, post, JS
 * and logoff. Loads the maven project application context "applicationContext-test.xml" by default (all OL project has this
 * file), and can override it in the maven launcher.
 * 
 * Properties: org.openlegacy.port - control the embedded server port org.openlegacy.context.file - define the spring context for
 * the embedded session
 * 
 * @goal run
 * @phase process-classes
 * @configurator include-project-dependencies
 * @requiresDependencyResolution compile+runtime
 */
public class SessionRunner extends AbstractMojo {

	private static final String PREFIX = "org.openlegacy.";

	private static final String PORT = PREFIX + "port";
	private static final int defaultPort = 1512; // OL :)

	private static final String CONTEXT_FILE = PREFIX + "context.file";
	private static final String defaultContext = "/META-INF/spring/applicationContext-test.xml";

	protected static final String HEADER_EXPIRES = "Expires";

	private static final String RENDERER_TEMPLATE = "completeHtmlEmulationTemplate.html";

	private static final String LOGOFF_PAGE_CONTENT = "<html><body>SessionEnded. <a href=\"emulation\">Reconnect</a></body></html>";

	private static final String BROWSER_PATH = PREFIX + "browser";

	private TerminalSession terminalSession;
	private ApplicationContext applicationContext;
	private TerminalSnapshotHtmlRenderer terminalHtmlRenderer;

	public void execute() throws MojoExecutionException {
		int port = (Integer)getProperty(PORT, defaultPort);

		Server server = new Server(port);
		Handler handler = new AbstractHandler() {

			public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch)
					throws IOException, ServletException {

				try {
					initApplicationContext();

					String uri = request.getRequestURI();
					if (uri.contains(".js")) {
						handleJsFiles(request, response, uri);
						return;
					}

					if (uri.contains("sequence")) {
						response.getWriter().write(getTerminalSession().getSequence().toString());
						((Request)request).setHandled(true);
						return;
					}

					if (uri.contains("logoff")) {
						handleLogoff(response);
						((Request)request).setHandled(true);
						return;
					}
					String flip = request.getParameter("flip");
					if (flip != null) {
						getTerminalSession().getSnapshot();
						getTerminalSession().flip();
					}
					if (request.getMethod().equalsIgnoreCase("POST")) {
						handlePost(request);
					}
					handleHtmlRendering(request, response);
				} catch (RuntimeException e) {
					e.printStackTrace();
					throw (e);
				}
			}
		};

		server.setHandler(handler);
		try {
			server.start();
			// wait forever until maven is stopped

			openBrowser(port);

			while (true) {
				Thread.sleep(10000);
			}

		} catch (Exception e1) {
			throw (new RuntimeException(e1));
		}

	}

	private void openBrowser(int port) {
		Object browserPath = getProperty(BROWSER_PATH, null);
		if (browserPath != null) {
			try {
				Desktop.getDesktop().browse(new URL("http://localhost:" + port).toURI());
			} catch (Exception e) {
				getLog().warn("Unable to open browser at:" + browserPath);
			}
		}
	}

	private static Object getProperty(String propertyName, Object defaultValue) {
		String propertyValue = System.getProperty(propertyName);
		if (propertyValue != null) {
			return propertyValue;
		}
		return defaultValue;
	}

	private void handleHtmlRendering(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String html = getRenderer().render(getTerminalSession().getSnapshot());
		response.setContentType("text/html;charset=utf-8");
		response.getWriter().write(html);

		response.setStatus(HttpServletResponse.SC_OK);
		((Request)request).setHandled(true);
	}

	private static void handleJsFiles(HttpServletRequest request, HttpServletResponse response, String uri) throws IOException {
		PathMatchingResourcePatternResolver pathResolver = new PathMatchingResourcePatternResolver();
		Resource resource = pathResolver.getResource(uri);
		if (resource == null) {
			throw (new RuntimeException("Unable to find resource:" + uri));
		}
		response.setDateHeader(HEADER_EXPIRES, Long.MAX_VALUE);
		IOUtils.copy(resource.getInputStream(), response.getOutputStream());
		response.setStatus(HttpServletResponse.SC_OK);
		((Request)request).setHandled(true);
	}

	private void initApplicationContext() {
		if (applicationContext == null) {
			String configLocation = (String)getProperty(CONTEXT_FILE, defaultContext);
			applicationContext = new ClassPathXmlApplicationContext("/META-INF/openlegacy-webcomponents-context.xml",
					configLocation);
		}
	}

	private TerminalSession getTerminalSession() {
		if (terminalSession == null) {
			terminalSession = applicationContext.getBean(TerminalSession.class);
			// set unlimited recording
			DefaultTerminalTrail trail = (DefaultTerminalTrail)terminalSession.getModule(Trail.class).getSessionTrail();
			trail.setHistoryCount(null);
		}
		return terminalSession;
	}

	private TerminalSnapshotHtmlRenderer getRenderer() {
		if (terminalHtmlRenderer == null) {
			terminalHtmlRenderer = applicationContext.getBean(TerminalSnapshotHtmlRenderer.class);

		}
		((DefaultTerminalSnapshotHtmlRenderer)terminalHtmlRenderer).setTemplateResourceName(RENDERER_TEMPLATE);
		return terminalHtmlRenderer;
	}

	private void handlePost(HttpServletRequest request) {
		DefaultHttpPostSendActionBuilder sendActionBuilder = applicationContext.getBean(DefaultHttpPostSendActionBuilder.class);
		TerminalSendAction sendAction = sendActionBuilder.buildSendAction(getTerminalSession().getSnapshot(), request);
		getTerminalSession().doAction(sendAction);
	}

	private void handleLogoff(HttpServletResponse response) throws IOException {
		File trailFile = getBean(TrailUtil.class).saveTrail(terminalSession);

		getTerminalSession().disconnect();
		terminalSession = null;
		response.getWriter().write(LOGOFF_PAGE_CONTENT);
		if (trailFile != null) {
			response.getWriter().write(
					MessageFormat.format("<br/><br/>Trail file: {0} <br/>created in:{1}", trailFile.getName(),
							trailFile.getParentFile().getAbsolutePath()));
		}
	}

	private TrailUtil getBean(Class<TrailUtil> class1) {
		return applicationContext.getBean(class1);
	}

}