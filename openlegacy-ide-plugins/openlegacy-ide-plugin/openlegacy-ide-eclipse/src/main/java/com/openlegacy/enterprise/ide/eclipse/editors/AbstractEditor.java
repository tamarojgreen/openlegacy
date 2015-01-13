package com.openlegacy.enterprise.ide.eclipse.editors;

import com.openlegacy.enterprise.ide.eclipse.Constants;
import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.editors.pages.HtmlPreviewPage;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.part.FileEditorInput;
import org.openlegacy.designtime.generators.GenerateUtil;
import org.openlegacy.exceptions.OpenLegacyException;
import org.openlegacy.ide.eclipse.Activator;
import org.openlegacy.ide.eclipse.editors.graphical.IOpenLegacyEditor;
import org.openlegacy.layout.PageDefinition;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Abstract graphical editor for Java files containing @..Entity annotations
 * 
 * @author Ivan Bort
 * 
 */
public abstract class AbstractEditor extends FormEditor implements IOpenLegacyEditor, IResourceChangeListener {

	private static final Logger logger = Logger.getLogger(AbstractEditor.class);

	private CompilationUnitEditor sourcePage;

	private HtmlPreviewPage htmlPreviewPage;

	private File templateFile = null;

	protected Map<String, IMarker> markers = new HashMap<String, IMarker>();

	protected abstract void populateEntity(IEditorInput editorInput) throws PartInitException;

	protected abstract void addEditorPages() throws PartInitException;

	protected abstract void doSave() throws PartInitException, OpenLegacyException;

	protected abstract void doAfterSave();

	protected abstract PageDefinition getPageDefinitionForHtmlPreview();

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.forms.editor.FormEditor#createToolkit(org.eclipse.swt.widgets.Display)
	 */
	@Override
	protected FormToolkit createToolkit(Display display) {
		// Create a toolkit that shares colors between editors.
		return new FormToolkit(Activator.getDefault().getFormColors(display));
	}

	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		super.init(site, input);

		populateEntity(input);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
	}

	@Override
	protected void addPages() {
		setPartName(((FileEditorInput)getEditorInput()).getName());
		try {
			addEditorPages();
			addHtmlPreviewPage();
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), Messages.getString("error.adding.pages.title"), e.getMessage(),
					e.getStatus());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.resources.IResourceChangeListener#resourceChanged(org.eclipse.core.resources.IResourceChangeEvent)
	 */
	@Override
	public void resourceChanged(final IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.PRE_CLOSE) {
			Display.getDefault().asyncExec(new Runnable() {

				@Override
				public void run() {
					IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
					for (IWorkbenchPage page : pages) {
						if (((FileEditorInput)getEditorInput()).getFile().getProject().equals(event.getResource())) {
							IEditorPart editorPart = page.findEditor(getEditorInput());
							page.closeEditor(editorPart, true);
						}
					}
				}
			});
		}
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		if (!markers.isEmpty()) {
			return;
		}
		try {
			doSave();
		} catch (PartInitException e) {
			ErrorDialog.openError(getSite().getShell(), Messages.getString("error.save.file"), e.getMessage(), e.getStatus());//$NON-NLS-1$
		} catch (OpenLegacyException e) {
			ErrorDialog.openError(getSite().getShell(), Messages.getString("error.save.file"), e.getMessage(), new Status(//$NON-NLS-1$
					IStatus.ERROR, Activator.PLUGIN_ID, e.getMessage()));
		}
		clearMarkers();
		doAfterSave();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#doSaveAs()
	 */
	@Override
	public void doSaveAs() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.part.EditorPart#isSaveAsAllowed()
	 */
	@Override
	public boolean isSaveAsAllowed() {
		return false;
	}

	@Override
	public void dispose() {
		clearMarkers();
		sourcePage = null;
		htmlPreviewPage = null;
		super.dispose();
	}

	@Override
	protected void pageChange(int newPageIndex) {
		super.pageChange(newPageIndex);

		if ((htmlPreviewPage != null) && (newPageIndex == htmlPreviewPage.getPageIndex()) && (templateFile != null)) {
			try {
				String templateContent = FileUtils.readFileToString(templateFile);
				htmlPreviewPage.setPageDefinition(getPageDefinitionForHtmlPreview());
				htmlPreviewPage.setContainerPathes(getProject());
				htmlPreviewPage.setRightToLeft(getProject());

				String generated = GenerateUtil.generate(htmlPreviewPage, new StringReader(templateContent));

				htmlPreviewPage.getBrowser().loadContent(generated);
			} catch (IOException ex) {
				logger.fatal("Cannot read template file", ex);//$NON-NLS-1$
			}
		}
	}

	protected void addSourcePage() throws PartInitException {
		sourcePage = new CompilationUnitEditor();
		int index = addPage(sourcePage, getEditorInput());
		setPageText(index, Messages.getString("SourcePage.title"));//$NON-NLS-1$
		sourcePage.getViewer().setEditable(false);
	}

	protected void addHtmlPreviewPage() {
		// check if .template file exist in root of project
		if (isTemplateFileExist()) {
			htmlPreviewPage = new HtmlPreviewPage(getContainer(), 0);
			htmlPreviewPage.setPageIndex(addPage(htmlPreviewPage));
			setPageText(htmlPreviewPage.getPageIndex(), Messages.getString("HtmlPreviewPage.title"));//$NON-NLS-1$
		}
	}

	private boolean isTemplateFileExist() {
		IResource member = getProject().findMember(Constants.TEMPLATE_FILE);
		if (member != null) {
			templateFile = new File(member.getLocation().toOSString());
			return true;
		}
		templateFile = null;
		return false;
	}

	private IProject getProject() {
		IResource resource = (IResource)getEditorInput().getAdapter(IResource.class);
		return resource.getProject();
	}

	private void clearMarkers() {
		if (!markers.isEmpty()) {
			for (IMarker marker : markers.values()) {
				try {
					marker.delete();
				} catch (CoreException e) {
				}
			}
			markers.clear();
		}
	}

	public void removeValidationMarker(String key) {
		if (markers.containsKey(key)) {
			try {
				markers.get(key).delete();
				markers.remove(key);
				if (markers.isEmpty()) {
					setTitleImage(Activator.getDefault().getImage(Activator.IMG_EDITOR_NORMAL));
				}
			} catch (CoreException e) {
			}
		}
	}

	public void removeValidationMarkers(UUID uuid) {
		if (!markers.isEmpty()) {
			Set<String> keySet = markers.keySet();
			for (String key : keySet) {
				if (key.startsWith(uuid.toString())) {
					removeValidationMarker(key);
				}
			}
		}
	}

	public void addValidationMarker(String key, String text) {
		if (!markers.containsKey(key)) {
			IResource resource = (IResource)getEditorInput().getAdapter(IResource.class);
			try {
				IMarker marker = resource.createMarker(IMarker.PROBLEM);
				marker.setAttribute(IMarker.MESSAGE, text);
				marker.setAttribute(IMarker.PRIORITY, IMarker.PRIORITY_HIGH);
				marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
				markers.put(key, marker);
				setTitleImage(Activator.getDefault().getImage(Activator.IMG_EDITOR_ERROR));
			} catch (CoreException e) {
			}
		}
	}
}
