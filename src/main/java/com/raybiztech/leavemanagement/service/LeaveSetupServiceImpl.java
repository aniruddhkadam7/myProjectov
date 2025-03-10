package com.raybiztech.leavemanagement.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.biometric.dao.BiometricDAO;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.leavemanagement.builder.LeaveCategoryBuilder;
import com.raybiztech.leavemanagement.builder.LeaveSettingsBuilder;
import com.raybiztech.leavemanagement.builder.LeaveSummaryBuilder;
import com.raybiztech.leavemanagement.business.CarryForwardLeave;
import com.raybiztech.leavemanagement.business.LeaveCategory;
import com.raybiztech.leavemanagement.business.LeaveDebit;
import com.raybiztech.leavemanagement.business.LeaveSettingsLookup;
import com.raybiztech.leavemanagement.dao.LeaveDAO;
import com.raybiztech.leavemanagement.dto.LeaveCategoryDTO;
import com.raybiztech.leavemanagement.dto.LeaveCategorySummaryDTO;
import com.raybiztech.leavemanagement.dto.LeaveSettingsDTO;
import com.raybiztech.leavemanagement.dto.LeaveSummaryDTO;
import com.raybiztech.leavemanagement.exceptions.CannotDeleteLeaveCategoryException;
import com.raybiztech.leavemanagement.exceptions.EmployeeNotFoundException;
import com.raybiztech.leavemanagement.exceptions.LeaveCategoryAlreadyCreatedException;
import com.raybiztech.leavemanagement.utils.EmployeeUtils;
import com.raybiztech.leavemanagement.utils.LeaveManagementUtils;

@Service("leaveSetupService")
public class LeaveSetupServiceImpl implements LeaveSetupService {

	@Autowired
	LeaveDAO leaveDAO;

	@Autowired
	LeaveCategoryBuilder leaveCategoryBuilder;
	@Autowired
	LeaveSettingsBuilder leaveSettingsBuilder;
	@Autowired
	LeaveService leaveService;
	@Autowired
	EmployeeBuilder employeeBuilder;
	@Autowired
	LeaveSummaryBuilder leaveSummaryBuilder;
	@Autowired
	LeaveManagementUtils leaveManagementUtils;
	@Autowired
	BiometricDAO biometricDAO;
	@Autowired
	EmployeeUtils employeeUtils;

	Logger logger = Logger.getLogger(LeaveSetupServiceImpl.class);

	@Transactional
	@Override
	public void addLeaveCategory(LeaveCategoryDTO leaveCategoryDTO) {
		try {

			leaveDAO.save(leaveCategoryBuilder
					.createLeaveCategoryEntity(leaveCategoryDTO));

		} catch (Exception e) {
			throw new LeaveCategoryAlreadyCreatedException(e.getMessage());
		}

	}

	@Transactional
	@Override
	public void updateLeaveCalendarSettings(LeaveSettingsDTO leaveSettingsDTO) {
		// TODO Auto-generated method stub

		leaveDAO.saveOrUpdate(leaveSettingsBuilder
				.createLeaveSettingsEntity(leaveSettingsDTO));

	}

	@Transactional
	@Override
	public SortedSet<LeaveCategoryDTO> getAllLeaveCategories() {
		// TODO Auto-generated method stub
		return leaveCategoryBuilder.createLeaveCategoryDTOSet(leaveDAO
				.getAllLeaveCategories());
	}

	@Transactional
	@Override
	public void deleteLeaveCategory(Long id) {
		try {

			leaveDAO.delete(leaveDAO.findBy(LeaveCategory.class, id));
		} catch (Exception e) {
			throw new CannotDeleteLeaveCategoryException(e.getMessage());
		}

	}

	@Transactional
	@Override
	public void editLeaveCategory(LeaveCategoryDTO leaveCategoryDTO) {
		leaveDAO.saveOrUpdate(leaveCategoryBuilder
				.createLeaveCategoryEntity(leaveCategoryDTO));
	}

