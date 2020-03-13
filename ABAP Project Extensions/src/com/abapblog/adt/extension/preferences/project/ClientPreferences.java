package com.abapblog.adt.extension.preferences.project;

import org.eclipse.jface.preference.IPreferenceStore;

import com.abapblog.adt.extension.Activator;
import com.abapblog.adt.extension.preferences.PreferenceConstants;

public class ClientPreferences {

	public static final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

	private Boolean automaticLogon;
	private String clientPreferenceKeyForAutomaticLogon;

	public ClientPreferences(String projectName, String client) {
		setClientPreferenceKeyForAutomaticLogon(
				PreferenceConstants.AutomaticLogonForProjectPrefix + projectName + "@and@" + client);
		setDefaultValueForAutomaticLogon();
		setAutomaticLogon(store.getBoolean(getClientPreferenceKeyForAutomaticLogon()));
	}

	private void setDefaultValueForAutomaticLogon() {
		store.setDefault(getClientPreferenceKeyForAutomaticLogon(), true);
	}

	public Boolean getAutomaticLogon() {
		return automaticLogon;
	}

	public void setAutomaticLogon(Boolean automaticLogon) {
		this.automaticLogon = automaticLogon;
		store.setValue(getClientPreferenceKeyForAutomaticLogon(), automaticLogon);
	}

	private String getClientPreferenceKeyForAutomaticLogon() {
		return clientPreferenceKeyForAutomaticLogon;
	}

	private void setClientPreferenceKeyForAutomaticLogon(String clientPreferenceKey) {
		clientPreferenceKeyForAutomaticLogon = clientPreferenceKey;
	}

}
