package com.raybiztech.separation.business;

import java.io.Serializable;

public class PrimaryReason  implements Serializable{
	
	private static final long serialVersionUID = 8756610490328634098L;
	
	private Long reasonId;
	private String reasonName;
	public Long getReasonId() {
		return reasonId;
	}
	public void setReasonId(Long reasonId) {
		this.reasonId = reasonId;
	}
	public String getReasonName() {
		return reasonName;
	}
	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
	}
	
	

}
