package com.abapblog.adt.extension.preferences.project;

import org.eclipse.jface.preference.IPreferenceStore;

import com.abapblog.adt.extension.Activator;
import com.abapblog.adt.extension.preferences.PreferenceConstants;

public class ProjectPreferences {

	public static final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

	private Boolean automaticLogon;
	private String projectPreferenceKeyForAutomaticLogon;

	public ProjectPreferences(String projectName) {
		setProjectPreferenceKeyForAutomaticLogon(
				PreferenceConstants.AutomaticLogonForProjectPrefix + projectName);
		setDefaultValueForAutomaticLogon();
		setAutomaticLogon(store.getBoolean(getProjectPreferenceKeyForAutomaticLogon()));
	}

	private void setDefaultValueForAutomaticLogon() {
		store.setDefault(getProjectPreferenceKeyForAutomaticLogon(), true);
	}

	public Boolean getAutomaticLogon() {
		return automaticLogon;
	}

	public void setAutomaticLogon(Boolean automaticLogon) {
		this.automaticLogon = automaticLogon;
		store.setValue(getProjectPreferenceKeyForAutomaticLogon(), automaticLogon);
	}

	private String getProjectPreferenceKeyForAutomaticLogon() {
		return projectPreferenceKeyForAutomaticLogon;
	}

	private void setProjectPreferenceKeyForAutomaticLogon(String projectPreferenceKey) {
		projectPreferenceKeyForAutomaticLogon = projectPreferenceKey;
	}

}
