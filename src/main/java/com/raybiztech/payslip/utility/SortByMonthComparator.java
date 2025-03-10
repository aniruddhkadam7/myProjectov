package com.raybiztech.payslip.utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Locale;

import com.raybiztech.payslip.dto.PayslipEmpViewDTO;

public class SortByMonthComparator implements Comparator<PayslipEmpViewDTO> {

	@Override
	public int compare(PayslipEmpViewDTO payslip1, PayslipEmpViewDTO payslip2) {

		try {
			SimpleDateFormat fmt = new SimpleDateFormat("MMMM", Locale.US);
			return fmt.parse(payslip1.getMonth()).compareTo(
					fmt.parse(payslip2.getMonth()));
		} catch (ParseException ex) {
			return payslip1.getMonth().compareTo(payslip2.getMonth());
		}

	}
}