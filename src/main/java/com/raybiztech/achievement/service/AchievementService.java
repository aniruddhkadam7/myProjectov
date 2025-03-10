package com.raybiztech.achievement.service;

import java.util.List;
import java.util.Map;

import com.raybiztech.achievement.business.Leadership;
import com.raybiztech.achievement.dto.AchievementDTO;
import com.raybiztech.achievement.dto.AchievementTypeDTO;
import com.raybiztech.achievement.dto.LeadershipDTO;
import com.raybiztech.appraisals.dto.EmployeeDTO;

/**
 *
 * @author Aprajita
 */
public interface AchievementService {

	/* To add achievement type */
	void addAchievementType(AchievementTypeDTO achievementTypeDTO);

	/* To get all Achievement type List */
	Map<String, Object> getAllAchievementType();

	/* To Update achievement type name */
	void updateAchievementType(AchievementTypeDTO achievementTypeDto);

	/* To Delete achievement type name */
	void deleteAchievementType(Long typeId);

	/* To add achievement */
	Long addAchievement(AchievementDTO achievementDTO);

	/* To get all Achievement List */
	Map<String, Object> getAllAchievement(Integer startIndex, Integer endIndex,
			Long achievementTypeId, String dateSelection, Integer fromMonth,
			Integer fromYear, Integer toMonth, Integer toYear);

	void uploadAchievementCroppedImage(String parameter, Long empLong);
	
	/* To show Star Of The month in dash board */
	public List<AchievementDTO> showStarOfTheMonth();

	/* To show Associate Award in dash board */
	public List<AchievementDTO> showAssociateOfQuarter();

	/* To show Services Awards in dash board */
	public List<AchievementDTO> showServiceAward();

	/* Toggle show on dash board */
	void showOnDashBoard(Long achievementId, String dashBoardStatus);

	/*To get employee profile image on selection of their name*/ 
	public EmployeeDTO getImageData(Long empId);
	
	Map<String, Object> getAchievementHistory(Long achievementId, String filterName);

	Map<String, List<AchievementDTO>> getAllAchievementList();
	
	AchievementTypeDTO getAchievementTypeDetails(Long typeId);

	Map<String, Object> getEmployeeDetails();

	void addLeadership(LeadershipDTO leadershipDetails);
	public List<LeadershipDTO> getLeadershipList(String from,String to,String dateSelection,String statusSelection);

	LeadershipDTO getLeaderDetails(Long id);

	void leadershipApprove(Long id,String comments);

	void leadershipReject(Long id,String comments);
	
}
