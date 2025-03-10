package com.raybiztech.achievement.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.achievement.dto.AchievementDTO;
import com.raybiztech.achievement.dto.AchievementTypeDTO;
import com.raybiztech.achievement.dto.LeadershipDTO;
import com.raybiztech.achievement.exceptions.NotEnoughLeavesAvaialableException;
import com.raybiztech.achievement.service.AchievementService;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.observation.exceptions.DuplicateObservationException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;

/**
 *
 * @author Aprajita
 */
@Controller
@RequestMapping("/achievement")
public class AchievementController {

	Logger logger = Logger.getLogger(AchievementController.class);

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	AchievementService achievementServiceImpl;

	/* To add achievement type */
	@RequestMapping(value = "/addAchievementType", method = RequestMethod.POST)
	public @ResponseBody void addAchievementType(
			@RequestBody AchievementTypeDTO achievementTypeDto) {
		achievementServiceImpl.addAchievementType(achievementTypeDto);
	}

	/* To get all Achievement type List */
	@RequestMapping(value = "/getAllAchievementType", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllAchievementType() {
		return achievementServiceImpl.getAllAchievementType();
	}

	/* To Update achievement type name */
	@RequestMapping(value = "/updateAchievementType", method = RequestMethod.PUT)
	public @ResponseBody void updateAchievementType(
			@RequestBody AchievementTypeDTO achievementTypeDTO) {
		achievementServiceImpl.updateAchievementType(achievementTypeDTO);
	}

	/* To delete achievement type */
	@RequestMapping(value = "/deleteAchievementType", params = { "typeId" }, method = RequestMethod.DELETE)
	public @ResponseBody void deleteAchievementType(
			@RequestParam("typeId") Long typeId) {
		achievementServiceImpl.deleteAchievementType(typeId);
	}

	/* To add achievement */
	@RequestMapping(value = "/addAchievement", method = RequestMethod.POST)
	public @ResponseBody Long addAchievement(
			@RequestBody AchievementDTO achievementDto, HttpServletResponse httpServletResponse) {
		Long id = null;
		try {
			id = achievementServiceImpl.addAchievement(achievementDto);
		} catch (DuplicateObservationException ex) {
			httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);
		}
	return id;
		
	}

