package com.abapblog.adt.extension.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.sap.adt.debugger.AbapDebugRequestFilter;
import com.sap.adt.debugger.internal.AbapDebuggerServices;
import com.sap.adt.project.IAdtCoreProject;

public abstract class ChangeDebugUserHandler extends AbstractHandler {
	protected AbapDebugRequestFilter requestFilter = AbapDebugRequestFilter.logonUser;
	protected String filterUser = "";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		if (window != null) {
			IStructuredSelection selection = (IStructuredSelection) window.getSelectionService().getSelection();
			Object firstElement = selection.getFirstElement();
			if (firstElement instanceof IAdaptable) {

				IProject project = ((IAdaptable) firstElement).getAdapter(IProject.class);
				IAdtCoreProject AdtProject = project.getAdapter(IAdtCoreProject.class);
				AbapDebuggerServices.setRequestFilter(project, requestFilter, filterUser);
			}
		}
		return null;
	}

}
