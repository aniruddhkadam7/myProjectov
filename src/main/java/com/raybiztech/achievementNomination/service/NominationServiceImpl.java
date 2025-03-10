package com.raybiztech.achievementNomination.service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.achievement.dto.AchievementDTO;
import com.raybiztech.achievement.service.AchievementService;
import com.raybiztech.achievementNomination.builder.InitiateNominationBuilder;
import com.raybiztech.achievementNomination.builder.NominationBuilder;
import com.raybiztech.achievementNomination.builder.NominationCycleBuilder;
import com.raybiztech.achievementNomination.builder.NominationQuestionBuilder;
import com.raybiztech.achievementNomination.business.InitiateNomination;
import com.raybiztech.achievementNomination.business.Nomination;
import com.raybiztech.achievementNomination.business.NominationCycle;
import com.raybiztech.achievementNomination.business.NominationQuestion;
import com.raybiztech.achievementNomination.dao.NominationDao;
import com.raybiztech.achievementNomination.dto.InitiateNominationDto;
import com.raybiztech.achievementNomination.dto.NominationCycleDto;
import com.raybiztech.achievementNomination.dto.NominationDto;
import com.raybiztech.achievementNomination.dto.NominationQuestionDto;
import com.raybiztech.achievementNomination.util.NomineeAlreadyAddedException;
import com.raybiztech.appraisals.business.Cycle;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.exceptions.CycleNotActiveException;
import com.raybiztech.appraisals.exceptions.DuplicateCycleNameException;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Date;
import com.raybiztech.mailtemplates.util.PerformanceManagementMailConfiguration;
import com.raybiztech.projectmanagement.service.ProjectService;
import com.raybiztech.rolefeature.business.Permission;

/**
 *
 * @author Aprajita
 */

@Service("nominationServiceImpl")
@Transactional
public class NominationServiceImpl implements NominationService {

	Logger logger = Logger.getLogger(NominationServiceImpl.class);

	@Autowired
	SecurityUtils securityUtils;

	@Autowired
	NominationQuestionBuilder nominationQuestionBuilder;

	@Autowired
	NominationCycleBuilder nominationCycleBuilder;

	@Autowired
	NominationDao nominationDaoImpl;

	@Autowired
	InitiateNominationBuilder initiateNominationBuilder;

	@Autowired
	NominationBuilder nominationBuilder;

	@Autowired
	PerformanceManagementMailConfiguration performanceManagementMailConfiguration;

	@Autowired
	ProjectService projectService;
	
	@Autowired
	AchievementService achievementServiceImpl;

	/* add nomination question */
	@Override
	public void addQuestion(NominationQuestionDto nominationQuestionDto) {

		NominationQuestion nominationQuestion = nominationQuestionBuilder
				.convertNominationQuestionDtoToEntity(nominationQuestionDto);
		nominationDaoImpl.save(nominationQuestion);

	}

	/* To get all question List */
	@Override
	@SuppressWarnings("unchecked")
	public Map<String, Object> getAllQuestions() {

		Map<String, Object> questionMap = nominationDaoImpl.getAllQuestions();
		List<NominationQuestion> questionList = (List<NominationQuestion>) questionMap
				.get("list");

		Integer noOfRecords = (Integer) questionMap.get("size");
		Set<NominationQuestionDto> questionDtoList = nominationQuestionBuilder
				.convetTODtoList(new HashSet<NominationQuestion>(questionList));
		Map<String, Object> questionListMap = new HashMap<String, Object>();
		questionListMap.put("list", questionDtoList);
		questionListMap.put("size", noOfRecords);

		return questionListMap;
	}

	/* To delete question */
	@Override
	public void deleteQuestion(Long questionId) throws Exception {
		
		nominationDaoImpl.delete(nominationDaoImpl.findBy(
				NominationQuestion.class, questionId));
		
	}

