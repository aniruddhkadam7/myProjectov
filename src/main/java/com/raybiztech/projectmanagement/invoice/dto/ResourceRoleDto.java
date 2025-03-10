package com.raybiztech.projectmanagement.invoice.dto;

public class ResourceRoleDto {

	private String  role;
	private Integer numberOfResources;
	private Double durationcount;
	private String duration;
	private String rate;
	private String amount;
	
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Integer getNumberOfResources() {
		return numberOfResources;
	}
	public void setNumberOfResources(Integer numberOfResources) {
		this.numberOfResources = numberOfResources;
	}
	public Double getDurationcount() {
		return durationcount;
	}
	public void setDurationcount(Double durationcount) {
		this.durationcount = durationcount;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	
}
