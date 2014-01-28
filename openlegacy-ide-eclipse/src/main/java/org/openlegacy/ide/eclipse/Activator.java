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
package org.openlegacy.ide.eclipse;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.openlegacy.ide.eclipse.actions.EclipseDesignTimeExecuter;
import org.osgi.framework.BundleContext;

import java.io.PrintStream;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.openlegacy.ide.eclipse"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private MessageConsole olConsole;

	private Job newTrailJob;

	/**
	 * The constructor
	 */
	public Activator() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		EclipseDesignTimeExecuter.instance().initialize();

		olConsole = new MessageConsole(Messages.getString("console_title_openlegacy"), null);
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[] { olConsole });
		ConsolePlugin.getDefault().getConsoleManager().showConsoleView(olConsole);

		MessageConsoleStream stream = olConsole.newMessageStream();
		System.setOut(new PrintStream(stream));
		System.setErr(new PrintStream(stream));

		// Appender appender = new EclipseAppender(getLog());
		// appender.setLayout(new PatternLayout("%d{ISO8601} [%t] %-5p > %m (from: %l{1})%n"));
		// // BasicConfigurator.resetConfiguration();
		// BasicConfigurator.configure(appender);

		newTrailJob = new TrailJob();
		newTrailJob.setPriority(Job.SHORT);
		// wait 1 minute to allow eclipse build to run on eclipse start
		newTrailJob.schedule(60000);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in relative path
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	public static Display getUIDisplay() {
		return getDefault().getWorkbench().getDisplay();
	}

	public static IWorkbenchWindow getActiveWorkbenchWindow() {
		return getDefault().getWorkbench().getActiveWorkbenchWindow();
	}
}
