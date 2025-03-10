package com.raybiztech.employeeprofile.dto;

import java.util.List;

import com.raybiztech.appraisals.certification.Dto.CertificationDto;
import com.raybiztech.appraisals.dto.SkillDTO;
import com.raybiztech.appraisals.dto.VisaDetailDTO;

public class EmployeeSkillDTO {
	
	private Long id;
	private String empName;
	private Long employeeId;
	private List<SkillDTO> skilldtos;
	private List<VisaDetailDTO> visaDetailsDtos;
	private List<CertificationDto> certificationDtos;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public Long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}
	public List<SkillDTO> getSkilldtos() {
		return skilldtos;
	}
	public void setSkilldtos(List<SkillDTO> skilldtos) {
		this.skilldtos = skilldtos;
	}
	public List<VisaDetailDTO> getVisaDetailsDtos() {
		return visaDetailsDtos;
	}
	public void setVisaDetailsDtos(List<VisaDetailDTO> visaDetailsDtos) {
		this.visaDetailsDtos = visaDetailsDtos;
	}
	public List<CertificationDto> getCertificationDtos() {
		return certificationDtos;
	}
	public void setCertificationDtos(List<CertificationDto> certificationDtos) {
		this.certificationDtos = certificationDtos;
	}
	
	
	

}
