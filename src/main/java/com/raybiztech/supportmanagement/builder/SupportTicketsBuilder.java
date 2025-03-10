package com.raybiztech.supportmanagement.builder;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.supportmanagement.business.SupportTickets;
import com.raybiztech.supportmanagement.business.SupportTicketsWatchers;
import com.raybiztech.supportmanagement.business.TicketsSubCategory;
import com.raybiztech.supportmanagement.business.Tracker;
import com.raybiztech.supportmanagement.dto.SupportTicketsDTO;
import com.raybiztech.supportmanagement.dto.TicketReportDTO;
import com.raybiztech.supportmanagement.dto.TrackerDto;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.SpinnerListModel;

import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("supportTicketsBuilder")
public class SupportTicketsBuilder {
	@Autowired
	DAO dao;
	@Autowired
	SecurityUtils securityUtils;

	org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(SupportTicketsBuilder.class);

	public SupportTickets convertDTOToEntity(SupportTicketsDTO ticketsDTO)

	{
		Long logedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		SupportTickets supportTickets = null;
		if (ticketsDTO != null) {
			supportTickets = new SupportTickets();
			supportTickets.setSubject(ticketsDTO.getSubject());
			if (ticketsDTO.getActualTime() == null) {
				supportTickets.setActualTime("0.00");
			}
			if (ticketsDTO.getDescription() != null) {
				if (ticketsDTO.getDescription().equalsIgnoreCase("<br/>")) {
					supportTickets.setDescription(null);

				}

				else {
					supportTickets.setDescription(ticketsDTO.getDescription());
				}
			}
			// supportTickets.setActualTime(ticketsDTO.getActualTime());
			// supportTickets.setAssignee(dao.findBy(Employee.class,
			// ticketsDTO.getAssigneeId()));
			// supportTickets.setPercentageDone(ticketsDTO.getPercentageDone());
			supportTickets.setStatus("New");
			supportTickets.setPriority(ticketsDTO.getPriority());
			TicketsSubCategory subCategory = dao.findBy(TicketsSubCategory.class, ticketsDTO.getSubCategoryId());
			supportTickets.setTicketsSubCategory(subCategory);
			try {
				if (ticketsDTO.getStartDate() == null || ticketsDTO.getStartDate().isEmpty()) {
					supportTickets.setStartDate(DateParser.toDate(new Second().toString("dd/MM/yyyy")));

				} else {
					supportTickets.setStartDate(DateParser.toDate(ticketsDTO.getStartDate()));
				}
				supportTickets.setEndDate(DateParser.toDate(ticketsDTO.getEndDate()));
			} catch (ParseException ex) {
				Logger.getLogger(SupportTicketsBuilder.class.getName()).log(Level.SEVERE, null, ex);
			}
			Tracker tracker = dao.findBy(Tracker.class, ticketsDTO.getTracker());
			supportTickets.setTracker(tracker);

			if (subCategory.getWorkFlow() && tracker.getPermission() == true) {
				Employee employee = dao.findBy(Employee.class, logedInEmpId);
				if (subCategory.getTicketsCategory().getCategoryName().equalsIgnoreCase("Food")
						&& employee.getManager().getRole().equalsIgnoreCase("admin")) {
					supportTickets.setApprovalStatus("Approved");
					supportTickets.setManagesList(employee.getManager().getEmployeeId().toString());
				} else {
					supportTickets.setApprovalStatus("Pending Approval");
					supportTickets.setManagesList(employee.getManager().getEmployeeId().toString());
				}
			} else
				supportTickets.setApprovalStatus("Approved");
			supportTickets.setCreatedBy(logedInEmpId);
			supportTickets.setCreatedDate(new Second());
			// List<Long> ids=ticketsDTO.getWatcherIds();

			// supportTickets.setTicketsWatchers(ticketWatchers(ticketsDTO.getWatcherIds()));
			String empId = null;
			for (Long watcherId : ticketsDTO.getWatcherIds()) {
				if (empId != null)
					empId = empId + "_" + watcherId.toString();
				else
					empId = watcherId.toString();

			}
			supportTickets.setTicketWatchers(empId);
			try {
				supportTickets.setAccessStartDate(DateParser.toDate(ticketsDTO.getAccessStartDate()));
				supportTickets.setAccessEndDate(DateParser.toDate(ticketsDTO.getAccessEndDate()));
			} catch (ParseException ex) {
				Logger.getLogger(SupportTicketsBuilder.class.getName()).log(Level.SEVERE, null, ex);
			}
		}
		return supportTickets;
	}

