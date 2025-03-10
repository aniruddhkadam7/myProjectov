package com.raybiztech.appraisals.builder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.lang.WordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.DesignationKras;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.Skill;
import com.raybiztech.appraisals.business.TimeSlot;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.dto.ReportiesHierarchyDTO;
import com.raybiztech.appraisals.dto.SearchEmpDetailsDTO;
import com.raybiztech.appraisals.dto.SkillDTO;
import com.raybiztech.appraisals.dto.TimeSlotDTO;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.assetmanagement.business.VendorDetails;
import com.raybiztech.commons.Percentage;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.Second;
import com.raybiztech.multitenancy.TenantContextHolder;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;
import com.raybiztech.recruitment.business.NewJoinee;
import com.raybiztech.recruitment.dao.JobPortalDAOImpl;
import com.raybiztech.recruitment.service.JobPortalService;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.rolefeature.business.Role;

@Component("employeeBuilder")
public class EmployeeBuilder {

	@Autowired
	DesignationKrasBuilder designationKrasBuilder;
	@Autowired
	PropBean propBean;
	@Autowired
	DAO dao;

	@Autowired
	SecurityUtils securityUtils;
	
	@Autowired
	JobPortalService jobPortalServiceImpl;

	public DAO getDao() {
		return dao;
	}

	public void setDao(DAO dao) {
		this.dao = dao;
	}

	org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(EmployeeBuilder.class);

	public DesignationKrasBuilder getDesignationKrasBuilder() {
		return designationKrasBuilder;
	}

	public void setDesignationKrasBuilder(DesignationKrasBuilder designationKrasBuilder) {
		this.designationKrasBuilder = designationKrasBuilder;
	}

	public Employee createEmployeeEntity(EmployeeDTO employeeDTO) {
		Employee employee = new Employee();
		if (employeeDTO != null) {
			employee.setEmployeeId(employeeDTO.getId());
			employee.setFirstName(employeeDTO.getFirstName());
			employee.setLastName(employeeDTO.getLastName());
			employee.setProfilePicPath(employeeDTO.getProfilePicPath());
			//employee.setPassportBackPagePath(employeeDTO.getPassportBackPagePath());
			//employee.setPassportFrontPagePath(employeeDTO.getPassportFrontPagePath());
			employee.setProfilePicGeneratedPath(employeeDTO.getProfilePicGeneratedPath());
			employee.setEmployeeSubmitted(employeeDTO.isEmployeeSubmitted());
			employee.setManagerSubmitted(employeeDTO.isManagerSubmitted());
			employee.setAcknowledged(employeeDTO.isAcknowledged());
			employee.setEmail(employeeDTO.getEmailId());
			employee.setDesignationKras(designationKrasBuilder.createRoleEntity(employeeDTO.getDesignationKrasDTO()));
			employee.setRbtCvPath(employeeDTO.getRbtCvPath());
			Employee manager = new Employee();
			if (employeeDTO.getManager() != null) {
				manager.setEmployeeId(employeeDTO.getManager().getId());
			}
			employee.setManager(manager);
		}

		return employee;
	}

	public Employee createEmployeeEntity1(EmployeeDTO employeeDTO) {
		Employee employee = new Employee();
		if (employeeDTO != null) {
			employee.setEmployeeId(employeeDTO.getId());
			employee.setFirstName(employeeDTO.getFirstName());
			employee.setLastName(employeeDTO.getLastName());
			employee.setProfilePicPath(employeeDTO.getProfilePicPath());
			//employee.setPassportBackPagePath(employeeDTO.getPassportBackPagePath());
			//employee.setPassportFrontPagePath(employeeDTO.getPassportFrontPagePath());
			employee.setProfilePicGeneratedPath(employeeDTO.getProfilePicGeneratedPath());
			employee.setEmployeeSubmitted(employeeDTO.isEmployeeSubmitted());
			employee.setManagerSubmitted(employeeDTO.isManagerSubmitted());
			employee.setAcknowledged(employeeDTO.isAcknowledged());
			employee.setEmail(employeeDTO.getEmailId());
			// employee.setDesignationKras(designationKrasBuilder
			// .createRoleEntity(employeeDTO.getDesignationKrasDTO()));

			Employee manager = new Employee();
			if (employeeDTO.getManager() != null) {
				manager.setEmployeeId(employeeDTO.getManager().getId());
			}
			employee.setManager(manager);
		}

		return employee;
	}

	public EmployeeDTO leaveReportEmployeeDTO(Employee employee) {
		EmployeeDTO employeeDTO = new EmployeeDTO();
		employeeDTO.setId(employee.getEmployeeId());
		employeeDTO.setFullName(employee.getFullName());
		employeeDTO.setUserName(employee.getUsername());
		employeeDTO.setCountry(employee.getCountry()!=null?employee.getCountry():null);

		return employeeDTO;
	}

	public List<EmployeeDTO> leaveReportEmployeeDTOList(List<Employee> employeeList) {

		List<EmployeeDTO> employeeDTOList = new ArrayList<EmployeeDTO>();

		for (Employee employee : employeeList) {
			employeeDTOList.add(leaveReportEmployeeDTO(employee));
		}

		return employeeDTOList;
	}

