package com.raybiztech.appraisals.builder;

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
import com.raybiztech.appraisals.dto.EmployeeAuditDTO;
import com.raybiztech.projectmanagement.business.Audit;

@Component("employeeAuditBulider")
public class EmployeeAuditBulider {
	@Autowired
	DAO dao;

	Logger logger = Logger.getLogger(EmployeeAuditBulider.class);

	public Map<String, Object> ToEmployeeAuditDTO(Map<String, List<Audit>> map) {

		Map<String, Object> map1 = new HashMap<String, Object>();

		List<EmployeeAuditDTO> employeeAuditDTOs = new ArrayList<EmployeeAuditDTO>();

		if (map != null) {

			for (Map.Entry<String, List<Audit>> entry : map.entrySet()) {
				EmployeeAuditDTO empAuditDTO = new EmployeeAuditDTO();
				for (Audit audit : entry.getValue()) {

					Employee employee = dao.findBy(Employee.class, audit.getModifiedBy());
					System.out.println(employee.getEmployeeId() + employee.getFullName());
					empAuditDTO.setModifiedBy(employee.getFullName());
					empAuditDTO.setModifiedDate(audit.getModifiedDate().toString("dd-MMM-yyyy hh:mm:ss a"));
					empAuditDTO.setPersistType(audit.getPersistType());

					switch (audit.getColumnName()) {
					case "currentLocation":
						empAuditDTO.setCurrentLocation(audit.getNewValue());
						empAuditDTO.setOldCurrentLocation(audit.getOldValue());
						break;
					case "baseLocation":
						empAuditDTO.setBaseLocation(audit.getNewValue());
						empAuditDTO.setOldBaseLocation(audit.getOldValue());
						break;
					case "gender":
						empAuditDTO.setGender(audit.getNewValue());
						empAuditDTO.setOldgender(audit.getOldValue());
						break;
					case "bloodgroup":
						empAuditDTO.setBloodgroup(audit.getNewValue());
						empAuditDTO.setOldbloodGroup(audit.getOldValue());
						break;
					case "dob":
						empAuditDTO.setOfficialBirthday(
								audit.getNewValue() != null ? audit.getNewValue().toString() : null);
						empAuditDTO.setOldOfficalBirthday(
								audit.getOldValue() != null ? audit.getOldValue().toString() : null);
						break;
					case "realDob":
						empAuditDTO
								.setRealBirthday(audit.getNewValue() != null ? audit.getNewValue().toString() : null);
						empAuditDTO.setOldRealBirthday(
								audit.getOldValue() != null ? audit.getOldValue().toString() : null);
						break;
					case "maritalStatus":
						empAuditDTO.setMaritalStatus(audit.getNewValue()); 
						empAuditDTO.setOldMartialStatus(audit.getOldValue());
						break;
					case "marriageDate":
						empAuditDTO.setMarriageDate(audit.getNewValue());
						empAuditDTO.setOldMarriageDate(audit.getOldValue());
						break;
					case "skypeId":
						empAuditDTO.setSkypeId(audit.getNewValue());
						empAuditDTO.setOldSkypeId(audit.getOldValue());
						break;
					case "aboutMe":
						empAuditDTO.setAboutMe(audit.getNewValue());
						empAuditDTO.setOldAboutMe(audit.getOldValue());
						break;
					case "phone":
						empAuditDTO.setPhone(audit.getNewValue());
						empAuditDTO.setOldPhone(audit.getOldValue());
						break;
					case "alternativeMobile":
						empAuditDTO.setAlternativeMobile(audit.getNewValue());
						empAuditDTO.setOldAlternativeMobile(audit.getOldValue());
						break;
					case "homeCode":
						empAuditDTO.setHomeCode(audit.getNewValue());
						empAuditDTO.setOldHomeCode(audit.getOldValue());
						break;
					case "workCode":
						empAuditDTO.setWorkCode(audit.getNewValue());
						empAuditDTO.setOldWorkCode(audit.getOldValue());
						break;
					case "homePhoneNumber":
						empAuditDTO.setHomeNumber(audit.getNewValue());
						empAuditDTO.setOldHomeNumber(audit.getOldValue());
						break;
					case "officePhoneNumber":
						empAuditDTO.setWorkNumber(audit.getNewValue());
						empAuditDTO.setOldWorkNumber(audit.getOldValue());
						break;
					case "emergencyContactName":
						empAuditDTO.setEmergencyContactName(audit.getNewValue());
						empAuditDTO.setOldEmergencyContactName(audit.getOldValue());
						break;
					case "emergencyPhone":
						empAuditDTO.setEmergencyPhone(audit.getNewValue());
						empAuditDTO.setOldEmergencyPhone(audit.getOldValue());
						break;
					case "emergencyRelationShip":
						empAuditDTO.setEmergencyRelationShip(audit.getNewValue());
						empAuditDTO.setOldEmergencyRelationShip(audit.getOldValue());
						break;
					case "presentAddress":
						empAuditDTO.setPresentAddress(audit.getNewValue());
						empAuditDTO.setOldPresentAddress(audit.getOldValue());
						break;
					case "presentCity":
						empAuditDTO.setPresentCity(audit.getNewValue());
						empAuditDTO.setOldPresentCity(audit.getOldValue());
						break;
					case "presentZip":
						empAuditDTO.setPresentZip(audit.getNewValue());
						empAuditDTO.setOldPresentZip(audit.getOldValue());
						break;
					case "presentLandMark":
						empAuditDTO.setPresentLandMark(audit.getNewValue());
						empAuditDTO.setOldPresentLandMark(audit.getOldValue());
						break;
					case "permanentAddress":
						empAuditDTO.setPermanentAddress(audit.getNewValue());
						empAuditDTO.setOldPermanentAddress(audit.getOldValue());
						break;
					case "permanentCity":
						empAuditDTO.setPermanentCity(audit.getNewValue());
						empAuditDTO.setOldPermanentCity(audit.getOldValue());
						break;
					case "permanentZip":
						empAuditDTO.setPermanentZip(audit.getNewValue());
						empAuditDTO.setOldPermanentZip(audit.getOldValue());
						break;
					case "permanentLandMark":
						empAuditDTO.setPermanentLandMark(audit.getNewValue());
						empAuditDTO.setOldPermanentLandMark(audit.getOldValue());
						break;
					case "passportNumber":
						empAuditDTO.setPassportNumber(audit.getNewValue());
						empAuditDTO.setOldPassportNumber(audit.getOldValue());
						break;
					case "passportExpDate":
						empAuditDTO.setPassportExpDate(audit.getNewValue());
						empAuditDTO.setOldPassportExpDate(audit.getOldValue());
						break;
					case "passportIssuedPlace":
						empAuditDTO.setPassportIssuedPlace(audit.getNewValue());
						empAuditDTO.setOldPassportIssuedPlace(audit.getOldValue());
						break;
					case "passportIssuedDate":
						empAuditDTO.setPassportIssuedDate(audit.getNewValue());
						empAuditDTO.setOldPassportIssuedDate(audit.getOldValue());
						break;
					case "personName":
						empAuditDTO.setPersonName(audit.getNewValue());
						empAuditDTO.setOldPersonName(audit.getOldValue());
						break;
					case "relationship":
						empAuditDTO.setRelationShip(audit.getNewValue());
						empAuditDTO.setOldRelationShip(audit.getOldValue());
						break;
					case "contactNumber":
						empAuditDTO.setContactNumber(audit.getNewValue());
						empAuditDTO.setOldContactNumber(audit.getOldValue());
						break;
					case "employmentTypeName":
						empAuditDTO.setEmploymentTypeName(audit.getNewValue());
						empAuditDTO.setOldEmploymentTypeName(audit.getOldValue());
						break;
					case "timeSlot":
						empAuditDTO.setTimeSlot(audit.getNewValue());
						empAuditDTO.setOldtimeSlot(audit.getOldValue());
						break;
					case "underNotice":
						empAuditDTO.setUnderNotice(audit.getNewValue());
						empAuditDTO.setOldunderNotice(audit.getOldValue());
						break;
					case "empRole":
						empAuditDTO.setEmpRole(audit.getNewValue());
						empAuditDTO.setOldempRole(audit.getOldValue());
						break;
					case "designation":
						empAuditDTO.setDesignation(audit.getNewValue());
						empAuditDTO.setOlddesignation(audit.getOldValue());
						break;
					/*
					 * case "role": empAuditDTO.setRole(audit.getNewValue());
					 * empAuditDTO.setOldrole(audit.getOldValue()); break;
					 */
					case "departmentName":
						empAuditDTO.setDepartmentName(audit.getNewValue());
						empAuditDTO.setOldDepartmentName(audit.getOldValue());
						break;
					case "dateOfBirth":
						empAuditDTO.setDateOfBirth(audit.getNewValue());
						empAuditDTO.setOldDateOfBirth(audit.getOldValue());
						break;
					case "jobTypeName":
						empAuditDTO.setJobTypeName(audit.getNewValue());
						empAuditDTO.setOldJobTypeName(audit.getOldValue());
						break;
					case "manager":
						empAuditDTO.setManager(audit.getNewValue());
						empAuditDTO.setOldmanager(audit.getOldValue());
						break;
					case "underNoticeDate":
						empAuditDTO.setReleavingDate(audit.getNewValue());
						empAuditDTO.setOldreleavingDate(audit.getOldValue());
						break;
					case "statusName":
						empAuditDTO.setStatusName(audit.getNewValue());
						empAuditDTO.setOldstatusName(audit.getOldValue());
						break;
					case "releavingDate":
						empAuditDTO.setReleavingDate(audit.getNewValue());
						empAuditDTO.setOldreleavingDate(audit.getOldValue());
						break;
					case "hrAssociate":
						empAuditDTO.setHrAssociate(audit.getNewValue());
						empAuditDTO.setOldHrAssociate(audit.getOldValue());
						break;
					case "isAbsconded":
						empAuditDTO.setIsAbsconeded(audit.getNewValue());
						empAuditDTO.setOldisAbsconed(audit.getOldValue());
						break;
					case "comments":
						empAuditDTO.setComments(audit.getNewValue());
						empAuditDTO.setOldComments(audit.getOldValue());
						
					}
				}
				employeeAuditDTOs.add(empAuditDTO);

			}
		}

		if (employeeAuditDTOs != null) {
			Collections.sort(employeeAuditDTOs, new Comparator<EmployeeAuditDTO>() {
				@Override
				public int compare(EmployeeAuditDTO audit1, EmployeeAuditDTO audit2) {
					int k = 0;
					try {
						SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss a");
						java.util.Date date1 = sdf.parse(audit1.getModifiedDate());
						java.util.Date date2 = sdf.parse(audit2.getModifiedDate());

						if (date1.after(date2)) {
							k = -1;
						}
						if (date1.before(date2)) {
							k = 1;
						}
					} catch (ParseException pe) {
						java.util.logging.Logger.getLogger(EmployeeAuditBulider.class.getName()).log(Level.SEVERE, null,
								pe);
					}
					return k;
				}
			});
		}
		map1.put("list", employeeAuditDTOs != null ? employeeAuditDTOs : "");
		map1.put("size", employeeAuditDTOs != null ? employeeAuditDTOs.size() : "");
		return map1;
	}

}
