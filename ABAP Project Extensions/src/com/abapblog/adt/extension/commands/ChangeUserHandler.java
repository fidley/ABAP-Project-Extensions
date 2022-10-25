package com.abapblog.adt.extension.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.abapblog.adt.extension.dialogs.UserDialog;
import com.abapblog.adt.extension.passwords.logonService.ILogonService;
import com.abapblog.adt.extension.passwords.logonService.LogonServiceFactory;
import com.sap.adt.destinations.model.IDestinationData;
import com.sap.adt.destinations.model.IDestinationDataWritable;
import com.sap.adt.project.IAdtCoreProject;
import com.sap.adt.tools.core.ui.userinfo.AdtUserServiceUIFactory;
import com.sap.adt.tools.core.ui.userinfo.IAdtUserServiceUI;

public class ChangeUserHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent arg0) throws ExecutionException {

		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection();
			Object firstElement = selection.getFirstElement();
			if (firstElement instanceof IAdaptable) {

				IProject project = ((IAdaptable) firstElement).getAdapter(IProject.class);
				IAdtCoreProject AdtProject = project.getAdapter(IAdtCoreProject.class);
				IDestinationData DestinationData = AdtProject.getDestinationData();
				IDestinationDataWritable DestinationDataWritable = DestinationData.getWritable();

				String userName = getNewUserName(project, DestinationDataWritable.getUser());
				if (userName != null) {
					DestinationDataWritable.setUser(userName);
					IDestinationData newDestinationData = DestinationDataWritable.getReadOnlyClone();
					AdtProject.setDestinationData(newDestinationData);

					try {
						project.close(null);
						project.open(null);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;

	}

	private String getNewUserName(IProject project, String currentUser) {

		ILogonService logonService = LogonServiceFactory.create();
		if (logonService.isAlreadyLoggedOn(project)) {
			IAdtUserServiceUI adtUserService = AdtUserServiceUIFactory.createAdtUserServiceUI();
			String[] users = adtUserService.openUserNameSelectionDialog(null, false, project, currentUser);
			if (users != null && users.length == 1) {
				return users[0].toUpperCase();
			}
		} else {
			UserDialog userDialog = new UserDialog(null);
			userDialog.create();
			userDialog.setUser(currentUser);
			if (userDialog.open() == Window.OK) {
				return userDialog.getUser();
			}
		}

		return null;
	}
}