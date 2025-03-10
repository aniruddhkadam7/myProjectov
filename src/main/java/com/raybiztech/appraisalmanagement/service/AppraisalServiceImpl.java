/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.raybiztech.appraisalmanagement.service;

import static com.raybiztech.leavemanagement.service.LeaveServiceImpl.logger;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.inject.New;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.raybiztech.appraisalmanagement.builder.AppraisalCycleBuilder;
import com.raybiztech.appraisalmanagement.builder.AppraisalFormBuilder;
import com.raybiztech.appraisalmanagement.builder.DesignationKRAMappingBuilder;
import com.raybiztech.appraisalmanagement.builder.KRABuilder;
import com.raybiztech.appraisalmanagement.business.AppraisalCycle;
import com.raybiztech.appraisalmanagement.business.AppraisalForm;
import com.raybiztech.appraisalmanagement.business.AppraisalFormAvgRatings;
import com.raybiztech.appraisalmanagement.business.AppraisalKPIData;
import com.raybiztech.appraisalmanagement.business.AppraisalKRAData;
import com.raybiztech.appraisalmanagement.business.DesignationKRAMapping;
import com.raybiztech.appraisalmanagement.business.FormStatus;
import com.raybiztech.appraisalmanagement.business.KPI;
import com.raybiztech.appraisalmanagement.business.KRA;
import com.raybiztech.appraisalmanagement.business.ReviewAudit;
import com.raybiztech.appraisalmanagement.dao.AppraisalDao;
import com.raybiztech.appraisalmanagement.dto.AppraisalCycleDto;
import com.raybiztech.appraisalmanagement.dto.AppraisalFormDto;
import com.raybiztech.appraisalmanagement.dto.AppraisalFormListDto;
import com.raybiztech.appraisalmanagement.dto.AppraisalKPIDataDto;
import com.raybiztech.appraisalmanagement.dto.DesignationKRAMappingDto;
import com.raybiztech.appraisalmanagement.dto.KPIReviewDto;
import com.raybiztech.appraisalmanagement.dto.KRADto;
import com.raybiztech.appraisalmanagement.dto.ManagerCommentsDto;
import com.raybiztech.appraisalmanagement.dto.ReviewAuditDto;
import com.raybiztech.appraisalmanagement.dto.SearchQueryParamsInAppraisalForm;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.exceptions.CycleNotAccessException;
import com.raybiztech.appraisals.exceptions.CycleNotActiveException;
import com.raybiztech.appraisals.exceptions.DuplicateActiveCycleException;
import com.raybiztech.appraisals.exceptions.DuplicateCycleNameException;
import com.raybiztech.appraisals.exceptions.InvalidCycleRangeException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.leavemanagement.dao.LeaveDAO;
import com.raybiztech.leavemanagement.utils.EmployeeUtils;
import com.raybiztech.mailtemplates.util.AppraisalManagementMailConfiguration;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.rolefeature.business.Permission;
import com.raybiztech.separation.builder.SeparationBuilder;

/**
 *
 * @author naresh
 */
@Service("appraisalService")
@Transactional
public class AppraisalServiceImpl implements AppraisalService {

	@Autowired
	AppraisalCycleBuilder appraisalCycleBuilder;
	@Autowired
	AppraisalDao appraisalDao;
	@Autowired
	AppraisalFormBuilder appraisalFormBuilder;
	@Autowired
	KRABuilder kraBuilder;
	@Autowired
	EmployeeUtils employeeUtils;
	@Autowired
	LeaveDAO leaveDAO;
	@Autowired
	SecurityUtils utils;
	@Autowired
	SessionFactory sessionFactory;

	@Autowired
	SeparationBuilder separationBuilder;

	@Autowired
	DesignationKRAMappingBuilder designationKRAMappingBuilder;
	@Autowired
	DAO dao;

	@Autowired
	AppraisalManagementMailConfiguration appraisalManagementMailConfiguration;
	@Autowired
	ProjectService projectService;

	Logger log = Logger.getLogger(AppraisalServiceImpl.class);

	@Override
	public void addCycle(AppraisalCycleDto appraisalCycleDto) {

		AppraisalCycle appraisalCycle = appraisalCycleBuilder
				.toEntity(appraisalCycleDto);

		List<AppraisalCycle> allCycles = appraisalDao.get(AppraisalCycle.class);

		for (AppraisalCycle cycle : allCycles) {

			if (cycle.getName().equalsIgnoreCase(appraisalCycle.getName())) {
				throw new DuplicateCycleNameException();
			}

		}

		List<AppraisalCycle> activeCycles = appraisalDao.getActiveCycles();
		if (activeCycles != null && activeCycles.size() >= 1
				&& appraisalCycle.isActive()) {
			throw new DuplicateActiveCycleException(
					"Only one is active at a time");

		}

		List<AppraisalCycle> duplicateRangeCycles = appraisalDao
				.getOverriddenCycles(appraisalCycle);
		if (duplicateRangeCycles.size() >= 1) {

			throw new InvalidCycleRangeException();
		}

		appraisalDao.save(appraisalCycle);
	}

	@Override
	public List<AppraisalCycleDto> getAllAppraisalCycle() {
		List<AppraisalCycle> list = appraisalDao.get(AppraisalCycle.class);
		return appraisalCycleBuilder.toDtoList(list);
	}