	/* To add nomination cycle */
	@Override
	public void addCycle(NominationCycleDto nominationCycleDto) {
		Date startDate = null;
		Date enddate = null;
		try {
			startDate = Date
					.parse(nominationCycleDto.getFromMonth(), "MM/yyyy");
			enddate = Date.parse(nominationCycleDto.getToMonth(), "MM/yyyy");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Nomination> nominations = nominationDaoImpl.getDuplicateDate(
				startDate, enddate, nominationCycleDto.getId());
		if (nominations.size() >= 1) {
			throw new DuplicateCycleNameException();
		} else {
			NominationCycle cycle = nominationCycleBuilder
					.convertNominationCycleDtoToEntity(nominationCycleDto);
			if (nominationCycleDto.getActivateFlag() == true) {
				NominationCycle nominationdetails = nominationDaoImpl
						.getActiveCycle();

				if (nominationdetails != null) {
					nominationdetails.setActivateFlag(false);
					nominationDaoImpl.saveOrUpdate(nominationdetails);
				}

				nominationDaoImpl.save(cycle);
			} else {
				nominationDaoImpl.save(cycle);
			}
		}

	}

	/* To get all cycle List */
	@Override
	public Map<String, Object> getAllCycles() {
		Map<String, Object> allCyclesMap = null;
		allCyclesMap = nominationDaoImpl.getAllCycles();
		List<NominationCycle> nominationCycles = (List<NominationCycle>) allCyclesMap
				.get("list");
		Integer noOfRecords = (Integer) allCyclesMap.get("size");
		List<NominationCycleDto> nominationCycleList = nominationCycleBuilder
				.convertToDtoList(nominationCycles);
		Map<String, Object> allCyclesListMap = new HashMap<String, Object>();
		allCyclesListMap.put("list", nominationCycleList);
		allCyclesListMap.put("size", noOfRecords);

		return allCyclesListMap;
	}

	/* To delete cycle */
	@Override
	public void deleteCycle(Long cycleId) {
		NominationCycle cycle = nominationDaoImpl.findBy(NominationCycle.class,
				cycleId);
		if (!cycle.getActivateFlag())
			nominationDaoImpl.delete(cycle);
	}

	/* To get active cycle */
	@Override
	public InitiateNominationDto getActiveCycleData() {
		NominationCycle nominationCycle = nominationDaoImpl.getActiveCycle();
		if(nominationCycle==null)
			throw new CycleNotActiveException();
		InitiateNomination initiateNomination = nominationDaoImpl
				.getAllQuestionUnderCycle(nominationCycle.getId());
		InitiateNominationDto dto = new InitiateNominationDto();

		// for 1st time when no mapping are there.
		if (initiateNomination == null) {
			dto.setNominationCycleDto(nominationCycleBuilder
					.convertNominationCycleToDto(nominationCycle));
		} else {
			dto = initiateNominationBuilder
					.concertEntityToDto(initiateNomination);
		}

		return dto;
	}

	/* Get particular cycle For Edit */
	@Override
	public NominationCycleDto editCycle(Long cycleId) {
		NominationCycle cycle = nominationDaoImpl.findBy(NominationCycle.class,
				cycleId);
		NominationCycleDto cycleDto = nominationCycleBuilder
				.convertNominationCycleToDto(cycle);
		return cycleDto;
	}

	/* Update Cycle */
	@Override
	public void updateCycle(NominationCycleDto nominationCycleDto) {

		Date startDate = null;
		Date enddate = null;
		try {
			startDate = Date
					.parse(nominationCycleDto.getFromMonth(), "MM/yyyy");
			enddate = Date.parse(nominationCycleDto.getToMonth(), "MM/yyyy");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		List<Nomination> nominations = nominationDaoImpl.getDuplicateDate(
				startDate, enddate, nominationCycleDto.getId());
		if (nominations.size() >= 1) {
			throw new DuplicateCycleNameException();
		} else {
			if (nominationCycleDto.getActivateFlag() == true) {
				NominationCycle nominationdetails = nominationDaoImpl
						.getActiveCycle();

				if (nominationdetails != null) {
					if (!(nominationdetails.getId().toString()
							.equalsIgnoreCase(nominationCycleDto.getId()
									.toString()))) {
						nominationdetails.setActivateFlag(false);
						nominationDaoImpl.saveOrUpdate(nominationdetails);
					}
				}

			}
			NominationCycle nominationCycle = nominationCycleBuilder
					.convertdtoToEditEntity(nominationCycleDto);
			nominationDaoImpl.update(nominationCycle);

		}

	}

	@Override
	public void initiateCycle(InitiateNominationDto initiateCycleDto) {

		InitiateNomination initiateNominations = nominationDaoImpl
				.getAllQuestionUnderCycle(initiateCycleDto
						.getNominationCycleDto().getId());
		NominationCycle nominationCycle = nominationDaoImpl.findBy(
				NominationCycle.class, initiateCycleDto.getNominationCycleDto()
						.getId());
		Set<NominationQuestion> nominationQuestionsSet = new HashSet<NominationQuestion>();
		for (NominationQuestionDto nominationQuestionsDto : initiateCycleDto
				.getNominationQuestionDto()) {
			nominationQuestionsSet.add(nominationDaoImpl.findBy(
					NominationQuestion.class, nominationQuestionsDto.getId()));
		}

		if (initiateNominations == null) {
			InitiateNomination initiateNomination = new InitiateNomination();
			initiateNomination.setNominationCycle(nominationCycle);
			initiateNomination.setNominationQuestions(nominationQuestionsSet);
			nominationDaoImpl.save(initiateNomination);
		} else {
			initiateNominations.setNominationQuestions(nominationQuestionsSet);
			nominationDaoImpl.saveOrUpdate(initiateNominations);
		}

	}

	@Override
	public NominationDto getFormDetails() {
		NominationCycle nominationCycle = nominationDaoImpl.getActiveCycle();
		InitiateNomination nominationDetails = nominationDaoImpl
				.getNominationFormDetailsOfActiveCycle(nominationCycle);
		return initiateNominationBuilder.buildNominationForm(nominationDetails);
	}

	@Override
	public void addNominee(NominationDto dto) {

		NominationCycle cycle = nominationDaoImpl.findBy(NominationCycle.class,
				dto.getCycleID());
		Employee employee = nominationDaoImpl.findBy(Employee.class,
				dto.getEmployeeId());

		if (!nominationDaoImpl.checkNomineeAlreadyAdded(cycle, employee)) {
			Nomination nomination = nominationBuilder.toEntity(dto);
			nominationDaoImpl.save(nomination);
			performanceManagementMailConfiguration
					.sendNominationMailNotification(nomination);

		} else {
			throw new NomineeAlreadyAddedException();
		}

	}

	@Override
	public List<NominationDto> getNominations(Long cycleId) {

		Employee loggedEmployee = nominationDaoImpl.findBy(Employee.class,
				securityUtils.getLoggedEmployeeIdforSecurityContextHolder());

		NominationCycle cycle = nominationDaoImpl.findBy(NominationCycle.class,
				cycleId);

		Permission nomineeList = nominationDaoImpl.checkForPermission(
				"Nominee List", loggedEmployee);

		Permission hierarchyList = nominationDaoImpl.checkForPermission(
				"Hierarchy Nominee List", loggedEmployee);
		
		
		
		List<Nomination> nominations = null;
		if (nomineeList.getView() && hierarchyList.getView()) {

			List<Long> managersList = projectService
					.mangerUnderManager(loggedEmployee.getEmployeeId());

			nominations = nominationDaoImpl.getHierarcalNomintions(cycle,
					managersList);

		} else if (nomineeList.getView() && !hierarchyList.getView()) {
			nominations = nominationDaoImpl.getNominations(cycle);
		}

		List<NominationDto> dtos = null;
		if (nominations != null) {
			dtos = new ArrayList<NominationDto>();
			for (Nomination nomination : nominations) {
				NominationDto dto = new NominationDto();
				dto.setId(nomination.getId());
				dto.setEmployeeName(nomination.getEmployee().getFullName());
				dto.setAchievementType(nomination.getAchievementType()
						.getAchievementType());
				dto.setFromMonth(nomination.getNominationCycleId()
						.getFromMonth().toString("MM/yyyy"));
				dto.setToMonth(nomination.getNominationCycleId().getToMonth()
						.toString("MM/yyyy"));
				dto.setRating(nomination.getRating());
				dto.setNominationStatus((nomination.getNominationStatus() != null) ? nomination
						.getNominationStatus() : "N/A");
				Employee createdEmployee = nominationDaoImpl.findBy(
						Employee.class, nomination.getCreatedBy());
				dto.setCreatedBy(createdEmployee.getFullName());
				dtos.add(dto);
			}
		}
		return dtos;
	}

	@Override
	public NominationDto getNominationDetails(Long nominationId) {
		return nominationBuilder.toDto(nominationDaoImpl.findBy(
				Nomination.class, nominationId));
	}

	@Override
	public void updateNominee(NominationDto dto) {
		Nomination nomination = nominationDaoImpl.findBy(Nomination.class,
				dto.getId());
		nomination.setRating(dto.getRating());
		nomination.setFinalComments(dto.getFinalComments());
		nomination.setNominationStatus(dto.getNominationStatus());
		nominationDaoImpl.update(nomination);
		
		if(dto.getNominationStatus().equalsIgnoreCase("SELECTED")) {
			
			AchievementDTO  achievementDTO = new AchievementDTO();
			achievementDTO.setAchievementTypeId(dto.getAchievementTypeId());
			achievementDTO.setAchievementType(dto.getAchievementType());
			achievementDTO.setDescription(dto.getFinalComments());
			achievementDTO.setEmployeeId(dto.getEmployeeId());
			achievementDTO.setEmployeeName(dto.getEmployeeName());
			achievementDTO.setStartDate(dto.getFromMonth());
			achievementDTO.setEndDate(dto.getToMonth());
			achievementServiceImpl.addAchievement(achievementDTO);
		
		}
		

	}

	@Override
	public ByteArrayOutputStream exportNomineesListData(Long cycle)throws IOException {
		Employee loggedEmployee = nominationDaoImpl.findBy(Employee.class,
				securityUtils.getLoggedEmployeeIdforSecurityContextHolder());

		NominationCycle cycle1 = nominationDaoImpl.findBy(NominationCycle.class,
				cycle);

		Permission nomineeList = nominationDaoImpl.checkForPermission(
				"Nominee List", loggedEmployee);

		Permission hierarchyList = nominationDaoImpl.checkForPermission(
				"Hierarchy Nominee List", loggedEmployee);
		
		
		
		List<Nomination> nominations = null;
		if (nomineeList.getView() && hierarchyList.getView()) {

			List<Long> managersList = projectService
					.mangerUnderManager(loggedEmployee.getEmployeeId());

			nominations = nominationDaoImpl.getHierarcalNomintions(cycle1,
					managersList);

		} else if (nomineeList.getView() && !hierarchyList.getView()) {
			nominations = nominationDaoImpl.getNominations(cycle1);
		}
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		int rowindex=1;
		Workbook workbook = new HSSFWorkbook();
		Sheet sheet = workbook.createSheet();
		Row row1 = sheet.createRow(0);
		Font font = workbook.createFont();
		font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		font.setFontHeightInPoints((short) 10);
		CellStyle style=workbook.createCellStyle();
		
		style.setAlignment(CellStyle.ALIGN_CENTER);
		style.setFont(font);
		
		Cell cell0 = row1.createCell(0);
		cell0.setCellStyle(style);
		cell0.setCellValue("Nominee Name");
		
		Cell cell1 = row1.createCell(1);
		cell1.setCellStyle(style);
		cell1.setCellValue("Achievement Type");
		
		Cell cell2 = row1.createCell(2);
		cell2.setCellStyle(style);
		cell2.setCellValue("From Month");
		
		Cell cell3 = row1.createCell(3);
		cell3.setCellStyle(style);
		cell3.setCellValue("To Month");
		
		Cell cell4 = row1.createCell(4);
		cell4.setCellStyle(style);
		cell4.setCellValue("Added By");
		
		Cell cell5 = row1.createCell(5);
		cell5.setCellStyle(style);
		cell5.setCellValue("Status");
		
		for(Nomination obj:nominations){

			Row row = sheet.createRow(rowindex++);
			
			Cell cel0 = row.createCell(0);
			cel0.setCellValue(obj.getEmployee().getFullName());
			
			Cell cel1 = row.createCell(1);
			cel1.setCellValue(obj.getAchievementType().getAchievementType());
			
			Cell cel2 = row.createCell(2);
			cel2.setCellValue(obj.getNominationCycleId().getFromMonth().toString());
			
			Cell cel3 = row.createCell(3);
			cel3.setCellValue(obj.getNominationCycleId().getToMonth().toString());
			
			Cell cel4 = row.createCell(4);
			Employee employee=nominationDaoImpl.findBy(Employee.class, obj.getCreatedBy());
			cel4.setCellValue(employee.getFullName());
			
			Cell cel5 = row.createCell(5);
			if(obj.getNominationStatus() == null){
				cel5.setCellValue("N/A");
			}
			else{
			cel5.setCellValue(obj.getNominationStatus());
			}
			
			sheet.autoSizeColumn(0);
			sheet.autoSizeColumn(1);
			sheet.autoSizeColumn(2);
			sheet.autoSizeColumn(3);
			sheet.autoSizeColumn(4);
			sheet.autoSizeColumn(5);
		}
		workbook.write(bos);
		bos.flush();
		bos.close();
		
		return bos;
	}
}
