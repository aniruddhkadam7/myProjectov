package com.raybiztech.appraisals.dto;

import java.io.Serializable;

public class MyDTO implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String name;
	private Integer employee_Rating;
	private String employee_Comment;
	private String employeeFileName;
	private Integer manager_Rating;
	private String manager_Comment;
	private String managerFileName;

	private Long employeeId;
	private Long cycleId;
	private Long kpiId;
	private String employeeDummyFileName;
	private String managerDummyFileName;

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

	public Integer getEmployee_Rating() {
		return employee_Rating;
	}

	public void setEmployee_Rating(Integer employee_Rating) {
		this.employee_Rating = employee_Rating;
	}

	public String getEmployee_Comment() {
		return employee_Comment;
	}

	public void setEmployee_Comment(String employee_Comment) {
		this.employee_Comment = employee_Comment;
	}

	public String getEmployeeFileName() {
		return employeeFileName;
	}

	public void setEmployeeFileName(String employeeFileName) {
		this.employeeFileName = employeeFileName;
	}

	public Integer getManager_Rating() {
		return manager_Rating;
	}

	public void setManager_Rating(Integer manager_Rating) {
		this.manager_Rating = manager_Rating;
	}

	public String getManager_Comment() {
		return manager_Comment;
	}

	public void setManager_Comment(String manager_Comment) {
		this.manager_Comment = manager_Comment;
	}

	public String getManagerFileName() {
		return managerFileName;
	}

	public void setManagerFileName(String managerFileName) {
		this.managerFileName = managerFileName;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public Long getCycleId() {
		return cycleId;
	}

	public void setCycleId(Long cycleId) {
		this.cycleId = cycleId;
	}

	public Long getKpiId() {
		return kpiId;
	}

	public void setKpiId(Long kpiId) {
		this.kpiId = kpiId;
	}

	/**
	 * @return the employeeDummyFileName
	 */
	public String getEmployeeDummyFileName() {
		return employeeDummyFileName;
	}

	/**
	 * @param employeeDummyFileName
	 *            the employeeDummyFileName to set
	 */
	public void setEmployeeDummyFileName(String employeeDummyFileName) {
		this.employeeDummyFileName = employeeDummyFileName;
	}

	/**
	 * @return the managerDummyFileName
	 */
	public String getManagerDummyFileName() {
		return managerDummyFileName;
	}

	/**
	 * @param managerDummyFileName
	 *            the managerDummyFileName to set
	 */
	public void setManagerDummyFileName(String managerDummyFileName) {
		this.managerDummyFileName = managerDummyFileName;
	}

}
