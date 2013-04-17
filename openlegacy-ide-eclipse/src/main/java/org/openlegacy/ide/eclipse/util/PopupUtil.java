/*******************************************************************************
 * Copyright (c) 2012 OpenLegacy Inc.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     OpenLegacy Inc. - initial API and implementation
 *******************************************************************************/
package org.openlegacy.ide.eclipse.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.openlegacy.ide.eclipse.Messages;

public class PopupUtil {

	private static final String MESSAGE_DIALOG_TITLE = org.openlegacy.ide.eclipse.Messages.getString("console_title_openlegacy");

	private final static Log logger = LogFactory.getLog(PopupUtil.class);

	public static void message(final String message) {
		message(message, MESSAGE_DIALOG_TITLE, true);
	}

	private static Display getDisplay() {
		Display display = null;
		if (PlatformUI.isWorkbenchRunning()) {
			display = PlatformUI.getWorkbench().getDisplay();
		}
		return display;
	}

	public static void message(final String message, final String title, boolean writeToLog) {
		if (writeToLog) {
			logger.info(message);
		}
		getDisplay().asyncExec(new Runnable() {

			public void run() {
				Shell shell = new Shell();
				String theTitle = title;
				if (theTitle == null) {
					theTitle = MESSAGE_DIALOG_TITLE;
				}
				MessageDialog.openInformation(shell, theTitle, message);
			}
		});

	}

	public static boolean confirm(String message) {
		Shell shell = new Shell();
		return MessageDialog.openConfirm(shell, MESSAGE_DIALOG_TITLE, message);
	}

	public static void inform(String message) {
		Shell shell = new Shell();
		MessageDialog.openInformation(shell, MESSAGE_DIALOG_TITLE, message);
	}

	/**
	 * Informs the user of the specified message and shows a check box for "do not show this message again" returns true if the
	 * user selected NOT to show message again. and false otherwise.
	 * 
	 * @param message
	 *            - the message we would like to inform the user of.
	 * 
	 * @return whether the user would like to see this message again. <value>true</value> if the user no longer wants to see this
	 *         message; <value>false</value> otherwise.
	 */
	public static boolean informDoNotShowThisMessageAgain(String message) {
		Shell shell = new Shell();
		DoNotShowThisMessageAgainMessageDialog diag = new DoNotShowThisMessageAgainMessageDialog(shell, MESSAGE_DIALOG_TITLE,
				message);
		return diag.openInfoWithShowThisMessageAgain();
	}

	public static boolean question(String message) {
		return question(message, MESSAGE_DIALOG_TITLE);
	}

	public static boolean question(String message, String title) {
		Shell shell = new Shell();
		return MessageDialog.openQuestion(shell, title, message);

	}

	public static void error(final String message) {
		error(message, true);
	}

	public static void error(final String message, boolean writeToLog) {
		error(message, MESSAGE_DIALOG_TITLE, writeToLog);
	}

	public static void error(final String message, final String title) {
		error(message, title, true);
	}

	public static void error(final String message, final String title, boolean writeToLog) {
		if (writeToLog) {
			logger.error(message);
		}
		getDisplay().asyncExec(new Runnable() {

			public void run() {
				Shell shell = new Shell();
				MessageDialog.openError(shell, title, message);
			}
		});

	}

	public static void exception(final Throwable ex) {
		exception(ex.getLocalizedMessage(), ex);
	}

	public static void exception(final String message, final Throwable ex) {
		exception(message, ex, true);
	}

	public static void exception(final String message, final Throwable ex, final boolean writeToLog) {

		getDisplay().asyncExec(new Runnable() {

			public void run() {
				Shell shell = new Shell();
				ErrorDialog.openError(shell, MESSAGE_DIALOG_TITLE, message, ex);
			}
		});

	}

	public static void warn(final String message) {
		warn(message, true);
	}

	public static void warn(final String message, boolean writeToLog, final String title) {
		if (writeToLog) {
			logger.warn(message);
		}
		getDisplay().asyncExec(new Runnable() {

			public void run() {
				Shell shell = new Shell();
				MessageDialog.openWarning(shell, title, message);
			}
		});
	}

	public static void warn(final String message, boolean writeToLog) {
		warn(message, writeToLog, MESSAGE_DIALOG_TITLE);
	}