	@Override
	public AppraisalFormDto getAppraisalForm(Long employeeId) {
		// Appraisal form is exist or not for current cycle and with logged in
		// employee
		AppraisalForm appraisalForm = appraisalDao
				.activeCycleApprailsalForm(employeeId);
		AppraisalFormDto appraisalFormDto = null;
		if (appraisalForm == null) {
			// step:1 get current Year Appraisal Cycle.
			AppraisalCycle appraisalCycle = appraisalDao
					.getcurrentYearAppraisalCycle();

			if (appraisalCycle == null) {

				throw new CycleNotActiveException();
			}

			// step:2 get loggedin Employee Details.
			Employee employee = appraisalDao.findBy(Employee.class, employeeId);

			/* Probationary period checking code start */
			// LeaveSettingsLookup leaveSettingsLookup =
			// leaveDAO.getLeaveSettings();
			//
			// Boolean probationaryPeriod =
			// employeeUtils.isInProbationPeriod(employee, leaveSettingsLookup);
			//
			// // Date activeCycleEndDate =
			// appraisalCycle.getPeriod().getMaximum();
			// Date
			// activeCycleStartDate=appraisalCycle.getConfigurationPeriod().getMinimum();
			// if (probationaryPeriod) {
			//
			// Date probationEndDate = employee.getJoiningDate()
			// .shift(new Duration(TimeUnit.MONTH,
			// leaveSettingsLookup.getProbationPeriod()));
			// if (probationEndDate.isAfter(activeCycleStartDate)) {
			//
			// throw new CycleNotAccessException();
			//
			// }
			// }

			Integer employeeServicePeriod = employeeUtils
					.employeeServicePeriod(employee, appraisalCycle);
			logger.info("employeeServicePeriod count:" + employeeServicePeriod);
			logger.info("condition:"
					+ (employeeServicePeriod < appraisalCycle
							.getServicePeriod()));
			if (employeeServicePeriod < appraisalCycle.getServicePeriod()) {
				throw new CycleNotAccessException();
			}

			/* Probationary period checking code end */

			// step:3 get Kra and Kpi Template based on Employee Designation.
			DesignationKRAMapping kRAMapping = appraisalDao
					.getAllKrasUnderDesignation(employee.getDesignation(),
							employee.getDepartmentName());

			// Save the appraisal form for the first time when the user clicks
			// on 'Employee Appraisal' left menu For the the first time
			AppraisalForm appraisalForm2 = new AppraisalForm();
			appraisalForm2.setAppraisalCycle(appraisalCycle);
			Set<KRA> kras = kRAMapping.getKraLookups();
			Set<AppraisalKRAData> appraisalKRADatas = new HashSet<AppraisalKRAData>();
			for (KRA kra : kras) {
				AppraisalKRAData appraisalKRAData = new AppraisalKRAData();
				// appraisalKRAData.setId(kra.getId());
				appraisalKRAData.setName(kra.getName());
				appraisalKRAData.setDescription(kra.getDescription());
				appraisalKRAData.setDesignationKraPercentage(kra
						.getDesignationKraPercentage());
				Set<KPI> kpis = kra.getKpiLookps();
				Set<AppraisalKPIData> appraisalKPIDatas = new HashSet<AppraisalKPIData>();
				for (KPI kpi : kpis) {
					AppraisalKPIData appraisalKPIData = new AppraisalKPIData();

					if (kpi.getFrequency() != null) {
						appraisalKPIData.setFrequency(kpi.getFrequency()
								.getFrequencyname());
					}
					appraisalKPIData.setTarget(kpi.getTarget());
					appraisalKPIData.setDescription(kpi.getDescription());
					appraisalKPIData.setName(kpi.getName());
					// appraisalKPIData.setId(kpi.getId());
					appraisalKPIDatas.add(appraisalKPIData);
				}
				appraisalKRAData.setKpis(appraisalKPIDatas);

				appraisalKRADatas.add(appraisalKRAData);

			}
			appraisalForm2.setKra(appraisalKRADatas);
			// employee,firstmanager and secondmanger emp
			// id's,names,departmnetnames and designation names are stored in
			// appraisalformavgratings table
			// setting avgratings to appraisal code start here
			Set<AppraisalFormAvgRatings> avgRatingses = new HashSet<AppraisalFormAvgRatings>();
			for (int i = 0; i <= appraisalCycle.getLevelOfHierarchy(); i++) {
				if (i <= 2) {
					AppraisalFormAvgRatings formAvgRatings = null;
					Employee manager1 = dao.findBy(Employee.class, employee
							.getManager().getEmployeeId());
					Employee manager2 = dao.findBy(Employee.class, manager1
							.getManager().getEmployeeId());
					if (i == 0) {
						// for employee
						formAvgRatings = new AppraisalFormAvgRatings();
						formAvgRatings.setEmployeeId(employeeId);
						formAvgRatings.setEmployeeName(employee.getFullName());
						formAvgRatings.setDepartmentName(employee
								.getDepartmentName());
						formAvgRatings.setDesignationName(employee
								.getDesignation());

					} else if (i == 1) {
						// for first manager
						formAvgRatings = new AppraisalFormAvgRatings();
						formAvgRatings.setEmployeeId(manager1.getEmployeeId());
						formAvgRatings.setEmployeeName(manager1.getFullName());
						formAvgRatings.setDepartmentName(manager1
								.getDepartmentName());
						formAvgRatings.setDesignationName(manager1
								.getDesignation());
					} else if (i == 2) {
						// for second manager
						formAvgRatings = new AppraisalFormAvgRatings();
						formAvgRatings.setEmployeeId(manager2.getEmployeeId());
						formAvgRatings.setEmployeeName(manager2.getFullName());
						formAvgRatings.setDepartmentName(manager2
								.getDepartmentName());
						formAvgRatings.setDesignationName(manager2
								.getDesignation());
					}
					formAvgRatings.setiAgree(Boolean.FALSE);
					formAvgRatings.setLevel(i);
					avgRatingses.add(formAvgRatings);
				}
			}
			appraisalForm2.setFormAvgRatings(avgRatingses);
			// setting avgratings to appraisal code start here
			appraisalForm2.setEmployee(employee);
			appraisalForm2.setCreatedBy(utils
					.getLoggedEmployeeIdforSecurityContextHolder());
			appraisalForm2.setCreatedDate(new Second());
			appraisalForm2.setFormStatus(FormStatus.SAVE);
			appraisalForm2.setRequestDiscussion(Boolean.FALSE);

			Long id = (Long) appraisalDao.save(appraisalForm2);
			AppraisalForm appraisalFormisExisted = appraisalDao
					.activeCycleApprailsalForm(employeeId);

			appraisalFormDto = appraisalFormBuilder
					.toDto(appraisalFormisExisted);

			// if(kRAMapping==null){
			//
			// throw new DesignationNotAssginedException();
			// }
			// build EmployeeAppraisalFrom Template using above
			// step:1,step:2,step:3
			// step:1,step:2,step:3

			// appraisalFormDto = appraisalCycleBuilder
			// .buildEmployeeAppraisalForm(appraisalCycle, employee,
			// kRAMapping);

		} else {
			appraisalFormDto = appraisalFormBuilder.toDto(appraisalForm);
		}

		return appraisalFormDto;
	}

