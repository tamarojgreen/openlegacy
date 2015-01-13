package com.openlegacy.enterprise.ide.eclipse.ws.generator.dialogs;

import com.openlegacy.enterprise.ide.eclipse.Messages;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractEntityModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.AbstractNamedModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.LoadingModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.dialog.InTableModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.dialog.OutTableModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.dialog.TreeViewerModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc.RpcEntityModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc.RpcFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.rpc.RpcPartModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenEntityModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenFieldModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.models.screen.ScreenPartModel;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.providers.ItemLabelProvider;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.providers.TableContentProvider;
import com.openlegacy.enterprise.ide.eclipse.ws.generator.providers.TreeViewContentProvider;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.openlegacy.EntityDefinition;
import org.openlegacy.designtime.PreferencesConstants;
import org.openlegacy.designtime.UserInteraction;
import org.openlegacy.designtime.mains.DesignTimeExecuter;
import org.openlegacy.designtime.mains.DesignTimeExecuterImpl;
import org.openlegacy.designtime.mains.GenerateServiceRequest;
import org.openlegacy.designtime.mains.GenerateServiceRequest.ServiceType;
import org.openlegacy.designtime.mains.ServiceEntityFieldParameter;
import org.openlegacy.designtime.mains.ServiceEntityParameter;
import org.openlegacy.designtime.mains.ServiceParameter;
import org.openlegacy.designtime.mains.ServicePartParameter;
import org.openlegacy.ide.eclipse.Activator;
import org.openlegacy.ide.eclipse.components.rpc.RpcComposite;
import org.openlegacy.ide.eclipse.components.screen.SnapshotComposite;
import org.openlegacy.ide.eclipse.util.PathsUtil;
import org.openlegacy.terminal.TerminalField;
import org.openlegacy.terminal.TerminalPosition;
import org.openlegacy.terminal.TerminalSnapshot;
import org.openlegacy.terminal.definitions.ScreenFieldDefinition;
import org.openlegacy.terminal.render.DefaultTerminalSnapshotImageRenderer;
import org.openlegacy.terminal.support.SimpleTerminalPosition;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

/**
 * @author Ivan Bort
 * 
 */
public class GenerateServiceDialog extends Dialog implements UserInteraction {

	private static final String STRUCTURE_TYPE = "structure_type";
	private static final String STRUCTURE_IN = "structure_in";
	private static final String STRUCTURE_OUT = "structure_out";

	private static final String SERVICES_SUFFIX = ".services";

	private final static Logger logger = Logger.getLogger(GenerateServiceDialog.class);
	private SnapshotComposite mSnapshotComposite;
	private RpcComposite mRpcComposite;
	private TreeViewer mTreeViewer;
	private IProject project = null;

	private boolean isCreatePool = true;
	private boolean isGenerateJUnit = true;
	private String serviceName = null;

	// for input block
	private Button mInAddBtn;
	private Button mInRemoveBtn;
	private TableViewer mInTableViewer;
	private Button mInUpBtn;
	private Button mInDownBtn;
	// for output block
	private Button mOutAddBtn;
	private Button mOutRemoveBtn;
	private TableViewer mOutTableViewer;
	private Button mOutUpBtn;
	private Button mOutDownBtn;

	private TreeViewerModel mTreeViewerModel = null;
	private InTableModel mInTableModel = null;
	private OutTableModel mOutTableModel = null;
	private ServiceType serviceType;

	public GenerateServiceDialog(Shell parentShell, TreeViewerModel model, IProject project, ServiceType serviceType) {
		super(parentShell);
		this.mTreeViewerModel = model;
		this.serviceType = serviceType;
		this.mInTableModel = new InTableModel();
		this.mOutTableModel = new OutTableModel();
		this.project = project;
	}

	@Override
	protected void configureShell(Shell newShell) {
		super.configureShell(newShell);
		newShell.setText(Messages.getString("service.generator.dialog.title"));//$NON-NLS-1$
	}

