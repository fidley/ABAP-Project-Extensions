package com.abapblog.adt.extension.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.abapblog.adt.extension.passwords.logonService.ILogonService;
import com.abapblog.adt.extension.passwords.logonService.LogonServiceFactory;
import com.sap.adt.debugger.AbapDebugRequestFilter;
import com.sap.adt.destinations.model.IDestinationData;
import com.sap.adt.project.IAdtCoreProject;
import com.sap.adt.tools.core.ui.userinfo.AdtUserServiceUIFactory;
import com.sap.adt.tools.core.ui.userinfo.IAdtUserServiceUI;

public class ChangeDebugUserSpecificUserHandler extends ChangeDebugUserHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		requestFilter = AbapDebugRequestFilter.user;

		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection();
			Object firstElement = selection.getFirstElement();
			if (firstElement instanceof IAdaptable) {

				IProject project = ((IAdaptable) firstElement).getAdapter(IProject.class);
				ILogonService logonService = LogonServiceFactory.create();
				if (Boolean.TRUE.equals(logonService.isAlreadyLoggedOn(project))) {
					IAdtCoreProject adtProject = project.getAdapter(IAdtCoreProject.class);
					IDestinationData destinationData = adtProject.getDestinationData();
					IAdtUserServiceUI adtUserService = AdtUserServiceUIFactory.createAdtUserServiceUI();
					String[] users = adtUserService.openUserNameSelectionDialog(null, false, project,
							destinationData.getUser());
					if (users != null && users.length == 1) {
						filterUser = users[0].toUpperCase();
					} else {
						return null;
					}

					return super.execute(event);
				}
			}
		}
		return null;
	}

}
