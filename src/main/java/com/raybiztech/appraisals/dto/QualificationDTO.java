package com.raybiztech.appraisals.dto;

import java.util.List;


/**
 * @author aprajita
 *
 */
public class QualificationDTO {

	private Long id;
	private List<MultipleSelectionDTO> pgLookUp;
	private List<MultipleSelectionDTO> graduationLookUp;
	//private List<MultipleSelectionDTO> hscLookUp;
	private String others;
	private String empName;
	private Long empId;
	private String sscName;
	private String hscName;
	private String createdBy;
	private String createdDate;
	private String updatedBy;
	private String updatedDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<MultipleSelectionDTO> getPgLookUp() {
		return pgLookUp;
	}

	public void setPgLookUp(List<MultipleSelectionDTO> pgLookUp) {
		this.pgLookUp = pgLookUp;
	}

	public List<MultipleSelectionDTO> getGraduationLookUp() {
		return graduationLookUp;
	}

	public void setGraduationLookUp(List<MultipleSelectionDTO> graduationLookUp) {
		this.graduationLookUp = graduationLookUp;
	}
	
	public String getHscName() {
		return hscName;
	}

	public void setHscName(String hscName) {
		this.hscName = hscName;
	}

	/*public List<MultipleSelectionDTO> getHscLookUp() {
		return hscLookUp;
	}

	public void setHscLookUp(List<MultipleSelectionDTO> hscLookUp) {
		this.hscLookUp = hscLookUp;
	}
*/
	public String getSscName() {
		return sscName;
	}

	public void setSscName(String sscName) {
		this.sscName = sscName;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public String getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public String getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(String updatedDate) {
		this.updatedDate = updatedDate;
	}

	public String getEmpName() {
		return empName;
	}

	public void setEmpName(String empName) {
		this.empName = empName;
	}

	public Long getEmpId() {
		return empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public String getOthers() {
		return others;
	}

	public void setOthers(String others) {
		this.others = others;
	}



	
}
