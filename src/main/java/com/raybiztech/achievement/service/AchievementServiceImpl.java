package com.raybiztech.achievement.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.achievement.builder.AchievementAuditBuilder;
import com.raybiztech.achievement.builder.AchievementBuilder;
import com.raybiztech.achievement.builder.AchievementTypeBuilder;
import com.raybiztech.achievement.business.Achievement;
import com.raybiztech.achievement.business.AchievementType;
import com.raybiztech.achievement.business.Leadership;
import com.raybiztech.achievement.business.LeadershipAudit;
import com.raybiztech.achievement.dao.AchievementDao;
import com.raybiztech.achievement.dto.AchievementDTO;
import com.raybiztech.achievement.dto.AchievementTypeDTO;
import com.raybiztech.achievement.dto.LeadershipDTO;
import com.raybiztech.achievement.exceptions.NotEnoughLeavesAvaialableException;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;
import com.raybiztech.date.MonthOfYear;
import com.raybiztech.date.MonthYear;
import com.raybiztech.date.Second;
import com.raybiztech.date.YearOfEra;
import com.raybiztech.leavemanagement.utils.LeaveManagementUtils;
import com.raybiztech.projectmanagement.builder.AuditBuilder;
import com.raybiztech.projectmanagement.business.Audit;
import com.raybiztech.recruitment.service.JobPortalService;
import com.raybiztech.recruitment.utils.DateParser;
import com.raybiztech.recruitment.utils.FileUploaderUtility;

/**
 *
 * @author Aprajita
 */

@Service("achievementServiceImpl")
@Transactional
public class AchievementServiceImpl implements AchievementService, Cloneable {

	Logger logger = Logger.getLogger(AchievementServiceImpl.class);

	@Autowired
	DAO dao;

	@Autowired
	AchievementDao achievementDaoImpl;

	@Autowired
	AchievementTypeBuilder achievementTypeBuilder;

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	AchievementBuilder achievementBuilder;

	@Autowired
	PropBean propBean;

	@Autowired
	JobPortalService jobPortalServiceImpl;

	@Autowired
	AchievementAuditBuilder achievementAuditBuilder;

	@Autowired
	AuditBuilder auditBuilder;

	/* add achievement type */
	@Override
	public void addAchievementType(AchievementTypeDTO achievementTypeDTO) {

		AchievementType achievementType = achievementTypeBuilder
				.convertAchievementDTOToEntity(achievementTypeDTO);
		achievementDaoImpl.save(achievementType);

	}

	/* To get all Achievement type List */
	@Override
	public Map<String, Object> getAllAchievementType() {

		Map<String, Object> achievementTypeMap = null;

		achievementTypeMap = achievementDaoImpl.getAllAchievementType();
		List<AchievementType> achievementTypesList = (List<AchievementType>) achievementTypeMap
				.get("list");
		Integer noOfRecords = (Integer) achievementTypeMap.get("size");
		List<AchievementTypeDTO> achievementTypeDTOsList = achievementTypeBuilder
				.convertToDTOList(achievementTypesList);
		Map<String, Object> achievementListMap = new HashMap<String, Object>();
		achievementListMap.put("list", achievementTypeDTOsList);
		achievementListMap.put("size", noOfRecords);

		return achievementListMap;
	}

	/* To Update achievement type name */
	@Override
	public void updateAchievementType(AchievementTypeDTO achievementTypeDto) {
		dao.update(achievementTypeBuilder.toEditEntity(achievementTypeDto));
	}

	/* To Delete achievement type name */
	@Override
	public void deleteAchievementType(Long typeId) {
		dao.delete(dao.findBy(AchievementType.class, typeId));

	}

	/* add achievement */
	@Override
	public Long addAchievement(AchievementDTO achievementDTO) {

		Achievement achievement = achievementBuilder
				.convertAchievementDToToAchievementEntity(achievementDTO);
		Long id = (Long) achievementDaoImpl.save(achievement);
		List<Audit> achievementAudit = auditBuilder.auditToAchievementEntity(
				achievement, id, "Achievement", "CREATED");
		for (Audit audit : achievementAudit) {
			achievementDaoImpl.save(audit);
		}
		Long ID = achievement.getId();
		return ID;
	}

