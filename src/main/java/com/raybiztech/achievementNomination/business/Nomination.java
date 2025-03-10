package com.raybiztech.achievementNomination.business;

import java.io.Serializable;
import java.util.Set;

import com.raybiztech.achievement.business.AchievementType;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Second;

/**
 * @author Aprajita
 */
public class Nomination implements Serializable {

	private static final long serialVersionUID = -292733845611232L;

	private Long id;
	private Employee employee;
	private AchievementType achievementType;
	private NominationCycle nominationCycleId;
	private Set<NominationQuestionsData> nominationQuestionsData;
	private Long rating;
	private String finalComments;
	private String nominationStatus;
	private Long createdBy;
	private Second createdDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public AchievementType getAchievementType() {
		return achievementType;
	}

	public void setAchievementType(AchievementType achievementType) {
		this.achievementType = achievementType;
	}

	public NominationCycle getNominationCycleId() {
		return nominationCycleId;
	}

	public void setNominationCycleId(NominationCycle nominationCycleId) {
		this.nominationCycleId = nominationCycleId;
	}

	public Set<NominationQuestionsData> getNominationQuestionsData() {
		return nominationQuestionsData;
	}

	public void setNominationQuestionsData(
			Set<NominationQuestionsData> nominationQuestionsData) {
		this.nominationQuestionsData = nominationQuestionsData;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Second getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}

	public Long getRating() {
		return rating;
	}

	public void setRating(Long rating) {
		this.rating = rating;
	}

	public String getFinalComments() {
		return finalComments;
	}

	public void setFinalComments(String finalComments) {
		this.finalComments = finalComments;
	}

	public String getNominationStatus() {
		return nominationStatus;
	}

	public void setNominationStatus(String nominationStatus) {
		this.nominationStatus = nominationStatus;
	}

}
