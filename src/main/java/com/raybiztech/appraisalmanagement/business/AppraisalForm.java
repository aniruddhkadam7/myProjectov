package com.raybiztech.appraisalmanagement.business;

import java.io.Serializable;
import java.util.Set;

import org.apache.log4j.Logger;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import java.util.Objects;

public class AppraisalForm implements Serializable, Cloneable {

	/**
     *
     */
	private static final long serialVersionUID = 9136155345095212920L;
	private Long id;
	private AppraisalCycle appraisalCycle;
	private Set<AppraisalKRAData> kra;
	private Employee employee;
	private FormStatus formStatus;
	public String managesList;
	public Set<AppraisalFormAvgRatings> formAvgRatings;
	public Long finalRAting;
	public Date discussionOn;
	// public String discussionSummary;
	private String closedSummary;
	private Date closedOn;
	private String closedStatus;
	private Long closedBy;
	private Long createdBy;
	private Long updatedBy;
	private Boolean requestDiscussion;
	private Second createdDate;
	private Second updatedDate;

	Logger logger = Logger.getLogger(AppraisalForm.class);

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public AppraisalCycle getAppraisalCycle() {
		return appraisalCycle;
	}

	public void setAppraisalCycle(AppraisalCycle appraisalCycle) {
		this.appraisalCycle = appraisalCycle;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Set<AppraisalKRAData> getKra() {
		return kra;
	}

	public void setKra(Set<AppraisalKRAData> kra) {
		this.kra = kra;
	}

	public FormStatus getFormStatus() {
		return formStatus;
	}

	public void setFormStatus(FormStatus formStatus) {
		this.formStatus = formStatus;
	}

	public String getManagesList() {
		return managesList;
	}

	public void setManagesList(String managesList) {
		this.managesList = managesList;
	}

	// public Double getEmployeesumOfKraRatingMeanvalue() {
	// Double sum = 0.0;
	// Double meanvalue;
	// for (AppraisalKRAData akrad : kra) {
	// sum = sum + akrad.getEmployeesumOfKpiRatingMeanvalue();
	// }
	// meanvalue = sum / kra.size();
	// return meanvalue;
	// }
	//
	// public Double getManagersumOfKraRatingMeanvalue() {
	// Double sum = 0.0;
	// Double meanvalue;
	// for (AppraisalKRAData akrad : kra) {
	// sum = sum + akrad.getManagersumOfKpiRatingMeanvalue();
	// }
	// meanvalue = sum / kra.size();
	// return meanvalue;
	// }
	// @Override
	// public int hashCode() {
	// return new HashCodeBuilder(1989, 97).append(getId()).hashCode();
	// }
	//
	// @Override
	// public boolean equals(Object obj) {
	// if (obj == null) {
	// return false;
	// }
	// if (getClass() != obj.getClass()) {
	// return false;
	// }
	// final AppraisalForm other = (AppraisalForm) obj;
	// return new EqualsBuilder().append(getId(), other.getId()).isEquals();
	// }

	// public Map<String, Float> getFormAverageRating() {
	// Float empFormRating = 0.0f;
	// Float mngrFormRating = 0.0f;
	// Map<String, Float> mapFormRating = new HashMap<String, Float>();
	// Integer noOfKra = getKra().size();
	// Integer noOfKra2 = getKra().size();
	// for (AppraisalKRAData appraisalKRAData : getKra()) {
	//
	// Float emprating=1.0f;
	// Float mngrrating=1.0f;
	// // Float emprating = appraisalKRAData.getKRAAverageRating().get(
	// // "emprating");
	// // Float mngrrating = appraisalKRAData.getKRAAverageRating().get(
	// // "mngrrating");
	// if (emprating == 0.0) {
	// noOfKra--;
	// }
	// if (mngrrating == 0.0) {
	//
	// noOfKra2--;
	// }
	// empFormRating = empFormRating + emprating;
	// mngrFormRating = mngrFormRating + mngrrating;
	// }
	//
	// mapFormRating.put("empFromavgrtng", round(empFormRating / noOfKra));
	// if (noOfKra2 != 0.0) {
	// mapFormRating.put("mgrFromavgrtng",
	// round(mngrFormRating / noOfKra2));
	// }
	// return mapFormRating;
	// }
	//
	// public float round(float d) {
	// BigDecimal bd = null;
	//
	// String s = String.valueOf(d);
	// if (!s.equalsIgnoreCase("NaN")) {
	// bd = new BigDecimal(Float.toString(d));
	// } else {
	// bd = new BigDecimal(Float.toString(0.00F));
	// }
	// bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
	// return bd.floatValue();
	// }
	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	public Set<AppraisalFormAvgRatings> getFormAvgRatings() {
		return formAvgRatings;
	}

	public void setFormAvgRatings(Set<AppraisalFormAvgRatings> formAvgRatings) {
		this.formAvgRatings = formAvgRatings;
	}

	public Long getFinalRAting() {
		return finalRAting;
	}

	public void setFinalRAting(Long finalRAting) {
		this.finalRAting = finalRAting;
	}

	public Date getDiscussionOn() {
		return discussionOn;
	}

	public void setDiscussionOn(Date discussionOn) {
		this.discussionOn = discussionOn;
	}

	// public String getDiscussionSummary() {
	// return discussionSummary;
	// }
	//
	// public void setDiscussionSummary(String discussionSummary) {
	// this.discussionSummary = discussionSummary;
	// }
	public String getClosedSummary() {
		return closedSummary;
	}

	public void setClosedSummary(String closedSummary) {
		this.closedSummary = closedSummary;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 59 * hash + Objects.hashCode(this.id);
		hash = 59 * hash + Objects.hashCode(this.appraisalCycle);
		hash = 59 * hash + Objects.hashCode(this.kra);
		hash = 59 * hash + Objects.hashCode(this.employee);
		hash = 59 * hash + Objects.hashCode(this.formStatus);
		hash = 59 * hash + Objects.hashCode(this.managesList);
		hash = 59 * hash + Objects.hashCode(this.formAvgRatings);
		hash = 59 * hash + Objects.hashCode(this.finalRAting);
		hash = 59 * hash + Objects.hashCode(this.discussionOn);
		// hash = 59 * hash + Objects.hashCode(this.discussionSummary);
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final AppraisalForm other = (AppraisalForm) obj;
		if (!Objects.equals(this.managesList, other.managesList)) {
			return false;
		}
		// if (!Objects.equals(this.discussionSummary, other.discussionSummary))
		// {
		// return false;
		// }
		if (!Objects.equals(this.id, other.id)) {
			return false;
		}
		if (!Objects.equals(this.appraisalCycle, other.appraisalCycle)) {
			return false;
		}
		if (!Objects.equals(this.kra, other.kra)) {
			return false;
		}
		if (!Objects.equals(this.employee, other.employee)) {
			return false;
		}
		if (this.formStatus != other.formStatus) {
			return false;
		}
		if (!Objects.equals(this.formAvgRatings, other.formAvgRatings)) {
			return false;
		}
		if (!Objects.equals(this.finalRAting, other.finalRAting)) {
			return false;
		}
		if (!Objects.equals(this.discussionOn, other.discussionOn)) {
			return false;
		}
		return true;
	}

	public Date getClosedOn() {
		return closedOn;
	}

	public void setClosedOn(Date closedOn) {
		this.closedOn = closedOn;
	}

	public String getClosedStatus() {
		return closedStatus;
	}

	public void setClosedStatus(String closedStatus) {
		this.closedStatus = closedStatus;
	}

	public Long getClosedBy() {
		return closedBy;
	}

	public void setClosedBy(Long closedBy) {
		this.closedBy = closedBy;
	}

	public Long getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(Long createdBy) {
		this.createdBy = createdBy;
	}

	public Long getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(Long updatedBy) {
		this.updatedBy = updatedBy;
	}

	

	public Boolean getRequestDiscussion() {
		return requestDiscussion;
	}

	public void setRequestDiscussion(Boolean requestDiscussion) {
		this.requestDiscussion = requestDiscussion;
	}

	public Second getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Second createdDate) {
		this.createdDate = createdDate;
	}

	public Second getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Second updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Logger getLogger() {
		return logger;
	}

	public void setLogger(Logger logger) {
		this.logger = logger;
	}

}