	/* To get all Achievement List */
	@Override
	public Map<String, Object> getAllAchievement(Integer startIndex,
			Integer endIndex, Long achievementTypeId, String dateSelection,
			Integer fromMonth, Integer fromYear, Integer toMonth, Integer toYear) {

		Map<String, Object> achievementMap = null;
		Date finalFromDate = null;
		Date finalToDate = null;

		if (dateSelection.equalsIgnoreCase("custom")) {

			LeaveManagementUtils leaveManagementUtils = new LeaveManagementUtils();

			MonthOfYear fromMonthOfYear = MonthOfYear.valueOf(fromMonth - 1);
			YearOfEra fromYearOfEra = YearOfEra.valueOf(fromYear);
			MonthOfYear toMonthOfYear = MonthOfYear.valueOf(toMonth - 1);
			YearOfEra toYearOfEra = YearOfEra.valueOf(toYear);

			DateRange fromMonthPeriod = leaveManagementUtils
					.getConstructMonthPeriod(new MonthYear(fromMonthOfYear,
							fromYearOfEra));

			DateRange toMonthPeriod = leaveManagementUtils
					.getConstructMonthPeriod(new MonthYear(toMonthOfYear,
							toYearOfEra));

			finalFromDate = fromMonthPeriod.getMinimum();

			finalToDate = toMonthPeriod.getMaximum();

		} else {
			Map<String, Date> dateMap = dao.getCustomDates(dateSelection);
			finalFromDate = dateMap.get("startDate");
			finalToDate = dateMap.get("endDate");
		}

		DateRange dateRange = new DateRange(finalFromDate, finalToDate);

		achievementMap = achievementDaoImpl.getAllAchievement(startIndex,
				endIndex, achievementTypeId, dateRange);

		List<Achievement> achievementList = (List<Achievement>) achievementMap
				.get("list");

		Integer noOfRecords = (Integer) achievementMap.get("size");
		List<AchievementDTO> achievementDtoList = achievementBuilder
				.toDTOList(achievementList);
		Map<String, Object> achievmentListMap = new HashMap<String, Object>();
		achievmentListMap.put("list", achievementDtoList);
		achievmentListMap.put("size", noOfRecords);

		return achievmentListMap;

	}

	@Override
	public void uploadAchievementCroppedImage(String parameter, Long empLong) {

		Employee employee = dao.findBy(Employee.class, empLong);
		FileUploaderUtility fileUploaderUtility = new FileUploaderUtility();
		byte[] bytes = Base64.decodeBase64(parameter);

		try {
			fileUploaderUtility.uploadBase64Image(bytes, employee, propBean);
		} catch (IOException e) {

			/*
			 * Logger.getLogger(AchievementServiceImpl.class.getName()).log(
			 * Level.SEVERE, null, e);
			 */
			employee.setProfilePicPath("default.gif");
			employee.setThumbPicture("default.gif");
			dao.update(employee);
		}
	}

	/* To show Associate Award in dash board */
	@Override
	public List<AchievementDTO> showAssociateOfQuarter() {
		return achievementBuilder.toDTOList(achievementDaoImpl
				.getAchieversOnDashBoard());
	}

	/* To show Services Awards in dash board */
	@Override
	public List<AchievementDTO> showServiceAward() {
		return achievementBuilder.toDTOList(achievementDaoImpl
				.getServiceOnDashBoard());
	}

	/* Toggle button show for dash board */
	@Override
	public void showOnDashBoard(Long achievementId, String dashBoardStatus) {
		Achievement achievement = dao.findBy(Achievement.class, achievementId);
		Achievement oldAchievement = new Achievement();
		try {
			oldAchievement = (Achievement) achievement.clone();
		} catch (CloneNotSupportedException ce) {
			java.util.logging.Logger.getLogger(
					AchievementServiceImpl.class.getName()).log(Level.SEVERE,
					null, ce);
		}
		achievement.setUpdatedBy(securityUtils
				.getLoggedEmployeeIdforSecurityContextHolder());
		achievement.setUpdatedDate(new Second());
		achievement.setShowOnDashBoard((dashBoardStatus
				.equalsIgnoreCase("true") ? Boolean.TRUE : Boolean.FALSE));
		dao.update(achievement);

		List<Audit> showAudit = auditBuilder.UpdateAuditToAchievementEntity(
				achievement, achievementId, oldAchievement, "Achievement",
				"UPDATED");
		for (Audit audit : showAudit) {
			achievementDaoImpl.save(audit);
		}
	}

	/* To get employee profile image on selection of their name */
	@Override
	public EmployeeDTO getImageData(Long empId) {

		EmployeeDTO dto = new EmployeeDTO();
		dto.setId(empId);
		dto.setImageData(jobPortalServiceImpl.getImage(empId).getImageData());
		return dto;
	}

	@Override
	public Map<String, Object> getAchievementHistory(Long achievementId,
			String filterName) {
		String dbName = null;
		if (filterName.equalsIgnoreCase("achievementHistory")) {
			dbName = "Achievement";
		}
		Map<String, List<Audit>> map = dao.getAudit(achievementId, dbName);

		return achievementAuditBuilder.ToAchievementAuditDTO(map);

	}

	@Override
	public List<AchievementDTO> showStarOfTheMonth() {
		
		return achievementBuilder.toDTOList(achievementDaoImpl.showStarOfTheMonth());
	}

