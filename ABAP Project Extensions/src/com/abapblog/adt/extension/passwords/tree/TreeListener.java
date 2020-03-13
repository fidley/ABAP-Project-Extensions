package com.abapblog.adt.extension.passwords.tree;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ITreeViewerListener;
import org.eclipse.jface.viewers.TreeExpansionEvent;

import com.abapblog.adt.extension.preferences.project.ClientPreferences;
import com.abapblog.adt.extension.preferences.project.ProjectPreferences;
import com.abapblog.adt.extension.preferences.project.UserPreferences;

public class TreeListener implements ITreeViewerListener {

	private CheckboxTreeViewer viewer;

	public TreeListener(CheckboxTreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void treeCollapsed(TreeExpansionEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void treeExpanded(TreeExpansionEvent event) {
		final TreeParent element = (TreeParent) event.getElement();
		final Object[] children = element.getChildren();
		for (Object child : children) {
			if (child instanceof TreeParent) {
				TreeParent parent = (TreeParent) child;
				if (parent.getClient().equals("")) {
					viewer.setChecked(child,
							new ProjectPreferences(parent.getProject()).getAutomaticLogon());
				} else {
					viewer.setChecked(child,
							new ClientPreferences(parent.getProject(), parent.getClient()).getAutomaticLogon());
				}
			} else if (child instanceof TreeObject) {
				TreeObject userNode = (TreeObject) child;
				viewer.setChecked(child,
						new UserPreferences(userNode.getProject(), userNode.getClient(), userNode.getUser())
								.getAutomaticLogon());
			}

		}
	}

}
