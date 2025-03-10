package com.raybiztech.separation.dto;

import java.util.List;
import java.util.Set;

import com.raybiztech.appraisals.PIPManagement.dto.PIPAuditDTO;
import com.raybiztech.appraisals.PIPManagement.dto.PIPDTO;

public class SeparationDTO {

	private Long separationId;
	private String relievingDate;
	private String resignationDate;
	private Long employeeId;
	private String employeeName;
	private Set<SeparationCommentsDTO> separationComments;
	private String employeeComments;
	private String managerComments;
	private String withdrawComments;
	private Long primaryReasonId;
	private String primaryReasonName;
	private String reasonComments;
	private String status;
	private Boolean canberevoked;
	private Boolean isRevoked;
	private Boolean isprocessInitiated;
	private String adminCcCss;
	private String hrCcCss;
	private String managerCcCss;
	private String itCcCss;
	private String finanaceCcCss;
	private Boolean showCommentsBox;
	private Boolean showEditButton;
	private Set<ClearanceCertificateDTO> certificateDTO;
	private String relievingLetterPath;
	private String managerName;
	private String exitFeedbackFormPath;
	private Boolean separationExist;
	private Boolean showManagerClearance;
	private Boolean showTimeline;
	private Boolean isPIP;
	private List<PIPAuditDTO> pipAuditDTO;
	/*private String abscondDate;
	private String abscondComments;*/
	//private Boolean isAbsconded;
	private Boolean contractExists;
	private String contractStartDate;
	private String contractEndDate;
	private Boolean personalEmailFlag;
	private String initiatedDate;
	private String empStatus;
	
	
	

