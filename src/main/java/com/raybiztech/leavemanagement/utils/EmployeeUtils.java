package com.raybiztech.leavemanagement.utils;

import org.apache.poi.util.SystemOutLogger;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisalmanagement.business.AppraisalCycle;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.date.Duration;
import com.raybiztech.date.TimeUnit;
import com.raybiztech.leavemanagement.business.LeaveSettingsLookup;
import com.raybiztech.recruitment.utils.DateParser;

import java.text.ParseException;
import java.util.Calendar;

@Component("employeeUtils")
public class EmployeeUtils {

	public Boolean isInProbationPeriod(Employee employee,
			LeaveSettingsLookup leaveSettings) {

		Boolean flag = Boolean.TRUE;
		Date currentDate = new Date();
		Date resultantDate = employee.getJoiningDate()
				.shift(new Duration(TimeUnit.MONTH, leaveSettings
						.getProbationPeriod()));
		System.out.println("date of joining"+resultantDate);
		if (currentDate.isAfter(resultantDate)
				|| currentDate.equals(resultantDate)) {
			flag = Boolean.FALSE;

		}
		return flag;

	}
        // this method is used for calculation of employee service period
        public Integer employeeServicePeriod(Employee employee,AppraisalCycle appraisalCycle){
            //Date currentDate = new Date();
            Date cycleStartDate=appraisalCycle.getConfigurationPeriod().getMinimum();
            Date joiningDate=employee.getJoiningDate();
                
		Calendar joiningDateCal = Calendar.getInstance();
		joiningDateCal.setTime(joiningDate.getJavaDate());

		Calendar cycleStartDateCal = Calendar.getInstance();
		cycleStartDateCal.setTime(cycleStartDate.getJavaDate());
                
                
                 java.util.Date d1=joiningDateCal.getTime();
                 java.util.Date d2=cycleStartDateCal.getTime();
 
                long diff=d2.getTime()-d1.getTime();
                 Integer noOfDays=(int)(diff/(1000*24*60*60));
            return ++noOfDays;
        }
}
