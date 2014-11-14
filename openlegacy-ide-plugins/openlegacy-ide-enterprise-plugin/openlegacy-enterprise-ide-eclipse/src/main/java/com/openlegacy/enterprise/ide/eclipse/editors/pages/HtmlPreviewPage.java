package com.openlegacy.enterprise.ide.eclipse.editors.pages;

import com.openlegacy.enterprise.ide.eclipse.Constants;

import org.eclipse.core.resources.IProject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.openlegacy.designtime.mains.DesignTimeExecuter;
import org.openlegacy.designtime.mains.DesignTimeExecuterImpl;
import org.openlegacy.layout.PageDefinition;
import org.openlegacy.utils.StringUtil;

import java.io.File;
import java.text.MessageFormat;

/**
 * @author Ivan Bort
 * 
 */
public class HtmlPreviewPage extends Composite {

	private int pageIndex = -1;

	private HtmlPreviewBrowser browser;

	private PageDefinition pageDefinition;

	private String cssContainerPath = null;

	private String jsContainerPath = null;

	private DesignTimeExecuter designTimeExecuter = new DesignTimeExecuterImpl();

	public HtmlPreviewPage(Composite parent, int style) {
		super(parent, style);
		FillLayout fillLayout = new FillLayout();
		setLayout(fillLayout);
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public HtmlPreviewBrowser getBrowser() {
		if (browser == null) {
			browser = new HtmlPreviewBrowser();
			browser.create(this, SWT.NONE);
			layout();
		}
		return browser;
	}

	public PageDefinition getPageDefinition() {
		return pageDefinition;
	}

	public void setPageDefinition(PageDefinition pageDefinition) {
		this.pageDefinition = pageDefinition;
	}

	public String getCssContainerPath() {
		return cssContainerPath;
	}

	public String getJsContainerPath() {
		return jsContainerPath;
	}

	public void setContainerPathes(IProject project) {
		String projectPath = project.getLocation().toOSString();
		String cssPath = designTimeExecuter.getPreferences(new File(project.getLocation().toOSString()),
				Constants.CSS_FOLDER_PATH);
		if (!StringUtil.isEmpty(cssPath)) {
			this.cssContainerPath = MessageFormat.format("{0}/{1}", projectPath, cssPath); //$NON-NLS-1$
		}
		String jsPath = designTimeExecuter.getPreferences(new File(project.getLocation().toOSString()), Constants.JS_FOLDER_PATH);
		if (!StringUtil.isEmpty(jsPath)) {
			this.jsContainerPath = MessageFormat.format("{0}/{1}", projectPath, jsPath);//$NON-NLS-1$
		}
	}
}
