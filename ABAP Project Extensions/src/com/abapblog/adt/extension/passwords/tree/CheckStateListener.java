package com.abapblog.adt.extension.passwords.tree;

import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;

import com.abapblog.adt.extension.preferences.project.ClientPreferences;
import com.abapblog.adt.extension.preferences.project.ProjectPreferences;
import com.abapblog.adt.extension.preferences.project.UserPreferences;

public class CheckStateListener implements ICheckStateListener {

	private CheckboxTreeViewer viewer;

	public CheckStateListener(CheckboxTreeViewer viewer) {
		this.viewer = viewer;
	}

	@Override
	public void checkStateChanged(CheckStateChangedEvent event) {
		if (event.getChecked()) {

			viewer.setSubtreeChecked(event.getElement(), true);
			changeStateInPreferencesToAllChildren(event);

		} else {

			final TreeObject element = (TreeObject) event.getElement();
			changeStateForObjectAndParents(element);
		}
	}

	private void changeStateForObjectAndParents(final TreeObject node) {
		viewer.setChecked(node, false);
		changeStateInPreferences(node, false);
		changeParentState(node, false);
	}

	private void changeParentState(final TreeObject node, Boolean state) {
		TreeParent parent = node.getParent();
		if (!parent.getType().equals(TreeParent.TypeOfFolder.Root)) {
			viewer.setChecked(parent, false);
			changeStateInPreferences(parent, state);
			changeParentState(parent, state);

		}
	}

	private void changeStateInPreferencesToAllChildren(CheckStateChangedEvent event) {
		try {
			final TreeParent element = (TreeParent) event.getElement();
			changeStateInPreferences(element, true);
			final TreeObject[] children = element.getChildren();
			for (TreeObject child : children) {
				changeStateInPreferences(child, true);
				if (child instanceof TreeParent) {
					TreeParent client = (TreeParent) child;
					TreeObject[] users = client.getChildren();
					for (TreeObject user : users) {
						changeStateInPreferences(user, true);
					}

				}
			}
		} catch (Exception e) {
			changeStateInPreferences((TreeObject) event.getElement(), true);
		}
	}

	private void changeStateInPreferences(final TreeObject node, Boolean state) {
		if (node instanceof TreeParent) {
			TreeParent parent = (TreeParent) node;
			if (parent.getType().equals(TreeParent.TypeOfFolder.Project)) {
				new ProjectPreferences(parent.getProject()).setAutomaticLogon(state);
			} else {
				new ClientPreferences(parent.getProject(), parent.getClient()).setAutomaticLogon(state);
			}
		} else {
			new UserPreferences(node.getProject(), node.getClient(), node.getUser()).setAutomaticLogon(state);
		}
	}

}
