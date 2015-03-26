package com.openlegacy.enterprise.ide.eclipse.editors.pages.helpers;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.TypesSelectionDialog;
import com.openlegacy.enterprise.ide.eclipse.editors.dialogs.filters.AbstractViewerFilter;
import com.openlegacy.enterprise.ide.eclipse.views.OpenLegacyHelpView;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.ui.util.BusyIndicatorRunnableContext;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.openlegacy.ide.eclipse.Activator;
import org.openlegacy.utils.StringUtil;

import java.text.MessageFormat;
import java.util.Map;

/**
 * @author Ivan Bort
 * 
 */
public class FormRowCreator {

	public static final String ID_KEY = "id.key";//$NON-NLS-1$
	public static final String ID_FULLY_QUALIFIED_NAME = "id.fullyQualifiedName";//$NON-NLS-1$

	private static final int DEFAULT_WIDTH = 200;
	private static final int DEFAULT_INDENT = 10;
	private static final String HELP_VIEW_ID = "com.openlegacy.enterprise.ide.eclipse.views.OpenLegacyHelpView";

	public enum JAVA_DOCUMENTATION_TYPE {
		SCREEN("http://files.openlegacy.org/javadoc/org/openlegacy/annotations/screen/{0}.html#{1}()"), RPC(
				"http://files.openlegacy.org/javadoc/org/openlegacy/annotations/rpc/{0}.html#{1}()"), JPA(
				"http://docs.oracle.com/javaee/7/api/javax/persistence/{0}.html#{1}()"), JAVA_BASICS(
				"http://docs.oracle.com/javase/tutorial/java/nutsandbolts/{0}.html#{1}"), DB(
				"http://files.openlegacy.org/javadoc/org/openlegacy/annotations/db/{0}.html#{1}()");

		private static final long serialVersionUID = 1L;

		private String url;

		private JAVA_DOCUMENTATION_TYPE(String url) {
			this.url = url;
		}

		public String getUrl() {
			return url;
		}
	}

	/**
	 * Create empty row
	 * 
	 * @param toolkit
	 * @param parent
	 * @param span
	 */
	public static void createSpacer(FormToolkit toolkit, Composite parent, int span) {
		Label spacer = toolkit.createLabel(parent, ""); //$NON-NLS-1$
		GridData gd = new GridData();
		gd.horizontalSpan = span;
		spacer.setLayoutData(gd);
	}

	/**
	 * Creates Label and Label
	 * 
	 * @param toolkit
	 * @param parent
	 * @param javaTypeName
	 * @param mapLabels
	 * @param label
	 * @param value
	 */
	public static void createLabelRow(FormToolkit toolkit, Composite parent, Map<String, Label> mapLabels, String labelTitle,
			String text, String key, JAVA_DOCUMENTATION_TYPE helpBaseUrl, String helpPage, String helpAnchor) {
		createLabel(toolkit, parent, labelTitle, helpBaseUrl, helpPage, helpAnchor);
		Label label = toolkit.createLabel(parent, text);
		GridData gd = new GridData();
		gd.widthHint = DEFAULT_WIDTH;
		gd.horizontalIndent = DEFAULT_INDENT;
		label.setLayoutData(gd);
		FontData fontData = label.getFont().getFontData()[0];
		Font font = new Font(label.getDisplay(), new FontData(fontData.getName(), fontData.getHeight(), SWT.BOLD));
		label.setFont(font);
		mapLabels.put(key, label);
	}

