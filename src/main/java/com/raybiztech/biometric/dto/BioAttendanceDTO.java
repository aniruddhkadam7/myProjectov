package com.raybiztech.biometric.dto;

import java.io.Serializable;

import com.raybiztech.biometric.business.AttendanceStatus;

public class BioAttendanceDTO implements Serializable,
        Comparable<BioAttendanceDTO> {

    /**
     *
     */
    private static final long serialVersionUID = 6044559909006743886L;
    private Long id;
    private String start;
    private String inTime;
    private String outTime;
    private AttendanceStatus attendanceStatus;
    private String color;
    private String rendering;
    private Boolean overlap;
    private String title;
    private String textColor;
    private String className;
    private String timeInOffice;
    private String hiveHours;
    private Boolean lateReport;

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getInTime() {
        return inTime;
    }

    public void setInTime(String inTime) {
        this.inTime = inTime;
    }

    public String getOutTime() {
        return outTime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOutTime(String outTime) {
        this.outTime = outTime;
    }

    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRendering() {
        return rendering;
    }

    public void setRendering(String rendering) {
        this.rendering = rendering;
    }

    public void setAttendanceStatus(AttendanceStatus attendanceStatus) {
        this.attendanceStatus = attendanceStatus;
    }

    public Boolean getOverlap() {
        return overlap;
    }

    public void setOverlap(Boolean overlap) {
        this.overlap = overlap;
    }

    public String getTimeInOffice() {
        return timeInOffice;
    }

    public void setTimeInOffice(String timeInOffice) {
        this.timeInOffice = timeInOffice;
    }

    public String getHiveHours() {
        return hiveHours;
    }

    public void setHiveHours(String hiveHours) {
        this.hiveHours = hiveHours;
    }

   

    @Override
    public int compareTo(BioAttendanceDTO o) {
        // TODO Auto-generated method stub
        return (int) (this.id - o.id);
    }

	public Boolean getLateReport() {
		return lateReport;
	}

	public void setLateReport(Boolean lateReport) {
		this.lateReport = lateReport;
	}
}
