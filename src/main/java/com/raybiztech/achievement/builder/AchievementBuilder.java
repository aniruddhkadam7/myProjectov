package com.raybiztech.achievement.builder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.achievement.business.Achievement;
import com.raybiztech.achievement.business.AchievementType;
import com.raybiztech.achievement.business.Leadership;
import com.raybiztech.achievement.business.LeadershipAudit;
import com.raybiztech.achievement.dto.AchievementDTO;
import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.properties.PropBean;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.recruitment.utils.FileUploaderUtility;

@Component("achievementBuilder")
public class AchievementBuilder {

	Logger logger = Logger.getLogger(AchievementBuilder.class);

	@Autowired
	EmployeeBuilder employeeBuilder;
	@Autowired
	SecurityUtils securityUtils;
	@Autowired
	DAO dao;
	@Autowired
	FileUploaderUtility fileUploaderUtility;
	@Autowired
	PropBean propBean;

	public Achievement convertAchievementDToToAchievementEntity(
			AchievementDTO achievementDTO) {

		Achievement achievement = null;

		if (achievementDTO != null) {
			achievement = new Achievement();
			achievement.setAchievementType(dao.findBy(AchievementType.class,
					achievementDTO.getAchievementTypeId()));

			Employee employee = dao.findBy(Employee.class,
					achievementDTO.getEmployeeId());

			achievement.setEmployee(employee);
			achievement.setTimePeriod(achievementDTO.getTimePeriod());
			try {
				achievement.setStartDate(Date.parse(
						achievementDTO.getStartDate(), "MM/yyyy"));
				achievement.setEndDate(Date.parse(achievementDTO.getEndDate(),
						"MM/yyyy"));
			} catch (ParseException pe) {
				pe.printStackTrace();
			}

			String imageData = (achievementDTO.getCroppedImageData() != null)?achievementDTO.getCroppedImageData():null;
			String path = null;
			String thumbNailPath = null;

			try {
				
				if(imageData != null) {
					path = uploadImage(imageData, employee);
				}
				else {
					path = employee.getProfilePicPath();
				}

				thumbNailPath = fileUploaderUtility.generateThumnail(new File(
						path), "achiever" + employee.getEmployeeId());
			} catch (IOException e) {
				e.printStackTrace();
			}
			achievement.setProfilePicture(path);
			achievement.setThumbPicture(thumbNailPath);
			achievement.setDescription(achievementDTO.getDescription());
			achievement.setShowOnDashBoard(Boolean.FALSE);
			achievement.setCreatedBy(securityUtils.getLoggedEmployeeIdforSecurityContextHolder());
			achievement.setCreatedDate(new Second());
		}

		return achievement;
	}

	public String uploadImage(String imageData, Employee employee)
			throws IOException {

		int dot1 = imageData.indexOf(",");
		String substr = imageData.substring(dot1 + 1, imageData.length());

		byte[] bytes = Base64.decodeBase64(substr);
		String filePath = propBean.getPropData().get("achieversPics")
				.toString();
		String fileName = "acheiver" + employee.getEmployeeId() + new Second()
				+ ".jpeg";

		logger.warn("file path and name" + filePath + fileName);
		File someFile = new File(filePath + fileName);
		FileOutputStream fos = new FileOutputStream(someFile);
		fos.write(bytes);
		return someFile.getAbsolutePath();

	}

	public AchievementDTO convertAchievementEntityToAchievementDTO(
			Achievement achievement) {
		Employee createdBy = null, updatedBy = null;

		createdBy = achievement.getCreatedBy() != null ? dao.findBy(
				Employee.class, achievement.getCreatedBy()) : null;

		updatedBy = achievement.getUpdatedBy() != null ? dao.findBy(
				Employee.class, achievement.getUpdatedBy()) : null;

		AchievementDTO achievementDTO = null;
		if (achievement != null) {
			achievementDTO = new AchievementDTO();
			achievementDTO.setId(achievement.getId());
			achievementDTO.setAchievementTypeId(achievement
					.getAchievementType().getId());
			achievementDTO.setAchievementType(achievement.getAchievementType()
					.getAchievementType());
			Employee employee = achievement.getEmployee();
			achievementDTO.setEmployeeId(employee.getEmployeeId());
			achievementDTO.setEmployeeName(employee.getEmployeeFullName());
			achievementDTO
					.setStartDate(achievement.getStartDate() != null ? achievement
							.getStartDate().toString("MMM-yyyy") : null);
			achievementDTO
					.setEndDate(achievement.getEndDate() != null ? achievement
							.getEndDate().toString("MMM-yyyy") : null);
			achievementDTO.setDescription(achievement.getDescription());
			achievementDTO.setTimePeriod(achievement.getTimePeriod());

			String string = achievement.getProfilePicture();
			if (string != null) {
				String editedString = "/achieverprofilepics"
						+ string.substring(string.lastIndexOf("/"),
								string.length());
				achievementDTO.setProfilePicture(editedString);
			} else {
				String empString = employee.getThumbPicture();
				String empEditedString = "/profilepics"
						+ empString.substring(empString.lastIndexOf("/"),
								empString.length());
				achievementDTO.setProfilePicture(empEditedString);
			}
			achievementDTO.setThumbPicture(achievement.getThumbPicture());
			achievementDTO.setShowOnDashBoard(achievement.getShowOnDashBoard());
			achievementDTO.setCreatedBy(createdBy != null ? createdBy
					.getEmployeeFullName() : "");
			achievementDTO.setCreatedDate(achievement.getCreatedDate()
					.toString("dd/MM/yyyy"));
			achievementDTO.setUpdatedBy(updatedBy != null ? updatedBy
					.getEmployeeFullName() : "");
			achievementDTO
					.setUpdatedDate(achievement.getUpdatedDate() != null ? achievement
							.getUpdatedDate().toString("dd/MM/YYYY") : null);
		}
		return achievementDTO;
	}

