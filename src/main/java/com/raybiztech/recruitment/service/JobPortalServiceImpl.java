package com.raybiztech.recruitment.service;

import com.raybiztech.appraisalmanagement.builder.AppraisalFormBuilder;
import com.raybiztech.appraisalmanagement.business.AppraisalForm;
import com.raybiztech.appraisalmanagement.business.Designation;
import com.raybiztech.appraisalmanagement.dao.AppraisalDao;
import com.raybiztech.appraisalmanagement.dto.AppraisalFormListDto;
import com.raybiztech.appraisalmanagement.dto.DesignationDto;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.exception.LockAcquisitionException;
import org.hornetq.utils.json.JSONException;
import org.hornetq.utils.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.appraisals.alerts.builder.AlertBuilder;
import com.raybiztech.appraisals.alerts.business.Alert;
import com.raybiztech.appraisals.builder.CategoryBuilder;
import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Category;
import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.EmployeeSkillLookUp;
import com.raybiztech.appraisals.business.Finance;
import com.raybiztech.appraisals.business.Skill;
import com.raybiztech.appraisals.business.Status;
import com.raybiztech.appraisals.business.TimeSlot;
import com.raybiztech.appraisals.business.VisaDetails;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.CategoryDTO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.dto.EmployeeSkillLookUpDTO;
import com.raybiztech.appraisals.dto.ReportiesHierarchyDTO;
import com.raybiztech.appraisals.dto.SearchEmpDetailsDTO;
import com.raybiztech.appraisals.dto.SkillDTO;
import com.raybiztech.appraisals.dto.TimeSlotDTO;
import com.raybiztech.appraisals.exception.FileUploaderUtilException;
import com.raybiztech.appraisals.exceptions.DuplicateActiveCycleException;
import com.raybiztech.appraisals.observation.builder.ObservationBuilder;
import com.raybiztech.appraisals.observation.business.Observation;
import com.raybiztech.appraisals.observation.dao.ObservationDAO;
import com.raybiztech.appraisals.observation.dto.ObservationDTO;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.appraisals.utils.FileUploaderUtil;
import com.raybiztech.assetmanagement.business.VendorDetails;
import com.raybiztech.assetmanagement.dto.VendorDto;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.employeeprofile.builder.EmployeeProfileBuilder;
import com.raybiztech.employeeprofile.dto.EmployeeBankInformationDTO;
import com.raybiztech.employeeprofile.dto.EmployeeFamilyInformationDTO;
import com.raybiztech.mail.sender.MessageSender;
import com.raybiztech.mailtemplates.util.RecuritmentMailConfiguration;
import com.raybiztech.projectmanagement.builder.AuditBuilder;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.dao.ResourceManagementDAO;
import com.raybiztech.projectmanagement.invoice.dto.CountryLookUpDTO;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.recruitment.builder.CandidateBuilder;
import com.raybiztech.recruitment.builder.CandidateInterviewCycleBuilder;
import com.raybiztech.recruitment.builder.CandidateScheduleInterviewBuilder;
import com.raybiztech.recruitment.builder.JobVacancyBuilder;
import com.raybiztech.recruitment.builder.NewJoineeBuilder;
import com.raybiztech.recruitment.builder.PanelBuilder;
import com.raybiztech.recruitment.builder.ScheduleBuilder;
import com.raybiztech.recruitment.builder.SkillLookUpBuilder;
import com.raybiztech.recruitment.business.BloodGroup;
import com.raybiztech.recruitment.business.Candidate;
import com.raybiztech.recruitment.business.CandidateInterviewCycle;
import com.raybiztech.recruitment.business.Companies;
import com.raybiztech.recruitment.business.Department;
import com.raybiztech.recruitment.business.EmploymentType;
import com.raybiztech.recruitment.business.Gender;
import com.raybiztech.recruitment.business.Interview;
import com.raybiztech.recruitment.business.JobType;
import com.raybiztech.recruitment.business.JobVacancy;
import com.raybiztech.recruitment.business.MaritalStatus;
import com.raybiztech.recruitment.business.NewJoinee;
import com.raybiztech.recruitment.business.Panel;
import com.raybiztech.recruitment.business.QualificationCategory;
import com.raybiztech.recruitment.business.Relations;
import com.raybiztech.recruitment.business.Schedule;
import com.raybiztech.recruitment.business.Technology;
import com.raybiztech.recruitment.chart.StatusChart;
import com.raybiztech.recruitment.constants.StringConstant;
import com.raybiztech.recruitment.controller.ImageDTO;
import com.raybiztech.recruitment.controller.PassportBackPageDTO;
import com.raybiztech.recruitment.controller.PassportDTO;
import com.raybiztech.recruitment.controller.VisaImageDTO;
import com.raybiztech.recruitment.dao.JobPortalDAO;
import com.raybiztech.recruitment.dto.CandidateDTO;
import com.raybiztech.recruitment.dto.CandidateInterviewCycleDTO;
import com.raybiztech.recruitment.dto.CandidateInterviewTimelineDTO;
import com.raybiztech.recruitment.dto.CandidateScheduleDto;
import com.raybiztech.recruitment.dto.CandidateStatus;
import com.raybiztech.recruitment.dto.CompaniesDTO;
import com.raybiztech.recruitment.dto.CompanyDTO;
import com.raybiztech.recruitment.dto.DepartmentDTO;
import com.raybiztech.recruitment.dto.InterviewStatusReportDTO;
import com.raybiztech.recruitment.dto.JobVacancyDTO;
import com.raybiztech.recruitment.dto.NewJoineeDTO;
import com.raybiztech.recruitment.dto.PanelDTO;
import com.raybiztech.recruitment.dto.ScheduledCadidateDTO;
import com.raybiztech.recruitment.dto.SearchQueryParams;
import com.raybiztech.recruitment.dto.SkillLookUpDTO;
import com.raybiztech.recruitment.exception.CandidateCantBeDeletedException;
import com.raybiztech.recruitment.exception.DuplicateScheduleException;
import com.raybiztech.recruitment.exception.JobPortalException;
import com.raybiztech.recruitment.exception.NoJobVacancyException;
import com.raybiztech.recruitment.utils.CandidateInterviewCycleUtil;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.recruitment.utils.FileUploaderUtility;
import com.raybiztech.recruitment.utils.MailSenderUtility;
import com.raybiztech.recruitment.utils.ReadExcelFile;
import com.raybiztech.recruitment.utils.WriteToExcelFile;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.rolefeature.business.Role;
import com.raybiztech.rolefeature.business.User;
import com.raybiztech.separation.business.Separation;
import com.raybiztech.separation.dao.SeparationDao;
import com.raybiztech.supportmanagement.service.SupportManagementServiceImpl;
import com.raybiztech.recruitment.dto.EmploymentTypeDTO;
import com.raybiztech.recruitment.dto.GenderDTO;
import com.raybiztech.recruitment.dto.MaritalStatusDTO;
import com.raybiztech.recruitment.dto.BloodGroupDTO;
import com.raybiztech.recruitment.dto.QualificationCategoryDTO;
import com.raybiztech.recruitment.dto.RelationsDTO;
import com.raybiztech.recruitment.dto.JobTypeDTO;

@Service("jobPortalServiceImpl")
@Transactional
public class JobPortalServiceImpl implements JobPortalService, Cloneable {

	@Autowired
	DAO dao;
	@Autowired
	CandidateScheduleInterviewBuilder candidateScheduleInterviewBuilder;
	@Autowired
	JobPortalDAO jobPortalDAOImpl;
	@Autowired
	EmployeeBuilder employeeBuilder;
	@Autowired
	CandidateBuilder candidateBuilder;
	@Autowired
	ScheduleBuilder scheduleBuilder;
	@Autowired
	SkillLookUpBuilder skillLookUpBuilder;
	@Autowired
	PanelBuilder panelBuilder;
	@Autowired
	PropBean propBean;
	@Autowired
	CategoryBuilder categoryBuilder;
	@Autowired
	CandidateInterviewCycleBuilder candidateInterviewCycleBuilder;
	@Autowired
	MessageSender messageSender;

	@Autowired
	AppraisalDao appraisalDao;

	@Autowired
	MailSenderUtility mailSenderUtility;

	@Autowired
	EmployeeProfileBuilder employeeProfileBuilder;
	@Autowired
	ObservationBuilder observationBuilder;
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	SeparationDao separationDaoImpl;
	org.apache.log4j.Logger logger1 = org.apache.log4j.Logger
			.getLogger(JobPortalServiceImpl.class);
	@Autowired
	ReadExcelFile readExcelFile;
	@Autowired
	WriteToExcelFile writeToExcelFile;
	@Autowired
	NewJoineeBuilder joineeBuilder;
	@Autowired
	CandidateInterviewCycleUtil candidateInterviewCycleUtil;
	@Autowired
	AlertBuilder alertBuilder;
	@Autowired
	JobVacancyBuilder jobVacancyBuilder;

	@Autowired
	ProjectService projectService;

	@Autowired
	RecuritmentMailConfiguration recuritmentMailConfiguration;

	@Autowired
	ObservationDAO observationDao;
	@Autowired
	ResourceManagementDAO resourceManagementDAO;

	@Autowired
	AuditBuilder auditBuilder;

	@Autowired
	AppraisalFormBuilder appraisalFormBuilder;

	@Override
	public Long addNewCandidadate(CandidateScheduleDto candidateScheduleDto)
			throws JobPortalException {
		Candidate candidate = candidateScheduleInterviewBuilder
				.createCandidateEntity(candidateScheduleDto);
		if (candidateScheduleDto.getAppliedFor() != null) {
			JobVacancy jobVacancy = dao.findBy(JobVacancy.class,
					candidateScheduleDto.getAppliedFor().getId());
			candidate.setAppliedFor(jobVacancy);
			candidate.setAppliedForLookUp(jobVacancy.getPositionVacant());
		}

		logger1.warn("company" + candidate.getCurrentEmployer());

		if (candidate.getCurrentEmployer() != null) {
			logger1.warn("in flagif");
			Boolean companyFlag = jobPortalDAOImpl.isCompanyExits(candidate
					.getCurrentEmployer());
			logger1.warn("flag" + companyFlag);
			if (!companyFlag) {
				Companies company = new Companies();
				company.setCompanyName(candidate.getCurrentEmployer());
				dao.save(company);
			}
		}
		System.out.println("candidate Adhar" +candidate.getAdhar());
		System.out.println("Candidate PAN"+candidate.getPan());
		System.out.println("LinkedIn Id "+candidate.getLinkedin());
		Long id = (Long) dao.save(candidate);
		System.out.println("Successfully save"+id);
		return id;

	}

	@Override
	public List<EmployeeDTO> getAllEmployeeData() {
		List<Employee> employees = new ArrayList<Employee>();
		List<Panel> panelList = dao.get(Panel.class);
		for (Panel panel : panelList) {
			employees.add(panel.getEmployee());
		}
		return employeeBuilder.getemployeeDTOList(employees);
	}

	@Override
	public List<CandidateDTO> getAllCandidate() {
		List<Candidate> candidateList = dao.get(Candidate.class);
		return candidateBuilder.createCandidateDTOList(candidateList);
	}

	/*
	 * @Override public List<ScheduledCadidateDTO> searchCadidates(String skill,
	 * String fromDate, String toDate) { List<Schedule> candidates =
	 * jobPortalDAOImpl.searchCadidates(skill, fromDate, toDate); return
	 * scheduleBuilder.convertScheduleDTO(candidates); // return
	 * scheduleBuilder.convertScheduleDTO(candidates, skill); }
	 */

	@Override
	public List<SkillLookUpDTO> getAllSkillsLookUp() {
		return skillLookUpBuilder.createSkillLookUpDTOList(jobPortalDAOImpl
				.getAllSkillsLookUp());
	}

