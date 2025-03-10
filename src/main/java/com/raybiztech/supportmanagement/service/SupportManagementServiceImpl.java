package com.raybiztech.supportmanagement.service;

import com.raybiztech.appraisalmanagement.business.AppraisalForm;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.EmpDepartmentDTO;
import com.raybiztech.appraisals.exception.FileUploaderUtilException;
import com.raybiztech.appraisals.exceptions.UnauthorizedUserException;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.appraisals.utils.FileUploaderUtil;
import com.raybiztech.assetmanagement.service.AssetManagementServiceImpl;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.mailtemplates.util.SupportManagementMailConfiguration;
import com.raybiztech.projectmanagement.builder.AuditBuilder;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.recruitment.utils.FileUploaderUtility;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.supportmanagement.builder.SupportAuditBuilder;
import com.raybiztech.supportmanagement.builder.SupportTicketsBuilder;
import com.raybiztech.supportmanagement.builder.TicketsCategoryBuilder;
import com.raybiztech.supportmanagement.builder.TicketsSubCategoryBuilder;
import com.raybiztech.supportmanagement.business.SupportTickets;
import com.raybiztech.supportmanagement.business.TicketsCategory;
import com.raybiztech.supportmanagement.business.TicketsSubCategory;
import com.raybiztech.supportmanagement.business.Tracker;
import com.raybiztech.supportmanagement.dao.SupportManagementDAO;
import com.raybiztech.supportmanagement.dto.SupportTicketsDTO;
import com.raybiztech.supportmanagement.dto.TicketReportDTO;
import com.raybiztech.supportmanagement.dto.TicketsCategoryDTO;
import com.raybiztech.supportmanagement.dto.TicketsSubCategoryDTO;
import com.raybiztech.supportmanagement.dto.TrackerDto;
import com.raybiztech.supportmanagement.mailNotification.SupportTicketMailNotification;
import com.raybiztech.supportmanagement.utility.EntityInUseException;
import com.raybiztech.ticketmanagement.exceptions.DateException;
import com.raybiztech.ticketmanagement.exceptions.DateTimeException;

@Service("supportManagementServiceImpl")
@Transactional
public class SupportManagementServiceImpl implements SupportManagementService, Cloneable {

	Logger logger = Logger.getLogger(SupportManagementServiceImpl.class);

	@Autowired
	SupportManagementDAO supportManagementDAOImpl;
	@Autowired
	TicketsCategoryBuilder ticketsCategoryBuilder;
	@Autowired
	TicketsSubCategoryBuilder ticketsSubCategoryBuilder;
	@Autowired
	SupportTicketsBuilder supportTicketsBuilder;
	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	PropBean propBean;
	@Autowired
	AuditBuilder auditBuilder;
	@Autowired
	SupportAuditBuilder supportAuditBuilder;
	@Autowired
	ProjectService projectServiceImpl;
	@Autowired
	SupportManagementMailConfiguration supportManagementMailConfiguration;

	@Override
	public List<TicketsCategoryDTO> departmentCategoryList(Long deptId) {
		return ticketsCategoryBuilder.getDTOList(supportManagementDAOImpl.departmentCategoryList(deptId));
	}

	@Override
	public List<TicketsSubCategoryDTO> subCategoryList(Long categoryId) {
		return ticketsSubCategoryBuilder.getDTOList(supportManagementDAOImpl.subCategoryList(categoryId));
	}

