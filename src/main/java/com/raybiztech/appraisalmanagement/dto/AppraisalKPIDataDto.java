package com.raybiztech.appraisalmanagement.dto;

import java.util.List;
import java.util.Set;

import com.raybiztech.appraisals.dto.EmployeeDTO;

public class AppraisalKPIDataDto {

	private Long id;
	private String name;
	private String description;
	private String employeeFeedback;
	private Integer employeeRating;
	private String employeeRatingName;
	private EmployeeDTO manager;
	private String managerFeedback;
	private Integer managerRating;
	private String frequency;
	private String target;
	private Set<ManagerCommentsDto> managerCommentsDtos;

	public AppraisalKPIDataDto() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEmployeeFeedback() {
		return employeeFeedback;
	}

	public void setEmployeeFeedback(String employeeFeedback) {
		this.employeeFeedback = employeeFeedback;
	}

	public Integer getEmployeeRating() {
		return employeeRating;
	}

	public void setEmployeeRating(Integer employeeRating) {
		this.employeeRating = employeeRating;
	}

	public EmployeeDTO getManager() {
		return manager;
	}

	public void setManager(EmployeeDTO manager) {
		this.manager = manager;
	}

	public String getManagerFeedback() {
		return managerFeedback;
	}

	public void setManagerFeedback(String managerFeedback) {
		this.managerFeedback = managerFeedback;
	}

	public Integer getManagerRating() {
		return managerRating;
	}

	public void setManagerRating(Integer managerRating) {
		this.managerRating = managerRating;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Set<ManagerCommentsDto> getManagerCommentsDtos() {
		return managerCommentsDtos;
	}

	public void setManagerCommentsDtos(
			Set<ManagerCommentsDto> managerCommentsDtos) {
		this.managerCommentsDtos = managerCommentsDtos;
	}

	public String getEmployeeRatingName() {
		return employeeRatingName;
	}

	public void setEmployeeRatingName(String employeeRatingName) {
		this.employeeRatingName = employeeRatingName;
	}

}
