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

package org.openlegacy.ide.eclipse.misc.editors.pages.general;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.forms.DetailsPart;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.MasterDetailsBlock;
import org.eclipse.ui.forms.SectionPart;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.openlegacy.ide.eclipse.misc.Activator;
import org.openlegacy.ide.eclipse.misc.Messages;
import org.openlegacy.terminal.definitions.ScreenEntityDefinition;

/**
 * @author Imivan
 * 
 */
public class GeneralScrolledBlock extends MasterDetailsBlock {

	private FormPage page;

	/**
	 * 
	 */
	public GeneralScrolledBlock(FormPage page) {
		this.page = page;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.MasterDetailsBlock#createMasterPart(org.eclipse.ui.forms.IManagedForm, org.eclipse.swt.widgets.Composite)
	 */
	@Override
	protected void createMasterPart(final IManagedForm managedForm, Composite parent) {
		FormToolkit toolkit = managedForm.getToolkit();
		Section section = toolkit.createSection(parent, Section.DESCRIPTION | ExpandableComposite.TITLE_BAR);
		section.setText(Messages.getString("ScrolledBlock.sname")); //$NON-NLS-1$
		section.setDescription(Messages.getString("ScrolledBlock.sdesc")); //$NON-NLS-1$
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
		//		Button b = toolkit.createButton(client, Messages.getString("ScrolledPropertiesBlock.add"), SWT.PUSH); //$NON-NLS-1$
		// gd = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		// b.setLayoutData(gd);
		section.setClient(client);
		final SectionPart spart = new SectionPart(section);
		managedForm.addPart(spart);
		TableViewer viewer = new TableViewer(t);
		viewer.addSelectionChangedListener(new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				managedForm.fireSelectionChanged(spart, event.getSelection());
			}
		});
		viewer.setContentProvider(new MasterContentProvider());
		viewer.setLabelProvider(new MasterLabelProvider());
		viewer.setInput(page.getEditor().getEditorInput());
	}

	/* (non-Javadoc)
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
		haction.setToolTipText(Messages.getString("ScrolledBlock.horizontal")); //$NON-NLS-1$
		haction.setImageDescriptor(Activator.getDefault().getImageRegistry().getDescriptor(Activator.IMG_HORIZONTAL));
		Action vaction = new Action("ver", IAction.AS_RADIO_BUTTON) { //$NON-NLS-1$

			@Override
			public void run() {
				sashForm.setOrientation(SWT.VERTICAL);
				form.reflow(true);
			}
		};
		vaction.setChecked(false);
		vaction.setToolTipText(Messages.getString("ScrolledBlock.vertical")); //$NON-NLS-1$
		vaction.setImageDescriptor(Activator.getDefault().getImageRegistry().getDescriptor(Activator.IMG_VERTICAL));
		form.getToolBarManager().add(haction);
		form.getToolBarManager().add(vaction);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.forms.MasterDetailsBlock#registerPages(org.eclipse.ui.forms.DetailsPart)
	 */
	@Override
	protected void registerPages(DetailsPart detailsPart) {
		detailsPart.registerPage(ScreenEntityDefinition.class, new ScreenEntityDetailsPage());
		detailsPart.registerPage(ScreenEntityDefinition.class, new ScreenNavigationDetailsPage());
	}

	/**
	 * @param id
	 * @param title
	 */
	class MasterContentProvider implements IStructuredContentProvider {

		@Override
		public Object[] getElements(Object inputElement) {
			// if (inputElement instanceof SimpleFormEditorInput) {
			// SimpleFormEditorInput input = (SimpleFormEditorInput)page.getEditor().getEditorInput();
			// return input.getModel().getContents();
			// }
			return new Object[0];
		}

		@Override
		public void dispose() {}

		@Override
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {}
	}

	class MasterLabelProvider extends LabelProvider implements ITableLabelProvider {

		@Override
		public String getColumnText(Object obj, int index) {
			return obj.toString();
		}

		@Override
		public Image getColumnImage(Object obj, int index) {
			// if (obj instanceof TypeOne) {
			// return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
			// }
			// if (obj instanceof TypeTwo) {
			// return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
			// }
			return null;
		}
	}

}