	@Override
	public Map<String, List<AchievementDTO>> getAllAchievementList() {
		// TODO Auto-generated method stub
		
		Map<String,List<AchievementDTO>> achievmentMap = new HashMap<String, List<AchievementDTO>>();
		
		List<Achievement> achevementList = achievementDaoImpl.getAllAchievementList();
		
		achievmentMap = achievementBuilder.convertAllachievementList(achevementList);
		
		return achievmentMap;
	}

	@Override
	public AchievementTypeDTO getAchievementTypeDetails(Long typeId) {
		
		return achievementTypeBuilder.convertAchivementTypeToDTO
				(achievementDaoImpl.getAchievementTypeDetails(typeId));
	}
	
	@Override
	public Map<String, Object> getEmployeeDetails() {
		Map<String, Object> map = new HashMap<String, Object>();
		Employee emp = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder().get("employee");
		EmployeeDTO dto = new EmployeeDTO();
		dto.setFullName(emp.getFullName());
		dto.setId(emp.getEmployeeId());
		//dto.setCompanyExperience(emp.getCompanyExperience());
		//Checking Duplicate Entry
		List<Leadership> duplicateLeadership = achievementDaoImpl.checkLeadershipDuplication(emp.getEmployeeId());
		if(duplicateLeadership.isEmpty()){
			map.put("duplicate", false);
		}
		else{
			map.put("duplicate", true);
		}
		map.put("Id", dto.getId());
		map.put("name", dto.getFullName());
		//map.put("experience", dto.getCompanyExperience());
		
		return map;
	}
	
	@Override
	public void addLeadership(LeadershipDTO leadershipDetails) {
		
		Employee emp = (Employee) securityUtils.getLoggedEmployeeDetailsSecurityContextHolder().get("employee");
		
		List<Leadership> duplicateLeadership = achievementDaoImpl.checkLeadershipDuplication(emp.getEmployeeId());
		
		boolean flag = false;
		
		if(duplicateLeadership.isEmpty()){
			
			flag = false;
		}
		
		else{
			flag = true;
		}
		System.out.println("exp:" + emp.getCompanyExperience());
		if(flag){
			System.out.println("already applied");
			throw new NotEnoughLeavesAvaialableException("Sorry! Your form is already submitted or you are not elligible!!");
		}else if(emp.getCompanyExperience()<1){
          System.out.println("experience required atleast 1 year");
			throw new NotEnoughLeavesAvaialableException("Sorry! You should have experience of atleast 1 year!!");
			
		}
		else{
			Leadership leadership = new Leadership();
			if(leadershipDetails.getAcceptance() != null && leadershipDetails.getCommunicate() != null && leadershipDetails.getConstructiveCriticism() != null
					&& leadershipDetails.getDirectlyWorking() != null && leadershipDetails.getExpectationsExample() != null && leadershipDetails.getHelper() != null && leadershipDetails.getInitiative() != null
					&& leadershipDetails.getInnovationAndResearch() != null && leadershipDetails.getLeader() != null && leadershipDetails.getReasonDetails() != null && leadershipDetails.getTeamWorker() != null
					&& leadershipDetails.getTravelOnsite() != null){
				
			System.out.println("in if of eligible");
			
			leadership.setAcceptance(leadershipDetails.getAcceptance());
			leadership.setComments(leadershipDetails.getComments());
			leadership.setCommunicate(leadershipDetails.getCommunicate());
			leadership.setConstructiveCriticism(leadershipDetails.getConstructiveCriticism());
			leadership.setCreatedDate(new Date());
			leadership.setDirectlyWorking(leadershipDetails.getDirectlyWorking());
			leadership.setEmployee(dao.findBy(Employee.class, leadershipDetails.getEmployeeId()));
			leadership.setExpectationsExample(leadershipDetails.getExpectationsExample());
			leadership.setHelper(leadershipDetails.getHelper());
			leadership.setInitiative(leadershipDetails.getInitiative());
			leadership.setInnovationAndResearch(leadershipDetails.getInnovationAndResearch());
			leadership.setLeader(leadershipDetails.getLeader());
			leadership.setReasonDetails(leadershipDetails.getReasonDetails());
			leadership.setStatus("NEW");
			leadership.setTeamWorker(leadershipDetails.getTeamWorker());
			leadership.setTravelOnsite(leadershipDetails.getTravelOnsite());
			achievementDaoImpl.save(leadership);
			LeadershipAudit audit = new LeadershipAudit();
			audit = achievementBuilder.convertLeadershipDtoToLeadershipAudit(leadership);
			achievementDaoImpl.save(audit);
			System.out.println("Leadership added successfully");
		}else{
				System.out.println("Not enough details");
				throw new NotEnoughLeavesAvaialableException("Sorry! Not enough Details!!");
			 }
		}

	}
	public List<LeadershipDTO> getLeadershipList(String from,String to,String dateSelection,String statusSelection) {
		Date fromDate= null;
		Date toDate= null;
		
		  if(dateSelection.equalsIgnoreCase("Custom")) 
		  {
			  System.out.println("in custom");
			  try { 
				  fromDate = DateParser.toDate(from); 
				  toDate   = DateParser.toDate(to);
				   }
		  catch(ParseException e)
		  { 
			  e.printStackTrace();
		  }
		   
		  } 
	    else { 
	    	
	    	Map<String, Date> dateMap = dao.getCustomDates(dateSelection);
	    	fromDate = dateMap.get("startDate"); 
	    	toDate= dateMap.get("endDate");
	    }
		 		 
		 List<Leadership> map = achievementDaoImpl.getlist(fromDate,toDate,dateSelection,statusSelection);
		 List<LeadershipDTO> listdto = new ArrayList<LeadershipDTO>();
		 if(!map.isEmpty()) {
		 for(Leadership dto : map) {
			 
			 LeadershipDTO leadership = new LeadershipDTO();
			 
				leadership.setAcceptance(dto.getAcceptance());
				leadership.setComments(dto.getComments());
				leadership.setCommunicate(dto.getCommunicate());
				leadership.setConstructiveCriticism(dto.getConstructiveCriticism());
				leadership.setCreatedDate((dto.getCreatedDate().toString()));
				leadership.setDirectlyWorking(dto.getDirectlyWorking());
				Employee emp = dao.findBy(Employee.class, dto.getEmployee().getEmployeeId());
				leadership.setEmployeeId(emp.getEmployeeId());
				leadership.setDesignation(emp.getDesignation());
				leadership.setEmployeeName(emp.getEmployeeFullName());
		        leadership.setExpectationsExample(dto.getExpectationsExample());
				leadership.setHelper(dto.getHelper());
				leadership.setInitiative(dto.getInitiative());
				leadership.setInnovationAndResearch(dto.getInnovationAndResearch());
				leadership.setLeader(dto.getLeader());
				leadership.setReasonDetails(dto.getReasonDetails());
				leadership.setStatus(dto.getStatus());
				leadership.setTeamWorker(dto.getTeamWorker());
				leadership.setTravelOnsite(dto.getTravelOnsite());
				listdto.add(leadership);

			 
		 }
		
	}
		 return listdto;
	}
	
