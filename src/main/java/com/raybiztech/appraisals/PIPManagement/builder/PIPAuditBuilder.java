package com.raybiztech.appraisals.PIPManagement.builder;

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

import com.raybiztech.appraisals.PIPManagement.dto.PIPAuditDTO;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.projectmanagement.business.Audit;

@Component("PIPAuditBuilder")
public class PIPAuditBuilder {

	@Autowired
	DAO dao;

	Logger logger = Logger.getLogger(PIPAuditBuilder.class);

	public Map<String, Object> ToPIPAuditDTO(Map<String, List<Audit>> map) {

		HashMap<String, Object> hashMap = new HashMap<String, Object>();
		List<PIPAuditDTO> AuditDtos = new ArrayList<PIPAuditDTO>();
		if (map != null) {
			for (Map.Entry<String, List<Audit>> entry : map.entrySet()) {
				PIPAuditDTO pipAuditDTO = new PIPAuditDTO();

				for (Audit audit : entry.getValue()) {
					Employee employee = dao.findBy(Employee.class,
							audit.getModifiedBy());
					pipAuditDTO.setModifiedBy(employee.getFullName());
					pipAuditDTO.setModifiedDate(audit.getModifiedDate()
							.toString("dd-MMM-yyyy hh:mm:ss a"));
					pipAuditDTO.setPersistType(audit.getPersistType());

					switch (audit.getColumnName()) {

					case "startDate":
						pipAuditDTO
								.setStartDate(audit.getNewValue() != null ? audit
										.getNewValue().toString() : null);
						pipAuditDTO
								.setOldStartDate(audit.getOldValue() != null ? audit
										.getOldValue().toString() : null);
						break;
					case "endDate":
						pipAuditDTO
								.setEndDate(audit.getNewValue() != null ? audit
										.getNewValue().toString() : null);
						pipAuditDTO
								.setOldEndDate(audit.getOldValue() != null ? audit
										.getOldValue().toString() : null);
						break;
					case "extendDate":
						pipAuditDTO
								.setExtendDate(audit.getNewValue() != null ? audit
										.getNewValue().toString() : null);
						pipAuditDTO
								.setOldExtendDate(audit.getOldValue() != null ? audit
										.getOldValue().toString() : null);
						break;
					case "employee":
						pipAuditDTO.setEmployee(audit.getNewValue());
						pipAuditDTO.setOldEmployee(audit.getOldValue());
						break;
					case "rating":
						pipAuditDTO.setRating(audit.getNewValue().toString());
						pipAuditDTO
								.setOldRating(audit.getOldValue() != null ? audit
										.getOldValue().toString() : null);
						break;
					case "PIPFlag":
						pipAuditDTO.setPIPFlag(audit.getNewValue().toString());
						pipAuditDTO
								.setOldPIPFlag(audit.getOldValue() != null ? audit
										.getOldValue().toString() : null);
						break;
					case "remarks":
						pipAuditDTO.setRemarks(audit.getNewValue().toString());
						pipAuditDTO
								.setOldRemarks(audit.getOldValue() != null ? audit
										.getOldValue().toString() : null);
						break;
					case "improvement":
						pipAuditDTO.setImprovement(audit.getNewValue()
								.toString());
						pipAuditDTO
								.setOldImprovement(audit.getOldValue() != null ? audit
										.getOldValue().toString() : null);
						break;
					}

				}
				AuditDtos.add(pipAuditDTO);
			}

		}
		if (AuditDtos != null) {
			Collections.sort(AuditDtos, new Comparator<PIPAuditDTO>() {

				@Override
				public int compare(PIPAuditDTO audit1, PIPAuditDTO audit2) {
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
								PIPAuditBuilder.class.getName()).log(
								Level.SEVERE, null, ex);
					}
					return k;
				}
			});
		}

		hashMap.put("list", AuditDtos != null ? AuditDtos : "");
		hashMap.put("size", AuditDtos != null ? AuditDtos.size() : "");
		return hashMap;
	}

}
