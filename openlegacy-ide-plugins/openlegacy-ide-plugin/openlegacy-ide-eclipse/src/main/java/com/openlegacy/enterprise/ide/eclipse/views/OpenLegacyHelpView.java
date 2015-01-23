package com.openlegacy.enterprise.ide.eclipse.views;

import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

public class OpenLegacyHelpView extends ViewPart {

	private Composite browserContainer;
	private Browser browser;

	@Override
	public void createPartControl(Composite parent) {
		browserContainer = new Composite(parent, SWT.FILL);
		browserContainer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		GridLayout m_stackLayout = new GridLayout(1, true);
		browserContainer.setLayout(m_stackLayout);
		browser = new Browser(browserContainer, SWT.FILL);
		browser.setUrl("http://files.openlegacy.org/javadoc/org/openlegacy/annotations/screen/ScreenEntity.html#displayName()");
		browser.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
	}
	
	public void setUrl(String url) {
		browser.setUrl(url);
		browser.refresh();
	}

	@Override
	public void setFocus() {
		// TODO Auto-generated method stub
		
	}

}
