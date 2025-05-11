package com.abapblog.adt.extension.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.abapblog.adt.extension.Activator;

public class PreferencesDialog extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	private final IPreferenceStore store;
	private Boolean doAutomaticLogonAtStart = false;
	private Boolean AutomaticLogonOnlyForStoredPasswords = false;
	private Boolean doAutomaticLogonAtExpandOfProject = false;
	private Boolean askForPasswordAtProjectCreation = false;
	private Boolean AutomaticLogonForAllPossibleProjects = false;
	private Boolean ShowTransactionInputOnToolbar = false;
	public static final String ID = "com.abapblog.adt.extension.preferences.PreferencesDialog"; //$NON-NLS-1$

	public PreferencesDialog() {
		super(GRID);
		this.store = Activator.getDefault().getPreferenceStore();
		setPreferenceStore(this.store);
		setDescription("Settings for ABAP Favorites plugin");
	}

	@Override
	public void createFieldEditors() {
		addField(new BooleanFieldEditor(PreferenceConstants.doAutomaticLogonAtStart,
				"&Logon Automatically at Eclipse Start?", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.AutomaticLogonOnlyForStoredPasswords,
				"&Logon Automatically only to Systems with stored password?", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.AutomaticLogonForAllPossibleProjects,
				"&Logon Automatically for all stored users?", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.doAutomaticLogonAtExpandOfProject,
				"&Logon Automatically at expand of the project?", getFieldEditorParent()));
		addField(new BooleanFieldEditor(PreferenceConstants.askForPasswordAtProjectCreation,
				"&Ask for password to be stored at creation of new project?", getFieldEditorParent()));
//		addField(new BooleanFieldEditor(PreferenceConstants.ShowTransactionInputOnToolbar,
//				"&Show transaction input field on main toolbar", getFieldEditorParent()));

	}

	@Override
	public void init(final IWorkbench workbench) {
		this.doAutomaticLogonAtStart = Preferences.doAutomaticLogonAtStart();
		this.AutomaticLogonOnlyForStoredPasswords = Preferences.automaticLogonOnlyForStoredPasswords();
		this.askForPasswordAtProjectCreation = Preferences.askForPasswordAtProjectCreation();
		this.doAutomaticLogonAtExpandOfProject = Preferences.doAutomaticLogonAtExpandOfProject();
		this.AutomaticLogonForAllPossibleProjects = Preferences.automaticLogonForAllPossibleProjects();
		this.ShowTransactionInputOnToolbar = Preferences.showTransactionInputOnToolbar();
	}

	@Override
	protected void performApply() {
		super.performApply();
	}

//Apply&Close
	@Override
	public boolean performOk() {

		final Boolean ApplyClose = super.performOk();
		return ApplyClose;
	}

}