	public Set<SupportTicketsWatchers> ticketWatchers(List<Long> watcherIds) {
		Set<SupportTicketsWatchers> setWatchers = new HashSet<SupportTicketsWatchers>();
		SupportTicketsWatchers watchers = null;
		if (watcherIds != null) {
			for (Long ids : watcherIds) {
				watchers = new SupportTicketsWatchers();
				watchers.setEmployee(dao.findBy(Employee.class, ids));
				setWatchers.add(watchers);
			}

		}
		return setWatchers;
	}

	public SupportTicketsDTO convertEntityToDTO(SupportTickets supportTickets) {
		SupportTicketsDTO ticketsDTO = null;
		Employee logedInEmpId = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");

		Permission hierarchyPermission = dao.checkForPermission("Hierarchy Ticket Approvals", logedInEmpId);
		Permission totalListPermission = dao.checkForPermission("Ticket Approvals", logedInEmpId);
		Permission departmentPermission = dao.checkForPermission("Department Wise Ticket Approvals", logedInEmpId);
		if (supportTickets != null) {
			ticketsDTO = new SupportTicketsDTO();
			ticketsDTO.setId(supportTickets.getId());
			ticketsDTO.setActualTime(supportTickets.getActualTime());
			ticketsDTO.setCategoryName(supportTickets.getTicketsSubCategory().getTicketsCategory().getCategoryName());
			ticketsDTO.setCategoryId(supportTickets.getTicketsSubCategory().getTicketsCategory().getId());
			ticketsDTO.setSubCategoryName(supportTickets.getTicketsSubCategory().getSubCategoryName());
			ticketsDTO.setSubCategoryId(supportTickets.getTicketsSubCategory().getId());
			ticketsDTO.setDepartmentName(
					supportTickets.getTicketsSubCategory().getTicketsCategory().getEmpDepartment().getDepartmentName());
			ticketsDTO.setDepartmentId(
					supportTickets.getTicketsSubCategory().getTicketsCategory().getEmpDepartment().getDepartmentId());
			ticketsDTO.setDescription(supportTickets.getDescription());
			ticketsDTO.setPercentageDone(supportTickets.getPercentageDone());
			ticketsDTO.setPriority(supportTickets.getPriority());
			ticketsDTO.setSubject(supportTickets.getSubject());
			ticketsDTO.setStatus(supportTickets.getStatus());
			if (supportTickets.getTracker() != null) {
				ticketsDTO.setTracker(
						/* supportTickets.getTracker()!=null? */supportTickets.getTracker().getId()/* :null */);
				ticketsDTO.setTrackerName(
						supportTickets.getTracker() != null ? supportTickets.getTracker().getName() : "N/A");
				if (supportTickets.getTracker().getPermission() == true) {
					if (supportTickets.getTicketsSubCategory().getWorkFlow() == true) {
						ticketsDTO.setApprovalStatus(supportTickets.getApprovalStatus());
					} else {
						ticketsDTO.setApprovalStatus(supportTickets.getApprovalStatus().equalsIgnoreCase("Cancelled")
								? supportTickets.getApprovalStatus()
								: "N/A");
						// ticketsDTO.setApprovalStatus("N/A");
					}
				} else {
					ticketsDTO.setApprovalStatus(supportTickets.getApprovalStatus().equalsIgnoreCase("Cancelled")
							? supportTickets.getApprovalStatus()
							: "N/A");
					// ticketsDTO.setApprovalStatus("N/A");
				}
			} else {
				if (supportTickets.getTicketsSubCategory().getWorkFlow() == true) {
					ticketsDTO.setApprovalStatus(supportTickets.getApprovalStatus());
				} else {

					ticketsDTO.setApprovalStatus(supportTickets.getApprovalStatus().equalsIgnoreCase("Cancelled")
							? supportTickets.getApprovalStatus()
							: "N/A");
				}
			}
			Employee createdEmployee = dao.findBy(Employee.class, supportTickets.getCreatedBy());
			ticketsDTO.setEmployeeName(createdEmployee.getFullName());
			ticketsDTO.setStartDate(
					supportTickets.getStartDate() != null ? supportTickets.getStartDate().toString("dd/MM/yyyy")
							: null);
			ticketsDTO.setEndDate(
					supportTickets.getEndDate() != null ? supportTickets.getEndDate().toString("dd/MM/yyyy") : null);
			Employee authorId = dao.findBy(Employee.class, supportTickets.getCreatedBy());
			ticketsDTO.setAuthorName(authorId.getFullName());
			ticketsDTO.setAssigneeName(
					supportTickets.getAssignee() != null ? supportTickets.getAssignee().getFullName() : null);
			ticketsDTO.setAssigneeId(
					supportTickets.getAssignee() != null ? supportTickets.getAssignee().getEmployeeId() : null);
			// ticketsDTO.setApprovalStatus(supportTickets.getApprovalStatus());
			String originalPath = supportTickets.getDocumentsPath();
			if (originalPath != null) {
				String splitedPath = originalPath.substring(originalPath.lastIndexOf("/") + 1);
				ticketsDTO.setFilePath(splitedPath);
			}
			ticketsDTO.setEstimatedTime(supportTickets.getTicketsSubCategory().getEstimatedTime());
			List<String> empNames = new ArrayList<String>();
			if (supportTickets.getTicketWatchers() != null) {
				String[] empIds = supportTickets.getTicketWatchers().split("_");
				// if(empIds != null){
				for (String empId : empIds) {
					Long id = Long.parseLong(empId);
					Employee employee = dao.findBy(Employee.class, id);
					empNames.add(employee.getFullName());
				}
			}
			ticketsDTO.setWatcherNames(empNames);
			ticketsDTO.setAccessStartDate(supportTickets.getAccessStartDate() != null
					? supportTickets.getAccessStartDate().toString("dd/MM/yyyy")
					: null);
			ticketsDTO.setAccessEndDate(
					supportTickets.getAccessEndDate() != null ? supportTickets.getAccessEndDate().toString("dd/MM/yyyy")
							: null);
			ticketsDTO.setCreatedDate(supportTickets.getCreatedDate() != null
					? supportTickets.getCreatedDate().toString("dd/MM/yyyy (hh:mm)")
					: null);
			if (supportTickets.getTracker().getName().equalsIgnoreCase("New Request")) {
				ticketsDTO.setApprovedBy(authorId.getManager().getFullName());
			} else {
				ticketsDTO.setApprovedBy("N/A");
			}

			if (supportTickets.getManagesList() != null) {
				if (supportTickets.getManagesList().contains(","))
					ticketsDTO.setDisableCancel(true);
				else
					ticketsDTO.setDisableCancel(false);

				String[] empIds = supportTickets.getManagesList().split(",");
				if (empIds[empIds.length - 1].equalsIgnoreCase(logedInEmpId.getEmployeeId().toString())
						|| (totalListPermission.getView() && !hierarchyPermission.getView()
								&& !departmentPermission.getView())) {
					ticketsDTO.setDisableApprove(true);
				} else {
					ticketsDTO.setDisableApprove(false);
				}
			} else {
				ticketsDTO.setDisableCancel(false);
				if (totalListPermission.getView() && !hierarchyPermission.getView()
						&& !departmentPermission.getView()) {

					ticketsDTO.setDisableApprove(true);
				} else {

					ticketsDTO.setDisableApprove(false);
				}
			}

		}
		return ticketsDTO;
	}