	/**
	 * Creates Label and Button$SWT.CHECK controls. Adds created Button to map with key parameter.
	 * 
	 * @param toolkit
	 * @param parent
	 * @param map
	 *            - can be null
	 * @param selectionListener
	 *            - can be null
	 * @param label
	 * @param value
	 * @param key
	 * @param dataKey
	 */
	public static Button createBooleanRow(FormToolkit toolkit, Composite parent, Map<String, Button> map,
			SelectionListener selectionListener, String label, boolean value, String key, JAVA_DOCUMENTATION_TYPE helpBaseUrl,
			String helpPage, String helpAnchor) {

		createLabel(toolkit, parent, label, helpBaseUrl, helpPage, helpAnchor);

		Button button = toolkit.createButton(parent, "", SWT.CHECK);//$NON-NLS-1$
		GridData gd = new GridData();
		gd.widthHint = DEFAULT_WIDTH;
		gd.horizontalIndent = DEFAULT_INDENT;
		button.setLayoutData(gd);
		button.setSelection(value);
		button.setData(ID_KEY, key);
		if (selectionListener != null) {
			button.addSelectionListener(selectionListener);
		}
		if (map != null) {
			map.put(key, button);
		}
		return button;
	}

	/**
	 * Creates Label and Text controls. Adds created Text to map with key parameter. Adds VerifyListener which allows typing
	 * digits only.
	 * 
	 * @param toolkit
	 * @param parent
	 * @param map
	 *            - can be null
	 * @param modifyListener
	 *            - can be null
	 * @param verifyListener
	 *            - can be null
	 * @param label
	 * @param value
	 * @param key
	 * @param dataKey
	 */
	public static Text createIntRow(FormToolkit toolkit, Composite parent, Map<String, Text> map, ModifyListener modifyListener,
			VerifyListener verifyListener, String label, Integer value, String key, JAVA_DOCUMENTATION_TYPE helpBaseUrl,
			String helpPage, String helpAnchor) {

		createLabel(toolkit, parent, label, helpBaseUrl, helpPage, helpAnchor);

		Text text = toolkit.createText(parent, (value != null) ? value.toString() : "", SWT.SINGLE);
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.widthHint = DEFAULT_WIDTH;
		gd.horizontalIndent = DEFAULT_INDENT;
		text.setLayoutData(gd);
		text.setData(ID_KEY, key);
		if (verifyListener != null) {
			text.addVerifyListener(verifyListener);
		}
		if (modifyListener != null) {
			text.addModifyListener(modifyListener);
		}
		if (map != null) {
			map.put(key, text);
		}
		return text;
	}

	/**
	 * Creates Label and Text controls. Adds created Text to map with key parameter. Adds VerifyListener which allows typing
	 * digits and one dot only.
	 * 
	 * @param toolkit
	 * @param parent
	 * @param map
	 *            - can be null
	 * @param modifyListener
	 *            - can be null
	 * @param verifyListener
	 *            - can be null
	 * @param label
	 * @param value
	 * @param key
	 * @param dataKey
	 */
	public static Text createDoubleRow(FormToolkit toolkit, Composite parent, Map<String, Text> map,
			ModifyListener modifyListener, VerifyListener verifyListener, String label, Double value, String key,
			JAVA_DOCUMENTATION_TYPE helpBaseUrl, String helpPage, String helpAnchor) {

		createLabel(toolkit, parent, label, helpBaseUrl, helpPage, helpAnchor);

		Text text = toolkit.createText(parent, (value != null) ? value.toString() : "", SWT.SINGLE);
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.widthHint = DEFAULT_WIDTH;
		gd.horizontalIndent = DEFAULT_INDENT;
		text.setLayoutData(gd);
		text.setData(ID_KEY, key);
		if (verifyListener != null) {
			text.addVerifyListener(verifyListener);
		}
		if (modifyListener != null) {
			text.addModifyListener(modifyListener);
		}
		if (map != null) {
			map.put(key, text);
		}
		return text;
	}

	/**
	 * Creates Label and Text controls. Adds created Text to map with key parameter.
	 * 
	 * @param toolkit
	 * @param parent
	 * @param map
	 *            - can be null
	 * @param modifyListener
	 *            - can be null
	 * @param label
	 * @param value
	 * @param key
	 * @param dataKey
	 */
	public static Text createStringRow(FormToolkit toolkit, Composite parent, Map<String, Text> map,
			ModifyListener modifyListener, String label, String value, String key, JAVA_DOCUMENTATION_TYPE helpBaseUrl,
			String helpPage, String helpAnchor) {

		createLabel(toolkit, parent, label, helpBaseUrl, helpPage, helpAnchor);

		Text text = toolkit.createText(parent, value, SWT.SINGLE | SWT.BORDER);
		GridData gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gd.widthHint = DEFAULT_WIDTH;
		gd.horizontalIndent = DEFAULT_INDENT;
		text.setLayoutData(gd);
		text.setData(ID_KEY, key);
		if (modifyListener != null) {
			text.addModifyListener(modifyListener);
		}
		if (map != null) {
			map.put(key, text);
		}
		return text;
	}

