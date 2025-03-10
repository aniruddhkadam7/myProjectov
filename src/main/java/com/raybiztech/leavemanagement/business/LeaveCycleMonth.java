package com.raybiztech.leavemanagement.business;

public enum LeaveCycleMonth {

	January(0), February(1), March(2), April(3), May(4), June(5), July(6), August(
			7), September(8), October(9), November(10), December(11);

	private Integer monthCode;

	private LeaveCycleMonth(Integer monthCode){
		this.monthCode=monthCode;
	}

	public Integer getMonthCode() {
		return monthCode;
	}

	public void setMonthCode(Integer monthCode) {
		this.monthCode = monthCode;
	}


}
