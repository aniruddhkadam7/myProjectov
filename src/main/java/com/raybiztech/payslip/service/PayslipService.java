package com.raybiztech.payslip.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRException;

import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.payslip.dto.PayslipDto;
import com.raybiztech.payslip.dto.PayslipEmpViewDTO;
import com.raybiztech.payslip.dto.PayslipRetrieveDto;

public interface PayslipService {

	List<PayslipDto> getExcelDetails(MultipartFile multipartFile)
			throws IOException;

	public List<PayslipDto> saveExcelFile(String month, String year)
			throws IOException;

	// generate payslips

	Map<String, Object> getAllPayslipByMonthOfYear(String month, String year,
			int startIndex, int endIndex);

	void updatePayslip(PayslipRetrieveDto payslipRetrieveDto);

	void deletePayslip(Long id);

	void deleteCheckedPayslips(List<Long> id);

	void generatePayslip(HttpServletResponse httpServletResponse, String month,
			String year) throws JRException;

	boolean checkPayslipExists(HttpServletResponse response, String empid,
			String month, String year);

	void downloadPayslip(HttpServletResponse response, String empid,
			String month, String year) throws IOException;

	List<PayslipEmpViewDTO> getEmployeePayslipsForSelectedYear(String empid,
			String year);

	List<PayslipRetrieveDto> getPayslipDataForViewToEmployee(String empid,
			String month, String year);

	Map<String, Object> getSearchEmployees(String searchStringCand,
			String month, String year, Integer startIndex, Integer endIndex);
	
	void generatePayslipandDownload(HttpServletResponse httpServletResponse, String month,
			String year,String empid, HttpServletRequest httpServletRequest) throws JRException;
}
