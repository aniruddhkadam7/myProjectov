/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.appraisals.security.utils;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

/**
 *
 * @author anil
 */
public class SecurityConfig {
    
    
    @Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() {
//	    DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
//	    driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
//	    driverManagerDataSource.setUrl("jdbc:mysql://localhost:3306/test");
//	    driverManagerDataSource.setUsername("root");
//	    driverManagerDataSource.setPassword("password");
           DriverManagerDataSource driverManagerDataSource=new DriverManagerDataSource();
           driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
            driverManagerDataSource.setUrl("jdbc:mysql://localhost:3306/LPSEC");
            driverManagerDataSource.setUsername("root");
	    driverManagerDataSource.setPassword("root123");
            
	    return driverManagerDataSource;
	}
}