	@Override
	public void updateScheduleCandidadate(
			ScheduledCadidateDTO scheduledCadidateDTO) {

		Schedule schedule = scheduleBuilder
				.createUpdateScheduleEntity(scheduledCadidateDTO);
		dao.saveOrUpdate(schedule);
		CandidateInterviewCycle interviewCycle = new CandidateInterviewCycle();
		interviewCycle.setCandidate(dao.findBy(Candidate.class,
				scheduledCadidateDTO.getCandidateid()));
		try {
			if (scheduledCadidateDTO.getInterviewDate().contains(" ")) {
				interviewCycle.setInterviewDate(DateParser
						.toDateOtherFormat(scheduledCadidateDTO
								.getInterviewDate()));
			} else {
				interviewCycle.setInterviewDate(DateParser
						.toDate(scheduledCadidateDTO.getInterviewDate()));
			}
		} catch (ParseException ex) {
			Logger.getLogger(JobPortalServiceImpl.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		interviewCycle.setInterviewRound(Long.valueOf(scheduledCadidateDTO
				.getRound()));
		interviewCycle.setInterviewStatus(scheduledCadidateDTO
				.getInterviewStatus());
		interviewCycle
				.setInterviewTime(scheduledCadidateDTO.getInterviewTime());
		interviewCycle.setInterviewComments(scheduledCadidateDTO.getComments());
		String interviewersNames = "";
		for (EmployeeDTO employeeDTO : scheduledCadidateDTO.getEmpoyeeData()) {

			interviewersNames += employeeDTO.getFirstName() + " "
					+ employeeDTO.getLastName() + ",";
		}
		interviewCycle.setInterviewers(interviewersNames.substring(0,
				interviewersNames.length() - 1));
		dao.save(interviewCycle);
	}

	@Override
	public Map<String, Object> getAllCadidates(Integer startIndex,
			Integer endIndex) {
		Map<String, Object> candidatesMap = jobPortalDAOImpl
				.getAllnonScheduledCadidates(startIndex, endIndex);
		List<Candidate> candidateList = (List) candidatesMap.get("list");
		Integer noOfRecords = (Integer) candidatesMap.get("size");
		List<CandidateDTO> candidateDTOList = candidateBuilder
				.convertCandidateListToDTOList(candidateList);
		Map<String, Object> candidateDTOMap = new HashMap<String, Object>();
		candidateDTOMap.put("list", candidateDTOList);
		candidateDTOMap.put("size", noOfRecords);
		return candidateDTOMap;
	}

	/**
	 *
	 * @param startIndex
	 * @param endIndex
	 * @param searchStr
	 * @return scheduled candidates list based on search query
	 */
	@Override
	public Map<String, Object> searchScheduledCandidate(Integer startIndex,
			Integer endIndex, String searchStr) {
		Employee loggedEmployee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		String country = loggedEmployee.getCountry();
		Map<String, Object> candidatesMap = jobPortalDAOImpl
				.getScheduledCadidatesBySearch(startIndex, endIndex, searchStr,country);
		List<Candidate> candidateList = (List) candidatesMap.get("list");
		Integer noOfRecords = (Integer) candidatesMap.get("size");
		List<CandidateDTO> candidateDTOList = candidateBuilder
				.convertCandidateListToDTOList(candidateList);
		Map<String, Object> candidateDTOMap = new HashMap<String, Object>();
		candidateDTOMap.put("list", candidateDTOList);
		candidateDTOMap.put("size", noOfRecords);
		return candidateDTOMap;
	}

	@Override
	public CandidateScheduleDto editCandidate(String candidateId) {
		Candidate candidate = dao.findBy(Candidate.class,
				Long.parseLong(candidateId));

		return candidateBuilder.editCandidateDTO(candidate);

	}

	@Override
	public void deleteCandidate(String candidateId) {
		Candidate candidate = dao.findBy(Candidate.class,
				Long.parseLong(candidateId));
		if (candidate.getCandidateInterviewStatus() == Status.REJECTED
				|| candidate.getCandidateInterviewStatus() == Status.NEW) {
			candidate.setStatus("0");
			dao.saveOrUpdate(candidate);
		} else {
			throw new CandidateCantBeDeletedException();
		}
		// dao.delete(candidate);
	}

	@Override
	public void updateCandidadate(CandidateScheduleDto candidateScheduleDto) {

		Candidate candidate = dao.findBy(Candidate.class,
				candidateScheduleDto.getCandidateId());
		Candidate updatedCandidate = candidateScheduleInterviewBuilder
				.editCandidateEntity(candidate, candidateScheduleDto);

		logger1.warn("company" + candidate.getCurrentEmployer());

		if (candidate.getCurrentEmployer() != null) {
			logger1.warn("in flagif");
			Boolean companyFlag = jobPortalDAOImpl.isCompanyExits(candidate
					.getCurrentEmployer());
			logger1.warn("flag" + companyFlag);
			if (!companyFlag) {
				logger1.warn("in company");
				Companies company = new Companies();
				company.setCompanyName(candidate.getCurrentEmployer());
				dao.save(company);
			}
		}
		dao.saveOrUpdate(updatedCandidate);
	}

	@Override
	public void scheduleInterview(CandidateScheduleDto candidateScheduleDto) {
		Candidate candidate = dao.findBy(Candidate.class,
				candidateScheduleDto.getCandidateId());
		Integer pendingInterview = jobPortalDAOImpl.pendingInterviewList(
				candidateScheduleDto.getCandidateId().toString()).size();
		Schedule schedule=null;
		if (pendingInterview > 0) {
			throw new DuplicateScheduleException();
		} else {
			try {
				 schedule = candidateScheduleInterviewBuilder
						.createScheduleForCandidateEntity(candidate,
								candidateScheduleDto);
				Serializable id = dao.save(schedule);

				dao.save(candidateInterviewCycleUtil
						.generateCandidateInterviewCycle(candidate,
								candidateScheduleDto));
				Schedule schedule1 = dao.findBy(Schedule.class, id);
				candidate.setCandidateInterviewStatus(Status.IN_PROCESS);
				// SOMETIMES INTERVIEW IS NOT UPDATING IN CANDIDATE SO WE ARE
				// SETTING MANUALLY
				candidate.setInterview(schedule1.getInterview());
				// candidate.setUpdatedDate(new Second());
				dao.saveOrUpdate(candidate);

			} catch (Exception e) {
				if (e instanceof LockAcquisitionException) {
					try {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
						 schedule = candidateScheduleInterviewBuilder
								.createScheduleForCandidateEntity(candidate,
										candidateScheduleDto);

						Serializable id = dao.save(schedule);
						dao.save(candidateInterviewCycleUtil
								.generateCandidateInterviewCycle(candidate,
										candidateScheduleDto));

		                System.out.println("outside:" + schedule.getScheduleDate());
						Schedule schedule1 = dao.findBy(Schedule.class, id);
						System.out.println("ij else:" + schedule1.getScheduleTime());
						candidate
								.setCandidateInterviewStatus(Status.IN_PROCESS);
						// SOMETIMES INTERVIEW IS NOT UPDATING IN CANDIDATE SO
						// WE ARE SETTING MANUALLY
						candidate.setInterview(schedule1.getInterview());

						dao.saveOrUpdate(candidate);

					} catch (LockAcquisitionException ex) {
						logger1.warn("LockAcquisitionException ex");
					}
				}

			}
			// Schedule schedule = candidateScheduleInterviewBuilder
			// .createScheduleForCandidateEntity(candidate,
			// candidateScheduleDto);
			//
			// Serializable id=dao.save(schedule);
			//
			// dao.save(candidateInterviewCycleUtil
			// .generateCandidateInterviewCycle(candidate,
			// candidateScheduleDto));
			// Schedule schedule1 = dao.findBy(Schedule.class, id);
			// if(id!=null){
			// candidate.setCandidateInterviewStatus(Status.IN_PROCESS);
			// //SOMETIMES INTERVIEW IS NOT UPDATING IN CANDIDATE SO WE ARE
			// SETTING MANUALLY
			// candidate.setInterview(schedule1.getInterview());
			//
			// dao.saveOrUpdate(candidate);
			// }
		}

        System.out.println("here:" + schedule.getScheduleDate());
		String link = null;
		if(candidateScheduleDto.getContactDetails()!=null){
		 link = candidateScheduleDto.getContactDetails();
		}
        String contactPerson = "Recruitment Team";
		String contactNumber = "+91 4023118011"; String location = "https://goo.gl/maps/EyA3NVxkd5zi1PXcA"; String likendIn = "https://in.linkedin.com/company/rbtoriginal";
		String template =""; 
		String to="";
		String templateParams ="";
		if(candidate.getCountry().getName().equalsIgnoreCase("INDIA")){
		  to =91+candidate.getMobile();
		}
		else if(candidate.getCountry().getName().equalsIgnoreCase("USA")){
			  to =1+candidate.getMobile();
			}
		else if(candidate.getCountry().getName().equalsIgnoreCase("CANADA")){
			  to =1+candidate.getMobile();
			}
		else if(candidate.getCountry().getName().equalsIgnoreCase("PHILIPPINES")){
			  to =63+candidate.getMobile();
			}
		else if(candidate.getCountry().getName().equalsIgnoreCase("AUSTRALIA")){
			  to =61+candidate.getMobile();
			}
		if(candidate.getNotifications().equalsIgnoreCase("Yes") && candidateScheduleDto.getSendMessageToCandidate().toString().equalsIgnoreCase("true")){
			if(schedule.getInterview().getInterviewType().toString().equalsIgnoreCase("FACE_TO_FACE") || schedule.getInterview().getInterviewType().toString().equalsIgnoreCase("SYSTEM")){
				if(Integer.parseInt(candidate.getInterview().getRound()) == 1){
					 templateParams ='\"' + candidate.getFullName() + '\"' + ',' + '\"' + schedule.getScheduleDate() + '\"'+ ','+ '\"' + schedule.getScheduleTime() + '\"' +','+'\"'+ contactPerson + '\"'+
		     				','+ '\"' + contactNumber + '\"' + ',' + '\"'+ location +'\"' +','+'\"'+ likendIn +'\"';
					 template="test_candidateinterview";
					sendWhatsappScheduleInterviewNotificationtoCandidate(to,template,templateParams);
				}
				else{
					 template="candidateselected_round1_n";
		   	         templateParams = '\"' + candidate.getFullName() + '\"' + ',' + '\"' + candidate.getAppliedForLookUp() + '\"'+ ','+ '\"' + schedule.getScheduleDate() + '\"' +','+'\"'+ schedule.getScheduleTime() + '\"'+
		   					','+ '\"' + contactPerson + '\"' + ',' + '\"'+ contactNumber +'\"' +','+'\"'+ location +'\"';
					sendWhatsappScheduleInterviewNotificationtoCandidate(to,template,templateParams);
				}
			}else{
				templateParams ='\"' + candidate.getFullName() + '\"' + ',' + '\"' + schedule.getScheduleDate() + '\"'+ ','+ '\"' + schedule.getScheduleTime() + '\"' +
        				',' +'\"' + contactPerson + '\"' + ',' + '\"'+ contactNumber +'\"' +',' + '\"'+ schedule.getInterview().getInterviewType()+'\"'+',' +'\"'+ link+'\"'+','+'\"'+ likendIn +'\"';
        	   template ="scheduling_onlineinterview";
				sendWhatsappScheduleInterviewNotificationtoCandidate(to,template,templateParams);
			}
		}
		if(candidate.getNotifications().equalsIgnoreCase("Yes") && candidateScheduleDto.getSendMessageToInterviewer().toString().equalsIgnoreCase("true")){
			Employee emp =dao.findBy(Employee.class, candidateScheduleDto.getInterviewerId());
			String empContact="";
			if(emp.getCountry().equalsIgnoreCase("INDIA")){
				empContact =91+emp.getPhone();
			}
			if(emp.getCountry().equalsIgnoreCase("USA")){
				empContact =1+emp.getPhone();
				}
			if(emp.getCountry().equalsIgnoreCase("CANADA")){
				empContact =1+emp.getPhone();
				}
			if(emp.getCountry().equalsIgnoreCase("PHILIPPINES")){
				empContact =63+emp.getPhone();
				}
			if(emp.getCountry().equalsIgnoreCase("AUSTRALIA")){
				  empContact =61+emp.getPhone();
				}
			 templateParams = '\"' + emp.getFullName() + '\"' + ',' + '\"' + candidate.getFullName() + '\"'+ ','+ '\"' + candidate.getAppliedForLookUp()+ '\"' +','+'\"'+ schedule.getScheduleDate() + '\"'+
					','+ '\"' + schedule.getScheduleTime() + '\"';
			 template = "f2f_interview_interviewer_n";
			sendWhatsappScheduleInterviewNotificationtoCandidate(empContact,template,templateParams);
		}
		 sendScheduleMailToCandidate(candidateScheduleDto);
		

	}

	@Override
	public void addEmployeeToPanel(String employeeEmailId, String departmentId) {
		Employee employee = dao.findByEmployeeMailId(Employee.class,
				employeeEmailId);
		Panel panel = new Panel();
		panel.setDeptId(departmentId);
		panel.setEmployee(employee);
		dao.saveOrUpdate(panel);
	}

	@Override
	public List<PanelDTO> panelDepartmentEmployeeData(String departmentId) {
		List<Panel> panels = jobPortalDAOImpl
				.panelDepartmentEmployeeData(departmentId);
		return panelBuilder.convertPanelListToPanelDTOList(panels);
	}

	@Override
	public void deleteEmployeeToPanel(String panelId) {
		Panel panel = dao.findBy(Panel.class, Long.parseLong(panelId));
		dao.delete(panel);
	}

	// @Cacheable(value = "otherEmployeeInformation", key = "#employeeId")
	@Override
	public EmployeeDTO getEmployee(Long employeeId) {
		Employee employee = dao.findBy(Employee.class, employeeId);
		EmployeeDTO employeeDTO = employeeBuilder.createEmployeeDTO(employee);
		Set<SkillDTO> skills = employeeBuilder.convertSkillDTO(employee
				.getEmployeeSkills());
		employeeDTO.setSkillList(skills);

		employeeDTO.setImageData(getImageByteData(employee.getProfilePicPath()));
	/*	System.out.println("imagedata:" + getImage(employeeDTO.getId()).getImageData());*/
		/*if (employee.getPassportFrontPagePath() != null) {
			employeeDTO.setPassportFrontPage(getPassportFrontImage(
					employeeDTO.getId()).getPassportFrontPage());
		}
		if (employee.getPassportBackPagePath() != null) {
			employeeDTO.setPassportBackPage(getPassportBackImage(
					employeeDTO.getId()).getPassportBackPage());
		}*/
      //  employeeDTO.setImageData(getImageByteData(employee.getProfilePicPath()));
		List<EmployeeFamilyInformationDTO> informationList = employeeProfileBuilder
				.convertEmployeeFamilyInformationDTO(employee
						.getEmployeeFamilyDetails());
		employeeDTO.setInformationList(informationList);
		List<EmployeeBankInformationDTO> bankInformationList = employeeProfileBuilder
				.convertEmployeeBankInformationDTO(employee
						.getBankInformations());
		employeeDTO.setBankInformationList(bankInformationList);
		Employee hrAssociate = employee.getHrAssociate();
		if (hrAssociate != null) {
			EmployeeDTO dto = new EmployeeDTO();
			dto.setId(hrAssociate.getEmployeeId());
			dto.setFirstName(hrAssociate.getFirstName());
			dto.setLastName(hrAssociate.getLastName());
			dto.setFullName(hrAssociate.getFullName());
			employeeDTO.setHrAssociate(dto);
		}
		// new field project manager
		EmployeeDTO projectManager = new EmployeeDTO();
		if (employee.getProjectManager() != null) {
			projectManager.setId(employee.getProjectManager().getEmployeeId());
			projectManager.setFirstName(employee.getProjectManager()
					.getFirstName());
			projectManager.setLastName(employee.getProjectManager()
					.getLastName());
			employeeDTO.setProjectManager(projectManager);
		}
		return employeeDTO;
	}

	@Override
	public EmployeeDTO getEmployeeDataFormSearch(Long employeeId,
			HttpServletResponse res) {
		EmployeeDTO employeeDTO = null;
		Employee loggedEmployee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		Permission empView = dao.checkForPermission(
				"Employee Directory-Options", loggedEmployee);
		Permission empGenralInfo = dao.checkForPermission("General All",
				loggedEmployee);
		Employee employee = dao.findBy(Employee.class, employeeId);
		if (employee.getStatusName().equalsIgnoreCase("Inactive")
				&& empView.getView()
				|| employee.getStatusName().equalsIgnoreCase("active")) {

			if (!empGenralInfo.getView()) {
				employeeDTO = employeeBuilder.createEmployeeListDto(employee);
				employeeDTO.setImageData(getImage(employeeDTO.getId())
						.getImageData());
				String orignalPath = employee.getProfilePicPath();
				String splitedPath = orignalPath.substring(orignalPath
						.lastIndexOf("/") + 1);
				employeeDTO.setProfilePicPath("../profilepics/" + splitedPath);
				/*
				 * if(employee.getPassportFrontPagePath()!=null) {
				 * employeeDTO.setPassportFrontPage
				 * (getPassportFrontImage(employeeDTO
				 * .getId()).getPassportFrontPage()); String
				 * passportOrignalFrontPath =
				 * employee.getPassportFrontPagePath(); String
				 * passportFrontsplitedPath =
				 * passportOrignalFrontPath.substring(
				 * passportOrignalFrontPath.lastIndexOf("/") + 1);
				 * employeeDTO.setPassportFrontPagePath("../passportFrontpics/"
				 * + passportFrontsplitedPath); }
				 * if(employee.getPassportBackPagePath()!=null) {
				 * employeeDTO.setPassportBackPage
				 * (getPassportBackImage(employeeDTO
				 * .getId()).getPassportBackPage()); String
				 * passportOrignalBackPath = employee.getPassportBackPagePath();
				 * String passportBacksplitedPath =
				 * passportOrignalBackPath.substring
				 * (passportOrignalBackPath.lastIndexOf("/") + 1);
				 * employeeDTO.setPassportBackPagePath("../passportBackpics/" +
				 * passportBacksplitedPath); }
				 */

			} else {

				employeeDTO = employeeBuilder.createEmployeeDTO(employee);

				employeeDTO.setImageData(getImage(employeeDTO.getId())
						.getImageData());
				/*
				 * if(employee.getPassportFrontPagePath()!=null) {
				 * employeeDTO.setPassportFrontPage
				 * (getPassportFrontImage(employeeDTO
				 * .getId()).getPassportFrontPage()); }
				 * if(employee.getPassportBackPagePath()!=null) {
				 * employeeDTO.setPassportBackPage
				 * (getPassportBackImage(employeeDTO
				 * .getId()).getPassportBackPage()); }
				 */
				List<EmployeeFamilyInformationDTO> informationList = employeeProfileBuilder
						.convertEmployeeFamilyInformationDTO(employee
								.getEmployeeFamilyDetails());
				employeeDTO.setInformationList(informationList);
				// employeeDTO.setStatusName(null);
				employeeDTO.setToken(null);
				employeeDTO.setUnderNotice(null);
				employeeDTO.setUnderNoticeDate(null);
			}
		} else {
			try {
				res.sendError(res.SC_FORBIDDEN);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return employeeDTO;
	}

	@Override
	public void updateLoggedInEmployee(EmployeeDTO employeeDTO) {
		Employee employee = employeeBuilder.createEmployeeEntity1(employeeDTO);

		dao.saveOrUpdate(employee);
	}

	@Override
	public List<SearchEmpDetailsDTO> getAllProfileEmployeesData(String searchStr) {
		List<Employee> employees = jobPortalDAOImpl
				.getSearchEmployeeData(searchStr);
		List<SearchEmpDetailsDTO> dTOs = employeeBuilder
				.getemployeeDTOListForSearch(employees);
		return dTOs;
	}

	@Override
	public List<SearchEmpDetailsDTO> getAllProfileEmployeesData() {
		List<Employee> employees = jobPortalDAOImpl.getActiveEmployeeData();
		List<SearchEmpDetailsDTO> dTOs = employeeBuilder
				.getemployeeDTOListForSearch(employees);
		return dTOs;
	}

	@Override
	public void deleteEmployee(Long employeeId) {

		Employee employee = dao.findBy(Employee.class, employeeId);
		dao.delete(employee);
	}

	@Override
	public List<EmployeeDTO> getAllReportingManagersData() {

		return employeeBuilder.getManagerDTOList(dao
				.findByManagerName(Employee.class));

	}

	@Caching(evict = { @CacheEvict(value = "employees", allEntries = true),
			@CacheEvict(value = "employeeAllocation", allEntries = true) })
	@Override
	public void addNewEmployee(EmployeeDTO employeeDTO) {
		
		Employee lastEmpId= dao.getlastEmployeeId();
		Long empId = lastEmpId.getEmployeeId();
		//System.out.println("lastempId:"+lastEmpId.getEmployeeId());
		
		Employee employeeHr = employeeBuilder
				.createNewEmployeeEntity(employeeDTO);
		employeeHr.setEmployeeId(empId+1);
		//System.out.println("after setting lastempId:"+(empId+1));

		// here new Field is added
		if (employeeDTO.getHrAssociate() != null) {
			Employee hrAssociate = dao.findBy(Employee.class, employeeDTO
					.getHrAssociate().getId());
			employeeHr.setHrAssociate(hrAssociate);
		}
		// new project manager field
		Employee projectManager = new Employee();
		if (employeeDTO.getProjectManager() != null) {
			projectManager.setEmployeeId(employeeDTO.getProjectManager()
					.getId());
			employeeHr.setProjectManager(projectManager);
		}

		Serializable employeeId = dao.save(employeeHr);
		if (employeeDTO.getRole().equalsIgnoreCase("admin")) {
			User user = new User();
			user.setUserid((Long) employeeId);
			Set<Role> roles = new HashSet<Role>();
			Role adminRole = dao.findBy(Role.class, 1l);
			roles.add(adminRole);
			user.setRole(roles);
			dao.save(user);
		} else if (employeeDTO.getRole().equalsIgnoreCase("Employee")) {
			User user = new User();
			user.setUserid((Long) employeeId);
			Set<Role> roles = new HashSet<Role>();
			Role employeeRole = dao.findBy(Role.class, 2l);
			roles.add(employeeRole);
			user.setRole(roles);
			dao.save(user);
		} else if (employeeDTO.getRole().equalsIgnoreCase("Manager")) {
			User user = new User();
			user.setUserid((Long) employeeId);
			Set<Role> roles = new HashSet<Role>();
			Role managerRole = dao.findBy(Role.class, 3l);
			roles.add(managerRole);
			user.setRole(roles);
			dao.save(user);
		} else if (employeeDTO.getRole().equalsIgnoreCase("Finance")) {
			User user = new User();
			user.setUserid((Long) employeeId);
			Set<Role> roles = new HashSet<Role>();
			Role managerRole = dao.findBy(Role.class, 4l);
			roles.add(managerRole);
			user.setRole(roles);
			dao.save(user);
		}

		Employee employee = dao.findBy(Employee.class, employeeId);
		Finance finance = new Finance();
		finance.setEmployee(employee);
		dao.save(finance);
		recuritmentMailConfiguration.sendMailToNewEmployee(employee);

		/*
		 * try { mailSenderUtility.sendMailToEmployee(employee); } catch
		 * (MalformedURLException ex) {
		 * Logger.getLogger(JobPortalServiceImpl.class.getName()).log(
		 * Level.SEVERE, null, ex); }
		 */

	}

	@Override
	public ImageDTO getImage(Long id) {

		String imageUrl;

		Employee employee = dao.findBy(Employee.class, id);

		imageUrl = employee.getProfilePicPath();
		System.out.println("imgurl:" + imageUrl);

		if (!"undefined".equalsIgnoreCase(imageUrl) && imageUrl != ""
				&& imageUrl != null) {
			return new ImageDTO(getImageExtesion(imageUrl),
					getImageByteData(imageUrl), imageUrl, id);
		} else {
			return new ImageDTO(null, null, null, id);
		}
	}

	public ImageDTO getImagebyPath(String imageUrl) {

		if (!"undefined".equalsIgnoreCase(imageUrl) && imageUrl != ""
				&& imageUrl != null) {
			return new ImageDTO(getImageExtesion(imageUrl),
					getImageByteData(imageUrl), imageUrl);
		} else {
			return new ImageDTO(null, null, null);
		}
	}

	public String getImageExtesion(String imgName) {
		String fileExtension = null;
		if (imgName != null) {
			int i = imgName.lastIndexOf('.');
			if (i > 0) {
				fileExtension = imgName.substring(i + 1);

			}
		}
		return fileExtension;
	}

	/**
	 *
	 * @param photoUrl
	 * @return
	 */
	@Override
	public byte[] getImageByteData(String photoUrl) {
		if (photoUrl != null) {
			System.out.println(" profile pic url:" + photoUrl);
			try {
				System.out.println("in try");

				BufferedImage bufferedImage = ImageIO.read(new File(photoUrl));
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage, getImageExtesion(photoUrl),
						byteArrayOutputStream);
				return byteArrayOutputStream.toByteArray();

			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			System.out.println("No url");
		}
		return new byte[] {};

	}

	@Caching(evict = {
			@CacheEvict(value = "otherEmployeeInformation", key = "#id"),
			@CacheEvict(value = "employees", allEntries = true) })
	@Override
	public void uploadImage(MultipartFile mpf, String id) {

		Long employeeId = Long.parseLong(id);
		Employee employee = dao.findBy(Employee.class, employeeId);
		FileUploaderUtility fileUploaderUtility = new FileUploaderUtility();
		try {
			dao.update(fileUploaderUtility.uploadImage(mpf, employee, propBean));
			dao.update(employee);
		} catch (IOException ex) {
			Logger.getLogger(JobPortalServiceImpl.class.getName()).log(
					Level.SEVERE, null, ex);
			employee.setProfilePicPath("default.gif");
			employee.setThumbPicture("default.gif");
			dao.update(employee);
		}
	}

	@Override
	public void uploadBase64Image(String parameter, Long empLong) {
		System.out.println("in service");

		Employee employee = dao.findBy(Employee.class, empLong);
		FileUploaderUtility fileUploaderUtility = new FileUploaderUtility();
		byte[] bytes = Base64.decodeBase64(parameter);

		try {
			fileUploaderUtility.uploadBase64Image(bytes, employee, propBean);
		} catch (IOException e) {
			Logger.getLogger(JobPortalServiceImpl.class.getName()).log(
					Level.SEVERE, null, e);
			employee.setProfilePicPath("default.gif");
			employee.setThumbPicture("default.gif");
			e.printStackTrace();
			dao.update(employee);
		}
	}

	@Caching(evict = {
			@CacheEvict(value = "otherEmployeeInformation", key = "#employeeDTO.id"),
			@CacheEvict(value = "employees", allEntries = true) })
	@Override
	public void updateEmployeeDetails(EmployeeDTO employeeDTO) {
		System.out.println("in update");
		Employee employee = dao.findBy(Employee.class, employeeDTO.getId());
		Employee oldEmployee = cloneMethod(employee);
		Employee employeeBasicInformation = employeeBuilder
				.createEmployeeEntityForBasicInformation(employee, employeeDTO);
		dao.saveOrUpdate(employeeBasicInformation);

		// for storing in audit table
		List<Audit> empAudit = auditBuilder.UpdateAuditTOEmployeeEntity(
				employeeBasicInformation, employeeDTO.getId(), oldEmployee,
				"EMPLOYEE", "UPDATED");

		for (Audit audit : empAudit) {
			dao.save(audit);
		}
	}

	public Employee cloneMethod(Employee emp) {
		Employee oldEmp = new Employee();
		try {
			oldEmp = (Employee) emp.clone();
		} catch (CloneNotSupportedException ex) {
			java.util.logging.Logger.getLogger(
					SupportManagementServiceImpl.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		return oldEmp;
	}

	@Caching(evict = {
			@CacheEvict(value = "otherEmployeeInformation", key = "#skillDTO.employee.id"),
			@CacheEvict(value = "employees", allEntries = true) })
	@Override
	public void updateEmployeeSkill(SkillDTO skillDTO) {
		Skill skill = dao.findBy(Skill.class, skillDTO.getSkillId());
		skill.setCategoryType(dao.findBy(Category.class,
				skillDTO.getCategoryId()).getCategoryType());
		skill.setComments(skillDTO.getComments());
		skill.setCompetency(skillDTO.getCompetency());
		Employee employee = dao.findBy(Employee.class, skillDTO.getEmployee()
				.getId());
		skill.setEmployee(employee);
		skill.setExpYear(skillDTO.getExpYear());
		skill.setExpMonth(skillDTO.getExpMonth());
		skill.setSkillType(skillDTO.getSkillType());
		dao.saveOrUpdate(skill);

	}

	@Caching(evict = {
			@CacheEvict(value = "otherEmployeeInformation", allEntries = true),
			@CacheEvict(value = "employees", allEntries = true) })
	@Override
	public void deleteEmployeeSkill(long skillId) {
		Skill skill = dao.findBy(Skill.class, skillId);
		skill.setEmployee(null);
		dao.saveOrUpdate(skill);
		dao.delete(skill);
	}

	@Caching(evict = {
			@CacheEvict(value = "otherEmployeeInformation", key = "#skillDTO.employee.id"),
			@CacheEvict(value = "employees", allEntries = true) })
	@Override
	public void addSkillToEmployee(SkillDTO skillDTO) {
		Skill skill = new Skill();
		Boolean skillAlreadyExistsFlag = false;
		Category category = dao.findBy(Category.class,
				Long.parseLong(skillDTO.getCategoryType()));
		skill.setCategoryType(category.getCategoryType());
		skill.setComments(skillDTO.getComments());
		skill.setCompetency(skillDTO.getCompetency());
		Employee employee = dao.findBy(Employee.class, skillDTO.getEmployee()
				.getId());

		List<Skill> skillList = new ArrayList<Skill>(
				employee.getEmployeeSkills());
		for (Skill skill1 : skillList) {

			if (skill1.getCategoryTypeId().equalsIgnoreCase(
					skillDTO.getCategoryType())
					&& skill1.getSkillType().equalsIgnoreCase(
							skillDTO.getSkillType())) {

				skillAlreadyExistsFlag = true;
				break;
			}

		}
		if (!skillAlreadyExistsFlag) {

			skill.setEmployee(employee);
			skill.setExpYear(skillDTO.getExpYear());
			skill.setExpMonth(skillDTO.getExpMonth());
			skill.setSkillType(skillDTO.getSkillType());
			skill.setCategoryTypeId(String.valueOf(category.getCategoryId()));
			dao.save(skill);
		} else {
			throw new RuntimeException("The skill already exists");
		}
	}

	@Caching(evict = {
			@CacheEvict(value = "otherEmployeeInformation", allEntries = true),
			@CacheEvict(value = "employees", allEntries = true) })
	@Override
	public SkillDTO editSkill(long skillId) {
		Skill skill = dao.findBy(Skill.class, skillId);
		SkillDTO skillDTO = new SkillDTO();
		skillDTO.setSkillId(skill.getSkillId());
		Category category = dao.findByCategoryTypeName(Category.class,
				skill.getCategoryType());
		skillDTO.setLookUpDTOs(categoryBuilder
				.convertFromEmployeeSkillLookUpToEmployeeSkillLookUpDTO(category
						.getEmployeeSkill()));
		skillDTO.setComments(skill.getComments());
		skillDTO.setCategoryType(category.getCategoryId().toString());
		skillDTO.setCategoryId(category.getCategoryId());
		skillDTO.setCompetency(skill.getCompetency());
		EmployeeDTO employee = new EmployeeDTO();
		employee.setId(skill.getEmployee().getEmployeeId());
		skillDTO.setEmployee(employee);
		skillDTO.setExpYear(skill.getExpYear());
		skillDTO.setExpMonth(skill.getExpMonth());
		skillDTO.setSkillType(skill.getSkillType());
		return skillDTO;
	}

	@Override
	public List<EmployeeSkillLookUpDTO> getCategorySkill(long categoryId) {
		Category category = dao.findBy(Category.class, categoryId);
		return categoryBuilder
				.convertFromEmployeeSkillLookUpToEmployeeSkillLookUpDTO(category
						.getEmployeeSkill());
	}

	@Cacheable("categories")
	@Override
	public List<CategoryDTO> getAllCategories() {
		List<Category> categorys = dao.get(Category.class);
		return categoryBuilder.convertFromCategoryToCategoryDTO(categorys);
	}

	@CacheEvict(value = "categories", allEntries = true)
	@Override
	public List<CategoryDTO> addCategory(String categoryName) {
		Category category = new Category();
		category.setCategoryType(categoryName);
		dao.save(category);
		return getAllCategories();

	}

	@CacheEvict(value = "categories", allEntries = true)
	@Override
	public List<CategoryDTO> deleteCategory(long categoryId) {
		Category category = dao.findBy(Category.class, categoryId);
		dao.delete(category);
		return getAllCategories();
	}

	@Override
	public List<EmployeeSkillLookUpDTO> addSkillToSpecificCategory(
			String skillName, long categoryId) {
		Category category = dao.findBy(Category.class, categoryId);
		EmployeeSkillLookUp employeeSkillLookUp = new EmployeeSkillLookUp();
		employeeSkillLookUp.setSkill(skillName);
		employeeSkillLookUp.setCategory(category);
		dao.save(employeeSkillLookUp);

		return getCategorySkill(categoryId);

	}

	@Override
	public List<EmployeeSkillLookUpDTO> deleteSkill(long skillId) {
		EmployeeSkillLookUp skillLookUp = dao.findBy(EmployeeSkillLookUp.class,
				skillId);
		long categoryId = skillLookUp.getCategory().getCategoryId();
		skillLookUp.setCategory(null);
		dao.update(skillLookUp);
		EmployeeSkillLookUp skillLookUp1 = dao.findBy(
				EmployeeSkillLookUp.class, skillId);
		dao.delete(skillLookUp1);
		return getCategorySkill(categoryId);
	}

	@Override
	public List<SkillDTO> getEmployeeskillList(long parseLong) {
		Employee employee = dao.findBy(Employee.class, parseLong);
		List<SkillDTO> skills = employeeBuilder.convertSkillDTO1(employee
				.getEmployeeSkills());
		return skills;
	}

	@Override
	public void importCandiadetFile(String filePath) {
		List<Candidate> candidateList = readExcelFile.readExcelData(filePath);
		try {
			for (int i = 0; i < candidateList.size(); i++) {
				Candidate candi = candidateList.get(i);
				dao.saveOrUpdate(candi);
			}

		} catch (Exception e) {
		}

	}

	@Override
	public List<ScheduledCadidateDTO> getAllUpcomingJoineeList() {
		List<Candidate> candidates = jobPortalDAOImpl
				.getAllUpcomingJoineeList();
		return candidateBuilder.convertEntityToDTO(candidates);

	}

	@Override
	public List<CandidateDTO> importExcelFile(MultipartFile mpf,
			String parameter) {
		List<Candidate> candidates = null;
		try {
			FileUploaderUtility fileUploaderUtility = new FileUploaderUtility();
			String path = fileUploaderUtility.uploadDocument(mpf, propBean);
			candidates = readExcelFile.readExcelData(path);
		} catch (IOException ex) {
			Logger.getLogger(JobPortalServiceImpl.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		List<CandidateDTO> cdtos = null;
		if (!candidates.isEmpty()) {
			cdtos = new ArrayList<CandidateDTO>();
			for (Candidate cand : candidates) {
				Candidate candidate = dao.findByEmployeeMailId(Candidate.class,
						cand.getEmail());
				if (candidate != null) {
					CandidateDTO dTO = new CandidateDTO();
					dTO.setFirstName(cand.getFirstName());
					dTO.setEmail(cand.getEmail());
					dTO.setExperience(cand.getExperience());
					dTO.setMobile(cand.getMobile());
					dTO.setAppliedForVacancy(cand.getAppliedForLookUp());
					dTO.setSkills(cand.getSkill());

					cdtos.add(dTO);
				} else {
					dao.saveOrUpdate(cand);

				}

			}

		}
		return cdtos;

	}

	@Override
	public void overwriteDuplicateCandidateData(CandidateDTO candidateDTO) {

		Candidate candidate = dao.findByEmployeeMailId(Candidate.class,
				candidateDTO.getEmail());
		candidate.setFirstName(candidateDTO.getFirstName());
		candidate.setEmail(candidateDTO.getEmail());
		candidate.setExperience(candidateDTO.getExperience());
		candidate.setSkill(candidateDTO.getSkills());
		candidate.setMobile(candidateDTO.getMobile());
		candidate.setAppliedForLookUp(candidateDTO.getAppliedForVacancy());
		dao.saveOrUpdate(candidate);

	}

	@Override
	public void sendScheduleMailToCandidate(
			CandidateScheduleDto candidateScheduleDto) {
		Candidate candidate = dao.findBy(Candidate.class,
				candidateScheduleDto.getCandidateId());

		try {
			recuritmentMailConfiguration.sendScheduleInterviewMail(candidate,
					candidateScheduleDto.getScheduleDate(),
					candidateScheduleDto.getScheduleTime(),
					candidateScheduleDto.getInterviewRound(),
					candidateScheduleDto.getSendMailToCandidate(),
					candidateScheduleDto.getSendMailToInterviewer());

		} catch (Exception e) {
			e.printStackTrace();
		}

		scheduleInterview(candidate.getInterview().getInterviewers(), candidate);

	}

	// BELOW COMMENTED LINES WERE PREVIOUSLY USED iN ABOVE METHOD FOR SENDING
	// MAILS//
	/*
	 * mailSenderUtility.sendMail(candidate,
	 * candidateScheduleDto.getScheduleDate(),
	 * candidateScheduleDto.getScheduleTime(),
	 * candidateScheduleDto.getInterviewRound());
	 */

	@Override
	public void sendUpdateScheduleMailToCandidate(
			CandidateScheduleDto scheduleDto) {
		Candidate candidate = dao.findBy(Candidate.class,
				scheduleDto.getCandidateId());

		try {
			recuritmentMailConfiguration.sendRescheduleMail(candidate,
					scheduleDto.getScheduleDate(), scheduleDto
							.getScheduleTime(), candidate.getInterview()
							.getRound(), scheduleDto.getInterviewType());
		} catch (Exception e) {
			e.printStackTrace();
		}

		ReScheduleInterview(candidate.getInterview().getInterviewers(),
				candidate);
	}

	// BELOW COMMENTED LINES WERE PREVIOUSLY USED iN ABOVE METHOD FOR SENDING
	/*
	 * // MAILS// mailSenderUtility.sendRescheduleMail(candidate,
	 * scheduleDto.getScheduleDate(), scheduleDto.getScheduleTime(),
	 * candidate.getInterview().getRound(), scheduleDto.getInterviewType());
	 */

	@Override
	public void sendScheduleMailToNewCandidate(
			CandidateScheduleDto candidateScheduleDto, Long id) {

		if (candidateScheduleDto.getScheduleFlag().equalsIgnoreCase(
				StringConstant.YES)) {
			Candidate candidate1 = dao.findBy(Candidate.class, id);

			try {
				recuritmentMailConfiguration.sendScheduleInterviewMail(
						candidate1, candidateScheduleDto.getScheduleDate(),
						candidateScheduleDto.getScheduleTime(),
						candidateScheduleDto.getInterviewRound(),
						candidateScheduleDto.getSendMailToCandidate(),
						candidateScheduleDto.getSendMailToInterviewer());
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

	}

	// BELOW COMMENTED LINES WERE PREVIOUSLY USED iN ABOVE METHOD FOR SENDING
	/*
	 * // MAILS// /* mailSenderUtility.sendMail(candidate1,
	 * candidateScheduleDto.getScheduleDate(),
	 * candidateScheduleDto.getScheduleTime(), "1");
	 */

	@Override
	public CandidateInterviewTimelineDTO timeLineDetails(String candidateId) {
		Candidate candidate = dao.findBy(Candidate.class,
				Long.valueOf(candidateId));
		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		//System.out.println("role:" + employee.getRole());
		if(employee.getRole().equalsIgnoreCase("Employee")) {
		String name = employee.getFullName();
		List<CandidateInterviewCycle> candidateInterviewCycles = jobPortalDAOImpl
				.getCandidateTimeLinedetails(candidateId,name);
		Integer pendingInterview = jobPortalDAOImpl.pendingInterviewList(
				candidateId).size();
		CandidateInterviewTimelineDTO interviewTimelineDTO = candidateInterviewCycleBuilder
				.createCandidateInterviewTimeLineDTOList(
						candidateInterviewCycles, candidate);
		interviewTimelineDTO.setPendingInterviewStatus(pendingInterview);
		return interviewTimelineDTO;
		}
		List<CandidateInterviewCycle> candidateInterviewCycles = jobPortalDAOImpl
				.getCandidateTimeLineDetails(candidateId);
		Integer pendingInterview = jobPortalDAOImpl.pendingInterviewList(
				candidateId).size();
		CandidateInterviewTimelineDTO interviewTimelineDTO = candidateInterviewCycleBuilder
				.createCandidateInterviewTimeLineDTOList(
						candidateInterviewCycles, candidate);
		interviewTimelineDTO.setPendingInterviewStatus(pendingInterview);
		return interviewTimelineDTO;
	}

	@Override
	public Boolean isCandidateMailExists(String candidateEmail) {
		return jobPortalDAOImpl.isCandidateMailExists(candidateEmail);
	}

	@Override
	public Boolean isCandidateMobileNumberExists(String candidateMobileNumber) {
		return jobPortalDAOImpl
				.isCandidateMobileNumberExists(candidateMobileNumber);
	}

	@Override
	public Boolean isEditCandidateMailExists(String candidateId, String mailId) {
		return jobPortalDAOImpl.isEditCandidateMailExists(candidateId, mailId);
	}

	@Override
	public Boolean isEditCandidateMobileNumberExists(String candidateId,
			String mobileNum) {
		return jobPortalDAOImpl.isEditCandidateMobileNumberExists(candidateId,
				mobileNum);
	}

	// @Cacheable("employees")
	@Override
	public Map<String, Object> getProfilePaginationEmployeesData(
			Integer startIndex, Integer endIndex, String selectionStatus,
			String searchStr) {
		Map<String, Object> serachEmpList = new HashMap<String, Object>();

		/*List<Long> inactiveIds =null;
		List<Long> allIds  =null;
		inactiveIds = jobPortalDAOImpl.getInactiveEmpList();
		allIds = separationDaoImpl.AllEmpIds();
		inactiveIds.removeAll(allIds);
		*/
		if (selectionStatus.equalsIgnoreCase("pip")) {
			List<Long> employeeIds = jobPortalDAOImpl.getAllPipList();
			if(!employeeIds.isEmpty()) {
			serachEmpList = jobPortalDAOImpl.getEmployeeList(employeeIds,
					startIndex, endIndex, searchStr);
			}
		}else if(selectionStatus.equalsIgnoreCase("Contract")){
			List<Long> employeeIds = jobPortalDAOImpl.getAllContractList();
			if(!employeeIds.isEmpty()) {
				serachEmpList = jobPortalDAOImpl.getEmployeeList(employeeIds,
						startIndex, endIndex, searchStr);
				}
		}
		/*
		 * else if (selectionStatus.equalsIgnoreCase("Absconded")) { List<Long>
		 * empIds = jobPortalDAOImpl.getAbscondedList(); serachEmpList =
		 * jobPortalDAOImpl.getEmployeeList(empIds, startIndex, endIndex,
		 * searchStr); }
		 */else {
			serachEmpList = jobPortalDAOImpl.getProfilePaginationEmployeesData(
					selectionStatus, startIndex, endIndex, searchStr);
			List<Employee> underNoticeEmployeeList = (List<Employee>) serachEmpList
					.get("employeeList");

			if (underNoticeEmployeeList != null) {
				Collections.sort(underNoticeEmployeeList,
						new Comparator<Employee>() {
							@Override
							public int compare(Employee e1, Employee e2) {
								int k = 0;

								if (e1.isUnderNotice() == true
										&& e2.isUnderNotice() == true) {

									if (e1.getUnderNoticeDate().isAfter(
											e2.getUnderNoticeDate())) {
										k = 1;
									}
									if (e1.getUnderNoticeDate().isBefore(
											e2.getUnderNoticeDate())) {
										k = -1;
									}
								}
								return k;
							}
						});
			}

		}

		List<EmployeeDTO> dTOs = employeeBuilder
				.getemployeeDTOList((List) serachEmpList.get("employeeList"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Empsize", serachEmpList.get("size"));
		map.put("emps", dTOs);
		return map;
	}
	@Override
	public Map<String, Object> getEmployeesCategoryData(
			Integer startIndex, Integer endIndex, String selectionDesignation) {
		Map<String, Object> serachEmpList = new HashMap<String, Object>();

		
			serachEmpList = jobPortalDAOImpl.getEmployeeCategoryData(
					selectionDesignation, startIndex, endIndex);

		

		List<EmployeeDTO> dTOs = employeeBuilder
				.getemployeeDTOList((List) serachEmpList.get("employeeList"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Empsize", serachEmpList.get("size"));
		map.put("emps", dTOs);
		return map;
	}

	@Caching(evict = {
			@CacheEvict(value = "employees", allEntries = true),
			@CacheEvict(value = "otherEmployeeInformation", key = "#employeeDTO.id"),
			@CacheEvict(value = "employeeAllocation", allEntries = true) })
	@Override
	public void editEmployee(EmployeeDTO employeeDTO) {
		Employee employee = dao.findBy(Employee.class, employeeDTO.getId());
		Employee oldEmployee = cloneMethod(employee);
		employee.setDepartmentName(employeeDTO.getDepartmentName());
		employee.setRole(employeeDTO.getRole());
		employee.setTimeSlot(employeeDTO.getTimeSlotDTO() != null ? dao.findBy(
				TimeSlot.class, employeeDTO.getTimeSlotDTO().getId()) : null);
		Employee manager = null;
		if (employeeDTO.getManager() != null) {
			manager = dao.findBy(Employee.class, employeeDTO.getManager()
					.getId());
		}
		employee.setManager(manager);
		employee.setEmploymentTypeName(employeeDTO.getEmploymentTypeName());
		employee.setJobTypeName(employeeDTO.getJobTypeName());
		employee.setDesignation(employeeDTO.getDesignation());
		employee.setTechnology(employeeDTO.getTechnology());

		if (employeeDTO.getStatusName().equalsIgnoreCase("underNotice")) {
			employee.setUnderNotice(Boolean.TRUE);
			employee.setStatusName("Active");
		} else {

			employee.setStatusName(employeeDTO.getStatusName());
			// If employee is inActive then we are de-allocated him from all
			// project.
			if (employeeDTO.getStatusName().equalsIgnoreCase("InActive")) {
				// employee.setIsAbsconded(employeeDTO.getIsAbsconded());

				resourceManagementDAO.deAllocateWhenInactive(employeeDTO
						.getId());
			}
		}
		if (employeeDTO.getRelievingDate() != null
				&& "" != employeeDTO.getRelievingDate()) {
			try {
				if ("Active".equalsIgnoreCase(employeeDTO.getStatusName())) {
					employee.setReleavingDate(null);
					employee.setUnderNoticeDate(null);
					employee.setUnderNotice(Boolean.FALSE);
				} // else
					// if("underNotice".equalsIgnoreCase(employeeDTO.getStatusName())){
					// employee.setReleavingDate(null);
					// }
				else {
					employee.setReleavingDate(DateParser.toDate(employeeDTO
							.getRelievingDate()));
					employee.setUnderNotice(Boolean.FALSE);
				}
			} catch (ParseException ex) {
				Logger.getLogger(JobPortalServiceImpl.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
		if (employeeDTO.getUnderNoticeDate() != null
				&& "" != employeeDTO.getUnderNoticeDate()) {
			try {
				if ("Active".equalsIgnoreCase(employeeDTO.getStatusName())) {
					employee.setReleavingDate(null);
					employee.setUnderNoticeDate(null);
					employee.setUnderNotice(Boolean.FALSE);
				} // else
					// if("InActive".equalsIgnoreCase(employeeDTO.getStatusName())){
					// employee.setUnderNoticeDate(null);
					// }
				else {
					employee.setUnderNoticeDate(DateParser.toDate(employeeDTO
							.getUnderNoticeDate()));

				}
			} catch (ParseException ex) {
				Logger.getLogger(JobPortalServiceImpl.class.getName()).log(
						Level.SEVERE, null, ex);
			}
		}
		employee.setEmpRole(dao.findByName(Role.class, employeeDTO.getRole()));

		// HR associate is the new Field
		if (employeeDTO.getHrAssociate() != null) {
			Employee hrAssociate = dao.findBy(Employee.class, employeeDTO
					.getHrAssociate().getId());
			employee.setHrAssociate(hrAssociate);
		}
		// new project manager field
		Employee projectManager = null;
		if (employeeDTO.getProjectManager() != null) {
			projectManager = dao.findBy(Employee.class, employeeDTO
					.getProjectManager().getId());
			employee.setProjectManager(projectManager);
		} else {
			employee.setProjectManager(projectManager);
		}
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
		employee.setWorkStatus(employeeDTO.getWorkStatus()!=null? employeeDTO.getWorkStatus():null);
		if(employeeDTO.getVendorId()!=null){
		VendorDetails ven = dao.findBy(VendorDetails.class, employeeDTO.getVendorId());
		VendorDetails details = new VendorDetails();
		details.setVendorId(ven.getVendorId());
		details.setVendorName(ven.getVendorName());
		employee.setVendor(details);
		}else{
			employee.setVendor(null);
		}
		
		dao.saveOrUpdate(employee);
		// for storing in audit table
		List<Audit> empAudit = auditBuilder.UpdateAuditTOEmployeeEntity(
				employee, employeeDTO.getId(), oldEmployee, "EMPLOYEE",
				"UPDATED");

		for (Audit audit : empAudit) {
			dao.save(audit);
		}

	}

	@Override
	public List<DepartmentDTO> getAllDepartmentList() {
		List<Department> departments = dao.get(Department.class);
		return panelBuilder
				.convertDepartmentListToDepartmentDTOList(departments);
	}

	@Override
	public List<EmployeeDTO> panelEmployeeData(String departmentId) {
		List<Panel> panels = jobPortalDAOImpl
				.panelDepartmentEmployeeData(departmentId);
		return panelBuilder.convertPanelListToEmployeeDTOList(panels);
	}

	@Override
	public EmployeeDTO getLoggedInEmployee(String username) {
		Employee employee = dao.findByEmployeeName(Employee.class, username);
		EmployeeDTO employeeDTO = employeeBuilder.createEmployeeDTO(employee);
		Set<SkillDTO> skills = employeeBuilder.convertSkillDTO(employee
				.getEmployeeSkills());
		employeeDTO.setSkillList(skills);
		employeeDTO.setImageData(getImage(employeeDTO.getId()).getImageData());
		
		  if(employee.getPassportFrontPagePath()!=null) {
		  employeeDTO.setPassportFrontPage
		  (getPassportFrontImage(employeeDTO.getId()).getPassportFrontPage());
		  } if(employee.getPassportBackPagePath()!=null) {
		  employeeDTO.setPassportBackPage
		  (getPassportBackImage(employeeDTO.getId()).getPassportBackPage()); }
		 
		List<EmployeeFamilyInformationDTO> informationList = employeeProfileBuilder
				.convertEmployeeFamilyInformationDTO(employee
						.getEmployeeFamilyDetails());
		employeeDTO.setInformationList(informationList);
		List<EmployeeBankInformationDTO> bankInformationList = employeeProfileBuilder
				.convertEmployeeBankInformationDTO(employee
						.getBankInformations());
		employeeDTO.setBankInformationList(bankInformationList);
		return employeeDTO;
	}

	@Override
	public CandidateInterviewCycleDTO reSheduleInterviewForCandidate(
			String candidateId) {
		Candidate candidate = dao.findBy(Candidate.class,
				Long.parseLong(candidateId));
		CandidateInterviewCycle interviewCycle = jobPortalDAOImpl
				.reSheduleInterviewForCandidate(candidateId);
		CandidateInterviewCycleDTO cycleDTO = new CandidateInterviewCycleDTO();
		cycleDTO.setInterviewCycleId(interviewCycle.getInterviewCycleId());
		cycleDTO.setCandidateId(String.valueOf(candidate.getPersonId()));
		cycleDTO.setCandidateName(candidate.getFirstName());
		cycleDTO.setInterviewMode(candidate.getInterview().getInterviewType()
				.toString());
		cycleDTO.setInterviewStatus(interviewCycle.getInterviewStatus());
		cycleDTO.setInterviewRound(String.valueOf(interviewCycle
				.getInterviewRound()));
		cycleDTO.setInterviewTime(interviewCycle.getInterviewTime());
		cycleDTO.setInterviewDate(interviewCycle.getInterviewDate().toString(
				"dd MMM yyyy"));
		return cycleDTO;

	}

	@Override
	public void reSheduleInterviewUpdate(CandidateInterviewCycleDTO cycleDTO) {
		Candidate candidate = dao.findBy(Candidate.class,
				Long.parseLong(cycleDTO.getCandidateId()));
		Interview interview = candidate.getInterview();
		Set<Employee> interviewers = new HashSet<Employee>(
				employeeBuilder.getemployeeEntityList1(cycleDTO
						.getInterviewersDTOList()));
		interview.setInterviewers(interviewers);
		Schedule schedule = jobPortalDAOImpl.getCadidateSchedule(candidate
				.getInterview().getInterviewId());
		try {
			if (cycleDTO.getInterviewDate().contains(" ")) {
				schedule.setScheduleDate(DateParser.toDateOtherFormat(cycleDTO
						.getInterviewDate()));
			} else {
				schedule.setScheduleDate(DateParser.toDate(cycleDTO
						.getInterviewDate()));
			}
			schedule.setScheduleTime(cycleDTO.getInterviewTime());
			schedule.setInterview(interview);
		} catch (ParseException ex) {
			Logger.getLogger(JobPortalServiceImpl.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		dao.saveOrUpdate(schedule);

		CandidateInterviewCycle candidateInterviewCycle = dao.findBy(
				CandidateInterviewCycle.class, cycleDTO.getInterviewCycleId());
		try {
			if (cycleDTO.getInterviewDate().contains(" ")) {

				candidateInterviewCycle.setInterviewDate(DateParser
						.toDateOtherFormat(cycleDTO.getInterviewDate()));
			} else {
				candidateInterviewCycle.setInterviewDate(DateParser
						.toDate(cycleDTO.getInterviewDate()));
			}
		} catch (ParseException ex) {
			Logger.getLogger(JobPortalServiceImpl.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		candidateInterviewCycle.setInterviewTime(cycleDTO.getInterviewTime());
		Set<Employee> employeesList = new HashSet<Employee>();

		String names = "";
		for (Employee employee : interviewers) {
			employeesList.add(dao.findBy(Employee.class,
					employee.getEmployeeId()));
			names = names + employee.getFullName() + ",";
		}
		candidateInterviewCycle.setInterviewers(names.substring(0,
				names.length() - 1));
		candidateInterviewCycle.setDescription(cycleDTO.getDescription());
		candidateInterviewCycle.setStatus("pending");
		candidateInterviewCycle.setEmployeeList(employeesList);
		dao.saveOrUpdate(candidateInterviewCycle);
	}

	@Override
	public Map<String, Object> getScheduledCandidatesForEmployee(
			Long employeeId, Integer startIndex, Integer endIndex) {

		Map<String, Object> cicsMap = jobPortalDAOImpl
				.getScheduledCandidatesForEmployee(employeeId, startIndex,
						endIndex);

		Integer noOfRecords = (Integer) cicsMap.get("size");
		List<CandidateInterviewCycle> cicList = (List) cicsMap.get("list");
		List<CandidateInterviewCycleDTO> cycleDTOs = candidateBuilder
				.convertCandidateInterviewDTOsListForEmployee(cicList);
		Map<String, Object> shdCdMap = new HashMap<String, Object>();
		shdCdMap.put("list", cycleDTOs);
		shdCdMap.put("size", noOfRecords);
		return shdCdMap;

	}

	@Override
	public CandidateInterviewCycleDTO empScheduleInterviewDetailsByID(
			Long interviewCycleId) {
		CandidateInterviewCycleDTO candidateInterviewCycleDTO = candidateBuilder
				.getCandidateInterviewCycleDTO(jobPortalDAOImpl
						.getEmpScheduleInterviewDetailsByID(interviewCycleId));
		// List<CandidateInterviewCycle> cicList = jobPortalDAOImpl
		// .viewScheduleCompletedCandidates(candidateInterviewCycleDTO
		// .getCandidateId());
		// List<CandidateInterviewCycleDTO> cicdtos =
		// candidateInterviewCycleBuilder
		// .createCandidateCycleDTOList(cicList);
		// candidateInterviewCycleDTO.setCycleDTOs(cicdtos);
		return candidateInterviewCycleDTO;
	}

	@Override
	public void updateScheduleCandidatebyEmployee(
			CandidateInterviewCycleDTO cycleDTO) {
		CandidateInterviewCycle candidateInterviewCycle = dao.findBy(
				CandidateInterviewCycle.class, cycleDTO.getInterviewCycleId());
		candidateInterviewCycle.setInterviewComments(cycleDTO
				.getInterviewComments());
		candidateInterviewCycle.setRating(cycleDTO.getRating());
		candidateInterviewCycle.setStatus(cycleDTO.getStatus());
		dao.saveOrUpdate(candidateInterviewCycle);
	}

	@Override
	public Map<String, Object> getSearchScheduledCandidatesForEmployee(
			Long employeeId, String status, String fromDate, String toDate,
			Integer startIndex, Integer endIndex) {
		Date fDate = null;
		Date tDate = null;
		try {
			fDate = DateParser.toDate(fromDate);
			tDate = DateParser.toDate(toDate);
		} catch (ParseException ex) {
			Logger.getLogger(JobPortalServiceImpl.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		Map<String, Object> interviewCycles = jobPortalDAOImpl
				.getSearchScheduledCandidatesForEmployee(employeeId, status,
						fDate, tDate, startIndex, endIndex);

		// Integer noOfRecords = (Integer) interviewCycles.get("size");

		List<CandidateInterviewCycleDTO> cycleDTOs = candidateBuilder
				.convertCandidateInterviewDTOsListForEmploye((List) interviewCycles
						.get("list"));
		Integer noOfRecords = cycleDTOs.size();
		// pagination because if we will do pagination from DaoImpl then there
		// is a chance of
		// getting duplicate date and after restricting duplicate, pagination
		// won't work properly.
		List<CandidateInterviewCycleDTO> cycleDTO = new ArrayList<CandidateInterviewCycleDTO>();
		if (endIndex <= cycleDTOs.size()) {
			cycleDTO = cycleDTOs.subList(startIndex, endIndex);
		} else {
			cycleDTO = cycleDTOs.subList(startIndex, cycleDTOs.size());
		}

		Map<String, Object> srchCandForEmp = new HashMap<String, Object>();
		srchCandForEmp.put("list", cycleDTO);
		srchCandForEmp.put("size", noOfRecords);
		return srchCandForEmp;

	}

	@Override
	public CandidateInterviewCycleDTO getViewCandiadetForAdmin(Long candidateId) {
		Candidate candidate = dao.findBy(Candidate.class, candidateId);
		CandidateInterviewCycleDTO candidateInterviewCycleDTO = new CandidateInterviewCycleDTO();
		candidateInterviewCycleDTO.setCandidateName(candidate.getFirstName());
		candidateInterviewCycleDTO.setSkills(candidate.getSkill());
		candidateInterviewCycleDTO.setCandiadateEmailId(candidate.getEmail());
		candidateInterviewCycleDTO.setMobileNumber(candidate.getMobile());
		List<CandidateInterviewCycle> candidateInterviewCycleList = jobPortalDAOImpl
				.getCandidateTimeLineDetails(String.valueOf(candidateId));
		candidateInterviewCycleDTO.setCycleDTOs(candidateInterviewCycleBuilder
				.createCandidateCycleDTOList(candidateInterviewCycleList));
		return candidateInterviewCycleDTO;

	}

	@Override
	public Boolean isJobCodeExist(String jobCode) {
		return jobPortalDAOImpl.isJobCodeExist(jobCode);
	}

	@Override
	public void downloadFile(HttpServletResponse response, String fileName) {

		try {
			FileUploaderUtil util = new FileUploaderUtil();
			util.downloadFile(response, fileName, propBean);
		} catch (Exception ex) {
			throw new FileUploaderUtilException(
					"Exception occured while uploading a file in Legal "
							+ ex.getMessage(), ex);
		}

	}

	@Override
	public void downloadAttachment(HttpServletResponse response, String fileName) {
		String filePath = (String) propBean.getPropData().get("newJoinee");
		try {
			FileUploaderUtil util = new FileUploaderUtil();
			util.downloadUploadedFile(response, fileName, filePath);
		} catch (Exception ex) {
			throw new FileUploaderUtilException(
					"Exception occured while uploading a file in Legal "
							+ ex.getMessage(), ex);
		}

	}

	@Override
	public void uploadCandidateResume(MultipartFile mpf, String candidateId) {
		if (candidateId != null) {
			Long cadidateId = Long.parseLong(candidateId);
			Candidate candidate = dao.findBy(Candidate.class, cadidateId);
			FileUploaderUtility fileUploaderUtility = new FileUploaderUtility();
			try {
				String path = fileUploaderUtility.uploadCandidateDocument(
						candidate, mpf, propBean);
				System.out.println("file path1:" + path);
				candidate.setResumePath(path);
				System.out.println("resumePath:" + candidate.getResumePath());
				dao.update(candidate);
				System.out.println(" candid resume uploaded successfully");
			} catch (IOException ex) {
				Logger.getLogger(JobPortalServiceImpl.class.getName()).log(
						Level.SEVERE, null, ex);
				ex.printStackTrace();
				dao.update(candidate);
				System.out.println(" candid resume uploaded successfully");
			}
		}
	}

	/*
	 * @Override public void downloadScheduleCandidatesFile(HttpServletResponse
	 * response, String skill, String fromDate, String toDate) {
	 * 
	 * List<Schedule> candidates = jobPortalDAOImpl.searchCadidates(skill, fromDate,
	 * toDate); List<ScheduledCadidateDTO> scdtosList = scheduleBuilder
	 * .convertScheduleDTO(candidates); try {
	 * writeToExcelFile.writeScheduleCandidateListToFile(scdtosList); } catch
	 * (Exception ex) { Logger.getLogger(JobPortalServiceImpl.class.getName()).log(
	 * Level.SEVERE, null, ex); }
	 * 
	 * try { FileUploaderUtil util = new FileUploaderUtil();
	 * util.downloadFile(response, "ScheduledCandidates.xls", propBean); } catch
	 * (Exception ex) { throw new FileUploaderUtilException(
	 * "Exception occured while uploading a file in Legal " + ex.getMessage(), ex);
	 * } }
	 */

	/*
	 * @Override public Map<String, Object> searchCandidates(String skill,
	 * String fromDate, String toDate, Integer startIndex, Integer endIndex) {
	 * Map<String, Object> srchCandMap =
	 * jobPortalDAOImpl.searchCandidates(skill, fromDate, toDate, startIndex,
	 * endIndex); List<Schedule> candidates = (List) srchCandMap.get("list1");
	 * Integer noOfRecords = (Integer) srchCandMap.get("size1");
	 * 
	 * List<ScheduledCadidateDTO> scdtos =
	 * scheduleBuilder.convertScheduleDTO(candidates); Map<String, Object>
	 * srchScheduleCandMap = new HashMap<String, Object>();
	 * logger1.info("^^^^^^ candidates sizeee servicer Impl: @@ " +
	 * scdtos.size()); srchScheduleCandMap.put("list", scdtos);
	 * srchScheduleCandMap.put("size", noOfRecords); return srchScheduleCandMap;
	 * 
	 * }
	 */
	@Override
	public Map<String, Object> searchCandidates(String skill, String fromDate,
			String toDate, Integer startIndex, Integer endIndex) {

		Date fDate = null;
		Date tDate = null;
		try {
			fDate = DateParser.toDate(fromDate);
			tDate = DateParser.toDate(toDate);
		} catch (ParseException ex) {
			Logger.getLogger(JobPortalServiceImpl.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		Map<String, Object> interviewCycles = jobPortalDAOImpl
				.searchCandidates(skill, fDate, tDate, startIndex, endIndex);

		Integer noOfRecords = (Integer) interviewCycles.get("size");
		List<CandidateInterviewCycleDTO> cycleDTOs = candidateBuilder
				.convertCandidateInterviewDTOsListForEmployee((List) interviewCycles
						.get("list"));

		Map<String, Object> srchCandForAdmin = new HashMap<String, Object>();
		srchCandForAdmin.put("list", cycleDTOs);
		srchCandForAdmin.put("size", noOfRecords);
		return srchCandForAdmin;

	}

	@Override
	public void addNewJoinee(NewJoineeDTO joineeDTO) {
		Candidate candidate = dao.findBy(Candidate.class,
				joineeDTO.getCandidateId());
		candidate.setJoineeComments(joineeDTO.getComments());
		dao.update(candidate);
		try {
			dao.save(joineeBuilder.createJoineeEntityFromJoineeDTO(joineeDTO));
		} catch (Exception e) {
			logger1.info("exception occured while adding new joinee");
		}
		String time ="10:00AM";
		String to="";
		if(candidate.getCountry().getName().equalsIgnoreCase("INDIA")){
		  to =91+candidate.getMobile();
		}
		if(candidate.getCountry().getName().equalsIgnoreCase("USA")){
			  to =1+candidate.getMobile();
			}
		if(candidate.getCountry().getName().equalsIgnoreCase("CANADA")){
			  to =1+candidate.getMobile();
			}
		if(candidate.getCountry().getName().equalsIgnoreCase("PHILIPPINES")){
			  to =63+candidate.getMobile();
			}
		if(candidate.getCountry().getName().equalsIgnoreCase("AUSTRALIA")){
			  to =61+candidate.getMobile();
			}
		if(candidate.getNotifications().equalsIgnoreCase("Yes") &&joineeDTO.getSendOfferMessagetoCandidate().toString().equalsIgnoreCase("true")){
			String params = '\"' + joineeDTO.getCandidateName()+ '\"' + ',' + '\"' + joineeDTO.getAppliedForLookUp() + '\"'+ ','+ '\"' + joineeDTO.getDateOfJoining() + '\"' +','+'\"'+ time + '\"';
			String template ="offer_candidate";
			sendWhatsappScheduleInterviewNotificationtoCandidate(to,template,params);
		}
	}


	@Override
	public void updateInterview(CandidateInterviewCycleDTO cicdto) {
		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		try {
			Date interviewDate = DateParser.toDateOtherFormat(cicdto
					.getInterviewDate());
			if (interviewDate.isBefore(new Date())
					|| interviewDate.equals(new Date())) {
				CandidateInterviewCycle candidateInterviewCycle = dao.findBy(
						CandidateInterviewCycle.class,
						cicdto.getInterviewCycleId());
				
				candidateInterviewCycle.setInterviewComments(cicdto.getProactiveComments()+" @@@@"+
						cicdto.getCommunicationComments()+" @@@@"+
						cicdto.getExcellenceComments()+" @@@@"+
						cicdto.getInterviewComments()
						+ " (Updated By :- " + employee.getFullName() + ")");
				/*candidateInterviewCycle.setInterviewComments(cicdto
						.getInterviewComments()
						+ " (Updated By :- "
						+ employee.getFullName() + ")");*/
				candidateInterviewCycle.setUpdatedBy(employee.getFullName());
				candidateInterviewCycle.setRating(cicdto.getRating());
				candidateInterviewCycle.setStatus("finished");
				candidateInterviewCycle.setCommentedDate(new Date());
				SimpleDateFormat f2 = new SimpleDateFormat("hh:mma");
				String time = f2.format(new java.util.Date());
				candidateInterviewCycle.setCommentedTime(time);
				dao.saveOrUpdate(candidateInterviewCycle);

			} else {

				throw new RuntimeException();
			}

		} catch (ParseException e) {
			logger1.error(e);
		}
		System.out.println(cicdto.getCandidateId());
		Candidate candidate = dao.findBy(Candidate.class, Long.parseLong(cicdto.getCandidateId()));
		String to="";
		if(candidate.getCountry().getName().equalsIgnoreCase("INDIA")){
		  to =91+candidate.getMobile();
		}
		else if(candidate.getCountry().getName().equalsIgnoreCase("USA")){
			  to =1+candidate.getMobile();
			}
		else if(candidate.getCountry().getName().equalsIgnoreCase("CANADA")){
			  to =1+candidate.getMobile();
			}
		else if(candidate.getCountry().getName().equalsIgnoreCase("PHILIPPINES")){
			  to =63+candidate.getMobile();
			}
		else if(candidate.getCountry().getName().equalsIgnoreCase("AUSTRALIA")){
			  to =61+candidate.getMobile();
			}
		
		if(candidate.getNotifications()!=null && candidate.getNotifications().equalsIgnoreCase("Yes")){
			String template ="thankyou";
			 String thankyouParams = '\"' + candidate.getFullName() + '\"';
			 sendWhatsappScheduleInterviewNotificationtoCandidate(to,template,thankyouParams);
		}

	}

	@Override
	public Integer countofInterviewRoundList(String candidateId) {
		// return jobPortalDAOImpl.viewScheduleCompletedCandidates(candidateId)
		// .size();
		Long roundOfInterview = jobPortalDAOImpl.highestInterviewRound(Long
				.parseLong(candidateId));
		return roundOfInterview.intValue();
	}

	@Override
	public Map<String, Object> getUpcomingJoineeList(Integer startIndex,
			Integer endIndex, String searchName) {

		Map<String, Object> upcmngJoineeMap = jobPortalDAOImpl
				.getUpcomingJoineeList(startIndex, endIndex, searchName);
		List<NewJoinee> newJoineeList = (List) upcmngJoineeMap.get("list");
		Integer noOfrecords = (Integer) upcmngJoineeMap.get("size");
		List<NewJoineeDTO> newJoineeDTOList = joineeBuilder
				.convertJoineeDTOListTOEntityList(newJoineeList);
		Map<String, Object> joineeMap = new HashMap<String, Object>();
		joineeMap.put("list", newJoineeDTOList);
		joineeMap.put("size", noOfrecords);
		return joineeMap;
	}

	@Override
	public NewJoineeDTO getJoineeById(Long joineeId) {

		NewJoinee newJoinee = dao.findBy(NewJoinee.class, joineeId);
		return joineeBuilder.convertEntityToDTO(newJoinee);

	}

	@Override
	public void updateNewJoinee(NewJoineeDTO joineeDTO) {
		NewJoinee newJoinee = dao.findBy(NewJoinee.class, joineeDTO.getId());
		NewJoinee newJoinee1 = joineeBuilder.convertEditJoineeToJoinee(
				newJoinee, joineeDTO);
		Candidate candidate = dao.findBy(Candidate.class,
				joineeDTO.getCandidateId());
		candidate.setExperience(newJoinee1.getExperience());
		candidate.setEmail(newJoinee.getEmail());
		candidate.setFirstName(newJoinee1.getJoineeName());
		candidate.setCandidateInterviewStatus(Status.valueOf(newJoinee1
				.getCandidateInterviewStatus()));
		candidate.setJoineeComments(newJoinee.getComments());

	//	candidate.setCandidateInterviewStatus(Status.valueOf(newJoinee
			//	.getCandidateInterviewStatus()));

		if (newJoinee1.getCandidateInterviewStatus().equalsIgnoreCase(
				"DID_NOT_JOIN")) {
			/*
			 * System.out.println("line no 1661"); JobVacancy jobVacancy =
			 * candidate.getAppliedFor(); System.out.println("line no 1663");
			 * jobVacancy.setOffered(jobVacancy.getOffered() - 1);
			 * System.out.println("line no 1665");
			 * jobVacancy.setPositionVacant(jobVacancy.getPositionVacant() + 1);
			 * System.out.println("line no 1667"); dao.saveOrUpdate(jobVacancy);
			 */

			/*
			 * //decrementing the offer value in job vacancy table JobVacancy
			 * jobVacancy = candidate.getAppliedFor();
			 * 
			 * Integer noOfVacancies = jobVacancy.getNoOfRequirements(); Integer
			 * noOfOffered = jobVacancy.getOffered() if (noOfVacancies >
			 * noOfOffered) { if (candidate.getCandidateInterviewStatus() !=
			 * Status.OFFERED) { // jobVacancy.setNoOfRequirements(noOfVacancies
			 * - 1); jobVacancy.setOffered(noOfOffered + 1);
			 * dao.saveOrUpdate(jobVacancy); }
			 */
			// jobVacancy.setOffered(jobVacancy.getOffered()-1);
			/*
			 * jobVacancy.setNoOfRequirements(jobVacancy.getNoOfRequirements()+1)
			 * ; dao.saveOrUpdate(jobVacancy);
			 */
			candidate.setStatusComments("Did not join");
		}
		if (newJoinee1.getCandidateInterviewStatus().equalsIgnoreCase(
				"OFFER_CANCELLED")) {

			candidate.setStatusComments("OFFER CANCELLED");

		}
		// candidate.setUpdatedDate(new Second());
		dao.update(candidate);
		if (newJoinee1.getCandidateInterviewStatus()
				.equalsIgnoreCase("OFFERED")) {
			dao.saveOrUpdate(newJoinee1);
		} else if (newJoinee1.getCandidateInterviewStatus().equalsIgnoreCase(
				"DID_NOT_JOIN")) {
			// If did not join then deleting from newjoinee table
			dao.delete(dao.findBy(NewJoinee.class, joineeDTO.getId()));

			// If did not join decrementing the offer value in job vacancy table
			JobVacancy jobVacancy = candidate.getAppliedFor();
			jobVacancy.setOffered(jobVacancy.getOffered() - 1);
			// jobVacancy.setPositionVacant(jobVacancy.getPositionVacant() + 1);
			dao.saveOrUpdate(jobVacancy);
			reprocessingCandidate(candidate, "DID NOT JOIN");
		} else if (newJoinee1.getCandidateInterviewStatus().equalsIgnoreCase(
				"OFFER_CANCELLED")) {
			dao.delete(dao.findBy(NewJoinee.class, joineeDTO.getId()));
			JobVacancy jobVacancy = candidate.getAppliedFor();
			jobVacancy.setOffered(jobVacancy.getOffered() - 1);
			dao.saveOrUpdate(jobVacancy);
			reprocessingCandidate(candidate, "OFFER CANCELLED");
		}/*else if(newJoinee1.getCandidateInterviewStatus().equalsIgnoreCase("JOINED")){
			System.out.println("in new joinee:" + newJoinee1.getCandidateInterviewStatus());
			//dao.delete(dao.findBy(NewJoinee.class,joineeDTO.getId()));
			System.out.println("id:" + dao.findBy(NewJoinee.class,joineeDTO.getId()));
		    dao.saveOrUpdate(newJoinee1);
		}*/

	}

	@Override
	public void reScheduleInterview(CandidateScheduleDto candidateScheduleDto) {
		Schedule schedule;
		List<CandidateInterviewCycle> cycles = jobPortalDAOImpl
				.pendingInterviewList(String.valueOf(candidateScheduleDto
						.getCandidateId()));
		CandidateInterviewCycle cycle = jobPortalDAOImpl
				.getrescduleDetails(candidateScheduleDto.getCandidateId()
						.toString());
		CandidateInterviewCycle candidateInterview = new CandidateInterviewCycle();
		try {
			candidateInterview = (CandidateInterviewCycle) cycle.clone();
		} catch (CloneNotSupportedException ex) {
			java.util.logging.Logger.getLogger(
					SupportManagementServiceImpl.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		Candidate candidate = dao.findBy(Candidate.class,
				candidateScheduleDto.getCandidateId());
		if (candidate.getInterview() != null) {
			candidateScheduleDto.setInterviewRound(candidate.getInterview()
					.getRound());
		}
		 schedule = candidateScheduleInterviewBuilder
				.createScheduleForCandidateEntity(candidate,
						candidateScheduleDto);
		dao.saveOrUpdate(schedule);

		List<CandidateInterviewCycle> interviewCycles = candidateInterviewCycleUtil
				.generateCandidateInterviewCycleForReschdule(
						candidateScheduleDto, cycles);

		for (CandidateInterviewCycle candidateInterviewCycle : interviewCycles) {

			dao.saveOrUpdate(candidateInterviewCycle);
		}

		candidate.setCandidateInterviewStatus(Status.RESCHEDULED);
		// candidate.setUpdatedDate(new Second());
		dao.save(candidate);
		// Here Interview in candidate is updating but not reflecting so I am
		// manually setting the updated Interview to candidate.
		Interview interview = candidate.getInterview();
		Set<Employee> interviewers = new HashSet<Employee>();
		Employee interviewer = dao.findBy(Employee.class,
				candidateScheduleDto.getInterviewerId());
		interviewers.add(interviewer);
		interview.setInterviewers(interviewers);
		candidate.setInterview(interview);
		// Bcz candidate shouldnot get a mail when only interviewer change.
		Employee interviewr = jobPortalDAOImpl.findBy(Employee.class,
				candidateScheduleDto.getInterviewerId());
		
		String link = null;
		if(candidateScheduleDto.getContactDetails()!=null){
		 link = candidateScheduleDto.getContactDetails();
		}
		String to="";
		if(candidate.getCountry().getName().equalsIgnoreCase("INDIA")){
		  to =91+candidate.getMobile();
		}
		else if(candidate.getCountry().getName().equalsIgnoreCase("USA")){
			  to =1+candidate.getMobile();
			}
		else if(candidate.getCountry().getName().equalsIgnoreCase("CANADA")){
			  to =1+candidate.getMobile();
			}
		else if(candidate.getCountry().getName().equalsIgnoreCase("PHILIPPINES")){
			  to =63+candidate.getMobile();
			}
		else if(candidate.getCountry().getName().equalsIgnoreCase("AUSTRALIA")){
			  to =61+candidate.getMobile();
			}
		    String contactPerson = "Recruitment Team";
			String contactNumber = "+91 4023118011"; String location = "https://goo.gl/maps/EyA3NVxkd5zi1PXcA"; String likendIn = "https://in.linkedin.com/company/rbtoriginal";
			String template =""; String templateParams ="";
	      	  System.out.println("status:" + candidateScheduleDto.getSendMessageToCandidate() + candidateScheduleDto.getSendMessageToInterviewer());
	       
		if(candidate.getNotifications().equalsIgnoreCase("Yes") && candidateScheduleDto.getSendMessageToCandidate().toString().equalsIgnoreCase("true")){
			if(schedule.getInterview().getInterviewType().toString().equalsIgnoreCase("FACE_TO_FACE") || schedule.getInterview().getInterviewType().toString().equalsIgnoreCase("SYSTEM")){
				template ="interview_reschedule_n";
				templateParams = '\"' + candidate.getFullName() + '\"' + ',' + '\"' + schedule.getScheduleDate() + '\"'+ ','+ '\"' + schedule.getScheduleTime() + '\"' +','+'\"'+ contactPerson + '\"'+
	     				','+ '\"' + contactNumber + '\"' + ',' + '\"'+ location +'\"' +','+'\"'+ likendIn +'\"';
		     sendWhatsappScheduleInterviewNotificationtoCandidate(to,template,templateParams);
			}else{
				templateParams ='\"' + candidate.getFullName() + '\"' + ',' + '\"' + schedule.getScheduleDate() + '\"'+ ','+ '\"' + schedule.getScheduleTime() + '\"' +
        				',' +'\"' + contactPerson + '\"' + ',' + '\"'+ contactNumber +'\"' +',' + '\"'+ schedule.getInterview().getInterviewType()+'\"'+',' +'\"'+ link+'\"'+','+'\"'+ likendIn +'\"';
        	   template ="rescheduling_onlineinterview";
				sendWhatsappScheduleInterviewNotificationtoCandidate(to,template,templateParams);
		}
		}
		if(candidate.getNotifications().equalsIgnoreCase("Yes") && candidateScheduleDto.getSendMessageToInterviewer().toString().equalsIgnoreCase("true")){

			Employee emp =dao.findBy(Employee.class, candidateScheduleDto.getInterviewerId());
			String empContact="";
			if(emp.getCountry().equalsIgnoreCase("INDIA")){
				empContact =91+emp.getPhone();
			}
			if(emp.getCountry().equalsIgnoreCase("USA")){
				empContact =1+emp.getPhone();
				}
			if(emp.getCountry().equalsIgnoreCase("CANADA")){
				empContact =1+emp.getPhone();
				}
			if(emp.getCountry().equalsIgnoreCase("PHILIPPINES")){
				empContact =63+emp.getPhone();
				}
			if(emp.getCountry().equalsIgnoreCase("AUSTRALIA")){
				  empContact =61+emp.getPhone();
				}
			 templateParams = '\"' + emp.getFullName() + '\"' + ',' + '\"' + candidate.getFullName() + '\"'+ ','+ '\"' + candidate.getAppliedForLookUp()+ '\"' +','+'\"'+ schedule.getScheduleDate() + '\"'+
					','+ '\"' + schedule.getScheduleTime() + '\"';
			 template = "f2f_interview_interviewer_n";
			sendWhatsappScheduleInterviewNotificationtoCandidate(empContact,template,templateParams);
		
		}

		try {

			recuritmentMailConfiguration.sendRescheduleMailOnCondition(
					candidate, candidateScheduleDto, interviewr,
					candidateInterview);
		} catch (Exception e) {
			e.printStackTrace();
		}
		ReScheduleInterview(candidate.getInterview().getInterviewers(),
				candidate);

	}

	// this was used in above method previoisy to send emails
	/*
	 * mailSenderUtility.duplicateMethodForRescheduleInterview(candidate,
	 * candidateScheduleDto, interviewr, candidateInterview);
	 */



	@Override
	public Map<String, Object> interviewStatusReport(
			SearchQueryParams queryParams) {
		List<InterviewStatusReportDTO> reportDTOList = new ArrayList<InterviewStatusReportDTO>();
		List<InterviewStatusReportDTO> UniqreportDTOList = new ArrayList<InterviewStatusReportDTO>();
		Map<String, Object> interviewCycles = new HashMap<String, Object>();
		Map<String, Object> interviewCyclesMap = jobPortalDAOImpl
				.interviewStatusReport(queryParams);
		List<CandidateInterviewCycle> interviewCyclesList = (List<CandidateInterviewCycle>) interviewCyclesMap
				.get("list");
		// here we are sorting on basis of latest date
		if (interviewCyclesList != null) {
			Collections.sort(interviewCyclesList,
					new Comparator<CandidateInterviewCycle>() {
						@Override
						public int compare(CandidateInterviewCycle o1,
								CandidateInterviewCycle o2) {
							int k = 0;
							if (o1.getInterviewDate().isAfter(
									o2.getInterviewDate())) {
								k = -1;
							}
							if (o1.getInterviewDate().isBefore(
									o2.getInterviewDate())) {
								k = 1;
							}
							return k;

						}
					});
		}

		for (CandidateInterviewCycle interviewCycle : interviewCyclesList) {
			List <InterviewStatusReportDTO> remove = new ArrayList<InterviewStatusReportDTO>();
			InterviewStatusReportDTO reportDTO = new InterviewStatusReportDTO();
			reportDTO.setInterviewDate(interviewCycle.getInterviewDate()
					.toString());
			reportDTO.setCandidateName(interviewCycle.getCandidate()
					.getFirstName());
			reportDTO.setContactNumber(interviewCycle.getCandidate()
					.getMobile());
			reportDTO.setEmailId(interviewCycle.getCandidate().getEmail());
			reportDTO.setTechnology(interviewCycle.getCandidate()
					.getAppliedFor().getPositionVacant());
			reportDTO.setExperiance(interviewCycle.getCandidate()
					.getExperience());

			reportDTO.setStatus(interviewCycle.getCandidate()
					.getCandidateInterviewStatus().toString());

			reportDTO.setTimeLineStatus(interviewCycle.getCandidate()
					.getTimelineStatus());

			reportDTO.setCandidateId(interviewCycle.getCandidate()
					.getPersonId());
			// getting recruiter name
			reportDTO
					.setRecruiter(interviewCycle.getCandidate().getRecruiter() != null ? interviewCycle
							.getCandidate().getRecruiter() : "N/A");
			// getting source name
			reportDTO.setSource(interviewCycle.getCandidate().getSourcelookUp()
					.getSourceName());
			reportDTO.setInterviewResultStatus(interviewCycle.getInterviewResultStatus()!=null? interviewCycle.getInterviewResultStatus():null);
			if(!interviewCycle.getInterviewers().isEmpty() || interviewCycle.getInterviewers()!=null){
			reportDTO.setInterviewerName(interviewCycle.getInterviewers());
			}
			if(interviewCycle.getInterviewRound()!=null){
				reportDTO.setInterviewRound(interviewCycle.getInterviewRound());
				}

			int i = 0;
			if (reportDTOList.size() > 0) {// we are checking duplicate date for
											// candidate interview status
				for (InterviewStatusReportDTO reportDTO2 : reportDTOList) {
					if (reportDTO2.getCandidateId().equals(
							reportDTO.getCandidateId())) {
						i = 1;
					}
				}
			}
			if(reportDTO.getInterviewResultStatus()!=null && reportDTO.getInterviewResultStatus().equalsIgnoreCase("ADD_COMMENTS")){
				System.out.println("in add comm");
				remove.add(reportDTO);
			}
			if (i == 0) {
				reportDTOList.add(reportDTO);
				i=0;
			} else {
				i = 0;
			}
               reportDTOList.removeAll(remove);
		}
		if (queryParams.getEndIndex() <= reportDTOList.size()) {
			UniqreportDTOList = reportDTOList.subList(
					queryParams.getStartIndex(), queryParams.getEndIndex());
		} else {
			UniqreportDTOList = reportDTOList.subList(
					queryParams.getStartIndex(), reportDTOList.size());
		}

		interviewCycles.put("list", UniqreportDTOList);
		interviewCycles.put("size", reportDTOList.size());
		return interviewCycles;
	}
	public Map<String, Object> getCountryWiseCandidatesList(SearchQueryParams queryParams){
	     Map<String, Object> candidatesMap = jobPortalDAOImpl
			.getCountryWiseCandidatesList(queryParams);
	       List<Candidate> candidateList = (List) candidatesMap.get("list");
	      Integer noOfRecords = (Integer) candidatesMap.get("size");
	     List<CandidateDTO> candidateDTOList = candidateBuilder
			.convertCandidateListToDTOList(candidateList);
	      Map<String, Object> candidateDTOMap = new HashMap<String, Object>();
	     candidateDTOMap.put("list", candidateDTOList);
	     candidateDTOMap.put("size", noOfRecords);
	     System.out.println("no:" + noOfRecords);
	     return candidateDTOMap;
	}
	@Override
	@CacheEvict(value = "jobVacancies", allEntries = true)
	public void updateCandidateInterviewStatus(CandidateStatus candidateStatus) {
	
		SimpleDateFormat f2 = new SimpleDateFormat("hh:mma");
		String statusChangeTime = f2.format(new java.util.Date());
		System.out.println(new java.util.Date());
		 System.out.println(statusChangeTime) ;
		Candidate candidate = jobPortalDAOImpl.findBy(Candidate.class,
				candidateStatus.getCandidateId());
		//candidate.setHoldSubStatus(candidateStatus.getHoldSubStatus());
		String holdSubStatus ="";
		if (candidateStatus.getStatus().equalsIgnoreCase("OFFERED")) {
			JobVacancy jobVacancy = candidate.getAppliedFor();
			Integer noOfVacancies = jobVacancy.getNoOfRequirements();
			Integer noOfOffered = jobVacancy.getOffered();
			if (noOfVacancies > noOfOffered) {
				if (candidate.getCandidateInterviewStatus() != Status.OFFERED) {
					// jobVacancy.setNoOfRequirements(noOfVacancies - 1);
					jobVacancy.setOffered(noOfOffered + 1);
					dao.saveOrUpdate(jobVacancy);
				}
				candidate.setCandidateInterviewStatus(Status
						.valueOf(candidateStatus.getStatus()));
				candidate
						.setStatusComments(candidateStatus.getStatusComments());
				candidate.setStatusChangeTime(statusChangeTime);
				// candidate.setUpdatedDate(new Second());
				dao.saveOrUpdate(candidate);
				reprocessingCandidate(candidate, "OFFERED");
				completeAllPendingInertview(candidate);

			} else {
				throw new NoJobVacancyException();
			}
		} else if (candidate.getCandidateInterviewStatus() == Status.OFFERED) {
			if (candidateStatus.getStatus().equalsIgnoreCase("HOLD")
					|| candidateStatus.getStatus().equalsIgnoreCase("REJECTED")) {
				JobVacancy jobVacancy = candidate.getAppliedFor();
				jobVacancy.setOffered(jobVacancy.getOffered() - 1);
				/*
				 * jobVacancy .setPositionVacant(jobVacancy.getPositionVacant()
				 * + 1);
				 */
				dao.saveOrUpdate(jobVacancy);
				candidate.setCandidateInterviewStatus(Status
						.valueOf(candidateStatus.getStatus()));
				candidate
						.setStatusComments(candidateStatus.getStatusComments());
				candidate.setStatusChangeTime(statusChangeTime);
				// candidate.setUpdatedDate(new Second());
				dao.saveOrUpdate(candidate);
				completeAllPendingInertview(candidate);
			}
		}

		 if (!(candidateStatus.getStatus().equalsIgnoreCase("ADD_COMMENTS"))){
			candidate.setCandidateInterviewStatus(Status
					.valueOf(candidateStatus.getStatus()));
			candidate.setStatusComments(candidateStatus.getStatusComments());
			candidate.setStatusChangeTime(statusChangeTime);
			// candidate.setUpdatedDate(new Second());
			dao.saveOrUpdate(candidate);
		}
		// After Rejecting or reprocessing a candidate we are inserting one row
		// in
		// candidateInterviewCycle table saying that when candidate got rejected
		// or reprocessed
		if (candidateStatus.getStatus().equalsIgnoreCase("REJECTED")) {
			// After offered if we are rejecting candidate then here we are
			// removing from upcoming Joinee
			NewJoinee newJoinee = jobPortalDAOImpl.findnewJoinee(candidate
					.getEmail());
			if (newJoinee != null) {
				jobPortalDAOImpl.delete(newJoinee);
			}
			reprocessingCandidate(candidate, "REJECTED");
			completeAllPendingInertview(candidate);
		} else if (candidateStatus.getStatus().equalsIgnoreCase("REPROCESS")) {
	        reprocessingCandidate(candidate, "REPROCESS");
			candidate.setInitialComments("");
			completeAllPendingInertview(candidate);
		}else if(candidateStatus.getStatus().equalsIgnoreCase("HOLD")){
			 candidate.setHoldSubStatus(candidateStatus.getHoldSubStatus());
		     reprocessingCandidate(candidate,"HOLD");
		     completeAllPendingInertview(candidate);
		}
		else if(candidateStatus.getStatus().equalsIgnoreCase("ADD_COMMENTS")){
			candidate.setAddComments(candidateStatus.getStatusComments());
			 holdSubStatus+= candidate.getHoldSubStatus();
			candidate.setHoldSubStatus(holdSubStatus);
			System.out.println(holdSubStatus);
			candidate.setStatusChangeTime(statusChangeTime);
			reprocessingCandidate(candidate, "ADD_COMMENTS");
			completeAllPendingInertview(candidate);
		}
		
	}

	private void scheduleInterview(Set<Employee> interviewers,
			Candidate candidate) {
		for (Employee employee : interviewers) {
			Alert alert = alertBuilder.createScheduleInterviewAlert(employee,
					candidate.getFirstName());
			dao.save(alert);
		}
	}

	private void ReScheduleInterview(Set<Employee> interviewers,
			Candidate candidate) {
		for (Employee employee : interviewers) {
			Alert alert = alertBuilder.createScheduleInterviewAlert(employee,
					candidate.getFirstName());
			dao.save(alert);
		}
	}

	@Override
	public Boolean isCandidateMappedWithJob(String jobCode) {

		return jobPortalDAOImpl.isCandidateMappedWithJob(jobCode);

	}

	@Override
	public void deleteJoinee(Long joineeId) {
		NewJoinee newJoinee = dao.findBy(NewJoinee.class, joineeId);
		dao.delete(newJoinee);

	}

	@Override
	public Map<String, Object> searchScheduledCandidates(String skill,
			String fromDate, String toDate, String search, Integer startIndex,
			Integer endIndex) {

		Date fDate = null;
		Date tDate = null;
		try {
			fDate = DateParser.toDate(fromDate);
			tDate = DateParser.toDate(toDate);
		} catch (ParseException ex) {
			Logger.getLogger(JobPortalServiceImpl.class.getName()).log(
					Level.SEVERE, null, ex);
		}
		Map<String, Object> interviewCycles = jobPortalDAOImpl
				.searchScheduledCandidates(skill, fDate, tDate, search,
						startIndex, endIndex);
		// List<CandidateInterviewCycle> candidateInterviewCycles = (List)
		// interviewCycles
		// .get("list");
		// Integer noOfRecords = (Integer) interviewCycles.get("size");

		List<CandidateInterviewCycleDTO> cycleDTOs = candidateBuilder
				.convertCandidateInterviewDTOsListForEmployee((List) interviewCycles
						.get("list"));
		Integer noOfRecords = cycleDTOs.size();
		List<CandidateInterviewCycleDTO> cycleDTO = new ArrayList<CandidateInterviewCycleDTO>();
		// pagination because if we will do pagination from DaoImpl then there
		// is a chance of
		// getting duplicate date and after restricting duplicate, pagination
		// won't work properly.

		if (endIndex <= cycleDTOs.size() - 1) {
			cycleDTO = cycleDTOs.subList(startIndex, endIndex);
		} else {
			cycleDTO = cycleDTOs.subList(startIndex, cycleDTOs.size());
		}

		Map<String, Object> srchCandForAdmin = new HashMap<String, Object>();
		srchCandForAdmin.put("list", cycleDTO);
		srchCandForAdmin.put("size", noOfRecords);
		return srchCandForAdmin;
	}

	@Override
	public JobVacancyDTO getJobOpeningById(Long jobVacancyId) {

		return jobVacancyBuilder.createJobVacancyDTO(jobPortalDAOImpl.findBy(
				JobVacancy.class, jobVacancyId));

	}

	@Override
	public Boolean checkemail(String email) {
		return jobPortalDAOImpl.checkemail(email);
	}

	@Override
	public void addTechnology(String technology) {
		Technology technology2 = new Technology();
		technology2.setName(technology);
		dao.save(technology2);
	}

	@Override
	public List<Technology> viewTechnology() {
		List<Technology> tecList = dao.get(Technology.class);

		return tecList;
	}
	@Override
	public List<Designation> viewDesignation() {
		List<Designation> designationlist = dao.get(Designation.class);

		return designationlist;
	}

	@Override
	public void deleteTechnology(Long technologyId) {
		Technology technology = dao.findBy(Technology.class, technologyId);
		dao.delete(technology);

	}

	@Override
	public void defaultPic(String gender) {

		Long loggedempId = securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder();

		Employee employee = dao.findBy(Employee.class, loggedempId);

		String fileLocation = (String) propBean.getPropData().get(
				"fileLocation");
		String s = fileLocation + "Default_Male.jpg";
		String s1 = fileLocation + "Default_Male.jpg";

		if (gender.equalsIgnoreCase("Male")) {
			employee.setProfilePicPath(fileLocation + "Default_Male.jpg");
			employee.setThumbPicture(fileLocation + "Default_Male.jpg");
		} else {
			employee.setProfilePicPath(fileLocation + "Default_Female.jpg");
			employee.setThumbPicture(fileLocation + "Default_Female.jpg");

		}
		// dao.saveOrUpdate(employee);
	}

	@Override
	public void uploadFileInObservation(MultipartFile mf, String personId) {
		if (personId != null) {
			Long id = Long.parseLong(personId);

			Observation observation = dao.findBy(Observation.class, id);

			FileUploaderUtility fileUploaderUtil = new FileUploaderUtility();
			try {
				String path = fileUploaderUtil.uploadFileInObservation(
						observation, mf, propBean);

				observation.setObsFilePath(path);

				dao.update(observation);
			} catch (IOException ex) {

				Logger.getLogger(Observation.class.getName()).log(Level.SEVERE,
						null, ex);
				dao.update(observation);

			}
		}

	}

	@Override
	public String getpersontechnology(Long id) {

		Candidate per = dao.findBy(Candidate.class, id);
		String tech = per.getTechnology();

		return tech;
	}

	@Override
	public List<ReportiesHierarchyDTO> getReportiesUnderManager(Long empID) {
		List<Employee> employeeList = null;
		/*
		 * Map<String, Object> reporteeReportMap = null; Integer noOfRecords =
		 * null;
		 */
		if (empID != 1001L && empID != 1002L && empID != 1007L) {
			List<Long> empIds = projectService.mangerUnderManager(empID);
			employeeList = jobPortalDAOImpl.getReportiesUnderManager(empIds);
		}

		List<ReportiesHierarchyDTO> reportiesHierarchyDTOs = employeeBuilder
				.getReporteeDTOList(employeeList);

		/*
		 * noOfRecords = reportiesHierarchyDTOs.size(); if (endIndex >=
		 * noOfRecords) { endIndex = noOfRecords; }
		 * 
		 * reportiesHierarchyDTOs = reportiesHierarchyDTOs.subList(startIndex,
		 * endIndex);
		 * 
		 * reporteeReportMap = new HashMap<String, Object>();
		 * reporteeReportMap.put("list", reportiesHierarchyDTOs);
		 * reporteeReportMap.put("size", noOfRecords);
		 */

		return reportiesHierarchyDTOs;
	}

	@Override
	public void addDesignation(DesignationDto designationDto) {
		Designation designationIsExisted = jobPortalDAOImpl
				.designtionIsExisted(designationDto.getDepartmentId(),
						designationDto.getName());
		if (designationIsExisted == null) {
			Designation designation = new Designation();
			designation.setEmpDepartment(dao.findBy(EmpDepartment.class,
					designationDto.getDepartmentId()));
			designation.setName(designationDto.getName());
			designation.setCreatedBy(securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder());
			designation.setCreatedDate(new Second());
			dao.save(designation);
		} else {
			throw new DuplicateActiveCycleException();
		}
	}

	@Override
	public void deleteDesignation(Long designationId) {
		Designation designation = dao.findBy(Designation.class, designationId);
		Boolean designationIsAssigned = jobPortalDAOImpl
				.designationIsAssigned(designation);
		if (!designationIsAssigned)
			dao.delete(designation);
		else
			throw new CandidateCantBeDeletedException();
	}

	/* To Upload RBT Resume */

	@Override
	public void uploadRBTResume(MultipartFile mpf, Long employeeId) {
		if (employeeId != null) {

			String path = "N/A";

			Employee updatingEmployee = dao.findBy(Employee.class, employeeId);

			FileUploaderUtility fileUploaderUtility = new FileUploaderUtility();
			try {
				path = fileUploaderUtility.uploadRBTCvDocument(
						updatingEmployee, mpf, propBean);
				updatingEmployee.setRbtCvPath(path);
				dao.update(updatingEmployee);

			} catch (IOException ex) {
				Logger.getLogger(JobPortalServiceImpl.class.getName()).log(
						Level.SEVERE, null, ex);
			}

		}
	}

	@Override
	public void downloadRBTCv(HttpServletResponse response, String fileName) {
		try {
			FileUploaderUtil util = new FileUploaderUtil();
			util.downloadRBTCv(response, fileName, propBean);
		} catch (Exception ex) {
			throw new FileUploaderUtilException(
					"Exception occured while uploading a file in Legal "
							+ ex.getMessage(), ex);
		}
	}

	@Override
	public List<ObservationDTO> getEmployeeMyProfileObservations(
			Long loggedInEmpID) {

		Employee employee = observationDao
				.findBy(Employee.class, loggedInEmpID);
		Set<Observation> observations = null;
		observations = employee.getObservation();
		List<Observation> observationList = new ArrayList<Observation>(
				observations);
		if (observationList != null) {
			Collections.sort(observationList, new Comparator<Observation>() {
				@Override
				public int compare(Observation o1, Observation o2) {
					int k = 0;
					if (o1.getDate().isAfter(o2.getDate())) {
						k = -1;
					}
					if (o1.getDate().isBefore(o2.getDate())) {
						k = 1;
					}
					return k;
				}
			});
		}

		List<ObservationDTO> observationDTOs = observationBuilder
				.convertFormObservationToObservationDTOList(observationList);
		return observationDTOs;
	}

	// While rejecting or reprocessing a candidate
	public void reprocessingCandidate(Candidate candidate, String status) {
		Long maxinterviewRound = jobPortalDAOImpl
				.highestInterviewRound(candidate.getPersonId());
		String interviewerName = jobPortalDAOImpl.getInterviewerName(candidate
				.getPersonId());
		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		CandidateInterviewCycle candidateInterviewCycle = new CandidateInterviewCycle();
		candidateInterviewCycle.setCandidate(candidate);
		candidateInterviewCycle.setInterviewDate(new Date());
		candidateInterviewCycle.setInterviewers(interviewerName);
		candidateInterviewCycle.setUpdatedBy(employee.getEmployeeFullName());
		candidateInterviewCycle.setStatus("finished");
		
		if (status == "REPROCESS") {
			candidateInterviewCycle
					.setInterviewComments(status == "REPROCESS" ? candidate
							.getStatusComments() : status);
			candidateInterviewCycle.setInterviewResultStatus(status);
		}
		if (status == "OFFERED") {
			candidateInterviewCycle
					.setInterviewComments(status == "OFFERED" ? candidate
							.getStatusComments() : status);
			candidateInterviewCycle.setInterviewResultStatus(status);
		}
		if (status == "HOLD") {
			candidateInterviewCycle
					.setInterviewComments(status == "HOLD" ? candidate
							.getStatusComments() : status);
			candidateInterviewCycle.setInterviewResultStatus(status);
		}
		if (status == "REJECTED") {
			candidateInterviewCycle
					.setInterviewComments(status == "REJECTED" ? candidate
							.getStatusComments() : status);
			candidateInterviewCycle.setInterviewResultStatus(status);
		}
		if (status == "ADD_COMMENTS") {
			candidateInterviewCycle
			.setInterviewComments(status == "ADD_COMMENTS" ? candidate
					      .getAddComments():status);
			candidateInterviewCycle.setInterviewResultStatus(status);
			candidateInterviewCycle.setInterviewTime(candidate.getStatusChangeTime());
		}
		// If REPROCESS or DID NOT JOIN then again setting interview round as 0.
		if (!(status.equalsIgnoreCase("REPROCESS")
				|| status.equalsIgnoreCase("DID NOT JOIN") || status
					.equalsIgnoreCase("OFFER_CANCELLED"))) {
			candidateInterviewCycle.setInterviewRound(maxinterviewRound);
		} else {
			candidateInterviewCycle.setInterviewRound(0l);
		}

		jobPortalDAOImpl.save(candidateInterviewCycle);
	}

	// If candidate get selected or rejected without completing pending
	// interview then Automatically interview is completed
	public void completeAllPendingInertview(Candidate candidate) {

		List<CandidateInterviewCycle> candidateInterviewCycles = jobPortalDAOImpl
				.pendingInterviewList(candidate.getPersonId().toString());
		if (candidateInterviewCycles != null) {
			for (CandidateInterviewCycle candidateInterviewCycle : candidateInterviewCycles) {
				candidateInterviewCycle.setStatus("finished");
				if(candidateInterviewCycle.getInterviewComments() == null){
					candidateInterviewCycle.setStatus("pending");
				}else{
					candidateInterviewCycle.setStatus("finished");
				}
				dao.saveOrUpdate(candidateInterviewCycle);
			}
		}

	}

	@Override
	public void addTimeSlot(TimeSlotDTO timeSlotDTO) {
		int slotExist = jobPortalDAOImpl.checkSlot(timeSlotDTO.getName());
		if (slotExist > 0) {
			throw new DuplicateActiveCycleException();
		} else {

			TimeSlot timeSlot = new TimeSlot();
			timeSlot.setCreatedBy(securityUtils
					.getLoggedEmployeeIdforSecurityContextHolder());
			timeSlot.setCreatedDate(new Second());
			timeSlot.setName(timeSlotDTO.getName());
			timeSlot.setStartTime(timeSlotDTO.getStartTimeHour() + ":"
					+ timeSlotDTO.getStartTimeMinutes());
			timeSlot.setEndTime(timeSlotDTO.getEndTimeHour() + ":"
					+ timeSlotDTO.getEndTimeMinutes());
			timeSlot.setGraceTime(timeSlotDTO.getGraceTime());
			dao.save(timeSlot);
		}
	}

	@Override
	public List<TimeSlotDTO> getAllShifting() {
		return employeeBuilder.convertShiftToEntity(jobPortalDAOImpl
				.getAllShifting());
	}

	// To update shift details
	@Override
	public void updateShiftDetail(TimeSlotDTO timeSlotDTO) {
		dao.update(employeeBuilder.toEditEntity(timeSlotDTO));

	}

	// To delete shift details
	@Override
	public void deleteShiftDetail(Long shifId) {
		dao.delete(dao.findBy(TimeSlot.class, shifId));

	}

	@Override
	public void uploadFileForNewJoinee(MultipartFile mf, String candidateId) {
		if (candidateId != null) {
			Long id = Long.parseLong(candidateId);
			Candidate candidate = dao.findBy(Candidate.class, id);

			NewJoinee newJoinee = dao.findByUniqueProperty(NewJoinee.class,
					"candidateId", id);

			FileUploaderUtility fileUploaderUtil = new FileUploaderUtility();
			try {
				String path = fileUploaderUtil.uploadFileForNewJoinee(
						newJoinee, mf, propBean);
				newJoinee.setAttachedDocumentPath(path);
				dao.update(newJoinee);
				// As attachment should come over candidate time-line
				candidate.setOtherDocumentPath(path);
				dao.update(candidate);
			} catch (IOException ex) {

				Logger.getLogger(Observation.class.getName()).log(Level.SEVERE,
						null, ex);
				dao.update(newJoinee);

			}
		}
	}

	@Override
	public void savingInitialComments(String comments, Long personId) {
		Candidate candidate = dao.findBy(Candidate.class, personId);
		candidate.setInitialComments(comments);
		dao.update(candidate);

	}

	@SuppressWarnings("unchecked")
	@Override
	public ByteArrayOutputStream exportEmployeeData(String selectionStatus,
			String searchStr) throws IOException {
		Map<String, Object> serachEmpList = null;

	/*	List<Long> inactiveIds =null;
		List<Long> allIds  =null;
		inactiveIds = jobPortalDAOImpl.getInactiveEmpList();
		allIds = separationDaoImpl.AllEmpIds();
		inactiveIds.removeAll(allIds);*/
		if (selectionStatus.equalsIgnoreCase("pip")) {
			List<Long> employeeIds = jobPortalDAOImpl.getAllPipList();
			serachEmpList = jobPortalDAOImpl.getEmployeeList(employeeIds, null,
					null, searchStr);
		} else {
			serachEmpList = jobPortalDAOImpl.getProfilePaginationEmployeesData(
					selectionStatus, null, null, searchStr);
		}

		List<Employee> employeeList = (List<Employee>) serachEmpList
				.get("employeeList");

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
		cell0.setCellValue("Employee ID");
		cell0.setCellStyle(style);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("First Name");
		cell1.setCellStyle(style);

		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Last Name");
		cell2.setCellStyle(style);

		Cell cell3 = row1.createCell(3);
		cell3.setCellValue("Designation");
		cell3.setCellStyle(style);

		Cell cell4 = row1.createCell(4);
		cell4.setCellValue("Manager");
		cell4.setCellStyle(style);

		Cell cell5 = row1.createCell(5);
		cell5.setCellValue("Email ID");
		cell5.setCellStyle(style);

		Cell cell6 = row1.createCell(6);
		cell6.setCellValue("Phone Number");
		cell6.setCellStyle(style);

		Cell cell7 = row1.createCell(7);
		cell7.setCellValue("DOB");
		cell7.setCellStyle(style);

		Cell cell8 = row1.createCell(8);
		cell8.setCellValue("DOJ");
		cell8.setCellStyle(style);

		Cell cell9 = row1.createCell(9);
		cell9.setCellValue("Blood Group");
		cell9.setCellStyle(style);
		
		Cell cell10 = row1.createCell(10);
		cell10.setCellValue("Experience");
		cell10.setCellStyle(style);
		
		
	  if (selectionStatus.equalsIgnoreCase("InActive")
				|| selectionStatus.equalsIgnoreCase("underNotice")) {

			Cell cell11 = row1.createCell(11);
			cell11.setCellValue("Relieving Date");
			cell11.setCellStyle(style);

		}
		Cell cell12 = row1.createCell(12);
		cell12.setCellValue("Employment Type");
		cell12.setCellStyle(style);

		Cell cell13 = row1.createCell(13);
		cell13.setCellValue("Job Type");
		cell13.setCellStyle(style);

		Cell cell14 = row1.createCell(14);
		cell14.setCellValue("Country");
		cell14.setCellStyle(style);
		
		Cell cell15 = row1.createCell(15);
		cell15.setCellValue("Company Experience");
		cell15.setCellStyle(style);
		
		if (selectionStatus.equalsIgnoreCase("Contract")) {

			Cell cell16 = row1.createCell(16);
			cell16.setCellValue("Contract Start Date");
			cell16.setCellStyle(style);
			
			Cell cell17 = row1.createCell(17);
			cell17.setCellValue("Contract End Date");
			cell17.setCellStyle(style);

		}

		for (Employee dto : employeeList) {

			Row row = sheet.createRow(rowIndex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(dto.getEmployeeId());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(dto.getFirstName());

			Cell cel2 = row.createCell(2);
			cel2.setCellValue(dto.getLastName());

			Cell cel3 = row.createCell(3);
			cel3.setCellValue(dto.getDesignation());

			String manager = dto.getManager().getFullName();

			Cell cel4 = row.createCell(4);
			cel4.setCellValue(manager != null ? manager : "N/A");

			Cell cel5 = row.createCell(5);
			cel5.setCellValue(dto.getEmail());

			Cell cel6 = row.createCell(6);
			cel6.setCellValue(dto.getPhone() != null ? dto.getPhone() : "N/A");

			Cell cel7 = row.createCell(7);
			cel7.setCellValue(dto.getDob() != null ? dto.getDob().toString(
					"dd/MM/yyyy") : "N/A");

			Cell cel8 = row.createCell(8);
			cel8.setCellValue(dto.getJoiningDate() != null ? dto
					.getJoiningDate().toString("dd/MM/yyyy") : "N/A");

			Cell cel9 = row.createCell(9);
			cel9.setCellValue(dto.getBloodgroup() != null ? dto.getBloodgroup()
					: "N/A");
			
			Cell cel10 = row.createCell(10);
			if(dto.getExperience() != null && dto.getCompanyExperience() != null){
				String oldExp = String.valueOf(dto.getExperience());
				String cureentExp = String.valueOf(dto.getCompanyExperience());
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
					 cel10.setCellValue(updatedExperience);
				}
				else{
					
					 int year = oldYear+newYear;
					 int month = oldMonth+newMonth;
					 String date = String.valueOf(year)+"."+String.valueOf(month);
					 Float f = new Float("20.75f");
					float retval = f.parseFloat(date);
					double roundedFloat = Math.round(retval * 100.0) / 100.0;
					 Double updatedExperience =(double) (roundedFloat);
					 cel10.setCellValue(updatedExperience);
				}
				
			}
			else if(dto.getExperience() != null){
				cel10.setCellValue(dto.getExperience());
			}
			

			if (selectionStatus.equalsIgnoreCase("InActive")) {
				Cell cel11 = row.createCell(11);
				cel11.setCellValue(dto.getReleavingDate() != null ? dto
						.getReleavingDate().toString("dd/MM/yyyy") : "N/A");
			} else if (selectionStatus.equalsIgnoreCase("underNotice")) {

				Cell cel11 = row.createCell(11);
				cel11.setCellValue(dto.getUnderNoticeDate() != null ? dto
						.getUnderNoticeDate().toString("dd/MM/yyyy") : "N/A");
			}
			
			Cell cel12 = row.createCell(12);
			cel12.setCellValue(dto.getEmploymentTypeName() != null ? dto.getEmploymentTypeName()
					: "N/A");
			
			Cell cel13 = row.createCell(13);
			cel13.setCellValue(dto.getJobTypeName() != null ? dto.getJobTypeName()
					: "N/A");
			Cell cel14 = row.createCell(14);
			cel14.setCellValue(dto.getCountry() != null ? dto.getCountry()
					: "N/A");
			Cell cel15 = row.createCell(15);
			cel15.setCellValue(dto.getCompanyExperience() != null ? dto.getCompanyExperience().toString()
					: "N/A");
			
			if (selectionStatus.equalsIgnoreCase("Contract")) {
				Cell cel16 = row.createCell(16);
				cel16.setCellValue(dto.getContractStartDate()!= null ? dto
						.getContractStartDate().toString("dd/MM/yyyy") : "N/A");
				 Cell cel17 = row.createCell(17);
					cel17.setCellValue(dto.getContractEndDate()!= null ? dto
							.getContractEndDate().toString("dd/MM/yyyy") : "N/A");
			}

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
			sheet.autoSizeColumn(16);
			sheet.autoSizeColumn(17);
		}

		workbook.write(bos);
		bos.flush();
		bos.close();
		// for exporting code end here
		return bos;
	}
	@SuppressWarnings("unchecked")
	@Override
	public ByteArrayOutputStream exportEmployeeCategoryData(String selectionDesignation) throws IOException {
		Map<String, Object> serachEmpList = null;

			serachEmpList = jobPortalDAOImpl.getEmployeeCategoryData(
					selectionDesignation, null, null);
		

		List<Employee> employeeList = (List<Employee>) serachEmpList
				.get("employeeList");

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
		cell0.setCellValue("Employee ID");
		cell0.setCellStyle(style);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("First Name");
		cell1.setCellStyle(style);

		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Last Name");
		cell2.setCellStyle(style);

		Cell cell3 = row1.createCell(3);
		cell3.setCellValue("Designation");
		cell3.setCellStyle(style);

		Cell cell4 = row1.createCell(4);
		cell4.setCellValue("Manager");
		cell4.setCellStyle(style);

		Cell cell5 = row1.createCell(5);
		cell5.setCellValue("Email ID");
		cell5.setCellStyle(style);

		Cell cell6 = row1.createCell(6);
		cell6.setCellValue("Phone Number");
		cell6.setCellStyle(style);

		Cell cell7 = row1.createCell(7);
		cell7.setCellValue("DOB");
		cell7.setCellStyle(style);

		Cell cell8 = row1.createCell(8);
		cell8.setCellValue("DOJ");
		cell8.setCellStyle(style);

		Cell cell9 = row1.createCell(9);
		cell9.setCellValue("Blood Group");
		cell9.setCellStyle(style);
		
		Cell cell10 = row1.createCell(10);
		cell10.setCellValue("Experience");
		cell10.setCellStyle(style);
	
		Cell cell11 = row1.createCell(11);
		cell11.setCellValue("Employment Type");
		cell11.setCellStyle(style);

		Cell cell12 = row1.createCell(12);
		cell12.setCellValue("Job Type");
		cell12.setCellStyle(style);

		for (Employee dto : employeeList) {

			Row row = sheet.createRow(rowIndex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(dto.getEmployeeId());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(dto.getFirstName());

			Cell cel2 = row.createCell(2);
			cel2.setCellValue(dto.getLastName());

			Cell cel3 = row.createCell(3);
			cel3.setCellValue(dto.getDesignation());

			String manager = dto.getManager().getFullName();

			Cell cel4 = row.createCell(4);
			cel4.setCellValue(manager != null ? manager : "N/A");

			Cell cel5 = row.createCell(5);
			cel5.setCellValue(dto.getEmail());

			Cell cel6 = row.createCell(6);
			cel6.setCellValue(dto.getPhone() != null ? dto.getPhone() : "N/A");

			Cell cel7 = row.createCell(7);
			cel7.setCellValue(dto.getDob() != null ? dto.getDob().toString(
					"dd/MM/yyyy") : "N/A");

			Cell cel8 = row.createCell(8);
			cel8.setCellValue(dto.getJoiningDate() != null ? dto
					.getJoiningDate().toString("dd/MM/yyyy") : "N/A");

			Cell cel9 = row.createCell(9);
			cel9.setCellValue(dto.getBloodgroup() != null ? dto.getBloodgroup()
					: "N/A");
			
			Cell cel10 = row.createCell(10);
			if(dto.getExperience() != null && dto.getCompanyExperience() != null){
				String oldExp = String.valueOf(dto.getExperience());
				String cureentExp = String.valueOf(dto.getCompanyExperience());
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
					 cel10.setCellValue(updatedExperience);
				}
				else{
					
					 int year = oldYear+newYear;
					 int month = oldMonth+newMonth;
					 String date = String.valueOf(year)+"."+String.valueOf(month);
					 Float f = new Float("20.75f");
					float retval = f.parseFloat(date);
					double roundedFloat = Math.round(retval * 100.0) / 100.0;
					 Double updatedExperience =(double) (roundedFloat);
					 cel10.setCellValue(updatedExperience);
				}
				
			}
			else if(dto.getExperience() != null){
				cel10.setCellValue(dto.getExperience());
			}
			
			
			Cell cel11 = row.createCell(11);
			cel11.setCellValue(dto.getEmploymentTypeName() != null ? dto.getEmploymentTypeName()
					: "N/A");
			
			Cell cel12 = row.createCell(11);
			cel12.setCellValue(dto.getJobTypeName() != null ? dto.getJobTypeName()
					: "N/A");

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
		}

		workbook.write(bos);
		bos.flush();
		bos.close();
		// for exporting code end here
		return bos;
	}

	// uploading candidates records (through Excel Sheet) to database
	@Override
	public List<Map<String, String>> uploadBulkOfCandidates(MultipartFile mpf) {
		List<Integer> integersList = new ArrayList<>();
		List<Integer> recordsNotInserted = new ArrayList<>();
		int candidates = 0;
		Sheet sheet = null;
		// TODO Auto-generated method stub
		try {
			Workbook workbook = null;
			FormulaEvaluator formulaEvaluator = null;
			DataFormatter dataFormatter = new DataFormatter();
			// creating object according to the file format
			if (mpf.getOriginalFilename().toLowerCase().endsWith("xlsx")) {
				workbook = new XSSFWorkbook(mpf.getInputStream());
				formulaEvaluator = new XSSFFormulaEvaluator(
						(XSSFWorkbook) workbook);

			} else if (mpf.getOriginalFilename().toLowerCase().endsWith("xls")) {
				workbook = new HSSFWorkbook(mpf.getInputStream());
				formulaEvaluator = new HSSFFormulaEvaluator(
						(HSSFWorkbook) workbook);
			}
			sheet = workbook.getSheetAt(0);
			int totalRecords = (sheet.getPhysicalNumberOfRows());
			Iterator<Row> rows = sheet.rowIterator();
			// If any record goes invalid then we are continuing next record
			candidate: while (rows.hasNext()) {
				Row row = (Row) rows.next();
				if (row.getRowNum() > 0) {
					// if any row containing empty cells
					Boolean isEmptyornull = rowValidation(row);
					if (!isEmptyornull) {
						continue;
					}

					CandidateScheduleDto csDto = new CandidateScheduleDto();
					Iterator<Cell> cells = row.cellIterator();
					// mail pattern validation
					String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
							+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
					Pattern pattern = Pattern.compile(EMAIL_PATTERN);

					if (row.getCell(2) == null) {
						continue;
					}
					Matcher matcher = pattern.matcher(row.getCell(2)
							.getStringCellValue());

					if (!matcher.matches()) {
						continue;
					}
					// email exits or not
					Boolean ismail = isCandidateMailExists(row.getCell(2)
							.getStringCellValue().trim());

					if (ismail) {
						continue;
					}
					/*
					 * //mobile number exits or not String mblNumber =
					 * Double.valueOf
					 * (row.getCell(3).getNumericCellValue()).toString();
					 * Boolean isMobileNumber =
					 * isCandidateMobileNumberExists(mblNumber);
					 * if(isMobileNumber) { continue; }
					 */
					String years = "";
					while (cells.hasNext()) {
						String experience = "";
						Cell cell = (Cell) cells.next();

						switch (cell.getColumnIndex()) {
						case 0:
							// Pattern patternFirstName =
							// Pattern.compile("\\d+");
							Pattern patternFirstName = Pattern
									.compile("[a-zA-Z]+");
							if (!patternFirstName.matcher(
									cell.getStringCellValue().trim()).matches()) {
								continue candidate;
							}
							csDto.setCandidateFirstName(cell
									.getStringCellValue().trim());
							break;
						case 1:
							Pattern patternLastName = Pattern
									.compile("[a-zA-Z]+");
							if (!patternLastName.matcher(
									cell.getStringCellValue().trim()).matches()) {
								continue candidate;
							}
							csDto.setCandidateLastName(cell
									.getStringCellValue().trim());
							break;
						case 2:
							csDto.setCandidateEmail(cell.getStringCellValue()
									.trim());
							break;
						case 3:
							formulaEvaluator.evaluate(cell);
							String mblNunbercellvalue = dataFormatter
									.formatCellValue(cell, formulaEvaluator);
							Pattern re = Pattern.compile("\\d+");
							Matcher m = re.matcher(mblNunbercellvalue);
							if (!m.matches()) {
								continue candidate;
							}
							if (mblNunbercellvalue.length() != 10) {
								continue candidate;
							}
							csDto.setMobile(mblNunbercellvalue);
							/*
							 * System.out.println("mobile"+cell.getNumericCellValue
							 * ()); try { String mbl =
							 * Double.valueOf(cell.getNumericCellValue
							 * ()).toString(); mbl = "" + mbl.charAt(0) +
							 * mbl.substring(2, mbl.indexOf("E"));
							 * if(mbl.length()!= 10) { continue candidate; }
							 * csDto.setMobile(mbl); } catch (Exception e) { //
							 * TODO: handle exception continue candidate; }
							 */

							break;
						case 4:
							Technology technology = dao.findByUniqueProperty(
									Technology.class, "name", cell
											.getStringCellValue().trim());
							if (technology == null) {
								continue candidate;
							}
							csDto.setTechnology(cell.getStringCellValue()
									.trim());
							break;
						case 5:
							csDto.setSkills(cell.getStringCellValue().trim());
							break;
						case 6:
							years = Double.valueOf(cell.getNumericCellValue())
									.toString().trim();
							years = years.substring(0, years.indexOf("."));
							if (years.length() > 3) {
								continue candidate;
							}
							break;
						case 7:
							String months = Double
									.valueOf(cell.getNumericCellValue())
									.toString().trim();
							months = months.substring(0, months.indexOf("."));
							double d = cell.getNumericCellValue();
							if (d > 11 || d < 0) {
								continue candidate;
							}
							csDto.setExperience(years + "." + months);

							break;
						case 8:
							/*
							 * SourceLookUp lookUp =
							 * dao.findByUniqueProperty(SourceLookUp
							 * .class,"sourceType",cell.
							 * getStringCellValue().trim()); if(lookUp == null)
							 * { continue candidate; }
							 */

							String sourceValue = "";
							sourceValue = cell.getStringCellValue().trim();
							if (!sourceValue.equalsIgnoreCase("External")
									&& !sourceValue
											.equalsIgnoreCase("Internal")
									&& !sourceValue.equalsIgnoreCase("Others")) {
								csDto.setSourceType("Others");
							} else if (sourceValue.equalsIgnoreCase("external")) {
								csDto.setSourceType("External");

							} else {
								csDto.setSourceType(cell.getStringCellValue());
							}
							break;
						case 9:
							if (row.getCell(8).getStringCellValue().trim()
									.equalsIgnoreCase("Internal")) {
								String employeeName = cell.getStringCellValue();
								String[] name = employeeName.split(" ");
								if (name.length != 2) {
									continue candidate;
								} else {
									String empFirstName = name[0];
									String empLastName = name[1];

									if (jobPortalDAOImpl.internalSource(
											empFirstName, empLastName)) {
										csDto.setSourceName(empFirstName + " "
												+ empLastName);
									} else {
										continue candidate;
									}

								}

							} else {
								csDto.setSourceName(cell.getStringCellValue());
							}

							break;
						case 10:
							String jobCodeValue = "";
							if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC) {
								jobCodeValue = "" + cell.getNumericCellValue();
								jobCodeValue = jobCodeValue.substring(0,
										jobCodeValue.indexOf("."));
							} else {
								jobCodeValue = cell.getStringCellValue().trim();
							}
							JobVacancy jobVacancy = dao.findByUniqueProperty(
									JobVacancy.class, "jobCode", jobCodeValue);
							if (jobVacancy != null) {
								JobVacancyDTO vacancyDTO = new JobVacancyDTO();
								vacancyDTO.setJobCode(jobVacancy.getJobCode());
								vacancyDTO.setId(jobVacancy.getJobVacancyId());
								csDto.setAppliedFor(vacancyDTO);

							} else {
								continue candidate;
							}
							break;

						case 11:

							try {
								SimpleDateFormat sdf = new SimpleDateFormat(
										"dd/MM/yyyy");
								String dob = sdf
										.format(cell.getDateCellValue());
								System.out.println("line no 2574" + dob);
								csDto.setDob(dob);
							} catch (Exception e) {
								logger1.warn("line no 2568 In valid date format");
								continue candidate;
							}

							/*
							 * Date dob = new
							 * Date(cell.getDateCellValue().getTime());
							 * System.out.println(dob);
							 * csDto.setDob(dob.toString("dd/MM/yyyy"));
							 */
							break;

						case 12:
							if (formulaEvaluator.evaluate(cell) == null) {
								continue candidate;
							}
							formulaEvaluator.evaluate(cell);
							String ctccellvalue = dataFormatter
									.formatCellValue(cell, formulaEvaluator);
							csDto.setCtc(ctccellvalue);

							break;

						case 13:
							if (formulaEvaluator.evaluate(cell) == null) {
								continue candidate;
							}
							formulaEvaluator.evaluate(cell);
							String ectccellvalue = dataFormatter
									.formatCellValue(cell, formulaEvaluator);
							csDto.setEctc(ectccellvalue);

							break;

						case 14:
							String currentEmployer = cell.getStringCellValue()
									.trim();
							csDto.setCurrentEmployer(currentEmployer);
							break;

						case 15:
							String currentLocation = cell.getStringCellValue()
									.trim();
							csDto.setCurrentLocation(currentLocation);
							break;

						case 16:
							try {
								String stringNp = cell.getRichStringCellValue()
										.getString();
								csDto.setNp(stringNp);
								System.out.println("line no 2636"
										+ csDto.getNp());
							} catch (Exception e) {
								continue candidate;
							}
							/*
							 * String stringNp =
							 * cell.getRichStringCellValue().getString();
							 * csDto.setNp(stringNp);
							 * System.out.println("line no 2636"+csDto.getNp());
							 */
							break;

						case 17:
							String reason = cell.getRichStringCellValue()
									.getString();
							csDto.setReason(reason);
						}

					}
					addNewCandidadate(csDto);
					candidates++;
					// getting added records row number
					integersList.add(row.getRowNum());
				}
			}

			// loop for getting not inserted records
			for (int i = 1; i < totalRecords; i++) {
				if (!(integersList.contains(i))) {
					recordsNotInserted.add(i);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return candidatesNotInserted(recordsNotInserted, sheet);

	}

	// method for getting non- inserted candidates
	public List<Map<String, String>> candidatesNotInserted(
			List<Integer> integers, Sheet sheet) {
		List<Map<String, String>> list = null;
		if (integers.size() == 0) {
			return list;
		} else {
			list = new ArrayList<>();
			for (Integer notInsertedRecord : integers) {
				Map<String, String> map = new HashMap<>();
				// notInsertedRecord.get
				Row row = sheet.getRow(notInsertedRecord);
				map.put("sno", "" + (notInsertedRecord + 1));
				Cell cell = row.getCell(0);
				if (cell != null) {
					map.put("firstName", cell.getStringCellValue());
				}
				Cell cell2 = row.getCell(2);
				if (cell2 != null) {
					map.put("email", cell2.getStringCellValue());
				}
				list.add(map);
			}
		}
		return list;
	}

	// for checking fields empty or not in candidate sheet
	public Boolean rowValidation(Row row) {
		for (int i = 0; i <= 17; i++) {
			Cell cell = row.getCell(i);

			if (i == 14) {
				continue;
			}

			if (cell != null) {

				if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
					// System.out.println("cell value"+cell.getStringCellValue());
					if (cell.getStringCellValue().isEmpty()) {
						return false;
					}

				} else {
					// System.out.println("cell value"+cell.getNumericCellValue());
					String numericvalue = Double.valueOf(
							cell.getNumericCellValue()).toString();
					if (numericvalue.isEmpty()) {
						return false;
					}
				}
			} else {
				return false;
			}

		}
		return true;
	}

	/*
	 * Iterator<Cell> cellvalue = row.cellIterator(); //for()
	 * while(cellvalue.hasNext()) {
	 * 
	 * Cell cell = cellvalue.next(); if (cell.getCellType() ==
	 * Cell.CELL_TYPE_STRING) { if (cell.getStringCellValue().isEmpty()) {
	 * return false; }
	 * 
	 * } else { String numericvalue =
	 * Double.valueOf(cell.getNumericCellValue()).toString(); if
	 * (numericvalue.isEmpty()) { return false; } } }
	 * 
	 * return true;
	 * 
	 * }
	 */

	@Override
	public List<EmployeeDTO> getAllHrData() {

		List<Employee> HrDetailsList = dao.getHrLookUp();
		List<EmployeeDTO> empdtoList = new ArrayList<EmployeeDTO>();
		for (Employee e : HrDetailsList) {
			EmployeeDTO employeeDTO = new EmployeeDTO();
			employeeDTO.setId(e.getEmployeeId());
			employeeDTO.setFirstName(e.getFirstName());
			employeeDTO.setLastName(e.getLastName());
			employeeDTO.setFullName(e.getFullName());
			empdtoList.add(employeeDTO);
		}
		return empdtoList;
	}
	@Override
	public List<CountryLookUpDTO> getEmpCountries() {

		List<CountryLookUp> countriesList = dao.getEmpCountries();
		List<CountryLookUpDTO> empdtoList = new ArrayList<CountryLookUpDTO>();
		for (CountryLookUp e : countriesList) {
			CountryLookUpDTO employeeCountryLookupDTO = new CountryLookUpDTO();
			employeeCountryLookupDTO.setId(e.getId());
			employeeCountryLookupDTO.setName(e.getName());
			employeeCountryLookupDTO.setMobileCode(e.getMobileCode());		
			employeeCountryLookupDTO.setCountryCode(e.getCountryCode());
			empdtoList.add(employeeCountryLookupDTO);
		}
		return empdtoList;
	}

	@Override
	public List<Object> getCandidatesListCount(String selectionStatus,
			String fromDate, String toDate) {
		List<Object> list = jobPortalDAOImpl.getCandidatesListCount(
				selectionStatus, fromDate, toDate);

		ArrayList<Object> arrayList = new ArrayList<>();
		Iterator<Object> it = list.iterator();
		while (it.hasNext()) {
			Object[] obj = (Object[]) it.next();
			HashMap hashMap = new HashMap<>();
			hashMap.put("CANDIDATE_INTERVIEW_STATUS", obj[0]);
			hashMap.put("count", obj[1]);
			arrayList.add(hashMap);

		}

		return arrayList;
	}

	// upload Passport Front Page
	public void uploadPassportFrontCopy(MultipartFile mpf, String id) {
		Long employeeId = Long.parseLong(id);
		Employee employee = dao.findBy(Employee.class, employeeId);
		FileUploaderUtility fileUploaderUtility = new FileUploaderUtility();
		try {
			dao.update(fileUploaderUtility.uploadPassportFrontCopy(mpf,
					employee, propBean));
		} catch (Exception ex) {
			Logger.getLogger(JobPortalServiceImpl.class.getName()).log(
					Level.SEVERE, null, ex);
			dao.update(employee);
		}

	}

	// upload Passport Back Page
	public void uploadPassportBackCopy(MultipartFile mpf, String id) {
		Long employeeId = Long.parseLong(id);
		Employee employee = dao.findBy(Employee.class, employeeId);
		FileUploaderUtility fileUploaderUtility = new FileUploaderUtility();
		try {
			dao.update(fileUploaderUtility.uploadPassportBackCopy(mpf,
					employee, propBean));
		} catch (Exception ex) {
			Logger.getLogger(JobPortalServiceImpl.class.getName()).log(
					Level.SEVERE, null, ex);
			dao.update(employee);
		}

	}

	// upload Visa Deatils Page
	public void uploadVisaDetailsCopy(MultipartFile mpf, String visaId) {
		Long id = Long.parseLong(visaId);
		VisaDetails visaDetails = dao.findBy(VisaDetails.class, id);
		FileUploaderUtility fileUploaderUtility = new FileUploaderUtility();
		try {
			dao.saveOrUpdate((fileUploaderUtility.uploadVisaDetailsCopy(mpf,
					visaDetails, propBean)));
		} catch (Exception ex) {
			Logger.getLogger(JobPortalServiceImpl.class.getName()).log(
					Level.SEVERE, null, ex);
			dao.saveOrUpdate(visaDetails);
		}

	}

	// ===========
	public PassportDTO getPassportFrontImage(Long id) {
		String imageUrl;
		Employee employee = dao.findBy(Employee.class, id);
		imageUrl = employee.getPassportFrontPagePath();
		System.out.println(imageUrl);
		if (!"undefined".equalsIgnoreCase(imageUrl) && imageUrl != ""
				&& imageUrl != null) {
			return new PassportDTO(getImageExtesion(imageUrl),
					getImageByteData(imageUrl), imageUrl, id);
		} else {
			return new PassportDTO(null, null, null, id);
		}
	}

	// =========
	public PassportBackPageDTO getPassportBackImage(Long id) {
		String imageUrl;
		Employee employee = dao.findBy(Employee.class, id);
		imageUrl = employee.getPassportBackPagePath();
		logger1.warn(imageUrl);
		if (!"undefined".equalsIgnoreCase(imageUrl) && imageUrl != ""
				&& imageUrl != null) {
			return new PassportBackPageDTO(getImageExtesion(imageUrl),
					getImageByteData(imageUrl), imageUrl, id);
		} else {
			return new PassportBackPageDTO(null, null, null, id);
		}
	}

	// ===========
	public VisaImageDTO getVisaImage(Long id) {
		String imageUrl = "";
		VisaDetails visaDetails = dao.findBy(VisaDetails.class, id);
		imageUrl = visaDetails.getVisaDetailsPath();
		if (!"undefined".equalsIgnoreCase(imageUrl) && imageUrl != ""
				&& imageUrl != null) {
			return new VisaImageDTO(getImageExtesion(imageUrl),
					getImageByteData(imageUrl), imageUrl, id);
		} else {
			return new VisaImageDTO(null, null, null, id);
		}
	}

	// gettting employee reviews
	@Override
	public List<AppraisalFormListDto> getLoggedInEmployeeReviews(Long employeeId) {
		// TODO Auto-generated method stub
		List<Long> appraisalCycleIds = appraisalDao.getAppraisalCycleIds();

		List<AppraisalFormListDto> appraisalFormListDtos = new ArrayList<AppraisalFormListDto>();
		List<AppraisalForm> appraisalForm = new ArrayList<>();
		if(!appraisalCycleIds.isEmpty()) {
		
		 appraisalForm = appraisalDao
				.getLoggedInEmployeeAppraisalForm(employeeId, appraisalCycleIds);
		}
		

		if (appraisalForm != null) {
			for (AppraisalForm appraisalform : appraisalForm) {
				appraisalFormListDtos.add(appraisalFormBuilder
						.toFormList(appraisalform));
			}
		}
		return appraisalFormListDtos;
	}

	@Override
	public ByteArrayOutputStream exportInterviewStatusReport(
			String candidateStatus, String fromDate,
			String searchByCandidateName, String searchByMultipleFlag,
			String searchByRecruiterName, String searchBySourceName,
			String selectionStatus, String selectionTechnology, String toDate)
			throws IOException {

		SearchQueryParams queryParams = new SearchQueryParams();
		queryParams.setCandidateStatus(candidateStatus);
		queryParams.setFromDate(fromDate);
		queryParams.setSearchByCandidateName(searchByCandidateName);
		queryParams.setSearchByMultipleFlag(searchByMultipleFlag);
		queryParams.setSearchByRecruiterName(searchByRecruiterName);
		queryParams.setSearchBySourceName(searchBySourceName);
		queryParams.setSelectionStatus(selectionStatus);
		queryParams.setSelectionTechnology(selectionTechnology);
		queryParams.setToDate(toDate);

		List<InterviewStatusReportDTO> reportDTOList = new ArrayList<InterviewStatusReportDTO>();
		List<InterviewStatusReportDTO> UniqreportDTOList = new ArrayList<InterviewStatusReportDTO>();
		Map<String, Object> interviewCycles = new HashMap<String, Object>();
		Map<String, Object> interviewCyclesMap = jobPortalDAOImpl
				.interviewStatusReport(queryParams);
		List<CandidateInterviewCycle> interviewCyclesList = (List<CandidateInterviewCycle>) interviewCyclesMap
				.get("list");
		// here we are sorting on basis of latest date
		if (interviewCyclesList != null) {
			Collections.sort(interviewCyclesList,
					new Comparator<CandidateInterviewCycle>() {
						@Override
						public int compare(CandidateInterviewCycle o1,
								CandidateInterviewCycle o2) {
							int k = 0;
							if (o1.getInterviewDate().isAfter(
									o2.getInterviewDate())) {
								k = -1;
							}
							if (o1.getInterviewDate().isBefore(
									o2.getInterviewDate())) {
								k = 1;
							}
							return k;

						}
					});
		}

		for (CandidateInterviewCycle interviewCycle : interviewCyclesList) {
			InterviewStatusReportDTO reportDTO = new InterviewStatusReportDTO();
			reportDTO.setInterviewDate(interviewCycle.getInterviewDate()
					.toString());
			reportDTO.setCandidateName(interviewCycle.getCandidate()
					.getFirstName());
			reportDTO.setContactNumber(interviewCycle.getCandidate()
					.getMobile());
			reportDTO.setEmailId(interviewCycle.getCandidate().getEmail());
			reportDTO.setTechnology(interviewCycle.getCandidate()
					.getAppliedFor().getPositionVacant());
			reportDTO.setExperiance(interviewCycle.getCandidate()
					.getExperience());

			reportDTO.setStatus(interviewCycle.getCandidate()
					.getCandidateInterviewStatus().toString());

			reportDTO.setTimeLineStatus(interviewCycle.getCandidate()
					.getTimelineStatus());

			reportDTO.setCandidateId(interviewCycle.getCandidate()
					.getPersonId());
			// getting recruiter name
			reportDTO
					.setRecruiter(interviewCycle.getCandidate().getRecruiter() != null ? interviewCycle
							.getCandidate().getRecruiter() : "N/A");
			// getting source name
			reportDTO.setSource(interviewCycle.getCandidate().getSourcelookUp()
					.getSourceName());
			reportDTO.setInterviewerName(interviewCycle.getInterviewers());
			if(interviewCycle.getInterviewRound()!=null){
			reportDTO.setInterviewRound(interviewCycle.getInterviewRound());
			}

			int i = 0;
			if (reportDTOList.size() > 0) {// we are checking duplicate date for
											// candidate interview status
				for (InterviewStatusReportDTO reportDTO2 : reportDTOList) {
					if (reportDTO2.getCandidateId().equals(
							reportDTO.getCandidateId())) {
						i = 1;
					}
				}
			}

			if (i == 0) {
				reportDTOList.add(reportDTO);
			} else {
				i = 0;
			}

		}

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

		Cell cel0 = row1.createCell(0);
		cel0.setCellStyle(style);
		cel0.setCellValue("Interview Date");

		Cell cel1 = row1.createCell(1);
		cel1.setCellStyle(style);
		cel1.setCellValue("Name");

		Cell cel2 = row1.createCell(2);
		cel2.setCellStyle(style);
		cel2.setCellValue("Mobile");

		Cell cel3 = row1.createCell(3);
		cel3.setCellStyle(style);
		cel3.setCellValue("Email ID");

		Cell cel4 = row1.createCell(4);
		cel4.setCellStyle(style);
		cel4.setCellValue("Technology");

		Cell cel5 = row1.createCell(5);
		cel5.setCellStyle(style);
		cel5.setCellValue("Experience");

		Cell cel6 = row1.createCell(6);
		cel6.setCellStyle(style);
		cel6.setCellValue("Source");

		Cell cel7 = row1.createCell(7);
		cel7.setCellStyle(style);
		cel7.setCellValue("Recruiter");

		Cell cel8 = row1.createCell(8);
		cel8.setCellStyle(style);
		cel8.setCellValue("Status");
		
		Cell cel9 = row1.createCell(9);
		cel9.setCellStyle(style);
		cel9.setCellValue("Interviewer Name");
		
		Cell cel10 = row1.createCell(10);
		cel10.setCellStyle(style);
		cel10.setCellValue("Interviewer Round");

		for (InterviewStatusReportDTO record : reportDTOList) {
			Row row = sheet.createRow(rowindex++);

			Cell cell0 = row.createCell(0);
			cell0.setCellValue(record.getInterviewDate());

			Cell cell1 = row.createCell(1);
			cell1.setCellValue(record.getCandidateName());

			Cell cell2 = row.createCell(2);
			cell2.setCellValue(record.getContactNumber());

			Cell cell3 = row.createCell(3);
			cell3.setCellValue(record.getEmailId());

			Cell cell4 = row.createCell(4);
			cell4.setCellValue(record.getTechnology());

			Cell cell5 = row.createCell(5);
			cell5.setCellValue(record.getExperiance());

			Cell cell6 = row.createCell(6);
			cell6.setCellValue(record.getSource());

			Cell cell7 = row.createCell(7);
			cell7.setCellValue(record.getRecruiter());

			Cell cell8 = row.createCell(8);
			cell8.setCellValue(record.getStatus());
			
			Cell cell9 = row.createCell(9);
			cell9.setCellValue(record.getInterviewerName());
			
			Cell cell10 = row.createCell(10);
			cell10.setCellValue(record.getInterviewRound());

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
		}

		workbook.write(bos);
		bos.flush();
		bos.close();

		return bos;

	}

	@Override
	public Map<String, Object> getAllCompanies(Integer startIndex,
			Integer endIndex, String searchCompany, String selectionTechnology) {

		Map<String, Object> CompniesList = null;
		CompniesList = jobPortalDAOImpl.getAllCompanies(startIndex, endIndex,
				searchCompany, selectionTechnology);
		List<Companies> companiesMapList = (List<Companies>) CompniesList
				.get("list");

		Integer noOfRecords = (Integer) CompniesList.get("size");
		//logger1.warn("total size:"+noOfRecords);

		List<CompanyDTO> companiesData = panelBuilder
				.convertCompaniesListToCompaniesDTOList(companiesMapList);

		List<CompaniesDTO> companyList = new ArrayList<CompaniesDTO>();

		CompaniesDTO companydto = null;


		if (companiesData != null) {
			for (CompanyDTO comList : companiesData) {

				if (comList != null) {

					CompaniesDTO countmap = getCount(
							((comList.getCompanyName())), selectionTechnology)
							.get("list");
					if(countmap != null) {
					companyList.add(countmap);
					}

				}

			}

		}

		Map<String, Object> companiesDataMap = new HashMap<String, Object>();
		//logger1.warn("filter size:"+companyList.size());
		Integer listsize = (Integer) companyList.size();
		if (endIndex <= companyList.size()) {
			companyList = companyList.subList(startIndex, endIndex);
		} else {
			companyList = companyList.subList(startIndex, companyList.size());
		}
		companiesDataMap.put("list", companyList);
		companiesDataMap.put("size", listsize);
		//logger1.warn("after index size:"+listsize);
		return companiesDataMap;

	}

	Map<String, CompaniesDTO> getCount(String ComName,
			String selectionTechnology) {
		Map<String, CompaniesDTO> countMap = new HashMap<String, CompaniesDTO>();
		CompaniesDTO companydto = null;

		Long candidatesCount = jobPortalDAOImpl.getCandidatesCount(ComName,
				selectionTechnology);
		Long employeeCount = jobPortalDAOImpl.getemployessCount(ComName,
				selectionTechnology);
		if(candidatesCount != 0 || employeeCount != 0) {
			companydto = new CompaniesDTO();
			companydto.setCompanyNmae(ComName);
			companydto.setCandidatesCount(candidatesCount);
			companydto.setEmployeesCount(employeeCount);
		}
		

		countMap.put("list", companydto);

		return countMap;

	}

	@Override
	public List<CompanyDTO> getAllCompaniesData() {
		List<Companies> comapnies = dao.get(Companies.class);
		return panelBuilder.convertCompaniesListToCompaniesDTOList(comapnies);
	}

	@Override
	public Map<String, Object>  getAllCandidatesInfo(Integer startIndex,
			Integer endIndex,String companyName) {

		Map<String, Object>  candidatesList = jobPortalDAOImpl
				.getAllCandidatesInfo( startIndex,
						 endIndex,companyName);
		
		List<Candidate> companiesMapList = (List<Candidate>) candidatesList
				.get("list");
		
		Integer noOfRecords = (Integer) candidatesList.get("size");


		List<CandidateDTO> candidateDTOList = candidateBuilder
				.convertCandidateListToDTOList(companiesMapList);
		
		Map<String, Object> candidatesDataMap = new HashMap<String, Object>();

		candidatesDataMap.put("list", candidateDTOList);
		candidatesDataMap.put("size", noOfRecords);

		return candidatesDataMap;
	}

	@Override
	public Map<String, Object> getAllEmployeesInfo(Integer startIndex,
			Integer endIndex,String companyName) {

		Map<String, Object> employessList = jobPortalDAOImpl
				.getAllEmployeesInfo(startIndex,
						 endIndex,companyName);
		
		List<Candidate> employeesMapList = (List<Candidate>) employessList
				.get("list");

		List<CandidateDTO> employeesDTOList = candidateBuilder
				.convertCandidateListToDTOList(employeesMapList);
		
		Integer noOfRecords = (Integer) employessList.get("size");

		
		Map<String, Object> employeesDataMap = new HashMap<String, Object>();

		employeesDataMap.put("list", employeesDTOList);
		employeesDataMap.put("size", noOfRecords);


		return employeesDataMap;
	}

	@Override
	public ByteArrayOutputStream exportCompaniesList(String companySearch,
			String selectionTechnology) throws Exception {
		

		
		Map<String, Object> CompniesList = null;

		
		CompniesList = jobPortalDAOImpl.getAllCompanies(null, null,
				companySearch, selectionTechnology);

		List<Companies> companiesMapList = (List<Companies>) CompniesList
				.get("list");
		
		Integer noOfRecords = (Integer) CompniesList.get("size");
		
		

		List<CompanyDTO> companiesData = panelBuilder
				.convertCompaniesListToCompaniesDTOList(companiesMapList);

		List<CompaniesDTO> companyList = new ArrayList<CompaniesDTO>();

		CompaniesDTO companydto = null;

     String companyname ="";
     Long canCount;
     Long EmpCount;
		if (companiesData != null) {
			for (CompanyDTO comList : companiesData) {

				if (comList != null) {
				 CompaniesDTO countmap = getCount(
							((comList.getCompanyName())), selectionTechnology)
							.get("list");
				 if(countmap != null) {
						companyList.add(countmap);
						}
			}

		}
		   }

		Map<String, Object> companiesDataMap = new HashMap<String, Object>();

		companiesDataMap.put("list", companyList);
		
		
		logger1.warn("companiesListSize"+companyList.size());

		//return companiesDataMap;
		
		 //List<CompaniesDTO> dtoList =  new ArrayList(companiesDataMap.values());
		List<CompaniesDTO> dtoList = (List<CompaniesDTO>) companiesDataMap.get("list");
		
		
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
		cell0.setCellValue("Company Name");
		cell0.setCellStyle(style);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Candidates Count");
		cell1.setCellStyle(style);

		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Employees Count");
		cell2.setCellStyle(style);

	
        if(companyList!=null){
		for (CompaniesDTO list : dtoList) {
			Row row = sheet.createRow(rowIndex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(list.getCompanyNmae()!=null?list.getCompanyNmae():null);

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(list.getCandidatesCount());

			Cell cel2 = row.createCell(2);
			cel2.setCellValue(list.getEmployeesCount());

			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);

		
         }
        }

		workbook.write(bos);
		bos.flush();
		bos.close();
		

		return bos;
	}

	@Override
	public ByteArrayOutputStream exportInterviewerDetails(String fromDate, String toDate, String selectionStatus)
			throws IOException {

		List<CandidateInterviewCycle> interviewList = jobPortalDAOImpl.getInterviewerDetails(fromDate, toDate, selectionStatus);
		
		
		List<Employee> emplist = jobPortalDAOImpl.getActiveEmployees();
		
		Map<String, Integer> map = new HashMap<String, Integer>();
      
		for (CandidateInterviewCycle list : interviewList)
		{   
			int count = 1;
			for(Employee emp :emplist) {
		       
			    if (list.getInterviewers().equalsIgnoreCase((emp.getFullName()))) 
			    {
			    	if(!map.containsKey(list.getInterviewers())) {
					map.put(list.getInterviewers(), count);
			    	}
			    	 else {
					    	Integer oldCount = map.get(list.getInterviewers());
					    	map.put(list.getInterviewers(), oldCount+1);
					    	
					    }
				}
			
			}	
			
		}
		
		
		
		
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
		cell0.setCellValue("Interviewer Name");
		cell0.setCellStyle(style);

		Cell cell1 = row1.createCell(1);
		cell1.setCellValue("Count");
		cell1.setCellStyle(style);
		
		for(Map.Entry<String,Integer> entry : map.entrySet()) {
		
		Row row = sheet.createRow(rowIndex++);

		Cell cel0 = row.createCell(0);
		cel0.setCellValue(entry.getKey());
		

		Cell cel1 = row.createCell(1);
		cel1.setCellValue(entry.getValue());
		
		sheet.autoSizeColumn(0);
		sheet.autoSizeColumn(1);
		}
		
		workbook.write(bos);
		bos.flush();
		bos.close();
		return bos;
		
	}

	@Override
	public ByteArrayOutputStream downloadScheduleCandidatesFile(String fromDate, String toDate,String skill) throws IOException
	{
	
      List<CandidateInterviewCycle> candidates = null;

		
      candidates =  jobPortalDAOImpl.searchCadidates(
				fromDate, toDate,skill);
		
      List<CandidateInterviewCycleDTO> dto= candidateInterviewCycleBuilder
			.convertDTO(candidates);
      
	
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
	
	
     int serialNum = 0;
     Row row = sheet.createRow(0);


	 Cell cell00 = row.createCell(0);
     cell00.setCellValue("S.No");

     Cell cell01 = row.createCell(1);
     cell01.setCellValue("Candidate Name");
     
     Cell cell02 = row.createCell(2);
     cell02.setCellValue("Technology");

     Cell cell03 = row.createCell(3);
     cell03.setCellValue("Experience");
     
     Cell cell04 = row.createCell(4);
     cell04.setCellValue("Current CTC");
     
     Cell cell05 = row.createCell(5);
     cell05.setCellValue("Expected CTC");
     
     Cell cell06 = row.createCell(6);
     cell06.setCellValue("Notice");

     Cell cell07 = row.createCell(7);
     cell07.setCellValue("Contact Number");

     Cell cell08 = row.createCell(8);
     cell08.setCellValue("Interviewer");

     Cell cell09 = row.createCell(9);
     cell09.setCellValue("Interview Date");

     Cell cell10 = row.createCell(10);
     cell10.setCellValue("Interview Timings");

     Cell cell11 = row.createCell(11);
     cell11.setCellValue("Mode of Interview");
     
     Cell cell12 = row.createCell(12);
     cell12.setCellValue("Interview Round");
     
     Cell cell13 = row.createCell(13);
     cell13.setCellValue("Interview Status");
     
     Cell cell14 = row.createCell(14);
     cell14.setCellValue("Recruiter");
    
    
     for (CandidateInterviewCycleDTO candidate: dto) {
        
         row = sheet.createRow(rowIndex++);
         
         Cell cell0 = row.createCell(0);
         cell0.setCellValue(++serialNum);

         Cell cell1 = row.createCell(1);
         cell1.setCellValue(candidate.getCandidateName());
         
         Cell cell2 = row.createCell(2);
         cell2.setCellValue(candidate.getTechnology());

         Cell cell3 = row.createCell(3);
         cell3.setCellValue(candidate.getExperiance());
         
         Cell cell4 = row.createCell(4);
          cell4.setCellValue(candidate.getCtc());
         
         Cell cell5 = row.createCell(5);
          cell5.setCellValue(candidate.getEctc());
         
         Cell cell6 = row.createCell(6);
         cell6.setCellValue(candidate.getNp());

         Cell cell7 = row.createCell(7);
         cell7.setCellValue(candidate.getMobileNumber());

         Cell cell8 = row.createCell(8);
         cell8.setCellValue(candidate.getInterviewers());

         Cell cell9 = row.createCell(9);
         cell9.setCellValue(candidate.getInterviewDate());

         Cell c10 = row.createCell(10);
         c10.setCellValue(candidate.getInterviewTime());

         Cell c11 = row.createCell(11);
         c11.setCellValue(candidate.getInterviewMode());

         Cell c12 = row.createCell(12);
         c12.setCellValue(candidate.getInterviewRound());
         
         Cell c13 = row.createCell(13);
         c13.setCellValue(candidate.getInterviewStatus());
         
         Cell c14 = row.createCell(14);
         c14.setCellValue(candidate.getRecruiter());
         
         
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
 		
 		
     }
     workbook.write(bos);
		bos.flush();
		bos.close();
		return bos;
}
	
	@Override
	public StatusChart getStatusChart(String dateSelection,String from, String to) {
		  Date fromDate = null;
		  Date toDate   = null;
		 
		  if(dateSelection.equalsIgnoreCase("Custom")) {
			  fromDate = stringToDate(from);
			  toDate   = stringToDate(to);
		  }
		  else {
			  Map<String, Date> dateMap = jobPortalDAOImpl.getDates(dateSelection,from,to);
				fromDate = dateMap.get("startDate");
				toDate = dateMap.get("endDate");
		  }
		  List<Object[]> statusListCount = jobPortalDAOImpl.
				                   getInterviewStatusCount(dateSelection,fromDate,toDate);
		  
		 
		
		  
		  return candidateBuilder.buildChart(statusListCount, fromDate.toString("dd-MMM-yyyy"), toDate.toString("dd-MMM-yyyy"));
		  
		  }
	
	
	private Date stringToDate(String date) {
		 Date Date = null;
		 
		 try {
			 Date =DateParser.toDate(date);
		 }catch(ParseException e) {
			 e.printStackTrace();
		 }
		 
		return Date;
	}

	@Override
	public Map<String, Object> getSelectedTypeEmployeeData(Integer startIndex,
			Integer endIndex, String selectionStatus, String selectionType,
			String searchStr,Integer country) {
		String Country ="";
		if(country !=null){
		 CountryLookUp Con = dao.findBy(CountryLookUp.class, country);
		  Country = Con.getName();
		}else{
			Country="";
		}
		Map<String, Object> serachEmpList = new HashMap<String, Object>();
		
			serachEmpList = jobPortalDAOImpl.getSelectedTypeEmployeeData(
					selectionStatus,selectionType,startIndex, endIndex, searchStr,Country);
				
		List<EmployeeDTO> dTOs = employeeBuilder
				.getemployeeDTOList((List) serachEmpList.get("employeeList"));
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("Empsize", serachEmpList.get("size"));
		map.put("emps", dTOs);
		return map;
	}
	
	@Override
	public List<VendorDto> getVendorDetails() {

		List<VendorDetails> vendorsList = jobPortalDAOImpl.getVendorDetails();
		List<VendorDto> vendors = new ArrayList<VendorDto>();
		for(VendorDetails list: vendorsList){
			VendorDto dto = new VendorDto();
			dto.setVendorId(list.getVendorId());
			dto.setVendorName(list.getVendorName());
			//dto.setEmpDepartment(list.getEmpDepartment());
			vendors.add(dto);
		};
		
		return vendors;
	}

	@Override
	public void sendWhatsappScheduleInterviewNotificationtoCandidate(
			String to,String template, String templateParams) {
		URL url = null;
		System.out.println("in schedule notification");
		try {
			
			url = new URL("https://api.kaleyra.io/v1/HXIN1720279606IN/messages");
		} catch (MalformedURLException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("from", "919206697777");
        map.put("to", to);
        map.put("type", "template");
        map.put("channel", "Whatsapp");
        map.put("template_name",template);
        map.put("params",templateParams);
        map.put("lang_code","en");
        StringBuilder postData = new StringBuilder();
        for (Map.Entry param : map.entrySet()) {
	        if (postData.length() != 0) 
	        	postData.append('&');
	        try {
				postData.append(URLEncoder.encode((String) param.getKey(), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        postData.append('=');
	        try {
				postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    } 
        byte[] postDataBytes = null;
		try {
			postDataBytes = postData.toString().getBytes("UTF-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        HttpURLConnection conn = null;
		try {
			conn = (HttpURLConnection)url.openConnection();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    try {
			conn.setRequestMethod("POST");
			System.out.println("opening post is ok");
		} catch (ProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
	    conn.setRequestProperty("api-key", "A9ecaaba986f12c06b781710430a213cc");
	    conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
	    conn.setDoOutput(true);
	    try {
			conn.getOutputStream().write(postDataBytes);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	    
	    Reader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
			System.out.println("in:" + in);
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	    StringBuilder sb = new StringBuilder();
	    try {
			for (int c; (c = in.read()) >= 0;)
			sb.append((char)c);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    String response = sb.toString();
	    System.out.println(response);
	    JSONObject myResponse =null;
		try {
			myResponse = new JSONObject(response.toString());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    /*System.out.println("result after Reading JSON Response");
			try {
				System.out.println("id- "+ myResponse.getString("id"));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
	    try {
			System.out.println("type- "+myResponse.getString("type"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			System.out.println("datetime- "+myResponse.getString("createdDateTime"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			System.out.println("count- "+myResponse.getString("totalCount"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			JSONObject form_data = myResponse.getJSONObject("data");
		} catch (JSONException e) {
			// TODO Auto-generated catc2h block
			e.printStackTrace();
		}*/
	 System.out.println("end of schedule interview");	
	}

	
	

	@Override
	public void sendRejectedMessagetoCandidate(Long candidateId) {
		System.out.println("in reject candidate");
		Candidate candidate = dao.findBy(Candidate.class,candidateId);
		String to="";
		if(candidate.getCountry().getName().equalsIgnoreCase("INDIA")){
		  to =91+candidate.getMobile();
		}
		if(candidate.getCountry().getName().equalsIgnoreCase("USA")){
			  to =1+candidate.getMobile();
			}
		if(candidate.getCountry().getName().equalsIgnoreCase("CANADA")){
			  to =1+candidate.getMobile();
			}
		if(candidate.getCountry().getName().equalsIgnoreCase("PHILIPPINES")){
			  to =63+candidate.getMobile();
			}
		if(candidate.getCountry().getName().equalsIgnoreCase("AUSTRALIA")){
			  to =61+candidate.getMobile();
			}

		if(candidate.getNotifications().equalsIgnoreCase("Yes")){
         String template="reject_candidate";
		 String params = '\"' + candidate.getFullName()+ '\"' + ',' + '\"' + candidate.getAppliedForLookUp() + '\"';
		 sendWhatsappScheduleInterviewNotificationtoCandidate(to,template,params);
		}
		
	}
	@Override
	public List<GenderDTO> getGenders() {
		List<Gender> list = jobPortalDAOImpl.getGenders();
		List<GenderDTO> genderDto = new ArrayList<GenderDTO>();
		for(Gender gender:list){
			GenderDTO dto = new GenderDTO();
			dto.setId(gender.getId());
			dto.setGenderName(gender.getGenderName());
			genderDto.add(dto);
		}
		return genderDto;
	}
	
	@Override
	public List<BloodGroupDTO> getBloodgroups() {
		List<BloodGroup> list = jobPortalDAOImpl.getBloodgroups();
		List<BloodGroupDTO> groupDto = new ArrayList<BloodGroupDTO>();
		for(BloodGroup bloodgroup:list){
			BloodGroupDTO dto = new BloodGroupDTO();
			dto.setId(bloodgroup.getId());
			dto.setGroupName(bloodgroup.getGroupName());
			groupDto.add(dto);
		}
		return groupDto;
	}
	@Override
	public List<MaritalStatusDTO> getMaritalStatus() {
		List<MaritalStatus> list = jobPortalDAOImpl.getMaritalStatus();
		List<MaritalStatusDTO> statusDto = new ArrayList<MaritalStatusDTO>();
		for(MaritalStatus marital:list){
			MaritalStatusDTO dto = new MaritalStatusDTO();
			dto.setId(marital.getId());
			dto.setMaritalStatus(marital.getMaritalStatus());
			statusDto.add(dto);
		}
		return statusDto;
	}

	@Override
	public List<RelationsDTO> getRelations() {
		List<Relations> list = jobPortalDAOImpl.getRelations();
		List<RelationsDTO> relationDto = new ArrayList<RelationsDTO>();
		for(Relations relation:list){
			RelationsDTO dto = new RelationsDTO();
			dto.setId(relation.getId());
			dto.setRelationName(relation.getRelationName());
			relationDto.add(dto);
		}
		return relationDto;
	}

	@Override
	public List<QualificationCategoryDTO> getQualificationCatergory() {
		List<QualificationCategory> list = jobPortalDAOImpl.getQualificationCatergory();
		List<QualificationCategoryDTO> categoryDto = new ArrayList<QualificationCategoryDTO>();
		for(QualificationCategory category:list){
			QualificationCategoryDTO dto = new QualificationCategoryDTO();
			dto.setId(category.getId());
			dto.setQualificationCategory(category.getQualificationCategory());
			categoryDto.add(dto);
		}
		return categoryDto;
	}

	@Override
	public List<EmploymentTypeDTO> getEmploymentType() {
		List<EmploymentType> list = jobPortalDAOImpl.getEmploymentType();
		List<EmploymentTypeDTO> EmploymentType = new ArrayList<EmploymentTypeDTO>();
		for(EmploymentType type:list){
			EmploymentTypeDTO dto = new EmploymentTypeDTO();
			dto.setId(type.getId());
			dto.setEmploymentType(type.getEmploymentType());
			EmploymentType.add(dto);
		}
		return EmploymentType;
	}
	@Override
	public List<JobTypeDTO> getJobType() {
		List<JobType> list = jobPortalDAOImpl.getJobType();
		List<JobTypeDTO> JobType = new ArrayList<JobTypeDTO>();
		for(JobType type:list){
			JobTypeDTO dto = new JobTypeDTO();
			dto.setId(type.getId());
			dto.setJobType(type.getJobType());
			JobType.add(dto);
		}
		return JobType;
	}

}