	private static class ErrorDialog extends MessageDialog {

		private final int DETAILS_BUTTON_ID = 1;
		private static final int MAX_TEXT_LINE_COUNT = 15;

		private Throwable m_detail;
		private Text text;

		public static void openError(Shell shell, String title, String message, Throwable detail) {
			new ErrorDialog(shell, title, message, detail).open();
		}

		private ErrorDialog(Shell shell, String title, String message, Throwable detail) {
			super(shell, title, null, message, ERROR, new String[] { IDialogConstants.OK_LABEL,
					IDialogConstants.SHOW_DETAILS_LABEL }, 0);

			m_detail = detail;
			setShellStyle(getShellStyle() | SWT.APPLICATION_MODAL | SWT.RESIZE);
		}

		@Override
		protected void buttonPressed(int buttonId) {
			if (buttonId == DETAILS_BUTTON_ID) {
				toggleDetailsArea();
			} else {
				setReturnCode(buttonId);
				close();
			}
		}

		private void toggleDetailsArea() {
			Point windowSize = getShell().getSize();
			Point oldSize = getContents().computeSize(SWT.DEFAULT, SWT.DEFAULT);

			if (text != null) {
				text.dispose();
				text = null;
				getButton(DETAILS_BUTTON_ID).setText(IDialogConstants.SHOW_DETAILS_LABEL);
			} else {
				createDropDownText((Composite)getContents());
				getButton(DETAILS_BUTTON_ID).setText(IDialogConstants.HIDE_DETAILS_LABEL);
			}

			Point newSize = getContents().computeSize(SWT.DEFAULT, SWT.DEFAULT);
			getShell().setSize(new Point(windowSize.x, windowSize.y + (newSize.y - oldSize.y)));
		}

		protected void createDropDownText(Composite parent) {
			text = new Text(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
			text.setFont(parent.getFont());
			text.setText(m_detail.toString());

			GridData data = new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.GRAB_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL
					| GridData.GRAB_VERTICAL);
			data.heightHint = text.getLineHeight() * Math.min(MAX_TEXT_LINE_COUNT, text.getLineCount() + 2);
			data.horizontalSpan = 2;
			text.setLayoutData(data);
		}
	}

	//
	private static class DoNotShowThisMessageAgainMessageDialog extends MessageDialog {

		private static final String DO_NOT_SHOW_THIS_MESSAGE_AGAIN = Messages.getString("label_do_not_show_again");

		public static final int DO_NOT_SHOW_AGAIN = 1;
		public static final int CONTINUE_TO_SHOW = 0;

		private Button btnDoNotShowMessage;
		protected boolean doNotShowAgain;

		private DoNotShowThisMessageAgainMessageDialog(Shell shell, String title, String message) {
			super(shell, title, null, // accept
					// the
					// default
					// window
					// icon
					message, INFORMATION, new String[] { IDialogConstants.OK_LABEL }, 0);
			setShellStyle(getShellStyle() | SWT.APPLICATION_MODAL | SWT.RESIZE);
		}

		@Override
		protected Control createCustomArea(Composite parent) {
			btnDoNotShowMessage = new Button(parent, SWT.CHECK);
			btnDoNotShowMessage.setText(DO_NOT_SHOW_THIS_MESSAGE_AGAIN);
			btnDoNotShowMessage.setSelection(false);
			btnDoNotShowMessage.addSelectionListener(new SelectionListener() {

				public void widgetSelected(SelectionEvent e) {
					doNotShowAgain = btnDoNotShowMessage.getSelection();
				}

				public void widgetDefaultSelected(SelectionEvent e) {
					widgetSelected(e);
				}
			});
			return btnDoNotShowMessage;
		}

		@Override
		public int open() {
			super.open();
			return doNotShowAgain ? DO_NOT_SHOW_AGAIN : CONTINUE_TO_SHOW;
		}

		/**
		 * Opens the message dialog and returns whether the user toggled the show message button. returns <value>true</value> if
		 * the user no longer wants to see this message; <value>false</value> otherwise.
		 * 
		 * @return <value>true</value> if the user no longer wants to see this message; <value>false</value> otherwise.
		 */
		public boolean openInfoWithShowThisMessageAgain() {
			open();
			return doNotShowAgain;
		}
	}
}