	@Override
	public void employeeAppraisalForm(AppraisalFormDto appraisalFormDto) {

		Employee loggedEmployee = appraisalDao.findBy(Employee.class,
				utils.getLoggedEmployeeIdforSecurityContextHolder());
		String formStatus = appraisalFormDto.getFormStatus();
		// if (appraisalFormDto.getId() == null) {
		// synchronized (this) {
		// Employee employee = appraisalDao.findBy(Employee.class,
		// appraisalFormDto.getEmployee().getId());
		// AppraisalForm appraisalForm = appraisalFormBuilder
		// .toEntity(appraisalFormDto);
		// if(appraisalFormDto.getFormStatus().equalsIgnoreCase("SUBMIT")){
		// appraisalForm.setManagesList(loggedEmployee.getManager().getEmployeeId().toString());
		// }
		// appraisalForm.setEmployee(employee);
		// appraisalDao.getSessionFactory().getCurrentSession().flush();
		// AppraisalForm appraisalForm2 = appraisalDao
		// .activeCycleApprailsalForm(employee.getEmployeeId());
		//
		// if (appraisalForm2 == null) {
		// appraisalDao.save(appraisalForm);
		// if
		// (loggedEmployee.getEmployeeId().equals(appraisalFormDto.getEmployee().getId())
		// && formStatus.equalsIgnoreCase("Submit")) {
		// try {
		// log.warn("try for emp block for direct sumbit");
		// appraisalApplicationAcknowledgement
		// .sendEmpAppraisalApplicationAcknowledgement(appraisalForm);
		// } catch (MailCantSendException e) {
		// logger.error("Mail cannot be send:" + e);
		// }
		// }
		// } else {
		// appraisalFormDto.setId(appraisalForm2.getId());
		// saveorUpdate(appraisalFormDto);
		// }
		// }
		// } else {
		AppraisalForm appraisalForm = appraisalDao.findBy(AppraisalForm.class,
				appraisalFormDto.getId());
		if (!appraisalFormDto.getFormStatus().equalsIgnoreCase("COMPLETED"))
			appraisalForm.setFormStatus(FormStatus.valueOf(appraisalFormDto
					.getFormStatus()));

		if (formStatus.equalsIgnoreCase("SUBMIT")) {
			if (loggedEmployee.getRole().equalsIgnoreCase("admin")) {
				if (loggedEmployee.equals(appraisalForm.getEmployee()))
					appraisalForm.setManagesList(loggedEmployee.getManager()
							.getEmployeeId().toString());

				else
					appraisalForm.setFormStatus(FormStatus.PENDINGAGREEMENT);

			} else {
				// If there is some hierarchy level to get approved for a ticket
				if (appraisalForm.getManagesList() != null) {
					String[] check = appraisalForm.getManagesList().split(",");

					if (check.length < appraisalForm.getAppraisalCycle()
							.getLevelOfHierarchy()
							&& check[check.length - 1]
									.equalsIgnoreCase(loggedEmployee
											.getEmployeeId().toString())) {
						appraisalForm.setManagesList(appraisalForm
								.getManagesList()
								+ ","
								+ loggedEmployee.getManager().getEmployeeId()
										.toString());
					}
					String[] check1 = appraisalForm.getManagesList().split(",");
					// If both level and manager approval length are equal then
					// directly
					// set the approval status as 'Approved'
					if (check1.length >= appraisalForm.getAppraisalCycle()
							.getLevelOfHierarchy()
							&& check1[check1.length - 1]
									.equalsIgnoreCase(loggedEmployee
											.getEmployeeId().toString())) {
						appraisalForm
								.setFormStatus(FormStatus.PENDINGAGREEMENT);

					}
				} else {
					appraisalForm.setManagesList(loggedEmployee.getManager()
							.getEmployeeId().toString());
				}
			}
		}

		appraisalFormBuilder.updateEmployeeAppraisalForm(appraisalForm,
				appraisalFormDto);
		appraisalDao.saveOrUpdate(appraisalForm);

		if (loggedEmployee.getEmployeeId().equals(
				appraisalForm.getEmployee().getEmployeeId())
				&& appraisalForm.getFormStatus().toString()
						.equalsIgnoreCase("Submit")) {

			appraisalManagementMailConfiguration
					.sendEmployeeReviewFormSubmisson(appraisalForm);

		} else if (!loggedEmployee.getEmployeeId().equals(
				appraisalForm.getEmployee().getEmployeeId())
				&& appraisalForm.getFormStatus().toString()
						.equalsIgnoreCase("Submit")
				|| appraisalForm.getFormStatus().toString()
						.equalsIgnoreCase("PENDINGAGREEMENT")) {

			appraisalManagementMailConfiguration
					.managerReviewFeedBack(appraisalForm);

		}

	}

	public void saveorUpdate(AppraisalFormDto appraisalFormDto) {
		AppraisalForm appraisalForm = appraisalDao.findBy(AppraisalForm.class,
				appraisalFormDto.getId());
		appraisalForm.setFormStatus(FormStatus.valueOf(appraisalFormDto
				.getFormStatus()));
		appraisalFormBuilder.updateEmployeeAppraisalForm(appraisalForm,
				appraisalFormDto);
		appraisalDao.saveOrUpdate(appraisalForm);
	}

