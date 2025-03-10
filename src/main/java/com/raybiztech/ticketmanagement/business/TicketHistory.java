/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.raybiztech.ticketmanagement.business;

import com.raybiztech.appraisals.business.Employee;
import java.io.Serializable;

/**
 *
 * @author ramesh
 */
public class TicketHistory implements  Serializable{
     private static final long serialVersionUID = -4022L;
    private Long id;
    private Employee employee;
    private Ticket ticket;

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

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }
    
    
}
