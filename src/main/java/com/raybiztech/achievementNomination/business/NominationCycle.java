package com.raybiztech.achievementNomination.business;

import java.io.Serializable;

import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

/**
 * @author Aprajita
 */
public class NominationCycle implements Serializable {
	
	private static final long serialVersionUID = -393733858313742L;
	
	private Long id;
	private String cycleName;
	private Date fromMonth;
	private Date toMonth;
	private Boolean activateFlag;
	private Long createdBy;
	private Second createdDate;
	private Date startDate;
	private Date endDate;
	
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Date getFromMonth() {
		return fromMonth;
	}
	public void setFromMonth(Date fromMonth) {
		this.fromMonth = fromMonth;
	}
	public Date getToMonth() {
		return toMonth;
	}
	public void setToMonth(Date toMonth) {
		this.toMonth = toMonth;
	}
	public Boolean getActivateFlag() {
		return activateFlag;
	}
	public void setActivateFlag(Boolean activateFlag) {
		this.activateFlag = activateFlag;
	}
	public Long getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}
	public String getCycleName() {
		return cycleName;
	}
	public void setCycleName(String cycleName) {
		this.cycleName = cycleName;
	}
	public Second getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	
	
	

}
