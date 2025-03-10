/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.employeeprofile.service;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.appraisals.builder.EmployeeAuditBulider;
import com.raybiztech.appraisals.builder.QualificationBuilder;
import com.raybiztech.appraisals.builder.QualificationLookUpBuilder;
import com.raybiztech.appraisals.builder.VisaDetailsBuilder;
import com.raybiztech.appraisals.builder.VisaLookUpBuilder;
import com.raybiztech.appraisals.business.DesignationKras;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.EmployeeBankInformation;
import com.raybiztech.appraisals.business.EmployeeFamilyInformation;
import com.raybiztech.appraisals.business.Finance;
import com.raybiztech.appraisals.business.Qualification;
import com.raybiztech.appraisals.business.QualificationLookUp;
import com.raybiztech.appraisals.business.VisaDetails;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.MultipleSelectionDTO;
import com.raybiztech.appraisals.dto.QualificationDTO;
import com.raybiztech.appraisals.dto.QualificationLookUpDTO;
import com.raybiztech.appraisals.dto.VisaDetailDTO;
import com.raybiztech.appraisals.dto.VisaLookUpDTO;
import com.raybiztech.appraisals.exception.FileUploaderUtilException;
import com.raybiztech.appraisals.observation.exceptions.DuplicateObservationException;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.appraisals.utils.FileUploaderUtil;
import com.raybiztech.assetmanagement.business.Product;
import com.raybiztech.assetmanagement.business.VendorDetails;
import com.raybiztech.assetmanagement.dto.AssetDto;
import com.raybiztech.assetmanagement.service.AssetManagementServiceImpl;
import com.raybiztech.commons.Percentage;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.Second;
import com.raybiztech.employeeprofile.Exception.HolidayAlredyExistException;
import com.raybiztech.employeeprofile.builder.EmployeeProfileBuilder;
import com.raybiztech.employeeprofile.dao.EmployeeProfileDAOI;
import com.raybiztech.employeeprofile.dto.EmployeeBankInformationDTO;
import com.raybiztech.employeeprofile.dto.EmployeeFamilyInformationDTO;
import com.raybiztech.employeeprofile.dto.EmployeeFinanceDTO;
import com.raybiztech.employeeprofile.dto.FinanceDTO;
import com.raybiztech.leavemanagement.dao.LeaveDAO;
import com.raybiztech.lookup.business.BankNameLookup;
import com.raybiztech.payslip.mailNotification.PayslipGeneratedMailNotification;
import com.raybiztech.projectmanagement.builder.AuditBuilder;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.recruitment.builder.CandidateBuilder;
import com.raybiztech.recruitment.business.Holidays;
import com.raybiztech.recruitment.dto.AnniversariesDTO;
import com.raybiztech.recruitment.dto.CandidateInterviewTimelineDTO;
import com.raybiztech.recruitment.dto.HolidaysDTO;
import com.raybiztech.recruitment.service.JobPortalService;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.recruitment.utils.FileUploaderUtility;
import com.raybiztech.supportmanagement.service.SupportManagementServiceImpl;

/**
 * 
 * @author naresh
 */
@Service("employeeProfileServiceImpl")
@Transactional
public class EmployeeProfileServiceImpl implements EmployeeProfileServiceI {

	@Autowired
	DAO dao;
	@Autowired
	EmployeeProfileDAOI employeeProfileDAOIMPL;
	@Autowired
	EmployeeProfileBuilder employeeProfileBuilder;
	@Autowired
	LeaveDAO leaveDAO;
	@Autowired
	PayslipGeneratedMailNotification generatedMailNotification;
	@Autowired
	CandidateBuilder candidateBuilder;
	@Autowired
	JobPortalService jobPortalServiceImpl;

	@Autowired
	QualificationLookUpBuilder qualificationLookUpBuilder;

	@Autowired
	QualificationBuilder qualificationBuilder;

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	EmployeeAuditBulider employeeAuditBulider;

	@Autowired
	AuditBuilder auditBuilder;

	@Autowired
	EmployeeProfileDAOI employeeProfileDAOImpl;

	@Autowired
	PropBean propBean;

	@Autowired
	ProjectService projectService;

	@Autowired
	VisaLookUpBuilder visaLookUpBuilder;

	@Autowired
	VisaDetailsBuilder visaDetailsBuilder;

