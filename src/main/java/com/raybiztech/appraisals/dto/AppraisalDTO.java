package com.raybiztech.appraisals.dto;

import java.io.Serializable;

public class AppraisalDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;

	private EmployeeDTO employeeDTO;
	private CycleDTO cycleDto;
	private String status;
	private Boolean employeeSubmitted;
	private Boolean managerSubmitted;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EmployeeDTO getEmployeeDTO() {
		return employeeDTO;
	}

	public void setEmployeeDTO(EmployeeDTO employeeDTO) {
		this.employeeDTO = employeeDTO;
	}

	public CycleDTO getCycleDto() {
		return cycleDto;
	}

	public void setCycleDto(CycleDTO cycleDto) {
		this.cycleDto = cycleDto;
	}

    public Boolean isEmployeeSubmitted() {
        return employeeSubmitted;
    }

    public void setEmployeeSubmitted(Boolean employeeSubmitted) {
        this.employeeSubmitted = employeeSubmitted;
    }

    public Boolean isManagerSubmitted() {
        return managerSubmitted;
    }

    public void setManagerSubmitted(Boolean managerSubmitted) {
        this.managerSubmitted = managerSubmitted;
    }

}
