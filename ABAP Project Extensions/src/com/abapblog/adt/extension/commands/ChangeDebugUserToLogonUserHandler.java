package com.abapblog.adt.extension.commands;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.sap.adt.debugger.AbapDebugRequestFilter;

public class ChangeDebugUserToLogonUserHandler extends ChangeDebugUserHandler {
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		requestFilter = AbapDebugRequestFilter.logonUser;
		filterUser = "";
		return super.execute(event);
	}
}
