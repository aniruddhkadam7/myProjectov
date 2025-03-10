package com.raybiztech.recruitment.service;

import com.raybiztech.appraisalmanagement.business.AppraisalForm;

import com.raybiztech.appraisalmanagement.business.Designation;
import com.raybiztech.appraisalmanagement.dto.AppraisalFormListDto;
import com.raybiztech.appraisalmanagement.dto.DesignationDto;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dto.CategoryDTO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.dto.EmployeeSkillLookUpDTO;
import com.raybiztech.appraisals.dto.ReportiesHierarchyDTO;
import com.raybiztech.appraisals.dto.SearchEmpDetailsDTO;
import com.raybiztech.appraisals.dto.SkillDTO;
import com.raybiztech.appraisals.dto.TimeSlotDTO;
import com.raybiztech.appraisals.observation.business.Observation;
import com.raybiztech.appraisals.observation.dto.ObservationDTO;
import com.raybiztech.assetmanagement.dto.VendorDto;
import com.raybiztech.recruitment.business.Candidate;
import com.raybiztech.recruitment.business.CandidateInterviewCycle;
import com.raybiztech.recruitment.business.Schedule;
import com.raybiztech.recruitment.business.Technology;
import com.raybiztech.recruitment.chart.StatusChart;
import com.raybiztech.projectmanagement.invoice.dto.CountryLookUpDTO;
import com.raybiztech.recruitment.controller.ImageDTO;
import com.raybiztech.recruitment.controller.PassportBackPageDTO;
import com.raybiztech.recruitment.controller.PassportDTO;
import com.raybiztech.recruitment.controller.VisaImageDTO;
import com.raybiztech.recruitment.dto.CandidateDTO;
import com.raybiztech.recruitment.dto.CandidateInterviewCycleDTO;
import com.raybiztech.recruitment.dto.CandidateInterviewTimelineDTO;
import com.raybiztech.recruitment.dto.CandidateScheduleDto;
import com.raybiztech.recruitment.dto.CandidateStatus;
import com.raybiztech.recruitment.dto.CompaniesDTO;
import com.raybiztech.recruitment.dto.CompanyDTO;
import com.raybiztech.recruitment.dto.DepartmentDTO;
import com.raybiztech.recruitment.dto.JobVacancyDTO;
import com.raybiztech.recruitment.dto.NewJoineeDTO;
import com.raybiztech.recruitment.dto.PanelDTO;
import com.raybiztech.recruitment.dto.ScheduledCadidateDTO;
import com.raybiztech.recruitment.dto.SearchQueryParams;
import com.raybiztech.recruitment.dto.SkillLookUpDTO;
import com.raybiztech.recruitment.exception.JobPortalException;
import com.raybiztech.recruitment.utils.FileUploaderUtility;
import com.raybiztech.supportmanagement.dto.TicketsCategoryDTO;

import com.raybiztech.recruitment.dto.EmploymentTypeDTO;
import com.raybiztech.recruitment.dto.GenderDTO;
import com.raybiztech.recruitment.dto.MaritalStatusDTO;
import com.raybiztech.recruitment.dto.BloodGroupDTO;
import com.raybiztech.recruitment.dto.QualificationCategoryDTO;
import com.raybiztech.recruitment.dto.RelationsDTO;
import com.raybiztech.recruitment.dto.JobTypeDTO;

public interface JobPortalService {

	Long addNewCandidadate(CandidateScheduleDto candidateScheduleDto)
			throws JobPortalException;

	List<EmployeeDTO> getAllEmployeeData();

	List<CandidateDTO> getAllCandidate();

	/*
	 * List<ScheduledCadidateDTO> searchCadidates(String skill, String fromDate,
	 * String toDate);
	 */

	List<SkillLookUpDTO> getAllSkillsLookUp();

	void updateScheduleCandidadate(ScheduledCadidateDTO scheduledCadidateDTO);

	Map<String, Object> getAllCadidates(Integer startIndex, Integer endIndex);

	CandidateScheduleDto editCandidate(String candidateId);

	void deleteCandidate(String candidateId);

	void updateCandidadate(CandidateScheduleDto candidateScheduleDto);

	void scheduleInterview(CandidateScheduleDto candidateScheduleDto);

	void addEmployeeToPanel(String employeeEmailId, String departmentId);

	// saving initial comments in interview details page
	void savingInitialComments(String comments, Long personId);

	List<PanelDTO> panelDepartmentEmployeeData(String departmentId);

	void deleteEmployeeToPanel(String panelId);

	EmployeeDTO getEmployee(Long employeeId);

	EmployeeDTO getEmployeeDataFormSearch(Long employeeId,
			HttpServletResponse res);

	void updateLoggedInEmployee(EmployeeDTO employeeDTO);

