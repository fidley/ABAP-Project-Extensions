package com.abapblog.adt.extension.commands;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.IHandlerListener;
import org.eclipse.jface.preference.IPreferenceNode;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

import com.abapblog.adt.extension.preferences.AbapTasks;
import com.abapblog.adt.extension.preferences.PreferencesDialog;

public class OpenPreferences implements IHandler {

	@Override
	public void addHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// Get all preference pages from your plugin
		IPreferenceNode rootNode = PlatformUI.getWorkbench().getPreferenceManager().find(PreferencesDialog.ID);

		List<String> pageIds = new ArrayList<>();

		// Always include the main pages
		pageIds.add(PreferencesDialog.ID);
		pageIds.add(AbapTasks.ID);

		// If root node exists, collect all its children
		if (rootNode != null) {
			collectPreferencePageIds(rootNode, pageIds);
		}

		// Open the preference dialog with all the specified pages
		PreferencesUtil.createPreferenceDialogOn(null, PreferencesDialog.ID, pageIds.toArray(new String[0]), null)
				.open();
		return null;
	}

	// Helper method to collect all preference page IDs recursively
	private void collectPreferencePageIds(IPreferenceNode node, List<String> pageIds) {
		if (node.getId() != null && !pageIds.contains(node.getId())) {
			pageIds.add(node.getId());
		}

		// Process child nodes recursively
		for (IPreferenceNode child : node.getSubNodes()) {
			collectPreferencePageIds(child, pageIds);
		}
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isHandled() {
		return true;
	}

	@Override
	public void removeHandlerListener(IHandlerListener handlerListener) {
		// TODO Auto-generated method stub

	}

}
