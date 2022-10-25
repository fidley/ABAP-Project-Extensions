package com.abapblog.adt.extension.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;

import com.abapblog.adt.extension.toolbar.gui.TransactionInputControl;

public class RunSAPTcode extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		TransactionInputControl.transactionInput.setFocus();
		return null;
	}

}
