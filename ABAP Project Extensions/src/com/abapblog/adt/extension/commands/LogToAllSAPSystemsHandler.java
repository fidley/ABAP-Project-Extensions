package com.abapblog.adt.extension.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;

import com.abapblog.adt.extension.passwords.logonService.LogonServiceFactory;
import com.abapblog.adt.extension.passwords.logonService.LogonWithJob;
import com.abapblog.adt.extension.preferences.Preferences;
import com.abapblog.adt.extension.preferences.project.UserPreferences;
import com.sap.adt.destinations.logon.AdtLogonServiceFactory;
import com.sap.adt.destinations.logon.IAdtLogonService;
import com.sap.adt.destinations.ui.logon.AdtLogonServiceUIFactory;
import com.sap.adt.destinations.ui.logon.IAdtLogonServiceUI;
import com.sap.adt.tools.core.project.AdtProjectServiceFactory;

public class LogToAllSAPSystemsHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		IAdtLogonService logonService = AdtLogonServiceFactory.createLogonService();
		IAdtLogonServiceUI logonServiceUI = AdtLogonServiceUIFactory.createLogonServiceUI();

		logonToAdtProjects(logonService, logonServiceUI);
		return null;
	}

	private void logonToAdtProjects(IAdtLogonService logonService, IAdtLogonServiceUI logonServiceUI) {
		for (IProject AdtProject : AdtProjectServiceFactory.createProjectService().getAvailableAdtCoreProjects()) {
			try {
				if (logonService.isLoggedOn(AdtProject.getName()) == false) {
					if (Preferences.automaticLogonForAllPossibleProjects() == false
							&& new UserPreferences(AdtProject).getAutomaticLogon() == false)
						continue;

					if (LogonServiceFactory.create().checkCanLogonWithSecureStorage(AdtProject)) {
						LogonWithJob logonWithJob = new LogonWithJob();
						logonWithJob.logon(AdtProject);
					} else if (doAutomaticLogonForAllSystems()) {
						logonServiceUI.ensureLoggedOn(AdtProject);
					}
				} else {

					logonServiceUI.ensureLoggedOn(AdtProject);

				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	private boolean doAutomaticLogonForAllSystems() {
		if (Preferences.automaticLogonOnlyForStoredPasswords() == false) {
			return true;
		} else {
			return false;
		}
	}

}