	@Override
	public void managerAppraisalForm(AppraisalFormDto appraisalFormDto) {
		AppraisalForm appraisalForm = appraisalDao.findBy(AppraisalForm.class,
				appraisalFormDto.getId());
		Set<AppraisalKRAData> kraList = appraisalFormBuilder
				.buildmanagerFeedBack(appraisalFormDto);
		appraisalForm.setKra(kraList);
	}

	@Override
	public Map<String, Object> getCurrentForms(Integer startIndex,
			Integer endIndex, Long employeeID, Long cycleId) {
		Map<String, Object> appraisalForms = null;
		Employee employee = appraisalDao.findBy(Employee.class, employeeID);
		Permission appraisalFromList = dao.checkForPermission("Review List",
				employee);
		Permission hierarchyAppraisalFromList = dao.checkForPermission(
				"Hierarchy Review List", employee);
		Permission individualAppraisalFromList = dao.checkForPermission(
				"Individual Review List", employee);
		if (appraisalFromList.getView()
				&& !hierarchyAppraisalFromList.getView()
				&& !individualAppraisalFromList.getView()) {
			// for admin role it will display all appraisal form list
			appraisalForms = appraisalDao.getAllAppraisalForm(startIndex,
					endIndex, cycleId);

		} else if (appraisalFromList.getView()
				&& hierarchyAppraisalFromList.getView()
				&& !individualAppraisalFromList.getView()) {
			// for hierarchy appraisal form list(manager's)
			appraisalForms = appraisalDao.getManagerAppraisalForms(startIndex,
					endIndex, employeeID, cycleId);
		} else if (appraisalFromList.getView()
				&& !hierarchyAppraisalFromList.getView()
				&& individualAppraisalFromList.getView()) {
			// for individual appraisal form list(employee)
			// appraisalForms =
			// appraisalDao.getEmployeeAppraisalForms(startIndex, endIndex,
			// employeeID, cycleId);
		}

		// List<AppraisalFormDto> appraisalFormDTOList=new
		// ArrayList<AppraisalFormDto>();
		List<AppraisalFormDto> appraisalFormDTOList = appraisalFormBuilder
				.toDtoList((List<AppraisalForm>) appraisalForms.get("list"));
		appraisalForms.put("list", appraisalFormDTOList);
		return appraisalForms;
	}

	@Override
	public AppraisalCycleDto getCycle(Long cycleId) {

		return appraisalCycleBuilder.toDto(appraisalDao.findBy(
				AppraisalCycle.class, cycleId));
	}

	@Override
	public void updateCycle(AppraisalCycleDto appraisalCycleDto) {

		AppraisalCycle appraisalCycle = appraisalDao.findBy(
				AppraisalCycle.class, appraisalCycleDto.getId());
		// Date currentDate=new Date();
		// if(currentDate.equals(appraisalCycle.getPeriod().getMinimum()) ||
		// currentDate.isAfter(appraisalCycle.getPeriod().getMinimum())){
		// throw new CycleNotAccessException("Cycle is already started");
		// }else{
		List<AppraisalCycle> activeCycles = appraisalDao
				.getActiveCyclesOtharthan(appraisalCycle);
		activeCycles.add(appraisalCycle);
		if (activeCycles.size() > 1 && appraisalCycleDto.isActive()) {
			throw new DuplicateActiveCycleException(
					"Only one cycle is active at a time");

		}

		appraisalDao.saveOrUpdate(appraisalCycleBuilder.createCycle(
				appraisalCycle, appraisalCycleDto));
		// }

	}

	@Override
	public AppraisalCycleDto getActiveCycleData() {

		AppraisalCycle appraisalCycle = appraisalDao
				.getcurrentYearAppraisalCycle();
		return appraisalCycleBuilder.toDto(appraisalCycle);
	}

	@Override
	public List<KRADto> getAllKrasUnderDesignation(Long cycleId,
			Long designationId) {
		DesignationKRAMapping aMapping = appraisalDao
				.getAllKrasUnderCyclewithDesignation(cycleId, designationId);
		Set<KRA> kras = new HashSet<KRA>();
		if (aMapping != null) {
			kras.addAll(aMapping.getKraLookups());
		}
		return kraBuilder.toDtoList(new ArrayList(kras));

	}

	@Override
	public void validateCycle(AppraisalCycleDto appraisalCycleDto) {

		List<AppraisalCycle> cycles = appraisalDao.getCycles(appraisalCycleDto
				.getName());
		if (cycles.size() >= 1) {

			throw new DuplicateCycleNameException();
		}

	}

	@Override
	public void validateCycleRange(AppraisalCycleDto appraisalCycleDto) {

		AppraisalCycle appraisalCycle = appraisalCycleBuilder
				.toEntity(appraisalCycleDto);

		List<AppraisalCycle> duplicateRangeCycles = appraisalDao
				.getOverriddenCycles(appraisalCycle, appraisalCycle.getId());
		if (duplicateRangeCycles.size() >= 1) {

			throw new InvalidCycleRangeException();
		}

	}

