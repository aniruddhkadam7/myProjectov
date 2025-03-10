package com.raybiztech.appraisals.business;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class KPIRating implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long kpiRatingId;
	private String kpiRatingName;
	private Integer employee_Rating;
	private String employee_Comment;
	private Integer manager_Rating;
	private String manage_comment;
	private String employeeFileName;
	private String managerFileName;
	private String employeeDummyFileName;
	private String managerDummyFileName;
	private KPI kpi;
	private Employee employee;
	private Cycle cycle;

	public KPIRating() {

	}

	public Long getKpiRatingId() {
		return kpiRatingId;
	}

	public void setKpiRatingId(Long kpiRatingId) {
		this.kpiRatingId = kpiRatingId;
	}

	public String getKpiRatingName() {
		return kpiRatingName;
	}

	public void setKpiRatingName(String kpiRatingName) {
		this.kpiRatingName = kpiRatingName;
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

	public KPI getKpi() {
		return kpi;
	}

	public void setKpi(KPI kpi) {
		this.kpi = kpi;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Cycle getCycle() {
		return cycle;
	}

	public void setCycle(Cycle cycle) {
		this.cycle = cycle;
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

	@Override
	public int hashCode() {
		return new HashCodeBuilder(1989, 55).append(kpiRatingId).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final KPIRating other = (KPIRating) obj;
		return new EqualsBuilder().append(kpiRatingId, other.kpiRatingId)
				.isEquals();
	}
}