	List<SearchEmpDetailsDTO> getAllProfileEmployeesData(String searchStr);

	List<SearchEmpDetailsDTO> getAllProfileEmployeesData();

	void deleteEmployee(Long employeeId);

	// String uploadFile(MultipartFile mpf);
	List<EmployeeDTO> getAllReportingManagersData();

	void addNewEmployee(EmployeeDTO employeeDTO);

	ImageDTO getImage(Long id);

	void uploadImage(MultipartFile mpf, String parameter);

	void uploadBase64Image(String parameter, Long empLong);

	byte[] getImageByteData(String photoUrl);

	void updateEmployeeDetails(EmployeeDTO employeeDTO);

	void updateEmployeeSkill(SkillDTO skillDTO);

	void deleteEmployeeSkill(long skillId);

	void addSkillToEmployee(SkillDTO skillDTO);

	SkillDTO editSkill(long skillId);

	List<EmployeeSkillLookUpDTO> getCategorySkill(long categoryId);

	List<CategoryDTO> getAllCategories();

	List<CategoryDTO> addCategory(String categoryName);

	List<CategoryDTO> deleteCategory(long categoryId);

	List<EmployeeSkillLookUpDTO> addSkillToSpecificCategory(String skillName,
			long categoryId);

	List<EmployeeSkillLookUpDTO> deleteSkill(long skillId);

	List<SkillDTO> getEmployeeskillList(long parseLong);

	void importCandiadetFile(String filePath);

	List<ScheduledCadidateDTO> getAllUpcomingJoineeList();

	List<CandidateDTO> importExcelFile(MultipartFile mpf, String parameter);

	void overwriteDuplicateCandidateData(CandidateDTO candidateDTO);

	void sendScheduleMailToCandidate(CandidateScheduleDto candidateScheduleDto);

	void sendUpdateScheduleMailToCandidate(CandidateScheduleDto scheduleDto);

	void sendScheduleMailToNewCandidate(
			CandidateScheduleDto candidateScheduleDto, Long id);

	CandidateInterviewTimelineDTO timeLineDetails(String candidateId);

	Boolean isCandidateMailExists(String candidateEmail);

	Boolean isCandidateMobileNumberExists(String candidateMobileNumber);

	Boolean isEditCandidateMailExists(String candidateId, String mailId);

	Boolean isEditCandidateMobileNumberExists(String candidateId,
			String mobileNum);

	Map<String, Object> getProfilePaginationEmployeesData(Integer startIndex,
			Integer endIndex, String selectionStatus, String searchStr);
	
	Map<String, Object> getEmployeesCategoryData(Integer startIndex,
			Integer endIndex, String selectionDesignation);
	
	Map<String, Object> getSelectedTypeEmployeeData(Integer startIndex,
			Integer endIndex, String selectionStatus,String selectionType, String searchStr,Integer country);

	void editEmployee(EmployeeDTO employeeDTO);

	List<DepartmentDTO> getAllDepartmentList();

	List<EmployeeDTO> panelEmployeeData(String departmentId);

	EmployeeDTO getLoggedInEmployee(String username);

	CandidateInterviewCycleDTO reSheduleInterviewForCandidate(String candidateId);

	void reSheduleInterviewUpdate(CandidateInterviewCycleDTO cycleDTO);

	Map<String, Object> getScheduledCandidatesForEmployee(Long employeeId,
			Integer startIndex, Integer endIndex);

	CandidateInterviewCycleDTO empScheduleInterviewDetailsByID(
			Long interviewCycleId);

	void updateScheduleCandidatebyEmployee(CandidateInterviewCycleDTO cycleDTO);

	Map<String, Object> getSearchScheduledCandidatesForEmployee(
			Long employeeId, String status, String fromDate, String toDate,
			Integer startIndex, Integer endIndex);

	CandidateInterviewCycleDTO getViewCandiadetForAdmin(Long candidateId);

	Boolean isJobCodeExist(String jobCode);

	void downloadFile(HttpServletResponse response, String fileName);

	void uploadCandidateResume(MultipartFile mpf, String parameter);

	Map<String, Object> searchScheduledCandidate(Integer startIndex,
			Integer endIndex, String searchStr);

	/*
	 * void downloadScheduleCandidatesFile(HttpServletResponse response, String
	 * skill, String fromDate, String toDate);
	 */

	Map<String, Object> searchCandidates(String skill, String fromDate,
			String toDate, Integer startIndex, Integer endIndex);

	void addNewJoinee(NewJoineeDTO JoineeDTO);

	void updateInterview(CandidateInterviewCycleDTO cicdto);

	Integer countofInterviewRoundList(String candidateId);

	Map<String, Object> getUpcomingJoineeList(Integer startIndex,
			Integer endIndex, String searchName);

