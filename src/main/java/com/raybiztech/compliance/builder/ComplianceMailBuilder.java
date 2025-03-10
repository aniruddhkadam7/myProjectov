package com.raybiztech.compliance.builder;

import java.util.List;

import org.springframework.stereotype.Component;

import com.raybiztech.compliance.business.Compliance;
import com.raybiztech.compliance.business.ComplianceTask;
import com.raybiztech.date.Calendar;
import com.raybiztech.date.Date;

@Component("complianceMailBuilder")
public class ComplianceMailBuilder {

	public Date lastWeek(Date today) {
		Calendar calendar = today.getCalendar();
		calendar.add(Calendar.WEEK_OF_MONTH, -1);
		return new Date(calendar.getTimeInMillis());
	}
	public Date lastMonth(Date today) {

		Calendar calendar = today.getCalendar();
		//Getting previous Month
		calendar.add(Calendar.MONTH_OF_YEAR, -1);
		return new Date(calendar.getTimeInMillis());
	}
	/*public Date () {
		
	}*/
	public Date toBack(Date date,Byte daysBack) {
		Calendar calendar = date.getCalendar();
		calendar.add(Calendar.DAY_OF_MONTH, -daysBack);
		return new Date(calendar.getTimeInMillis());
	}
	public Boolean isCurrentMonthGreater() {
		Date thisMonth = new Date();
		Calendar calendar = thisMonth.getCalendar();
		int currentLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH,calendar.getJavaDate()).getDate();
		
		calendar.add(Calendar.MONTH_OF_YEAR, -1);
		int lastLastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH,calendar.getJavaDate()).getDate();
		if(currentLastDay>=lastLastDay) {
			return true;
		}else {
			return false;
		}
		 
	}
	public List<ComplianceTask> adjustedComplianceTasks(List<ComplianceTask> loadedTasks) {
		for(ComplianceTask complianceTask : loadedTasks) {
			Compliance compliance = complianceTask.getCompliance();
			int day = compliance.getCreatedDate().getDayOfMonth().getValue();
			int today = new Date().getDayOfMonth().getValue();
			if(isCurrentMonthGreater()) {
				if(day>today) {
					
				}
			}
		}
		
		return loadedTasks;
	}
	
}