	@Cacheable("empAppraisals")
	@Override
	public Map<String, Object> searchEmployee(
			SearchQueryParamsInAppraisalForm paramsInAppraisalForm) {
		
		//Not-submitted list based on 30 days from employee joining date  
		/*java.util.Date javaDate=null;
		AppraisalCycle cycle=dao.findBy(AppraisalCycle.class, paramsInAppraisalForm.getCycleId());
		
		String date=cycle.getConfigurationPeriod().getMinimum().toString("dd/MM/yyyy");
		DateFormat format=new SimpleDateFormat("dd/MM/yyyy");
		
		try {
			javaDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	Calendar cal= Calendar.getInstance();
	cal.setTime(javaDate);
	Integer servicePeriod=cycle.getServicePeriod();
	cal.add(Calendar.DATE, -servicePeriod);
	java.util.Date cycleDate=cal.getTime();
	String finalDate=format.format(cycleDate);*/
		
		Map<String, Object> appraisalForms = null;
		List<AppraisalFormListDto> appraisalFormDTOList = new ArrayList<AppraisalFormListDto>();
		Employee employee = (Employee) utils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		Date fromdate = null;
		Date toDate = null;
		if (paramsInAppraisalForm.getFromDate() != null
				&& paramsInAppraisalForm.getToDate() != null) {
			fromdate = stringToDate("01/" + paramsInAppraisalForm.getFromDate());

			DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
			Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(dateFormat.parse(paramsInAppraisalForm
						.getToDate()));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			toDate = stringToDate(calendar
					.getActualMaximum(Calendar.DAY_OF_MONTH)
					+ "/"
					+ paramsInAppraisalForm.getToDate());
		}
		List<Long> apparaisalIds = null;
		if (fromdate != null && toDate != null) {
			apparaisalIds = appraisalDao.appraisalId(paramsInAppraisalForm,
					fromdate, toDate);
		}
		paramsInAppraisalForm.setEmployeeID(employee.getEmployeeId());
		Permission appraisalFromList = dao.checkForPermission("Review List",
				employee);
		Permission hierarchyAppraisalFromList = dao.checkForPermission(
				"Hierarchy Review List", employee);
		Permission individualAppraisalFromList = dao.checkForPermission(
				"Individual Review List", employee);
		if (appraisalFromList.getView()
				&& !hierarchyAppraisalFromList.getView()
				&& !individualAppraisalFromList.getView()) {
			
			//for admin role it will display  review form not-submitted list 
			if(paramsInAppraisalForm.getAppraisalFormStatus().equalsIgnoreCase("SAVE")) {
				Map<String, Object> formNotSubmittedList = null;
				formNotSubmittedList = appraisalDao.getNotSubmittedFormEmployees(paramsInAppraisalForm ,apparaisalIds);
				AppraisalCycle appraisalMonth=appraisalDao.findBy(AppraisalCycle.class, paramsInAppraisalForm.getCycleId());
				List<AppraisalFormListDto> appraisalFormDTO = appraisalFormBuilder.appraisalFormNotSubmittedList((List) formNotSubmittedList.get("list"),appraisalMonth);
				formNotSubmittedList.put("list", appraisalFormDTO);
				return formNotSubmittedList;
			}
			else
			{
				// for admin role it will display all appraisal form list
			appraisalForms = appraisalDao.getAllAppraisalFormByAdmin(
					paramsInAppraisalForm, apparaisalIds);
			}

		} else if (appraisalFromList.getView()
				&& hierarchyAppraisalFromList.getView()
				&& !individualAppraisalFromList.getView()) {
			
			//for Manager role it will display  review form not-submitted list 
			if(paramsInAppraisalForm.getAppraisalFormStatus().equalsIgnoreCase("SAVE")) {
				Map<String, Object> formNotSubmittedList = null;
				AppraisalCycle appraisalMonth=appraisalDao.findBy(AppraisalCycle.class, paramsInAppraisalForm.getCycleId());
				formNotSubmittedList = appraisalDao.getNotSubmittedFormEmployees(paramsInAppraisalForm ,apparaisalIds);
				List<AppraisalFormListDto> appraisalFormDTO = appraisalFormBuilder.appraisalFormNotSubmittedList((List) formNotSubmittedList.get("list"),appraisalMonth);
				formNotSubmittedList.put("list", appraisalFormDTO);
				return formNotSubmittedList;
			}
			else {
				// for hierarchy appraisal form list(manager's)
				List<Long> empIds = projectService.mangerUnderManager(utils
						.getLoggedEmployeeIdforSecurityContextHolder());
				appraisalForms = appraisalDao.getAllAppraisalFormByManager(
						paramsInAppraisalForm, empIds, apparaisalIds);
			}
		
		} else if (appraisalFromList.getView()
				&& !hierarchyAppraisalFromList.getView()
				&& individualAppraisalFromList.getView()) {
			// for individual appraisal form list(employee)
			appraisalForms = appraisalDao
					.getEmployeeAppraisalForms(paramsInAppraisalForm);
		}

		// List<AppraisalFormDto> appraisalFormDTOList = appraisalFormBuilder
		// .toDtoList((List<AppraisalForm>) appraisalForms.get("list"));
		
		if (appraisalForms.get("list") != null) {
			for (AppraisalForm appraisalForm : (List<AppraisalForm>) appraisalForms
					.get("list")) {
				appraisalFormDTOList.add(appraisalFormBuilder
						.toFormList(appraisalForm));
			}
		}
		appraisalForms.put("list", appraisalFormDTOList);
		return appraisalForms;

	}

	/*
	 * @Override public Map<String, Object> searchByManagerName(Integer
	 * startIndex, Integer endIndex, Long employeeID, Long cycleId,String
	 * searchString) { Map<String, Object> appraisalForms = null;
	 * 
	 * Employee employee = appraisalDao.findBy(Employee.class, employeeID);
	 * 
	 * if (employee.getRole().equalsIgnoreCase("admin")) {
	 * 
	 * appraisalForms = appraisalDao.searchByManagerName(startIndex,
	 * endIndex,employeeID, cycleId,searchString);
	 * 
	 * } if (employee.getRole().equalsIgnoreCase("manager")) {
	 * 
	 * appraisalForms = appraisalDao.getAllAppraisalFormByManager(startIndex,
	 * endIndex, employeeID, cycleId,searchString); }
	 * 
	 * List<AppraisalFormDto> appraisalFormDTOList = appraisalFormBuilder
	 * .toDtoList((List<AppraisalForm>) appraisalForms.get("list"));
	 * appraisalForms.put("list", appraisalFormDTOList); return appraisalForms;
	 * }
	 */

	@Override
	public Map<String, Object> getDesignationsUnderCycle(Integer startIndex,
			Integer endIndex, Long cycleId) {
		Map<String, Object> aMapping = null;
		aMapping = appraisalDao.getDesignationsUnderCycle(startIndex, endIndex,
				cycleId);
		List<DesignationKRAMappingDto> kRAMappingDtos = designationKRAMappingBuilder
				.toDtoList((List<DesignationKRAMapping>) aMapping.get("list"));
		aMapping.put("list", kRAMappingDtos);
		return aMapping;
	}

