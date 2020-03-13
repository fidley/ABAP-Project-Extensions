package com.abapblog.adt.extension.passwords.projectExplorer;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;

import com.abapblog.adt.extension.dialogs.ChangePasswordDialog;
import com.abapblog.adt.extension.passwords.logonService.ILogonService;
import com.abapblog.adt.extension.passwords.logonService.LogonServiceFactory;
import com.abapblog.adt.extension.passwords.secureStorage.SecureStorage;
import com.abapblog.adt.extension.passwords.view.PasswordView;
import com.abapblog.adt.extension.preferences.Preferences;

public class ProjectListener implements IResourceChangeListener {

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		if (event.getType() == IResourceChangeEvent.POST_CHANGE) {
			try {
				List<IProject> projects = getProjects(event.getDelta());
				// do something with new projects
				for (IProject project : projects) {
					ILogonService logonService = LogonServiceFactory.create();
					if (logonService.isAdtProject(project)) {
						if (Preferences.askForPasswordAtProjectCreation())
							showPasswordDialog(project, logonService);
					}
				}
			} catch (Exception e) {

			}
		}
	}

	private void showPasswordDialog(IProject project, ILogonService logonService) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				SecureStorage secureStorage = new SecureStorage();
				ChangePasswordDialog passwordDialog = new ChangePasswordDialog(getShell());
				passwordDialog.create();
				if (passwordDialog.open() == Window.OK) {
					secureStorage.createNodesForSAPProjects();
					secureStorage.changePasswordForUser(logonService.getProjectName(project),
							logonService.getClientForProject(project), logonService.getUserForProject(project),
							passwordDialog.getPassword(), passwordDialog.getEncryptValue());
					PasswordView.refreshViewer();
				}
			}
		});
	}

	private List<IProject> getProjects(IResourceDelta delta) {
		final List<IProject> projects = new ArrayList<IProject>();
		try {
			delta.accept(new IResourceDeltaVisitor() {
				@Override
				public boolean visit(IResourceDelta delta) throws CoreException {
					if (delta.getKind() == IResourceDelta.ADDED && delta.getResource().getType() == IResource.PROJECT) {
						IProject project = (IProject) delta.getResource();
						if (project.isAccessible()) {
							projects.add(project);
						}
					}
					// only continue for the workspace root
					return delta.getResource().getType() == IResource.ROOT;
				}
			});
		} catch (CoreException e) {
			// handle error
		}
		return projects;
	}

	private Shell getShell() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
	}
}