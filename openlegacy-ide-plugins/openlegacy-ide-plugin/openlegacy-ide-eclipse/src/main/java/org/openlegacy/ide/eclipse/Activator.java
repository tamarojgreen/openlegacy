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

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.forms.FormColors;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.openlegacy.ide.eclipse.actions.EclipseDesignTimeExecuter;
import org.osgi.framework.BundleContext;

import java.io.PrintStream;
import java.net.URL;

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

	// Resource bundle
	private FormColors formColors;
	public static final String ICON_DELETE = "delete";//$NON-NLS-1$
	public static final String ICON_PLUS = "plus";//$NON-NLS-1$

	public static final String IMG_HORIZONTAL = "horizontal"; //$NON-NLS-1$
	public static final String IMG_VERTICAL = "vertical"; //$NON-NLS-1$
	public static final String IMG_FORM_BG = "formBg"; //$NON-NLS-1$
	public static final String IMG_ANNOTATION = "annotation"; //$NON-NLS-1$
	public static final String IMG_TRANSPARENT = "transparent";//$NON-NLS-1$
	public static final String IMG_EDITOR_ERROR = "editorError";//$NON-NLS-1$
	public static final String IMG_EDITOR_NORMAL = "editorNormal";//$NON-NLS-1$
	// fields icons
	public static final String ICON_BOOLEAN = "boolean";//$NON-NLS-1$
	public static final String ICON_DATE = "date";//$NON-NLS-1$
	public static final String ICON_ENUM = "enum";//$NON-NLS-1$
	public static final String ICON_INTEGER = "integer";//$NON-NLS-1$
	public static final String ICON_PART = "part";//$NON-NLS-1$
	public static final String ICON_STRING = "string";//$NON-NLS-1$
	public static final String ICON_VALUES = "values";//$NON-NLS-1$
	public static final String ICON_BIG_INTEGER = "biginteger";//$NON-NLS-1$
	public static final String ICON_TABLE = "table";//$NON-NLS-1$
	public static final String ICON_ACTION = "action";//$NON-NLS-1$
	public static final String ICON_BYTE = "byte";//$NON-NLS-1$
	public static final String ICON_LIST = "list";//$NON-NLS-1$
	// key fields icons
	public static final String ICON_BOOLEAN_KEY = "boolean_key";//$NON-NLS-1$
	public static final String ICON_DATE_KEY = "date_key";//$NON-NLS-1$
	public static final String ICON_ENUM_KEY = "enum_key";//$NON-NLS-1$
	public static final String ICON_INTEGER_KEY = "integer_key";//$NON-NLS-1$	
	public static final String ICON_STRING_KEY = "string_key";//$NON-NLS-1$	
	public static final String ICON_BIG_INTEGER_KEY = "biginteger_key";//$NON-NLS-1$
	public static final String ICON_BYTE_KEY = "byte_key";//$NON-NLS-1$
	public static final String ICON_LIST_KEY = "list_key";//$NON-NLS-1$
	public static final String ICON_VALUES_KEY = "values_key";//$NON-NLS-1$
	// field icons with validation errors
	public static final String ICON_BOOLEAN_ERR = "boolean_err";//$NON-NLS-1$
	public static final String ICON_DATE_ERR = "date_err";//$NON-NLS-1$
	public static final String ICON_ENUM_ERR = "enum_err";//$NON-NLS-1$
	public static final String ICON_INTEGER_ERR = "integer_err";//$NON-NLS-1$
	public static final String ICON_PART_ERR = "part_err";//$NON-NLS-1$
	public static final String ICON_STRING_ERR = "string_err";//$NON-NLS-1$
	public static final String ICON_VALUES_ERR = "values_err";//$NON-NLS-1$
	public static final String ICON_BIG_INTEGER_ERR = "biginteger_err";//$NON-NLS-1$
	public static final String ICON_TABLE_ERR = "table_err";//$NON-NLS-1$
	public static final String ICON_ACTION_ERR = "action_err";//$NON-NLS-1$
	public static final String ICON_BYTE_ERR = "byte_err";//$NON-NLS-1$
	public static final String ICON_LIST_ERR = "list_err";//$NON-NLS-1$
	// key field icons with validation errors
	public static final String ICON_BOOLEAN_KEY_ERR = "boolean_key_err";//$NON-NLS-1$
	public static final String ICON_DATE_KEY_ERR = "date_key_err";//$NON-NLS-1$
	public static final String ICON_ENUM_KEY_ERR = "enum_key_err";//$NON-NLS-1$
	public static final String ICON_INTEGER_KEY_ERR = "integer_key_err";//$NON-NLS-1$	
	public static final String ICON_STRING_KEY_ERR = "string_key_err";//$NON-NLS-1$	
	public static final String ICON_BIG_INTEGER_KEY_ERR = "biginteger_key_err";//$NON-NLS-1$
	public static final String ICON_BYTE_KEY_ERR = "byte_key_err";//$NON-NLS-1$
	public static final String ICON_LIST_KEY_ERR = "list_key_err";//$NON-NLS-1$
	public static final String ICON_VALUES_KEY_ERR = "values_err";//$NON-NLS-1$
	// entities icons
	public static final String ICON_SCREEN_ENTITY = "screenEntity";//$NON-NLS-1$
	public static final String ICON_RPC_ENTITY = "rpcEntity";//$NON-NLS-1$

	public static final String ICON_ZOOM_IN = "zoomIn";//$NON-NLS-1$
	public static final String ICON_HELP = "help";//$NON-NLS-1$

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
		try {
			if (formColors != null) {
				formColors.dispose();
				formColors = null;
			}
		} finally {
			plugin = null;
			super.stop(context);
		}
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

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		registerImage(reg, ICON_PLUS, "plus.png");//$NON-NLS-1$
		registerImage(reg, ICON_DELETE, "delete.png");//$NON-NLS-1$

		registerImage(reg, IMG_HORIZONTAL, "th_horizontal.gif"); //$NON-NLS-1$
		registerImage(reg, IMG_VERTICAL, "th_vertical.gif"); //$NON-NLS-1$
		registerImage(reg, IMG_FORM_BG, "form_banner.gif"); //$NON-NLS-1$
		registerImage(reg, IMG_ANNOTATION, "annotation_obj.gif"); //$NON-NLS-1$
		registerImage(reg, IMG_TRANSPARENT, "transparent.gif");//$NON-NLS-1$
		registerImage(reg, IMG_EDITOR_ERROR, "openlegacy_err.png");//$NON-NLS-1$
		registerImage(reg, IMG_EDITOR_NORMAL, "openlegacy.png");//$NON-NLS-1$
		registerImage(reg, ICON_ZOOM_IN, "zoom_in.png");//$NON-NLS-1$
		// fields icons
		registerImage(reg, ICON_BOOLEAN, "fields/b.png");//$NON-NLS-1$
		registerImage(reg, ICON_DATE, "fields/d.png");//$NON-NLS-1$
		registerImage(reg, ICON_ENUM, "fields/e.png");//$NON-NLS-1$
		registerImage(reg, ICON_INTEGER, "fields/i.png");//$NON-NLS-1$
		registerImage(reg, ICON_PART, "fields/p.png");//$NON-NLS-1$
		registerImage(reg, ICON_STRING, "fields/s.png");//$NON-NLS-1$
		registerImage(reg, ICON_VALUES, "fields/v.png");//$NON-NLS-1$
		registerImage(reg, ICON_BIG_INTEGER, "fields/bi.png");//$NON-NLS-1$
		registerImage(reg, ICON_TABLE, "fields/t.png");//$NON-NLS-1$
		registerImage(reg, ICON_ACTION, "fields/a.png");//$NON-NLS-1$
		registerImage(reg, ICON_BYTE, "fields/bt.png");//$NON-NLS-1$
		registerImage(reg, ICON_LIST, "fields/l.png");//$NON-NLS-1$
		// key fields icons
		registerImage(reg, ICON_BOOLEAN_KEY, "fields/keys/b.png");//$NON-NLS-1$
		registerImage(reg, ICON_DATE_KEY, "fields/keys/d.png");//$NON-NLS-1$
		registerImage(reg, ICON_ENUM_KEY, "fields/keys/e.png");//$NON-NLS-1$
		registerImage(reg, ICON_INTEGER_KEY, "fields/keys/i.png");//$NON-NLS-1$		
		registerImage(reg, ICON_STRING_KEY, "fields/keys/s.png");//$NON-NLS-1$		
		registerImage(reg, ICON_BIG_INTEGER_KEY, "fields/keys/bi.png");//$NON-NLS-1$		
		registerImage(reg, ICON_BYTE_KEY, "fields/keys/bt.png");//$NON-NLS-1$
		registerImage(reg, ICON_LIST_KEY, "fields/keys/l.png");//$NON-NLS-1$
		registerImage(reg, ICON_VALUES_KEY, "fields/keys/v.png");//$NON-NLS-1$
		// field icons with validation errors
		registerImage(reg, ICON_BOOLEAN_ERR, "fields/err/b.png");//$NON-NLS-1$
		registerImage(reg, ICON_DATE_ERR, "fields/err/d.png");//$NON-NLS-1$
		registerImage(reg, ICON_ENUM_ERR, "fields/err/e.png");//$NON-NLS-1$
		registerImage(reg, ICON_INTEGER_ERR, "fields/err/i.png");//$NON-NLS-1$
		registerImage(reg, ICON_PART_ERR, "fields/err/p.png");//$NON-NLS-1$
		registerImage(reg, ICON_STRING_ERR, "fields/err/s.png");//$NON-NLS-1$
		registerImage(reg, ICON_VALUES_ERR, "fields/err/v.png");//$NON-NLS-1$
		registerImage(reg, ICON_BIG_INTEGER_ERR, "fields/err/bi.png");//$NON-NLS-1$
		registerImage(reg, ICON_TABLE_ERR, "fields/err/t.png");//$NON-NLS-1$
		registerImage(reg, ICON_ACTION_ERR, "fields/err/a.png");//$NON-NLS-1$
		registerImage(reg, ICON_BYTE_ERR, "fields/err/bt.png");//$NON-NLS-1$
		registerImage(reg, ICON_LIST_ERR, "fields/err/l.png");//$NON-NLS-1$
		// key fields icons with validation errors
		registerImage(reg, ICON_BOOLEAN_KEY_ERR, "fields/keys/err/b.png");//$NON-NLS-1$
		registerImage(reg, ICON_DATE_KEY_ERR, "fields/keys/err/d.png");//$NON-NLS-1$
		registerImage(reg, ICON_ENUM_KEY_ERR, "fields/keys/err/e.png");//$NON-NLS-1$
		registerImage(reg, ICON_INTEGER_KEY_ERR, "fields/keys/err/i.png");//$NON-NLS-1$		
		registerImage(reg, ICON_STRING_KEY_ERR, "fields/keys/err/s.png");//$NON-NLS-1$		
		registerImage(reg, ICON_BIG_INTEGER_KEY_ERR, "fields/keys/err/bi.png");//$NON-NLS-1$		
		registerImage(reg, ICON_BYTE_KEY_ERR, "fields/keys/err/bt.png");//$NON-NLS-1$
		registerImage(reg, ICON_LIST_KEY_ERR, "fields/keys/err/l.png");//$NON-NLS-1$
		registerImage(reg, ICON_VALUES_KEY_ERR, "fields/keys/err/v.png");//$NON-NLS-1$
		// entities icons
		registerImage(reg, ICON_SCREEN_ENTITY, "entities/se.png");//$NON-NLS-1$
		registerImage(reg, ICON_RPC_ENTITY, "entities/re.png");//$NON-NLS-1$

		registerImage(reg, ICON_HELP, "help_contents.gif");//$NON-NLS-1$
		super.initializeImageRegistry(reg);
	}

	private static void registerImage(ImageRegistry registry, String key, String fileName) {
		try {
			IPath path = new Path("icons/" + fileName); //$NON-NLS-1$
			URL url = FileLocator.find(Platform.getBundle(PLUGIN_ID), path, null);
			if (url != null) {
				ImageDescriptor desc = ImageDescriptor.createFromURL(url);
				registry.put(key, desc);
			}
		} catch (Exception e) {
		}
	}

	public Image getImage(String key) {
		return getImageRegistry().get(key);
	}

	public FormColors getFormColors(Display display) {
		if (formColors == null) {
			formColors = new FormColors(display);
			formColors.markShared();
		}
		return formColors;
	}
}
