package com.raybiztech.leavemanagement.business;

import java.io.Serializable;

public class ProbationPeriod implements Serializable {

    private static final long serialVersionUID = 8075382522652624666L;
    private Long id;
    private String probationMonth;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProbationMonth() {
		return probationMonth;
	}
	public void setProbationMonth(String probationMonth) {
		this.probationMonth = probationMonth;
	}

}