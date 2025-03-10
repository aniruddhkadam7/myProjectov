/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.TimeActivity.dto;

import java.io.Serializable;

import com.raybiztech.date.Date;

/**
 *
 * @author naresh
 */
public class EmpoloyeeHiveActivityTime  implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -7755372257395899822L;
	private Long id;
    private String hours;
    private Integer dayofMonth;
    private String projectDate;
    private Date pDate;
    private Long empId;
    private String sprintName;
	private Integer taskId;
	private String startDate;
	private String endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHours() {
        return hours;
    }

    public void setHours(String hours) {
        this.hours = hours;
    }

    public Integer getDayofMonth() {
        return dayofMonth;
    }

    public void setDayofMonth(Integer dayofMonth) {
        this.dayofMonth = dayofMonth;
    }

    public String getProjectDate() {
        return projectDate;
    }

    public void setProjectDate(String projectDate) {
        this.projectDate = projectDate;
    }

    public Date getpDate() {
        return pDate;
    }

    public void setpDate(Date pDate) {
        this.pDate = pDate;
    }

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public String getSprintName() {
		return sprintName;
	}

	public void setSprintName(String sprintName) {
		this.sprintName = sprintName;
	}

	public Integer getTaskId() {
		return taskId;
	}

	public void setTaskId(Integer taskId) {
		this.taskId = taskId;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
    
}
