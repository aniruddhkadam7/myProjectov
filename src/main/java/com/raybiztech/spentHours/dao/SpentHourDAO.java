package com.raybiztech.spentHours.dao;

import java.util.List;

import com.raybiztech.TimeInOffice.business.TimeInOffice;
import com.raybiztech.date.DateRange;
import com.raybiztech.spentHours.business.SpentTime;

public interface SpentHourDAO {
	
	List<TimeInOffice> getEmployeeSpentHours(String employeeId,DateRange monthPeriod);
	
	List<SpentTime> getSpentHours(DateRange monthPeriod);
	
	

}
