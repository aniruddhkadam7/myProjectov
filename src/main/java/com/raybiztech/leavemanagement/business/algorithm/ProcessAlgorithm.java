package com.raybiztech.leavemanagement.business.algorithm;

import com.raybiztech.leavemanagement.business.LeaveDebit;

public interface ProcessAlgorithm {

	public LeaveDebit processLeave(LeaveDebit leaveDebit,
			Double avialableLeaves, Integer maxAccuraral);
}
