package com.raybiztech.ticketmanagement.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import java.util.Set;
import org.apache.log4j.Logger;

public class Ticket implements Serializable {

	/**
     *
     */
	Logger logger = Logger.getLogger(Ticket.class);
	private static final long serialVersionUID = -4022436231627684924L;
	private Long TicketNumber;
	private Set<TicketHistory> ticketHistory;
	private Date raisedDate;
	private TicketStatus ticketStatus;
	private MealLookUp mealLookUp;
	// private String ticketAuthorName;
	private Long authorEmpId;
	private Boolean genarateType;
	private String isApproved;

	// public String getTicketAuthorName() {
	// return ticketAuthorName;
	// }
	//
	// public void setTicketAuthorName(String ticketAuthorName) {
	// this.ticketAuthorName = ticketAuthorName;
	// }

	public Long getAuthorEmpId() {
		return authorEmpId;
	}

	public void setAuthorEmpId(Long authorEmpId) {
		this.authorEmpId = authorEmpId;
	}

	public Long getTicketNumber() {
		return TicketNumber;
	}

	public void setTicketNumber(Long ticketNumber) {
		TicketNumber = ticketNumber;
	}

	public Set<TicketHistory> getTicketHistory() {

		// logger.warn("ticket number is"+this.getTicketHistory());
		return ticketHistory;

		// Set<TicketHistory> historys = null;
		// if (this.getTicketHistory() != null) {
		//
		// historys = new HashSet<TicketHistory>();
		// for (TicketHistory histroy : this.getTicketHistory()) {
		//
		// if (milestone.isBillable() && milestone.isClosed() &&
		// !milestone.getInvoiceStatus()) {
		//
		// milestoneSet.add(milestone);
		// }
		// }
		//
		// }
		//
		// return Collections.unmodifiableSet(milestoneSet);

	}

	public void setTicketHistory(Set<TicketHistory> ticketHistory) {
		this.ticketHistory = ticketHistory;
	}

	public TicketStatus getTicketStatus() {
		return ticketStatus;
	}

	public void setTicketStatus(TicketStatus ticketStatus) {
		this.ticketStatus = ticketStatus;
	}

	public MealLookUp getMealLookUp() {
		return mealLookUp;
	}

	public void setMealLookUp(MealLookUp mealLookUp) {
		this.mealLookUp = mealLookUp;
	}

	public Date getRaisedDate() {
		return raisedDate;
	}

	public void setRaisedDate(Date raisedDate) {
		this.raisedDate = raisedDate;
	}

	public Boolean getGenarateType() {
		return genarateType;
	}

	public void setGenarateType(Boolean genarateType) {
		this.genarateType = genarateType;
	}

	public String getIsApproved() {
		return isApproved;
	}

	public void setIsApproved(String isApproved) {
		this.isApproved = isApproved;
	}

}
