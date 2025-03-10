/*
 * To change this license header, choose License Headers in Project Properties. 
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.projectmanagement.builder;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.payslip.utility.AES256Encryption;
import com.raybiztech.projectmanagement.business.MilestonePeople;
import com.raybiztech.projectmanagement.dto.MilestonePeopleDTO;
import com.raybiztech.projectmanagement.dto.ReportDTO;
import com.raybiztech.projectmanagement.invoice.business.Invoice;
import com.raybiztech.projectmanagement.invoice.business.LineItem;
import com.raybiztech.recruitment.utils.DateParser;

/**
 *
 * @author sravani
 */
@Component("milstonePeopleBuilder")
public class MilestonePeopleBuilder {

	@Autowired
	DAO dao;

	org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(MilestonePeopleBuilder.class);

	public MilestonePeople convertToEntity(MilestonePeopleDTO milestonePeopleDTO) {
		MilestonePeople milestonePeople = null;

		if (milestonePeopleDTO != null) {
			milestonePeople = new MilestonePeople();
			Employee employee = dao.findBy(Employee.class,
					milestonePeopleDTO.getEmployeeId());
			milestonePeople.setEmployee(dao.findBy(Employee.class,
					employee.getEmployeeId()));
			// milestonePeople.setMilestone(dao.findBy(Milestone.class,
			// milestonePeopleDTO.getMilestoneId()));
			milestonePeople.setComments(milestonePeopleDTO.getComments());
			milestonePeople.setCount(milestonePeopleDTO.getCount());
			milestonePeople.setIsBillable(milestonePeopleDTO.getIsBillable());
			try {
				milestonePeople.setStartDate(DateParser
						.toDate(milestonePeopleDTO.getFromDate()));
				milestonePeople.setEndDate(DateParser.toDate(milestonePeopleDTO
						.getEndDate()));
			} catch (ParseException ex) {
				Logger.getLogger(MilestonePeopleBuilder.class.getName()).log(
						Level.SEVERE, null, ex);
			}
			milestonePeople.setMonthWorkingDays((milestonePeopleDTO
					.getMonthWorkingDays() != null) ? milestonePeopleDTO
					.getMonthWorkingDays() : null);
			milestonePeople
					.setHolidays((milestonePeopleDTO.getHolidays() != null) ? milestonePeopleDTO
							.getHolidays() : null);
			milestonePeople
					.setLeaves((milestonePeopleDTO.getLeaves() != null) ? milestonePeopleDTO
							.getLeaves() : null);
			milestonePeople
					.setTotalDays((milestonePeopleDTO.getTotalDays() != null) ? milestonePeopleDTO
							.getTotalDays() : null);

			milestonePeople
					.setHours((milestonePeopleDTO.getHours() != null) ? milestonePeopleDTO
							.getHours() : null);

			milestonePeople
					.setTotalValue((milestonePeopleDTO.getTotalValue() != null) ? milestonePeopleDTO
							.getTotalValue() : null);
		}
		return milestonePeople;
	}

	public MilestonePeopleDTO convertToDto(MilestonePeople milestonePeople) {
		MilestonePeopleDTO milestonePeopleDTO = null;
		if (milestonePeople != null) {
			milestonePeopleDTO = new MilestonePeopleDTO();
			milestonePeopleDTO.setMilestonePeopleId(milestonePeople.getId());
			// milestonePeopleDTO.setMilestoneId(milestonePeople.getMilestone().getId());
			milestonePeopleDTO.setEmployeeId(milestonePeople.getEmployee()
					.getEmployeeId());
			milestonePeopleDTO.setIsBillable(milestonePeople.getIsBillable());
			milestonePeopleDTO.setEndDate(DateParser.toString(milestonePeople
					.getEndDate()));
			milestonePeopleDTO.setFromDate(DateParser.toString(milestonePeople
					.getStartDate()));
			milestonePeopleDTO.setEmployeeDesignation(milestonePeople
					.getEmployee().getDesignation());
			milestonePeopleDTO.setEmployeeName(milestonePeople.getEmployee()
					.getFullName());

			milestonePeopleDTO
					.setFromDate(milestonePeople.getStartDate() != null ? milestonePeople
							.getStartDate().toString("dd/MM/yyyy") : null);
			milestonePeopleDTO
					.setEndDate(milestonePeople.getEndDate() != null ? milestonePeople
							.getEndDate().toString("dd/MM/yyyy") : null);
			milestonePeopleDTO.setComments(milestonePeople.getComments());

			milestonePeopleDTO.setMonthWorkingDays((milestonePeople
					.getMonthWorkingDays() != null) ? milestonePeople
					.getMonthWorkingDays() : null);
			milestonePeopleDTO
					.setHolidays((milestonePeople.getHolidays() != null) ? milestonePeople
							.getHolidays() : null);
			milestonePeopleDTO
					.setLeaves((milestonePeople.getLeaves() != null) ? milestonePeople
							.getLeaves() : null);
			milestonePeopleDTO
					.setTotalDays((milestonePeople.getTotalDays() != null) ? milestonePeople
							.getTotalDays() : null);

			// here we are setting total days to count because we are using
			// count invoice for calculation
			milestonePeopleDTO
					.setCount((milestonePeople.getTotalDays() != null) ? milestonePeople
							.getTotalDays() : null);

			milestonePeopleDTO
					.setHours((milestonePeople.getHours() != null) ? milestonePeople
							.getHours() : null);

			milestonePeopleDTO
					.setTotalValue((milestonePeople.getTotalValue() != null) ? milestonePeople
							.getTotalValue() : null);

			milestonePeopleDTO
					.setDuration((milestonePeopleDTO.getCount() != null) ? "DAYS"
							: null);

			Long totalValue = Long.parseLong(milestonePeople.getTotalValue());
			Long billableDays = totalValue / 8;

			milestonePeopleDTO.setBillableDays(String.valueOf(billableDays));

			/*
			 * if (milestonePeople.getEndDate() != null &&
			 * milestonePeople.getStartDate() != null) {
			 * 
			 * Long diff = milestonePeople.getEndDate().getJavaDate() .getTime()
			 * - milestonePeople.getStartDate().getJavaDate() .getTime();
			 * 
			 * Long days = diff / (1000 * 60 * 60 * 24);
			 * 
			 * milestonePeopleDTO .setCount(milestonePeople.getCount() != null ?
			 * milestonePeople .getCount() : String.valueOf(days + 1));
			 * 
			 * 
			 * }
			 */

		}

		return milestonePeopleDTO;

	}

