package com.raybiztech.separation.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ibm.icu.util.Holiday;
import com.raybiztech.appraisals.PIPManagement.builder.PIPAuditBuilder;
import com.raybiztech.appraisals.PIPManagement.builder.PIPBuilder;
import com.raybiztech.appraisals.PIPManagement.business.PIP;
import com.raybiztech.appraisals.PIPManagement.dao.PIPDao;
import com.raybiztech.appraisals.PIPManagement.dao.PIPDaoImpl;
import com.raybiztech.appraisals.PIPManagement.dto.PIPAuditDTO;
import com.raybiztech.appraisals.PIPManagement.dto.PIPDTO;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.exception.FileUploaderUtilException;
import com.raybiztech.appraisals.exceptions.UnauthorizedUserException;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.appraisals.utils.FileUploaderUtil;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.leavemanagement.business.algorithm.DateParser;
import com.raybiztech.mailtemplates.util.SeparationMailConfiguration;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.recruitment.business.Holidays;
import com.raybiztech.recruitment.utils.FileUploaderUtility;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.separation.builder.ClearanceCertificateBuilder;
import com.raybiztech.separation.builder.SeparationBuilder;
import com.raybiztech.separation.builder.SeparationCommentsBuilder;
import com.raybiztech.separation.business.ClearanceCertificate;
import com.raybiztech.separation.business.ExitFeedBack;
import com.raybiztech.separation.business.PrimaryReason;
import com.raybiztech.separation.business.Separation;
import com.raybiztech.separation.business.SeparationComments;
import com.raybiztech.separation.business.SeparationStatus;
import com.raybiztech.separation.chart.SeparationChart;
import com.raybiztech.separation.dao.SeparationDao;
import com.raybiztech.separation.dto.ClearanceCertificateDTO;
import com.raybiztech.separation.dto.ExitFeedBackDTO;
import com.raybiztech.separation.dto.SeparationDTO;
import com.raybiztech.separation.exception.CannotSubmitResignationException;
import com.raybiztech.separation.exception.InvalidTimeException;
import com.raybiztech.separation.exception.SeparationAlreadyExistException;

@Service("separationServiceImpl")
public class SeparationServiceImpl implements SeparationService {

	@Autowired
	SeparationBuilder separationBuilder;

	@Autowired
	SeparationDao separationDaoImpl;

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	ProjectService projectService;

	@Autowired
	SeparationCommentsBuilder separationCommentsBuilder;

	@Autowired
	ClearanceCertificateBuilder clearanceCertificateBuilder;

	@Autowired
	SeparationMailConfiguration separationMailConfiguration;

	@Autowired
	PropBean propBean;
	
	@Autowired
	PIPBuilder PIPBuilder;
	
	@Autowired
	PIPDao pipDAOImpl;
	
	@Autowired
	PIPAuditBuilder PIPAuditBuilder;

	Logger logger = Logger.getLogger(SeparationServiceImpl.class);

	// Below Line is important we are removing cache for employee list because
	// once employee put down his resignation he should appear in Resigned
	// List (in Employee Directory)
	@Caching(evict = { @CacheEvict(value = "employees", allEntries = true) })
	@Override
	public void saveResignationDetails(SeparationDTO separationDTO,
			HttpServletResponse response) {
		
		Boolean flag = this.checkForContract(separationDTO);
		
		if(!flag.equals(Boolean.TRUE)) {
		Boolean separationValidationFlag = checkSeparationValidation();
		
		if(separationValidationFlag)
		{
			throw new InvalidTimeException(
					"Invalid time to submit resgination");
		}
		else
		{
			Employee loggedEmployee = (Employee) securityUtils
					.getLoggedEmployeeDetailsSecurityContextHolder()
					.get("employee");

			Long separationId = null;

			Employee separationemployee = separationDaoImpl.findBy(Employee.class,
					separationDTO.getEmployeeId());

			if (separationemployee.equals(loggedEmployee)) {

				if (!separationDaoImpl.isSepartionExists(separationemployee)) {

					Separation separation = separationBuilder
							.toAddEntity(separationDTO);

					separationId = (Long) separationDaoImpl.save(separation);

					// making employee undernotice
					separationemployee.setUnderNotice(Boolean.TRUE);
					separationemployee.setUnderNoticeDate(separation
							.getRelievingDate());
					separationDaoImpl.update(separationemployee);

				} else {
					throw new SeparationAlreadyExistException(
							"separation is already existing ");
				}
			} else {
				throw new UnauthorizedUserException(
						"Trying to Add Separation Of Another Employee");
			}
			separationMailConfiguration
					.sendSeparationMailNotificationToEmployee(separationId);

			separationMailConfiguration
					.sendSeparationMailNotificationToManager(separationId);
			
			//separationMailConfiguration.sendSeparationMailNotificationToAccounts(separationId);
		}

		}
		else {
			throw new CannotSubmitResignationException("You Cannot Submit resignation");
		}

	}

