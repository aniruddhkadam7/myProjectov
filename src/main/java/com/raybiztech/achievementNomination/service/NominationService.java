package com.raybiztech.achievementNomination.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.output.ByteArrayOutputStream;

import com.raybiztech.achievementNomination.dto.InitiateNominationDto;
import com.raybiztech.achievementNomination.dto.NominationCycleDto;
import com.raybiztech.achievementNomination.dto.NominationDto;
import com.raybiztech.achievementNomination.dto.NominationQuestionDto;

/**
 *
 * @author Aprajita
 */
public interface NominationService {

	/* To add nomination Question */
	void addQuestion(NominationQuestionDto nominationQuestionDto);

	/* To get all question List */
	Map<String, Object> getAllQuestions();

	/* To delete question */
	void deleteQuestion(Long questionId) throws Exception;

	/* To add nomination cycle */
	void addCycle(NominationCycleDto nominationCycleDto);

	/* To get all cycle List */
	Map<String, Object> getAllCycles();

	/* To delete cycle */
	void deleteCycle(Long cycleId);

	NominationCycleDto editCycle(Long cycleId);

	void updateCycle(NominationCycleDto nominationCycleDto);

	/* To get active cycle */
	public InitiateNominationDto getActiveCycleData();

	/* To initiate Question */
	void initiateCycle(InitiateNominationDto initiateCycleDto);

	public NominationDto getFormDetails();

	public void addNominee(NominationDto dto);

	public List<NominationDto> getNominations(Long cycleId);

	public NominationDto getNominationDetails(Long nominationId);

	public void updateNominee(NominationDto dto);

	ByteArrayOutputStream exportNomineesListData( Long cycle)throws IOException;
}
