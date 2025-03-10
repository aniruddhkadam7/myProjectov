package com.raybiztech.separation.dto;

import java.io.Serializable;

public class PrimaryReasonDTO implements Serializable {
	private static final long serialVersionUID = 4610448099929161420L;
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
