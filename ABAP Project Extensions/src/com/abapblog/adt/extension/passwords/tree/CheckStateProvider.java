package com.abapblog.adt.extension.passwords.tree;

import org.eclipse.jface.viewers.ICheckStateProvider;

import com.abapblog.adt.extension.preferences.project.ClientPreferences;
import com.abapblog.adt.extension.preferences.project.ProjectPreferences;
import com.abapblog.adt.extension.preferences.project.UserPreferences;

public class CheckStateProvider implements ICheckStateProvider {

	@Override
	public boolean isChecked(Object element) {

		TreeObject node = (TreeObject) element;

		try {
			TreeParent parent = (TreeParent) node;
			if (parent.getType().equals(TreeParent.TypeOfFolder.Project)) {
				return new ProjectPreferences(node.getProject()).getAutomaticLogon();
			} else if (parent.getType().equals(TreeParent.TypeOfFolder.Client)) {
				return new ClientPreferences(node.getProject(), node.getClient()).getAutomaticLogon();
			}

		} catch (Exception e) {
			return new UserPreferences(node.getProject(), node.getClient(), node.getUser()).getAutomaticLogon();
		}
		return false;
	}

	@Override
	public boolean isGrayed(Object arg0) {
		// TODO Auto-generated method stub
		return false;
	}

}
