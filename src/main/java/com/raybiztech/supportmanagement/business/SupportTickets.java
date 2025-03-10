package com.raybiztech.supportmanagement.business;

import java.io.Serializable;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

import java.util.Set;

public class SupportTickets implements Serializable,Cloneable {

	/**
	 * @author sravani
	 */
	private static final long serialVersionUID = 5342885720840744811L;

	private Long id;
	public TicketsSubCategory ticketsSubCategory;
	public String subject;
	public String description;
	public String status;
	public String priority;
	public Date startDate;
	public Date endDate;
	public Employee assignee;
	public int percentageDone;
	public String actualTime;
	public Long createdBy;
	public Long updatedBy;
	private Second createdDate;
	private Second updatedDate;
	public String documentsPath;
	public String approvalStatus;
	public String managesList;
	public Tracker tracker;
	public Date accessStartDate;
	public Date accessEndDate;
//        private Set<SupportTicketsWatchers> ticketsWatchers;
        private String ticketWatchers;

	public String getDocumentsPath() {
		return documentsPath;
	}

	public void setDocumentsPath(String documentsPath) {
		this.documentsPath = documentsPath;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TicketsSubCategory getTicketsSubCategory() {
		return ticketsSubCategory;
	}

	public void setTicketsSubCategory(TicketsSubCategory ticketsSubCategory) {
		this.ticketsSubCategory = ticketsSubCategory;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Employee getAssignee() {
		return assignee;
	}

	public void setAssignee(Employee assignee) {
		this.assignee = assignee;
	}

	public int getPercentageDone() {
		return percentageDone;
	}

	public void setPercentageDone(int percentageDone) {
		this.percentageDone = percentageDone;
	}

    public String getActualTime() {
        return actualTime;
    }

    public void setActualTime(String actualTime) {
        this.actualTime = actualTime;
    }

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

//    public Set<SupportTicketsWatchers> getTicketsWatchers() {
//        return ticketsWatchers;
//    }
//
//    public void setTicketsWatchers(Set<SupportTicketsWatchers> ticketsWatchers) {
//        this.ticketsWatchers = ticketsWatchers;
//    }

    public String getTicketWatchers() {
        return ticketWatchers;
    }

    public void setTicketWatchers(String ticketWatchers) {
        this.ticketWatchers = ticketWatchers;
    }

	public Date getAccessStartDate() {
		return accessStartDate;
	}

	public void setAccessStartDate(Date accessStartDate) {
		this.accessStartDate = accessStartDate;
	}

	public Date getAccessEndDate() {
		return accessEndDate;
	}

	public void setAccessEndDate(Date accessEndDate) {
		this.accessEndDate = accessEndDate;
	}

	@Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
    

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actualTime == null) ? 0 : actualTime.hashCode());
		result = prime * result
				+ ((approvalStatus == null) ? 0 : approvalStatus.hashCode());
		result = prime * result
				+ ((assignee == null) ? 0 : assignee.hashCode());
		result = prime * result
				+ ((createdBy == null) ? 0 : createdBy.hashCode());
		result = prime * result
				+ ((createdDate == null) ? 0 : createdDate.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((documentsPath == null) ? 0 : documentsPath.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + percentageDone;
		result = prime * result
				+ ((priority == null) ? 0 : priority.hashCode());
		result = prime * result
				+ ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((subject == null) ? 0 : subject.hashCode());
		result = prime
				* result
				+ ((ticketsSubCategory == null) ? 0 : ticketsSubCategory
						.hashCode());
//		result = prime * result
//				+ ((ticketsWatchers == null) ? 0 : ticketsWatchers.hashCode());
		result = prime * result
				+ ((updatedBy == null) ? 0 : updatedBy.hashCode());
		result = prime * result
				+ ((updatedDate == null) ? 0 : updatedDate.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SupportTickets other = (SupportTickets) obj;
		if (actualTime == null) {
			if (other.actualTime != null)
				return false;
		} else if (!actualTime.equals(other.actualTime))
			return false;
		if (approvalStatus == null) {
			if (other.approvalStatus != null)
				return false;
		} else if (!approvalStatus.equals(other.approvalStatus))
			return false;
		if (assignee == null) {
			if (other.assignee != null)
				return false;
		} else if (!assignee.equals(other.assignee))
			return false;
		if (createdBy == null) {
			if (other.createdBy != null)
				return false;
		} else if (!createdBy.equals(other.createdBy))
			return false;
		if (createdDate == null) {
			if (other.createdDate != null)
				return false;
		} else if (!createdDate.equals(other.createdDate))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (documentsPath == null) {
			if (other.documentsPath != null)
				return false;
		} else if (!documentsPath.equals(other.documentsPath))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (percentageDone != other.percentageDone)
			return false;
		if (priority == null) {
			if (other.priority != null)
				return false;
		} else if (!priority.equals(other.priority))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (subject == null) {
			if (other.subject != null)
				return false;
		} else if (!subject.equals(other.subject))
			return false;
		if (ticketsSubCategory == null) {
			if (other.ticketsSubCategory != null)
				return false;
		} else if (!ticketsSubCategory.equals(other.ticketsSubCategory))
			return false;
//		if (ticketsWatchers == null) {
//			if (other.ticketsWatchers != null)
//				return false;
//		} else if (!ticketsWatchers.equals(other.ticketsWatchers))
//			return false;
		if (updatedBy == null) {
			if (other.updatedBy != null)
				return false;
		} else if (!updatedBy.equals(other.updatedBy))
			return false;
		if (updatedDate == null) {
			if (other.updatedDate != null)
				return false;
		} else if (!updatedDate.equals(other.updatedDate))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "SupportTickets [id=" + id + ", ticketsSubCategory="
				+ ticketsSubCategory + ", subject=" + subject
				+ ", description=" + description + ", status=" + status
				+ ", priority=" + priority + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", assignee=" + assignee
				+ ", percentageDone=" + percentageDone + ", actualTime="
				+ actualTime + ", createdBy=" + createdBy + ", updatedBy="
				+ updatedBy + ", createdDate=" + createdDate + ", updatedDate="
				+ updatedDate + ", documentsPath=" + documentsPath
				+ ", approvalStatus=" + approvalStatus +  "]";
	}

	public String getManagesList() {
		return managesList;
	}

	public void setManagesList(String managesList) {
		this.managesList = managesList;
	}

	public Tracker getTracker() {
		return tracker;
	}

	public void setTracker(Tracker tracker) {
		this.tracker = tracker;
	}
        
        
}
