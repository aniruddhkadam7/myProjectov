package com.raybiztech.payslip.dao;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.raybiztech.payslip.business.Payslip;
import com.raybiztech.projectmanagement.business.Audit;

public interface PayslipDao {
	public void save(List<Payslip> list, String Month, String Year)
			throws IOException;

	// generate payslips

	Map<String, Object> getAllPayslipByMonthOfYear(String month, String year,
			int startIndex, int endIndex);

	void deleteCheckedPayslips(List<Long> id);

	public List<Payslip> getallPayslip(String month, String year);

	public Boolean checkPayslipExist(String month, String year, String empId);

	public Boolean checkDataExistsForMonthAndYear(String month, String year);

	public List<Object[]> getEmployeePayslipsForSelectedYear(String empid,
			String year);

	public List<Payslip> getPayslipDataForViewToEmployee(String empid,
			String month, String year);

	Map<String, Object> searchEmployeeDetails(String searchString,
			String month, String year, Integer startIndex, Integer endIndex);
	
	public Payslip checkDataExistForMonthYearAndEmployeeId(String month, String year,Long Empid);
	
	public Audit  getlatestDesignationForEmployee(Long employeeId);
	
	
	
	
}
