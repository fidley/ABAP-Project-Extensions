package com.abapblog.adt.extension.preferences.project;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.preference.IPreferenceStore;

import com.abapblog.adt.extension.Activator;
import com.abapblog.adt.extension.passwords.logonService.ILogonService;
import com.abapblog.adt.extension.passwords.logonService.LogonServiceFactory;
import com.abapblog.adt.extension.preferences.PreferenceConstants;

public class UserPreferences {

	public static final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

	private Boolean automaticLogon;
	private String userPreferenceKeyForAutomaticLogon;

	public UserPreferences(String projectName, String client, String user) {
		setUserPreferenceKeyForAutomaticLogon(
				PreferenceConstants.AutomaticLogonForProjectPrefix + projectName + "@and@" + client + "@and@" + user);
		setDefaultValueForAutomaticLogon();
		setAutomaticLogon(store.getBoolean(getUserPreferenceKeyForAutomaticLogon()));
	}

	public UserPreferences(IProject adtProject) {
		ILogonService logonService = LogonServiceFactory.create();
		setUserPreferenceKeyForAutomaticLogon(PreferenceConstants.AutomaticLogonForProjectPrefix
				+ logonService.getProjectName(adtProject) + "@and@" + logonService.getClientForProject(adtProject)
				+ "@and@" + logonService.getUserForProject(adtProject));
		setDefaultValueForAutomaticLogon();
		setAutomaticLogon(store.getBoolean(getUserPreferenceKeyForAutomaticLogon()));
	}

	private void setDefaultValueForAutomaticLogon() {
		store.setDefault(getUserPreferenceKeyForAutomaticLogon(), true);
	}

	public Boolean getAutomaticLogon() {
		return automaticLogon;
	}

	public void setAutomaticLogon(Boolean automaticLogon) {
		this.automaticLogon = automaticLogon;
		store.setValue(getUserPreferenceKeyForAutomaticLogon(), automaticLogon);
	}

	private String getUserPreferenceKeyForAutomaticLogon() {
		return userPreferenceKeyForAutomaticLogon;
	}

	private void setUserPreferenceKeyForAutomaticLogon(String userPreferenceKey) {
		userPreferenceKeyForAutomaticLogon = userPreferenceKey;
	}

}
