/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisals.business;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 *
 * @author naresh
 */
public class EmployeeBankInformation implements Serializable, Comparable<EmployeeBankInformation> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3822296191942341264L;
	private Long bankId;
    private String bankName;
    private String bankAccountNumber;
    private String ifscCode;
    
    
    
    private Employee employee;
    
 
    public Long getBankId() {
        return bankId;
    }

    public void setBankId(Long bankId) {
        this.bankId = bankId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

  

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    
    @Override
    public int hashCode() {
        return new HashCodeBuilder(1989, 55).append(bankId).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EmployeeBankInformation other = (EmployeeBankInformation) obj;
        return new EqualsBuilder().append(bankId, other.bankId)
                .isEquals();
    }

    @Override
    public int compareTo(EmployeeBankInformation bankInformation) {
        return this.bankId.compareTo(bankInformation.getBankId());
    }

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}
}