	public EmployeeDTO createEmployeeDTO(Employee employee) {
		EmployeeDTO employeeDTO = new EmployeeDTO();
		if (employee != null) {
			employeeDTO.setId(employee.getEmployeeId());

			String orignalPath = employee.getProfilePicPath();
			System.out.println("original:" + orignalPath);
			String splitedPath = orignalPath.substring(orignalPath.lastIndexOf("/") + 1);
			System.out.println("spil:" + splitedPath);
			employeeDTO.setProfilePicPath("../profilepics/" + splitedPath);
		//	employeeDTO.setProfilePicPath(employee.getProfilePicPath());
		//	employeeDTO.setImageData(jobPortalServiceImpl.getImageByteData(employeeDTO.getProfilePicPath()));
			if(employee.getPassportFrontPagePath()!=null){
			String frontOriginalPath = employee.getPassportFrontPagePath();
			String frontSplitedPath = frontOriginalPath.substring(frontOriginalPath.lastIndexOf("/") + 1);
			employeeDTO.setPassportFrontPagePath("../passportfrontdetails/"+frontSplitedPath);
			employeeDTO.setPassportFrontPage(jobPortalServiceImpl.getPassportFrontImage(employeeDTO.getId()).getPassportFrontPage());
			}
			if(employee.getPassportBackPagePath()!=null) {
			String backOriginalPath = employee.getPassportBackPagePath();
			String backSplitedPath = backOriginalPath.substring(backOriginalPath.lastIndexOf("/") + 1);
			employeeDTO.setPassportBackPagePath("../passportbackdetails/"+backSplitedPath);
			employeeDTO.setPassportBackPage(jobPortalServiceImpl.getPassportBackImage(employeeDTO.getId()).getPassportBackPage());
			}
			String thumbnailPath = employee.getThumbPicture();
			if (thumbnailPath != null) {
				String splitedThumbnailPath = thumbnailPath.substring(thumbnailPath.lastIndexOf("/") + 1);
				employeeDTO.setThumbPicture("../profilepics/" + splitedThumbnailPath);
			}
			employeeDTO.setMiddleName(employee.getMiddleName());
			employeeDTO.setFirstName(employee.getFirstName());
			employeeDTO.setLastName(employee.getLastName());
			employeeDTO.setRole(employee.getRole());
			employeeDTO.setTimeSlotDTO(convertSlotEntityToDto(employee.getTimeSlot()));
			employeeDTO.setEmailId(employee.getEmail());
			employeeDTO.setEmpManager(employee.getManager().getFullName());
			employeeDTO.setEmployeeSubmitted(employee.isEmployeeSubmitted());
			employeeDTO.setManagerSubmitted(employee.isManagerSubmitted());
			employeeDTO.setAcknowledged(employee.isAcknowledged());
			employeeDTO.setDesignationKrasDTO(designationKrasBuilder.createRoleDTO(employee.getDesignationKras()));
			EmployeeDTO manager = new EmployeeDTO();
			if (employee.getManager() != null) {
				manager.setId(employee.getManager().getEmployeeId());
				manager.setFirstName(employee.getManager().getFirstName());
				manager.setLastName(employee.getManager().getLastName());
			}
			employeeDTO.setManager(manager);
			employeeDTO.setFullName(employee.getFullName());
			String gender = employee.getGender();
			if (gender == null) {
				employeeDTO.setGender("");
			} else {
				employeeDTO.setGender(gender);
			}
			employeeDTO.setBloodgroup(employee.getBloodgroup() != null ? employee.getBloodgroup() : "");
			if (employee.getDob() != null) {
				employeeDTO.setOfficialBirthday(employee.getDob().toString());
			}
			if (employee.getRealDob() != null) {
				employeeDTO.setRealBirthday(employee.getRealDob().toString());
			}

			if (employee.getJoiningDate() != null) {
				employeeDTO.setDateOfJoining(employee.getJoiningDate().toString());
			}

			employeeDTO.setBaseLocation(employee.getBaseLocation());
			employeeDTO.setCurentLocation(employee.getCurrentLocation());
			String maritalStatus = employee.getMaritalStatus();
			if (maritalStatus == null) {
				employeeDTO.setMaritalStatus("");
			} else {
				employeeDTO.setMaritalStatus(employee.getMaritalStatus());
			}

			if (employee.getEmergencyContactName() != null && employee.getEmergencyContactName() != "") {
				employeeDTO.setEmergencyContactName(employee.getEmergencyContactName());
				employeeDTO.setEmergencyContact(employee.getEmergencyContactName());
			}
			if (employee.getEmergencyPhone() != null && employee.getEmergencyPhone() != "") {
				employeeDTO.setEmergencyPhone(employee.getEmergencyPhone());
				employeeDTO
						.setEmergencyContact(employeeDTO.getEmergencyContact() + ", " + employee.getEmergencyPhone());
			}
			String empRelationShip = employee.getRelationship();
			if (empRelationShip == null) {
				employeeDTO.setEmergencyRelationShip("");
			} else {
				employeeDTO.setEmergencyRelationShip(empRelationShip);
				if (employeeDTO.getEmergencyContact() != null) {
					employeeDTO.setEmergencyContact(employeeDTO.getEmergencyContact() + ", " + empRelationShip);
				}
			}
			employeeDTO.setAlternativeMobile(employee.getAlternativeMobile());
			employeeDTO.setDepartmentName(employee.getDepartmentName());
			employeeDTO.setGrade(employee.getGrade());
			employeeDTO.setEmploymentTypeName(employee.getEmploymentTypeName());
			employeeDTO.setJobTypeName(employee.getJobTypeName());
			employeeDTO.setAboutMe(employee.getAboutMe());
			employeeDTO.setMobile(employee.getPhone());
			employeeDTO.setHomeNumber(employee.getHomePhoneNumber());
			employeeDTO.setWorkNumber(employee.getOfficePhoneNumber());
			employeeDTO.setTechnology(employee.getTechnology());

			if (employee.getPresentAddress() != null && employee.getPresentAddress() != "") {
				employeeDTO.setPresentAddress(employee.getPresentAddress());
				employeeDTO.setAddress(employee.getPresentAddress());
			}
			if (employee.getPresentCity() != null && employee.getPresentCity() != "") {
				employeeDTO.setPresentCity(employee.getPresentCity());
				employeeDTO.setAddress(employeeDTO.getAddress() + ", " + employee.getPresentCity());
			}
			employeeDTO.setPresentZip(employee.getPresentZip());
			if (employee.getPresentLandMark() != null && employee.getPresentLandMark() != "") {
				employeeDTO.setPresentLandMark(employee.getPresentLandMark());
				employeeDTO.setAddress(employeeDTO.getAddress() + ", " + employee.getPresentLandMark());
			}

			employeeDTO.setPermanentAddress(employee.getPermanentAddress());
			employeeDTO.setPermanentCity(employee.getPermanentCity());
			employeeDTO.setPermanentZip(employee.getPermanentZip());
			employeeDTO.setPermanentLandMark(employee.getPermanentLandMark());
			employeeDTO.setHomeCode(employee.getHomeCode());
			employeeDTO.setWorkCode(employee.getWorkCode());

			String rbtcvpath = employee.getRbtCvPath();

			employeeDTO.setRbtCvPath(rbtcvpath);
			if (rbtcvpath != null) {
				employeeDTO.setRbtCvName(rbtcvpath.substring(rbtcvpath.lastIndexOf("/") + 1, rbtcvpath.length()));
			}
			if (employee.getPassportExpDate() != null) {
				employeeDTO.setPassportExpDate(employee.getPassportExpDate().toString("dd/MM/yyyy"));
			}
			if (employee.getPassportIssuedDate() != null) {
				employeeDTO.setPassportIssuedDate(employee.getPassportIssuedDate().toString("dd/MM/yyyy"));
			}
			employeeDTO.setPassportIssuedPlace(employee.getPassportIssuedPlace());
			employeeDTO.setPassportNumber(employee.getPassportNumber());

			employeeDTO.setUserName(employee.getUserName());

			if (employee.getStatusName().equalsIgnoreCase("active") && employee.isUnderNotice()) {
				employeeDTO.setStatusName("underNotice");
			} else {
				employeeDTO.setStatusName(employee.getStatusName());
			}
			if (employee.getSkypeId() != null) {
				employeeDTO.setSkypeId(employee.getSkypeId());
			}
			employeeDTO.setDesignation(employee.getDesignation());
			if (employee.getReleavingDate() != null) {
				employeeDTO.setRelievingDate(employee.getReleavingDate().toString());
			}
			if (employee.getUnderNoticeDate() != null) {
				employeeDTO.setUnderNoticeDate(employee.getUnderNoticeDate().toString());
			}

			if (employee.getMarriageDate() != null) {
				employeeDTO.setAnniversary(employee.getMarriageDate().toString());
			}
			//employeeDTO.setIsAbsconded(employee.getIsAbsconded());
			employeeDTO.setContractExists(employee.getContractExists());
			if(employee.getContractStartDate() != null) {
				employeeDTO.setContractStartDate(employee.getContractStartDate().toString("dd/MM/yyyy"));
			}
			if(employee.getContractEndDate() != null) {
				employeeDTO.setContractEndDate(employee.getContractEndDate().toString("dd/MM/yyyy"));
			}
			
			employeeDTO.setPersonalEmail(employee.getPersonalEmail() != null ? employee.getPersonalEmail() : null);
			employeeDTO.setExperience(employee.getExperience() != null ? employee.getExperience() : null);
			employeeDTO.setCompanyExperience(employee.getCompanyExperience() != null ? employee.getCompanyExperience() : null);
			if(employee.getExperience() != null && employee.getCompanyExperience() != null){
				String oldExp = String.valueOf(employeeDTO.getExperience());
				String cureentExp = String.valueOf(employeeDTO.getCompanyExperience());
				String oldExpCount[] = oldExp.split("[.]",0);
				String cureentExpcount[] = cureentExp.split("[.]",0);
				int oldYear = Integer.parseInt(oldExpCount[0]);
				int newYear = Integer.parseInt(cureentExpcount[0]);
				int oldMonth = Integer.parseInt(oldExpCount[1]);
				int newMonth = Integer.parseInt(cureentExpcount[1]);
				
				if((oldMonth+newMonth -12) >= 0){
					
					int year =  (oldYear+newYear+1);
					int month = ((oldMonth+newMonth)-12);
					String date = String.valueOf(year)+"."+String.valueOf(month);
					 Float f = new Float("20.75f");
					 float retval = f.parseFloat(date);
					 double roundedFloat = Math.round(retval * 100.0) / 100.0;
					 Double updatedExperience =(double) (roundedFloat);
					 employeeDTO.setUpdatedExperience(updatedExperience);
				}
				else{
					
					 int year = oldYear+newYear;
					 int month = oldMonth+newMonth;
					 String date = String.valueOf(year)+"."+String.valueOf(month);
					 Float f = new Float("20.75f");
					float retval = f.parseFloat(date);
					double roundedFloat = Math.round(retval * 100.0) / 100.0;
					 Double updatedExperience =(double) (roundedFloat);
					 employeeDTO.setUpdatedExperience(updatedExperience);
				}
				
			}else if(employee.getExperience() != null){
				employeeDTO.setUpdatedExperience(employee.getExperience());
			}
			employeeDTO.setCountry(employee.getCountry()!=null? employee.getCountry():null);
			employeeDTO.setWorkStatus(employee.getWorkStatus()!=null?employee.getWorkStatus():null);
			if(employee.getVendor()!=null){
			employeeDTO.setVendorId(employee.getVendor().getVendorId());
			}
			if(employee.getCountryCodeAlternative()!=null){
				employeeDTO.setCountryCodeAlternative(employee.getCountryCodeAlternative());
			}
			if(employee.getCountryCodeEmergency()!=null){
				employeeDTO.setCountryCodeEmergency(employee.getCountryCodeEmergency());
			}
			if(employee.getCountryCodeHome()!=null){
				employeeDTO.setCountryCodeHome(employee.getCountryCodeHome());
			}
			if(employee.getCountryCodeMobile()!=null){
				employeeDTO.setCountryCodeMobile(employee.getCountryCodeMobile());
			}
			if(employee.getCountryCodeWork()!=null){
				employeeDTO.setCountryCodeWork(employee.getCountryCodeWork());
			}
		 
		}
		return employeeDTO;
	}