	org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Employee.class);

	@Override
	public List<EmployeeFamilyInformationDTO> getEmployeeFamilyInformation(Long employeeId) {
		Employee employee = dao.findBy(Employee.class, employeeId);
		List<EmployeeFamilyInformationDTO> familyInformation = employeeProfileBuilder
				.convertEmployeeFamilyInformationDTO(employee.getEmployeeFamilyDetails());
		return familyInformation;
	}

	@Caching(evict = { @CacheEvict(value = "otherEmployeeInformation", key = "#familyInformationDTO.employeeId"),
			@CacheEvict(value = "employees", allEntries = true) })
	@Override
	public void saveEmployeeFamilyDetails(EmployeeFamilyInformationDTO familyInformationDTO) {
		/*
		 * Employee employee = dao.findBy(Employee.class,
		 * Long.parseLong(familyInformationDTO.getEmployeeId()));
		 */
		Employee employee = dao.findBy(Employee.class, familyInformationDTO.getEmployeeId());
		// Employee oldEmployee = cloneMethod(employee);
		EmployeeFamilyInformation familyInformation = new EmployeeFamilyInformation();
		familyInformation.setEmployee(employee);
		familyInformation.setPersonName(familyInformationDTO.getPersonName());
		familyInformation.setRelationShip(familyInformationDTO.getRelationShip());
		familyInformation.setContactNumber(familyInformationDTO.getContactNumber());
		if(familyInformationDTO.getCountryCodeContact()!=null)
		  {
		      familyInformation.setCountryCodeContact(familyInformationDTO.getCountryCodeContact());
		   }else{
			   familyInformation.setCountryCodeContact(null);
		   }
		try {
			familyInformation.setDateOfBirth(DateParser.toDate(familyInformationDTO.getDateOfBirth()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dao.save(familyInformation);
		// for storing in audit table
		List<Audit> empFamilyAudit = auditBuilder.AuditTOEmployeeFamilyEntity(familyInformation,
				familyInformation.employee.getEmployeeId(), "EMPLOYEE_FAMILYDETAILS", "CREATED");
		for (Audit audit : empFamilyAudit) {
			employeeProfileDAOIMPL.save(audit);
		}

	}

	public Employee cloneMethod(Employee emp) {
		Employee oldEmp = new Employee();
		try {
			oldEmp = (Employee) emp.clone();
		} catch (CloneNotSupportedException ex) {
			java.util.logging.Logger.getLogger(EmployeeProfileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		return oldEmp;
	}

	@Override
	public EmployeeFamilyInformationDTO getFamilyInformation(long familyId) {
		EmployeeFamilyInformation familyInformation = dao.findBy(EmployeeFamilyInformation.class, familyId);
		EmployeeFamilyInformationDTO informationDTO = employeeProfileBuilder.convertEntityToDTO(familyInformation);
		return informationDTO;
	}

	@Caching(evict = { @CacheEvict(value = "otherEmployeeInformation", key = "#familyInformationDTO.employeeId"),
			@CacheEvict(value = "employees", allEntries = true) })
	@Override
	public void updateEmployeeFamilyDetails(EmployeeFamilyInformationDTO familyInformationDTO) {
		EmployeeFamilyInformation familyInformation = dao.findBy(EmployeeFamilyInformation.class,
				familyInformationDTO.getFamilyId());
		EmployeeFamilyInformation oldEmployeeFamilyInformation = cloneMethod(familyInformation);
		familyInformation.setPersonName(familyInformationDTO.getPersonName());
		familyInformation.setRelationShip(familyInformationDTO.getRelationShip());
		familyInformation.setContactNumber(familyInformationDTO.getContactNumber());
		if(familyInformationDTO.getCountryCodeContact()!=null){
			familyInformation.setCountryCodeContact(familyInformationDTO.getCountryCodeContact());
		}else{
			familyInformation.setCountryCodeContact(null);
		}
		try {
			familyInformation.setDateOfBirth(DateParser.toDate(familyInformationDTO.getDateOfBirth()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dao.saveOrUpdate(familyInformation);

		// for storing in audit table
		List<Audit> empFamilyAudit = auditBuilder.UpdateAuditTOEmployeeFamilyEntity(familyInformation,
				familyInformation.employee.getEmployeeId(), oldEmployeeFamilyInformation, "EMPLOYEE_FAMILYDETAILS",
				"UPDATED");
		for (Audit audit : empFamilyAudit) {
			dao.save(audit);
		}

	}

	public EmployeeFamilyInformation cloneMethod(EmployeeFamilyInformation employeeFamilyInformation) {
		EmployeeFamilyInformation oldEmployeeFamilyInformation = new EmployeeFamilyInformation();
		try {
			oldEmployeeFamilyInformation = (EmployeeFamilyInformation) employeeFamilyInformation.clone();
		} catch (CloneNotSupportedException ex) {
			java.util.logging.Logger.getLogger(SupportManagementServiceImpl.class.getName()).log(Level.SEVERE, null,
					ex);
		}
		return oldEmployeeFamilyInformation;
	}

	@Caching(evict = { @CacheEvict(value = "otherEmployeeInformation", allEntries = true),
			@CacheEvict(value = "employees", allEntries = true) })
	@Override
	public void deleteFamilymember(long familyId) {
		EmployeeFamilyInformation familyInformation = dao.findBy(EmployeeFamilyInformation.class, familyId);
		familyInformation.setEmployee(null);
		dao.saveOrUpdate(familyInformation);
		EmployeeFamilyInformation familyInformation1 = dao.findBy(EmployeeFamilyInformation.class, familyId);
		dao.delete(familyInformation1);
	}

	@Override
	public List<HolidaysDTO> getUpcomingHolidays(String country) {
		Map<String, Object> map = securityUtils.getLoggedEmployeeDetailsSecurityContextHolder();
		Employee loggedInEmployee = (Employee) map.get("employee");
		String empCountry = loggedInEmployee.getCountry();
		if(country == null){
			country = empCountry;
		}
		List<Holidays> hollydays = employeeProfileDAOIMPL.getUpcomingHolidays(country);
		Collections.sort(hollydays, new Comparator<Holidays>() {
			@Override
			public int compare(Holidays o1, Holidays o2) {
				int k = 0;
				if (o1.getDate().isAfter(o2.getDate())) {
					k = 1;
				}
				if (o1.getDate().isBefore(o2.getDate())) {
					k = -1;
				}
				return k;

			}
		});

		List<HolidaysDTO> hdtos = employeeProfileBuilder.convertHolidaysEntityToDTO(hollydays);
		return hdtos;
	}
	
	@Override
	public List<HolidaysDTO> getOnlyUpcomingHolidays() {
		Map<String, Object> map = securityUtils.getLoggedEmployeeDetailsSecurityContextHolder();
		Employee loggedInEmployee = (Employee) map.get("employee");
		String empCountry = loggedInEmployee.getCountry();
		List<Holidays> hollydays = employeeProfileDAOIMPL.getOnlyUpcomingHolidays(empCountry);
		Collections.sort(hollydays, new Comparator<Holidays>() {
			@Override
			public int compare(Holidays o1, Holidays o2) {
				int k = 0;
				if (o1.getDate().isAfter(o2.getDate())) {
					k = 1;
				}
				if (o1.getDate().isBefore(o2.getDate())) {
					k = -1;
				}
				return k;

			}
		});

		List<HolidaysDTO> hdtos = employeeProfileBuilder.convertHolidaysEntityToDTO(hollydays);
		return hdtos;
	}

	@Override
	public Map<String, Object> getUpcomingBirthdayAnniversaries(Integer startIndex, Integer endIndex) {
		Map<String, Object> upcomingBirthdays = employeeProfileDAOIMPL.getUpcomingBirthdayAnniversaries(startIndex,
				endIndex);

		List<Object[]> list = (List<Object[]>) upcomingBirthdays.get("birthdays");
		upcomingBirthdays.put("birthdays", employeeProfileBuilder.convertBirthdayAnniversariesEntityToDTO(list));

		return upcomingBirthdays;
	}

	@Override
	public List<AnniversariesDTO> UpcomingMarriedAnniversaries() {
		List<Employee> marriedEmployees = employeeProfileDAOIMPL.getUpcomingMarriedAnniversaries();
		List<AnniversariesDTO> adtos = employeeProfileBuilder.convertMarriedAnniversariesEntityToDTO(marriedEmployees);
		return adtos;
	}

	@Override
	public List<String> getAllDesignations() {

		List<String> designations = dao.getByProperty(DesignationKras.class, "designationName");
		return designations;
	}

	@Override
	public Boolean isemployeeUsernameexist(String userName) {
		return employeeProfileDAOIMPL.isemployeeUsernameexist(userName);
	}

	@Override
	public List<EmployeeBankInformationDTO> getEmployeeBankInformation(Long employeeId) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();

		Employee emplo = dao.findByEmployeeName(Employee.class, userName);

		Employee employee = dao.findBy(Employee.class, employeeId);
		List<EmployeeBankInformationDTO> bankInformation = employeeProfileBuilder
				.convertEmployeeBankInformationDTO(employee.getBankInformations());
		return bankInformation;

	}

	@Caching(evict = { @CacheEvict(value = "otherEmployeeInformation", key = "#bankInformationDTO.employeeId"),
			@CacheEvict(value = "employees", allEntries = true) })
	@Override
	public void saveEmployeeBankDetails(EmployeeBankInformationDTO bankInformationDTO) {
		/*
		 * Employee employee = dao.findBy(Employee.class,
		 * Long.parseLong(bankInformationDTO.getEmployeeId()));
		 */
		Employee employee = dao.findBy(Employee.class, bankInformationDTO.getEmployeeId());
		EmployeeBankInformation bankInformation = new EmployeeBankInformation();
		bankInformation.setBankName(bankInformationDTO.getBankName());
		bankInformation.setBankAccountNumber(bankInformationDTO.getBankAccountNumber());
		if(bankInformationDTO.getIfscCode()!=null || !bankInformationDTO.getIfscCode().isEmpty()){
			bankInformation.setIfscCode(bankInformationDTO.getIfscCode());
		}
		/*
		 * bankInformation.setPfAccountNumber(bankInformationDTO .getPfAccountNumber());
		 * bankInformation.setPanCardAccountNumber(bankInformationDTO
		 * .getPanCardAccountNumber());
		 */
		bankInformation.setEmployee(employee);
		dao.saveOrUpdate(bankInformation);
	}

	@Override
	public EmployeeBankInformationDTO getBankInformation(Long bankId) {
		EmployeeBankInformation bankInformation = dao.findBy(EmployeeBankInformation.class, bankId);
		EmployeeBankInformationDTO informationDTO = employeeProfileBuilder
				.convertBankInformationEntityToDTO(bankInformation);
		return informationDTO;
	}

	@Caching(evict = { @CacheEvict(value = "otherEmployeeInformation", key = "#bankInformationDTO.employeeId"),
			@CacheEvict(value = "employees", allEntries = true) })
	@Override
	public void updateEmployeeBankDetails(EmployeeBankInformationDTO bankInformationDTO) {
		EmployeeBankInformation bankInformation = dao.findBy(EmployeeBankInformation.class,
				bankInformationDTO.getBankId());
		bankInformation.setBankName(bankInformationDTO.getBankName());
		bankInformation.setBankAccountNumber(bankInformationDTO.getBankAccountNumber());
		if(bankInformationDTO.getIfscCode()!=null){
			bankInformation.setIfscCode(bankInformationDTO.getIfscCode());
		}
		/*
		 * bankInformation.setPfAccountNumber(bankInformationDTO .getPfAccountNumber());
		 * bankInformation.setPanCardAccountNumber(bankInformationDTO
		 * .getPanCardAccountNumber());
		 */
		dao.saveOrUpdate(bankInformation);
	}

	@Caching(evict = { @CacheEvict(value = "otherEmployeeInformation", allEntries = true),
			@CacheEvict(value = "employees", allEntries = true) })
	@Override
	public void deleteBankAccount(Long bankId) {
		EmployeeBankInformation bankInformation = dao.findBy(EmployeeBankInformation.class, bankId);
		if (bankInformation != null) {
			bankInformation.setEmployee(null);
			dao.saveOrUpdate(bankInformation);
		}
		EmployeeBankInformation bankInformation1 = dao.findBy(EmployeeBankInformation.class, bankId);
		dao.delete(bankInformation1);
	}

	@Override
	public void deleteHoliday(Long holidayId) {
		Holidays holiday = dao.findBy(Holidays.class, holidayId);
		dao.delete(holiday);
	}

	@Override
	public void saveNewHoliday(HolidaysDTO holidaysDTO) {
		Holidays holidays = new Holidays();
		try {
			holidays.setDate(DateParser.toDate(holidaysDTO.getDate()));
		} catch (ParseException ex) {
			Logger.getLogger(EmployeeProfileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		holidays.setName(holidaysDTO.getName());
		Date holidaydate = holidays.getDate();
	    holidays.setCountry(holidaysDTO.getCountry()!=null?holidaysDTO.getCountry():null);
		
		String country="";
		if(holidaysDTO.getCountry()!=null){
		 country = holidaysDTO.getCountry();
		}
		Boolean existflag = employeeProfileDAOIMPL.isHolidayDateExist(holidaydate,country);
		if (existflag == false) {
			dao.save(holidays);
		} else {
			throw new HolidayAlredyExistException("Already Hoilday is existing For This Day");
		}
	}

	@Override
	public void updateHoliday(HolidaysDTO holidaysDTO) {
		Holidays holidays = dao.findBy(Holidays.class, holidaysDTO.getId());
		Boolean existflag = null;
		Date dbDate = holidays.getDate();
		try {
			holidays.setDate(DateParser.toDate(holidaysDTO.getDate()));
		} catch (ParseException ex) {
			Logger.getLogger(EmployeeProfileServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		holidays.setName(holidaysDTO.getName());
		holidays.setCountry(holidaysDTO.getCountry()!=null? holidaysDTO.getCountry():null);
		String country="";
		if(holidaysDTO.getCountry()!=null){
			country = holidaysDTO.getCountry();
		}
		Date holidaydate = null;
		try {
			holidaydate = DateParser.toDate(holidaysDTO.getDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (dbDate.equals(holidaydate)) {
			dao.saveOrUpdate(holidays);
		} else {
			existflag = employeeProfileDAOIMPL.isHolidayDateExistUpdate(holidaydate,country);
			if (existflag == true) {
				throw new HolidayAlredyExistException("Holiday is added for this day");
			} else {
				dao.saveOrUpdate(holidays);
			}
		}

	}

	@Override
	public HolidaysDTO getHoliday(Long holidayId) {
		Holidays holidays = dao.findBy(Holidays.class, holidayId);
		HolidaysDTO holidaysDTO = new HolidaysDTO();
		holidaysDTO.setId(holidays.getId());
		holidaysDTO.setDate(holidays.getDate().toString("dd/MM/yyyy"));
		holidaysDTO.setName(holidays.getName());
        holidaysDTO.setCountry(holidays.getCountry()!=null?holidays.getCountry():null);
		return holidaysDTO;
	}

	@Override
	public Map<String, Object> getUpcomingProvisionalPeriod(Long employeeId, Integer startIndex, Integer endIndex) {

		Employee employee = dao.findBy(Employee.class, employeeId);
		Map<String, Object> map = employeeProfileDAOIMPL.getUpcomingProvisionalPeriod(employee,
				leaveDAO.getLeaveSettings().getProbationPeriod(), startIndex, endIndex);
		List<Object[]> list = (List<Object[]>) map.get("list");

		map.put("list", employeeProfileBuilder.convertEntityToDTO(list));
		return map;
	}

	@Override
	public FinanceDTO getFinanceInformation(Long employeeId) {
		// TODO Auto-generated method stub

		// Employee employee = dao.findBy(Employee.class, employeeId);
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();

		Employee emplo = dao.findByEmployeeName(Employee.class, userName);

		Finance finance = employeeProfileDAOIMPL.getEmplopyeeFinanceInfo(employeeId);

		FinanceDTO financeInformationDTO = employeeProfileBuilder.convertFinanceEntityToDTO(finance);
		return financeInformationDTO;

	}

	@Override
	public void updateFinanceInformation(FinanceDTO financeDTO) {
		Employee employee = dao.findBy(Employee.class, financeDTO.getEmployeeId());
		if (financeDTO.getFinanceId() == null) {
			Finance finance = new Finance();
			finance.setPanCardAccountNumber(financeDTO.getPanCardAccountNumber());
			finance.setPfAccountNumber(financeDTO.getPfAccountNumber());
			finance.setAadharCardNumber(financeDTO.getAadharCardNumber());
			finance.setEmployee(employee);
			dao.save(finance);
		} else {
			Finance finance = dao.findBy(Finance.class, financeDTO.getFinanceId());
			Finance financeInformation = employeeProfileBuilder.convertFinanceDTOToEntity(finance, financeDTO);
			dao.saveOrUpdate(financeInformation);
		}

	}

	@Override
	public List<BankNameLookup> getBankNameLookups() {

		return employeeProfileDAOIMPL.getBankNameList();
	}

	@Override
	public CandidateInterviewTimelineDTO getEmployeeHistory(Long employeeId) {

		Employee employee = dao.findBy(Employee.class, employeeId);
		// Candidate
		// candidate=dao.findBy(Candidate.class,employee.getCandidateId());
		if (employee.getCandidateId() != null) {
			return jobPortalServiceImpl.timeLineDetails(employee.getCandidateId().toString());
		}
		// return candidateBuilder.createCandidateDTO(candidate);
		return null;
	}

	/* To save Qualification Category */
	@Override
	public void saveQualiCategory(QualificationLookUpDTO qualificationLookUpDTO) {

		Boolean duplication = employeeProfileDAOIMPL.checkForDuplicate(qualificationLookUpDTO);
		if (!duplication) {
			QualificationLookUp qualificationLookUp = qualificationLookUpBuilder.toEntity(qualificationLookUpDTO);
			dao.save(qualificationLookUp);
		} else {
			throw new DuplicateObservationException("The Qualification details was already added.");
		}

	}

	/* To get list of qualification category list */
	@Override
	public List<QualificationLookUpDTO> getQualiactionCategoryList() {

		List<QualificationLookUp> qualificationLookUps = null;
		qualificationLookUps = employeeProfileDAOIMPL.getQualiactionCategoryList();
		List<QualificationLookUp> qualificationLookUpsList = new ArrayList<QualificationLookUp>(qualificationLookUps);

		List<QualificationLookUpDTO> qualificationLookUpDTOs = qualificationLookUpBuilder
				.toDTOList(qualificationLookUpsList);

		return qualificationLookUpDTOs;
	}

	@Override
	public void deleteQualiCategory(Long id) {

		Boolean exsist = employeeProfileDAOIMPL.checkForUse(id);
		if (!exsist) {
			dao.delete(dao.findBy(QualificationLookUp.class, id));
		} else {
			throw new DuplicateObservationException("The Qualification details was already in use.");
		}
	}

	@Override
	public void saveNewQualification(QualificationDTO qualificationDto) {

		if (!qualificationDto.getEmpId().equals(securityUtils.getLoggedEmployeeIdforSecurityContextHolder())) {
			throw new AccessDeniedException("unauthorized user");
		}
		Qualification qualification = qualificationBuilder.toEntity(qualificationDto);
		dao.save(qualification);

	}

	@Override
	public QualificationDTO getAllQualification(Long empID) {
		return qualificationBuilder.toDto(employeeProfileDAOIMPL.getAllQualification(empID));

	}

	@Override
	public Map<String, Object> multipleSelection() {

		return qualificationLookUpBuilder.qualificationLookUpList(employeeProfileDAOIMPL.getQualiactionCategoryList());
	}

	@Override
	public void updateQualification(QualificationDTO qualificationDTO) {

		if (!qualificationDTO.getEmpId().equals(securityUtils.getLoggedEmployeeIdforSecurityContextHolder())) {
			throw new AccessDeniedException("unauthorized user");
		}

		Qualification qualification = dao.findBy(Qualification.class, qualificationDTO.getId());
		String pg = null;
		String ug = null;
		String hsc = null;

		if (qualificationDTO != null) {
			for (MultipleSelectionDTO multipleSelectionDTOList : qualificationDTO.getPgLookUp()) {
				pg = pg + "," + multipleSelectionDTOList.getId();
			}
			for (MultipleSelectionDTO multipleSelectionDTOList : qualificationDTO.getGraduationLookUp()) {
				ug = ug + "," + multipleSelectionDTOList.getId();
			}
			/*
			 * for (MultipleSelectionDTO multipleSelectionDTOList : qualificationDTO
			 * .getHscLookUp()) { hsc = hsc + "," + multipleSelectionDTOList.getId(); }
			 */
			String str = pg + ug + hsc;
			qualification.setQualificationDetails(str.replace("null", ""));
			qualification.setSscName(qualificationDTO.getSscName());
			qualification.setHscName(qualificationDTO.getHscName());
			qualification.setOthers(qualificationDTO.getOthers());
			qualification.setUpdatedBy(securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
			qualification.setUpdatedDate(new Second());
			dao.saveOrUpdate(qualification);

		}
	}

	@Override
	public Map<String, Object> getEmployeeProfileHistory(Long employeeId) {

		Map<String, List<Audit>> map = employeeProfileDAOIMPL.getEmployeeDetailAudit(employeeId);

		return employeeAuditBulider.ToEmployeeAuditDTO(map);

	}

	@Override
	public Map<String, Object> getFinanceDetailsList(Integer startIndex, Integer endIndex, String employeeName) {

		Map<String, Object> financeMap = null;

		financeMap = employeeProfileDAOIMPL.getFinanceDetailsList(startIndex, endIndex, employeeName);
		List<Finance> financeList = (List<Finance>) financeMap.get("list");
		Integer noOfRecords = (Integer) financeMap.get("size");
		Set<EmployeeFinanceDTO> employeeFinanceDTOsList = employeeProfileBuilder
				.getFinanceDetailsOfAllEmployees(financeList);
		Map<String, Object> financeListMap = new HashMap<String, Object>();
		financeListMap.put("list", employeeFinanceDTOsList);
		financeListMap.put("size", noOfRecords);

		return financeListMap;
	}

	// export finance list
	@SuppressWarnings("unchecked")
	@Override
	public ByteArrayOutputStream exportFinanceData(String employeeName) throws IOException {
		Map<String, Object> financeMap = employeeProfileDAOIMPL.getFinanceDetailsList(null, null, employeeName);
		List<Finance> financeList = (List<Finance>) financeMap.get("list");
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
		cell0.setCellValue("Employee ID");
		cell0.setCellStyle(style);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Employee Name");
		cell1.setCellStyle(style);

		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("P.F A/C No.");
		cell2.setCellStyle(style);

		Cell cell3 = row1.createCell(3);
		cell3.setCellValue("UAN");
		cell3.setCellStyle(style);

		Cell cell4 = row1.createCell(4);
		cell4.setCellValue("Pan Card No.");
		cell4.setCellStyle(style);

		Cell cell5 = row1.createCell(5);
		cell5.setCellValue("Aadhar No.");
		cell5.setCellStyle(style);

		for (Finance dto : financeList) {

			Row row = sheet.createRow(rowindex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(dto.getEmployee().getEmployeeId());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(dto.getEmployee().getEmployeeFullName());

			Cell cel2 = row.createCell(2);
			if (dto.getPfAccountNumber() == null || dto.getPfAccountNumber() == "") {
				cel2.setCellValue("N/A");
			} else {
				cel2.setCellValue(dto.getPfAccountNumber());
			}

			Cell cel3 = row.createCell(3);
			if (dto.getUaNumber() == null || dto.getUaNumber() == "") {
				cel3.setCellValue("N/A");
			} else {
				cel3.setCellValue(dto.getUaNumber());
			}

			Cell cel4 = row.createCell(4);
			if (dto.getPanCardAccountNumber() == null || dto.getPanCardAccountNumber() == "") {
				cel4.setCellValue("N/A");
			} else {
				cel4.setCellValue(dto.getPanCardAccountNumber());
			}

			Cell cel5 = row.createCell(5);
			if (dto.getAadharCardNumber() == null || dto.getAadharCardNumber() == "") {
				cel5.setCellValue("N/A");
			} else {
				cel5.setCellValue(dto.getAadharCardNumber());
			}
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

	@Override
	public void uploadEmployeeFinanceDetails(MultipartFile mpf, String financeId) {
		if (financeId != null) {

			Long id = Long.parseLong(financeId);

			Finance finance = employeeProfileDAOImpl.findBy(Finance.class, id);

			FileUploaderUtility fileUploaderUtility = new FileUploaderUtility();
			try {
				String path = fileUploaderUtility.uploadEmployeeFinanceDetails(finance, mpf, propBean);
				finance.setFinanceFilePath(path);
				employeeProfileDAOImpl.update(finance);

			} catch (IOException ex) {
			}
		}
	}

	@Override
	public void downloadFinanceFile(HttpServletResponse response, String fileName) {

		String filePath = (String) propBean.getPropData().get("FinanceDocuments");
		try {
			FileUploaderUtil util = new FileUploaderUtil();
			filePath = (String) propBean.getPropData().get("FinanceDocuments");
			util.downloadUploadedFile(response, fileName, filePath);
		} catch (Exception ex) {
			throw new FileUploaderUtilException("Exception occured while uploading a file in Legal " + ex.getMessage(),
					ex);
		}
	}

	// export Reportee list
	@SuppressWarnings("unchecked")
	@Override
	public ByteArrayOutputStream exportReporteeList(Long empID) throws IOException {

		// List<Employee> employeeList = null;

		List<Long> empIds = projectService.mangerUnderManager(empID);

		Map<String, Object> empMap = dao.getReporteeExportList(empIds);
		List<Employee> empList = (List<Employee>) empMap.get("list");
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
		cell0.setCellValue("Manager Name");
		cell0.setCellStyle(style);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Reportee Name");
		cell1.setCellStyle(style);

		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Mobile No.");
		cell2.setCellStyle(style);

		Cell cell3 = row1.createCell(3);
		cell3.setCellValue("Reportee Project Name & Allocation %");
		cell3.setCellStyle(style);

		for (Employee dto : empList) {

			Row row = sheet.createRow(rowindex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(dto.getManager().getFullName());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(dto.getEmployeeFullName());

			Cell cel2 = row.createCell(2);
			if (dto.getPhone() == null || dto.getPhone() == "") {
				cel2.setCellValue("N/A");
			} else {
				cel2.setCellValue(dto.getPhone());
			}

			Cell cel3 = row.createCell(3);
			List<AllocationDetails> allocationDetails = dao.getAllOfProperty(AllocationDetails.class, "employee", dto);

			StringBuffer buffer = new StringBuffer();
			for (AllocationDetails empDetails : allocationDetails) {
				buffer.append(empDetails.getProject().getProjectName());
				buffer.append(" - ");
				Percentage percentage = empDetails.getPercentage();
				buffer.append(Math.round(percentage.getValue()));
				buffer.append("% , ");
			}

			String stringDetails = buffer.toString();

			String modifiedString = stringDetails.replaceAll(", $", ".");

			if (modifiedString == null || modifiedString.isEmpty()) {
				cel3.setCellValue("N/A");
			} else {
				cel3.setCellValue(modifiedString);

			}
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
		}
		workbook.write(bos);
		bos.flush();
		bos.close();
		return bos;

	}

	@Override
	public Map<String, Object> getCountryLookUps() {

		Map<String, Object> countryLookUp = employeeProfileDAOIMPL.getCountryLookUps();

		return countryLookUp;
	}

	@Override
	public List<VisaLookUpDTO> getCountryChangeList(Long id) {

		return visaLookUpBuilder.getDTOList(employeeProfileDAOIMPL.getCountryChangeList(id));
	}

	@Override
	public Long saveVisaDetails(VisaDetailDTO visaDetailsDTO) {
		Date fromDate = null;
		Date toDate = null;

		try {
			fromDate = DateParser.toDate(visaDetailsDTO.getDateOfIssue());
			toDate = DateParser.toDate(visaDetailsDTO.getDateOfExpire());
		} catch (ParseException ex) {
			java.util.logging.Logger.getLogger(AssetManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		DateRange dateRange = new DateRange(fromDate, toDate);

		
		Boolean duplication = employeeProfileDAOIMPL.checkForDuplicateVisa(visaDetailsDTO, dateRange);
		if (!duplication) {
			VisaDetails visaDetails = visaDetailsBuilder.toEntity(visaDetailsDTO);
			Long id= (Long) dao.save(visaDetails);
			return id;
		} else {
			throw new DuplicateObservationException("The visa details was already added.");
		}

	}

	@Override
	public List<VisaDetailDTO> getEmployeeVisaDetailsList(Long loggedInEmpId) {
		
		
		
		
		
		return visaDetailsBuilder.toDTOList(employeeProfileDAOIMPL.getEmployeeVisaDetailsList(loggedInEmpId));
	}

	@Override
	public VisaDetailDTO getVisaDetails(Long id) {
		VisaDetails visaDetails = dao.findBy(VisaDetails.class, id);
		VisaDetailDTO visaDetailDTO = visaDetailsBuilder.toDTO(visaDetails);
		return visaDetailDTO;
	}

	@Override
	public void updateVisaDetailsToEmployee(VisaDetailDTO editVisaDetailsDTO) {

		Date fromDate = null;
		Date toDate = null;

		try {
			fromDate = DateParser.toDate(editVisaDetailsDTO.getDateOfIssue());
			toDate = DateParser.toDate(editVisaDetailsDTO.getDateOfExpire());
		} catch (ParseException ex) {
			java.util.logging.Logger.getLogger(AssetManagementServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
		}
		DateRange dateRange = new DateRange(fromDate, toDate);

		Boolean duplication = employeeProfileDAOIMPL.checkForDuplicateVisaForEdit(editVisaDetailsDTO, dateRange);
		if (!duplication) {
			VisaDetails visaDetails = visaDetailsBuilder.toEditEntity(editVisaDetailsDTO);
			employeeProfileDAOIMPL.update(visaDetails);
		} else {
			throw new DuplicateObservationException("The visa details was already added.");
		}

	}

	@Override
	public void deleteVisaDetail(Long visaID) {

		VisaDetails visaDetails = dao.findBy(VisaDetails.class, visaID);
		if (visaDetails != null) {
			visaDetails.setEmployee(null);
			dao.saveOrUpdate(visaDetails);
		}
		VisaDetails visaDetails2 = dao.findBy(VisaDetails.class, visaID);
		dao.delete(visaDetails2);

	}

}
