/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.hrm.security.rest.transfer;

import java.util.Map;

/**
 *
 * @author hari
 */
public class UserTransfer {
    
    
	private final String name;

	private final Map<String, Boolean>  roles;


	public UserTransfer(String userName, Map<String, Boolean>  roles)
	{
		this.name = userName;
		this.roles = roles;
	}


	public String getName()
	{
		return this.name;
	}


	public Map<String, Boolean>  getRole()
	{
		return this.roles;
	}

}
