package com.raybiztech.recruitment.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.raybiztech.appraisalmanagement.business.Designation;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.TimeSlot;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.assetmanagement.business.VendorDetails;
import com.raybiztech.date.Date;
import com.raybiztech.lookup.business.SkillLookUp;
import com.raybiztech.recruitment.business.BloodGroup;
import com.raybiztech.recruitment.business.Candidate;
import com.raybiztech.recruitment.business.CandidateInterviewCycle;
import com.raybiztech.recruitment.business.Companies;
import com.raybiztech.recruitment.business.EmploymentType;
import com.raybiztech.recruitment.business.Gender;
import com.raybiztech.recruitment.business.JobType;
import com.raybiztech.recruitment.business.MaritalStatus;
import com.raybiztech.recruitment.business.NewJoinee;
import com.raybiztech.recruitment.business.Panel;
import com.raybiztech.recruitment.business.QualificationCategory;
import com.raybiztech.recruitment.business.Relations;
import com.raybiztech.recruitment.business.Schedule;
import com.raybiztech.recruitment.dto.CompanyDTO;
import com.raybiztech.recruitment.dto.SearchQueryParams;

public interface JobPortalDAO extends DAO {

	<T extends Serializable> T findByEmailName(Class<T> clazz, Serializable name);

	<T extends Serializable> T findByMobileNumber(Class<T> clazz,
			Serializable name);

	List<Employee> getAllEmployeeData();

	List<CandidateInterviewCycle> searchCadidates(String fromDate, String toDate,String skill);

	List<SkillLookUp> getAllSkillsLookUp();

	List<Candidate> getAllUpcomingJoineeList();

	Boolean isCandidateMailExists(String candidateEmail);

	Boolean isCandidateMobileNumberExists(String candidateMobileNumber);

	Boolean isEditCandidateMailExists(String candidateId, String mailId);

	Boolean isEditCandidateMobileNumberExists(String candidateId,
			String mobileNum);

	Map<String, Object> getProfilePaginationEmployeesData(
			String selectionStatus, Integer startIndex, Integer endIndex,
			String searchStr);
	Map<String, Object> getSelectedTypeEmployeeData(
			String selectionStatus, String selectionType, Integer startIndex, Integer endIndex,
			String searchStr, String country );

	List<Panel> panelDepartmentEmployeeData(String departmentId);

	List<Employee> getActiveEmployeeData();

	CandidateInterviewCycle reSheduleInterviewForCandidate(String candidateId);

	Schedule getCadidateSchedule(Long interviewId);

	List<CandidateInterviewCycle> getCandidateTimeLineDetails(String candidateId);
	
	List<CandidateInterviewCycle> getCandidateTimeLinedetails(String candidateId,String name);

	Map<String, Object> getScheduledCandidatesForEmployee(Long employeeId,
			Integer startIndex, Integer endIndex);

	CandidateInterviewCycle getEmpScheduleInterviewDetailsByID(
			Long interviewCycleId);

	Map<String, Object> getSearchScheduledCandidatesForEmployee(
			Long employeeId, String status, Date fromDate, Date toDate,
			Integer startIndex, Integer endIndex);

	List<CandidateInterviewCycle> viewScheduleCompletedCandidates(
			String candidateId);

	Map<String, Object> getAllnonScheduledCadidates(Integer startIndex,
			Integer endIndex);

	Map<String, Object> getScheduledCadidatesBySearch(Integer startIndex,
			Integer endIndex, String searchStr, String country);

	Boolean isJobCodeExist(String jobCode);

	List<Employee> getSearchEmployeeData(String searchStr);

	Map<String, Object> searchCandidates(String skill, Date fromDate,
			Date toDate, Integer startIndex, Integer endIndex);

	CandidateInterviewCycle getScheduledInterviewDataById(Long cycleId);

	NewJoinee getJoineeByCandidateId(Long candidateId);

	List<CandidateInterviewCycle> pendingInterviewList(String candidateId);

	Map<String, Object> interviewStatusReport(SearchQueryParams queryParams);
	
	Map<String, Object> getCountryWiseCandidatesList(SearchQueryParams queryParams);

	Boolean isCandidateMappedWithJob(String jobCode);

	Map<String, Object> getUpcomingJoineeList(Integer startIndex,
			Integer endIndex, String searchName);

	Map<String, Object> searchScheduledCandidates(String skill, Date fDate,
			Date tDate, String search, Integer startIndex, Integer endIndex);

	NewJoinee findnewJoinee(String email);

	Boolean checkemail(String email);

	CandidateInterviewCycle getrescduleDetails(String candidateId);

	Designation designtionIsExisted(Long departmentId, String designationName);

	Boolean designationIsAssigned(Designation designation);

	Long highestInterviewRound(Long candidateId);

	String getInterviewerName(Long candidateId); 
	
	int checkSlot(String name);

	List<TimeSlot> getAllShifting();
	
	//for getting SourceType employee name
	public Boolean internalSource(String firstName,String LastName); 
		
	//for reports page
	public List<Object> getCandidatesListCount(String selectionStatus,String fromDate,String toDate);
	
	//for getting pip list employee ids for pip table.
	List<Long> getAllPipList();
	
	//for getting employee object on sending list of employee id's
	public Map<String,Object> getEmployeeList(List<Long> ids, Integer startIndex, Integer endIndex,
			String searchStr);

	List<Long> getAbscondedList(); 
	
	Map<String,Object> getAllCompanies(Integer startIndex, Integer endIndex,String searchCompany,String selectionTechnology);
	
  Map<String,Object> getSearchCompanies(Integer startIndex, Integer endIndex,String searchCompany);
	
	Long getCandidatesCount( String comList, String selectionTechnology);
	
	Long getemployessCount(String comList ,String  selectionTechnology);
	
	Boolean isCompanyExits( String companyName);
	
	Map<String, Object> getAllCandidatesInfo(Integer startIndex, Integer endIndex,String companyName);
	
	Map<String, Object> getAllEmployeesInfo(Integer startIndex, Integer endIndex,String companyName);
	
	List<Long> getAllContractList();
	
	List<Employee> getEmployeesExperiencesList();


	List<CandidateInterviewCycle> getInterviewerDetails(String fromDate, String selectionStatus, String toDate);
	List<Employee> getActiveEmployees();
	

	Map<String, Date> getDates(String dateSelection ,String from,String to);
	
	 List<Object[]> getInterviewStatusCount(String dateSelection ,Date fromDate,Date toDate); 
	 
	 Map<String, Object> getEmployeeCategoryData(
				String selectionDesignation, Integer startIndex, Integer endIndex);

	List<VendorDetails> getVendorDetails();
	
	List<Gender> getGenders();

	List<BloodGroup> getBloodgroups();

	List<MaritalStatus> getMaritalStatus();

	List<Relations> getRelations();

	List<QualificationCategory> getQualificationCatergory();

	List<EmploymentType> getEmploymentType();

	List<JobType> getJobType();
	
}
