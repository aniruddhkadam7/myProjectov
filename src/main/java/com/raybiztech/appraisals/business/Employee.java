package com.raybiztech.appraisals.business;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
import org.springframework.security.core.userdetails.UserDetails;

import com.raybiztech.appraisals.certification.business.Certification;
import com.raybiztech.appraisals.observation.business.Observation;
import com.raybiztech.assetmanagement.business.VendorDetails;
import com.raybiztech.date.Date;
import com.raybiztech.date.Second;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.invoice.lookup.CountryLookUp;
import com.raybiztech.recruitment.business.Interview;
import com.raybiztech.rolefeature.business.Role;

public class Employee implements Serializable, Comparable<Employee>,
		UserDetails, Cloneable {

	private static final long serialVersionUID = 1L;
	private Long employeeId;
	public String firstName;
	public String lastName;
	public Employee manager;
	private DesignationKras designationKras;
	private Set<Interview> interviewEmployees;
	private Map<Project, AllocationDetails> allocations;
	public String middleName;
	private String userName;
	private String userToken;
	private String nickName;
	public Date dob;
	public String maritalStatus;
	public Date marriageDate;
	public String nationalityName;
	private String email;
	private String alternateEmail;
	private String pan;
	public String statusName;
	public String passportNumber;
	public Date passportExpDate;
	public String passportIssuedPlace;
	public Date passportIssuedDate;
	public String currentLocation;
	private String passwordHash;
	private String saltKey;
	public String gender;
	private String siteUrl;
	public String aboutMe;
	public String emergencyContactName;
	public String phone;
	public String relationship;
	private Date createdDate;
	public String employmentTypeName;
	public String jobTypeName;
	public Date realDob;
	public String baseLocation;
	public String workLocation;
	private String grade;
	public String departmentName;
	public String profilePicture;
	public String thumbPicture;
	private Date joiningDate;
	public Date releavingDate;
	private String modifiedBy;
	private String onlineStatus;
	public String role;
	private Boolean employeeSubmitted;
	private Boolean managerSubmitted;
	private Boolean acknowledged;
	public String profilePicPath;
	public String profilePicGeneratedPath;
	public String homePhoneNumber;
	public String officePhoneNumber;
	public String presentAddress;
	public String presentCity;
	public String presentZip;
	public String presentLandMark;
	public String permanentAddress;
	public String permanentCity;
	public String permanentZip;
	public String permanentLandMark;
	private Set<Skill> employeeSkills;
	public String homeCode;
	public String workCode;
	public String designation;
	public String alternativeMobile;
	public String emergencyPhone;
	private Set<EmployeeFamilyInformation> employeeFamilyDetails;
	private Set<EmployeeBankInformation> bankInformations;
	private Set<Observation> observation;
	private Set<Certification> certifications;
	public String skypeId;
	public Role empRole;
	public Date underNoticeDate;
	public Boolean underNotice;
	private String password;
	public String employeeFullName;
	public String bloodgroup;
	private String rbtCvPath;
	public Long createdBy;
	public Long updatedBy;
	private Second createddate;
	private Second updatedDate;
	public TimeSlot timeSlot;
	public String technology;
	// new hr associate field
	public Employee hrAssociate;
	// new attachments
	public String passportFrontPagePath;
	public String passportBackPagePath;
	// new project manager field
	public Employee projectManager;
	// public Boolean isAbsconded;
	public Boolean contractExists;
	public Date contractStartDate;
	public Date contractEndDate;
	public String personalEmail;
	public Double experience;
	public Double companyExperience;
	public String icon;
	public String country;
	public String workStatus;
    public String comments;
    public VendorDetails vendor;
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
    
    
	
	
   public VendorDetails getVendor() {
		return vendor;
	}

	public void setVendor(VendorDetails vendor) {
		this.vendor = vendor;
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

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Boolean isUnderNotice() {
		return underNotice;
	}

	public void setUnderNotice(Boolean underNotice) {
		this.underNotice = underNotice;
	}

	private Long candidateId;

	public Long getCandidateId() {
		return candidateId;
	}

	public void setCandidateId(Long candidateId) {
		this.candidateId = candidateId;
	}

	public Date getUnderNoticeDate() {
		return underNoticeDate;
	}

	public void setUnderNoticeDate(Date underNoticeDate) {
		this.underNoticeDate = underNoticeDate;
	}

	public Role getEmpRole() {
		return empRole;
	}

	public void setEmpRole(Role empRole) {
		this.empRole = empRole;
	}

	public String getPassportIssuedPlace() {
		return passportIssuedPlace;
	}

	public void setPassportIssuedPlace(String passportIssuedPlace) {
		this.passportIssuedPlace = passportIssuedPlace;
	}

	public Date getPassportIssuedDate() {
		return passportIssuedDate;
	}

	public void setPassportIssuedDate(Date passportIssuedDate) {
		this.passportIssuedDate = passportIssuedDate;
	}

	public Set<EmployeeBankInformation> getBankInformations() {
		return bankInformations;
	}

	public void setBankInformations(
			Set<EmployeeBankInformation> bankInformations) {
		this.bankInformations = bankInformations;
	}

	public Set<EmployeeFamilyInformation> getEmployeeFamilyDetails() {
		return employeeFamilyDetails;
	}

	public void setEmployeeFamilyDetails(
			Set<EmployeeFamilyInformation> employeeFamilyDetails) {
		this.employeeFamilyDetails = employeeFamilyDetails;
	}

	public Set<Certification> getCertifications() {
		return certifications;
	}

	public void setCertifications(Set<Certification> certifications) {
		this.certifications = certifications;
	}

	public String getEmergencyPhone() {
		return emergencyPhone;
	}

	public void setEmergencyPhone(String emergencyPhone) {
		this.emergencyPhone = emergencyPhone;
	}

	public String getAlternativeMobile() {
		return alternativeMobile;
	}

	public void setAlternativeMobile(String alternativeMobile) {
		this.alternativeMobile = alternativeMobile;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
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

	public Set<Skill> getEmployeeSkills() {
		return employeeSkills;
	}

	public void setEmployeeSkills(Set<Skill> employeeSkills) {
		this.employeeSkills = employeeSkills;
	}

	public String getHomePhoneNumber() {
		return homePhoneNumber;
	}

	public void setHomePhoneNumber(String homePhoneNumber) {
		this.homePhoneNumber = homePhoneNumber;
	}

	public String getOfficePhoneNumber() {
		return officePhoneNumber;
	}

	public void setOfficePhoneNumber(String officePhoneNumber) {
		this.officePhoneNumber = officePhoneNumber;
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

	public Date getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(Date joiningDate) {
		this.joiningDate = joiningDate;
	}

	public Employee() {
		interviewEmployees = new HashSet<Interview>();
	}

	public Employee(String userName, String lastName, String email,
			String statusName, String passwordHash, String saltKey,
			String gender, String employmentTypeName, String jobTypeName,
			String departmentName) {
		this.userName = userName;
		this.lastName = lastName;
		this.email = email;
		this.statusName = statusName;
		this.passwordHash = passwordHash;
		this.saltKey = saltKey;
		this.gender = gender;
		this.employmentTypeName = employmentTypeName;
		this.jobTypeName = jobTypeName;
		this.departmentName = departmentName;
	}

	public Set<Interview> getInterviewEmployees() {
		return interviewEmployees;
	}

	public void setInterviewEmployees(Set<Interview> interviewEmployees) {
		this.interviewEmployees = interviewEmployees;
	}

	public Long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Long employeeId) {
		this.employeeId = employeeId;
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

	public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
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

	public void setProfilePicGeneratedPath(String profilePicGeneratedPath) {
		this.profilePicGeneratedPath = profilePicGeneratedPath;
	}

	public DesignationKras getDesignationKras() {
		return designationKras;
	}

	public void setDesignationKras(DesignationKras designationKras) {
		this.designationKras = designationKras;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Date getDob() {
		return dob;
	}

	public void setDob(Date dob) {
		this.dob = dob;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public Date getMarriageDate() {
		return marriageDate;
	}

	public void setMarriageDate(Date marriageDate) {
		this.marriageDate = marriageDate;
	}

	public String getNationalityName() {
		return nationalityName;
	}

	public void setNationalityName(String nationalityName) {
		this.nationalityName = nationalityName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAlternateEmail() {
		return alternateEmail;
	}

	public void setAlternateEmail(String alternateEmail) {
		this.alternateEmail = alternateEmail;
	}

	public String getPan() {
		return pan;
	}

	public void setPan(String pan) {
		this.pan = pan;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

	public String getPassportNumber() {
		return passportNumber;
	}

	public void setPassportNumber(String passportNumber) {
		this.passportNumber = passportNumber;
	}

	public Date getPassportExpDate() {
		return passportExpDate;
	}

	public void setPassportExpDate(Date passportExpDate) {
		this.passportExpDate = passportExpDate;
	}

	public String getCurrentLocation() {
		return currentLocation;
	}

	public void setCurrentLocation(String currentLocation) {
		this.currentLocation = currentLocation;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getSaltKey() {
		return saltKey;
	}

	public void setSaltKey(String saltKey) {
		this.saltKey = saltKey;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSiteUrl() {
		return siteUrl;
	}

	public void setSiteUrl(String siteUrl) {
		this.siteUrl = siteUrl;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public String getEmergencyContactName() {
		return emergencyContactName;
	}

	public void setEmergencyContactName(String emergencyContactName) {
		this.emergencyContactName = emergencyContactName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getRelationship() {
		return relationship;
	}

	public void setRelationship(String relationship) {
		this.relationship = relationship;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getEmploymentTypeName() {
		return employmentTypeName;
	}

	public void setEmploymentTypeName(String employmentTypeName) {
		this.employmentTypeName = employmentTypeName;
	}

	public String getJobTypeName() {
		return jobTypeName;
	}

	public void setJobTypeName(String jobTypeName) {
		this.jobTypeName = jobTypeName;
	}

	public Date getRealDob() {
		return realDob;
	}

	public void setRealDob(Date realDob) {
		this.realDob = realDob;
	}

	public String getBaseLocation() {
		return baseLocation;
	}

	public void setBaseLocation(String baseLocation) {
		this.baseLocation = baseLocation;
	}

	public String getWorkLocation() {
		return workLocation;
	}

	public void setWorkLocation(String workLocation) {
		this.workLocation = workLocation;
	}

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

	public String getDepartmentName() {
		return departmentName;
	}

	public void setDepartmentName(String departmentName) {
		this.departmentName = departmentName;
	}

	public String getProfilePicture() {
		return profilePicture;
	}

	public void setProfilePicture(String profilePicture) {
		this.profilePicture = profilePicture;
	}

	public String getThumbPicture() {
		return thumbPicture;
	}

	public void setThumbPicture(String thumbPicture) {
		this.thumbPicture = thumbPicture;
	}

	public Date getReleavingDate() {
		return releavingDate;
	}

	public void setReleavingDate(Date releavingDate) {
		this.releavingDate = releavingDate;
	}

	public String getModifiedBy() {
		return modifiedBy;
	}

	public void setModifiedBy(String modifiedBy) {
		this.modifiedBy = modifiedBy;
	}

	public String getOnlineStatus() {
		return onlineStatus;
	}

	public void setOnlineStatus(String onlineStatus) {
		this.onlineStatus = onlineStatus;
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

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public Map<Project, AllocationDetails> getAllocations() {
		return allocations;
	}

	public void setAllocations(Map<Project, AllocationDetails> allocations) {
		this.allocations = allocations;
	}

	public Set<Observation> getObservation() {
		return observation;
	}

	public void setObservation(Set<Observation> observation) {
		this.observation = observation;
	}

	public String getSkypeId() {
		return skypeId;
	}

	public void setSkypeId(String skypeId) {
		this.skypeId = skypeId;
	}

	public String getEmployeeFullName() {
		return employeeFullName;
	}

	public void setEmployeeFullName(String employeeFullName) {
		this.employeeFullName = employeeFullName;
	}

	public String getRbtCvPath() {
		return rbtCvPath;
	}

	public void setRbtCvPath(String rbtCvPath) {
		this.rbtCvPath = rbtCvPath;
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

	public Second getUpdatedDate() {
		return updatedDate;
	}

	public void setUpdatedDate(Second updatedDate) {
		this.updatedDate = updatedDate;
	}

	public Second getCreateddate() {
		return createddate;
	}

	public void setCreateddate(Second createddate) {
		this.createddate = createddate;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	@Override
	public int hashCode() {
		return new HashCodeBuilder(1989, 55).append(employeeId).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Employee other = (Employee) obj;
		return new EqualsBuilder().append(employeeId, other.employeeId)
				.isEquals();
	}

	@Override
	public int compareTo(Employee employee) {
		// TODO Auto-generated method stub
		return this.employeeId.compareTo(employee.getEmployeeId());
	}

	public String getFullName() {
		String firstName = this.getFirstName().substring(0, 1).toUpperCase()
				+ this.getFirstName().substring(1);
		String lastName = this.getLastName().substring(0, 1).toUpperCase()
				+ this.getLastName().substring(1);

		return firstName + " " + lastName;

	}

	@Override
	public String toString() {
		return userName;
	}

	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		String roles = this.getRole();
		Set<String> roleSet = new HashSet<String>();
		roleSet.add(roles);

		if (roles == null) {
			return Collections.emptyList();
		}

		Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
		for (String userRole : roleSet) {
			if ("admin".equals(userRole)) {
				authorities.add(new GrantedAuthorityImpl("ROLE_ADMIN"));
				authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
				authorities.add(new GrantedAuthorityImpl("ROLE_MANAGER"));
				authorities.add(new GrantedAuthorityImpl("ROLE_FINANCE"));
				authorities.add(new GrantedAuthorityImpl("ROLE_HR"));
				authorities.add(new GrantedAuthorityImpl("ROLE_OFC_ADMIN"));
				authorities.add(new GrantedAuthorityImpl("ROLE_IT_ADMIN"));
			} else if ("Manager".equals(userRole)) {
				authorities.add(new GrantedAuthorityImpl("ROLE_MANAGER"));
				authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
			} else if ("Finance".equals(userRole)) {
				authorities.add(new GrantedAuthorityImpl("ROLE_FINANCE"));
				authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
			} else if ("IT_admin".equals(userRole)) {
				authorities.add(new GrantedAuthorityImpl("ROLE_IT_ADMIN"));
				authorities.add(new GrantedAuthorityImpl("ROLE_USER"));

			} else if ("OFC_admin".equals(userRole)) {
				authorities.add(new GrantedAuthorityImpl("ROLE_OFC_ADMIN"));
				authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
			} else if ("HR".equals(userRole)) {
				authorities.add(new GrantedAuthorityImpl("ROLE_HR"));
				authorities.add(new GrantedAuthorityImpl("ROLE_USER"));

			} else {
				authorities.add(new GrantedAuthorityImpl("ROLE_USER"));
			}
		}

		return authorities;
	}

	// @Override
	// public String getPassword() {
	// return "";
	// }

	@Override
	public String getUsername() {
		return userName;
	}

	@Override
	public boolean isAccountNonExpired() {
		throw new UnsupportedOperationException("Not supported yet."); // To
		// change
		// body
		// of
		// generated
		// methods,
		// choose
		// Tools
		// |
		// Templates.
	}

	@Override
	public boolean isAccountNonLocked() {
		throw new UnsupportedOperationException("Not supported yet."); // To
		// change
		// body
		// of
		// generated
		// methods,
		// choose
		// Tools
		// |
		// Templates.
	}

	@Override
	public boolean isCredentialsNonExpired() {
		throw new UnsupportedOperationException("Not supported yet."); // To
		// change
		// body
		// of
		// generated
		// methods,
		// choose
		// Tools
		// |
		// Templates.
	}

	@Override
	public boolean isEnabled() {
		throw new UnsupportedOperationException("Not supported yet."); // To
		// change
		// body
		// of
		// generated
		// methods,
		// choose
		// Tools
		// |
		// Templates.
	}

	public String getBloodgroup() {
		return bloodgroup;
	}

	public void setBloodgroup(String bloodgroup) {
		this.bloodgroup = bloodgroup;
	}

	public TimeSlot getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(TimeSlot timeSlot) {
		this.timeSlot = timeSlot;
	}

	public String getTechnology() {
		return technology;
	}

	public void setTechnology(String technology) {
		this.technology = technology;
	}

	public Employee getHrAssociate() {
		return hrAssociate;
	}

	public void setHrAssociate(Employee hrAssociate) {
		this.hrAssociate = hrAssociate;
	}

	public Employee getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(Employee projectManager) {
		this.projectManager = projectManager;
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

	/*
	 * public Boolean getIsAbsconded() { return isAbsconded; }
	 * 
	 * public void setIsAbsconded(Boolean isAbsconded) { this.isAbsconded =
	 * isAbsconded; }
	 */

	public Boolean getManagerSubmitted() {
		return managerSubmitted;
	}

	public Boolean getContractExists() {
		return contractExists;
	}

	public void setContractExists(Boolean contractExists) {
		this.contractExists = contractExists;
	}

	public Date getContractStartDate() {
		return contractStartDate;
	}

	public void setContractStartDate(Date contractStartDate) {
		this.contractStartDate = contractStartDate;
	}

	public Date getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public Double getExperience() {
		return experience;
	}

	public void setExperience(Double experience) {
		this.experience = experience;
	}

	public Double getCompanyExperience() {
		return companyExperience;
	}

	public void setCompanyExperience(Double companyExperience) {
		this.companyExperience = companyExperience;
	}

	public String getPersonalEmail() {
		return personalEmail;
	}

	public void setPersonalEmail(String personalEmail) {
		this.personalEmail = personalEmail;
	}

}
