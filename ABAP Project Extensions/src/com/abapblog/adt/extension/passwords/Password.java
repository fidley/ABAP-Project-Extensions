package com.abapblog.adt.extension.passwords;

public class Password implements Comparable {

	public String user;
	public String password;
	public Boolean encrypted;
	public String project;
	public String client;

	@Override
	public int compareTo(Object passwordToCompare) {

		return project.toUpperCase().compareTo(((Password) passwordToCompare).project.toUpperCase());
	}

}
