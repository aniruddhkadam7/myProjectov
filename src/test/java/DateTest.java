import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author hari
 */
public class DateTest {

	private java.util.Calendar calendar;

	public static void main(String[] args) throws IOException {

		// Date date = new Date(DayOfMonth.valueOf(1),
		// MonthOfYear.valueOf(new Date().getMonthOfYear().getValue()),
		// YearOfEra.valueOf(new Date().getYearOfEra().getValue()));
		// Date lastMonthLastDate = date.shift(new Duration(TimeUnit.DAY, -1));
		//
		// Date lastMonthFirstDate = new Date(DayOfMonth.valueOf(1),
		// MonthOfYear.valueOf(lastMonthLastDate.getMonthOfYear().getValue()),
		// YearOfEra.valueOf(lastMonthLastDate.getYearOfEra().getValue()));
		//
		// System.out.println("ssa " + lastMonthFirstDate + " " +
		// lastMonthLastDate);

		// String orignalPath =
		// "/home/hari/wildfly-8.0.0.Final/standalone/deployments/hrm-newUI.war/documents/Enrique Igeleasis 58bf12ed-3e05-4376-ad02-77a1c7bf7d13.pdf";
		// if (orignalPath != null) {
		// String splitedPath =
		// orignalPath.substring(orignalPath.lastIndexOf("/") + 1);
		// System.out.println("path is: "+splitedPath);
		// }
            
		Date date = new Date();
		
		DateRange dateRange=new DateRange(date, null);
	


		/*Set<String> set = new HashSet<String>();
		set.add("B");
		set.add("dAB");
		set.add("B");
		set.add("B");
	 
		for (String s : set) {
			if (s.equals("dAB")) {
				set.remove(s);
			}
		}*/

		
	}

	public static Date parse(String date, String pattern)
			throws java.text.ParseException {
		Date thisdate = null;
		if (date != null && date.trim().length() != 0 && pattern != null
				&& pattern.trim().length() != 0) {
			DateFormat dateFormat = new SimpleDateFormat(pattern);
			thisdate = new Date(dateFormat.parse(date).getTime());
		}
		return thisdate;
	}

	@Override
	public String toString() {
		return toString("dd-mmm-yyyy");
	}

	public String toString(String pattern) {
		DateFormat dateFormat = new SimpleDateFormat(pattern);
		return dateFormat.format(calendar.getTime());
	}

	public boolean validate(String s) {
		return s != null && s.trim().length() != 0;
	}

}
