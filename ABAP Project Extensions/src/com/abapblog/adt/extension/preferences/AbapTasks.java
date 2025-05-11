package com.abapblog.adt.extension.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.abapblog.adt.extension.Activator;

public class AbapTasks extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {
	private final IPreferenceStore store;
	private Boolean enableAbapTaskMining = false;
	public static final String ID = "com.abapblog.adt.extension.preferences.abaptasks"; //$NON-NLS-1$

	public AbapTasks() {
		super(GRID);
		this.store = Activator.getDefault().getPreferenceStore();
		setPreferenceStore(this.store);
		setDescription("ADT Poject Extensions ABAP Tags Mining settings");
	}

	@Override
	public void init(IWorkbench workbench) {
		this.enableAbapTaskMining = Preferences.enableAbapTaskMining();

	}

	@Override
	protected void createFieldEditors() {
		addField(new BooleanFieldEditor(PreferenceConstants.enableAbapTaskMining, "&Enable ABAP tasks mining",
				getFieldEditorParent()));

	}

}
