package org.openlegacy.ide.eclipse.actions;

import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaModel;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.wizards.NewWizardMessages;
import org.eclipse.jdt.internal.ui.wizards.TypedElementSelectionValidator;
import org.eclipse.jdt.internal.ui.wizards.TypedViewerFilter;
import org.eclipse.jdt.ui.JavaElementComparator;
import org.eclipse.jdt.ui.JavaElementLabelProvider;
import org.eclipse.jdt.ui.StandardJavaElementContentProvider;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.openlegacy.designtime.mains.OverrideConfirmer;
import org.openlegacy.ide.eclipse.PluginConstants;
import org.openlegacy.ide.eclipse.util.JavaUtils;
import org.openlegacy.ide.eclipse.util.Prefrences;

import java.io.File;

@SuppressWarnings("restriction")
public class GenerateScreensDialog extends Dialog implements OverrideConfirmer {

	private final static Logger logger = Logger.getLogger(GenerateScreensDialog.class);

	private Text sourceFolderPathText;
	private IPackageFragmentRoot sourceFolder;
	private ISelection selection;
	private Text packageText;
	private String packageValue;

	protected GenerateScreensDialog(Shell shell, ISelection selection) {
		super(shell);
		this.selection = selection;
	}

	@Override
	protected Control createDialogArea(Composite parent) {

		parent = new Composite(parent, SWT.NONE);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		GridData gd = new GridData();
		gd.widthHint = 400;
		parent.setLayoutData(gd);
		parent.setLayout(gridLayout);

		parent.getShell().setText(PluginConstants.TITLE);

		Label label = new Label(parent, SWT.NULL);
		label.setText("Source folder:");

		sourceFolderPathText = new Text(parent, SWT.SINGLE | SWT.BORDER);

		sourceFolderPathText.setEditable(false);

		gd = new GridData(GridData.FILL_HORIZONTAL);
		sourceFolderPathText.setLayoutData(gd);
		Button sourceFolderButton = new Button(parent, SWT.NONE);
		sourceFolderButton.setText("Browse...");
		sourceFolderButton.addSelectionListener(new SelectionListener() {

			public void widgetSelected(SelectionEvent e) {
				handleSourceFolderButtonSelected();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		});

		Label labelPackage = new Label(parent, SWT.NULL);
		labelPackage.setText("Package:");
		packageText = new Text(parent, SWT.SINGLE | SWT.BORDER);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		packageText.setLayoutData(gd);

		packageText.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent paramModifyEvent) {
				packageValue = packageText.getText();
			}
		});

		loadPrefrences();

		return parent;
	}

	private void loadPrefrences() {
		IFile selectionFile = (IFile)((IStructuredSelection)selection).getFirstElement();
		String prefrenceSourceFolderPath = Prefrences.get(PluginConstants.DEFAULT_SOURCE_FOLDER_ID,
				PluginConstants.DEFAULT_SOURCE_FOLDER);
		sourceFolderPathText.setText(selectionFile.getProject().getName() + "\\" + prefrenceSourceFolderPath);
		IJavaProject javaProject = JavaUtils.getJavaProjectFromIProject(selectionFile.getProject());
		setSourceFolder(javaProject.getPackageFragmentRoot(prefrenceSourceFolderPath));

		String prefrencePackage = Prefrences.get(PluginConstants.DEFAULT_PACKAGE, "");
		packageText.setText(prefrencePackage);
	}

	protected void handleSourceFolderButtonSelected() {
		IPackageFragmentRoot newSourceFolder = chooseContainer();

		if (newSourceFolder == null) {
			return;
		}

		setSourceFolder(newSourceFolder);
		String newSourceFolderPath = JavaUtils.convertSourceFolderToString(newSourceFolder);
		sourceFolderPathText.setText(newSourceFolderPath);
	}

	public void setSourceFolder(IPackageFragmentRoot sourceFolder) {
		this.sourceFolder = sourceFolder;
	}

	public IPackageFragmentRoot getSourceFolder() {
		return sourceFolder;
	}

	protected IPackageFragmentRoot chooseContainer() {
		IJavaElement initElement = getSourceFolder();
		Class<?>[] acceptedClasses = new Class[] { IPackageFragmentRoot.class, IJavaProject.class };
		TypedElementSelectionValidator validator = new TypedElementSelectionValidator(acceptedClasses, false) {

			@Override
			public boolean isSelectedValid(Object element) {
				try {
					if (element instanceof IJavaProject) {
						IJavaProject jproject = (IJavaProject)element;
						IPath path = jproject.getProject().getFullPath();
						return (jproject.findPackageFragmentRoot(path) != null);
					} else if (element instanceof IPackageFragmentRoot) {
						return (((IPackageFragmentRoot)element).getKind() == IPackageFragmentRoot.K_SOURCE);
					}
					return true;
				} catch (JavaModelException e) {
				}
				return false;
			}
		};

		acceptedClasses = new Class[] { IJavaModel.class, IPackageFragmentRoot.class, IJavaProject.class };
		ViewerFilter filter = new TypedViewerFilter(acceptedClasses) {

			@Override
			public boolean select(Viewer viewer, Object parent, Object element) {
				if (element instanceof IPackageFragmentRoot) {
					try {
						return (((IPackageFragmentRoot)element).getKind() == IPackageFragmentRoot.K_SOURCE);
					} catch (JavaModelException e) {
					}
				} else if (element instanceof IJavaProject) {
					IJavaProject javaPr = (IJavaProject)element;
					return javaPr.isOpen();
				}

				return super.select(viewer, parent, element);
			}
		};

		StandardJavaElementContentProvider provider = new StandardJavaElementContentProvider();
		ILabelProvider labelProvider = new JavaElementLabelProvider(JavaElementLabelProvider.SHOW_DEFAULT);
		ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(getShell(), labelProvider, provider);
		dialog.setValidator(validator);
		dialog.setComparator(new JavaElementComparator());
		dialog.setTitle(NewWizardMessages.NewContainerWizardPage_ChooseSourceContainerDialog_title);
		dialog.setMessage(NewWizardMessages.NewContainerWizardPage_ChooseSourceContainerDialog_description);
		dialog.addFilter(filter);
		IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
		dialog.setInput(JavaCore.create(root));
		dialog.setInitialSelection(initElement);
		dialog.setHelpAvailable(false);

		if (dialog.open() == Window.OK) {
			Object element = dialog.getFirstResult();
			if (element instanceof IJavaProject) {
				IJavaProject jproject = (IJavaProject)element;
				return jproject.getPackageFragmentRoot(jproject.getProject());
			} else if (element instanceof IPackageFragmentRoot) {
				return (IPackageFragmentRoot)element;
			}
			return null;
		}
		return null;
	}

	private boolean validate() {

		if (packageText.getText().length() == 0) {
			MessageDialog.openError(getShell(), PluginConstants.TITLE, "Package connot be empty");
			return false;
		}
		if (sourceFolderPathText.getText().length() == 0) {
			MessageDialog.openError(getShell(), PluginConstants.TITLE, "Source folder connot be empty");
			return false;
		}
		return true;

	}

	@Override
	protected void okPressed() {
		Object firstElement = ((TreeSelection)selection).getFirstElement();
		if (!(firstElement instanceof IFile)) {
			MessageDialog.openError(getShell(), PluginConstants.TITLE, "Invalid trail file selection");
		}
		final IFile trailPath = (IFile)firstElement;

		if (!validate()) {
			return;
		}

		savePreferences();
		executeGenerateScreensJob(trailPath);

		close();
	}

	private void executeGenerateScreensJob(final IFile trailPath) {
		Job job = new Job("Generating screens") {

			@Override
			protected IStatus run(final IProgressMonitor monitor) {

				int fileSize = (int)(new File(trailPath.getLocation().toOSString()).length() / 1000);
				monitor.beginTask("Activating Analyzer", fileSize);

				monitor.worked(2);
				EclipseDesignTimeExecuter.instance().generateScreens(trailPath, getSourceFolder(), packageValue,
						GenerateScreensDialog.this);

				monitor.worked(fileSize - 4);
				Display.getDefault().syncExec(new Runnable() {

					public void run() {
						try {
							monitor.worked(1);
							trailPath.getProject().refreshLocal(IResource.DEPTH_INFINITE, monitor);
							monitor.done();
						} catch (CoreException e) {
							logger.fatal(e);
						}
					}
				});

				return Status.OK_STATUS;
			}
		};
		job.schedule();
	}

	private void savePreferences() {
		String sourceFolderOnly = sourceFolderPathText.getText().substring(
				sourceFolder.getJavaProject().getProject().getName().length() + 1);
		Prefrences.put(PluginConstants.DEFAULT_SOURCE_FOLDER_ID, sourceFolderOnly);
		Prefrences.put(PluginConstants.DEFAULT_PACKAGE, packageText.getText());
	}

	public boolean isOverride(final File file) {
		final Object[] result = new Object[1];
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				result[0] = MessageDialog.openQuestion(getShell(), PluginConstants.TITLE, "Override class " + file.getName()
						+ "?");
			}
		});

		return (Boolean)result[0];
	}
}
