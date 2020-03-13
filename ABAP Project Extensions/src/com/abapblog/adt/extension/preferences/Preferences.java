package com.abapblog.adt.extension.preferences;

import org.eclipse.jface.preference.IPreferenceStore;

import com.abapblog.adt.extension.Activator;

public class Preferences {

	private final static IPreferenceStore store = Activator.getDefault().getPreferenceStore();

	public static Boolean doAutomaticLogonAtStart() {
		return store.getBoolean(PreferenceConstants.doAutomaticLogonAtStart);
	}

	public static Boolean automaticLogonOnlyForStoredPasswords() {
		return store.getBoolean(PreferenceConstants.AutomaticLogonOnlyForStoredPasswords);
	}

	public static Boolean askForPasswordAtProjectCreation() {
		return store.getBoolean(PreferenceConstants.askForPasswordAtProjectCreation);
	}

	public static Boolean doAutomaticLogonAtExpandOfProject() {
		return store.getBoolean(PreferenceConstants.doAutomaticLogonAtExpandOfProject);
	}

	public static Boolean automaticLogonForAllPossibleProjects() {
		return store.getBoolean(PreferenceConstants.AutomaticLogonForAllPossibleProjects);
	}

}
