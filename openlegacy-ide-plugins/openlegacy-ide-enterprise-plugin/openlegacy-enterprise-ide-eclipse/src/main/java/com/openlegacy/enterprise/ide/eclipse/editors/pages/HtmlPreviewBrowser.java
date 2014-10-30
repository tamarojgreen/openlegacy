package com.openlegacy.enterprise.ide.eclipse.editors.pages;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.ProgressEvent;
import org.eclipse.swt.browser.ProgressListener;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Ivan Bort
 * 
 */
public class HtmlPreviewBrowser implements ProgressListener {

	private Browser browser;

	public void create(Composite composite, int i) {
		this.browser = new Browser(composite, i);
		this.browser.addProgressListener(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.browser.ProgressListener#changed(org.eclipse.swt.browser.ProgressEvent)
	 */
	public void changed(ProgressEvent event) {
		// do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.swt.browser.ProgressListener#completed(org.eclipse.swt.browser.ProgressEvent)
	 */
	public void completed(ProgressEvent event) {
		// do nothing
	}

	/**
	 * @return the underlying swt Browser instance
	 */
	public Browser getBrowser() {
		return browser;
	}

	public synchronized void loadContent(String content) {
		if (browser == null) {
			return;
		}
		browser.setText(content);
		return;
	}
}
