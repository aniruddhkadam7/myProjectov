package com.raybiztech.appraisals.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import com.raybiztech.appraisals.observation.dto.ObservationDTO;
import com.raybiztech.biometric.dto.BioAttendanceDTO;
import com.raybiztech.employeeprofile.dto.EmployeeBankInformationDTO;
import com.raybiztech.employeeprofile.dto.EmployeeFamilyInformationDTO;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;

public class EmployeeDTO implements Serializable, Comparable<EmployeeDTO> {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String firstName;
	private String lastName;
	private String middleName;
	private String designation;
	private String role;
	private EmployeeDTO manager;
	private DesignationKrasDTO designationKrasDTO;
	private Boolean employeeSubmitted;
	private Boolean managerSubmitted;
	private Boolean acknowledged;
	private String fullName;
	private String profilePicPath;
	private String thumbPicture;
	private String profilePicGeneratedPath;
	private String gender;
	private String dob;
	private String departmentName;
	private String employmentTypeName;
	private String jobTypeName;
	private byte[] imageData;
	private String curentLocation;
	private String baseLocation;
	private String officialBirthday;
	private String realBirthday;
	private String maritalStatus;
	private String emergencyContactName;
	private String emergencyPhone;
	private String emergencyRelationShip;
	private String grade;
	private String aboutMe;
	private String homeNumber;
	private String workNumber;
	private String presentAddress;
	private String presentCity;
	private String presentZip;
	private String presentLandMark;
	private String permanentAddress;
	private String permanentCity;
	private String permanentZip;
	private String permanentLandMark;
	private String mobile;
	private String homeCode;
	private String workCode;
	private Set<SkillDTO> skillList;
	private String userName;
	private String alternativeMobile;
	private String dateOfJoining;
	private List<EmployeeFamilyInformationDTO> informationList;
	private String anniversary;
	private List<EmployeeBankInformationDTO> bankInformationList;
	private List<BioAttendanceDTO> bioAttendanceDtoSet;
	private List<String> dates;
	private Integer absentCount = 0;
	private String statusName;
	private String Address;
	private String emergencyContact;
	private String relievingDate;
	private List<ObservationDTO> observationDTOList;
	private String skypeId;
	private Double percent;
	private String passportNumber;
	private String passportExpDate;
	private String passportIssuedPlace;
	private String passportIssuedDate;
	private String token;
	private String underNoticeDate;
	private Long candidateId;
	private Boolean underNotice;
	private String emailId;
	private String empManager;
	private String bloodgroup;
	private String rbtCvPath;
	private String rbtCvName;
	private TimeSlotDTO timeSlotDTO;
	//here this is the new Hr Associate Field
	private String technology;
	private EmployeeDTO hrAssociate;
	//FOR LATE COMING COUNT IN EXCELSHEET
	private Long lateComingCount;
	//for new attachments
	public String passportFrontPagePath;
	public String passportBackPagePath;
	private byte[] passportFrontPage;
	private byte[] passportBackPage;
	//new project manager field
	private EmployeeDTO projectManager;
	//new fields for attendance sheet
	private Integer casualLeaveCount = 0;
	private Integer lopLeaveCount = 0;
	private Integer holidaysCount = 0;
	//private Boolean isAbsconded;
	public Boolean contractExists;
	public String contractStartDate;
	public String contractEndDate;
	public String personalEmail;
	public Double experience;
	public Double companyExperience;
	public Double updatedExperience;
	public String country;
	public String workStatus;
    public String comments;
    public Long vendorId;
    public String vendorName;
    public Integer countryCodeWork;
    public Integer countryCodeMobile;
    public Integer countryCodeHome;
    public Integer countryCodeEmergency;
    public Integer countryCodeAlternative;
    
    
    
    public Integer getCountryCodeWork() {
		return countryCodeWork;
	}

	public void setCountryCodeWork(Integer countryCodeWork) {
		this.countryCodeWork = countryCodeWork;
	}

	public Integer getCountryCodeMobile() {
		return countryCodeMobile;
	}

	public void setCountryCodeMobile(Integer countryCodeMobile) {
		this.countryCodeMobile = countryCodeMobile;
	}

	public Integer getCountryCodeHome() {
		return countryCodeHome;
	}

	public void setCountryCodeHome(Integer countryCodeHome) {
		this.countryCodeHome = countryCodeHome;
	}

