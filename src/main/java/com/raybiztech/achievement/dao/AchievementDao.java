package com.raybiztech.achievement.dao;

import java.util.List;
import java.util.Map;

import com.raybiztech.achievement.business.Achievement;
import com.raybiztech.achievement.business.AchievementType;
import com.raybiztech.achievement.business.Leadership;
import com.raybiztech.achievement.dto.AchievementDTO;
import com.raybiztech.achievement.dto.AchievementTypeDTO;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.date.Date;
import com.raybiztech.date.DateRange;

/**
 *
 * @author Aprajita
 */

public interface AchievementDao extends DAO {

	/* To get all Achievement type List */
	Map<String, Object> getAllAchievementType();
	
	/* To get all Achievement List */
	Map<String, Object> getAllAchievement(Integer startIndex, Integer endIndex,
			Long achievementTypeId, DateRange dateRange);
	
	/* To show Associate Award in dash board */
	public List<Achievement> getAchieversOnDashBoard();
	
	/* To show Services Awards in dash board */
	public List<Achievement> getServiceOnDashBoard();
	
	/* To show Star Of The Month Award in dash board */
	public List<Achievement> showStarOfTheMonth();

	List<Achievement> getAllAchievementList();
	
	AchievementType getAchievementTypeDetails(Long id);
	
	public Map<String,Date> getCustomDates(String dateSelection);

	 List<Leadership> getlist(Date fromDate , Date toDate, String dateSelection,String statusSelection);

	Leadership getLeadershipDetails(Long id);

}
