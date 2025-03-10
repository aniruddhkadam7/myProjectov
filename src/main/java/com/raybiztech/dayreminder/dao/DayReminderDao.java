package com.raybiztech.dayreminder.dao;

import java.util.List;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.recruitment.business.CandidateInterviewCycle;

public interface DayReminderDao extends DAO {
	
	public List<CandidateInterviewCycle> getWhatsappNumbersofCandidate();
	
	public List<CandidateInterviewCycle> getWhatsappNumbersofCandidateforTomorrow();

}
