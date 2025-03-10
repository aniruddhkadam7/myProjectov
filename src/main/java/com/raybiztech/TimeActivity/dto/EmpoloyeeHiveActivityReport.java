/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.TimeActivity.dto;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author naresh
 */
public class EmpoloyeeHiveActivityReport implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1106460333913293957L;

	public EmpoloyeeHiveActivityReport() {
    }
    private Long id;
    private String userName;
    private String firstName;
    private String lastName;
    private List<EmpoloyeeHiveActivityTime> activityTimes;
    private String totalHiveTime;
    private String projectIdentifier;
    public Long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<EmpoloyeeHiveActivityTime> getActivityTimes() {
        return activityTimes;
    }

    public void setActivityTimes(List<EmpoloyeeHiveActivityTime> activityTimes) {
        this.activityTimes = activityTimes;
    }

	public String getTotalHiveTime() {
		return totalHiveTime;
	}

	public void setTotalHiveTime(String totalHiveTime) {
		this.totalHiveTime = totalHiveTime;
	}

	public String getProjectIdentifier() {
		return projectIdentifier;
	}

	public void setProjectIdentifier(String projectIdentifier) {
		this.projectIdentifier = projectIdentifier;
	}
	
	
}
