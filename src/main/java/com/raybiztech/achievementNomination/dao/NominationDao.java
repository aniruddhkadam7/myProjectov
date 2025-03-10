package com.raybiztech.achievementNomination.dao;

import java.util.List;
import java.util.Map;

import com.raybiztech.achievementNomination.business.InitiateNomination;
import com.raybiztech.achievementNomination.business.Nomination;
import com.raybiztech.achievementNomination.business.NominationCycle;
import com.raybiztech.achievementNomination.dto.NominationCycleDto;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.date.Date;

/**
 *
 * @author Aprajita
 */

public interface NominationDao extends DAO {

	/* To get all question List */
	Map<String, Object> getAllQuestions();

	/* To get all cycle List */
	Map<String, Object> getAllCycles();

	/* To get active cycle List */
	NominationCycle getActiveCycle();

	// List<InitiateNomination> initiationDetails(Long nominationCycles);
	InitiateNomination isMappingExit(Long id);

	/* get All question under active cycle */
	InitiateNomination getAllQuestionUnderCycle(Long activeCycleId);

	InitiateNomination getNominationFormDetailsOfActiveCycle(
			NominationCycle activeCycle);

	Boolean checkNomineeAlreadyAdded(NominationCycle nominationCycle,
			Employee employee);

	List<Nomination> getNominations(NominationCycle cycle);

	List<Nomination> getHierarcalNomintions(NominationCycle cycle,
			List<Long> managersList);

	List<Nomination> getDuplicateDate(Date startDate, Date endDate, Long cycleId);
}