	@Override
	public List<PrimaryReason> getPrimaryReasonList() {

		return separationDaoImpl.get(PrimaryReason.class);
	}

	@Override
	public SeparationDTO getEmployeeInfo() {
		// TODO Auto-generated method stub
		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		SeparationDTO dto = new SeparationDTO();
		dto.setEmployeeName(employee.getFullName());
		dto.setEmployeeId(employee.getEmployeeId());
		dto.setResignationDate(new Date().toString("dd/MM/yyyy"));
		return dto;
	}

	public List<SeparationDTO> getResignationList() {
		List<Separation> separation = separationDaoImpl.get(Separation.class);
		return separationBuilder.toDtoList(separation);
	}

	@Override
	public Map<String, Object> getSeparationForm() {

		Map<String, Object> map = new HashMap<String, Object>();

		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		Boolean separation = separationDaoImpl.isSepartionExists(employee);
		SeparationDTO dto = new SeparationDTO();
		dto.setSeparationExist(separation);
		dto.setEmployeeName(employee.getFullName());
		dto.setEmployeeId(employee.getEmployeeId());
		dto.setResignationDate(new Date().toString("dd/MM/yyyy"));
		dto.setContractExists(employee.getContractExists());
		if(employee.getContractExists().equals(Boolean.TRUE)) {
			dto.setContractStartDate(employee.getContractStartDate().toString("dd/MM/yyyy"));
		
			dto.setContractEndDate(employee.getContractEndDate().toString("dd/MM/yyyy"));
		}

		map.put("form", dto);
		map.put("lookup", separationDaoImpl.get(PrimaryReason.class));

		return map;
	}
	@Override
	public SeparationDTO getSeparation(Long separationId) {
	       
        Employee loggedInEmployee= (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder().get("employee");
       
        Permission itDeptPermission=separationDaoImpl.checkForPermission("IT Cleranace", loggedInEmployee);
        Permission accountDeptPermission=separationDaoImpl.checkForPermission("Finance Cleranace", loggedInEmployee);
        Permission adminDeptPermission=separationDaoImpl.checkForPermission("Admin Cleranace", loggedInEmployee);
        Permission hrDeptPermission=separationDaoImpl.checkForPermission("HR Cleranace", loggedInEmployee);
        Separation sep = separationDaoImpl.findBy(Separation.class,
                separationId);
        if(itDeptPermission.getView() || accountDeptPermission.getView() || adminDeptPermission.getView() || hrDeptPermission.getView() )
        {
       

            List<Separation> list = (List<Separation>) separationDaoImpl
                    .getAllOfProperty(Separation.class, "employee",
                            sep.getEmployee());

            Set<SeparationComments> newList = new HashSet<SeparationComments>();

            sep.setComments(null);
            for (Separation separation : list) {
                newList.addAll(separation.getComments());
            }
            sep.setComments(newList);

           
        }
        else
        {
            List<Long> managerIds = projectService.mangerUnderManager(loggedInEmployee
                    .getEmployeeId());
            managerIds.add(loggedInEmployee.getEmployeeId());
            if(managerIds.contains(sep.getEmployee().getManager().getEmployeeId()))
            {
                List<Separation> list = (List<Separation>) separationDaoImpl
                        .getAllOfProperty(Separation.class, "employee",
                                sep.getEmployee());

                Set<SeparationComments> newList = new HashSet<SeparationComments>();

                sep.setComments(null);
                for (Separation separation : list) {
                    newList.addAll(separation.getComments());
                }
                sep.setComments(newList);
            }
            else
            {
                throw new UnauthorizedUserException("Unauthorized user");
            }
        }
        return separationBuilder.toDto(sep);
       
    }
	
	
	/*@Override
	public SeparationDTO getSeparation(Long separationId,HttpServletResponse res) {
		Separation sep = separationDaoImpl.findBy(Separation.class,
				separationId);
		
		Employee loggedEmp = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		if(loggedEmp.getRole().equalsIgnoreCase("manager")){
			List<Long> managerIds = projectService.mangerUnderManager(loggedEmp
					.getEmployeeId());
			List<Employee> employeelist=separationDaoImpl.getReportiesUnderManager(managerIds);
			for(Employee e:employeelist)
			{
				managerIds.add(e.getEmployeeId());
			}
			if(!managerIds.contains(sep.getEmployee().getEmployeeId())){
				throw new UnauthorizedUserException("Un authorized user");
			}
			
		}

		List<Separation> list = (List<Separation>) separationDaoImpl
				.getAllOfProperty(Separation.class, "employee",
						sep.getEmployee());

		Set<SeparationComments> newList = new HashSet<SeparationComments>();

		sep.setComments(null);
		for (Separation separation : list) {
			newList.addAll(separation.getComments());
		}
		sep.setComments(newList);

		return separationBuilder.toDto(sep);
	}*/

	@Override
	public void updateSeparation(SeparationDTO separationDTO) {

		Employee loggedEmployee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");

		Separation separation = separationBuilder.toEditEntity(separationDTO,
				loggedEmployee);

		if (!loggedEmployee.equals(separation.getEmployee())) {
			separationDaoImpl.saveOrUpdate(separation);
		}

		separationMailConfiguration
				.updateMailNotificationToEmployee(separationDTO);

		/*
		 * separationMailConfiguration.updateMailNotificationToManager(
		 * separation );
		 */

	}

	@Override
	public void saveClearanceCertificateComments(
			ClearanceCertificateDTO clearanceDTO) {

		ClearanceCertificate certificate = clearanceCertificateBuilder
				.toEntity(clearanceDTO);
		Separation separation = separationDaoImpl.findBy(Separation.class,
				clearanceDTO.getSeperationId());
		separation.getCertificate().add(certificate);
		separationDaoImpl.update(separation);

		if (clearanceDTO.getAddedBy().equalsIgnoreCase("hr")) {
			separationMailConfiguration.sendAcceptanceToEmployee(separation);

		}
	}

	@Override
	public Map<String, Object> getResignationList(String multiplesearch,
			Integer startIndex, Integer endIndex, String dateSelection,
			String from, String to, String status,String empStatus) {

		// SeparationStatus separationStatus = SeparationStatus.valueOf(status);
		logger.info("in resignation list");
		Date fromDate = null;
		Date toDate = null;
		if (dateSelection.equalsIgnoreCase("Custom")) {
			try {
				fromDate = DateParser.toDate(from);
				toDate = DateParser.toDate(to);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		else {
			Map<String, Date> dateMap = separationDaoImpl
					.getCustomDates(dateSelection);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}

		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");

		Permission resignationListPermission = separationDaoImpl
				.checkForPermission("Resignation List", employee);
		Permission hierarchyResignationList = separationDaoImpl
				.checkForPermission("Hierarchy Resignation List", employee);
		/*
		 * Permission IndividualResignationList =
		 * separationDaoImpl.checkForPermission("Individual Resignation List",
		 * employee);
		 */

		Permission initiateResignationList = separationDaoImpl
				.checkForPermission("initiate Resignation List", employee);

		Map<String, Object> detailsMap = null;

		if (resignationListPermission.getView()
				&& !hierarchyResignationList.getView()
				&& !initiateResignationList.getView()) {

			detailsMap = separationDaoImpl.getResignationList(multiplesearch,
					startIndex, endIndex, fromDate, toDate, status,empStatus);
			// logger.warn("first condition" + detailsMap);

		} else if (resignationListPermission.getView()
				&& hierarchyResignationList.getView()
				&& !initiateResignationList.getView()) {

			List<Long> managerIds = projectService.mangerUnderManager(employee
					.getEmployeeId());

			detailsMap = separationDaoImpl
					.getEmployeeResignationListUnderManager(managerIds,
							multiplesearch, startIndex, endIndex, fromDate,
							toDate, status, employee);
			// logger.warn("second condition" + detailsMap);

		} else if (resignationListPermission.getView()
				&& initiateResignationList.getView()
				&& !hierarchyResignationList.getView()) {
			detailsMap = separationDaoImpl.getInititateResignationList(
					multiplesearch, startIndex, endIndex, fromDate, toDate,
					status, employee);
			// logger.warn("fourth condition" + detailsMap);

		} else if (resignationListPermission.getView()
				&& initiateResignationList.getView()
				&& hierarchyResignationList.getView()) {
			// List<Long> managerIds =
			// projectService.mangerUnderManager(employee.getEmployeeId());

			detailsMap = separationDaoImpl
					.getInititateResignationListUnderManager(employee,
							multiplesearch, startIndex, endIndex, fromDate,
							toDate, status);
			// logger.warn("fifth condition" + detailsMap);

		}

		/*
		 * else if (resignationListPermission.getView() &&
		 * initiateResignationList.getView()) {
		 * 
		 * detailsMap =
		 * separationDaoImpl.getEmployeeResignationList(multiplesearch,
		 * startIndex, endIndex, fromDate, toDate, employee, status);
		 * logger.warn("third condition" + detailsMap);
		 * 
		 * }
		 */

		Map<String, Object> map = new HashMap<String, Object>();

		List<SeparationDTO> dtoList = separationBuilder
				.toDtoList((List) detailsMap.get("list"));

		map.put("list", dtoList);
		map.put("size", detailsMap.get("size"));

		return map;
	}

	@Override
	public void IntitiateCC(Long separationId) {

		Separation separation = separationDaoImpl.findBy(Separation.class,
				separationId);
		separation.setIsprocessInitiated(Boolean.TRUE);
		
		separation.setInitiatedDate(new Date());

	//	Date intitiateDate = new Date();
		
		/*
		 * SeparationComments comments = new SeparationComments();
		 * comments.setEmployee((Employee)
		 * securityUtils.getLoggedEmployeeDetailsSecurityContextHolder
		 * ().get("employee")); comments.setStatus(separation.getStatus()); //
		 * comments.setComments("N/A"); comments.setCreatedDate(new Second());
		 * 
		 * separation.getComments().add(comments);
		 */

		separationDaoImpl.update(separation);

		separationMailConfiguration.InitiateCcMailNotification(separationId);

	}

	@Override
	public List<ClearanceCertificateDTO> getClearanceDetails(Long separationId,
			String submittedBy) {

		List<ClearanceCertificate> cList = new ArrayList<ClearanceCertificate>();

		/*
		 * Separation separation = separationDaoImpl.findBy(Separation.class,
		 * separationId);
		 */
		// Separation separation =
		// separationDaoImpl.getClearanceDetails(separationId,submittedBy);

		Separation separation = separationDaoImpl.findBy(Separation.class,
				separationId);

		Set<ClearanceCertificate> certificates = separation.getCertificate();
		for (ClearanceCertificate clearanceCertificates : certificates) {
			if (clearanceCertificates.getAddedBy()
					.equalsIgnoreCase(submittedBy)) {
				cList.add(clearanceCertificates);
			}
		}

		return clearanceCertificateBuilder.toDtoList(cList, separation);
	}

	/*
	 * @Caching(evict = { @CacheEvict(value = "employees", allEntries = true) })
	 * 
	 * @Override public void revokeResignation(Long separationId) { Separation
	 * separation = separationDaoImpl.findBy(Separation.class, separationId);
	 * separation.setStatus(SeparationStatus.REVOKED);
	 * separation.setIsRevoked(Boolean.TRUE);
	 * 
	 * Employee separtionEmployee = separation.getEmployee();
	 * 
	 * SeparationComments newcomments = new SeparationComments();
	 * newcomments.setEmployee(separtionEmployee);
	 * newcomments.setComments("N/A");
	 * newcomments.setStatus(SeparationStatus.REVOKED);
	 * newcomments.setCreatedDate(new Second());
	 * 
	 * separation.getComments().add(newcomments);
	 * separationDaoImpl.update(separation);
	 * 
	 * separtionEmployee.setUnderNotice(Boolean.FALSE);
	 * separtionEmployee.setUnderNoticeDate(null);
	 * separationDaoImpl.update(separtionEmployee);
	 * 
	 * 
	 * separationMailConfiguration
	 * .revokeMailNotificationToEmployee(separationId);
	 * 
	 * 
	 * separationMailConfiguration
	 * .revokeMailNotificationToManager(separationId);
	 * 
	 * }
	 */
	@Caching(evict = { @CacheEvict(value = "employees", allEntries = true) })
	@Override
	public void revokeResignation(SeparationDTO separationDTO) {
		Separation separation = separationDaoImpl.findBy(Separation.class,
				separationDTO.getSeparationId());
		separation.setStatus(SeparationStatus.REVOKED);
		separation.setIsRevoked(Boolean.TRUE);
		separation.setCreatedDate(new Second());

		Employee separtionEmployee = separation.getEmployee();

		SeparationComments newcomments = new SeparationComments();
		newcomments.setEmployee(separtionEmployee);
		/* newcomments.setComments("N/A"); */
		newcomments.setWithdrawComments(separationDTO.getWithdrawComments());
		newcomments.setStatus(SeparationStatus.REVOKED);
		newcomments.setCreatedDate(new Second());

		separation.getComments().add(newcomments);
		separationDaoImpl.update(separation);

		separtionEmployee.setUnderNotice(Boolean.FALSE);
		separtionEmployee.setUnderNoticeDate(null);
		separationDaoImpl.update(separtionEmployee);

		/*
		 * separationMailConfiguration
		 * .revokeMailNotificationToEmployee(separationId);
		 */

		separationMailConfiguration
				.revokeMailNotificationToManager(separationDTO);

	}

	@Caching(evict = { @CacheEvict(value = "employees", allEntries = true) })
	@Override
	public void update(SeparationDTO separationDTO) {

		Separation separation = separationDaoImpl.findBy(Separation.class,
				separationDTO.getSeparationId());
		separation.setRelievingDate(stringToDate(separationDTO
				.getRelievingDate()));
		if (!separation.getStatus().equals(SeparationStatus.UNDERNOTICE)) {
			separation.setStatus(SeparationStatus.UNDERNOTICE);
		}

		SeparationComments newcomments = new SeparationComments();
		Employee loggedEmployee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		if(separationDTO.getStatus()!=null){
			SeparationStatus status = SeparationStatus
					.valueOf(separationDTO.getStatus());
			separation.setStatus(status);
			newcomments.setStatus(status);
			}
		newcomments.setEmployee(loggedEmployee);
		newcomments.setComments(separationDTO.getManagerComments());
		newcomments.setStatus(separation.getStatus());
		newcomments.setRelievingDate(stringToDate(separationDTO
				.getRelievingDate()));
		newcomments.setCreatedDate(new Second());

		separation.getComments().add(newcomments);

		separationDaoImpl.update(separation);

		Employee employee = separation.getEmployee();
		employee.setUnderNoticeDate(stringToDate(separationDTO
				.getRelievingDate()));
		logger.warn(separationDTO.getRelievingDate());
		separationDaoImpl.update(employee);

	}

	public Date stringToDate(String date) {

		Date date2 = null;
		try {
			date2 = DateParser.toDate(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date2;
	}

	public Long saveExitFeedBackForm(ExitFeedBackDTO exitFeedBackDTO) {
		ExitFeedBack exitFeedBack = separationBuilder
				.toExitFeedBackEntity(exitFeedBackDTO);
		Long exitFeedBackId = (Long) separationDaoImpl.save(exitFeedBack);

		this.relieveEmployee(exitFeedBackDTO.getSeparationId(),
				exitFeedBackDTO.getEmployeeId());
		return exitFeedBackId;

	}

	@Caching(evict = { @CacheEvict(value = "employees", allEntries = true) })
	public void relieveEmployee(Long separtionId, Long separationEmployeeId) {

		Separation separation = separationDaoImpl.findBy(Separation.class,
				separtionId);
		separation.setStatus(SeparationStatus.RELIEVED);

		SeparationComments newcomments = new SeparationComments();
		newcomments.setEmployee((Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee"));
		// newcomments.setComments("N/A");
		newcomments.setStatus(SeparationStatus.RELIEVED);
		newcomments.setCreatedDate(new Second());

		separation.getComments().add(newcomments);
		separationDaoImpl.update(separation);

		Employee separationEmployee = separation.getEmployee();
		separationEmployee.setStatusName("InActive");
		separationEmployee.setReleavingDate(separation.getRelievingDate());
		separationDaoImpl.update(separationEmployee);

	}

	@Override
	public SeparationDTO getEmpDetails(Long separationId) {

		Separation separation = separationDaoImpl.findBy(Separation.class,
				separationId);
		SeparationDTO dto = new SeparationDTO();
		dto.setEmployeeId(separation.getEmployee().getEmployeeId());
		dto.setEmployeeName(separation.getEmployee().getFullName());
		dto.setPrimaryReasonName(separation.getPrimaryReason().getReasonName());
		dto.setPrimaryReasonId(separation.getPrimaryReason().getReasonId());
		return dto;
	}

	@Override
	public ExitFeedBackDTO checkExitFeedBackForm(Long separationId) {

		ExitFeedBack feedBack = separationDaoImpl
				.checkExitFeedBackForm(separationId);
		ExitFeedBackDTO exitFeedBackDTO = separationBuilder
				.toExitFeedBackDTO(feedBack);
		return exitFeedBackDTO;
	}

	@Override
	public void uploadrelievingletter(MultipartFile mpf, Long exitFeedbakformId) {
		if (exitFeedbakformId != null) {
			ExitFeedBack exitFeedBack = separationDaoImpl.findBy(
					ExitFeedBack.class, exitFeedbakformId);
			FileUploaderUtility fileUploaderUtility = new FileUploaderUtility();
			String path = null;
			try {
				path = fileUploaderUtility.uploadrelievingletter(exitFeedBack,
						mpf, propBean);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			exitFeedBack.setRelievingLetter(path);
			separationDaoImpl.update(exitFeedBack);
		}

	}

	@Override
	public SeparationDTO getEmployeeSepartion(Long employeeId) {
		
		SeparationDTO separationDTO = null;
		
		Boolean PIPemp=separationDaoImpl.checkPIP(employeeId);
	

		if(PIPemp.equals(Boolean.FALSE))
		{

		Separation separation = separationDaoImpl
				.getEmployeeSepartion(employeeId);

		if(separation != null){
			
		List<Separation> list = (List<Separation>) separationDaoImpl
				.getAllOfProperty(Separation.class, "employee",
						separation.getEmployee());
		

		Set<SeparationComments> newList = new HashSet<SeparationComments>();

		separation.setComments(null);
		for (Separation seprationList : list) {
			newList.addAll(seprationList.getComments());
		}
		
		separation.setComments(newList);
		}
		if(separation!=null)
		{
			
			ExitFeedBack exitFeedback = separationDaoImpl
					.checkExitFeedBackForm(separation.getSeparationId());
			 separationDTO = separationBuilder.toDto(separation);
			if (exitFeedback.getRelievingLetter() != null) {
				String splitedPath = exitFeedback.getRelievingLetter().substring(
						exitFeedback.getRelievingLetter().lastIndexOf("/") + 1);
				separationDTO.setRelievingLetterPath(splitedPath);
			}
			if (exitFeedback.getExitFeedbackForm() != null) {
				String splitedPath = exitFeedback.getExitFeedbackForm().substring(
						exitFeedback.getExitFeedbackForm().lastIndexOf("/") + 1);
				separationDTO.setExitFeedbackFormPath(splitedPath);
			}
		}
		
		
	}
		else
		{
			separationDTO=this.getEmployeePIP(employeeId);
		}
		
		return separationDTO;
	}



	@Override
	public void downloadFile(HttpServletResponse response, String fileName) {
		try {
			FileUploaderUtil util = new FileUploaderUtil();
			String filePath = (String) propBean.getPropData().get(
					"RelievingLetters");
			util.downloadUploadedFile(response, fileName, filePath);
		} catch (Exception ex) {
			throw new FileUploaderUtilException(
					"Exception occured while uploading a file in Legal "
							+ ex.getMessage(), ex);
		}

	}

	@Override
	public void updateCC(ClearanceCertificateDTO clearanceCertificateDTO) {
		ClearanceCertificate certificate = separationDaoImpl.findBy(
				ClearanceCertificate.class, clearanceCertificateDTO.getCcId());
		certificate.setIsDue(clearanceCertificateDTO.getIsDue());
		certificate.setComments(clearanceCertificateDTO.getComments());
		separationDaoImpl.update(certificate);

	}

	@Override
	public SeparationChart getSeparationChart(String selection, String from,
			String to) {

		Date fromDate = null;
		Date toDate = null;
		if (selection.equalsIgnoreCase("Custom")) {
			fromDate = stringToDate(from);
			toDate = stringToDate(to);
		} else {
			Map<String, Date> dateMap = separationDaoImpl
					.getCustomDates(selection);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}

		Map<String, Object> map = separationDaoImpl.getSeparationChartDetails(
				separationDaoImpl.get(PrimaryReason.class), fromDate, toDate);

		return separationBuilder.buildChart(map, fromDate, toDate);

	}

	@Override
	public ByteArrayOutputStream exportResignationList(String status,
			String dateSelection, String from, String to, String multiplesearch,String empStatus)
			throws Exception {

		// SeparationStatus separationStatus = SeparationStatus.valueOf(status);
		System.out.println("empStatus:" +empStatus);

		Date fromDate = null;
		Date toDate = null;

		if (dateSelection.equalsIgnoreCase("Custom")) {
			try {
				fromDate = DateParser.toDate(from);
				toDate = DateParser.toDate(to);
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		else {
			Map<String, Date> dateMap = separationDaoImpl
					.getCustomDates(dateSelection);
			fromDate = dateMap.get("startDate");
			toDate = dateMap.get("endDate");
		}

		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");

		Permission resignationListPermission = separationDaoImpl
				.checkForPermission("Resignation List", employee);
		Permission hierarchyResignationList = separationDaoImpl
				.checkForPermission("Hierarchy Resignation List", employee);
		Permission initiateResignationList = separationDaoImpl
				.checkForPermission("initiate Resignation List", employee);

		Map<String, Object> detailsMap = null;

		if (resignationListPermission.getView()
				&& !hierarchyResignationList.getView()
				&& !initiateResignationList.getView()) {

			detailsMap = separationDaoImpl.getResignationList(multiplesearch,
					null, null, fromDate, toDate, status,empStatus);
			// logger.warn("first condition" + detailsMap);

		} else if (resignationListPermission.getView()
				&& hierarchyResignationList.getView()
				&& !initiateResignationList.getView()) {

			List<Long> managerIds = projectService.mangerUnderManager(employee
					.getEmployeeId());

			detailsMap = separationDaoImpl
					.getEmployeeResignationListUnderManager(managerIds,
							multiplesearch, null, null, fromDate,
							toDate, status, employee);
			// logger.warn("second condition" + detailsMap);

		} else if (resignationListPermission.getView()
				&& initiateResignationList.getView()
				&& !hierarchyResignationList.getView()) {
			detailsMap = separationDaoImpl.getInititateResignationList(
					multiplesearch, null, null, fromDate, toDate,
					status, employee);
			// logger.warn("fourth condition" + detailsMap);

		} else if (resignationListPermission.getView()
				&& initiateResignationList.getView()
				&& hierarchyResignationList.getView()) {
			// List<Long> managerIds =
			// projectService.mangerUnderManager(employee.getEmployeeId());

			detailsMap = separationDaoImpl
					.getInititateResignationListUnderManager(employee,
							multiplesearch, null, null, fromDate,
							toDate, status);
			// logger.warn("fifth condition" + detailsMap);

		}

		Map<String, Object> map = new HashMap<String, Object>();

		List<Separation> dtoList = (List<Separation>) detailsMap.get("list");

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
		cell1.setCellValue("Employee Name");
		cell1.setCellStyle(style);

		Cell cell2 = row1.createCell(2);
		cell2.setCellValue("Resignation Date");
		cell2.setCellStyle(style);

		Cell cell3 = row1.createCell(3);
		cell3.setCellValue("Relieving Date");
		cell3.setCellStyle(style);

		Cell cell4 = row1.createCell(4);
		cell4.setCellValue("Primary Reason");
		cell4.setCellStyle(style);

		Cell cell5 = row1.createCell(5);
		cell5.setCellValue("Status");
		cell5.setCellStyle(style);

		for (Separation list : dtoList) {
			Row row = sheet.createRow(rowIndex++);

			Cell cel0 = row.createCell(0);
			cel0.setCellValue(list.getEmployee().getEmployeeId());

			Cell cel1 = row.createCell(1);
			cel1.setCellValue(list.getEmployee().getEmployeeFullName());

			Cell cel2 = row.createCell(2);
			cel2.setCellValue(list.getResignationDate().toString("dd/MM/yyyy"));

			Cell cel3 = row.createCell(3);
			cel3.setCellValue(list.getRelievingDate().toString("dd/MM/yyyy"));

			Cell cel4 = row.createCell(4);
			cel4.setCellValue(list.getPrimaryReason().getReasonName());

			Cell cel5 = row.createCell(5);
			cel5.setCellValue(list.getStatus().getSeperationStatus());

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
	public void uploadExitfeedBackFile(MultipartFile mpf, Long exitFeedbakformId) {
		if (exitFeedbakformId != null) {
			ExitFeedBack exitFeedBack = separationDaoImpl.findBy(
					ExitFeedBack.class, exitFeedbakformId);
			FileUploaderUtility fileUploaderUtility = new FileUploaderUtility();
			String path = null;
			try {
				path = fileUploaderUtility.uploadExitfeedBackFile(exitFeedBack,
						mpf, propBean);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			exitFeedBack.setExitFeedbackForm(path);
			separationDaoImpl.update(exitFeedBack);
		}

	}

	@Override
	public void downloadForm(HttpServletResponse response, String fileName) {
		try {
			FileUploaderUtil util = new FileUploaderUtil();
			String filePath = (String) propBean.getPropData().get(
					"ExitFeedbackForm");
			util.downloadUploadedFile(response, fileName, filePath);
		} catch (Exception ex) {
			throw new FileUploaderUtilException(
					"Exception occured while uploading a file in Legal "
							+ ex.getMessage(), ex);
		}
	}

	public SeparationDTO getEmployeeResg() {
		Employee employee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		Separation separation = separationDaoImpl.getEmployeeSepartion(employee
				.getEmployeeId());
		SeparationDTO dto = new SeparationDTO();
		dto.setStatus(separation.getStatus().getSeperationStatus());
		dto.setRelievingDate(separation.getRelievingDate().toString());
		dto.setSeparationId(separation.getSeparationId());
		return dto;

	}

	@Override
	public SeparationDTO getEmployeePIP(Long employeeId) {
		
		SeparationDTO separationDTO = new SeparationDTO();
		
		
		ExitFeedBack exitFeedback = separationDaoImpl
				.getEmployeePIPexitFeedBack(employeeId);
		
		Employee employee=separationDaoImpl.findBy(Employee.class, employeeId);
		 
		separationDTO.setEmployeeId(employeeId);
		separationDTO.setEmployeeName(employee.getFullName());
		separationDTO.setIsPIP(true);
		
		if (exitFeedback.getRelievingLetter() != null) {
			String splitedPath = exitFeedback.getRelievingLetter().substring(
					exitFeedback.getRelievingLetter().lastIndexOf("/") + 1);
			separationDTO.setRelievingLetterPath(splitedPath);
		}
		if (exitFeedback.getExitFeedbackForm() != null) {
			String splitedPath = exitFeedback.getExitFeedbackForm().substring(
					exitFeedback.getExitFeedbackForm().lastIndexOf("/") + 1);
			separationDTO.setExitFeedbackFormPath(splitedPath);
		}
		
		List<PIP> piplist=separationDaoImpl.getEmployeePIPList(employeeId);
		
		Long PipId=null;
		
		for(PIP pip:piplist)
		{
			PipId=pip.getId();
		}

		Map<String, List<Audit>> pipAudit=pipDAOImpl.getAudit(PipId, "PIP");
		Map<String, Object> pipAudit1=PIPAuditBuilder.ToPIPAuditDTO(pipAudit);
		
		List<PIPAuditDTO> pipDToList=new ArrayList<PIPAuditDTO>();
		List<PIPAuditDTO> pipAuditList=(List<PIPAuditDTO>) pipAudit1.get("list");
		for(PIPAuditDTO pip:pipAuditList)
		{
		
			pipDToList.add(pip);
		}
		separationDTO.setPipAuditDTO(pipDToList);
		return separationDTO;
	}

	/*@Override
	public void abscondedEmployee(SeparationDTO separationDTO) throws ParseException {
		
		
		
		
	Separation separation = separationDaoImpl.findBy(Separation.class, separationDTO.getSeparationId());
		separation.setStatus(SeparationStatus.ABSCOND);
	
		try {
			separation.setAbscondedDate(DateParser.toDate(separationDTO.getAbscondDate()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SeparationComments newcomments = new SeparationComments();
		newcomments.setEmployee((Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee"));
		// newcomments.setComments("N/A");
		newcomments.setStatus(SeparationStatus.ABSCOND);
		newcomments.setCreatedDate(new Second());
		newcomments.setComments(separationDTO.getAbscondComments());
		separation.getComments().add(newcomments);
		//separation.setAbscondedcomments(separationDTO.getAbscondComments());
		
		separationDaoImpl.update(separation);
		
		Employee employee=separationDaoImpl.findBy(Employee.class, separationDTO.getEmployeeId());
		employee.setStatusName("InActive");
		employee.setReleavingDate(DateParser.toDate(separationDTO.getAbscondDate()));
		employee.setIsAbsconded(true);
		
		separationDaoImpl.update(employee);
		
		
		
	}*/
	
	public Boolean checkSeparationValidation()
	{
		List<Holidays> holidays=separationDaoImpl.get(Holidays.class);
		List<Date> dateList = new ArrayList<Date>();
		for(Holidays holiday : holidays)
		{
			dateList.add(holiday.getDate());
		}

		
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Calendar calobj = Calendar.getInstance();
		String date = df.format(calobj.getTime());
		String[] dateonly = date.split(" ");
		String timeonly = dateonly[1];

		String[] houronly = timeonly.split(":");
		
		Calendar startDate = Calendar.getInstance();
	
		Boolean checkFlag=false;

	   // startDate.set(2012, 12, 02);
		if((Integer.parseInt(houronly[0]) >= 19 && Integer.parseInt(houronly[1]) >= 00) ||  dateList.contains(new Date()) || startDate.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || startDate.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
		{
			checkFlag=true;
	    }else {
	      checkFlag=false;
	    }
		return checkFlag;

	}
	
	Boolean checkForContract(SeparationDTO separationDTO) {
		
		Boolean flag = false;
		Employee loggedEmployee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		
		
		Date resignationDate = null;
		try {
			resignationDate = DateParser.toDate(separationDTO.getResignationDate());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(loggedEmployee.getContractExists().equals(Boolean.TRUE)) {
			if((resignationDate.equals(loggedEmployee.getContractStartDate())) ||
				(resignationDate.isAfter(loggedEmployee.getContractStartDate())) &&
				(resignationDate.equals(loggedEmployee.getContractEndDate())) ||
				(resignationDate.isBefore(loggedEmployee.getContractEndDate()))){
				flag = true;
			}
			}
		return flag;
		
	}
	
	//Below functionality is for sending mail to employee personal email.

	@Override
	public void sendMailToEmployeePersonalEmail(Long separationId) {
		
		Separation separation = separationDaoImpl.findBy(Separation.class,
				separationId);
		
		Employee employee = separation.getEmployee();
		
		logger.warn("employee");
		
		if(employee.getPersonalEmail() != null){
		String employeePersonalEmail = employee.getPersonalEmail();
		}
		
		separationMailConfiguration.mailToemployeePersonalEmail(separationId);
		
		
	}


	
	
}
