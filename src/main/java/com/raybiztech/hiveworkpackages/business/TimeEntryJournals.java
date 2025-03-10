package com.raybiztech.hiveworkpackages.business;

import java.io.Serializable;
import java.util.Date;

public class TimeEntryJournals implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8050670377893727214L;
	
	private Long id;
	private Journals journal_id;
	private projects project_id;
	private users user_id;
	private work_packages work_package_id;
	private Float hours;
	private String comments;
	private Enumerations activity_id;
	private Date spent_on;
	private Long tyear;
	private Long tmonth;
	private Integer tweek;
	private Double overridden_costs;
	private Double costs;
	private Long rate_id;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Journals getJournal_id() {
		return journal_id;
	}
	public void setJournal_id(Journals journal_id) {
		this.journal_id = journal_id;
	}
	public projects getProject_id() {
		return project_id;
	}
	public void setProject_id(projects project_id) {
		this.project_id = project_id;
	}
	public users getUser_id() {
		return user_id;
	}
	public void setUser_id(users user_id) {
		this.user_id = user_id;
	}
	public work_packages getWork_package_id() {
		return work_package_id;
	}
	public void setWork_package_id(work_packages work_package_id) {
		this.work_package_id = work_package_id;
	}
	public Float getHours() {
		return hours;
	}
	public void setHours(Float hours) {
		this.hours = hours;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public Enumerations getActivity_id() {
		return activity_id;
	}
	public void setActivity_id(Enumerations activity_id) {
		this.activity_id = activity_id;
	}
	public Date getSpent_on() {
		return spent_on;
	}
	public void setSpent_on(Date spent_on) {
		this.spent_on = spent_on;
	}
	public Long getTyear() {
		return tyear;
	}
	public void setTyear(Long tyear) {
		this.tyear = tyear;
	}
	public Long getTmonth() {
		return tmonth;
	}
	public void setTmonth(Long tmonth) {
		this.tmonth = tmonth;
	}
	public Integer getTweek() {
		return tweek;
	}
	public void setTweek(Integer tweek) {
		this.tweek = tweek;
	}
	public Double getOverridden_costs() {
		return overridden_costs;
	}
	public void setOverridden_costs(Double overridden_costs) {
		this.overridden_costs = overridden_costs;
	}
	public Double getCosts() {
		return costs;
	}
	public void setCosts(Double costs) {
		this.costs = costs;
	}
	public Long getRate_id() {
		return rate_id;
	}
	public void setRate_id(Long rate_id) {
		this.rate_id = rate_id;
	}
	
	

}
