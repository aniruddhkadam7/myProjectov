package com.raybiztech.appraisals.observation.dto;

import com.raybiztech.recruitment.dto.PersonDTO;

public class ObservationDTO {

    private Long id;
    private String description;
    private String date;
    private PersonDTO employee;
    private PersonDTO addedBy;
    private Integer rating;
    private String comment;
    private String empName;
    private String addedByUser;
    private String obsFilePath;
    private String observationMonth;
  

    public PersonDTO getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(PersonDTO addedBy) {
        this.addedBy = addedBy;
    }

    public String getDescription() {
        return description;
    }

    public Integer getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PersonDTO getEmployee() {
        return employee;
    }

    public void setEmployee(PersonDTO employee) {
        this.employee = employee;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getAddedByUser() {
        return addedByUser;
    }

    public void setAddedByUser(String addedByUser) {
        this.addedByUser = addedByUser;
    }

	public String getObsFilePath() {
		return obsFilePath;
	}

	public void setObsFilePath(String obsFilePath) {
		this.obsFilePath = obsFilePath;
	}

	public String getObservationMonth() {
		return observationMonth;
	}

	public void setObservationMonth(String observationMonth) {
		this.observationMonth = observationMonth;
	}


}