	NewJoineeDTO getJoineeById(Long joineeId);

	void updateNewJoinee(NewJoineeDTO joineeDTO);

	void reScheduleInterview(CandidateScheduleDto cicdto);

	Map<String, Object> interviewStatusReport(SearchQueryParams queryParams);
	

	Map<String, Object> getCountryWiseCandidatesList(SearchQueryParams queryParams);

	void updateCandidateInterviewStatus(CandidateStatus candidateStatus);

	Boolean isCandidateMappedWithJob(String jobCode);

	void deleteJoinee(Long joineeId);

	Map<String, Object> searchScheduledCandidates(String skill,
			String fromDate, String toDate, String search, Integer startIndex,
			Integer endIndex);

	JobVacancyDTO getJobOpeningById(Long jobVacancyId);

	Boolean checkemail(String email);

	void addTechnology(String technology);

	List<Technology> viewTechnology();
	
	List<Designation> viewDesignation();

	void deleteTechnology(Long technologyId);

	void defaultPic(String gender);

	public void uploadFileInObservation(MultipartFile mf, String personId);

	String getpersontechnology(Long id);

	List<ReportiesHierarchyDTO> getReportiesUnderManager(Long empID);

	void addDesignation(DesignationDto designationDto);

	void deleteDesignation(Long designationId);

	void uploadRBTResume(MultipartFile mpf, Long parameter);

	void downloadRBTCv(HttpServletResponse response, String fileName);

	List<ObservationDTO> getEmployeeMyProfileObservations(Long parseLong);

	void addTimeSlot(TimeSlotDTO timeSlotDTO);

	List<TimeSlotDTO> getAllShifting();

	// To update shift details
	void updateShiftDetail(TimeSlotDTO timeSlotDTO);

	// To delete shift details
	void deleteShiftDetail(Long shifId);

	public void uploadFileForNewJoinee(MultipartFile mf, String candidateId);

	public void downloadAttachment(HttpServletResponse response, String fileName);

	ByteArrayOutputStream exportEmployeeData(String selectionStatus,
			String searchStr) throws IOException;
	ByteArrayOutputStream exportEmployeeCategoryData(String selectionDesignation) throws IOException;
	
	//for uploading candidates through excel sheet
	List<Map<String, String>> uploadBulkOfCandidates(MultipartFile mpf);
	
	List<EmployeeDTO> getAllHrData();
	
	//for reports page 
	public List<Object> getCandidatesListCount(String selectionStatus,String fromDate,String toDate);
	//for passport details
	void uploadPassportFrontCopy(MultipartFile file,String id);
	void uploadPassportBackCopy(MultipartFile file,String id);
	void uploadVisaDetailsCopy(MultipartFile file,String id);
	public VisaImageDTO getVisaImage(Long id);
	
	public PassportDTO getPassportFrontImage(Long id);
	public PassportBackPageDTO getPassportBackImage(Long id);
	List<AppraisalFormListDto> getLoggedInEmployeeReviews(Long employeeId);
	
	
	ByteArrayOutputStream exportInterviewStatusReport(String candidateStatus,String fromDate, String searchByCandidateName, String searchByMultipleFlag, String searchByRecruiterName,
    		 String searchBySourceName, String selectionStatus, String selectionTechnology, String toDate) throws IOException;
	
Map<String,Object> getAllCompanies(Integer startIndex, Integer endIndex,String searchCompany, String selectionTechnology);
	
	List<CompanyDTO> getAllCompaniesData();
	
	Map<String, Object>  getAllCandidatesInfo(Integer startIndex, Integer endIndex,String companyName);
	
	Map<String, Object>  getAllEmployeesInfo(Integer startIndex, Integer endIndex,String companyName);
	
	ByteArrayOutputStream exportCompaniesList( String companySearch, String selectionTechnology) throws Exception;

	ByteArrayOutputStream exportInterviewerDetails(String fromDate ,String toDate,String selectionStatus) throws IOException;
	
	ByteArrayOutputStream downloadScheduleCandidatesFile(String fromDate,String toDate,String skill) throws IOException;

	public StatusChart getStatusChart(String dateSelection,String from,String to);

	List<CountryLookUpDTO> getEmpCountries();

	List<VendorDto> getVendorDetails();

	void sendWhatsappScheduleInterviewNotificationtoCandidate(
			String to,String template, String templateParams);

	void sendRejectedMessagetoCandidate(Long candidateId);
	
	List<GenderDTO> getGenders();

	List<BloodGroupDTO> getBloodgroups();

	List<MaritalStatusDTO> getMaritalStatus();

	List<RelationsDTO> getRelations();

	List<QualificationCategoryDTO> getQualificationCatergory();

	List<EmploymentTypeDTO> getEmploymentType();

	List<JobTypeDTO> getJobType();
	
}
