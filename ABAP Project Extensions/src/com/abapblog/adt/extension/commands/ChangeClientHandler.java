package com.abapblog.adt.extension.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import com.abapblog.adt.extension.dialogs.ClientDialog;
import com.sap.adt.destinations.model.IDestinationData;
import com.sap.adt.destinations.model.IDestinationDataWritable;
import com.sap.adt.project.IAdtCoreProject;

public class ChangeClientHandler extends AbstractHandler {

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
				ClientDialog ClientDialog = new ClientDialog(null);
				ClientDialog.create();
				ClientDialog.setClient(DestinationDataWritable.getClient());
				if (ClientDialog.open() == Dialog.OK) {
					String client = ClientDialog.getClient();
					DestinationDataWritable.setClient(client);
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

}
