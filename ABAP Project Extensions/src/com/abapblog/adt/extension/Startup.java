package com.abapblog.adt.extension;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.ui.IStartup;

import com.abapblog.adt.extension.commands.LogToAllSAPSystemsHandler;
import com.abapblog.adt.extension.passwords.projectExplorer.ProjectListener;
import com.abapblog.adt.extension.preferences.Preferences;

public class Startup implements IStartup {

	@Override
	public void earlyStartup() {

		addProjectListener();
		logonToAllAdtSystems();
//		Display.getDefault().asyncExec(new Runnable() {
//			@Override
//			public void run() {
//
//				Shell myShell = new Shell(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
//						SWT.SHELL_TRIM);
//				// Very important, generally parents must have a layout set to display children
//				myShell.setLayout(new FillLayout());
//				Browser browser = new Browser(myShell, SWT.NONE);
//				browser.setUrl("../releaseNotes/releaseNotes.md");
//				myShell.layout();
//				myShell.open();
//
//			}
//		});

	}

	private void logonToAllAdtSystems() {
		LogToAllSAPSystemsHandler logToAllSystems = new LogToAllSAPSystemsHandler();
		try {
			if (Preferences.doAutomaticLogonAtStart())
				logToAllSystems.execute(new ExecutionEvent());
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void addProjectListener() {
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		workspace.addResourceChangeListener(new ProjectListener());
	}
}
