package com.raybiztech.supportmanagement.builder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.supportmanagement.dto.SupportAuditDTO;

@Component("SupportAuditBuilder")
public class SupportAuditBuilder {
	@Autowired
	DAO dao;

	Logger logger = Logger.getLogger(SupportAuditBuilder.class);

	public Map<String, Object> ToSupportAuditDTO(Map<String, List<Audit>> map) {
		Map<String, Object> map1 = new HashMap<String, Object>();
		List<SupportAuditDTO> AuditDTOs = new ArrayList<SupportAuditDTO>();

		if (map != null) {
			for (Map.Entry<String, List<Audit>> entry : map.entrySet()) {
				SupportAuditDTO auditDTO = new SupportAuditDTO();
				for (Audit audit : entry.getValue()) {

					Employee employee = dao.findBy(Employee.class,
							audit.getModifiedBy());
					auditDTO.setModifiedBy(employee.getFullName());
					auditDTO.setModifiedDate(audit.getModifiedDate().toString(
							"dd-MMM-yyyy hh:mm:ss a"));
					auditDTO.setPersistType(audit.getPersistType());

					switch (audit.getColumnName()) {

					case "subject":
						auditDTO.setSubject(audit.getNewValue());
						auditDTO.setOldsubject(audit.getOldValue());
						break;
					case "status":
						auditDTO.setStatus(audit.getNewValue());
						auditDTO.setOldstatus(audit.getOldValue());
						break;
					case "priority":
						auditDTO.setPriority(audit.getNewValue());
						auditDTO.setOldpriority(audit.getOldValue());
						break;
					/*
					 * case "ticketsSubCategory":
					 * auditDTO.setApprovalStatus(audit.getNewValue());
					 * auditDTO.setOldapprovalStatus(audit.getOldValue());
					 * break;
					 */
					case "description":

						auditDTO.setDescription(audit.getNewValue());
						auditDTO.setOlddescription(audit.getOldValue());
						break;
					case "startDate":
						auditDTO.setStartDate(audit.getNewValue() != null ? audit
								.getNewValue().toString() : null);
						auditDTO.setOldstartDate(audit.getOldValue() != null ? audit
								.getOldValue().toString() : null);
						break;
					case "endDate":
						auditDTO.setEndDate(audit.getNewValue() != null ? audit
								.getNewValue().toString() : null);
						auditDTO.setOldendDate(audit.getOldValue() != null ? audit
								.getOldValue().toString() : null);
						break;
					case "percentageDone":
						auditDTO.setPercentageDone(audit.getNewValue() != null ? audit
								.getNewValue().toString() : null);
						auditDTO.setOldpercentageDone(audit.getOldValue() != null ? audit
								.getOldValue().toString() : null);
						break;
					case "approvalStatus":
						auditDTO.setApprovalStatus(audit.getNewValue()
								.toString());
						auditDTO.setOldapprovalStatus(audit.getOldValue() != null ? audit
								.getOldValue().toString() : null);
						break;
					case "assignee":
						if (audit.getOldValue() != null) {
							Employee oldemployee = dao.findBy(Employee.class,
									Long.parseLong(audit.getOldValue()));
							auditDTO.setOldassignee(oldemployee.getFirstName()
									+ " " + oldemployee.getLastName());
						}
						Employee newAssignee = dao.findBy(Employee.class,
								Long.parseLong(audit.getNewValue()));
						auditDTO.setAssignee(newAssignee.getFirstName() + " "
								+ newAssignee.getLastName());

						break;
					case "actualTime":
						auditDTO.setActualTime(audit.getNewValue().toString());
						auditDTO.setOldactualTime(audit.getOldValue() != null ? audit
								.getOldValue().toString() : null);
						break;
					case "workFlow":
						auditDTO.setWorkFlow(audit.getNewValue().toString());
						auditDTO.setOldworkFlow(audit.getOldValue() != null ? audit
								.getOldValue().toString() : null);
						break;
					case "estimatedTime":
						auditDTO.setEstimatedTime(audit.getNewValue()
								.toString());
						auditDTO.setOldestimatedTime(audit.getOldValue() != null ? audit
								.getOldValue().toString() : null);
						break;
					case "subCategoryName":
						auditDTO.setSubCategoryName(audit.getNewValue()
								.toString());
						auditDTO.setOldsubCategoryName(audit.getOldValue() != null ? audit
								.getOldValue().toString() : null);
						break;
					case "managesList":
						String[] check = audit.getNewValue().split(",");
						if (audit.getOldValue() != null) {
							String[] oldcheck = audit.getOldValue().split(",");
							Employee employee1 = dao
									.findBy(Employee.class,
											Long.parseLong(oldcheck[oldcheck.length - 1]));
							auditDTO.setOldapprovedByManager(employee1
									.getFullName());
						}
						Employee employee2 = dao.findBy(Employee.class,
								Long.parseLong(check[check.length - 1]));
						auditDTO.setApprovedByManager(employee2.getFullName());
						break;
					case "levelOfHierarchy":
						auditDTO.setLevelOfHierarchy(audit.getNewValue()
								.toString());
						auditDTO.setOldlevelOfHierarchy(audit.getOldValue() != null ? audit
								.getOldValue().toString() : null);
						break;
					case "tracker":
						auditDTO.setTracker(audit.getNewValue());
						auditDTO.setOldtracker(audit.getOldValue());
						break;
					case "accessStartDate":
						auditDTO.setAccessStartDate(audit.getNewValue() != null ? audit
								.getNewValue().toString() : null);
						auditDTO.setOldAccessStartDate(audit.getOldValue() != null ? audit
								.getOldValue().toString() : null);
						break;
					case "accessEndDate":
						auditDTO.setAccessEndDate(audit.getNewValue() != null ? audit
								.getNewValue().toString() : null);
						auditDTO.setOldAccessEndDate(audit.getOldValue() != null ? audit
								.getOldValue().toString() : null);
					}
				}
				AuditDTOs.add(auditDTO);

			}

		}

		if (AuditDTOs != null) {
			Collections.sort(AuditDTOs, new Comparator<SupportAuditDTO>() {

				@Override
				public int compare(SupportAuditDTO audit1,
						SupportAuditDTO audit2) {
					int k = 0;
					try {
						SimpleDateFormat sdf = new SimpleDateFormat(
								"dd-MMM-yyyy hh:mm:ss a");
						java.util.Date date1 = sdf.parse(audit1
								.getModifiedDate());
						java.util.Date date2 = sdf.parse(audit2
								.getModifiedDate());

						if (date1.after(date2)) {
							k = -1;
						}
						if (date1.before(date2)) {
							k = 1;
						}

					} catch (ParseException ex) {
						java.util.logging.Logger.getLogger(
								SupportAuditBuilder.class.getName()).log(
								Level.SEVERE, null, ex);
					}
					return k;
				}
			});
		}

		map1.put("list", AuditDTOs != null ? AuditDTOs : "");
		map1.put("size", AuditDTOs != null ? AuditDTOs.size() : "");
		return map1;
	}
}
