/*package com.raybiztech.leavemanagement.businesstest;

import org.junit.Test;
import org.mockito.Mockito;

import com.raybiztech.leavemanagement.business.Leave;
import com.raybiztech.leavemanagement.business.LeaveSummary;
import com.raybiztech.leavemanagement.business.algorithm.LeaveAlgorithmImpl;
import com.raybiztech.leavemanagement.dao.LeaveDAOImpl;
import com.raybiztech.leavemanagement.exceptions.LeaveCannotProcessException;
import com.raybiztech.leavemanagement.exceptions.NotEnoughLeavesAvaialableException;
import com.raybiztech.leavemanagement.service.LeaveHelper;

public class LeaveAlgorithmTest {
	LeaveSummary leaveSummary = null;
	@BeforeClass
	public void beforeTest(){
		
	}

	@Test
	public void processLeaveTest_processEarnedLeave_whenAvailable() {
		LeaveData leaveData = new LeaveData();
		Leave leave = leaveData.getLeaveData();
		LeaveAlgorithmImpl leaveAlgorithmImpl = new LeaveAlgorithmImpl();
		LeaveDAOImpl leaveDAOImpl = Mockito.mock(LeaveDAOImpl.class);
		LeaveHelper leaveHelper = Mockito.mock(LeaveHelper.class);
	//	leaveAlgorithmImpl.setLeaveHelper(leaveHelper);
		LeaveSummaryData ls = new LeaveSummaryData();
		LeaveCategorySummaryData leaveCategorySummaryData = new LeaveCategorySummaryData();
		Mockito.when(
				leaveDAOImpl.getLeaveSummary(leave.getEmployee()
						.getEmployeeId())).thenReturn(ls.getLeaveSummaryData());
		Mockito.when(
				leaveHelper.getAppliedLeaveSummary(leave,
						ls.getLeaveSummaryData())).thenReturn(
				leaveCategorySummaryData.getLeaveCategorySummaryData());
		leaveAlgorithmImpl.setLeaveDAOImpl(leaveDAOImpl);

		LeaveSummary leaveSummary;
		try {
			leaveSummary = leaveAlgorithmImpl.processLeave(leave);
		} catch (LeaveCannotProcessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals(new Double(6.0),
				leaveSummary.getAllAvailableLeaves());
	}
	
	@Test(expected = NotEnoughLeavesAvaialableException.class)
	public void processLeaveTest_processEarnedLeave_whenLeavesNotAvailable() {
		LeaveData leaveData = new LeaveData();
		Leave leave = leaveData.getLeaveData();
		LeaveAlgorithmImpl leaveAlgorithmImpl = new LeaveAlgorithmImpl();
		LeaveDAOImpl leaveDAOImpl = Mockito.mock(LeaveDAOImpl.class);
		LeaveHelper leaveHelper = Mockito.mock(LeaveHelper.class);
		leaveAlgorithmImpl.setLeaveHelper(leaveHelper);
		LeaveSummaryData ls = new LeaveSummaryData();
		LeaveCategorySummaryData leaveCategorySummaryData = new LeaveCategorySummaryData();
		Mockito.when(
				leaveDAOImpl.getLeaveSummary(leave.getEmployee()
						.getEmployeeId())).thenReturn(ls.getLeaveSummaryData());
		Mockito.when(
				leaveHelper.getAppliedLeaveSummary(leave,
						ls.getLeaveSummaryData())).thenReturn(
				leaveCategorySummaryData.getLeaveCategorySummaryData());
		leaveAlgorithmImpl.setLeaveDAOImpl(leaveDAOImpl);

		LeaveSummary leaveSummary;
		try {
			leaveSummary = leaveAlgorithmImpl.processLeave(leave);
		} catch (LeaveCannotProcessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assert.assertEquals(new Double("0"),
				leaveSummary.getAllAvailableLeaves());
	}
}
*/