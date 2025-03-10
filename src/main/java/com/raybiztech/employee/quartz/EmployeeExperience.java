package com.raybiztech.employee.quartz;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.joda.time.Months;
import org.joda.time.Years;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Calendar;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.recruitment.dao.JobPortalDAO;

@Transactional
@Component("employeeExperience")
public class EmployeeExperience {

	@Autowired
	JobPortalDAO jobPortalDAOImpl;

	public void updateEmployeeExperience() {

		List<Employee> empList = jobPortalDAOImpl.getEmployeesExperiencesList();

		if (!empList.isEmpty()) {
			for (Employee emp : empList) {

				System.out.println("emp.getJ" + emp.getJoiningDate());
				Date joiningDate = emp.getJoiningDate();

				GregorianCalendar joiningDay = new GregorianCalendar();

				joiningDay.setTime(joiningDate.getJavaDate());

				GregorianCalendar today = new GregorianCalendar();
				today.setTime(new Date().getJavaDate());

				int yearsInBetween = today.get(Calendar.YEAR_OF_ERA)
						- joiningDay.get(Calendar.YEAR_OF_ERA);
				int monthsDiff = today.get(Calendar.MONTH_OF_YEAR)
						- joiningDay.get(Calendar.MONTH_OF_YEAR);
				Double ageInMonths = (double) (yearsInBetween * 12 + monthsDiff);
				long age = yearsInBetween;

				System.out.println("Number of months : " + ageInMonths + emp.getEmployeeFullName());
/*
				System.out.println("no of months " + ageInMonths);
				// Double experienceValue = 0.1;
				Double empExp = (double) (ageInMonths / 12.0);
				System.out.println("employee experience:" + empExp);

				String formattedString = String.format("%.01f", empExp);

				Double employeeExpe = Double.valueOf(formattedString);*/
				int exp = (int)(ageInMonths/12);
				int res = (int)(ageInMonths%12);
				System.out.println("experience:" + exp );
				System.out.println("modulus:" + res );
				String expi = exp + "." + res;
				Double experience = Double.valueOf(expi);
				System.out.println("exp:" + experience);

				emp.setCompanyExperience(experience);
				jobPortalDAOImpl.saveOrUpdate(emp);

			}
		}
	}

}
