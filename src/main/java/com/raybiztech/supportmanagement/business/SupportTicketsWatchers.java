/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.supportmanagement.business;

import com.raybiztech.appraisals.business.Employee;
import java.io.Serializable;

/**
 *
 * @author anil
 */
public class SupportTicketsWatchers implements Serializable{
    private static final long serialVersionUID = 5342720840744811L;
    
    private Long id;
    private Employee employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    
    
}
