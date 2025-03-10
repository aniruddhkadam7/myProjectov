package com.raybiztech.achievement.builder;

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

import com.raybiztech.achievement.dto.AchievementAuditDTO;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.projectmanagement.business.Audit;

@Component("AchievementAuditBuilder")
public class AchievementAuditBuilder {

	@Autowired
	DAO dao;

	Logger logger = Logger.getLogger(AchievementAuditBuilder.class);

	public Map<String, Object> ToAchievementAuditDTO(
			Map<String, List<Audit>> map) {
		Map<String, Object> map1 = new HashMap<String, Object>();
		List<AchievementAuditDTO> AuditDTOs = new ArrayList<AchievementAuditDTO>();

		if (map != null) {
			for (Map.Entry<String, List<Audit>> entry : map.entrySet()) {
				AchievementAuditDTO auditDTO = new AchievementAuditDTO();
				for (Audit audit : entry.getValue()) {

					Employee employee = dao.findBy(Employee.class,
							audit.getModifiedBy());
					auditDTO.setModifiedBy(employee.getFullName());
					auditDTO.setModifiedDate(audit.getModifiedDate().toString(
							"dd-MMM-yyyy hh:mm:ss a"));
					auditDTO.setPersistType(audit.getPersistType());

					switch (audit.getColumnName()) {

					case "achievementType":
						auditDTO.setAchievementType(audit.getNewValue());
						auditDTO.setOldachievementType(audit.getOldValue());
						break;
					case "timePeriod":
						auditDTO.setTimePeriod(audit.getNewValue());
						auditDTO.setOldtimePeriod(audit.getOldValue());
						break;
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
					case "showOnDashBoard":
						auditDTO.setShowOnDashBoard(audit.getNewValue()
								.toString());
						auditDTO.setOldshowOnDashBoard(audit.getOldValue() != null ? audit
								.getOldValue().toString() : null);
						break;
					case "employee":
						if (audit.getOldValue() != null) {
							Employee oldemployee = dao.findBy(Employee.class,
									Long.parseLong(audit.getOldValue()));
							auditDTO.setOldemployee(oldemployee.getFirstName()
									+ " " + oldemployee.getLastName());
						}
						Employee newAssignee = dao.findBy(Employee.class,
								Long.parseLong(audit.getNewValue()));
						auditDTO.setEmployee(newAssignee.getFirstName() + " "
								+ newAssignee.getLastName());

						break;
					case "profilePicture":
						auditDTO.setProfilePicture(audit.getNewValue()
								.toString());
						auditDTO.setOldprofilePicture(audit.getOldValue() != null ? audit
								.getOldValue().toString() : null);
						break;
					case "thumbPicture":
						auditDTO.setThumbPicture(audit.getNewValue().toString());
						auditDTO.setOldthumbPicture(audit.getOldValue() != null ? audit
								.getOldValue().toString() : null);
						break;
					}

				}
				AuditDTOs.add(auditDTO);

			}

		}

		if (AuditDTOs != null) {
			Collections.sort(AuditDTOs, new Comparator<AchievementAuditDTO>() {

				@Override
				public int compare(AchievementAuditDTO audit1,
						AchievementAuditDTO audit2) {
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
								AchievementAuditBuilder.class.getName()).log(
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