	/**
	 * Creates Label and CCombo controls. Adds created CCombo to map with key parameter.
	 * 
	 * @param toolkit
	 * @param parent
	 * @param map
	 * @param modifyListener
	 * @param label
	 * @param items
	 * @param index
	 * @param key
	 * @param dataKey
	 */
	public static CCombo createComboBoxRow(FormToolkit toolkit, Composite parent, Map<String, CCombo> map,
			ModifyListener modifyListener, KeyListener keyListener, String label, String[] items, int index, String key,
			boolean editable, JAVA_DOCUMENTATION_TYPE helpBaseUrl, String helpPage, String helpAnchor) {

		createLabel(toolkit, parent, label, helpBaseUrl, helpPage, helpAnchor);

		CCombo combo = new CCombo(parent, SWT.FLAT | SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = DEFAULT_WIDTH + DEFAULT_INDENT - 2;
		gd.horizontalIndent = DEFAULT_INDENT;
		combo.setLayoutData(gd);
		combo.setEditable(editable);

		if (items != null) {
			combo.setItems(items);
			combo.select(index);
		}
		toolkit.adapt(combo);
		toolkit.paintBordersFor(combo);
		combo.setData(ID_KEY, key);
		if (modifyListener != null) {
			combo.addModifyListener(modifyListener);
		}
		if (keyListener != null) {
			combo.addKeyListener(keyListener);
		}
		if (map != null) {
			map.put(key, combo);
		}
		return combo;
	}

	public static Text createStringRowWithBrowseButton(FormToolkit toolkit, Composite parent, Map<String, Text> map,
			ModifyListener modifyListener, String label, String value, String key, AbstractViewerFilter filter,
			JAVA_DOCUMENTATION_TYPE helpBaseUrl, String helpPage, String helpAnchor) {
		return createStringRowWithBrowseButton(toolkit, parent, map, modifyListener, label, value, key, filter,
				IJavaSearchConstants.CLASS, false, null, helpBaseUrl, helpPage, helpAnchor);
	}

	public static Text createStringRowWithBrowseButton(FormToolkit toolkit, Composite parent, Map<String, Text> map,
			ModifyListener modifyListener, String label, String value, String key, AbstractViewerFilter filter,
			boolean createClearButton, SelectionListener clearButtonSelectionListener, JAVA_DOCUMENTATION_TYPE helpBaseUrl,
			String helpPage, String helpAnchor) {
		return createStringRowWithBrowseButton(toolkit, parent, map, modifyListener, label, value, key, filter,
				IJavaSearchConstants.CLASS, createClearButton, clearButtonSelectionListener, helpBaseUrl, helpPage, helpAnchor);
	}

	public static Text createStringRowWithBrowseButton(FormToolkit toolkit, Composite parent, Map<String, Text> map,
			ModifyListener modifyListener, String label, String value, String key, final AbstractViewerFilter filter,
			final int javaSearchConstants, boolean createClearButton, SelectionListener clearButtonSelectionListener,
			JAVA_DOCUMENTATION_TYPE helpBaseUrl, String helpPage, String helpAnchor) {

		createLabel(toolkit, parent, label, helpBaseUrl, helpPage, helpAnchor);

		Composite composite = toolkit.createComposite(parent);
		GridLayout gl = new GridLayout();
		gl.marginWidth = gl.marginHeight = 0;
		gl.numColumns = (createClearButton && (clearButtonSelectionListener != null)) ? 3 : 2;
		composite.setLayout(gl);

		final Text text = toolkit.createText(composite, value, SWT.SINGLE);
		map.put(key, text);
		GridData gd = new GridData();
		gd.widthHint = DEFAULT_WIDTH;
		gd.horizontalIndent = 10;
		text.setLayoutData(gd);
		text.setData(ID_KEY, key);
		text.setEditable(false);
		text.setBackground(new Color(Display.getCurrent(), new RGB(0xEE, 0xEE, 0xEE)));
		if (modifyListener != null) {
			text.addModifyListener(modifyListener);
		}

		Button button = toolkit.createButton(composite, Messages.getString("Button.browse"), SWT.PUSH);//$NON-NLS-1$
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		button.setLayoutData(gd);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IRunnableContext context = new BusyIndicatorRunnableContext();
				IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
				TypesSelectionDialog dialog = new TypesSelectionDialog(e.widget.getDisplay().getActiveShell(), false, context,
						scope, javaSearchConstants);
				if (filter != null) {
					dialog.addListFilter(filter);
				}
				dialog.setTitle(Messages.getString("Dialog.selectClass.title"));//$NON-NLS-1$
				if (dialog.open() == Window.OK) {
					IType res = (IType) dialog.getResult()[0];
					// text.setData(ID_FULLY_QUALIFIED_NAME, res.getFullyQualifiedName('.'));
					text.setData(ID_FULLY_QUALIFIED_NAME, res.getFullyQualifiedName());
					text.setText(res.getElementName());
				}
			}
		});
		if (createClearButton && (clearButtonSelectionListener != null)) {
			button = toolkit.createButton(composite, Messages.getString("Button.clear"), SWT.PUSH);//$NON-NLS-1$
			gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
			button.setLayoutData(gd);
			button.addSelectionListener(clearButtonSelectionListener);
		}
		return text;
	}

	public static CCombo createComboBoxRowWithBrowseButton(FormToolkit toolkit, Composite parent, Map<String, CCombo> map,
			ModifyListener modifyListener, KeyListener keyListener, String label, String[] items, int index, String key,
			final AbstractViewerFilter filter, JAVA_DOCUMENTATION_TYPE helpBaseUrl, String helpPage, String helpAnchor) {
		return createComboBoxRowWithBrowseButton(toolkit, parent, map, modifyListener, keyListener, label, items, index, key,
				filter, false, null, helpBaseUrl, helpPage, helpAnchor);
	}

	public static CCombo createComboBoxRowWithBrowseButton(FormToolkit toolkit, Composite parent, Map<String, CCombo> map,
			ModifyListener modifyListener, KeyListener keyListener, String label, String[] items, int index, String key,
			final AbstractViewerFilter filter, boolean createClearButton, SelectionListener clearButtonSelectionListener,
			JAVA_DOCUMENTATION_TYPE helpBaseUrl, String helpPage, String helpAnchor) {

		createLabel(toolkit, parent, label, helpBaseUrl, helpPage, helpAnchor);

		Composite composite = toolkit.createComposite(parent);
		GridLayout gl = new GridLayout();
		gl.marginWidth = gl.marginHeight = 0;
		gl.numColumns = (createClearButton && (clearButtonSelectionListener != null)) ? 3 : 2;
		composite.setLayout(gl);

		final CCombo combo = new CCombo(composite, SWT.FLAT | SWT.DROP_DOWN | SWT.READ_ONLY | SWT.BORDER);
		GridData gd = new GridData();
		gd.widthHint = DEFAULT_WIDTH + DEFAULT_INDENT - 2;
		gd.horizontalIndent = DEFAULT_INDENT;
		combo.setLayoutData(gd);

		if (items != null) {
			combo.setItems(items);
			combo.select(index);
		}
		toolkit.adapt(combo);
		toolkit.paintBordersFor(combo);
		combo.setData(ID_KEY, key);
		if (modifyListener != null) {
			combo.addModifyListener(modifyListener);
		}
		if (keyListener != null) {
			combo.addKeyListener(keyListener);
		}
		if (map != null) {
			map.put(key, combo);
		}

		Button button = toolkit.createButton(composite, Messages.getString("Button.browse"), SWT.PUSH);//$NON-NLS-1$
		gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		button.setLayoutData(gd);
		button.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}

			@Override
			public void widgetSelected(SelectionEvent e) {
				IRunnableContext context = new BusyIndicatorRunnableContext();
				IJavaSearchScope scope = SearchEngine.createWorkspaceScope();
				TypesSelectionDialog dialog = new TypesSelectionDialog(e.widget.getDisplay().getActiveShell(), false, context,
						scope, IJavaSearchConstants.CLASS);
				if (filter != null) {
					dialog.addListFilter(filter);
				}
				dialog.setTitle(Messages.getString("Dialog.selectClass.title"));//$NON-NLS-1$
				if (dialog.open() == Window.OK) {
					IType res = (IType) dialog.getResult()[0];
					// combo.setData(ID_FULLY_QUALIFIED_NAME, res.getFullyQualifiedName('.'));
					combo.setData(ID_FULLY_QUALIFIED_NAME, res.getFullyQualifiedName());
					combo.setText(res.getElementName());
				}
			}
		});
		if (createClearButton && (clearButtonSelectionListener != null)) {
			button = toolkit.createButton(composite, Messages.getString("Button.clear"), SWT.PUSH);//$NON-NLS-1$
			gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
			button.setLayoutData(gd);
			button.addSelectionListener(clearButtonSelectionListener);
		}
		return combo;
	}

	private static void createLabel(FormToolkit toolkit, final Composite parent, String text,
			final JAVA_DOCUMENTATION_TYPE baseUrl, final String page, final String anchor) {
		Composite shared = new Composite(parent, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		GridData layoutData = new GridData(SWT.BEGINNING, SWT.BEGINNING, false, false);
		shared.setLayout(layout);
		shared.setLayoutData(layoutData);

		final Button button = initHelpButton(parent, shared);
		button.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				showView(baseUrl, page, anchor);
			}

		});

		Label label = toolkit.createLabel(shared, text);
		GridData gd = new GridData();
		gd.widthHint = 160;
		label.setLayoutData(gd);
	}

	private static Button initHelpButton(Composite parent, Composite shared) {
		final Button button = new Button(shared, SWT.PUSH);
		button.setImage(Activator.getDefault().getImage(Activator.ICON_HELP));
		button.setCursor(new Cursor(parent.getDisplay(), SWT.CURSOR_HAND));

		GridData gdbtn = new GridData();
		gdbtn.widthHint = 14;
		gdbtn.heightHint = 14;
		button.setLayoutData(gdbtn);
		return button;
	}

	private static void showView(JAVA_DOCUMENTATION_TYPE baseUrl, String page, String anchor) {
		if (!validateDocumentationParams(baseUrl, page)) {
			return;
		}
		try {
			OpenLegacyHelpView helpView = null;
			IViewReference ref = getMapViewReference();
			if (ref != null) {
				IViewPart view = ref.getView(true);
				if (view != null) {
					helpView = (OpenLegacyHelpView) view;
					PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(HELP_VIEW_ID, "help",
							IWorkbenchPage.VIEW_ACTIVATE);
				}
			}

			if (helpView == null) {
				helpView = (OpenLegacyHelpView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView(
						HELP_VIEW_ID, "help", IWorkbenchPage.VIEW_VISIBLE);
			}
			helpView.setUrl(MessageFormat.format(baseUrl.getUrl(), page, anchor));

		} catch (PartInitException ex) {
			ex.printStackTrace();
		}
	}

	private static boolean validateDocumentationParams(JAVA_DOCUMENTATION_TYPE baseUrl, String page) {
		if ((baseUrl == null) || StringUtil.isEmpty(page)) {
			return false;
		}
		return true;
	}

	public static IViewReference getMapViewReference() {
		IWorkbenchWindow[] windows = PlatformUI.getWorkbench().getWorkbenchWindows();
		if (windows != null && windows.length > 0) {
			return windows[0].getActivePage().findViewReference(HELP_VIEW_ID);
		}
		return null;
	}
}