	public List<MilestonePeople> convertToEntityList(
			List<MilestonePeopleDTO> milestonePeopleDTOs) {

		List<MilestonePeople> milestonePeoples = new ArrayList<MilestonePeople>();
		if (milestonePeopleDTOs != null) {
			for (MilestonePeopleDTO milestonePeopleDTO : milestonePeopleDTOs) {

				milestonePeoples.add(convertToEntity(milestonePeopleDTO));

			}

		}
		return milestonePeoples;

	}

	public List<MilestonePeopleDTO> convertTODTOList(
			List<MilestonePeople> milestonePeoples) {
		List<MilestonePeopleDTO> milestonePeopleDTOs = new ArrayList<MilestonePeopleDTO>();
		if (milestonePeoples != null) {
			for (MilestonePeople milestonePeople : milestonePeoples) {
				if (milestonePeople.getIsBillable())
					milestonePeopleDTOs.add(convertToDto(milestonePeople));
			}

		}
		return milestonePeopleDTOs;
	}

	public List<MilestonePeople> convertReportDtoToList(
			List<ReportDTO> reportDTOs) {
		List<MilestonePeople> milestonePeoples = new ArrayList<MilestonePeople>();
		MilestonePeople milestonePeople = null;
		for (ReportDTO reportDTO : reportDTOs) {

			milestonePeople = new MilestonePeople();
			try {
				milestonePeople.setStartDate(DateParser.toDate(reportDTO
						.getStartDate()));
				milestonePeople.setEndDate(DateParser.toDate(reportDTO
						.getEndDate()));
			} catch (ParseException ex) {
				Logger.getLogger(MilestonePeopleBuilder.class.getName()).log(
						Level.SEVERE, null, ex);
			}
			Employee employee = dao.findBy(Employee.class,
					reportDTO.getEmployeeId());
			milestonePeople.setEmployee(employee);
			milestonePeople.setIsBillable(reportDTO.isBillable());
			milestonePeople.setComments(reportDTO.getComments());
			milestonePeople.setCount(reportDTO.getCount());
			milestonePeople.setRole(reportDTO.getRole());

			milestonePeople.setMonthWorkingDays((reportDTO
					.getMonthWorkingDays() != null) ? reportDTO
					.getMonthWorkingDays() : null);
			milestonePeople
					.setHolidays((reportDTO.getHolidays() != null) ? reportDTO
							.getHolidays() : null);
			milestonePeople
					.setLeaves((reportDTO.getLeaves() != null) ? reportDTO
							.getLeaves() : null);
			milestonePeople
					.setTotalDays((reportDTO.getTotalDays() != null) ? reportDTO
							.getTotalDays() : null);

			milestonePeople.setHours((reportDTO.getHours() != null) ? reportDTO
					.getHours() : null);

			milestonePeople
					.setTotalValue((reportDTO.getTotalValue() != null) ? reportDTO
							.getTotalValue() : null);

			milestonePeoples.add(milestonePeople);
		}

		return milestonePeoples;
	}