	public Integer getCountryCodeEmergency() {
		return countryCodeEmergency;
	}

	public void setCountryCodeEmergency(Integer countryCodeEmergency) {
		this.countryCodeEmergency = countryCodeEmergency;
	}

	public Integer getCountryCodeAlternative() {
		return countryCodeAlternative;
	}

	public void setCountryCodeAlternative(Integer countryCodeAlternative) {
		this.countryCodeAlternative = countryCodeAlternative;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String getWorkStatus() {
		return workStatus;
	}

	public void setWorkStatus(String workStatus) {
		this.workStatus = workStatus;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Double getExperience() {
		return experience;
	}

	public void setExperience(Double experience) {
		this.experience = experience;
	}

	public String getEmpManager() {
		return empManager;
	}

	public void setEmpManager(String empManager) {
		this.empManager = empManager;
	}

	public Long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;

	}

	public Boolean isUnderNotice() {
		return underNotice;
	}

	public void setUnderNotice(Boolean underNotice) {
		this.underNotice = underNotice;
	}

	public String getUnderNoticeDate() {
		return underNoticeDate;
	}

	public void setUnderNoticeDate(String underNoticeDate) {
		this.underNoticeDate = underNoticeDate;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public String getPassportIssuedPlace() {
		return passportIssuedPlace;
	}

	public void setPassportIssuedPlace(String passportIssuedPlace) {
		this.passportIssuedPlace = passportIssuedPlace;
	}

	public String getPassportExpDate() {
		return passportExpDate;
	}

	public void setPassportExpDate(String passportExpDate) {
		this.passportExpDate = passportExpDate;
	}

	public String getPassportIssuedDate() {
		return passportIssuedDate;
	}

	public void setPassportIssuedDate(String passportIssuedDate) {
		this.passportIssuedDate = passportIssuedDate;
	}

	public String getAddress() {
		return Address;
	}

	public void setAddress(String Address) {
		this.Address = Address;
	}

	public String getEmergencyContact() {
		return emergencyContact;
	}

	public void setEmergencyContact(String emergencyContact) {
		this.emergencyContact = emergencyContact;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public Integer getAbsentCount() {
		return absentCount;
	}

	public void setAbsentCount(Integer absentCount) {
		this.absentCount = absentCount;
	}

	public List<EmployeeBankInformationDTO> getBankInformationList() {
		return bankInformationList;
	}

	public void setBankInformationList(
			List<EmployeeBankInformationDTO> bankInformationList) {
		this.bankInformationList = bankInformationList;
	}

	public String getDateOfJoining() {
		return dateOfJoining;
	}

	public void setDateOfJoining(String dateOfJoining) {
		this.dateOfJoining = dateOfJoining;
	}

	public String getAnniversary() {
		return anniversary;
	}

	public void setAnniversary(String anniversary) {
		this.anniversary = anniversary;
	}

	public List<EmployeeFamilyInformationDTO> getInformationList() {
		return informationList;
	}

	public void setInformationList(
			List<EmployeeFamilyInformationDTO> informationList) {
		this.informationList = informationList;
	}

	public String getAlternativeMobile() {
		return alternativeMobile;
	}

	public void setAlternativeMobile(String alternativeMobile) {
		this.alternativeMobile = alternativeMobile;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Set<SkillDTO> getSkillList() {
		return skillList;
	}

	public void setSkillList(Set<SkillDTO> skillList) {
		this.skillList = skillList;
	}

	public String getHomeCode() {
		return homeCode;
	}

	public void setHomeCode(String homeCode) {
		this.homeCode = homeCode;
	}

	public String getWorkCode() {
		return workCode;
	}

	public void setWorkCode(String workCode) {
		this.workCode = workCode;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getHomeNumber() {
		return homeNumber;
	}

	public void setHomeNumber(String homeNumber) {
		this.homeNumber = homeNumber;
	}

	public String getWorkNumber() {
		return workNumber;
	}

	public void setWorkNumber(String workNumber) {
		this.workNumber = workNumber;
	}

	public String getPresentAddress() {
		return presentAddress;
	}

	public void setPresentAddress(String presentAddress) {
		this.presentAddress = presentAddress;
	}

	public String getPresentCity() {
		return presentCity;
	}

	public void setPresentCity(String presentCity) {
		this.presentCity = presentCity;
	}

	public String getPresentZip() {
		return presentZip;
	}

	public void setPresentZip(String presentZip) {
		this.presentZip = presentZip;
	}

	public String getPresentLandMark() {
		return presentLandMark;
	}

	public void setPresentLandMark(String presentLandMark) {
		this.presentLandMark = presentLandMark;
	}

	public String getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public String getPermanentCity() {
		return permanentCity;
	}

	public void setPermanentCity(String permanentCity) {
		this.permanentCity = permanentCity;
	}

	public String getPermanentZip() {
		return permanentZip;
	}

	public void setPermanentZip(String permanentZip) {
		this.permanentZip = permanentZip;
	}

	public String getPermanentLandMark() {
		return permanentLandMark;
	}

	public void setPermanentLandMark(String permanentLandMark) {
		this.permanentLandMark = permanentLandMark;
	}

	public String getCurentLocation() {
		return curentLocation;
	}

	public void setCurentLocation(String curentLocation) {
		this.curentLocation = curentLocation;
	}

	public String getBaseLocation() {
		return baseLocation;
	}

	public void setBaseLocation(String baseLocation) {
		this.baseLocation = baseLocation;
	}

	public String getOfficialBirthday() {
		return officialBirthday;
	}

	public void setOfficialBirthday(String officialBirthday) {
		this.officialBirthday = officialBirthday;
	}

	public String getRealBirthday() {
		return realBirthday;
	}

	public void setRealBirthday(String realBirthday) {
		this.realBirthday = realBirthday;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public String getEmergencyContactName() {
		return emergencyContactName;
	}

	public void setEmergencyContactName(String emergencyContactName) {
		this.emergencyContactName = emergencyContactName;
	}

	public String getEmergencyPhone() {
		return emergencyPhone;
	}

	public void setEmergencyPhone(String emergencyPhone) {
		this.emergencyPhone = emergencyPhone;
	}

	public String getEmergencyRelationShip() {
		return emergencyRelationShip;
	}

	public void setEmergencyRelationShip(String emergencyRelationShip) {
		this.emergencyRelationShip = emergencyRelationShip;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public String getJobTypeName() {
		return jobTypeName;
	}

	public void setJobTypeName(String jobTypeName) {
		this.jobTypeName = jobTypeName;
	}

	public String getEmploymentTypeName() {
		return employmentTypeName;
	}

	public void setEmploymentTypeName(String employmentTypeName) {
		this.employmentTypeName = employmentTypeName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public EmployeeDTO getManager() {
		return manager;
	}

	public void setManager(EmployeeDTO manager) {
		this.manager = manager;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public DesignationKrasDTO getDesignationKrasDTO() {
		return designationKrasDTO;
	}

	public void setDesignationKrasDTO(DesignationKrasDTO designationKrasDTO) {
		this.designationKrasDTO = designationKrasDTO;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public Boolean isEmployeeSubmitted() {
		return employeeSubmitted;
	}

	public void setEmployeeSubmitted(Boolean employeeSubmitted) {
		this.employeeSubmitted = employeeSubmitted;
	}

	public Boolean isManagerSubmitted() {
		return managerSubmitted;
	}

	public void setManagerSubmitted(Boolean managerSubmitted) {
		this.managerSubmitted = managerSubmitted;
	}

	public Boolean isAcknowledged() {
		return acknowledged;
	}

	public void setAcknowledged(Boolean acknowledged) {
		this.acknowledged = acknowledged;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getProfilePicPath() {
		return profilePicPath;
	}

	public void setProfilePicPath(String profilePicPath) {
		this.profilePicPath = profilePicPath;
	}

	public String getProfilePicGeneratedPath() {
		return profilePicGeneratedPath;
	}

	public List<BioAttendanceDTO> getBioAttendanceDtoSet() {
		return bioAttendanceDtoSet;
	}

	public void setBioAttendanceDtoSet(
			List<BioAttendanceDTO> bioAttendanceDtoSet) {
		this.bioAttendanceDtoSet = bioAttendanceDtoSet;
	}

	public void setProfilePicGeneratedPath(String profilePicGeneratedPath) {
		this.profilePicGeneratedPath = profilePicGeneratedPath;
	}

	public List<String> getDates() {
		return dates;
	}

	public void setDates(List<String> dates) {
		this.dates = dates;
	}

	public String getRelievingDate() {
		return relievingDate;
	}

	public void setRelievingDate(String relievingDate) {
		this.relievingDate = relievingDate;
	}

	public List<ObservationDTO> getObservationDTOList() {
		return observationDTOList;
	}

	public void setObservationDTOList(List<ObservationDTO> observationDTOList) {
		this.observationDTOList = observationDTOList;
	}

	public String getSkypeId() {
		return skypeId;
	}

	public void setSkypeId(String skypeId) {
		this.skypeId = skypeId;
	}

	public Double getPercent() {
		return percent;
	}

	public void setPercent(Double percent) {
		this.percent = percent;
	}

	public String getThumbPicture() {
		return thumbPicture;
	}

	public void setThumbPicture(String thumbPicture) {
		this.thumbPicture = thumbPicture;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public int compareTo(EmployeeDTO o) {

		return (this.getId()).compareTo(o.getId());
	}

	public String getBloodgroup() {
		return bloodgroup;
	}

	public void setBloodgroup(String bloodgroup) {
		this.bloodgroup = bloodgroup;
	}

	public String getRbtCvPath() {
		return rbtCvPath;
	}

	public void setRbtCvPath(String rbtCvPath) {
		this.rbtCvPath = rbtCvPath;
	}

	public String getRbtCvName() {
		return rbtCvName;
	}

	public void setRbtCvName(String rbtCvName) {
		this.rbtCvName = rbtCvName;
	}

	public TimeSlotDTO getTimeSlotDTO() {
		return timeSlotDTO;
	}

	public void setTimeSlotDTO(TimeSlotDTO timeSlotDTO) {
		this.timeSlotDTO = timeSlotDTO;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public EmployeeDTO getHrAssociate() {
		return hrAssociate;
	}

	public void setHrAssociate(EmployeeDTO hrAssociate) {
		this.hrAssociate = hrAssociate;
	}

	public Long getLateComingCount() {
		return lateComingCount;
	}

	public void setLateComingCount(Long lateComingCount) {
		this.lateComingCount = lateComingCount;
	}

	public EmployeeDTO getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(EmployeeDTO projectManager) {
		this.projectManager = projectManager;
	}

	public Integer getCasualLeaveCount() {
		return casualLeaveCount;
	}

	public void setCasualLeaveCount(Integer casualLeaveCount) {
		this.casualLeaveCount = casualLeaveCount;
	}

	public Integer getLopLeaveCount() {
		return lopLeaveCount;
	}

	public void setLopLeaveCount(Integer lopLeaveCount) {
		this.lopLeaveCount = lopLeaveCount;
	}

	public Integer getHolidaysCount() {
		return holidaysCount;
	}

	public void setHolidaysCount(Integer holidaysCount) {
		this.holidaysCount = holidaysCount;
	}

	public String getPassportFrontPagePath() {
		return passportFrontPagePath;
	}

	public void setPassportFrontPagePath(String passportFrontPagePath) {
		this.passportFrontPagePath = passportFrontPagePath;
	}

	public String getPassportBackPagePath() {
		return passportBackPagePath;
	}

	public void setPassportBackPagePath(String passportBackPagePath) {
		this.passportBackPagePath = passportBackPagePath;
	}

	public byte[] getPassportFrontPage() {
		return passportFrontPage;
	}

	public void setPassportFrontPage(byte[] passportFrontPage) {
		this.passportFrontPage = passportFrontPage;
	}

	public byte[] getPassportBackPage() {
		return passportBackPage;
	}

	public void setPassportBackPage(byte[] passportBackPage) {
		this.passportBackPage = passportBackPage;
	}

	/*public Boolean getIsAbsconded() {
		return isAbsconded;
	}

	public void setIsAbsconded(Boolean isAbsconded) {
		this.isAbsconded = isAbsconded;
	}*/

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

	public Double getCompanyExperience() {
		return companyExperience;
	}

	public void setCompanyExperience(Double companyExperience) {
		this.companyExperience = companyExperience;
	}

	public Double getUpdatedExperience() {
		return updatedExperience;
	}

	public void setUpdatedExperience(Double updatedExperience) {
		this.updatedExperience = updatedExperience;
	}

	public String getPersonalEmail() {
		return personalEmail;
	}

	public void setPersonalEmail(String personalEmail) {
		this.personalEmail = personalEmail;
	}

	
	
	
	
	
}
