package com.gan4x4.greedyalarm.test;

public class UserAccount {

	private String email;
	public String password;
	
	public UserAccount(String em,String pwd){
		setEmail(em);
		password = pwd;
		
		
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}