	@Transactional
	@Override
	public Map<String, Object> getAllEmployeesLeaveSummary(Integer currentYear,
			Integer startIndex, Integer endIndex) {

		DateRange financialPeriod = leaveManagementUtils
				.constructFinancicalYearPeriod(currentYear);

		if (financialPeriod.contains(new Date())) {
			financialPeriod = new DateRange(financialPeriod.getMinimum(),
					new Date());
		}

		List<CarryForwardLeave> leaveCreditsList = leaveDAO
				.getAllEmployeeCarryForwardedLeaves(financialPeriod);

		List<LeaveDebit> leaveDebitsList = leaveDAO
				.getAllEmployeeLeaveDebits(financialPeriod);
		Map<String, Object> employeeMap = biometricDAO
				.getStatusPaginatedEmployees(startIndex, endIndex, "Active","","");
		List<Employee> employeeList = (List) employeeMap.get("employeeList");
		Integer employeeSize = (Integer) employeeMap.get("size");
		Integer maxLeavesEarned = leaveDAO.getLeaveSettings()
				.getMaxLeavesEarned();

		SortedSet<LeaveSummaryDTO> leaveSummaryDTOs = leaveSummaryBuilder
				.getLeaveSummaryDtoSet(leaveDebitsList, employeeList,
						leaveDAO.getAllLeaveCategories(), leaveCreditsList,
						financialPeriod, maxLeavesEarned);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", leaveSummaryDTOs);
		map.put("size", employeeSize);
		return map;
	}

	@Transactional
	@Override
	public LeaveSettingsDTO getLeaveCalendarSetting() {

		return leaveSettingsBuilder.createLeaveSettingsDTO(leaveDAO
				.getLeaveSettings());

	}

	@Transactional
	@Override
	public List<Integer> getAllCreditedYears() {
		// TODO Auto-generated method stub
		return leaveDAO.getAllCreditedYears();
	}

	@Transactional
	@Override
	public Set<EmployeeDTO> getAllEmployeesOfCompany() {
		// TODO Auto-generated method stub
		return new HashSet<EmployeeDTO>(
				employeeBuilder.getemployeeDTOList(biometricDAO
						.getActiveEmployees()));
	}

	@Transactional
	@Override
	public LeaveCategoryDTO getleaveCategory(Long leaveCategoryId) {
		// TODO Auto-generated method stub
		return leaveCategoryBuilder.createLeaveCategoryDTO(leaveDAO.findBy(
				LeaveCategory.class, leaveCategoryId));
	}

	@Override
	public Integer getCurrentFinancialYear() {
		// TODO Auto-generated method stub
		return leaveManagementUtils.getCurrentFinancialPeriod().getMinimum()
				.getYearOfEra().getValue();
	}

	@Override
	public SortedSet<LeaveCategoryDTO> getlopCategories() {
		// TODO Auto-generated method stub
		return leaveCategoryBuilder.createLeaveCategoryDTOSet(leaveDAO
				.getlopCategories());
	}

	@Transactional
	@Override
	public Map<String, Object> searchLeaveSummaries(Integer financialYear,
			String search, Integer startIndex, Integer endIndex) {
		// TODO Auto-generated method stub
		DateRange financialPeriod = leaveManagementUtils
				.constructFinancicalYearPeriod(financialYear);

		if (financialPeriod.contains(new Date())) {
			financialPeriod = new DateRange(financialPeriod.getMinimum(),
					new Date());
		}

		List<CarryForwardLeave> leaveCreditsList = leaveDAO
				.getAllEmployeeCarryForwardedLeaves(financialPeriod);

		List<LeaveDebit> leaveDebitsList = leaveDAO
				.getAllEmployeeLeaveDebits(financialPeriod);

		Map<String, Object> searchEmpMap = biometricDAO.searchEmployees(search,
				startIndex, endIndex, "Active","");

		List<Employee> employees = (List) searchEmpMap.get("searchEmpList");
		Integer noOfEmp = (Integer) searchEmpMap.get("size");
		Integer maxLeavesEarned = leaveDAO.getLeaveSettings()
				.getMaxLeavesEarned();

		SortedSet<LeaveSummaryDTO> leaveSummaryDTOs = leaveSummaryBuilder
				.getLeaveSummaryDtoSet(leaveDebitsList, employees,
						leaveDAO.getAllLeaveCategories(), leaveCreditsList,
						financialPeriod, maxLeavesEarned);

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("list", leaveSummaryDTOs);
		map.put("size", noOfEmp);
		return map;

	}

