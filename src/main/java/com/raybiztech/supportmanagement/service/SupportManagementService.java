package com.raybiztech.supportmanagement.service;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.dto.EmpDepartmentDTO;
import com.raybiztech.date.Date;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.raybiztech.recruitment.business.Department;
import com.raybiztech.recruitment.dto.DepartmentDTO;
import com.raybiztech.supportmanagement.dto.SupportTicketsDTO;
import com.raybiztech.supportmanagement.dto.TicketsCategoryDTO;
import com.raybiztech.supportmanagement.dto.TicketsSubCategoryDTO;
import com.raybiztech.supportmanagement.dto.TrackerDto;

import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

public interface SupportManagementService {

	List<TicketsCategoryDTO> departmentCategoryList(Long deptId);

	void addCategory(TicketsCategoryDTO ticketsCategoryDTO);

	void addSubCategory(TicketsSubCategoryDTO ticketsSubCategoryDTO);

	List<TicketsSubCategoryDTO> subCategoryList(Long categoryId);

	List<TicketsCategoryDTO> getAllCategoryList();

	List<TicketsSubCategoryDTO> getAllSubCategories();

	Map<String, Object> createTickets(SupportTicketsDTO supportTicketsDTO);

	void uploadSupportTicketsDocuments(MultipartFile mpf, String parameter);

	void deleteCategory(Long categoryId);

	void updateCategory(TicketsCategoryDTO ticketsCategoryDTO);

	void updateSubCategory(TicketsSubCategoryDTO ticketsSubCategoryDTO);

	Map<String, Object> getAllTicketsForApproval(String ticketStatus, Long categoryId, Long subCategoryId,
			Integer startIndex, Integer endIndex, String progressStatus, String multiSearch, String searchByEmpName,
			String searchByAssigneeName, String from, String to, String dateSelection, Long departmentId,
			Long trackerID);

	void deleteSubCategory(Long subCategoryId);

	List<SupportTicketsDTO> getIndividualTickets();

	void editIndividualTickets(SupportTicketsDTO supportTicketsDTO);

	Map<String, Object> getSearchSubCategoryList(TicketsSubCategoryDTO paramsOfSubCategory, Integer startIndex,
			Integer endIndex);

	void cancelTicketRequest(Long requestId);

	void downloadFile(HttpServletResponse response, String fileName);

	List<TicketsCategoryDTO> getAllLookups();

	void approveByManagerTicket(Long ticketId);

	Map<String, Object> searchTicketData(Integer startIndex, Integer endIndex, String multiSearch);

	ByteArrayOutputStream exportRaisedTickets(String multiSearch) throws IOException;

	void rejectManagerTicket(Long tktId);

	Map<String, Object> getAudit(Long projectId, String filterName);

	String getdateandtime();

	List<EmpDepartment> departmentWiseList();

	ByteArrayOutputStream exportTicketStatusList(Long empId, String ticketStatus, Long categoryId, Long subCategoryId,
			Integer startIndex, Integer endIndex, String progressStatus, String multiSearch, String searchByEmpName,
			String searchByAssigneeName, String from, String to, String dateSelection, HttpServletResponse response,
			Long departmentId, Long trackerID);

	void updateHierarchyReportingManager(String oldmanager, String employee, String newmanager);

	List<EmpDepartmentDTO> getDepartmentNameList();

	List<EmpDepartmentDTO> getDepartmentListForRaise();

	SupportTicketsDTO getTicket(Long ticketId);

	List<TrackerDto> addTracker(TrackerDto dto);

	List<TrackerDto> deleteTracker(Long id);

	List<TrackerDto> getAllTracker();

	ByteArrayOutputStream exportTicketApprovalList(Long empId, String ticketStatus, Long categoryId, Long subCategoryId,
			Integer startIndex, Integer endIndex, String progressStatus, String multiSearch, String searchByEmpName,
			String searchByAssigneeName, String from, String to, String dateSelection, HttpServletResponse response,
			Long departmentId, Long trackerID) throws IOException;

	// To get the tickets report
	Map<String, Object> getTicketsReport(String ticketStatus, String from, String to, String dateSelection,
			Long departmentId);

	// To get the tickets list according to status
	Map<String, Object> getTicketsDetails(String filter, Long trackerId, Integer startIndex, Integer endIndex,
			Long categoryId, Long subCategoryId, String from, String to, String dateSelection, Long departmentId,
			String ticketStatus);

	ByteArrayOutputStream exportReportList(Integer startIndex, Integer endIndex, String from, String to,
			String dateSelection, Long departmentId) throws IOException;

}