	public EmployeeDTO createEmployeeListDto(Employee employee) {
		EmployeeDTO employeeDTO = new EmployeeDTO();
		if (employee != null) {

			String thumbnailPath = employee.getThumbPicture();
			if (thumbnailPath != null) {
				String splitedThumbnailPath = thumbnailPath.substring(thumbnailPath.lastIndexOf("/") + 1);
				employeeDTO.setThumbPicture("../profilepics/" + splitedThumbnailPath);
			}
			employeeDTO.setId(employee.getEmployeeId());
			employeeDTO.setFirstName(employee.getFirstName());
			employeeDTO.setLastName(employee.getLastName());
			employeeDTO.setFullName(getCompleteFullName(employee));
			System.out.println("fullname in emplist:" + employeeDTO.getFullName());
			employeeDTO.setEmailId(employee.getEmail() != null ? employee.getEmail() : "N/A");
			employeeDTO.setMobile(employee.getPhone() != null ? employee.getPhone() : "N/A");
			employeeDTO.setDesignation(employee.getDesignation());
			employeeDTO.setDepartmentName(employee.getDepartmentName());
			employeeDTO.setEmpManager(employee.getManager().getFullName());
			employeeDTO.setBloodgroup(employee.getBloodgroup() != null ? employee.getBloodgroup() : "N/A");
			employeeDTO
					.setDateOfJoining(employee.getJoiningDate() != null ? employee.getJoiningDate().toString() : "N/A");

			employeeDTO.setDob(employee.getDob() != null ? employee.getDob().toString("dd/MM/yyyy") : "N/A");

			Employee loggedEmployee = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder()
					.get("employee");

			Permission permission = dao.checkForPermission("Employee Directory-Options", loggedEmployee);
			if (permission != null && permission.getView()) {
				employeeDTO.setRelievingDate(
						employee.getReleavingDate() != null ? employee.getReleavingDate().toString() : "N/A");

				employeeDTO.setUnderNoticeDate(
						employee.getUnderNoticeDate() != null ? employee.getUnderNoticeDate().toString() : "N/A");
			}
			
			employeeDTO.setExperience(employee.getExperience() != null ? employee.getExperience() : null);
			employeeDTO.setCompanyExperience(employee.getCompanyExperience() != null ? employee.getCompanyExperience() : null);
			if(employee.getExperience() != null && employee.getCompanyExperience() != null){
				Double updatedExperience =employee.getExperience()+employee.getCompanyExperience();
				employeeDTO.setUpdatedExperience(updatedExperience);
			}else if(employee.getExperience() != null){
				employeeDTO.setUpdatedExperience(employee.getExperience());
			}
			employeeDTO.setCountry(employee.getCountry()!=null?employee.getCountry():null);
			employeeDTO.setCurentLocation(employee.getCurrentLocation()!=null?employee.getCurrentLocation():null);
			employeeDTO.setContractStartDate(employee.getContractStartDate()!=null?employee.getContractStartDate().toString():null);
			employeeDTO.setContractEndDate(employee.getContractEndDate()!=null?employee.getContractEndDate().toString():null);
			if(employee.getVendor()!=null){
			employeeDTO.setVendorName(employee.getVendor().getVendorName()!=null? employee.getVendor().getVendorName():"N/A");
			}else{
				employeeDTO.setVendorName("N/A");
			}

		}
		return employeeDTO;
	}

