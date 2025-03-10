package com.raybiztech.biometric.business;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;

public class BioAttendance implements Serializable ,Comparable<BioAttendance>{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4468282478761681992L;
	private Long id;
	private Long unusedId;
	private Employee employee;
	private Date attendanceDate;
	private Second inTime;
	private Second outTime;
	private AttendanceStatus attendanceStatus;
	private Boolean lateReport;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getAttendanceDate() {
		return attendanceDate;
	}

	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
	}

	public Second getInTime() {
		return inTime;
	}

	public void setInTime(Second inTime) {
		this.inTime = inTime;
	}

	public Second getOutTime() {
		return outTime;
	}

	public void setOutTime(Second outTime) {
		this.outTime = outTime;
	}

	public Long getUnusedId() {
		return unusedId;
	}

	public void setUnusedId(Long unusedId) {
		this.unusedId = unusedId;
	}

	public AttendanceStatus getAttendanceStatus() {
		return attendanceStatus;
	}

	public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
		this.attendanceStatus = attendanceStatus;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	@Override
	public int compareTo(BioAttendance o) {
		// TODO Auto-generated method stub
		return (int) (this.id-o.id);
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(1989, 55).append(this.attendanceDate).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final BioAttendance other = (BioAttendance) obj;
		return new EqualsBuilder().append(attendanceDate, other.attendanceDate).isEquals();
	}

	public Boolean getLateReport() {
		return lateReport;
	}

	public void setLateReport(Boolean lateReport) {
		this.lateReport = lateReport;
	}


}