	/* To get all Achievement List */
	@RequestMapping(value = "/getAllAchievement", params = { "startIndex",
			"endIndex", "achievementTypeId", "dateSelection", "fromMonth",
			"fromYear", "toMonth", "toYear" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAllAchievement(
			@RequestParam Integer startIndex, @RequestParam Integer endIndex,
			@RequestParam Long achievementTypeId,
			@RequestParam String dateSelection,
			@RequestParam Integer fromMonth, @RequestParam Integer fromYear,
			@RequestParam Integer toMonth, @RequestParam Integer toYear) {

		return achievementServiceImpl.getAllAchievement(startIndex, endIndex,
				achievementTypeId, dateSelection, fromMonth, fromYear, toMonth,
				toYear);
	}
	
	/*To show star of the Month award in dash board */
	@RequestMapping(value = "/starOfTheMonth", method = RequestMethod.GET)
	public @ResponseBody List<AchievementDTO> showStarOfTheMonth(){
		return achievementServiceImpl.showStarOfTheMonth();
	}
	
	/* To show Associate Award in dash board */
	@RequestMapping(value = "/showAssociateOfQuarter", method = RequestMethod.GET)
	public @ResponseBody List<AchievementDTO> showAssociateOfQuarter() {
		return achievementServiceImpl.showAssociateOfQuarter();
	}

	/* To show Services Awards in dash board */
	@RequestMapping(value = "/showServiceAward", method = RequestMethod.GET)
	public @ResponseBody List<AchievementDTO> showServiceAward() {
		return achievementServiceImpl.showServiceAward();
	}

	/* Toggle button show for dash board */
	@RequestMapping(value = "/showOnDashBoard", params = { "achievementId",
			"dashBoardStatus" }, method = RequestMethod.POST)
	public @ResponseBody void showOnDashBoard(
			@RequestParam("achievementId") Long achievementId,
			String dashBoardStatus, HttpServletResponse httpServletResponse) {
		achievementServiceImpl.showOnDashBoard(achievementId, dashBoardStatus);
	}

	/* To get employee profile image on selection of their name */
	@RequestMapping(value = "/getImageData", params = { "id" }, method = RequestMethod.GET)
	public @ResponseBody EmployeeDTO getImageData(@RequestParam("id") Long id) {
		return achievementServiceImpl.getImageData(id);
	}

	/* To get achievement history */
	@RequestMapping(value = "/getAchievementHistory", params = {
			"achievementId", "filterName" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getAchievementHistory(
			@RequestParam Long achievementId, @RequestParam String filterName,
			HttpServletResponse httpServletResponse) {
		return achievementServiceImpl.getAchievementHistory(achievementId, filterName);
	}
	
	@RequestMapping(value="/getAllAchievementList",method=RequestMethod.GET)
	public @ResponseBody Map<String,List<AchievementDTO>> getAllAchievementList()
	{
		return achievementServiceImpl.getAllAchievementList();
	}
	
	@RequestMapping(value="/getAchievementTypeDetails", params = {"typeId"}, method = RequestMethod.GET)
	public @ResponseBody AchievementTypeDTO getAchievementTypeDetails(Long typeId) {
		return achievementServiceImpl.getAchievementTypeDetails(typeId);
	}
	
	// This is for getting  employee information
		@RequestMapping(value = "/getEmployeeDetails", method = RequestMethod.GET)
		public @ResponseBody Map<String, Object> getEmployeeDetails() {
			return achievementServiceImpl.getEmployeeDetails();
		}
		
		@RequestMapping(value = "/addLeadership", method = RequestMethod.POST)
		public @ResponseBody void addLeadership(@RequestBody LeadershipDTO leadershipDetailsdto,HttpServletResponse httpServletResponse) {
			//logger.warn("in backend controller");
			System.out.println("in Add Leadership confroller "+leadershipDetailsdto);
			try{
				achievementServiceImpl.addLeadership(leadershipDetailsdto);
			}
			catch (NotEnoughLeavesAvaialableException ex) {
				System.out.println("In else block");
				logger.info("Sorry! Your form is already submitted!!");
				httpServletResponse
						.setStatus(HttpServletResponse.SC_PAYMENT_REQUIRED);
			}
		}

@RequestMapping(value="/getLeadershipList", params= {"from","to","dateSelection","statusSelection"}, method = RequestMethod.GET)
		public @ResponseBody  List<LeadershipDTO>  getLeadershipList(@RequestParam String from,@RequestParam String to ,@RequestParam String 
				dateSelection, @RequestParam String statusSelection,
				HttpServletResponse httpServletResponse ) {
			return achievementServiceImpl.getLeadershipList(from,to,dateSelection,statusSelection);
		}

   @RequestMapping(value="/getLeaderDetails",params= {"id"},  method = RequestMethod.GET)
     public @ResponseBody  LeadershipDTO  getLeaderDetails(@RequestParam Long id,
	     	HttpServletResponse httpServletResponse ) {
	    return achievementServiceImpl.getLeaderDetails(id);
}
     @RequestMapping(value="/leadershipApprove",params= {"id","comments"},  method = RequestMethod.PUT)
          public @ResponseBody  void  leadershipApprove(@RequestParam Long id,String comments,
	      	  HttpServletResponse httpServletResponse ) {
	        achievementServiceImpl.leadershipApprove(id,comments);
}
    
     @RequestMapping(value="/leadershipReject",params= {"id","comments"},  method = RequestMethod.PUT)
     public @ResponseBody  void  leadershipReject(@RequestParam Long id,String comments,
     	  HttpServletResponse httpServletResponse ) {
       achievementServiceImpl.leadershipReject(id,comments);
}
}