	@Override
	public Double employeeAppraisalFormForRating(
			AppraisalFormDto appraisalFormDto) {
		Long loggedInEmpId = utils
				.getLoggedEmployeeIdforSecurityContextHolder();

		Double rating = appraisalFormBuilder.defaultAvgRating(appraisalFormDto,
				loggedInEmpId);

		return rating;
	}

	@Override
	public AppraisalFormDto getExistingAppraisalForm(Long appraisalFormId) {
		AppraisalForm appraisalForm = appraisalDao.findBy(AppraisalForm.class,
				appraisalFormId);
		AppraisalFormDto appraisalFormDto = null;
		if (appraisalForm != null) {
			appraisalFormDto = appraisalFormBuilder.toDto(appraisalForm);
		}
		return appraisalFormDto;
	}

	@Override
	public void closeAppraisalForm(AppraisalFormDto appraisalFormDto) {
		AppraisalForm appraisalForm = dao.findBy(AppraisalForm.class,
				appraisalFormDto.getId());
		appraisalForm.setClosedOn(new Date());
		appraisalForm.setClosedSummary(appraisalFormDto.getClosedSummary());
		appraisalForm.setFormStatus(FormStatus.CLOSED);
		appraisalForm.setClosedStatus(appraisalFormDto.getClosedStatus());
		appraisalForm.setClosedBy(utils
				.getLoggedEmployeeIdforSecurityContextHolder());
		dao.update(appraisalForm);

	}

	@Override
	public void appraisalConfirmation(AppraisalFormDto appraisalFormDto) {

		dao.update(appraisalFormBuilder
				.conformationAppriasalForm(appraisalFormDto));

		AppraisalForm appraisalForm = dao.findBy(AppraisalForm.class,
				appraisalFormDto.getId());

		appraisalManagementMailConfiguration
				.appraisalFormreviewAcknowledgement(appraisalForm);

	}

	@Override
	public void copyTheCycleData(@RequestParam Long oldCycleId,
			@RequestParam Long newCycleId) {

		/*
		 * List<DesignationKRAMapping>
		 * cycleList=appraisalDao.getAppraisalCycleList(newCycleId);
		 * 
		 * if(!cycleList.isEmpty()){
		 * 
		 * throw new CycleAlreadyExistedException("Cycle is already existed"); }
		 */

		appraisalDao.copyTheCycleData(oldCycleId, newCycleId);
	}

	@Override
	public Boolean isAlreadyExist(Long newCycleId) {
		return appraisalDao.getAppraisalCycleList(newCycleId);

	}

	@Override
	public void requestDiscussion(Long appraisalFormId) {
		AppraisalForm appraisalForm = dao.findBy(AppraisalForm.class,
				appraisalFormId);
		Employee employee = (Employee) utils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		if (appraisalForm.getEmployee().equals(employee)) {
			appraisalForm.setFormStatus(FormStatus.OPENFORDISCUSSION);
			appraisalForm.setRequestDiscussion(Boolean.TRUE);
			dao.update(appraisalForm);
			appraisalManagementMailConfiguration
					.requestForDiscussion(appraisalForm);
		}
	}

	@Override
	public void saveReviewComments(String comments, Long appraisalFormId) {
		AppraisalForm appraisalForm = dao.findBy(AppraisalForm.class,
				appraisalFormId);
		Employee employee = (Employee) utils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		if (appraisalForm.getEmployee().equals(employee)) {
			appraisalForm.setFormStatus(FormStatus.OPENFORDISCUSSION);
			appraisalForm.setRequestDiscussion(Boolean.TRUE);
			dao.update(appraisalForm);

			ReviewAudit audit = new ReviewAudit();
			audit.setAppraisalForm(appraisalForm);
			audit.setEmployee(employee);
			audit.setComments(comments);
			audit.setStatus(appraisalForm.getFormStatus().toString());
			audit.setCreatedDate(new Second());
			dao.save(audit);
			appraisalManagementMailConfiguration
					.requestForDiscussion(appraisalForm);
		}

	}

	@Override
	public Map<String, Object> getReviewComments(Long appriasalFormId) {
		// TODO Auto-generated method stub
		Map<String, List<ReviewAudit>> map = null;
		List<ReviewAudit> reviewAudits = appraisalDao
				.getReviewComments(appriasalFormId);
		Map<String, Object> returnMap = new HashMap<String, Object>();
		List<ReviewAuditDto> revAuditDtos = new ArrayList<ReviewAuditDto>();
		if (!reviewAudits.isEmpty()) {
			map = getPaireValue(reviewAudits);

			for (Map.Entry<String, List<ReviewAudit>> entry : map.entrySet()) {
				ReviewAuditDto auditDto = new ReviewAuditDto();

				Set<KPIReviewDto> setOfkpiReviewDtos = new HashSet<KPIReviewDto>();

				for (ReviewAudit audit : entry.getValue()) {
					auditDto.setId(audit.getId());
					auditDto.setComments(audit.getComments());
					auditDto.setCreatedDate(audit.getCreatedDate().toString());
					auditDto.setEmployeeName(audit.getEmployee().getFullName());
					auditDto.setStatus(audit.getStatus());

					KPIReviewDto kpiReviewDto = new KPIReviewDto();
					kpiReviewDto.setKpiName(audit.getKpiName());
					kpiReviewDto.setOldValue(audit.getOldValue());
					kpiReviewDto.setNewValue(audit.getNewValue());

					setOfkpiReviewDtos.add(kpiReviewDto);

					auditDto.setKpiReviewDtos(setOfkpiReviewDtos);
				}
				revAuditDtos.add(auditDto);
			}
		}
		returnMap.put("list", revAuditDtos);
		returnMap.put("size", revAuditDtos.size());

		return returnMap;
	}

