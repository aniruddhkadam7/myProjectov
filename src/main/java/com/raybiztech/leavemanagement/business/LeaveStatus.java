package com.raybiztech.leavemanagement.business;

import java.io.Serializable;

public enum LeaveStatus implements Serializable {
	PendingApproval, Approved, Cancelled,Rejected,CancelAfterApproval;
        

}