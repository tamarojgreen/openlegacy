package com.openlegacy.enterprise.ide.eclipse.editors.pages;

import com.openlegacy.enterprise.ide.eclipse.Messages;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.openlegacy.ide.eclipse.Activator;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public abstract class AbstractMasterBlock extends MasterDetailsBlock implements IOpenLegacyMasterBlock {

	protected AbstractPage page;

	protected TableViewer tableViewer;

	protected List<IDetailsPage> detailsPages = new ArrayList<IDetailsPage>();

	public AbstractMasterBlock(AbstractPage page) {
		this.page = page;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.IOpenLegacyMasterBlock#refresh()
	 */
	@Override
	public abstract void refresh();

	@Override
	public abstract void updateLabels();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.MasterDetailsBlock#createMasterPart(org.eclipse.ui.forms.IManagedForm,
	 * org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected abstract void createMasterPart(IManagedForm managedForm, Composite parent);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.MasterDetailsBlock#registerPages(org.eclipse.ui.forms.DetailsPart)
	 */
	@Override
	protected abstract void registerPages(DetailsPart detailsPart);

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.MasterDetailsBlock#createToolBarActions(org.eclipse.ui.forms.IManagedForm)
	 */
	@Override
	protected void createToolBarActions(IManagedForm managedForm) {
		final ScrolledForm form = managedForm.getForm();
		Action haction = new Action("hor", IAction.AS_RADIO_BUTTON) { //$NON-NLS-1$

			@Override
			public void run() {
				sashForm.setOrientation(SWT.HORIZONTAL);
				form.reflow(true);
			}
		};
		haction.setChecked(true);
		haction.setToolTipText(Messages.getString("ScrolledBlock.toolbar.horizontal")); //$NON-NLS-1$
		haction.setImageDescriptor(Activator.getImageDescriptor(Activator.IMG_HORIZONTAL));
		Action vaction = new Action("ver", IAction.AS_RADIO_BUTTON) { //$NON-NLS-1$

			@Override
			public void run() {
				sashForm.setOrientation(SWT.VERTICAL);
				form.reflow(true);
			}
		};
		vaction.setChecked(false);
		vaction.setToolTipText(Messages.getString("ScrolledBlock.toolbar.vertical")); //$NON-NLS-1$
		vaction.setImageDescriptor(Activator.getImageDescriptor(Activator.IMG_VERTICAL));
		form.getToolBarManager().add(haction);
		form.getToolBarManager().add(vaction);
	}

	public AbstractPage getAbstractPage() {
		return this.page;
	}
}
