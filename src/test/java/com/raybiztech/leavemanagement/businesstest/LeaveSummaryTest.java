/*package com.raybiztech.leavemanagement.businesstest;

import static org.junit.Assert.*;

import java.util.SortedSet;
import java.util.TreeSet;

import org.junit.Test;

import com.raybiztech.leavemanagement.business.LeaveCategorySummary;
import com.raybiztech.leavemanagement.business.LeaveSummary;

public class LeaveSummaryTest {
	
@Test
	public void getAllCreditedLeavesLeavesTest(){
		LeaveSummary leaveSummary = new LeaveSummary();
		SortedSet<LeaveCategorySummary> leaveCategorySummaries = new TreeSet<LeaveCategorySummary>();
		Double initialLeaves = 0.0;
		
		if(leaveCategorySummaries.size() > 0){
		for (LeaveCategorySummary leaveCategorySummary : leaveCategorySummaries) {
			leaveCategorySummary.setCreditedLeaves(initialLeaves);
			initialLeaves = initialLeaves
					+ leaveCategorySummary.getCreditedLeaves();
			
		}
		System.out.println("initialLeaves" + initialLeaves);

		System.out.println("leaveSummary" + leaveSummary);
		assertEquals(initialLeaves,leaveSummary.getAllCreditedLeaves());
		
	} 
		
		
	}
}
*/