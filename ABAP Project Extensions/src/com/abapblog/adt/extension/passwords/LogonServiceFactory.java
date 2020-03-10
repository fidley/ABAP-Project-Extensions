package com.abapblog.adt.extension.passwords;

public class LogonServiceFactory{
	private static ILogonService logonService;
	public static ILogonService create() {
		if (logonService != null) {
			return logonService;
		}
		logonService = new LogonService();
		return logonService;
	}
}
