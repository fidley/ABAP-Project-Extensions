package com.abapblog.adt.extension.passwords.logonService;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;

import com.abapblog.adt.extension.passwords.secureStorage.SecureStorage;
import com.sap.adt.destinations.logon.AdtLogonServiceFactory;
import com.sap.adt.destinations.logon.IAdtLogonService;
import com.sap.adt.destinations.model.IAuthenticationToken;
import com.sap.adt.destinations.model.IDestinationData;
import com.sap.adt.destinations.model.IDestinationDataWritable;
import com.sap.adt.destinations.model.internal.AuthenticationToken;
import com.sap.adt.destinations.ui.logon.AdtLogonServiceUIFactory;
import com.sap.adt.destinations.ui.logon.IAdtLogonServiceUI;
import com.sap.adt.project.IAdtCoreProject;

public class LogonService implements ILogonService {
	private SecureStorage secureStorage;
	private IAdtLogonService adtLogonService;
	private IAdtLogonServiceUI adtLogonServiceUI;

	public LogonService() {
		secureStorage = new SecureStorage();
		adtLogonService = AdtLogonServiceFactory.createLogonService();
		adtLogonServiceUI = AdtLogonServiceUIFactory.createLogonServiceUI();
	}

	@Override
	public Boolean checkCanLogonWithSecureStorage(IProject project) {
		if (isAdtProject(project)) {
			if (secureStorage.getPassword(project).equals(SecureStorage.EmptyPassword)) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	@Override
	public void LogonToProject(IProject project) {
		if (isAdtProject(project)) {
			if (checkCanLogonWithSecureStorage(project)) {
				IAdtCoreProject AdtProject = project.getAdapter(IAdtCoreProject.class);
//				HttpAuthenticationHandlerFactory handletFactory = HttpAuthenticationHandlerFactory.getInstance();

//				IHttpAuthenticationHandler AuthenticatioHandler = handletFactory
//						.findAuthenticationHandler((IHttpDestinationData) AdtProject.getDestinationData());
//				IHttpAuthenticationToken HttpAuthenticationToken =
//				IHttpDestinationData httpauthent = (IHttpDestinationData) AdtProject.getDestinationData();
//				IHttpAuthenticationToken HttpAuthenticationToken = HttpAuthenticationHandlerFactory.getInstance()
//						.findAuthenticationHandler((IHttpDestinationData) AdtProject.getDestinationData()).getAuthenticationHeaders(null, null, null).;
				IDestinationData DestinationData = AdtProject.getDestinationData();
				IAuthenticationToken AuthenticationToken = new AuthenticationToken();
//				IProjectNature nature = project.getNature(null);
				AuthenticationToken.setPassword(secureStorage.getPassword(project));
				adtLogonService.ensureLoggedOn(DestinationData, AuthenticationToken, new NullProgressMonitor());
//				IDestinationDataWritable DestinationDataWritable = DestinationData.getWritable();
//				DestinationDataWritable.setUser("MALCOLMX");
//				DestinationDataWritable.setClient("000");
//				ISystemConfigurationWritable systemConfigurationWritable = DestinationDataWritable
//						.getSystemConfiguration().getWritable();
//				systemConfigurationWritable.setServer("https://test.sap.com:443");
//				systemConfigurationWritable.setSystemId("ABAP");
//				systemConfigurationWritable.setMessageServer("mySAP");
//				DestinationDataWritable.setSystemConfiguration(systemConfigurationWritable);
//				AdtProject.setDestinationData(DestinationDataWritable.getReadOnlyClone());
			} else {
				adtLogonServiceUI.ensureLoggedOn(project);
			}
		}
	}

	@Override
	public void LogonToProject(IProject project, String user, String client) {
		if (isAdtProject(project)) {
			if (user == "" && client == "") {
				LogonToProject(project);
			} else {

				IAdtCoreProject AdtProject = project.getAdapter(IAdtCoreProject.class);
				IDestinationData DestinationData = AdtProject.getDestinationData();
				try {
					IDestinationDataWritable DestinationDataWritable = DestinationData.getWritable();
					if (user != "")
						DestinationDataWritable.setUser(user);
					if (client != "")
						DestinationDataWritable.setClient(client);
					IDestinationData newDestinationData = DestinationDataWritable.getReadOnlyClone();
					IAuthenticationToken AuthenticationToken = new AuthenticationToken();
					String password = secureStorage.getPassword(project.getName(), client, user);
					AuthenticationToken.setPassword(password);
					adtLogonService.ensureLoggedOn(newDestinationData, AuthenticationToken, new NullProgressMonitor());
				} catch (Exception e) {
					adtLogonServiceUI.ensureLoggedOn(project);
				}
			}
		}
	}

	@Override
	public Boolean isAlreadyLoggedOn(IProject project) {
		if (isAdtProject(project)) {
			return adtLogonService.isLoggedOn(project.getName());
		} else {
			return false;
		}
	}

	@Override
	public Boolean checkCanLogonWithSecureStorage(IProject project, String user, String client) {
		if (isAdtProject(project)) {
			if (user == "" && client == "") {
				return checkCanLogonWithSecureStorage(project);
			} else {
				if (secureStorage.getPassword(project.getName(), client, user).equals(SecureStorage.EmptyPassword)) {
					return false;
				} else {
					return true;
				}
			}
		} else {
			return false;
		}
	}

	@Override
	public String getUserForProject(IProject project) {
		IAdtCoreProject AdtProject = project.getAdapter(IAdtCoreProject.class);
		IDestinationData DestinationData = AdtProject.getDestinationData();
		return DestinationData.getUser();
	}

	@Override
	public Boolean isAdtProject(IProject project) {
		try {
			IAdtCoreProject AdtProject = project.getAdapter(IAdtCoreProject.class);
			if (AdtProject != null) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public String getProjectName(IProject project) {
		return project.getName();
	}

	@Override
	public String getClientForProject(IProject project) {
		IAdtCoreProject AdtProject = project.getAdapter(IAdtCoreProject.class);
		IDestinationData DestinationData = AdtProject.getDestinationData();
		return DestinationData.getClient();
	}

}
