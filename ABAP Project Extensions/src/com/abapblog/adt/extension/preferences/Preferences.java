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

	public static String getReadReleaseNotesVersion() {
		return store.getString(PreferenceConstants.readReleaseNotesVersion);
	}

	public static void saveReadReleaseNotesVersion() {
		store.setValue(PreferenceConstants.readReleaseNotesVersion, Activator.getDefault().getVersion());
	}

	public static Boolean showTransactionInputOnToolbar() {
		return store.getBoolean(PreferenceConstants.ShowTransactionInputOnToolbar);
	}

}