	public List<MilestonePeopleDTO> getMilestonePeopleWithInvoiceRate(
			List<MilestonePeople> milestonePeople, List<Invoice> invoices) {

		// here from invoices list(DESC ORDER on ID) we are getting latest
		// updated or added
		// invoice rates for resources and adding them to lineitems list

		// And employees is used to find out duplicates and restrict addition

		List<Employee> employees = new ArrayList<Employee>();
		Set<LineItem> lineItems = new HashSet<LineItem>();
		for (Invoice invoice : invoices) {
			for (LineItem item : invoice.getLineItems()) {
				if (item.getItem() instanceof Employee) {
					if (!employees.contains(item.getItem())) {
						lineItems.add(item);
						employees.add((Employee) item.getItem());
					}
				}
			}
		}

		List<MilestonePeopleDTO> milestonePeopleDTOs = new ArrayList<MilestonePeopleDTO>();
		MilestonePeopleDTO milestonePeopleDTO = null;
		if (milestonePeople != null) {
			for (MilestonePeople people : milestonePeople) {
				if (people.getIsBillable()) {
					milestonePeopleDTO = new MilestonePeopleDTO();
					milestonePeopleDTO.setMilestonePeopleId(people.getId());
					milestonePeopleDTO.setEmployeeId(people.getEmployee()
							.getEmployeeId());
					milestonePeopleDTO.setIsBillable(people.getIsBillable());
					milestonePeopleDTO.setEndDate(DateParser.toString(people
							.getEndDate()));
					milestonePeopleDTO.setFromDate(DateParser.toString(people
							.getStartDate()));
					milestonePeopleDTO.setEmployeeDesignation(people
							.getEmployee().getDesignation());
					milestonePeopleDTO.setEmployeeName(people.getEmployee()
							.getFullName());

					milestonePeopleDTO
							.setFromDate(people.getStartDate() != null ? people
									.getStartDate().toString("dd/MM/yyyy")
									: null);
					milestonePeopleDTO
							.setEndDate(people.getEndDate() != null ? people
									.getEndDate().toString("dd/MM/yyyy") : null);
					milestonePeopleDTO.setComments(people.getComments());

					milestonePeopleDTO.setMonthWorkingDays((people
							.getMonthWorkingDays() != null) ? people
							.getMonthWorkingDays() : null);
					milestonePeopleDTO
							.setHolidays((people.getHolidays() != null) ? people
									.getHolidays() : null);
					milestonePeopleDTO
							.setLeaves((people.getLeaves() != null) ? people
									.getLeaves() : null);
					milestonePeopleDTO
							.setTotalDays((people.getTotalDays() != null) ? people
									.getTotalDays() : null);

					// here we are setting total days to count because we are
					// using
					// count invoice for calculation
					milestonePeopleDTO
							.setCount((people.getTotalDays() != null) ? people
									.getTotalDays() : null);

					milestonePeopleDTO
							.setHours((people.getHours() != null) ? people
									.getHours() : null);

					milestonePeopleDTO
							.setTotalValue((people.getTotalValue() != null) ? people
									.getTotalValue() : null);

					Long totalValue = Long.parseLong(people.getTotalValue());
					Long billableDays = totalValue / 8;

					milestonePeopleDTO.setBillableDays(String
							.valueOf(billableDays));

					milestonePeopleDTO.setDuration("DAYS");
					milestonePeopleDTO.setRole(people.getRole());

					// here we are checking the resource rate in milestone
					// people
					// from line items list which is retrieved from invoice list
					// above

					for (LineItem item : lineItems) {

						// here checking wheather milestonepeople and invoice
						// line employee
						if (item.getItem()
								.toString()
								.equalsIgnoreCase(
										people.getEmployee().getUsername())) {

							AES256Encryption aes256Encryption = new AES256Encryption(
									String.valueOf(item.getId()),
									item.getSaltkey());

							String duration = item.getDuration().toString();
							milestonePeopleDTO.setDuration(duration);
							// milestonePeopleDTO.setRole(item.getDescription());

							if (item.getRate() != null && item.getRate() != "") {
								milestonePeopleDTO.setRate(item.getRate());
								Double rate = Double.valueOf(item.getRate());
								Double daysAmount = (billableDays * rate);
								Double hoursAmount = (totalValue * rate);
								milestonePeopleDTO
										.setAmount((duration
												.equalsIgnoreCase("DAYS") ? daysAmount
												.toString()
												: duration
														.equalsIgnoreCase("HOURS") ? hoursAmount
														.toString()
														: aes256Encryption.decrypt(item
																.getAmount())));

							} else {
								milestonePeopleDTO.setAmount(aes256Encryption
										.decrypt(item.getAmount()));
								// Here this is Most Important Because while
								// showing roles on PDF we are using rate
								// condition
								milestonePeopleDTO.setRate("");
							}

						}

					}

					milestonePeopleDTOs.add(milestonePeopleDTO);
				}
			}
		}

		return milestonePeopleDTOs;

	}
}