	@Override
	protected void okPressed() {
		if (!validate()) {
			return;
		}

		executeGenerate();

		close();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite area = (Composite)super.createDialogArea(parent);
		GridLayout gl = new GridLayout();
		gl.marginHeight = 0;
		gl.marginWidth = 0;
		area.setLayout(gl);
		Composite container = new Composite(area, SWT.NONE);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginBottom = 0;
		gridLayout.marginLeft = 0;
		gridLayout.marginTop = 0;
		gridLayout.marginRight = 0;
		gridLayout.marginHeight = 0;
		gridLayout.marginWidth = 0;
		container.setLayout(gridLayout);
		GridData gridData = new GridData(SWT.FILL);
		gridData.widthHint = 616;
		gridData.grabExcessHorizontalSpace = true;
		container.setLayoutData(gridData);

		// first row - "Service name"
		createFirstRow(container);
		// second row - "input/output"
		createSecondRow(container);
		// third row - "screen preview"
		createThirdRow(container);

		return area;
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		parent.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		ToolBar toolBar = new ToolBar(parent, SWT.FLAT | SWT.NO_FOCUS);
		((GridLayout)parent.getLayout()).numColumns++;
		toolBar.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_CENTER));
		final Cursor cursor = new Cursor(parent.getDisplay(), SWT.CURSOR_HAND);
		toolBar.setCursor(cursor);
		toolBar.addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				cursor.dispose();
			}
		});

		ToolItem toolItem = new ToolItem(toolBar, SWT.PUSH);
		toolItem.setImage(Activator.getDefault().getImage(Activator.ICON_ZOOM_IN));
		toolItem.setToolTipText(Messages.getString("button.enlarge.image"));
		toolItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (mSnapshotComposite != null && mSnapshotComposite.isVisible()) {
					mSnapshotComposite.showEnlargedImage();
				} else if (mRpcComposite != null && mRpcComposite.isVisible()) {
					mRpcComposite.showEnlargedImage();
				}
			}

		});

		Label l = new Label(parent, SWT.NONE);
		l.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		GridLayout layout = (GridLayout)parent.getLayout();
		layout.numColumns++;
		layout.makeColumnsEqualWidth = false;

		super.createButtonsForButtonBar(parent);
		// Set the size of the parent shell
		parent.getShell().setSize(616, 750);
		// Set the dialog position in the middle of the monitor
		setDialogLocation();
	}

	@Override
	public boolean isOverride(final File file) {
		final Object[] result = new Object[1];
		Display.getDefault().syncExec(new Runnable() {

			@Override
			public void run() {
				result[0] = MessageDialog.openQuestion(getShell(), Messages.getString("title.openlegacy"),
						MessageFormat.format(Messages.getString("question.override.file"), file.getName()));
			}
		});

		return (Boolean)result[0];
	}

	@Override
	public void open(File file) {}

	@Override
	public void open(File file, EntityDefinition<?> entityDefinition) {}

	public void setTreeViewerInput(TreeViewerModel treeViewerModel) {
		mTreeViewerModel = treeViewerModel;
		if (mTreeViewer != null) {
			mTreeViewer.setInput(mTreeViewerModel);
			mTreeViewer.collapseAll();
		}
	}

	private void createFirstRow(Composite parent) {
		Composite container = new Composite(parent, SWT.FILL);

		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.marginBottom = 0;
		gl.marginTop = 0;
		gl.marginHeight = 0;
		container.setLayout(gl);

		Composite innerLeftContainer = new Composite(container, SWT.NONE);
		innerLeftContainer.setLayout(new GridLayout(2, false));
		GridData gd = new GridData(SWT.NONE);
		innerLeftContainer.setLayoutData(gd);

		Label label = new Label(innerLeftContainer, SWT.NONE);
		label.setText(Messages.getString("service.generator.service.name"));//$NON-NLS-1$
		gd = new GridData(SWT.NONE);
		label.setLayoutData(gd);

		Text text = new Text(innerLeftContainer, SWT.SINGLE | SWT.BORDER);
		text.clearSelection();
		gd = new GridData(SWT.NONE);
		gd.widthHint = 300;
		text.setLayoutData(gd);
		text.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				serviceName = ((Text)e.widget).getText();
			}
		});

		Composite innerRightContainer = new Composite(container, SWT.NONE);
		innerRightContainer.setLayout(new RowLayout(SWT.VERTICAL));
		gd = new GridData(GridData.END, GridData.CENTER, false, false);
		gd.widthHint = 150;
		gd.horizontalIndent = 40;
		innerRightContainer.setLayoutData(gd);

		// create pool
		Button createPoolBtn = new Button(innerRightContainer, SWT.CHECK);
		createPoolBtn.setText(Messages.getString("service.generator.create.pool"));
		createPoolBtn.setSelection(isCreatePool);
		createPoolBtn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				isCreatePool = !isCreatePool;
			}

		});

		Button generateJUnitBtn = new Button(innerRightContainer, SWT.CHECK);
		generateJUnitBtn.setText(Messages.getString("service.generator.generate.junit"));
		generateJUnitBtn.setSelection(isGenerateJUnit);
		generateJUnitBtn.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				isGenerateJUnit = !isGenerateJUnit;
			}

		});
	}

	private void createSecondRow(Composite parent) {
		GridLayout gl = new GridLayout();
		gl.numColumns = 2;
		gl.marginBottom = 0;
		gl.marginTop = 0;
		gl.marginHeight = 0;
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(gl);

		// first column
		createFirstColOfSecondRow(container);
		// second column
		createSecondColOfSecondRow(container);
	}

	private void createThirdRow(Composite parent) {
		if (ServiceType.SCREEN.equals(serviceType)) {
			mSnapshotComposite = new SnapshotComposite(parent, PathsUtil.toOsLocation(project));
			mSnapshotComposite.setIsScalable(true);
		} else if (ServiceType.RPC.equals(serviceType)) {
			mRpcComposite = new RpcComposite(parent, null);
			mRpcComposite.setIsScalable(true);
		}
	}

	private void createFirstColOfSecondRow(Composite parent) {
		Composite container = new Composite(parent, SWT.FILL);
		GridData gd = new GridData();
		gd.widthHint = 250;
		gd.heightHint = 300;
		gd.verticalSpan = 2;
		container.setLayoutData(gd);
		container.setLayout(new FillLayout(SWT.VERTICAL));
		createTreeControl(container);
	}

	private void createTreeControl(Composite parent) {
		mTreeViewer = new TreeViewer(parent, SWT.SINGLE | SWT.BORDER);
		mTreeViewer.setContentProvider(new TreeViewContentProvider());
		mTreeViewer.setLabelProvider(new ItemLabelProvider());
		mTreeViewer.addSelectionChangedListener(getTreeViewerSelectionListener());
		mTreeViewer.setInput(mTreeViewerModel);
		mTreeViewer.collapseAll();
	}

	private void createSecondColOfSecondRow(Composite parent) {
		// input area
		createInputArea(parent);
		// output area
		createOutputArea(parent);
	}

	private void createInputArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 3;
		container.setLayout(gl);
		// create add/remove buttons
		createInAddRemoveButtons(container);
		// setMiddleArrowButtonsEnabled(false);
		// create Table
		mInTableViewer = createTable(container, Messages.getString("col.input.title"));
		mInTableViewer.setContentProvider(new TableContentProvider());
		mInTableViewer.setLabelProvider(new ItemLabelProvider());
		mInTableViewer.setInput(mInTableModel);
		// create List buttons
		createInTableButtons(container);
	}

	private void createOutputArea(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 3;
		container.setLayout(gl);
		// create add/remove buttons
		createOutAddRemoveButtons(container);
		// setMiddleArrowButtonsEnabled(false);
		// create Table
		mOutTableViewer = createTable(container, Messages.getString("col.output.title"));
		mOutTableViewer.setContentProvider(new TableContentProvider());
		mOutTableViewer.setLabelProvider(new ItemLabelProvider());
		mOutTableViewer.setInput(mOutTableModel);
		// create List buttons
		createOutTableButtons(container);
	}

	private void createInAddRemoveButtons(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		container.setLayout(gl);
		GridData gd = new GridData(SWT.NONE);
		container.setLayoutData(gd);

		mInAddBtn = new Button(container, SWT.ARROW | SWT.RIGHT);
		mInAddBtn.setText(">");
		mInAddBtn.setData(STRUCTURE_TYPE, STRUCTURE_IN);
		mInAddBtn.addSelectionListener(getAddBtnListener());

		mInRemoveBtn = new Button(container, SWT.ARROW | SWT.LEFT);
		mInRemoveBtn.setText("<");
		mInRemoveBtn.setData(STRUCTURE_TYPE, STRUCTURE_IN);
		mInRemoveBtn.addSelectionListener(getRemoveBtnListener());
	}

	private void createOutAddRemoveButtons(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		container.setLayout(gl);
		GridData gd = new GridData(SWT.NONE);
		container.setLayoutData(gd);

		mOutAddBtn = new Button(container, SWT.ARROW | SWT.RIGHT);
		mOutAddBtn.setText(">");
		mOutAddBtn.setData(STRUCTURE_TYPE, STRUCTURE_OUT);
		mOutAddBtn.addSelectionListener(getAddBtnListener());

		mOutRemoveBtn = new Button(container, SWT.ARROW | SWT.LEFT);
		mOutRemoveBtn.setText("<");
		mOutRemoveBtn.setData(STRUCTURE_TYPE, STRUCTURE_OUT);
		mOutRemoveBtn.addSelectionListener(getRemoveBtnListener());
	}

	private static TableViewer createTable(Composite parent, String columnTitle) {
		Table table = new Table(parent, SWT.BORDER | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.SINGLE);
		GridData gd = new GridData(SWT.BORDER);
		gd.widthHint = 250;
		gd.heightHint = 121;
		table.setLayoutData(gd);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		TableViewer tw = new TableViewer(table);
		TableViewerColumn col = new TableViewerColumn(tw, SWT.NONE);
		col.getColumn().setText(columnTitle);
		col.getColumn().setWidth(250);
		return tw;
	}

	private void createInTableButtons(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		container.setLayout(gl);
		mInUpBtn = new Button(container, SWT.ARROW | SWT.UP);
		mInUpBtn.setText("UP");
		mInUpBtn.setData(STRUCTURE_TYPE, STRUCTURE_IN);
		mInUpBtn.addSelectionListener(getUpBtnListener());

		mInDownBtn = new Button(container, SWT.ARROW | SWT.DOWN);
		mInDownBtn.setText("DOWN");
		mInDownBtn.setData(STRUCTURE_TYPE, STRUCTURE_IN);
		mInDownBtn.addSelectionListener(getDownBtnListener());
	}

	private void createOutTableButtons(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout();
		gl.numColumns = 1;
		container.setLayout(gl);
		mOutUpBtn = new Button(container, SWT.ARROW | SWT.UP);
		mOutUpBtn.setText("UP");
		mOutUpBtn.setData(STRUCTURE_TYPE, STRUCTURE_OUT);
		mOutUpBtn.addSelectionListener(getUpBtnListener());

		mOutDownBtn = new Button(container, SWT.ARROW | SWT.DOWN);
		mOutDownBtn.setText("DOWN");
		mOutDownBtn.setData(STRUCTURE_TYPE, STRUCTURE_OUT);
		mOutDownBtn.addSelectionListener(getDownBtnListener());
	}

	private SelectionListener getAddBtnListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String dataValue = (String)e.widget.getData(STRUCTURE_TYPE);
				if (StringUtils.isEmpty(dataValue)) {
					return;
				}
				IStructuredSelection selection = (IStructuredSelection)mTreeViewer.getSelection();
				if (selection.size() == 1) {
					Object element = selection.getFirstElement();
					if (element instanceof AbstractNamedModel && !(element instanceof LoadingModel)) {
						AbstractNamedModel model = (AbstractNamedModel)element;
						// Ivan: no need hide now, maybe in future
						// model.setVisible(false);
						if (STRUCTURE_IN.equals(dataValue)) {
							mInTableModel.addElement(model);
							mInTableViewer.setInput(mInTableModel);
						} else {
							mOutTableModel.addElement(model);
							mOutTableViewer.setInput(mOutTableModel);
						}
					}
				}
			}

		};
	}

	private SelectionListener getRemoveBtnListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String dataValue = (String)e.widget.getData(STRUCTURE_TYPE);
				if (StringUtils.isEmpty(dataValue)) {
					return;
				}
				IStructuredSelection selection = null;
				if (STRUCTURE_IN.equals(dataValue)) {
					selection = (IStructuredSelection)mInTableViewer.getSelection();
				} else {
					selection = (IStructuredSelection)mOutTableViewer.getSelection();
				}
				if (selection != null && selection.size() == 1) {
					Object element = selection.getFirstElement();
					if (element instanceof AbstractNamedModel && !(element instanceof LoadingModel)) {
						AbstractNamedModel model = (AbstractNamedModel)element;
						// model.setVisible(true);
						if (STRUCTURE_IN.equals(dataValue)) {
							mInTableModel.getElements().remove(model);
							mInTableViewer.setInput(mInTableModel);
						} else {
							mOutTableModel.getElements().remove(model);
							mOutTableViewer.setInput(mOutTableModel);
						}
					}
				}
			}

		};
	}

	private SelectionListener getUpBtnListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String dataValue = (String)e.widget.getData(STRUCTURE_TYPE);
				if (StringUtils.isEmpty(dataValue)) {
					return;
				}
				IStructuredSelection selection = null;
				if (STRUCTURE_IN.equals(dataValue)) {
					selection = (IStructuredSelection)mInTableViewer.getSelection();
				} else {
					selection = (IStructuredSelection)mOutTableViewer.getSelection();
				}
				if (selection != null && selection.size() == 1) {
					Object element = selection.getFirstElement();
					if (element instanceof AbstractNamedModel) {
						AbstractNamedModel model = (AbstractNamedModel)element;
						if (STRUCTURE_IN.equals(dataValue)) {
							mInTableModel.moveUp(model);
							mInTableViewer.setInput(mInTableModel);
						} else {
							mOutTableModel.moveUp(model);
							mOutTableViewer.setInput(mOutTableModel);
						}
					}
				}
			}

		};
	}

	private SelectionListener getDownBtnListener() {
		return new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				String dataValue = (String)e.widget.getData(STRUCTURE_TYPE);
				if (StringUtils.isEmpty(dataValue)) {
					return;
				}
				IStructuredSelection selection = null;
				if (STRUCTURE_IN.equals(dataValue)) {
					selection = (IStructuredSelection)mInTableViewer.getSelection();
				} else {
					selection = (IStructuredSelection)mOutTableViewer.getSelection();
				}
				if (selection != null && selection.size() == 1) {
					Object element = selection.getFirstElement();
					if (element instanceof AbstractNamedModel) {
						AbstractNamedModel model = (AbstractNamedModel)element;
						if (STRUCTURE_IN.equals(dataValue)) {
							mInTableModel.moveDown(model);
							mInTableViewer.setInput(mInTableModel);
						} else {
							mOutTableModel.moveDown(model);
							mOutTableViewer.setInput(mOutTableModel);
						}
					}
				}
			}

		};
	}

	private ISelectionChangedListener getTreeViewerSelectionListener() {
		return new ISelectionChangedListener() {

			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection)event.getSelection();
				if (selection != null && selection.size() == 1) {
					Object element = selection.getFirstElement();
					AbstractEntityModel entityModel = getEntityModelFromSelectionElement(element);
					if (entityModel != null && (entityModel instanceof ScreenEntityModel)) {
						showScreenPreviewImage(entityModel.getResourceFile());
						// draw rectangle of field and part.
						// Cannot draw earlier because showPreviewImage clears list of rectangles
						if ((element instanceof ScreenPartModel) || (element instanceof ScreenFieldModel)) {
							setPreviewDrawingRectangle((AbstractNamedModel)element,
									((ScreenEntityModel)entityModel).getTerminalSnapshot());
						}
					} else if (entityModel != null && (entityModel instanceof RpcEntityModel)) {
						showRpcPreviewImage(entityModel.getResourceFile());
					}
				}
			}
		};
	}

	private static AbstractEntityModel getEntityModelFromSelectionElement(Object element) {
		if (element instanceof ScreenEntityModel) {
			return (AbstractEntityModel)element;
		} else if (element instanceof ScreenPartModel) {
			return (AbstractEntityModel)((ScreenPartModel)element).getParent();
		} else if (element instanceof ScreenFieldModel) {
			ScreenFieldModel fieldModel = (ScreenFieldModel)element;
			AbstractNamedModel parent = fieldModel.getParent();
			if (parent instanceof ScreenEntityModel) {
				return (AbstractEntityModel)parent;
			} else if (parent instanceof ScreenPartModel) {
				return (AbstractEntityModel)parent.getParent();
			}
		} else if (element instanceof RpcEntityModel) {
			return (AbstractEntityModel)element;
		} else if (element instanceof RpcPartModel) {
			return (AbstractEntityModel)((RpcPartModel)element).getParent();
		} else if (element instanceof RpcFieldModel) {
			RpcFieldModel fieldModel = (RpcFieldModel)element;
			AbstractNamedModel parent = fieldModel.getParent();
			if (parent instanceof RpcEntityModel) {
				return (AbstractEntityModel)parent;
			} else if (parent instanceof RpcPartModel) {
				return (AbstractEntityModel)parent.getParent();
			}
		}
		return null;
	}

	private void showScreenPreviewImage(IFile resourceFile) {
		if (resourceFile != null && resourceFile.exists()) {
			// show snapshot composite
			mSnapshotComposite.setSnapshotFromPath(new File(resourceFile.getLocationURI()).getAbsolutePath());
			mSnapshotComposite.setVisible(true);
			return;
		}
		// hide image
		mSnapshotComposite.setVisible(false);
	}

	private void showRpcPreviewImage(IFile resourceFile) {
		if (resourceFile != null && resourceFile.exists()) {
			try {
				mRpcComposite.setSource(IOUtils.toString(resourceFile.getContents()));
				mRpcComposite.setVisible(true);
				return;
			} catch (IOException e) {
			} catch (CoreException e) {
			}
		}
		mRpcComposite.setVisible(false);
	}

	private void setPreviewDrawingRectangle(AbstractNamedModel model, TerminalSnapshot terminalSnapshot) {
		if (model instanceof ScreenPartModel) {
			setPreviewDrawingRectangleForPart((ScreenPartModel)model, terminalSnapshot);
		} else if (model instanceof ScreenFieldModel) {
			setPreviewDrawingRectangleForField((ScreenFieldModel)model, terminalSnapshot);
		}
	}

	private void setPreviewDrawingRectangleForPart(ScreenPartModel model, TerminalSnapshot terminalSnapshot) {
		List<ScreenFieldDefinition> sortedFields = model.getDefinition().getSortedFields();
		int startRow = sortedFields.size() > 0 ? (sortedFields.get(0).getPosition() != null ? sortedFields.get(0).getPosition().getRow()
				: 0)
				: 0;
		for (ScreenFieldDefinition fieldDefinition : sortedFields) {
			int row = fieldDefinition.getPosition() != null ? fieldDefinition.getPosition().getRow() : 0;
			if (startRow > row) {
				startRow = row;
			}
		}
		int endRow = sortedFields.size() > 0 ? (sortedFields.get(0).getEndPosition() != null ? sortedFields.get(0).getEndPosition().getRow()
				: 0)
				: 0;
		for (ScreenFieldDefinition fieldDefinition : sortedFields) {
			int row = fieldDefinition.getEndPosition() != null ? fieldDefinition.getEndPosition().getRow() : 0;
			if (endRow < row) {
				endRow = row;
			}
		}
		int startColumn = sortedFields.size() > 0 ? (sortedFields.get(0).getPosition() != null ? sortedFields.get(0).getPosition().getColumn()
				: 0)
				: 0;
		for (ScreenFieldDefinition fieldDefinition : sortedFields) {
			int column = fieldDefinition.getPosition() != null ? fieldDefinition.getPosition().getColumn() : 0;
			if (startColumn > column) {
				startColumn = column;
			}
		}
		int endColumn = sortedFields.size() > 0 ? (sortedFields.get(0).getEndPosition() != null ? sortedFields.get(0).getEndPosition().getColumn()
				: 0)
				: 0;
		for (ScreenFieldDefinition fieldDefinition : sortedFields) {
			int column = fieldDefinition.getEndPosition() != null ? fieldDefinition.getEndPosition().getColumn() : 0;
			if (endColumn < column) {
				endColumn = column;
			}
		}
		mSnapshotComposite.addDrawingRectangle(getRectangle(startRow, endRow, startColumn, endColumn, "", terminalSnapshot),
				SWT.COLOR_YELLOW, true);
	}

	private void setPreviewDrawingRectangleForField(ScreenFieldModel model, TerminalSnapshot terminalSnapshot) {
		ScreenFieldDefinition definition = model.getDefinition();
		boolean isRectangle = definition.isRectangle();

		int row = definition.getPosition() != null ? definition.getPosition().getRow() : 0;
		int column = definition.getPosition() != null ? definition.getPosition().getColumn() : 0;
		int endRow = definition.getEndPosition() != null ? definition.getEndPosition().getRow() : 0;
		int endColumn = definition.getEndPosition() != null ? definition.getEndPosition().getColumn() : 0;

		TerminalPosition endPosition = null;
		if (isRectangle) {
			endPosition = new SimpleTerminalPosition(endRow, endColumn);
		} else {
			endPosition = new SimpleTerminalPosition(row, endColumn);
		}

		mSnapshotComposite.addDrawingRectangle(
				getRectangle(row, endPosition.getRow(), column, endPosition.getColumn(), "", terminalSnapshot), SWT.COLOR_YELLOW, true);//$NON-NLS-1$
		// add rectangle for @ScreenDescriptionField
		if (definition.getDescriptionFieldDefinition() != null
				&& definition.getDescriptionFieldDefinition().getPosition() != null
				&& definition.getDescriptionFieldDefinition().getPosition().getColumn() > 0) {
			ScreenFieldDefinition descDefinition = definition.getDescriptionFieldDefinition();
			if (descDefinition.getPosition().getRow() > 0) {
				row = descDefinition.getPosition().getRow();
			}
			column = descDefinition.getPosition().getColumn();
			endColumn = descDefinition.getEndPosition().getColumn();
			mSnapshotComposite.addDrawingRectangle(
					getRectangle(row, row, column, endColumn, "", terminalSnapshot), SWT.COLOR_YELLOW);//$NON-NLS-1$
		}
	}

	private static Rectangle getRectangle(int row, int endRow, int column, int endColumn, String value,
			TerminalSnapshot terminalSnapshot) {
		if (terminalSnapshot == null) {
			return new Rectangle(0, 0, 0, 0);
		}
		DefaultTerminalSnapshotImageRenderer renderer = new DefaultTerminalSnapshotImageRenderer();

		int length = 0;
		if (endColumn == 0) {
			SimpleTerminalPosition terminalPosition = new SimpleTerminalPosition(row, column);
			TerminalField field = terminalSnapshot.getField(terminalPosition);
			if (field != null) {
				TerminalPosition endPosition = field.getEndPosition();
				endColumn = endPosition.getColumn();
			} else {
				// if column attribute was changed then calculate endColumn
				endColumn = column + value.length() - 1;
			}
		}
		length = endColumn - column + 1;

		int x = renderer.toWidth(column - 1 + renderer.getLeftColumnsOffset());
		int y = renderer.toHeight(row - 1) + renderer.getTopPixelsOffset();
		int width = renderer.toWidth(length);
		int height = renderer.toHeight(1);
		if (endRow >= row) {
			height = renderer.toHeight(endRow - row + 1);
		}
		return new Rectangle(x, y, width, height);
	}

	private boolean validate() {
		if (StringUtils.isEmpty(serviceName)) {
			MessageDialog.openError(getParentShell(), Messages.getString("title.openlegacy"),//$NON-NLS-1$
					Messages.getString("error.service.name.cannot.be.empty"));//$NON-NLS-1$
			return false;
		}
		if (mInTableModel.getElements().isEmpty() && mOutTableModel.getElements().isEmpty()) {
			MessageDialog.openError(getParentShell(), Messages.getString("title.openlegacy"),//$NON-NLS-1$
					Messages.getString("error.no.input.and.output"));//$NON-NLS-1$
			return false;
		}
		return true;
	}

	private void executeGenerate() {
		Job job = new Job(Messages.getString("job.generating.service")) {//$NON-NLS-1$

			@Override
			protected IStatus run(final IProgressMonitor monitor) {
				monitor.beginTask(Messages.getString("task.generating"), mInTableModel.getElements().size()//$NON-NLS-1$
						+ mOutTableModel.getElements().size() + 1);

				DesignTimeExecuter executer = new DesignTimeExecuterImpl();

				GenerateServiceRequest request = new GenerateServiceRequest();
				request.setServiceType(serviceType);
				List<ServiceParameter> inputParameters = request.getInputParameters();
				List<AbstractNamedModel> inputElements = mInTableModel.getElements();
				for (AbstractNamedModel model : inputElements) {
					ServiceParameter parameter = getServiceParameter(model);
					if (parameter != null) {
						inputParameters.add(parameter);
					}
					monitor.worked(1);
				}

				List<ServiceParameter> outputParameters = request.getOutputParameters();
				List<AbstractNamedModel> outputElements = mOutTableModel.getElements();
				for (AbstractNamedModel model : outputElements) {
					ServiceParameter parameter = getServiceParameter(model);
					if (parameter != null) {
						outputParameters.add(parameter);
					}
					monitor.worked(1);
				}

				File projectOsLocation = PathsUtil.toOsLocation(project);
				String apiPackage = executer.getPreferences(projectOsLocation, PreferencesConstants.API_PACKAGE);
				if (StringUtils.isEmpty(apiPackage)) {
					MessageDialog.openError(getParentShell(), Messages.getString("title.openlegacy"),//$NON-NLS-1$
							Messages.getString("error.cannot.find.api.package.in.preferences"));//$NON-NLS-1$
					return Status.CANCEL_STATUS;
				}

				String sourceFolder = executer.getPreferences(projectOsLocation, PreferencesConstants.API_SOURCE_FOLDER);
				if (StringUtils.isEmpty(sourceFolder)) {
					sourceFolder = PreferencesConstants.API_SOURCE_FOLDER_DEFAULT;
				}
				// set service name
				request.setServiceName(serviceName);
				// set generate test
				request.setGenerateTest(isGenerateJUnit);
				// set generate pool
				request.setGeneratePool(isCreatePool);
				// set project path
				request.setProjectPath(projectOsLocation);
				// set source dir
				request.setSourceDirectory(new File(projectOsLocation, sourceFolder));
				// set package dir
				request.setPackageDirectory(PathsUtil.packageToPath(apiPackage + SERVICES_SUFFIX));
				// set templates dir
				request.setTemplatesDirectory(new File(projectOsLocation, DesignTimeExecuterImpl.TEMPLATES_DIR));
				// set user interaction
				request.setUserInteraction(GenerateServiceDialog.this);

				executer.generateService(request);

				Display.getDefault().syncExec(new Runnable() {

					@Override
					public void run() {
						try {
							monitor.worked(1);
							ResourcesPlugin.getWorkspace().getRoot().refreshLocal(IResource.DEPTH_INFINITE, monitor);
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

	private static ServiceParameter getServiceParameter(AbstractNamedModel model) {
		if (model instanceof ScreenEntityModel) {
			return new ServiceEntityParameter(((ScreenEntityModel)model).getDefinition());
		} else if (model instanceof ScreenPartModel) {
			return new ServicePartParameter(((ScreenEntityModel)model.getParent()).getDefinition(),
					((ScreenPartModel)model).getDefinition());
		} else if (model instanceof ScreenFieldModel) {
			AbstractNamedModel parent = model.getParent();
			if (parent instanceof ScreenEntityModel) {
				return new ServiceEntityFieldParameter(((ScreenEntityModel)parent).getDefinition(),
						((ScreenFieldModel)model).getDefinition());
			} else {
				return new ServiceEntityFieldParameter(((ScreenEntityModel)parent.getParent()).getDefinition(),
						((ScreenFieldModel)model).getDefinition());
			}
		} else if (model instanceof RpcEntityModel) {
			return new ServiceEntityParameter(((RpcEntityModel)model).getDefinition());
		} else if (model instanceof RpcPartModel) {
			return new ServicePartParameter(((RpcEntityModel)model.getParent()).getDefinition(),
					((RpcPartModel)model).getDefinition());
		} else if (model instanceof RpcFieldModel) {
			AbstractNamedModel parent = model.getParent();
			if (parent instanceof RpcEntityModel) {
				return new ServiceEntityFieldParameter(((RpcEntityModel)parent).getDefinition(),
						((RpcFieldModel)model).getDefinition());
			} else {
				return new ServiceEntityFieldParameter(((RpcEntityModel)parent.getParent()).getDefinition(),
						((RpcFieldModel)model).getDefinition());
			}
		}
		return null;
	}

	/**
	 * Method used to set the dialog in the centre of the monitor
	 */
	private void setDialogLocation() {
		Rectangle monitorArea = getShell().getDisplay().getPrimaryMonitor().getBounds();
		Rectangle shellArea = getShell().getBounds();
		int x = monitorArea.x + (monitorArea.width - shellArea.width) / 2;
		int y = monitorArea.y + (monitorArea.height - shellArea.height) / 2;
		getShell().setLocation(x, y);
	}

}