	public Map<String, List<ReviewAudit>> getPaireValue(List<ReviewAudit> audits) {
		Map<String, List<ReviewAudit>> map = new HashMap<String, List<ReviewAudit>>();
		List<ReviewAudit> auditList = new ArrayList<ReviewAudit>();
		ReviewAudit audit2 = audits.get(0);
		auditList.add(audit2);
		map.put(audit2.getCreatedDate().toString(), auditList);
		audits.remove(0);
		for (ReviewAudit reviewAudit : audits) {
			if (map.get(reviewAudit.getCreatedDate().toString()) == null) {
				List<ReviewAudit> newAuditList = new ArrayList<ReviewAudit>();
				newAuditList.add(reviewAudit);
				map.put(reviewAudit.getCreatedDate().toString(), newAuditList);

			} else {
				List<ReviewAudit> tempList = map.get(reviewAudit
						.getCreatedDate().toString());
				tempList.add(reviewAudit);
				map.put(reviewAudit.getCreatedDate().toString(), tempList);

			}
		}

		return map;
	}

	@Override
	public void changedKPISRating(AppraisalFormDto kpis) {
		// TODO Auto-generated method stub
		Employee employee = (Employee) utils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		for (AppraisalKPIDataDto kpi : kpis.getKpis()) {
			ReviewAudit audit = new ReviewAudit();
			for (ManagerCommentsDto kpiData : kpi.getManagerCommentsDtos()) {
				AppraisalKPIData appraisalKPIData = dao.findBy(
						AppraisalKPIData.class, kpiData.getId());
				audit.setOldValue(appraisalKPIData.getRating().toString());
			}
			audit.setNewValue(kpi.getManagerRating().toString());
			audit.setKpiName(kpi.getName());
			audit.setCreatedDate(new Second());
			audit.setStatus(FormStatus.PENDINGAGREEMENT.toString());
			audit.setEmployee(employee);
			audit.setAppraisalForm(dao.findBy(AppraisalForm.class, kpis.getId()));
			dao.save(audit);
		}
	}