	@Override
	public SortedSet<LeaveCategoryDTO> getEligibleLeaveCategories(
			Long employeeId) {
		// TODO Auto-generated method stub
		Employee employee = leaveDAO.findBy(Employee.class, employeeId);
		if (employee == null) {
			throw new EmployeeNotFoundException();
		}
		LeaveSettingsLookup leaveSettings = leaveDAO.getLeaveSettings();

		SortedSet<LeaveCategoryDTO> leaveCategories = null;
		
		//new functionality for employees who are in probation period will apply all leave  categories.
		
		/*if (employeeUtils.isInProbationPeriod(employee, leaveSettings)) {

			leaveCategories = getlopCategories();
		} else {

			leaveCategories = getAllLeaveCategories();
		}*/
		
		leaveCategories = getAllLeaveCategories();

		return leaveCategories;
	}

	@Override
	public ByteArrayOutputStream exportLeavereport(Integer currentYear, Integer startIndex, Integer endIndex,
			String search, HttpServletResponse response) throws IOException {
		
		DateRange financialPeriod = leaveManagementUtils
				.constructFinancicalYearPeriod(currentYear);

		if (financialPeriod.contains(new Date())) {
			financialPeriod = new DateRange(financialPeriod.getMinimum(),
					new Date());
		}

		List<CarryForwardLeave> leaveCreditsList = leaveDAO
				.getAllEmployeeCarryForwardedLeaves(financialPeriod);

		List<LeaveDebit> leaveDebitsList = leaveDAO
				.getAllEmployeeLeaveDebits(financialPeriod);

		Map<String, Object> employeeMap = new HashMap<String, Object>();
		List<Employee> employeeList = new ArrayList<Employee>();
		
		if(!search.isEmpty()) {
		employeeMap = biometricDAO.searchEmployees(search,
				null, null, "Active","");
		employeeList = (List) employeeMap.get("searchEmpList");
		}
		else {
		employeeMap = biometricDAO
				.getStatusPaginatedEmployees(null, null, "Active","","");
		employeeList = (List) employeeMap.get("employeeList");
		}
		
		
		Integer employeeSize = (Integer) employeeMap.get("size");
		Integer maxLeavesEarned = leaveDAO.getLeaveSettings()
				.getMaxLeavesEarned();

		SortedSet<LeaveSummaryDTO> leaveSummaryDTOs = leaveSummaryBuilder
				.getLeaveSummaryDtoSet(leaveDebitsList, employeeList,
						leaveDAO.getAllLeaveCategories(), leaveCreditsList,
						financialPeriod, maxLeavesEarned);
		
		SortedSet<LeaveCategory> categoryList = leaveDAO.getAllLeaveCategories();
		
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int rowindex = 1;
		List<String> columns = new ArrayList<String>();
		columns.add("Id");
		columns.add("Name");
		columns.add("Carry Forwarded (Days)");
		columns.add("Credited (Days)");
		for(LeaveCategory category : categoryList) {
			columns.add(category.getName() + " " + "Applied (Days)");
		}
		columns.add("Leaves Remaining (Days)");
		int total = (columns.size())-1;

		
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		CellStyle style = workbook.createCellStyle();
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);
		for(int i=0;i<columns.size();i++) {
			Cell celli = row1.createCell(i);
			celli.setCellStyle(style);
			celli.setCellValue(columns.get(i));
		}
		
		for(LeaveSummaryDTO record : leaveSummaryDTOs) {
			Row row = sheet.createRow(rowindex++);
			
			Cell cel0 = row.createCell(0);
			cel0.setCellValue(record.getId());
			
			Cell cel1 = row.createCell(1);
			cel1.setCellValue(record.getEmployeeDTO().getFullName());
			
			Cell cel2 = row.createCell(2);
			cel2.setCellValue(record.getCarryForwardedLeaves());
			
			Cell cel3 = row.createCell(3);
			cel3.setCellValue(record.getCalculatedCreditedLeaves());
			
			int j=4;
			for(int i=0;i < record.getLeaveCategorySummaries().size();i++) {
				
				Cell celj = row.createCell(j);
				celj.setCellValue(Double.valueOf(record.getLeaveCategorySummaries().get(i).getDaysPending()+
						record.getLeaveCategorySummaries().get(i).getDaysScheduled()+record.getLeaveCategorySummaries().get(i).getDaysTaken()));
				j++;
				
			}
			Cell celtotal = row.createCell(total);
			celtotal.setCellValue((record.getAllAvailableLeaves()));
			
			for(int i = 0; i < columns.size(); i++) {
	            sheet.autoSizeColumn(i);
	        }
		}
		
		
		workbook.write(bos);
		bos.flush();
		bos.close();

		return bos;
	}
}