	public List<AchievementDTO> toDTOList(List<Achievement> achievements) {
		List<AchievementDTO> doTOList = null;
		if (achievements != null) {
			doTOList = new ArrayList<AchievementDTO>();
			for (Achievement achievement : achievements) {
				doTOList.add(convertAchievementEntityToAchievementDTO(achievement));
			}
		}
		return doTOList;
	}

	public Map<String, List<AchievementDTO>> convertAllachievementList(
			List<Achievement> achevementList) {
		
		Map<String,List<AchievementDTO>> achievementMap = new LinkedHashMap<String, List<AchievementDTO>>();
		
		AchievementDTO achievementDTO = null;
		List<AchievementDTO> achievemnetDTOList = new ArrayList<AchievementDTO>();
		
		for(Achievement achievement : achevementList)
		{
			achievementDTO = new AchievementDTO();
			
			achievementDTO.setId(achievement.getId());
			achievementDTO.setAchievementTypeId(achievement
					.getAchievementType().getId());
			achievementDTO.setAchievementType(achievement.getAchievementType()
					.getAchievementType());
			Employee employee = achievement.getEmployee();
			achievementDTO.setEmployeeId(employee.getEmployeeId());
			achievementDTO.setEmployeeName(employee.getEmployeeFullName());
			achievementDTO
					.setStartDate(achievement.getStartDate() != null ? achievement
							.getStartDate().toString("MMM-yyyy") : null);
			achievementDTO
					.setEndDate(achievement.getEndDate() != null ? achievement
							.getEndDate().toString("MMM-yyyy") : null);
			achievementDTO.setDescription(achievement.getDescription());
			achievementDTO.setTimePeriod(achievement.getTimePeriod());

			String string = achievement.getProfilePicture();
			if (string != null) {
				String editedString = "/achieverprofilepics"
						+ string.substring(string.lastIndexOf("/"),
								string.length());
				achievementDTO.setProfilePicture(editedString);
			} else {
				String empString = employee.getThumbPicture();
				String empEditedString = "/profilepics"
						+ empString.substring(empString.lastIndexOf("/"),
								empString.length());
				achievementDTO.setProfilePicture(empEditedString);
			}
			achievementDTO.setThumbPicture(achievement.getThumbPicture());
			achievementDTO.setShowOnDashBoard(achievement.getShowOnDashBoard());
			achievementDTO.setTimePeriodRequired(achievement.getAchievementType().getTimePeriodRequired());
			achievementDTO.setDateRequired(achievement.getAchievementType().getFromMonthToMontRequired());
			
			if(achievementDTO.getStartDate()!=null && achievementDTO.getEndDate()!=null)
			{
				if(achievementDTO.getStartDate().equalsIgnoreCase(achievementDTO.getEndDate()))
				{
				
					achievementDTO.setEndDate(null);
				}
			}
			
			
			
			if(achievementMap.containsKey(achievementDTO.getAchievementType()))
			{
				System.out.println(achievementDTO.getId());
				
				List<AchievementDTO> achievementDToList = achievementMap.get(achievementDTO.getAchievementType());
				if(achievementDToList.size()<=3)
				{
					achievementDToList.add(achievementDTO);
					achievementMap.put(achievementDTO.getAchievementType(), achievementDToList);
				}
				
			}
			else
			{
				
				List<AchievementDTO> achievementDTOs2 = new ArrayList<AchievementDTO>();
				achievementDTOs2.add(achievementDTO);
				achievementMap.put(achievementDTO.getAchievementType(), achievementDTOs2);
			}
			
			
			
			
		}
		
		

		return achievementMap;
	}

	public LeadershipAudit convertLeadershipDtoToLeadershipAudit(Leadership leadership) {
		
		LeadershipAudit audit = new LeadershipAudit();
		
		//audit.setAcceptance(leadership.getAcceptance());
		audit.setComments(leadership.getComments());
		audit.setCommunicate(leadership.getConstructiveCriticism());
		audit.setConstructiveCriticism(leadership.getConstructiveCriticism());
		audit.setCreatedDate(new Date());
		audit.setDirectlyWorking(leadership.getDirectlyWorking());
		audit.setEmployee(leadership.getEmployee());
		audit.setExpectationsExample(leadership.getExpectationsExample());
		audit.setHelper(leadership.getHelper());
		audit.setInitiative(leadership.getInitiative());
		audit.setInnovationAndResearch(leadership.getInnovationAndResearch());
		audit.setLeader(leadership.getLeader());
		audit.setReasonDetails(leadership.getReasonDetails());
		audit.setStatus(leadership.getStatus());
		audit.setTeamWorker(leadership.getTeamWorker());
		audit.setTravelOnsite(leadership.getTravelOnsite());
		audit.setUpdatedDate(leadership.getUpdatedDate());
		
		return audit;
		
		
	}

}
