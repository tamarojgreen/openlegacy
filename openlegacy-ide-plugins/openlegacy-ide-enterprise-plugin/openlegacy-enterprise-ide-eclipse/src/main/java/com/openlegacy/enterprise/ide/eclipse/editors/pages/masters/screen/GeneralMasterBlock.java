package com.openlegacy.enterprise.ide.eclipse.editors.pages.masters.screen;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.models.NamedObject;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.AbstractPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.IOpenLegacyDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen.GeneralScreenEntityDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.details.screen.GeneralScreenNavigationDetailsPage;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen.GeneralMasterBlockContentProvider;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.providers.screen.GeneralMasterBlockLabelProvider;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IDetailsPage;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

/**
 * @author Ivan Bort
 * 
 */
public class GeneralMasterBlock extends AbstractScreenEntityMasterBlock {

	public GeneralMasterBlock(AbstractPage page) {
		super(page);
	}

	@Override
	public void refresh() {
		for (IDetailsPage page : detailsPages) {
			page.refresh();
		}
		IStructuredSelection structuredSelection = (IStructuredSelection)tableViewer.getSelection();
		tableViewer.setInput(getEntity());
		if (structuredSelection.size() == 1) {
			tableViewerSetSelectionByLabel(((NamedObject)structuredSelection.getFirstElement()).getModelName());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.MasterDetailsBlock#createMasterPart(org.eclipse.ui.forms.IManagedForm,
	 * org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createMasterPart(final IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		Section section = toolkit.createSection(parent, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR);
		section.setText(Messages.getString("GeneralScrolledBlock.masterPartName")); //$NON-NLS-1$
		section.setDescription(Messages.getString("GeneralScrolledBlock.masterPartDesc")); //$NON-NLS-1$
		section.marginWidth = 10;
		section.marginHeight = 5;

		Composite client = toolkit.createComposite(section, SWT.WRAP);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		layout.marginWidth = 2;
		layout.marginHeight = 2;
		client.setLayout(layout);

		Table t = toolkit.createTable(client, SWT.NULL);
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.heightHint = 20;
		gd.widthHint = 100;
		t.setLayoutData(gd);
		toolkit.paintBordersFor(client);

		section.setClient(client);
		final SectionPart spart = new SectionPart(section);
		managedForm.addPart(spart);
		tableViewer = new TableViewer(t);
		tableViewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged(spart, event.getSelection());
			}
		});
		tableViewer.setContentProvider(new GeneralMasterBlockContentProvider());
		tableViewer.setLabelProvider(new GeneralMasterBlockLabelProvider());
		tableViewer.setInput(getEntity());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.MasterDetailsBlock#registerPages(org.eclipse.ui.forms.DetailsPart)
	 */
	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPages.add(new GeneralScreenEntityDetailsPage(this));
		detailsPages.add(new GeneralScreenNavigationDetailsPage(this));
		for (IDetailsPage page : detailsPages) {
			detailsPart.registerPage(((IOpenLegacyDetailsPage)page).getDetailsModel(), page);
		}
	}

	@Override
	public void createContent(IManagedForm managedForm) {
		super.createContent(managedForm);
		this.sashForm.setWeights(new int[] { 30, 70 });
		int itemCount = this.tableViewer.getTable().getItemCount();
		if (itemCount > 0) {
			this.tableViewer.getTable().select(0);
			this.tableViewer.getTable().showSelection();
			ISelection selection = this.tableViewer.getSelection();
			this.tableViewer.setSelection(selection);
		}
	}

	private void tableViewerSetSelectionByLabel(String label) {
		int itemCount = this.tableViewer.getTable().getItemCount();
		if (itemCount > 0) {
			for (int i = 0; i < itemCount; i++) {
				TableItem item = this.tableViewer.getTable().getItem(i);
				if (item.getText().equals(label)) {
					this.tableViewer.getTable().select(i);
					this.tableViewer.getTable().showSelection();
					ISelection selection = this.tableViewer.getSelection();
					this.tableViewer.setSelection(selection);
					return;
				}
			}
		}
	}
}