	@Override
	@Transactional
	public Map<String, Object> createTickets(SupportTicketsDTO supportTicketsDTO) {
		TicketsSubCategory subCategory = dao.findBy(TicketsSubCategory.class, supportTicketsDTO.getSubCategoryId());

		Boolean validToSave = false;
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<String> dublicateNames = new ArrayList<String>();
		String date = supportTicketsDTO.getStartDate();
		String serverdate = this.getdateandtime();

		String[] dateonly = serverdate.split(" ");
		String timeonly = dateonly[1];

		String[] houronly = timeonly.split(":");
		DateFormat dfo = new SimpleDateFormat("dd/MM/yyyy");

		Date frontdate = null;
		Date servaerDate = null;
		try {
			servaerDate = DateParser.toDate(dateonly[0]);
			frontdate = DateParser.toDate(date);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		// This is used when the category is meal type and makes the
		// "validToSave" flag as true if there are no duplicate names exists
		// by checking whether the list is empty or not
		if (subCategory.getTicketsCategory().isMealType()) {
			if (dateonly[0].equalsIgnoreCase(date)) {
				if (Integer.parseInt(houronly[0]) >= 17 && Integer.parseInt(houronly[1]) >= 30) {
					throw new DateTimeException("After 17:30 you can't raise Ticket");
				} else
					dublicateNames = saveEmployeesForTicket(supportTicketsDTO);
			} else if (frontdate.isAfter(servaerDate)) {
				dublicateNames = saveEmployeesForTicket(supportTicketsDTO);
			} else {
				throw new DateException("you can't raise ticket for previous dates");
			}
			if (dublicateNames.isEmpty()) {
				validToSave = true;
			}
		} else {
			// If it is not meal type it will directly save by making the flag
			// as true
			validToSave = true;
		}

		if (validToSave) {
			SupportTickets tickets = supportTicketsBuilder.convertDTOToEntity(supportTicketsDTO);
			Long id = (Long) supportManagementDAOImpl.save(tickets);
			// for storing in audit table
			List<Audit> supportAudit = auditBuilder.auditTOSupportEntity(tickets, id, "support_tickets", "CREATED");
			for (Audit audit : supportAudit) {
				supportManagementDAOImpl.save(audit);
			}

			supportManagementMailConfiguration.sendNewTicketMail(tickets);

			returnMap.put("ticketId", id);
		} else
			returnMap.put("existedEmpNames", dublicateNames);
		return returnMap;
	}

	public List<String> saveEmployeesForTicket(SupportTicketsDTO supportTicketsDTO) {

		Set<Long> set = new HashSet<Long>(supportTicketsDTO.getWatcherIds());
		List<Long> watcherIds = new ArrayList<Long>(set);
		List<String> dublicateNames = new ArrayList<String>();
		Date startDate = null;
		for (Long watcherId : watcherIds) {
			Employee employee = dao.findBy(Employee.class, watcherId);
			try {
				startDate = DateParser.toDate(supportTicketsDTO.getStartDate());
			} catch (ParseException ex) {
				java.util.logging.Logger.getLogger(SupportManagementServiceImpl.class.getName()).log(Level.SEVERE, null,
						ex);
			}
			TicketsSubCategory subCategory = dao.findBy(TicketsSubCategory.class, supportTicketsDTO.getSubCategoryId());
			Boolean flag = true;
			List<SupportTickets> supportTickets = supportManagementDAOImpl.getTicketList(startDate, subCategory);
			if (!supportTickets.isEmpty()) {
				for (SupportTickets tickets : supportTickets) {
					String[] id = tickets.getTicketWatchers().split("_");
					for (String var : id) {
						if (watcherId.toString().equalsIgnoreCase(var)) {
							flag = false;
							break;
						}
					}

				}
			}
			if (!flag) {
				dublicateNames.add(employee.getFullName());
			}

		}

		return dublicateNames;
	}

	@Override
	public void addCategory(TicketsCategoryDTO ticketsCategoryDTO) {
		TicketsCategory ticketsCategory = ticketsCategoryBuilder.convertDTOToEntity(ticketsCategoryDTO);
		supportManagementDAOImpl.save(ticketsCategory);

	}

	@Override
	public void addSubCategory(TicketsSubCategoryDTO ticketsSubCategoryDTO) {
		Long id = (Long) dao.save(ticketsSubCategoryBuilder.convertDTOToEntity(ticketsSubCategoryDTO));
		TicketsSubCategory subcatagory = dao.findBy(TicketsSubCategory.class, id);

		// for storing in audit table
		List<Audit> supportAudit = auditBuilder.auditBySupportSubcatagory(subcatagory, "tickets_subcategory",
				"CREATED");

		for (Audit audit : supportAudit) {

			supportManagementDAOImpl.save(audit);
		}
	}

	@Override
	public List<TicketsSubCategoryDTO> getAllSubCategories() {
		List<TicketsSubCategory> subCategories = dao.get(TicketsSubCategory.class);
		return ticketsSubCategoryBuilder.getDTOList(subCategories);
	}

	@Override
	public List<TicketsCategoryDTO> getAllCategoryList() {

		List<TicketsCategory> ticketsCategories = supportManagementDAOImpl.getAllCAtegories();
		return ticketsCategoryBuilder.getDTOList(ticketsCategories);
	}

	@Override
	public void uploadSupportTicketsDocuments(MultipartFile mpf, String ticketId) {

		if (ticketId != null) {
			Long id = Long.parseLong(ticketId);
			SupportTickets tickets = dao.findBy(SupportTickets.class, id);
			FileUploaderUtility fileUploaderUtility = new FileUploaderUtility();
			try {
				String path = fileUploaderUtility.uploadSupportTicketsDocuments(tickets, mpf, propBean);
				tickets.setDocumentsPath(path);
				dao.update(tickets);
			} catch (IOException ex) {

			}
		}
	}

	@Override
	public void deleteCategory(Long categoryId) {
		dao.delete(dao.findBy(TicketsCategory.class, categoryId));
	}

	@Override
	public void updateCategory(TicketsCategoryDTO ticketsCategoryDTO) {
		dao.update(ticketsCategoryBuilder.toEditEntity(ticketsCategoryDTO));
	}

	@Override
	public void updateSubCategory(TicketsSubCategoryDTO ticketsSubCategoryDTO) {
		TicketsSubCategory subCategory = dao.findBy(TicketsSubCategory.class, ticketsSubCategoryDTO.getSubCategoryId());
		TicketsSubCategory oldsubcatagory = new TicketsSubCategory();
		try {
			oldsubcatagory = (TicketsSubCategory) subCategory.clone();
		} catch (CloneNotSupportedException ex) {
			java.util.logging.Logger.getLogger(SupportManagementServiceImpl.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		TicketsSubCategory subCategory3 = ticketsSubCategoryBuilder.toEditEntity(ticketsSubCategoryDTO);
		dao.update(subCategory3);

		// for storing in audit table
		List<Audit> supportAudit = auditBuilder.UpdateAuditBySupportSubcatagory(subCategory3, oldsubcatagory,
				"tickets_subcategory", "UPDATED");

		for (Audit audit : supportAudit) {

			supportManagementDAOImpl.save(audit);
		}

	}

	@Override
	public Map<String, Object> getAllTicketsForApproval(String ticketStatus, Long categoryId, Long subCategoryId,
			Integer startIndex, Integer endIndex, String progressStatus, String multiSearch, String searchByEmpName,
			String searchByAssigneeName, String from, String to, String dateSelection, Long departmentId,
			Long trackerID) {

		Date fromDate = null;
		Date toDate = null;

		if (dateSelection.equalsIgnoreCase("Custom")) {
			try {
				fromDate = DateParser.toDate(from);
				toDate = DateParser.toDate(to);
			} catch (ParseException ex) {
				java.util.logging.Logger.getLogger(AssetManagementServiceImpl.class.getName()).log(Level.SEVERE, null,
						ex);
			}
		} else {
			Map<String, Date> dateMap = dao.getCustomDates(dateSelection);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}

		DateRange dateRange = new DateRange(fromDate, toDate);

		Map<String, Object> map = securityUtils.getLoggedEmployeeDetailsSecurityContextHolder();
		Map<String, Object> finalMap = new HashMap<String, Object>();
		Employee loggedInEmployee = (Employee) map.get("employee");

		Permission hierarchyPermission = dao.checkForPermission("Hierarchy Ticket Approvals", loggedInEmployee);
		Permission totalListPermission = dao.checkForPermission("Ticket Approvals", loggedInEmployee);
		Permission departmentPermission = dao.checkForPermission("Department Wise Ticket Approvals", loggedInEmployee);

		List<SupportTicketsDTO> supportTicketsDTOs = null;
		// List<SupportTickets> supportTickets = null;

		if (hierarchyPermission.getView() && totalListPermission.getView() && !departmentPermission.getView()) {
			// List<Long> empIds = projectServiceImpl
			// .mangerUnderManager(loggedInEmployee.getEmployeeId());

			// For Manager
			finalMap = supportManagementDAOImpl.getReporteeTickets(loggedInEmployee.getEmployeeId().toString(),
					ticketStatus, categoryId, subCategoryId, startIndex, endIndex, progressStatus, multiSearch,
					searchByEmpName, searchByAssigneeName, dateRange, departmentId, trackerID);
		} else if (totalListPermission.getView() && !hierarchyPermission.getView() && !departmentPermission.getView()) {
			// For Admin
			finalMap = supportManagementDAOImpl.getTotalTicketsForAdmin(ticketStatus, categoryId, subCategoryId,
					startIndex, endIndex, progressStatus, multiSearch, searchByEmpName, searchByAssigneeName, dateRange,
					departmentId, trackerID);
		} else if (totalListPermission.getView() && !hierarchyPermission.getView() && departmentPermission.getView()) {
			// For department wise based tickets
			finalMap = supportManagementDAOImpl.getDepartmentWiseTickets(loggedInEmployee.getDepartmentName(),
					categoryId, subCategoryId, startIndex, endIndex, progressStatus, multiSearch, searchByEmpName,
					searchByAssigneeName, dateRange, departmentId, trackerID);
		}
		// For Department Wise manager
		else if (totalListPermission.getView() && hierarchyPermission.getView() && departmentPermission.getView()) {

			// department ticket
			if (ticketStatus.equalsIgnoreCase("Department")) {
				finalMap = supportManagementDAOImpl.getDepartmentWiseTickets(loggedInEmployee.getDepartmentName(),
						categoryId, subCategoryId, startIndex, endIndex, progressStatus, multiSearch, searchByEmpName,
						searchByAssigneeName, dateRange, departmentId, trackerID);
			} else {
				// As a manager ticket
				finalMap = supportManagementDAOImpl.getReporteeTickets(loggedInEmployee.getEmployeeId().toString(),
						ticketStatus, categoryId, subCategoryId, startIndex, endIndex, progressStatus, multiSearch,
						searchByEmpName, searchByAssigneeName, dateRange, departmentId, trackerID);
			}

		}

		// return supportTicketsDTOs;

		List<SupportTickets> list = (List<SupportTickets>) finalMap.get("list");
		Integer noOfrecords = (Integer) finalMap.get("size");
		supportTicketsDTOs = supportTicketsBuilder.toDTOList(list);
		Map<String, Object> ticketMap = new HashMap<String, Object>();
		ticketMap.put("list", supportTicketsDTOs);
		ticketMap.put("size", noOfrecords);

		return ticketMap;
	}

	@Override
	public void deleteSubCategory(Long subCategoryId) {
		dao.delete(dao.findBy(TicketsSubCategory.class, subCategoryId));
	}

	@Override
	public List<SupportTicketsDTO> getIndividualTickets() {
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		List<SupportTicketsDTO> supportTicketsDTO = supportTicketsBuilder
				.toDTOList(supportManagementDAOImpl.getIndividualTickets(loggedInEmpId));
		return supportTicketsDTO;
	}

	@Override
	public void editIndividualTickets(SupportTicketsDTO supportTicketsDTO) {
		SupportTickets ticket = dao.findBy(SupportTickets.class, supportTicketsDTO.getId());

		SupportTickets oldTicket = cloneMethod(ticket);

		SupportTickets supportTickets = supportTicketsBuilder.editDTOToEntity(supportTicketsDTO, oldTicket);

		supportManagementDAOImpl.update(supportTickets);

		// for storing in audit table
		List<Audit> supportAudit = auditBuilder.UpdateAuditTOSupportEntity(supportTickets, supportTicketsDTO.getId(),
				oldTicket, "support_tickets", "UPDATED");

		for (Audit audit : supportAudit) {
			supportManagementDAOImpl.save(audit);
		}

		supportManagementMailConfiguration.sendTicketUpdationMail(ticket);
	}

	@Override
	public Map<String, Object> getSearchSubCategoryList(TicketsSubCategoryDTO paramsOfSubCategory, Integer startIndex,
			Integer endIndex) {
		Map<String, Object> subMap = supportManagementDAOImpl.getSearchSubCategoryList(paramsOfSubCategory, startIndex,
				endIndex);

		List<TicketsSubCategory> list = (List<TicketsSubCategory>) subMap.get("list");
		Integer noOfrecords = (Integer) subMap.get("size");
		List<TicketsSubCategoryDTO> subCategory = ticketsSubCategoryBuilder.ToDTOList(list);
		Map<String, Object> subCategoryMap = new HashMap<String, Object>();
		subCategoryMap.put("list", subCategory);
		subCategoryMap.put("size", noOfrecords);

		return subCategoryMap;
	}

	@Override
	@Transactional
	public void cancelTicketRequest(Long requestId) {
		SupportTickets supportTickets = supportManagementDAOImpl.findBy(SupportTickets.class, requestId);
		SupportTickets olSupportTickets = cloneMethod(supportTickets);

		supportTickets.setApprovalStatus("Cancelled");
		// supportTickets.setStatus("Cancelled");
		dao.saveOrUpdate(supportTickets);
		List<Audit> supportAudit = auditBuilder.UpdateAuditTOSupportEntity(supportTickets, supportTickets.getId(),
				olSupportTickets, "support_tickets", "CANCELLED");

		for (Audit audit : supportAudit) {
			supportManagementDAOImpl.save(audit);
		}

		supportManagementMailConfiguration.sendTicketCancellationMail(supportTickets);

	}

	@Override
	public void downloadFile(HttpServletResponse response, String fileName) {

		try {
			FileUploaderUtil util = new FileUploaderUtil();
			String filePath = (String) propBean.getPropData().get("supportDocLocation");
			util.downloadUploadedFile(response, fileName, filePath);
		} catch (Exception ex) {
			throw new FileUploaderUtilException("Exception occured while uploading a file in Legal " + ex.getMessage(),
					ex);
		}

	}

	@Override
	public List<TicketsCategoryDTO> getAllLookups() {

		Map<String, Object> map = securityUtils.getLoggedEmployeeDetailsSecurityContextHolder();
		Employee loggedInemployee = (Employee) map.get("employee");
		List<TicketsCategory> ticketsCategories = new ArrayList<TicketsCategory>();

		Permission totalListPermission = dao.checkForPermission("Ticket Approvals", loggedInemployee);
		Permission departmentPermission = dao.checkForPermission("Department Wise Ticket Approvals", loggedInemployee);

		if (totalListPermission.getView() && !departmentPermission.getView()) {
			ticketsCategories = dao.get(TicketsCategory.class);
		} else if (totalListPermission.getView() && departmentPermission.getView()) {
			ticketsCategories = supportManagementDAOImpl.getCategories(loggedInemployee.getDepartmentName());
		}

		return ticketsCategoryBuilder.getDTOList(ticketsCategories);
	}

	@Override
	public Map<String, Object> searchTicketData(Integer startIndex, Integer endIndex, String multiSearch) {

		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();

		Map<String, Object> map = supportManagementDAOImpl.searchTicketData(loggedInEmpId, startIndex, endIndex,
				multiSearch);

		List<SupportTickets> list = (List<SupportTickets>) map.get("list");
		Integer noOfrecords = (Integer) map.get("size");
		List<SupportTicketsDTO> suTickets = supportTicketsBuilder.toDTOList(list);
		Map<String, Object> ticketMap = new HashMap<String, Object>();
		ticketMap.put("list", suTickets);
		ticketMap.put("size", noOfrecords);

		return ticketMap;
	}

	@Override
	public void approveByManagerTicket(Long ticketId) {
		Employee employee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder().get("employee");
		SupportTickets supportTickt = dao.findBy(SupportTickets.class, ticketId);
		SupportTickets oldticket = cloneMethod(supportTickt);

		String date = supportTickt.getStartDate().toString("dd/MM/yyyy");
		String serverdate = this.getdateandtime();

		String[] dateonly = serverdate.split(" ");
		String timeonly = dateonly[1];

		String[] houronly = timeonly.split(":");

		Date frontdate = null;
		Date servaerDate = null;
		try {
			servaerDate = DateParser.toDate(dateonly[0]);
			frontdate = DateParser.toDate(date);
		} catch (ParseException e1) {
			e1.printStackTrace();
		}

		Permission hierarchyPermission = dao.checkForPermission("Hierarchy Ticket Approvals", employee);
		Permission totalListPermission = dao.checkForPermission("Ticket Approvals", employee);
		Permission departmentPermission = dao.checkForPermission("Department Wise Ticket Approvals", employee);
		// If admin approved or the ticket has no level then here we set the
		// approval status as approved
		if ((totalListPermission.getView() && !hierarchyPermission.getView() && !departmentPermission.getView())) {
			supportTickt.setApprovalStatus("Approved");
		} else {
			// If there is some hierarchy level to get approved for a ticket
			String[] check = supportTickt.getManagesList().split(",");
			// It checks whether the level given for that subcategory in the
			// ticket and the length of List of managers approved

			// If the list of manager approval length is less than the sub
			// category level then next manager in the hierarchy will be added
			// to that list
			if (check.length < supportTickt.getTicketsSubCategory().getLevelOfHierarchy()
					&& check[check.length - 1].equalsIgnoreCase(employee.getEmployeeId().toString())) {
				supportTickt.setManagesList(
						supportTickt.getManagesList() + "," + employee.getManager().getEmployeeId().toString());
				// supportTicketMailNotification.sendRaiseTicketMailNotification(supportTickt);
			}
			String[] check1 = supportTickt.getManagesList().split(",");
			// If both level and manager approval length are equal then directly
			// set the approval status as 'Approved'
			if (supportTickt.getTicketsSubCategory().getTicketsCategory().isMealType()) {
				if (dateonly[0].equalsIgnoreCase(date)) {
					if (Integer.parseInt(houronly[0]) >= 18) {
						throw new DateTimeException("After 18 you can't approve Ticket");
					} else if (check1.length >= supportTickt.getTicketsSubCategory().getLevelOfHierarchy()
							&& check1[check1.length - 1].equalsIgnoreCase(employee.getEmployeeId().toString())) {
						supportTickt.setApprovalStatus("Approved");
						// supportTicketMailNotification.sendManagerApprovedTicketMailNotification(supportTickt);
					}
				} else if (frontdate.isAfter(servaerDate)) {
					if (check1.length >= supportTickt.getTicketsSubCategory().getLevelOfHierarchy()
							&& check1[check1.length - 1].equalsIgnoreCase(employee.getEmployeeId().toString())) {
						supportTickt.setApprovalStatus("Approved");
						// supportTicketMailNotification.sendManagerApprovedTicketMailNotification(supportTickt);
					}
				} else {
					throw new DateException("you can't approve ticket for previous dates");
				}
			} else {
				// If it is not meal type it will directly save by making the flag
				// as true
				if (check1.length >= supportTickt.getTicketsSubCategory().getLevelOfHierarchy()
						&& check1[check1.length - 1].equalsIgnoreCase(employee.getEmployeeId().toString())) {
					supportTickt.setApprovalStatus("Approved");
					// supportTicketMailNotification.sendManagerApprovedTicketMailNotification(supportTickt);
				}
			}
		}
		// This is for audit
		// if (validToSave) {
		dao.saveOrUpdate(supportTickt);
		List<Audit> supportAudit = auditBuilder.UpdateAuditTOSupportEntity(supportTickt, supportTickt.getId(),
				oldticket, "support_tickets", "UPDATED");
		for (Audit audit : supportAudit) {
			supportManagementDAOImpl.save(audit);
		}
		try {
			if (supportTickt.getApprovalStatus().equalsIgnoreCase("Approved")) {
				supportManagementMailConfiguration.sendTicketApproval(supportTickt);
				/*
				 * supportTicketMailNotification
				 * .sendManagerApprovedTicketMailNotification(supportTickt);
				 */
			} else {
				supportManagementMailConfiguration.sendNewTicketMail(supportTickt);
				/*
				 * supportTicketMailNotification .sendRaiseTicketMailNotification(supportTickt);
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		// }
	}

	@Override
	public void rejectManagerTicket(Long tktId) {
		SupportTickets supportTickets = dao.findBy(SupportTickets.class, tktId);
		SupportTickets olSupportTickets = cloneMethod(supportTickets);
		supportTickets.setApprovalStatus("Rejected");
		dao.saveOrUpdate(supportTickets);
		List<Audit> supportAudit = auditBuilder.UpdateAuditTOSupportEntity(supportTickets, supportTickets.getId(),
				olSupportTickets, "support_tickets", "REJECTED");

		for (Audit audit : supportAudit) {
			supportManagementDAOImpl.save(audit);
		}

		supportManagementMailConfiguration.sendTicketRejectionMail(supportTickets);

		/*
		 * try { supportTicketMailNotification
		 * .sendManagerRejectTicketMailNotification(supportTickets); } catch (Exception
		 * e) { e.printStackTrace(); }
		 */
	}

	@Override
	public Map<String, Object> getAudit(Long id, String filterName) {
		String dbName = null;
		if (filterName.equalsIgnoreCase("support")) {
			dbName = "support_tickets";
		} else if (filterName.equalsIgnoreCase("sub_catagory")) {
			dbName = "tickets_subcategory";
		}
		Map<String, List<Audit>> map = dao.getAudit(id, dbName);

		return supportAuditBuilder.ToSupportAuditDTO(map);
	}

	@Override
	public String getdateandtime() {

		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Calendar calobj = Calendar.getInstance();
		String date = df.format(calobj.getTime());
		return date;

	}

	@Override
	public List<EmpDepartment> departmentWiseList() {
		List<EmpDepartment> empDepartments = new ArrayList<EmpDepartment>();
		Long empId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee loggedInemployee = dao.findBy(Employee.class, empId);
		Permission departmentWiseList = dao.checkForPermission("Department Wise Ticket Approvals", loggedInemployee);
		if (departmentWiseList.getView())
			empDepartments = supportManagementDAOImpl.departmentWiseList(loggedInemployee);
		else
			empDepartments = dao.get(EmpDepartment.class);
		return empDepartments;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ByteArrayOutputStream exportTicketStatusList(Long empId, String ticketStatus, Long categoryId,
			Long subCategoryId, Integer startIndex, Integer endIndex, String progressStatus, String multiSearch,
			String searchByEmpName, String searchByAssigneeName, String from, String to, String dateSelection,
			HttpServletResponse response, Long departmentId, Long trackerID) {

		Date fromDate = null;
		Date toDate = null;

		if (dateSelection.equalsIgnoreCase("Custom")) {
			try {
				fromDate = DateParser.toDate(from);
				toDate = DateParser.toDate(to);
			} catch (ParseException ex) {
				java.util.logging.Logger.getLogger(AssetManagementServiceImpl.class.getName()).log(Level.SEVERE, null,
						ex);
			}
		} else {
			Map<String, Date> dateMap = dao.getCustomDates(dateSelection);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}

		DateRange dateRange = new DateRange(fromDate, toDate);

		Integer total = 0;

		Employee employee = dao.findBy(Employee.class, empId);

		Map<String, Object> map = new HashMap<String, Object>();

		Permission hierarchyPermission = dao.checkForPermission("Hierarchy Ticket Approvals", employee);
		Permission totalListPermission = dao.checkForPermission("Ticket Approvals", employee);
		Permission departmentPermission = dao.checkForPermission("Department Wise Ticket Approvals", employee);

		if (hierarchyPermission.getView() && totalListPermission.getView() && !departmentPermission.getView()) {
			// List<Long> empIds =
			// projectServiceImpl.mangerUnderManager(employee
			// .getEmployeeId());
			// For Manager
			map = supportManagementDAOImpl.getReporteeTickets(/* empIds */empId.toString(), ticketStatus, categoryId,
					subCategoryId, null, null, progressStatus, multiSearch, searchByEmpName, searchByAssigneeName,
					dateRange, departmentId, trackerID);
		} else if (totalListPermission.getView() && !hierarchyPermission.getView() && !departmentPermission.getView()) {
			// For Admin
			map = supportManagementDAOImpl.getTotalTicketsForAdmin(ticketStatus, categoryId, subCategoryId, null, null,
					progressStatus, multiSearch, searchByEmpName, searchByAssigneeName, dateRange, departmentId,
					trackerID);
		} else if (totalListPermission.getView() && !hierarchyPermission.getView() && departmentPermission.getView()) {
			// For department wise based tickets
			map = supportManagementDAOImpl.getDepartmentWiseTickets(employee.getDepartmentName(), categoryId,
					subCategoryId, null, null, progressStatus, multiSearch, searchByEmpName, searchByAssigneeName,
					dateRange, departmentId, trackerID);
		}
		List<SupportTickets> supportTicket = (List<SupportTickets>) map.get("list");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			Workbook workbook = new HSSFWorkbook();
			Sheet sheet = workbook.createSheet();
			Row row1 = sheet.createRow(0);

			Font font = workbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			font.setFontHeightInPoints((short) 10);
			CellStyle style = workbook.createCellStyle();

			style.setAlignment(CellStyle.ALIGN_CENTER);
			style.setFont(font);

			Cell cell0 = row1.createCell(0);
			cell0.setCellValue("Ticket Number");
			cell0.setCellStyle(style);

			Cell cell1 = row1.createCell(1);
			cell1.setCellValue("Manager Name");
			cell1.setCellStyle(style);

			Cell cell2 = row1.createCell(2);
			cell2.setCellValue("Employee Name");
			cell2.setCellStyle(style);

			Cell cell3 = row1.createCell(3);
			cell3.setCellValue("Ticket Status");
			cell3.setCellStyle(style);

			Cell cell4 = row1.createCell(4);
			cell4.setCellValue("Date");
			cell4.setCellStyle(style);

			Cell cell5 = row1.createCell(5);
			cell5.setCellValue("Meal Type");
			cell5.setCellStyle(style);

			Cell cell6 = row1.createCell(6);
			cell6.setCellValue("Signature");
			cell6.setCellStyle(style);

			int rowIndex = 1;
			int empTicketList = 0;

			for (SupportTickets ticketlist : supportTicket) {

				List<SupportTickets> ticketHistory = supportManagementDAOImpl.getTicketHistorys(ticketlist.getId());

				// Employee e = dao.findBy(Employee.class,
				// ticketlist.getCreatedBy());

				try {

					for (SupportTickets history : ticketHistory) {

						if (history.getTicketWatchers() != null) {

							Row row = sheet.createRow(rowIndex++);

							String[] empIdList = history.getTicketWatchers().split("_");
							empTicketList = empIdList.length;

							for (String id : empIdList) {

								row = sheet.createRow(rowIndex++);

								Employee emp = dao.findBy(Employee.class, Long.parseLong(id));

								Cell cel0 = row.createCell(0);
								cel0.setCellValue(ticketlist.getId());

								Cell cel1 = row.createCell(1);
								cel1.setCellValue(emp.getManager().getFullName());

								Cell cel2 = row.createCell(2);
								cel2.setCellValue(emp.getFullName());

								Cell cel3 = row.createCell(3);
								cel3.setCellValue(ticketlist.getApprovalStatus());

								Cell cel4 = row.createCell(4);
								cel4.setCellValue(ticketlist.getStartDate().toString());

								Cell cel5 = row.createCell(5);
								cel5.setCellValue(ticketlist.getTicketsSubCategory().getSubCategoryName());
								total++;
							}
							// row = sheet.createRow(rowIndex++);
						}
					}
					sheet.autoSizeColumn(0);
					sheet.autoSizeColumn(1);
					sheet.autoSizeColumn(2);
					sheet.autoSizeColumn(3);
					sheet.autoSizeColumn(4);
					sheet.autoSizeColumn(5);
					sheet.autoSizeColumn(6);
				} catch (NullPointerException npe) {
				}

			}

			int lastRow = rowIndex + 2;
			Row row2 = sheet.createRow(lastRow);

			Cell cel = row2.createCell(0);
			cel.setCellValue("Total Employees");
			cel.setCellStyle(style);

			Cell cel1 = row2.createCell(1);
			cel1.setCellValue(total);
			cel1.setCellStyle(style);

			workbook.write(bos);
			bos.flush();
			bos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return bos;
	}

	// If reporting manager for any Employee changes then below method will
	// change the approval from reporting manager
	@Override
	public void updateHierarchyReportingManager(String oldmanager, String employee, String newmanager) {
		List<SupportTickets> list = supportManagementDAOImpl.updateHierarchyReportingManager(oldmanager, employee,
				SupportTickets.class, "approvalStatus", "managesList", "createdBy");

		for (SupportTickets supportTickets : list) {
			SupportTickets oldTicket = cloneMethod(supportTickets);
			String newChangedManager = supportTickets.getManagesList();
			newChangedManager = newChangedManager.replace(oldmanager, newmanager);
			supportTickets.setManagesList(newChangedManager);

			dao.saveOrUpdate(supportTickets);
			List<Audit> supportAudit = auditBuilder.UpdateAuditTOSupportEntity(supportTickets, supportTickets.getId(),
					oldTicket, "support_tickets", "UPDATED");

			for (Audit audit : supportAudit) {
				supportManagementDAOImpl.save(audit);
			}

			supportManagementMailConfiguration.sendNewTicketMail(supportTickets);

			/*
			 * supportTicketMailNotification
			 * .sendRaiseTicketMailNotification(supportTickets);
			 */

		}
		// for appraisals
		// List<AppraisalForm>
		// listForAppraisal=supportManagementDAOImpl.updateHierarchyReportingManagerForAppraisal(oldmanager,
		// employee, AppraisalForm.class, "managesList");
		// for(AppraisalForm appraisalForm:listForAppraisal){
		// logger.warn("for loop:"+appraisalForm.getId());
		// AppraisalForm
		// oldAppraisalForm=cloneMethodForAppraisal(appraisalForm);
		// String newChangedManager=appraisalForm.getManagesList();
		// newChangedManager=newChangedManager.replace(oldmanager,newmanager);
		// appraisalForm.setManagesList(newChangedManager);
		// dao.update(appraisalForm);
		// }
	}

	public SupportTickets cloneMethod(SupportTickets supportTickets) {
		SupportTickets oldTicket = new SupportTickets();
		try {

			oldTicket = (SupportTickets) supportTickets.clone();

		} catch (CloneNotSupportedException ex) {
			java.util.logging.Logger.getLogger(SupportManagementServiceImpl.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return oldTicket;
	}

	public AppraisalForm cloneMethodForAppraisal(AppraisalForm appraisalForm) {
		AppraisalForm oldAppraisalForm = new AppraisalForm();
		try {

			oldAppraisalForm = (AppraisalForm) appraisalForm.clone();

		} catch (CloneNotSupportedException ex) {
			java.util.logging.Logger.getLogger(SupportManagementServiceImpl.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return oldAppraisalForm;
	}

	@Override
	public List<EmpDepartmentDTO> getDepartmentNameList() {

		return ticketsCategoryBuilder.getDepartmentList(supportManagementDAOImpl.getDepartmentNameList());
	}

	@Override
	public List<EmpDepartmentDTO> getDepartmentListForRaise() {
		return ticketsCategoryBuilder.getDepartmentList(supportManagementDAOImpl.getDepartmentListForRaise());
	}

	@Override
	public SupportTicketsDTO getTicket(Long ticketId) {

		Map<String, Object> map = securityUtils.getLoggedEmployeeDetailsSecurityContextHolder();

		Employee employee = (Employee) map.get("employee");

		SupportTickets supportTickets = dao.findBy(SupportTickets.class, ticketId);

		Permission totalListPermission = dao.checkForPermission("Ticket Approvals", employee);

		if (employee.getEmployeeId().equals(supportTickets.getCreatedBy())) {
			return supportTicketsBuilder.convertEntityToDTO(supportTickets);
		} else if (totalListPermission.getView()) {
			return supportTicketsBuilder.convertEntityToDTO(supportTickets);
		} else {
			return null;
		}

	}

	@Override
	public List<TrackerDto> addTracker(TrackerDto dto) {
		Tracker tracker = supportTicketsBuilder.trackerDtoToEntity(dto);
		supportManagementDAOImpl.save(tracker);
		List<TrackerDto> trackerDtos = supportTicketsBuilder
				.trackerEntityToDto(supportManagementDAOImpl.getAllTracker());
		return trackerDtos;
	}

	@Override
	public List<TrackerDto> deleteTracker(Long id) {
		Integer size = supportManagementDAOImpl.trackerInUse(id);
		if (size < 1) {
			Tracker tracker = supportManagementDAOImpl.findBy(Tracker.class, id);
			supportManagementDAOImpl.delete(tracker);
			List<TrackerDto> trackerDtos = supportTicketsBuilder
					.trackerEntityToDto(supportManagementDAOImpl.getAllTracker());
			return trackerDtos;
		} else {
			throw new EntityInUseException();
		}

	}

	@Override
	public List<TrackerDto> getAllTracker() {
		List<TrackerDto> trackerDtos = supportTicketsBuilder
				.trackerEntityToDto(supportManagementDAOImpl.getAllTracker());
		return trackerDtos;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ByteArrayOutputStream exportRaisedTickets(String multiSearch) throws IOException {

		Map<String, Object> map = this.searchTicketData(null, null, multiSearch);

		List<SupportTicketsDTO> dtos = (List<SupportTicketsDTO>) map.get("list");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		int rowIndex = 1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		CellStyle style = workbook.createCellStyle();

		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);

		Cell cell0 = row1.createCell(0);
		cell0.setCellValue("Ticket No");
		cell0.setCellStyle(style);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Employee Name");
		cell1.setCellStyle(style);

		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Department Name");
		cell2.setCellStyle(style);

		Cell cell3 = row1.createCell(3);
		cell3.setCellValue("Category Name");
		cell3.setCellStyle(style);

		Cell cell4 = row1.createCell(4);
		cell4.setCellValue("Sub-Category Name");
		cell4.setCellStyle(style);

		Cell cell5 = row1.createCell(5);
		cell5.setCellValue("Subject");
		cell5.setCellStyle(style);

		Cell cell6 = row1.createCell(6);
		cell6.setCellValue("Discription");
		cell6.setCellStyle(style);

		Cell cell7 = row1.createCell(7);
		cell7.setCellValue("Priority");
		cell7.setCellStyle(style);

		Cell cell8 = row1.createCell(8);
		cell8.setCellValue("Actual Time");
		cell8.setCellStyle(style);

		Cell cell9 = row1.createCell(9);
		cell9.setCellValue("Estimated Time");
		cell9.setCellStyle(style);

		Cell cell10 = row1.createCell(10);
		cell10.setCellValue("Assignee Name");
		cell10.setCellStyle(style);

		Cell cell11 = row1.createCell(11);
		cell11.setCellValue("Start Date");
		cell11.setCellStyle(style);

		Cell cell12 = row1.createCell(12);
		cell12.setCellValue("Due Date");
		cell12.setCellStyle(style);

		Cell cell13 = row1.createCell(13);
		cell13.setCellValue("Ticket Status");
		cell13.setCellStyle(style);

		for (SupportTicketsDTO dto : dtos) {

			Row row = sheet.createRow(rowIndex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(dto.getId());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(dto.getEmployeeName());

			Cell cel2 = row.createCell(2);
			cel2.setCellValue(dto.getDepartmentName());

			Cell cel3 = row.createCell(3);
			cel3.setCellValue(dto.getCategoryName());

			Cell cel4 = row.createCell(4);
			cel4.setCellValue(dto.getSubCategoryName());

			Cell cel5 = row.createCell(5);
			cel5.setCellValue(dto.getSubject());

			Cell cel6 = row.createCell(6);
			cel6.setCellValue(dto.getDescription() != null ? dto.getDescription().replaceAll("\\<[^>]*>", "") : "N/A");

			Cell cel7 = row.createCell(7);
			cel7.setCellValue(dto.getPriority());

			Cell cel8 = row.createCell(8);
			cel8.setCellValue(dto.getActualTime());

			Cell cel9 = row.createCell(9);
			cel9.setCellValue(dto.getEstimatedTime());

			Cell cel10 = row.createCell(10);
			cel10.setCellValue(dto.getAssigneeName() != null ? dto.getAssigneeName() : "N/A");

			Cell cel11 = row.createCell(11);
			cel11.setCellValue(dto.getStartDate());

			Cell cel12 = row.createCell(12);
			cel12.setCellValue(dto.getEndDate() != null ? dto.getEndDate() : "N/A");

			Cell cel13 = row.createCell(13);
			cel13.setCellValue(dto.getStatus());

			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			// sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
			sheet.autoSizeColumn(9);
			sheet.autoSizeColumn(10);
			sheet.autoSizeColumn(11);
			sheet.autoSizeColumn(12);
			sheet.autoSizeColumn(13);

		}

		workbook.write(bos);
		bos.flush();
		bos.close();
		// for exporting code end here
		return bos;

	}

	@SuppressWarnings("unchecked")
	@Override
	public ByteArrayOutputStream exportTicketApprovalList(Long empId, String ticketStatus, Long categoryId,
			Long subCategoryId, Integer startIndex, Integer endIndex, String progressStatus, String multiSearch,
			String searchByEmpName, String searchByAssigneeName, String from, String to, String dateSelection,
			HttpServletResponse response, Long departmentId, Long trackerID) throws IOException {

		Map<String, Object> map = this.getAllTicketsForApproval(ticketStatus, categoryId, subCategoryId, null, null,
				progressStatus, multiSearch, searchByEmpName, searchByAssigneeName, from, to, dateSelection,
				departmentId, trackerID);

		List<SupportTicketsDTO> supportTicket = (List<SupportTicketsDTO>) map.get("list");

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		int rowIndex = 1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		CellStyle style = workbook.createCellStyle();

		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);

		Cell cell0 = row1.createCell(0);
		cell0.setCellValue("Ticket No");
		cell0.setCellStyle(style);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Employee Name");
		cell1.setCellStyle(style);

		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Subject");
		cell2.setCellStyle(style);

		Cell cell3 = row1.createCell(3);
		cell3.setCellValue("Tracker");
		cell3.setCellStyle(style);

		Cell cell4 = row1.createCell(4);
		cell4.setCellValue("Category");
		cell4.setCellStyle(style);

		Cell cell5 = row1.createCell(5);
		cell5.setCellValue("Sub-Category");
		cell5.setCellStyle(style);

		Cell cell6 = row1.createCell(6);
		cell6.setCellValue("Description");
		cell6.setCellStyle(style);

		Cell cell7 = row1.createCell(7);
		cell7.setCellValue("Priority");
		cell7.setCellStyle(style);

		Cell cell8 = row1.createCell(8);
		cell8.setCellValue("Start Date");
		cell8.setCellStyle(style);

		Cell cell9 = row1.createCell(9);
		cell9.setCellValue("Due Date");
		cell9.setCellStyle(style);

		Cell cell10 = row1.createCell(10);
		cell10.setCellValue("Created Date & Time(hh.mm)");
		cell10.setCellStyle(style);

		Cell cell11 = row1.createCell(11);
		cell11.setCellValue("Assignee");
		cell11.setCellStyle(style);

		Cell cell12 = row1.createCell(12);
		cell12.setCellValue("Spent Time(hh.mm)");
		cell12.setCellStyle(style);

		Cell cell13 = row1.createCell(13);
		cell13.setCellValue("Approved By");
		cell13.setCellStyle(style);

		Cell cell14 = row1.createCell(14);
		cell14.setCellValue("Approval Status");
		cell14.setCellStyle(style);

		Cell cell15 = row1.createCell(15);
		cell15.setCellValue("Ticket Status");
		cell15.setCellStyle(style);

		for (SupportTicketsDTO dto : supportTicket) {

			Row row = sheet.createRow(rowIndex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(dto.getId());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(dto.getEmployeeName());

			Cell cel2 = row.createCell(2);
			cel2.setCellValue(dto.getSubject());

			Cell cel3 = row.createCell(3);
			cel3.setCellValue(dto.getTrackerName());

			Cell cel4 = row.createCell(4);
			cel4.setCellValue(dto.getCategoryName());

			Cell cel5 = row.createCell(5);
			cel5.setCellValue(dto.getSubCategoryName());

			Cell cel6 = row.createCell(6);
			cel6.setCellValue(dto.getDescription() != null ? dto.getDescription().replaceAll("\\<[^>]*>", "") : "N/A");

			Cell cel7 = row.createCell(7);
			cel7.setCellValue(dto.getPriority());

			Cell cel8 = row.createCell(8);
			cel8.setCellValue(dto.getStartDate());

			Cell cel9 = row.createCell(9);
			cel9.setCellValue(dto.getEndDate() != null ? dto.getEndDate() : "N/A");

			Cell cel10 = row.createCell(10);
			cel10.setCellValue(dto.getCreatedDate() != null ? dto.getCreatedDate() : "N/A");

			Cell cel11 = row.createCell(11);
			cel11.setCellValue(dto.getAssigneeName() != null ? dto.getAssigneeName() : "N/A");

			Cell cel12 = row.createCell(12);
			cel12.setCellValue(dto.getActualTime());

			Cell cel13 = row.createCell(13);
			cel13.setCellValue(dto.getApprovedBy() != null ? dto.getApprovedBy() : "N/A");

			Cell cel14 = row.createCell(14);
			cel14.setCellValue(dto.getApprovalStatus());

			Cell cel15 = row.createCell(15);
			cel15.setCellValue(dto.getStatus());

			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
			sheet.autoSizeColumn(8);
			sheet.autoSizeColumn(9);
			sheet.autoSizeColumn(10);
			sheet.autoSizeColumn(11);
			sheet.autoSizeColumn(12);
			sheet.autoSizeColumn(13);
			sheet.autoSizeColumn(14);
			sheet.autoSizeColumn(15);
		}

		workbook.write(bos);
		bos.flush();
		bos.close();
		return bos;

	}

	// To get the tickets report
	@Override
	public Map<String, Object> getTicketsReport(String ticketStatus, String from, String to, String dateSelection,
			Long departmentId) {

		Date fromDate = null;
		Date toDate = null;

		if (dateSelection.equalsIgnoreCase("Custom")) {
			try {
				fromDate = DateParser.toDate(from);
				toDate = DateParser.toDate(to);
			} catch (ParseException ex) {
				java.util.logging.Logger.getLogger(AssetManagementServiceImpl.class.getName()).log(Level.SEVERE, null,
						ex);
			}
		} else {
			Map<String, Date> dateMap = dao.getCustomDates(dateSelection);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}

		DateRange dateRange = new DateRange(fromDate, toDate);

		Map<String, Object> map = securityUtils.getLoggedEmployeeDetailsSecurityContextHolder();
		Map<String, Object> finalMap = new HashMap<String, Object>();
		Employee loggedInEmployee = (Employee) map.get("employee");

		Permission hierarchyPermission = dao.checkForPermission("Hierarchy Ticket Report", loggedInEmployee);
		Permission totalListPermission = dao.checkForPermission("Report", loggedInEmployee);
		Permission departmentPermission = dao.checkForPermission("Department Wise Ticket Report", loggedInEmployee);

		List<TicketReportDTO> ticketReportDTO = null;

		// For Hierarchy wise ticket
		if (hierarchyPermission.getView() && totalListPermission.getView() && !departmentPermission.getView()) {

			finalMap = supportManagementDAOImpl.getManagerTicketsReport(loggedInEmployee.getEmployeeId().toString(),
					dateRange, departmentId);
		} else if (totalListPermission.getView() && !hierarchyPermission.getView() && !departmentPermission.getView()) {
			// For Admin
			finalMap = supportManagementDAOImpl.getTicketsReportForAdmin(dateRange, departmentId);
		} else if (totalListPermission.getView() && !hierarchyPermission.getView() && departmentPermission.getView()) {
			// For department wise based tickets
			finalMap = supportManagementDAOImpl.getDepartmentWiseTicketsReport(loggedInEmployee.getDepartmentName(),
					dateRange, departmentId);
		}

		// For Department Wise manager

		else if (totalListPermission.getView() && hierarchyPermission.getView() && departmentPermission.getView()) {

			// department ticket if
			// (ticketStatus.equalsIgnoreCase("Department")) {
			finalMap = supportManagementDAOImpl.getDepartmentWiseTicketsReport(loggedInEmployee.getDepartmentName(),
					dateRange, departmentId);
		} else {
			// As a manager ticket finalMap =
			supportManagementDAOImpl.getManagerTicketsReport(loggedInEmployee.getEmployeeId().toString(), dateRange,
					departmentId);
		}

		List<SupportTickets> list = (List<SupportTickets>) finalMap.get("list");
		// Integer noOfrecords = (Integer) finalMap.get("size");
		ticketReportDTO = supportTicketsBuilder.toDTOListForReport(list);
		Map<String, Object> reportMap = new HashMap<String, Object>();
		reportMap.put("list", ticketReportDTO);
		reportMap.put("size", ticketReportDTO.size());

		return reportMap;
	}

	// To get the tickets list according to status
	@Override
	public Map<String, Object> getTicketsDetails(String filter, Long trackerId, Integer startIndex, Integer endIndex,
			Long categoryId, Long subCategoryId, String from, String to, String dateSelection, Long departmentId,
			String ticketStatus) {

		Date fromDate = null;
		Date toDate = null;

		if (dateSelection.equalsIgnoreCase("Custom")) {
			try {
				fromDate = DateParser.toDate(from);
				toDate = DateParser.toDate(to);
			} catch (ParseException ex) {
				java.util.logging.Logger.getLogger(AssetManagementServiceImpl.class.getName()).log(Level.SEVERE, null,
						ex);
			}
		} else {
			Map<String, Date> dateMap = dao.getCustomDates(dateSelection);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}

		DateRange dateRange = new DateRange(fromDate, toDate);

		Map<String, Object> map = securityUtils.getLoggedEmployeeDetailsSecurityContextHolder();
		Map<String, Object> finalMap = new HashMap<String, Object>();
		Employee loggedInEmployee = (Employee) map.get("employee");

		Permission hierarchyPermission = dao.checkForPermission("Hierarchy Ticket Report", loggedInEmployee);
		Permission totalListPermission = dao.checkForPermission("Report", loggedInEmployee);
		Permission departmentPermission = dao.checkForPermission("Department Wise Ticket Report", loggedInEmployee);

		List<SupportTicketsDTO> supportTicketsDTOs = null;

		if (hierarchyPermission.getView() && totalListPermission.getView() && !departmentPermission.getView()) {

			// For Manager

			finalMap = supportManagementDAOImpl.getManagerTicketsDetails(loggedInEmployee.getEmployeeId().toString(),
					filter, trackerId, startIndex, endIndex, categoryId, subCategoryId, dateRange, departmentId);

		} else if (totalListPermission.getView() && !hierarchyPermission.getView() && !departmentPermission.getView()) {
			// For Admin

			finalMap = supportManagementDAOImpl.getTotalTicketsDetailsForAdmin(filter, trackerId, startIndex, endIndex,
					categoryId, subCategoryId, dateRange, departmentId);
		} else if (totalListPermission.getView() && !hierarchyPermission.getView() && departmentPermission.getView()) {

			finalMap = supportManagementDAOImpl.getDepartmentWiseTicketsDetails(loggedInEmployee.getDepartmentName(),
					filter, trackerId, startIndex, endIndex, categoryId, subCategoryId, dateRange, departmentId);
		}
		// For Department Wise manager
		else if (totalListPermission.getView() && hierarchyPermission.getView() && departmentPermission.getView()) {

			// department ticket
			if (ticketStatus.equalsIgnoreCase("Department")) {

				finalMap = supportManagementDAOImpl.getDepartmentWiseTicketsDetails(
						loggedInEmployee.getDepartmentName(), filter, trackerId, startIndex, endIndex, categoryId,
						subCategoryId, dateRange, departmentId);
			} else {
				// As a manager ticket
				finalMap = supportManagementDAOImpl.getManagerTicketsDetails(

						loggedInEmployee.getEmployeeId().toString(), filter, trackerId, startIndex, endIndex,
						categoryId, subCategoryId, dateRange, departmentId);
			}

		}

		List<SupportTickets> list = (List<SupportTickets>) finalMap.get("list");
		Integer noOfrecords = (Integer) finalMap.get("size");
		supportTicketsDTOs = supportTicketsBuilder.toDTOList(list);
		Map<String, Object> ticketMap = new HashMap<String, Object>();
		ticketMap.put("list", supportTicketsDTOs);
		ticketMap.put("size", noOfrecords);

		return ticketMap;

	}

	@Override
	public ByteArrayOutputStream exportReportList(Integer startIndex, Integer endIndex, String from, String to,
			String dateSelection, Long departmentId) throws IOException {

		Map<String, Object> map = this.getAllTicketsForApproval("", null, null, null, null, "", "", "", "", from, to,
				dateSelection, departmentId, null);

		List<SupportTicketsDTO> supportTicket = (List<SupportTicketsDTO>) map.get("list");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		int rowindex = 1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		CellStyle style = workbook.createCellStyle();

		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);

		Cell cell0 = row1.createCell(0);
		cell0.setCellValue("Department");
		cell0.setCellStyle(style);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Tracker");
		cell1.setCellStyle(style);

		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Category");
		cell2.setCellStyle(style);

		Cell cell3 = row1.createCell(3);
		cell3.setCellValue("Subcategory");
		cell3.setCellStyle(style);

		Cell cell4 = row1.createCell(4);
		cell4.setCellValue("subject");
		cell4.setCellStyle(style);

		Cell cell5 = row1.createCell(5);
		cell5.setCellValue("status");
		cell5.setCellStyle(style);

		for (SupportTicketsDTO ticket : supportTicket) {

			Row row = sheet.createRow(rowindex++);
			Cell cel0 = row.createCell(0);
			cel0.setCellValue(ticket.getDepartmentName());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(ticket.getTrackerName());

			Cell cel2 = row.createCell(2);
			cel2.setCellValue(ticket.getCategoryName());

			Cell cel3 = row.createCell(3);
			cel3.setCellValue(ticket.getSubCategoryName());
			Cell cel4 = row.createCell(4);
			cel4.setCellValue(ticket.getSubject());

			Cell cel5 = row.createCell(5);
			cel5.setCellValue(ticket.getStatus());

			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);

		}

		workbook.write(bos);
		bos.flush();
		bos.close();
		return bos;

	}
}
