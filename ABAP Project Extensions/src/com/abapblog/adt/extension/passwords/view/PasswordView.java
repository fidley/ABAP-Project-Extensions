package com.abapblog.adt.extension.passwords.view;

import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.part.ViewPart;

import com.abapblog.adt.extension.passwords.secureStorage.SecureStorage;
import com.abapblog.adt.extension.passwords.tree.CheckBoxFilteredTree;
import com.abapblog.adt.extension.passwords.tree.CheckStateListener;
import com.abapblog.adt.extension.passwords.tree.CheckStateProvider;
import com.abapblog.adt.extension.passwords.tree.TreeListener;
import com.abapblog.adt.extension.passwords.tree.TreeObject;
import com.abapblog.adt.extension.passwords.tree.TreeParent;
import com.abapblog.adt.extension.passwords.tree.TreePatternFilter;
import com.abapblog.adt.extension.preferences.project.ProjectPreferences;

public class PasswordView extends ViewPart {
	private final String ID = "com.abapblog.adt.extension.passwords.view";
	private Actions actions = new Actions();
	private static CheckboxTreeViewer viewer;

	@Override
	public void createPartControl(Composite parent) {
		createSecureStorageNodes();
		createTreeViewer(parent);

	}

	private void createSecureStorageNodes() {
		SecureStorage secureStorage = new SecureStorage();
		secureStorage.createNodesForSAPProjects();
	}

	private void createTreeViewer(Composite parent) {
		TreePatternFilter filter = new TreePatternFilter();
		final CheckBoxFilteredTree filteredTree = new CheckBoxFilteredTree(parent,
				SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CHECK, filter, true, true);
		final ColumnControlListener columnListener = new ColumnControlListener();
		columnListener.setID(getID());
		viewer = (CheckboxTreeViewer) filteredTree.getViewer();

		addCheckStateProvider();
		addCheckStateListener();
		addDoubleClickAction(viewer);
		Tree tree = viewer.getTree();
		addTreeExpansionListener();
		setTreeColumns(columnListener, tree);
		viewer.setContentProvider(new ViewContentProvider(getViewSite()));
		viewer.setInput(getViewSite());
		viewer.setLabelProvider(new ViewLabelProvider());
		hookContextMenu();
		refreshViewer();
	}

	private void addCheckStateProvider() {
		viewer.setCheckStateProvider(new CheckStateProvider());
	}

	private void addTreeExpansionListener() {
		viewer.addTreeListener(new TreeListener(viewer));
	}

	private void addCheckStateListener() {
		viewer.addCheckStateListener(new CheckStateListener(viewer));
	}

	public static void refreshViewer() {

		if (viewer != null) {

			final ViewContentProvider ContentProvider = (ViewContentProvider) viewer.getContentProvider();
			try {
				ContentProvider.initialize();
				for (TreeObject child : ContentProvider.getInvisibleRoot().getChildren()) {
					if (child instanceof TreeParent) {
						TreeParent project = (TreeParent) child;
						viewer.setChecked(project, new ProjectPreferences(project.getProject()).getAutomaticLogon());
					}
				}
			} catch (final Exception e) {

			}
			viewer.refresh();
		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	@Override
	public void setFocus() {

	}

	public String getID() {
		return ID;
	}

	private void setTreeColumns(final ColumnControlListener columnListener, final Tree tree) {
		tree.setHeaderVisible(true);
		final TreeColumn columnUser = new TreeColumn(tree, SWT.LEFT);
		columnUser.setText("Project/Client/User");
		columnUser.addControlListener(columnListener);
		loadColumnSettings(columnUser);
		final TreeColumn columnPassword = new TreeColumn(tree, SWT.LEFT);
		columnPassword.setText("Password");
		columnPassword.addControlListener(columnListener);
		loadColumnSettings(columnPassword);
		final TreeColumn ColumnEncrypted = new TreeColumn(tree, SWT.LEFT);
		ColumnEncrypted.setText("Encrypted");
		ColumnEncrypted.addControlListener(columnListener);
		loadColumnSettings(ColumnEncrypted);

	}

	protected void hookContextMenu() {
		final MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		ContextMenu contextMenu = new ContextMenu(viewer);

		menuMgr.addMenuListener(manager -> contextMenu.fillContextMenu(manager));
		final Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	protected void loadColumnSettings(final TreeColumn Column) {
		final IEclipsePreferences prefs = InstanceScope.INSTANCE.getNode(getID());
		Column.setWidth(prefs.getInt("column_width" + Column.getText(), 300));
	}

	protected void addDoubleClickAction(TreeViewer viewer) {
		actions.createDoubleClick(viewer);
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			@Override
			public void doubleClick(DoubleClickEvent event) {
				actions.doubleClick.run();
				refreshViewer();
			}
		});
	}
}