	@Override
	public LeadershipDTO getLeaderDetails(Long id) {
		
		Leadership details = achievementDaoImpl.getLeadershipDetails(id);
		
		 LeadershipDTO leadership = new LeadershipDTO();
		 
			leadership.setAcceptance(details.getAcceptance());
			leadership.setComments(details.getComments());
			leadership.setCommunicate(details.getCommunicate());
			leadership.setConstructiveCriticism(details.getConstructiveCriticism());
			leadership.setCreatedDate((details.getCreatedDate().toString()));
			leadership.setDirectlyWorking(details.getDirectlyWorking());
			Employee emp = dao.findBy(Employee.class, details.getEmployee().getEmployeeId());
			leadership.setEmployeeId(emp.getEmployeeId());
			leadership.setDesignation(emp.getDesignation());
			leadership.setEmployeeName(emp.getEmployeeFullName());
	        leadership.setExpectationsExample(details.getExpectationsExample());
			leadership.setHelper(details.getHelper());
			leadership.setInitiative(details.getInitiative());
			leadership.setInnovationAndResearch(details.getInnovationAndResearch());
			leadership.setLeader(details.getLeader());
			leadership.setReasonDetails(details.getReasonDetails());
			leadership.setStatus(details.getStatus());
			leadership.setTeamWorker(details.getTeamWorker());
			leadership.setTravelOnsite(details.getTravelOnsite());
			
		
		return leadership;
	}

	@Override
	public void leadershipApprove(Long id,String comments) {
		Leadership details = achievementDaoImpl.getLeadershipDetails(id);
		
		details.setComments(comments);
		details.setStatus("APPROVED");	
		details.setUpdatedDate(new Date());
        
        
        achievementDaoImpl.update(details);
        LeadershipAudit audit = new LeadershipAudit();
		audit = achievementBuilder.convertLeadershipDtoToLeadershipAudit(details);
		achievementDaoImpl.save(audit);
        }

	@Override
	public void leadershipReject(Long id, String comments) {
		Leadership details = achievementDaoImpl.getLeadershipDetails(id);
			
		 	details.setUpdatedDate(new Date());
			details.setComments(comments);
			details.setStatus("REJECTED");	
	        
	        
	        achievementDaoImpl.update(details);
	        LeadershipAudit audit = new LeadershipAudit();
			audit = achievementBuilder.convertLeadershipDtoToLeadershipAudit(details);
			achievementDaoImpl.save(audit);
	        
		
	}
	
	
}

	
	
	
	

