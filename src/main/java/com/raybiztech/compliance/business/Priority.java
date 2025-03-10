package com.raybiztech.compliance.business;

public enum Priority {
	LOW("Low"),NORMAL("Normal"),HIGH("High"),URGENT("Urgent"),IMMEDIATE("Immediate");
	
	private String priority;

	private Priority(String priority) {
		this.priority = priority;
	}

	public String getPriority() {
		return priority;
	}
}
