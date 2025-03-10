package com.raybiztech.appraisals.dto;

import java.io.Serializable;

public class KPIRatingDTO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
	private String name;
	private Integer employee_Rating;
	private String employee_Comment;
	private Integer manager_Rating;
	private String manage_comment;
	private String employeeFileName;
	private String managerFileName;
	private EmployeeDTO employee;
	private CycleDTO cycle;
	private KPIDTO kpi;
	private String employeeDummyFileName;
	private String managerDummyFileName;

	public KPIRatingDTO() {

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

	public Integer getManager_Rating() {
		return manager_Rating;
	}

	public void setManager_Rating(Integer manager_Rating) {
		this.manager_Rating = manager_Rating;
	}

	public String getManage_comment() {
		return manage_comment;
	}

	public void setManage_comment(String manage_comment) {
		this.manage_comment = manage_comment;
	}

	public String getEmployeeFileName() {
		return employeeFileName;
	}

	public void setEmployeeFileName(String employeeFileName) {
		this.employeeFileName = employeeFileName;
	}

	public String getManagerFileName() {
		return managerFileName;
	}

	public void setManagerFileName(String managerFileName) {
		this.managerFileName = managerFileName;
	}

	public EmployeeDTO getEmployee() {
		return employee;
	}

	public void setEmployee(EmployeeDTO employee) {
		this.employee = employee;
	}

	public CycleDTO getCycle() {
		return cycle;
	}

	public void setCycle(CycleDTO cycle) {
		this.cycle = cycle;
	}

	public KPIDTO getKpi() {
		return kpi;
	}

	public void setKpi(KPIDTO kpi) {
		this.kpi = kpi;
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