	public List<SupportTicketsDTO> toDTOList(List<SupportTickets> supportTicketses) {
		List<SupportTicketsDTO> dTOList = null;
		if (supportTicketses != null) {
			dTOList = new ArrayList<SupportTicketsDTO>();
			for (SupportTickets tickets : supportTicketses) {

				dTOList.add(convertEntityToDTO(tickets));
			}

		}
		return dTOList;
	}

	public SupportTickets editDTOToEntity(SupportTicketsDTO ticketsDTO, SupportTickets oldticket) {
		Long logedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		SupportTickets tickets = null;
		if (ticketsDTO != null) {
			tickets = dao.findBy(SupportTickets.class, ticketsDTO.getId());
			if (ticketsDTO.getDescription() != null) {
				if (ticketsDTO.getDescription().equalsIgnoreCase("<br/>")) {
					tickets.setDescription(null);
				} else {
					tickets.setDescription(ticketsDTO.getDescription());
				}
			}
			tickets.setSubject(ticketsDTO.getSubject());
			tickets.setPriority(ticketsDTO.getPriority());
			TicketsSubCategory subCategory = dao.findBy(TicketsSubCategory.class, ticketsDTO.getSubCategoryId());
			tickets.setTicketsSubCategory(subCategory);
			tickets.setStatus(ticketsDTO.getStatus());
			try {
				tickets.setStartDate(
						ticketsDTO.getStartDate() != null ? DateParser.toDate(ticketsDTO.getStartDate()) : null);
				tickets.setEndDate(ticketsDTO.getEndDate() != null ? DateParser.toDate(ticketsDTO.getEndDate()) : null);
			} catch (ParseException ex) {
				Logger.getLogger(SupportTicketsBuilder.class.getName()).log(Level.SEVERE, null, ex);
			}
			if (ticketsDTO.getActualTime().equalsIgnoreCase("0.00") || ticketsDTO.getActualTime().equalsIgnoreCase("0")
					|| ticketsDTO.getActualTime().equalsIgnoreCase("0.0")) {
				tickets.setActualTime("0.00");
			} else {
				tickets.setActualTime(ticketsDTO.getActualTime());
			}
			tickets.setPercentageDone(ticketsDTO.getPercentageDone());

			if (ticketsDTO.getAssigneeName() != null) {
				Long assigneeId = Long.parseLong(ticketsDTO.getAssigneeName());
				Employee assignee = dao.findBy(Employee.class, assigneeId);
				tickets.setAssignee(assignee);
			} else if (ticketsDTO.getAssigneeId() != null) {
				Long assigneeId = ticketsDTO.getAssigneeId();
				Employee assignee = dao.findBy(Employee.class, assigneeId);
				tickets.setAssignee(assignee);

			} else {
				tickets.setAssignee(null);
			}

			tickets.setUpdatedBy(logedInEmpId);
			tickets.setUpdatedDate(new Second());
			try {
				tickets.setAccessStartDate(
						ticketsDTO.getAccessStartDate() != null ? DateParser.toDate(ticketsDTO.getAccessStartDate())
								: null);
				tickets.setAccessEndDate(
						ticketsDTO.getAccessEndDate() != null ? DateParser.toDate(ticketsDTO.getAccessEndDate())
								: null);
			} catch (Exception e) {
				Logger.getLogger(SupportTicketsBuilder.class.getName()).log(Level.SEVERE, null, e);
			}

		}
		return tickets;
	}