	public String getEmpStatus() {
		return empStatus;
	}

	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus;
	}

	public Boolean getIsPIP() {
		return isPIP;
	}

	public void setIsPIP(Boolean isPIP) {
		this.isPIP = isPIP;
	}

	public Set<SeparationCommentsDTO> getSeparationComments() {
		return separationComments;
	}

	public void setSeparationComments(
			Set<SeparationCommentsDTO> separationComments) {
		this.separationComments = separationComments;
	}

	public Boolean getCanberevoked() {
		return canberevoked;
	}

	public void setCanberevoked(Boolean canberevoked) {
		this.canberevoked = canberevoked;
	}

	public Boolean getIsRevoked() {
		return isRevoked;
	}

	public void setIsRevoked(Boolean isRevoked) {
		this.isRevoked = isRevoked;
	}

	private Set<ClearanceCertificateDTO> certificate;

	public Long getSeparationId() {
		return separationId;
	}

	public void setSeparationId(Long separationId) {
		this.separationId = separationId;
	}

	public String getRelievingDate() {
		return relievingDate;
	}

	public void setRelievingDate(String relievingDate) {
		this.relievingDate = relievingDate;
	}

	public String getResignationDate() {
		return resignationDate;
	}

	public void setResignationDate(String resignationDate) {
		this.resignationDate = resignationDate;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Set<SeparationCommentsDTO> getSeperationComments() {
		return separationComments;
	}

	public void setSeperationComments(
			Set<SeparationCommentsDTO> seperationComments) {
		this.separationComments = seperationComments;
	}

	public String getEmployeeComments() {
		return employeeComments;
	}

	public void setEmployeeComments(String employeeComments) {
		this.employeeComments = employeeComments;
	}

	public Long getPrimaryReasonId() {
		return primaryReasonId;
	}

	public void setPrimaryReasonId(Long primaryReasonId) {
		this.primaryReasonId = primaryReasonId;
	}

	public String getPrimaryReasonName() {
		return primaryReasonName;
	}

	public void setPrimaryReasonName(String primaryReasonName) {
		this.primaryReasonName = primaryReasonName;
	}

	public String getReasonComments() {
		return reasonComments;
	}

	public void setReasonComments(String reasonComments) {
		this.reasonComments = reasonComments;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Set<ClearanceCertificateDTO> getCertificate() {
		return certificate;
	}

	public void setCertificate(Set<ClearanceCertificateDTO> certificate) {
		this.certificate = certificate;
	}

	public String getManagerComments() {
		return managerComments;
	}

	public void setManagerComments(String managerComments) {
		this.managerComments = managerComments;
	}

	public String getAdminCcCss() {
		return adminCcCss;
	}

	public void setAdminCcCss(String adminCcCss) {
		this.adminCcCss = adminCcCss;
	}

	public String getHrCcCss() {
		return hrCcCss;
	}

	public void setHrCcCss(String hrCcCss) {
		this.hrCcCss = hrCcCss;
	}

	public String getManagerCcCss() {
		return managerCcCss;
	}

	public void setManagerCcCss(String managerCcCss) {
		this.managerCcCss = managerCcCss;
	}

	public String getItCcCss() {
		return itCcCss;
	}

	public void setItCcCss(String itCcCss) {
		this.itCcCss = itCcCss;
	}

	public String getFinanaceCcCss() {
		return finanaceCcCss;
	}

	public void setFinanaceCcCss(String finanaceCcCss) {
		this.finanaceCcCss = finanaceCcCss;
	}

	public Boolean getIsprocessInitiated() {
		return isprocessInitiated;
	}

	public void setIsprocessInitiated(Boolean isprocessInitiated) {
		this.isprocessInitiated = isprocessInitiated;
	}

	public Set<ClearanceCertificateDTO> getCertificateDTO() {
		return certificateDTO;
	}

	public void setCertificateDTO(Set<ClearanceCertificateDTO> certificateDTO) {
		this.certificateDTO = certificateDTO;
	}

	public String getRelievingLetterPath() {
		return relievingLetterPath;
	}

	public void setRelievingLetterPath(String relievingLetterPath) {
		this.relievingLetterPath = relievingLetterPath;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public Boolean getShowCommentsBox() {
		return showCommentsBox;
	}

	public void setShowCommentsBox(Boolean showCommentsBox) {
		this.showCommentsBox = showCommentsBox;
	}

	public String getExitFeedbackFormPath() {
		return exitFeedbackFormPath;
	}

	public void setExitFeedbackFormPath(String exitFeedbackFormPath) {
		this.exitFeedbackFormPath = exitFeedbackFormPath;
	}

	public Boolean getSeparationExist() {
		return separationExist;
	}

	public void setSeparationExist(Boolean separationExist) {
		this.separationExist = separationExist;
	}

	public String getWithdrawComments() {
		return withdrawComments;
	}

	public void setWithdrawComments(String withdrawComments) {
		this.withdrawComments = withdrawComments;
	}

	public Boolean getShowEditButton() {
		return showEditButton;
	}

	public void setShowEditButton(Boolean showEditButton) {
		this.showEditButton = showEditButton;
	}

	public Boolean getShowManagerClearance() {
		return showManagerClearance;
	}

	public void setShowManagerClearance(Boolean showManagerClearance) {
		this.showManagerClearance = showManagerClearance;
	}

	public Boolean getShowTimeline() {
		return showTimeline;
	}

	public void setShowTimeline(Boolean showTimeline) {
		this.showTimeline = showTimeline;
	}

	public List<PIPAuditDTO> getPipAuditDTO() {
		return pipAuditDTO;
	}

	public void setPipAuditDTO(List<PIPAuditDTO> pipAuditDTO) {
		this.pipAuditDTO = pipAuditDTO;
	}

	/*public String getAbscondDate() {
		return abscondDate;
	}

	public void setAbscondDate(String abscondDate) {
		this.abscondDate = abscondDate;
	}

	public String getAbscondComments() {
		return abscondComments;
	}

	public void setAbscondComments(String abscondComments) {
		this.abscondComments = abscondComments;
	}*/

	/*public Boolean getIsAbsconded() {
		return isAbsconded;
	}

	public void setIsAbsconded(Boolean isAbsconded) {
		this.isAbsconded = isAbsconded;
	}
*/
	public Boolean getContractExists() {
		return contractExists;
	}

	public void setContractExists(Boolean contractExists) {
		this.contractExists = contractExists;
	}

	public String getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(String contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public String getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(String contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public Boolean getPersonalEmailFlag() {
		return personalEmailFlag;
	}

	public void setPersonalEmailFlag(Boolean personalEmailFlag) {
		this.personalEmailFlag = personalEmailFlag;
	}

	public String getInitiatedDate() {
		return initiatedDate;
	}

	public void setInitiatedDate(String initiatedDate) {
		this.initiatedDate = initiatedDate;
	}
	
	


}