	public SearchEmpDetailsDTO createEmployeeDTOForSearch(Employee employee) {
		SearchEmpDetailsDTO employeeDTO = new SearchEmpDetailsDTO();
		if (employee != null) {
			employeeDTO.setId(employee.getEmployeeId());
			String orignalPath = employee.getProfilePicPath();
			String splitedPath = orignalPath.substring(orignalPath.lastIndexOf("/") + 1);
			employeeDTO.setProfilePicPath("../profilepics/" + splitedPath);
			employeeDTO.setFirstName(employee.getFirstName());
			employeeDTO.setLastName(employee.getLastName());
			employeeDTO.setEmailId(employee.getEmail());
			employeeDTO.setDesignation(employee.getDesignation());
			employeeDTO.setFullName(employee.getFullName());
		}
		return employeeDTO;
	}

	public List<Employee> getemployeeEntityList(List<EmployeeDTO> employeeDTOList) {

		List<Employee> employeeList = new ArrayList<Employee>();

		for (EmployeeDTO employeeDTO : employeeDTOList) {
			employeeList.add(createEmployeeEntity(employeeDTO));
		}

		return employeeList;
	}

	public List<Employee> getemployeeEntityList1(List<EmployeeDTO> employeeDTOList) {

		List<Employee> employeeList = new ArrayList<Employee>();

		for (EmployeeDTO employeeDTO : employeeDTOList) {
			employeeList.add(createEmployeeEntity1(employeeDTO));
		}

		return employeeList;
	}

	public List<SearchEmpDetailsDTO> getemployeeDTOListForSearch(List<Employee> employeeList) {

		List<SearchEmpDetailsDTO> employeeDTOList = new ArrayList<SearchEmpDetailsDTO>();

		for (Employee employee : employeeList) {
			employeeDTOList.add(createEmployeeDTOForSearch(employee));
		}

		return employeeDTOList;
	}

	public List<EmployeeDTO> getemployeeDTOList(List<Employee> employeeList) {

		List<EmployeeDTO> employeeDTOList = new ArrayList<EmployeeDTO>();

		if(employeeList != null) {
		for (Employee employee : employeeList) {
			/* employeeDTOList.add(createEmployeeDTO(employee)); */
			employeeDTOList.add(createEmployeeListDto(employee));
		}
		}

		return employeeDTOList;
	}