	public Tracker trackerDtoToEntity(TrackerDto dto) {
		Tracker tracker = new Tracker();
		if (dto != null) {
			tracker.setName(dto.getName());
			tracker.setPermission(dto.getPermission() != null ? dto.getPermission() : false);
			tracker.setCreatedBy(securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
			tracker.setCreatedDate(new Second());
		}
		return tracker;
	}

	public List<TrackerDto> trackerEntityToDto(List<Tracker> list) {
		List<TrackerDto> trackerDtos = new ArrayList<TrackerDto>();
		if (list != null) {
			for (Tracker tracker : list) {
				TrackerDto dto = new TrackerDto();
				dto = trackerToDto(tracker);
				trackerDtos.add(dto);
			}
		}
		return trackerDtos;
	}

	public TrackerDto trackerToDto(Tracker tracker) {
		TrackerDto dto = new TrackerDto();
		if (tracker != null) {
			dto.setId(tracker.getId());
			dto.setName(tracker.getName());
			dto.setPermission(tracker.getPermission());
		}
		return dto;
	}

	public List<TicketReportDTO> toDTOListForReport(List<SupportTickets> supportTicketses) {
		List<TicketReportDTO> reportDTOs = new LinkedList<TicketReportDTO>();

		if (supportTicketses != null) {

			TreeMap<String, HashMap<String, Integer>> map = new TreeMap<String, HashMap<String, Integer>>();
			HashMap<String, Integer> value = new HashMap<String, Integer>();
			for (SupportTickets tickets : supportTicketses) {

				// System.out.println(tickets.getTracker().getName() + " vs " +
				// tickets.getTracker().getName());

				if (tickets.getTracker() != null) {

					String key1 = tickets.getTracker().getName() + "_"
							+ tickets.getTicketsSubCategory().getTicketsCategory().getCategoryName() + "_"
							+ tickets.getTicketsSubCategory().getSubCategoryName() + "_" + tickets.getTracker().getId()
							+ "_" + tickets.getTicketsSubCategory().getTicketsCategory().getId() + "_"
							+ tickets.getTicketsSubCategory().getId();
					// String key2 = key1;

					if (tickets.getStatus().equalsIgnoreCase("In Progress")
							|| tickets.getStatus().equalsIgnoreCase("New")
							|| tickets.getStatus().equalsIgnoreCase("Feedback")
							|| tickets.getStatus().equalsIgnoreCase("Fixed")) {

						// key2 = "_pending";
						// logger.warn(" key1 1 " +key1);

						value.put(key1 + "_pending",
								value.get(key1 + "_pending") != null ? value.get(key1 + "_pending") + 1 : 1);
						map.put(key1, value);

					} else if (tickets.getStatus().equalsIgnoreCase("closed")) {

						// key2 = "_closed";
						// logger.warn(" key1 2" +key1);
						value.put(key1 + "_closed",
								value.get(key1 + "_closed") != null ? value.get(key1 + "_closed") + 1 : 1);
						map.put(key1, value);
					}
				}
			}
			for (Entry<String, HashMap<String, Integer>> entry : map.entrySet()) {
				String[] splitting = entry.getKey().split("_");
				int closed = 0;
				int pending = 0;
				String close = entry.getKey() + "_closed";
				String pendi = entry.getKey() + "_pending";
				for (Entry<String, Integer> e : entry.getValue().entrySet()) {
					if (close.equalsIgnoreCase(e.getKey()))
						closed = e.getValue();
					else if (pendi.equalsIgnoreCase(e.getKey())) {
						pending = e.getValue();
					}
				}

				int total = closed + pending;

				/*
				 * logger.warn(" closed " + closed); logger.warn(" pending " + pending);
				 * logger.warn(" total " + total);
				 */
				TicketReportDTO reportDTO = new TicketReportDTO();

				reportDTO.setNoOfClosedTickets(String.valueOf(closed));
				reportDTO.setNoOfPendingTickets(String.valueOf(pending));
				reportDTO.setNoOfTickets(String.valueOf(total));
				reportDTO.setTrackerName(splitting[0]);
				reportDTO.setCategoryName(splitting[1]);
				reportDTO.setSubCategoryName(splitting[2]);
				reportDTO.setTrackerId(Long.parseLong(splitting[3]));
				reportDTO.setCategoryId(Long.parseLong(splitting[4]));
				reportDTO.setSubCategoryId(Long.parseLong(splitting[5]));
				// logger.warn(" closed " + reportDTO.getNoOfClosedTickets());
				// logger.warn(" pending " + reportDTO.getNoOfPendingTickets());
				// logger.warn(" total " + reportDTO.getNoOfTickets());
				reportDTOs.add(reportDTO);
			}
			/*
			 * for(TicketReportDTO dto:reportDTOs) {
			 * System.out.println(dto.getCategoryName()+" c "+dto.getNoOfClosedTickets()+
			 * " p "+dto.getNoOfPendingTickets()+" t "+dto.getNoOfTickets()); }
			 */
		}

		return reportDTOs;
	}

}