	public ByteArrayOutputStream exportAppraisalList(
			SearchQueryParamsInAppraisalForm paramsInAppraisalForm)
			throws IOException {
		Map<String, Object> appraisalForms = null;
		List<AppraisalForm> appraisalFormList = null;
		//appraisalForms = searchEmployee(paramsInAppraisalForm);

			Employee employee = (Employee) utils
					.getLoggedEmployeeDetailsSecurityContextHolder()
					.get("employee");
			Date fromdate = null;
			Date toDate = null;

			if (!paramsInAppraisalForm.getFromDate().equalsIgnoreCase("null")
					&& !paramsInAppraisalForm.getToDate().equalsIgnoreCase("null")) {
				fromdate = stringToDate("01/" + paramsInAppraisalForm.getFromDate());

				DateFormat dateFormat = new SimpleDateFormat("MM/yyyy");
				Calendar calendar = Calendar.getInstance();
				try {
					calendar.setTime(dateFormat.parse(paramsInAppraisalForm
							.getToDate()));
				} catch (ParseException e) {
					e.printStackTrace();
				}
				toDate = stringToDate(calendar
						.getActualMaximum(Calendar.DAY_OF_MONTH)
						+ "/"
						+ paramsInAppraisalForm.getToDate());
			}

			List<Long> apparaisalIds = null;

			if (fromdate != null && toDate != null) {

				apparaisalIds = appraisalDao.appraisalId(paramsInAppraisalForm,
						fromdate, toDate);
			}

			paramsInAppraisalForm.setEmployeeID(employee.getEmployeeId());

			Permission appraisalFromList = dao.checkForPermission("Review List",
					employee);
			Permission hierarchyAppraisalFromList = dao.checkForPermission(
					"Hierarchy Review List", employee);
			Permission individualAppraisalFromList = dao.checkForPermission(
					"Individual Review List", employee);
			if (appraisalFromList.getView()
					&& !hierarchyAppraisalFromList.getView()
					&& !individualAppraisalFromList.getView()) {
				// for admin role it will display all appraisal form list
				if(paramsInAppraisalForm.getAppraisalFormStatus().equalsIgnoreCase("SAVE")) {
					appraisalFormList =   appraisalFormBuilder.exportappraisalFormsList((List<Employee>) appraisalDao.getNotSubmittedFormEmployees(paramsInAppraisalForm,apparaisalIds).get("list"));
				/* appraisalFormList = (List<AppraisalForm>) appraisalForms
						.get("list");*/
				
				}
				else
				{
					appraisalForms = appraisalDao.getAllAppraisalFormByAdmin(
							paramsInAppraisalForm, apparaisalIds);
				}
			
			} else if (appraisalFromList.getView()
					&& hierarchyAppraisalFromList.getView()
					&& !individualAppraisalFromList.getView()) {
				// for hierarchy appraisal form list(manager's)
				List<Long> empIds = projectService.mangerUnderManager(utils
						.getLoggedEmployeeIdforSecurityContextHolder());
				
				
				if(paramsInAppraisalForm.getAppraisalFormStatus().equalsIgnoreCase("SAVE")) {
					appraisalFormList =   appraisalFormBuilder.exportappraisalFormsList((List<Employee>) appraisalDao.getNotSubmittedFormEmployees(paramsInAppraisalForm,apparaisalIds).get("list"));
				/* appraisalFormList = (List<AppraisalForm>) appraisalForms
						.get("list");*/
				
				}
				else {
					appraisalForms = appraisalDao.getAllAppraisalFormByManager(
							paramsInAppraisalForm, empIds, apparaisalIds);
				}
				
			} else if (appraisalFromList.getView()
					&& !hierarchyAppraisalFromList.getView()
					&& individualAppraisalFromList.getView()) {
				// for individual appraisal form list(employee)
				appraisalForms = appraisalDao
						.getEmployeeAppraisalForms(paramsInAppraisalForm);
			}
			// List<AppraisalFormDto> appraisalFormDTOList = appraisalFormBuilder
			// .toDtoList((List<AppraisalForm>) appraisalForms.get("list"));
			if(appraisalForms!=null)
			{
		 appraisalFormList = (List<AppraisalForm>) appraisalForms
					.get("list");
			}
		
		/*List<AppraisalForm> appraisalFormList = (List<AppraisalForm>) map
				.get("list");
		*/
		
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
		cell0.setCellStyle(style);
		cell0.setCellValue("ID");

		Cell cell1 = row1.createCell(1);
		cell1.setCellStyle(style);
		cell1.setCellValue("Employee Name");

		Cell cell2 = row1.createCell(2);
		cell2.setCellStyle(style);
		cell2.setCellValue("Manager Name");

		Cell cell3 = row1.createCell(3);
		cell3.setCellStyle(style);
		cell3.setCellValue("Department");

		Cell cell4 = row1.createCell(4);
		cell4.setCellStyle(style);
		cell4.setCellValue("Designation");

		Cell cell5 = row1.createCell(5);
		cell5.setCellStyle(style);
		cell5.setCellValue("Month");

		Cell cell6 = row1.createCell(6);
		cell6.setCellStyle(style);
		cell6.setCellValue("Emp Avg Rating");

		Cell cell7 = row1.createCell(7);
		cell7.setCellStyle(style);
		cell7.setCellValue("Manager's Avg Rating");

		Cell cell8 = row1.createCell(8);
		cell8.setCellStyle(style);
		cell8.setCellValue("Status");
		
		Cell cell9 = row1.createCell(9);
		cell9.setCellStyle(style);
		cell9.setCellValue("Date of joining");
		
		for (AppraisalForm appraisalform : appraisalFormList) {
			Row row = sheet.createRow(rowindex++);
			Cell cel2 = row.createCell(2);
			Cell cel3 = row.createCell(3);
			Cell cel4 = row.createCell(4);
			Cell cel7 = row.createCell(7);
			Cell cel0 = row.createCell(0);
			Cell cel5 = row.createCell(5);
			Cell cel6 = row.createCell(6);
			Cell cel9 = row.createCell(9);
			cel9.setCellValue(appraisalform.getEmployee().getJoiningDate().toString());
			cel0.setCellValue(appraisalform.getEmployee().getEmployeeId());
			Cell cel1 = row.createCell(1);
			cel1.setCellValue(appraisalform.getEmployee().getEmployeeFullName());
			if(!paramsInAppraisalForm.getAppraisalFormStatus().equalsIgnoreCase("SAVE"))
			{
				cel5.setCellValue(appraisalform.getAppraisalCycle()
						.getAppraisalPeriod().getMinimum()
						.toString("MMMM yyyy"));
			}
			else
			{
			AppraisalCycle cycle=appraisalDao.findBy(AppraisalCycle.class, paramsInAppraisalForm.getCycleId());
			cel5.setCellValue(cycle
					.getAppraisalPeriod().getMinimum()
					.toString("MMMM yyyy"));
			}
			if(appraisalform.getFormAvgRatings()!=null)
			{
			Set<AppraisalFormAvgRatings> setOfAvgRatings = appraisalform
					.getFormAvgRatings();

			for (AppraisalFormAvgRatings formAvgRatings : setOfAvgRatings) {

				if (formAvgRatings.getEmployeeId().equals(
						appraisalform.getEmployee().getEmployeeId())
						&& (formAvgRatings.getLevel().equals(0))) {
					cel3.setCellValue(formAvgRatings.getDepartmentName());
					cel4.setCellValue(formAvgRatings.getDesignationName());
					
					if (formAvgRatings.getDefaultAvgRating() != null) {
						cel6.setCellValue(formAvgRatings.getDefaultAvgRating());
					} else {
						cel6.setCellValue("N/A");
					}

				} else {

					cel2.setCellValue(formAvgRatings.getEmployeeName());
					if (formAvgRatings.getDefaultAvgRating() != null) {
						cel7.setCellValue(formAvgRatings.getDefaultAvgRating());
					} else {
						cel7.setCellValue("N/A");
					}

				}

			 }
			}
			else
			{
				cel2.setCellValue(appraisalform.getEmployee().getManager().getFullName());
				cel3.setCellValue(appraisalform.getEmployee().getDepartmentName());
				cel4.setCellValue(appraisalform.getEmployee().getDesignation());
				cel6.setCellValue("N/A");
				cel7.setCellValue("N/A");
				cel9.setCellValue(appraisalform.getEmployee().getJoiningDate().toString());
		
			}
		
			Cell cel8 = row.createCell(8);
			
			if(appraisalform.getFormStatus()!=null)
			{
			
			if (appraisalform.getFormStatus().name().equalsIgnoreCase("SUBMIT")
					|| appraisalform.getFormStatus().name()
							.equalsIgnoreCase("PENDING")) {
				cel8.setCellValue("Review Pending");
			} else if (appraisalform.getFormStatus().name()
					.equalsIgnoreCase("PENDINGAGREEMENT")) {

				cel8.setCellValue("Needs Acknowledgement");

			} else if (appraisalform.getFormStatus().name()
					.equalsIgnoreCase("OPENFORDISCUSSION")) {

				cel8.setCellValue("Needs Discussion");

			} else if (appraisalform.getFormStatus().name()
					.equalsIgnoreCase("COMPLETED")) {
				cel8.setCellValue("Completed");

			} else if (appraisalform.getFormStatus().name()
					.equalsIgnoreCase("CLOSED")) {
				cel8.setCellValue("Closed");

			}

			else if (appraisalform.getFormStatus().name()
					.equalsIgnoreCase("SAVE")) {
				cel8.setCellValue("Not-Submitted");

			}
		}
			else
			{
				cel8.setCellValue("Not-Submitted");
			}
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
			sheet.autoSizeColumn(6);
			sheet.autoSizeColumn(7);
		}
		workbook.write(bos);
		bos.flush();
		bos.close();
		return bos;

	}

	public Date stringToDate(String strDate) {
		Date date = null;
		try {
			date = Date.parse(strDate, "dd/MM/yyyy");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
}