	public Employee createNewEmployeeEntity(EmployeeDTO employeeDTO) {

		Employee employee = null;
		employee = new Employee();
		if (employeeDTO != null) {
			if (employeeDTO.getCandidateId() != null) {
				logger.warn("in builder" + employeeDTO.getCandidateId());
				NewJoinee newJoinee = dao.findBy(NewJoinee.class, employeeDTO.getCandidateId());
				employee.setCandidateId(newJoinee.getCandidateId());

				// dao.findBy(Candidate.class, newJoinee.getCandidateId());
			}

			employee.setUnderNotice(Boolean.FALSE);
			employee.setEmployeeId(employeeDTO.getId());
			String tenant = TenantContextHolder.getTenantType().name();
			String emailId = employeeDTO.getUserName() + "@"+tenant.toLowerCase()+".com";
			employee.setEmail(emailId.toLowerCase());
			//employee.setPersonalEmail(employeeDTO.getPersonalEmail() != null ? employeeDTO.getPersonalEmail() : null);
			logger.warn("experience from emploee"+employeeDTO.getExperience());
			employee.setExperience(employeeDTO.getExperience() != null ? employeeDTO.getExperience() : null);
			employee.setFirstName(WordUtils.capitalizeFully(employeeDTO.getFirstName()));
			employee.setMiddleName(WordUtils.capitalizeFully(employeeDTO.getMiddleName()));
			employee.setLastName(WordUtils.capitalizeFully(employeeDTO.getLastName()));
			employee.setEmployeeFullName(
					WordUtils.capitalizeFully(employeeDTO.getFirstName() + " " + employeeDTO.getLastName()));
			employee.setRole(employeeDTO.getRole());
			employee.setGender(employeeDTO.getGender());
			employee.setStatusName("Active");
			String fileLocation = (String) propBean.getPropData().get("fileLocation");
			if (employeeDTO.getGender().equalsIgnoreCase("Male")) {
				employee.setProfilePicPath(fileLocation + "Default_Male.jpg");
				employee.setThumbPicture(fileLocation + "Default_Male.jpg");
			} else {
				employee.setProfilePicPath(fileLocation + "Default_Female.jpg");
				employee.setThumbPicture(fileLocation + "Default_Female.jpg");
			}

			try {
				employee.setDob(DateParser.toDate(employeeDTO.getDob()));
			} catch (ParseException ex) {
				Logger.getLogger(EmployeeBuilder.class.getName()).log(Level.SEVERE, null, ex);
			}

			try {
				employee.setJoiningDate(DateParser.toDate(employeeDTO.getDateOfJoining()));
			} catch (ParseException ex) {
				Logger.getLogger(EmployeeBuilder.class.getName()).log(Level.SEVERE, null, ex);
			}

			employee.setDepartmentName(employeeDTO.getDepartmentName());

			Employee manager = new Employee();
			if (employeeDTO.getManager() != null) {
				manager.setEmployeeId(employeeDTO.getManager().getId());
				employee.setManager(manager);
			}
			employee.setEmploymentTypeName(employeeDTO.getEmploymentTypeName());
			employee.setJobTypeName(employeeDTO.getJobTypeName());
			employee.setUserName(employeeDTO.getUserName().toLowerCase());
			DesignationKras designationKras = dao.findByDesignationName(DesignationKras.class,
					employeeDTO.getDesignation());
			employee.setDesignation(employeeDTO.getDesignation());
			employee.setDesignationKras(designationKras);
			employee.setEmpRole(dao.findByName(Role.class, employeeDTO.getRole()));
			employee.setTimeSlot(employeeDTO.getTimeSlotDTO() != null
					? dao.findBy(TimeSlot.class, employeeDTO.getTimeSlotDTO().getId())
					: null);
			employee.setTechnology(employeeDTO.getTechnology() != null ? employeeDTO.getTechnology() : null);
			employee.setContractExists(employeeDTO.getContractExists());
			if(employeeDTO.getContractExists().equals(Boolean.TRUE)) {
				try {
					employee.setContractStartDate(DateParser.toDate(employeeDTO.getContractStartDate()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					employee.setContractEndDate(DateParser.toDate(employeeDTO.getContractEndDate()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			employee.setCountry(employeeDTO.getCountry()!=null?employeeDTO.getCountry():null);
			employee.setWorkStatus(employeeDTO.getWorkStatus()!=null?employeeDTO.getWorkStatus():null);
			
			// String password = generatePayslipPDFPassword(employeeDTO);
			// employee.setPasswordForTesting(WordUtils.capitalizeFully(employeeDTO.getFirstName())+"1@3");

			// here new Field is added
			//if (employeeDTO.getHrAssociate() != null) {
			
			//here new Field is added
			/*if(employeeDTO.getHrAssociate()!=null) {
				Employee hrAssociate = dao.findBy(Employee.class, employeeDTO.getHrAssociate().getId());
				employee.setHrAssociate(hrAssociate);
			}*/
			if(employeeDTO.getVendorId()!=null){
			VendorDetails ven = dao.findBy(VendorDetails.class, employeeDTO.getVendorId());
			employee.setVendor(ven);
			}else{
				employee.setVendor(null);
			}
		}

		return employee;

	}

	public Employee createEmployeeEntityForBasicInformation(Employee employee, EmployeeDTO employeeDTO) {
		if (employeeDTO != null) {
			System.out.println("in builder");
			employee.setCurrentLocation(employeeDTO.getCurentLocation());
			employee.setBaseLocation(employeeDTO.getBaseLocation());
			employee.setGender(employeeDTO.getGender());
			employee.setBloodgroup(employeeDTO.getBloodgroup());
			String officialBday = employeeDTO.getOfficialBirthday();
			String realBday = employeeDTO.getRealBirthday();
			String marriageday = employeeDTO.getAnniversary();
			if (officialBday != null) {
				if (!officialBday.contains("/")) {
					officialBday = convertStringDatetoMysqlDate(officialBday);
				}
			}
			if (realBday != null) {
				if (!realBday.contains("/")) {
					realBday = convertStringDatetoMysqlDate(realBday);
				}
			}
			if (marriageday != null) {
				if (!marriageday.contains("/")) {
					marriageday = convertStringDatetoMysqlDate(marriageday);
				}
			}

			try {
				employee.setDob(DateParser.toDate(officialBday));
			} catch (ParseException ex) {
				Logger.getLogger(EmployeeBuilder.class.getName()).log(Level.SEVERE, null, ex);
			}
			try {
				employee.setRealDob(DateParser.toDate(realBday));
				employee.setPassportExpDate(DateParser.toDate(employeeDTO.getPassportExpDate()));
				employee.setPassportIssuedDate(DateParser.toDate(employeeDTO.getPassportIssuedDate()));
				employee.setPassportIssuedPlace(employeeDTO.getPassportIssuedPlace());
				employee.setPassportNumber(employeeDTO.getPassportNumber());
				/*employee.setPassportFrontPagePath(employeeDTO.getPassportFrontPagePath());
				employee.setPassportBackPagePath(employeeDTO.getPassportBackPagePath());*/
				
			} catch (ParseException ex) {
				Logger.getLogger(EmployeeBuilder.class.getName()).log(Level.SEVERE, null, ex);
			}
			employee.setMaritalStatus(employeeDTO.getMaritalStatus());

			employee.setAboutMe(employeeDTO.getAboutMe());
			employee.setPhone(employeeDTO.getMobile());
			employee.setHomePhoneNumber(employeeDTO.getHomeNumber());
			employee.setOfficePhoneNumber(employeeDTO.getWorkNumber());
			employee.setPresentAddress(employeeDTO.getPresentAddress());
			employee.setPresentCity(employeeDTO.getPresentCity());
			employee.setPresentZip(employeeDTO.getPresentZip());
			employee.setPresentLandMark(employeeDTO.getPresentLandMark());
			employee.setPermanentAddress(employeeDTO.getPermanentAddress());
			employee.setPermanentCity(employeeDTO.getPermanentCity());
			employee.setPermanentZip(employeeDTO.getPermanentZip());
			employee.setPermanentLandMark(employeeDTO.getPermanentLandMark());
			employee.setHomeCode(employeeDTO.getHomeCode());
			employee.setWorkCode(employeeDTO.getWorkCode());
			employee.setEmergencyContactName(employeeDTO.getEmergencyContactName());
			employee.setEmergencyPhone(employeeDTO.getEmergencyPhone());
			employee.setRelationship(employeeDTO.getEmergencyRelationShip());
			employee.setAlternativeMobile(employeeDTO.getAlternativeMobile());
			employee.setSkypeId(employeeDTO.getSkypeId());
			employee.setPersonalEmail(employeeDTO.getPersonalEmail() != null ?employeeDTO.getPersonalEmail(): null);
			employee.setRbtCvPath(employeeDTO.getRbtCvPath());
			if(employeeDTO.getComments()!=null)
			  {
		        employee.setComments(employeeDTO.getComments());
		      }
			/*
			employee.setProfilePicPath(employeeDTO.getProfilePicPath());
			employee.setThumbPicture(employeeDTO.getThumbPicture());*/
			if(employeeDTO.getCountryCodeMobile()!=null){
				employee.setCountryCodeMobile(employeeDTO.getCountryCodeMobile());
			}else{
				employee.setCountryCodeMobile(null);
			}
			if(employeeDTO.getCountryCodeAlternative()!=null){
				employee.setCountryCodeAlternative(employeeDTO.getCountryCodeAlternative());
			}else{
				employee.setCountryCodeAlternative(null);
			}
			if(employeeDTO.getCountryCodeEmergency()!=null){
				employee.setCountryCodeEmergency(employeeDTO.getCountryCodeEmergency());
			}else{
				employee.setCountryCodeEmergency(null);
			}
			if(employeeDTO.getCountryCodeWork()!=null){
				employee.setCountryCodeWork(employeeDTO.getCountryCodeWork());
			}else{
				employee.setCountryCodeWork(null);
			}
			if(employeeDTO.getCountryCodeHome()!=null){
				employee.setCountryCodeHome(employeeDTO.getCountryCodeHome());
			}else{
				employee.setCountryCodeHome(null);
			}
			try {
				if (employee.getMaritalStatus().equalsIgnoreCase("Single")) {
					employee.setMarriageDate(null);
				} else {
					employee.setMarriageDate(DateParser.toDate(marriageday));
				}
			} catch (ParseException ex) {
				Logger.getLogger(EmployeeBuilder.class.getName()).log(Level.SEVERE, null, ex);
			}
		}

		return employee;
	}

	public Employee createEmployeeEntityForContactInformation(Employee employee, EmployeeDTO employeeDTO) {
		if (employeeDTO != null) {
			employee.setPhone(employeeDTO.getMobile());
			employee.setHomePhoneNumber(employeeDTO.getHomeNumber());
			employee.setOfficePhoneNumber(employeeDTO.getWorkNumber());
			employee.setPresentAddress(employeeDTO.getPresentAddress());
			employee.setPresentCity(employeeDTO.getPresentCity());
			employee.setPresentZip(employeeDTO.getPresentZip());
			employee.setPresentLandMark(employeeDTO.getPresentLandMark());
			employee.setPermanentAddress(employeeDTO.getPermanentAddress());
			employee.setPermanentCity(employeeDTO.getPermanentCity());
			employee.setPermanentZip(employeeDTO.getPermanentZip());
			employee.setPermanentLandMark(employeeDTO.getPermanentLandMark());
			employee.setHomeCode(employeeDTO.getHomeCode());
			employee.setWorkCode(employeeDTO.getWorkCode());
			employee.setEmergencyContactName(employeeDTO.getEmergencyContactName());
			employee.setEmergencyPhone(employeeDTO.getEmergencyPhone());
			employee.setRelationship(employeeDTO.getEmergencyRelationShip());
			employee.setAlternativeMobile(employeeDTO.getAlternativeMobile());
		}

		return employee;
	}

	public Set<SkillDTO> convertSkillDTO(Set<Skill> skills) {

		Set<SkillDTO> skillDTOs = new HashSet<SkillDTO>();

		if (!skills.isEmpty()) {
			for (Skill skill : skills) {
				SkillDTO skillDTO = new SkillDTO();
				skillDTO.setSkillId(skill.getSkillId());
				skillDTO.setCategoryType(skill.getCategoryType());
				skillDTO.setComments(skill.getComments());
				skillDTO.setCompetency(skill.getCompetency());
				EmployeeDTO employee = new EmployeeDTO();
				employee.setId(skill.getEmployee().getEmployeeId());
				skillDTO.setEmployee(employee);
				skillDTO.setExpYear(skill.getExpYear());
				skillDTO.setExpMonth(skill.getExpMonth());
				skillDTO.setSkillType(skill.getSkillType());
				skillDTOs.add(skillDTO);
			}
		}

		return skillDTOs;

	}

	public List<SkillDTO> convertSkillDTO1(Set<Skill> skills) {

		List<SkillDTO> skillDTOs = new ArrayList<SkillDTO>();

		if (!skills.isEmpty()) {
			for (Skill skill : skills) {
				SkillDTO skillDTO = new SkillDTO();
				skillDTO.setSkillId(skill.getSkillId());
				skillDTO.setCategoryType(skill.getCategoryType());
				skillDTO.setComments(skill.getComments());
				skillDTO.setCompetency(skill.getCompetency());
				EmployeeDTO employee = new EmployeeDTO();
				employee.setId(skill.getEmployee().getEmployeeId());
				skillDTO.setEmployee(employee);
				skillDTO.setExpYear(skill.getExpYear());
				skillDTO.setExpMonth(skill.getExpMonth());
				skillDTO.setSkillType(skill.getSkillType());
				skillDTO.setCategoryTypeId(skill.getCategoryTypeId());
				skillDTOs.add(skillDTO);
			}
		}

		return skillDTOs;

	}

	public String convertStringDatetoMysqlDate(String start) {
		// String start = "01/02/2014";
		SimpleDateFormat startFormat = new SimpleDateFormat("dd MMM yyyy");
		SimpleDateFormat toFormat = new SimpleDateFormat("dd/MM/yyyy");
		java.util.Date date = null;
		try {
			date = startFormat.parse(start);
		} catch (ParseException ex) {
			java.util.logging.Logger.getLogger(JobPortalDAOImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		String mysqlString = toFormat.format(date);
		return mysqlString;
	}
	public List<EmployeeDTO> getemployeeDTOListForSearchByName(List<Employee> employeeList) {

		List<EmployeeDTO> employeeDTOList = new ArrayList<EmployeeDTO>();
		for (Employee employee : employeeList) {
			EmployeeDTO employeeDTO = new EmployeeDTO();
			Double percent = 0.0;
			for (Map.Entry<Project, AllocationDetails> entry : employee.getAllocations().entrySet()) {

				AllocationDetails details = entry.getValue();

				if (details.getIsAllocated()) {
					percent = percent + details.getPercentage().getValue();

				}
			}
			employeeDTO.setPercent(percent);
			employeeDTO.setId(employee.getEmployeeId());
			employeeDTO.setFirstName(employee.getFirstName());
			employeeDTO.setLastName(employee.getLastName());
			employeeDTO.setDesignation(employee.getDesignation());
			employeeDTO.setDepartmentName(employee.getDepartmentName());
			employeeDTOList.add(employeeDTO);
		}
		return employeeDTOList;

	}

	public EmployeeDTO createAppraisalDTO(Employee employee) {
		EmployeeDTO employeeDTO = new EmployeeDTO();
		if (employee != null) {
			employeeDTO.setId(employee.getEmployeeId());
			String orignalPath = employee.getThumbPicture();
			String splitedPath = orignalPath.substring(orignalPath.lastIndexOf("/") + 1);
			employeeDTO.setProfilePicPath("../profilepics/" + splitedPath);
			String thumbnailPath = employee.getThumbPicture();
			if (thumbnailPath != null) {
				String splitedThumbnailPath = thumbnailPath.substring(thumbnailPath.lastIndexOf("/") + 1);
				employeeDTO.setThumbPicture("../profilepics/" + splitedThumbnailPath);
			}
			employeeDTO.setEmailId(employee.getEmail());
			employeeDTO.setEmpManager(employee.getManager().getFullName());
			employeeDTO.setDesignation(employee.getDesignation());
			employeeDTO.setFullName(employee.getFullName());
			employeeDTO.setDepartmentName(employee.getDepartmentName());
		}
		return employeeDTO;
	}
	public EmployeeDTO allcationSearchForEmployee(Employee employee, DateRange date) {
		EmployeeDTO employeeDTO = new EmployeeDTO();
		Double percent = 0.0;
		if (employee != null) {
			for (Map.Entry<Project, AllocationDetails> entry : employee.getAllocations().entrySet()) {
				AllocationDetails details = entry.getValue();
				if ((date.getMinimum().isBefore(details.getPeriod().getMinimum())
						&& date.getMaximum().isAfter(details.getPeriod().getMaximum()))
						|| (date.contains(details.getPeriod()))
						|| ((details.getPeriod().getMinimum().isBefore(date.getMaximum())
								|| details.getPeriod().getMinimum().equals(date.getMaximum()))
								&& (details.getPeriod().getMaximum().isAfter(date.getMinimum())
										|| details.getPeriod().getMaximum().equals(date.getMinimum())))) {
					if (details.getIsAllocated()) {
						percent = percent + details.getPercentage().getValue();
					}
				}
			}
			employeeDTO.setPercent(percent);
			employeeDTO.setId(employee.getEmployeeId());
			employeeDTO.setFirstName(employee.getFirstName());
			employeeDTO.setLastName(employee.getLastName());
			employeeDTO.setDesignation(employee.getDesignation());
			employeeDTO.setDepartmentName(employee.getDepartmentName());
		}
		return employeeDTO;
	}
	public List<ReportiesHierarchyDTO> getReporteeDTOList(List<Employee> reporteeList) {

		List<ReportiesHierarchyDTO> reporteeDTOList = new ArrayList<ReportiesHierarchyDTO>();
		if (reporteeList != null) {
			for (Employee employee : reporteeList) {
				ReportiesHierarchyDTO reportiesHierarchyDTO = new ReportiesHierarchyDTO();
				reportiesHierarchyDTO.setManagerId(employee.getManager().getEmployeeId());
				reportiesHierarchyDTO.setManagerName(employee.getManager().getEmployeeFullName());
				reportiesHierarchyDTO.setReporteeId(employee.getEmployeeId());
				reportiesHierarchyDTO.setReporteeName(employee.getEmployeeFullName());
				reportiesHierarchyDTO.setMobile(employee.getPhone());

				List<AllocationDetails> allocationDetails = dao.getAllOfProperty(AllocationDetails.class, "employee",
						employee);

				StringBuffer buffer = new StringBuffer();
				StringBuffer buffer2 = new StringBuffer();
				for (AllocationDetails empDetails : allocationDetails) {
					Percentage percentage1=empDetails.getPercentage();
				
					int per=0;
					
					if(per!=Math.round(Math.round(percentage1.getValue())))
					{
						
						buffer2.append(empDetails.getProject().getProjectName());
						buffer2.append(" - ");
						Percentage percentage = empDetails.getPercentage();
						buffer2.append(Math.round(percentage.getValue()));
						buffer2.append("% , ");
					}
					else
					{
						
						buffer.append(empDetails.getProject().getProjectName());
						buffer.append(" - ");
						Percentage percentage = empDetails.getPercentage();
						buffer.append(Math.round(percentage.getValue()));
						buffer.append("% , ");
					}
					
					
					
				}
				buffer2.append(buffer);
				String stringDetails = buffer2.toString();

				String modifiedString = stringDetails.replaceAll(", $", ".");

				reportiesHierarchyDTO.setAllcoationDetails(modifiedString);
				reporteeDTOList.add(reportiesHierarchyDTO);
			}

		}
		return reporteeDTOList;
	}

	public List<TimeSlotDTO> convertShiftToEntity(List<TimeSlot> timeSlots) {
		List<TimeSlotDTO> dtos = new ArrayList<TimeSlotDTO>();
		if (timeSlots != null) {
			for (TimeSlot slot : timeSlots) {
				TimeSlotDTO dto = convertSlotEntityToDto(slot);
				dtos.add(dto);
			}
		}
		return dtos;
	}

	public TimeSlotDTO convertSlotEntityToDto(TimeSlot slot) {
		TimeSlotDTO dto = new TimeSlotDTO();
		if (slot != null) {
			dto.setName(slot.getName());
			dto.setId(slot.getId());
			dto.setGraceTime(slot.getGraceTime());
			String[] starttime = slot.getStartTime().split(":");
			String[] endtime = slot.getEndTime().split(":");
			dto.setStartTimeHour(starttime[0]);
			dto.setStartTimeMinutes(starttime[1]);
			dto.setEndTimeHour(endtime[0]);
			dto.setEndTimeMinutes(endtime[1]);
		}
		return dto;
	}

	public TimeSlot toEditEntity(TimeSlotDTO timeSlotDTO) {
		Long loggedInEmpId = securityUtils.getLoggedEmployeeIdforSecurityContextHolder();
		Employee employee = dao.findBy(Employee.class, loggedInEmpId);
		TimeSlot timeSlot = null;
		if (timeSlotDTO != null) {
			timeSlot = dao.findBy(TimeSlot.class, timeSlotDTO.getId());
			timeSlot.setStartTime(timeSlotDTO.getStartTimeHour() + ":" + timeSlotDTO.getStartTimeMinutes());
			timeSlot.setEndTime(timeSlotDTO.getEndTimeHour() + ":" + timeSlotDTO.getEndTimeMinutes());
			timeSlot.setGraceTime(timeSlotDTO.getGraceTime());
			timeSlot.setUpdatedBy(employee.getEmployeeId());
			timeSlot.setUpdatedDate(new Second());
		}
		return timeSlot;
	}

	public List<EmployeeDTO> getManagerDTOList(List<Employee> employeeList) {

		List<EmployeeDTO> managerDTOList = new ArrayList<EmployeeDTO>();
		for (Employee employee : employeeList) {
			managerDTOList.add(createMangerDTO(employee));
		}
		return managerDTOList;
	}

	public EmployeeDTO createMangerDTO(Employee employee) {
		EmployeeDTO employeeDto = new EmployeeDTO();
		if (employee != null) {
			employeeDto.setId(employee.getEmployeeId());
			employeeDto.setFirstName(employee.getFirstName());
			employeeDto.setMiddleName(employee.getMiddleName());
			employeeDto.setLastName(employee.getLastName());
			EmployeeDTO manager = new EmployeeDTO();
			if (employee.getManager() != null) {
				manager.setId(employee.getManager().getEmployeeId());
				manager.setFirstName(employee.getManager().getFirstName());
				manager.setMiddleName(employee.getManager().getMiddleName());
				manager.setLastName(employee.getManager().getLastName());
				manager.setFullName(employee.getManager().getFullName());
				manager.setDepartmentName(employee.getManager().getDepartmentName());
				manager.setDepartmentName(employee.getManager().getDepartmentName());
			}
			employeeDto.setManager(manager);
			employeeDto.setFullName(employee.getFullName());
			EmployeeDTO projectManager = new EmployeeDTO();
			if(employee.getProjectManager() != null) {
				projectManager.setId(employee.getProjectManager().getEmployeeId());
				projectManager.setFirstName(employee.getProjectManager().getFirstName());
				projectManager.setMiddleName(employee.getProjectManager().getMiddleName());
				projectManager.setLastName(employee.getProjectManager().getLastName());
				projectManager.setFullName(employee.getProjectManager().getFullName());
				projectManager.setDepartmentName(employee.getProjectManager().getDepartmentName());
				employeeDto.setProjectManager(projectManager);
			}
		}
		return employeeDto;
	}

	// correct allocation report

	public List<EmployeeDTO> getemployeeDTOListForSearchByNameReport(
			List<Employee> employeeList, DateRange date,
			List<String> employeeStatus, String isBillable) {
		List<EmployeeDTO> employeeDTOList = new ArrayList<EmployeeDTO>();
		for (Employee employee : employeeList) {
			EmployeeDTO employeeDTO = new EmployeeDTO();
			Double percent = 0.0;
			for (Map.Entry<Project, AllocationDetails> entry : employee
					.getAllocations().entrySet()) {

				AllocationDetails details = entry.getValue();
				if ((date.getMinimum().isBefore(
						details.getPeriod().getMinimum()) && date.getMaximum()
						.isAfter(details.getPeriod().getMaximum()))
						|| (date.contains(details.getPeriod()))
						|| ((details.getPeriod().getMinimum()
								.isBefore(date.getMaximum()) || details
								.getPeriod().getMinimum()
								.equals(date.getMaximum())) && (details
								.getPeriod().getMaximum()
								.isAfter(date.getMinimum()) || details
								.getPeriod().getMaximum()
								.equals(date.getMinimum())))) {
					// System.out.println("In details value"+details.getBillable());

					/*
					 * if (details.getIsAllocated() &&
					 * (isAllocated.equalsIgnoreCase("true") ||
					 * isAllocated.equalsIgnoreCase("All"))) {
					 */

					if (employeeStatus.size() == 0
							&& isBillable.equalsIgnoreCase("All")) {

						percent = percent + details.getPercentage().getValue();
					} else {

					if (isBillable.equalsIgnoreCase("onBench")
								&& (details.getBillable().equals(false))) {
							percent = percent
									+ details.getPercentage().getValue();
						}

						if (((employeeStatus.size() == 0
								|| isBillable.equalsIgnoreCase("All"))) && (!isBillable.equalsIgnoreCase("onBench"))) {
							if (employeeStatus.size() == 0) {

								if (isBillable.equalsIgnoreCase(details
										.getBillable().toString())
										&& (details.getIsAllocated().toString()
												.equalsIgnoreCase("true") || details
												.getIsAllocated().toString()
												.equalsIgnoreCase("false"))) {

									percent = percent
											+ details.getPercentage()
													.getValue();
								}
							} else {

								if (employeeStatus.contains(details
										.getIsAllocated().toString())
										&& (details.getBillable().toString()
												.equalsIgnoreCase("true") || details
												.getBillable().toString()
												.equalsIgnoreCase("false"))) {
									percent = percent
											+ details.getPercentage()
													.getValue();
								}
							}
						} else {
							if (employeeStatus.contains(details
									.getIsAllocated().toString())
									&& isBillable.equalsIgnoreCase(details
											.getBillable().toString())) {
								percent = percent
										+ details.getPercentage().getValue();
							}

						}

					}

				}
			}
			employeeDTO.setPercent(percent);
			employeeDTO.setId(employee.getEmployeeId());
			employeeDTO.setFirstName(employee.getFirstName());
			employeeDTO.setLastName(employee.getLastName());
			employeeDTO.setDesignation(employee.getDesignation());
			employeeDTO.setDepartmentName(employee.getDepartmentName());
			employeeDTOList.add(employeeDTO);
		}
		return employeeDTOList;

	}

	public String getCompleteFullName(Employee employee) {

		String finalname = "";
		String firstName = employee.getFirstName().substring(0, 1).toUpperCase() + employee.getFirstName().substring(1);
		String middlename = employee.getMiddleName();
		finalname = middlename == null ? ""
				: !middlename.isEmpty() ? middlename.substring(0, 1).toUpperCase() + middlename.substring(1) : "";

		String lastName = employee.getLastName().substring(0, 1).toUpperCase() + employee.getLastName().substring(1);
		String completename = firstName + " " + finalname + " " + lastName;

		return completename;

	}

}
