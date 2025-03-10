package com.raybiztech.supportmanagement.dao;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.EmpDepartmentDTO;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.recruitment.business.Department;
import com.raybiztech.supportmanagement.business.SupportTickets;
import com.raybiztech.supportmanagement.business.TicketsCategory;
import com.raybiztech.supportmanagement.business.TicketsSubCategory;
import com.raybiztech.supportmanagement.business.Tracker;
import com.raybiztech.supportmanagement.dto.TicketsSubCategoryDTO;
import com.raybiztech.ticketmanagement.business.Ticket;
import com.raybiztech.ticketmanagement.business.TicketHistory;

import java.util.List;
import java.util.Map;

import org.jfree.chart.axis.SubCategoryAxis;

public interface SupportManagementDAO extends DAO {

	List<TicketsCategory> departmentCategoryList(Long deptId);

	List<TicketsSubCategory> subCategoryList(Long categoryId);

	// The employee will get his reportes tickets
	Map<String, Object> getReporteeTickets(/* List<Long> empIds */String managerId, String ticketStatus,
			Long categoryId, Long subCategoryId, Integer startIndex, Integer endIndex, String progressStatus,
			String multiSearch, String searchByEmpName, String searchByAssigneeName, DateRange dateRange,
			Long departmentId, Long trackerID);

	Map<String, Object> getTotalTicketsForAdmin(String ticketStatus, Long categoryId, Long subCategoryId,
			Integer startIndex, Integer endIndex, String progressStatus, String multiSearch, String searchByEmpName,
			String searchByAssigneeName, DateRange dateRange, Long departmentId, Long trackerID);

	// get Individual tickets of employee
	List<SupportTickets> getIndividualTickets(Long loggedEmpId);

	// get Search value of SubCategory
	Map<String, Object> getSearchSubCategoryList(TicketsSubCategoryDTO ticketsSubCategoryDto, Integer startIndex,
			Integer endIndex);

	Map<String, Object> getDepartmentWiseTickets(String departmentName, Long categoryId, Long subCategoryId,
			Integer startIndex, Integer endIndex, String progressStatus, String multiSearch, String searchByEmpName,
			String searchByAssigneeName, DateRange dateRange, Long departmentId, Long trackerID);

	List<TicketsCategory> getCategories(String departmentName);

	// SEARCH FOR INDIVIDUAL SUMMARY
	Map<String, Object> searchTicketData(Long loggedInEmpId, Integer startIndex, Integer endIndex, String multiSearch);

	List<SupportTickets> getTicketList(Date date, TicketsSubCategory ticketsSubCategory);

	List<TicketsCategory> getAllCAtegories();

	List<EmpDepartment> departmentWiseList(Employee employee);

	List<SupportTickets> getTicketHistorys(Long ticketId);
	// List<SupportTickets> updateHierarchyReportingManager(String oldmanager,String
	// employee);

	List<EmpDepartment> getDepartmentNameList();

	List<EmpDepartment> getDepartmentListForRaise();

	List<Tracker> getAllTracker();

	Integer trackerInUse(Long id);

	List<SupportTickets> getAccessTicketsWhoseEndDateisInNextFiveDays();

	// To get the tickets report for Admin
	Map<String, Object> getTicketsReportForAdmin(DateRange dateRange, Long departmentId);

	// To get the tickets report for Manager
	Map<String, Object> getManagerTicketsReport(String managerId, DateRange dateRange, Long departmentId);

	// To get the tickets report for Department
	Map<String, Object> getDepartmentWiseTicketsReport(String departmentName, DateRange dateRange, Long departmentId);

	// To get the tickets list according to status for Manager
	Map<String, Object> getManagerTicketsDetails(String managerId, String filter, Long trackerId, Integer startIndex,
			Integer endIndex, Long categoryId, Long subCategoryId, DateRange dateRange, Long departmentId);

	// To get the tickets list according to status for Admin
	Map<String, Object> getTotalTicketsDetailsForAdmin(String filter, Long trackerId, Integer startIndex,
			Integer endIndex, Long categoryId, Long subCategoryId, DateRange dateRange, Long departmentId);

	// To get the tickets list according to status for Department
	Map<String, Object> getDepartmentWiseTicketsDetails(String departmentName, String filter, Long trackerId,
			Integer startIndex, Integer endIndex, Long categoryId, Long subCategoryId, DateRange dateRange,
			Long departmentId);
	
	List<String> getManagersListforPendingFoodTicket();

}
