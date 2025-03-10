package com.raybiztech.appraisalmanagement.dto;

public class ManagerCommentsDto {
	
	private Long id;
	private Integer managerRating;
	private String managerComments;
	private Integer level;
        private String status;
        private Long employeeId;
        private String employeeName;
        private String managerRatingName;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Integer getManagerRating() {
		return managerRating;
	}
	public void setManagerRating(Integer managerRating) {
		this.managerRating = managerRating;
	}
	public String getManagerComments() {
		return managerComments;
	}
	public void setManagerComments(String managerComments) {
		this.managerComments = managerComments;
	}
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public String getManagerRatingName() {
        return managerRatingName;
    }

    public void setManagerRatingName(String managerRatingName) {
        this.managerRatingName = managerRatingName;
    }
        
}
