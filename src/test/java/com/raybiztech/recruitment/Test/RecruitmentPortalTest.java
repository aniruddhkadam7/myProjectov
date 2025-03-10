/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.

package com.raybiztech.RecruitmentPortal;

import com.raybiztech.appraisals.builder.DesignationKrasBuilder;
import com.raybiztech.appraisals.builder.EmployeeBuilder;
import com.raybiztech.appraisals.builder.KPIBuilder;
import com.raybiztech.appraisals.builder.KPIRatingBuilder;
import com.raybiztech.appraisals.builder.KRABuilder;
import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.business.Status;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.lookup.business.SkillLookUp;
import com.raybiztech.recruitment.builder.AddressBuilder;
import com.raybiztech.recruitment.builder.CandidateBuilder;
import com.raybiztech.recruitment.builder.CandidateScheduleInterviewBuilder;
import com.raybiztech.recruitment.builder.DocumentBuilder;
import com.raybiztech.recruitment.builder.JobVacancyBuilder;
import com.raybiztech.recruitment.builder.PanelBuilder;
import com.raybiztech.recruitment.builder.ScheduleBuilder;
import com.raybiztech.recruitment.builder.SkillLookUpBuilder;
import com.raybiztech.recruitment.builder.SourceLookUpBuilder;
import com.raybiztech.recruitment.business.Candidate;
import com.raybiztech.recruitment.business.Interview;
import com.raybiztech.recruitment.business.InterviewType;
import com.raybiztech.recruitment.business.Panel;
import com.raybiztech.recruitment.business.PanelLevel;
import com.raybiztech.recruitment.business.Schedule;
import com.raybiztech.recruitment.dao.JobPortalDAO;
import com.raybiztech.recruitment.dto.CandidateDTO;
import com.raybiztech.recruitment.dto.CandidateScheduleDto;
import com.raybiztech.recruitment.dto.PanelDTO;
import com.raybiztech.recruitment.dto.ScheduledCadidateDTO;
import com.raybiztech.recruitment.dto.SkillLookUpDTO;
import com.raybiztech.recruitment.exception.JobPortalException;
import com.raybiztech.recruitment.service.JobPortalServiceImpl;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
    

*//**
 *
 * @author naresh
 *//*
        public class RecruitmentPortalTest {
//
//    @Test
//    public void getAllCandidate() throws JobPortalException {
//        CandidateBuilder candidateBuilder = new CandidateBuilder();
//        JobVacancyBuilder jobVacancyBuilder = new JobVacancyBuilder();
//        AddressBuilder addressBuilder = new AddressBuilder();
//        DocumentBuilder documentBuilder = new DocumentBuilder();
//        SourceLookUpBuilder sourceLookUpBuilder = new SourceLookUpBuilder();
//        candidateBuilder.setJobVacancyBuilder(jobVacancyBuilder);
//        candidateBuilder.setAddressBuilder(addressBuilder);
//        candidateBuilder.setDocumentBuilder(documentBuilder);
//        candidateBuilder.setSourceLookUpBuilder(sourceLookUpBuilder);
//        List<Candidate> candidateList = new ArrayList<Candidate>();
//        Candidate candidate = new Candidate();
//        candidate.setPersonId(1L);
//        candidate.setFirstName("naresh");
//        candidateList.add(candidate);
//        List<CandidateDTO> candidateDTOs = new ArrayList<CandidateDTO>();
//        CandidateDTO candidateDTO = new CandidateDTO();
//        candidateDTO.setPersonId(1l);
//        candidateDTO.setFirstName("naresh");
//        candidateDTOs.add(candidateDTO);
//        JobPortalServiceImpl jobPortalServiceImpl = new JobPortalServiceImpl();
//        DAO dao = Mockito.mock(DAO.class);
//        jobPortalServiceImpl.setDao(dao);
//        jobPortalServiceImpl.setCandidateBuilder(candidateBuilder);
//        Mockito.when(dao.get(Candidate.class)).thenReturn(candidateList);
//        List<CandidateDTO> candidateDTOList = jobPortalServiceImpl.getAllCandidate();
//        Assert.assertEquals(candidateDTOs.size(), candidateDTOList.size());
//    }
//
//    @Test
//    public void getAllEmployeeData() {
//        DesignationKrasBuilder designationKrasBuilder = new DesignationKrasBuilder();
//        KPIRatingBuilder kpiRatingBuilder = new KPIRatingBuilder();
//        KRABuilder kraBuilder = new KRABuilder();
//        KPIBuilder kpiBuilder = new KPIBuilder();
//        EmployeeBuilder employeeBuilder = new EmployeeBuilder();
//        employeeBuilder.setDesignationKrasBuilder(designationKrasBuilder);
//        designationKrasBuilder.setKraBuilder(kraBuilder);
//        kraBuilder.setKpiBuilder(kpiBuilder);
//        kpiBuilder.setKpiRatingBuilder(kpiRatingBuilder);
//        List<Employee> employeeList = new ArrayList<Employee>();
//        Employee employee = new Employee();
//        employee.setEmployeeId(1225l);
//        employee.setEmail("1291naresh@gmail.com");
//        employeeList.add(employee);
//
//        List<EmployeeDTO> employeeDTOs = new ArrayList<EmployeeDTO>();
//        EmployeeDTO employeeDTO = new EmployeeDTO();
//        employeeDTO.setId(1225l);
//        employeeDTO.setEmailId("1291naresh@gmail.com");
//        employeeDTOs.add(employeeDTO);
//
//        JobPortalServiceImpl jobPortalServiceImpl = new JobPortalServiceImpl();
//        JobPortalDAO jobPortalDAOImpl = Mockito.mock(JobPortalDAO.class);
//        jobPortalServiceImpl.setJobPortalDAOImpl(jobPortalDAOImpl);
//        jobPortalServiceImpl.setEmployeeBuilder(employeeBuilder);
//
//        Mockito.when(jobPortalDAOImpl.getAllEmployeeData()).thenReturn(employeeList);
//
//        List<EmployeeDTO> employeeDTOList = jobPortalServiceImpl.getAllEmployeeData();
//
//        Assert.assertEquals(employeeDTOs.size(), employeeDTOList.size());
//
//    }
//
//    @Test
//    public void searchCadidates() {
//
//        ScheduleBuilder scheduleBuilder = new ScheduleBuilder();
//
//        List<Schedule> schedules = new ArrayList<Schedule>();
//        Schedule schedule = new Schedule();
//        Set<Candidate> candidates = new HashSet<Candidate>();
//        Candidate candidate = new Candidate();
//        candidate.setPersonId(1L);
//        candidate.setFirstName("naresh");
//        candidates.add(candidate);
//        Set<String> skills = new HashSet<String>();
//        skills.add("java");
//        skills.add(".net");
//        candidate.setSkills(skills);
//        Interview interview = new Interview();
//        interview.setInterviewId(2l);
//        interview.setStatus(Status.NEW);
//        interview.setInterviewType(InterviewType.SYSTEM);
//        schedule.setInterview(interview);
//        schedule.getInterview().setCandidates(candidates);
//        schedule.setScheduleId(1l);
//        schedule.setScheduleTime("12:30PM");
//        schedules.add(schedule);
//
//        List<ScheduledCadidateDTO> cadidateDTOs = new ArrayList<ScheduledCadidateDTO>();
//
//        ScheduledCadidateDTO scheduledDTO = new ScheduledCadidateDTO();
//        scheduledDTO.setCandidateid(1l);
//        scheduledDTO.setTime("12:30PM");
//        cadidateDTOs.add(scheduledDTO);
//
//        JobPortalServiceImpl jobPortalServiceImpl = new JobPortalServiceImpl();
//        JobPortalDAO jobPortalDAOImpl = Mockito.mock(JobPortalDAO.class);
//        jobPortalServiceImpl.setJobPortalDAOImpl(jobPortalDAOImpl);
//        jobPortalServiceImpl.setScheduleBuilder(scheduleBuilder);
//
//        Mockito.when(jobPortalDAOImpl.searchCadidates("", "", "")).thenReturn(schedules);
//
//        List<ScheduledCadidateDTO> scheduledCadidateDTOs = jobPortalServiceImpl.searchCadidates("", "", "");
//
//        Assert.assertEquals(cadidateDTOs.size(), scheduledCadidateDTOs.size());
//
//    }
//
//    @Test
//    public void getAllSkillsLookUp() {
//        SkillLookUpBuilder skillLookUpBuilder = new SkillLookUpBuilder();
//
//        List<SkillLookUp> skillLookUps = new ArrayList<SkillLookUp>();
//        SkillLookUp skillLookUp = new SkillLookUp();
//        skillLookUp.setId(1l);
//        skillLookUp.setName("java");
//        skillLookUps.add(skillLookUp);
//
//        List<SkillLookUpDTO> skillLookUpDTOs = new ArrayList<SkillLookUpDTO>();
//        SkillLookUpDTO lookUpDTO = new SkillLookUpDTO();
//        lookUpDTO.setId(1l);
//        lookUpDTO.setName("java");
//        skillLookUpDTOs.add(lookUpDTO);
//
//        JobPortalServiceImpl jobPortalServiceImpl = new JobPortalServiceImpl();
//        JobPortalDAO jobPortalDAOImpl = Mockito.mock(JobPortalDAO.class);
//        jobPortalServiceImpl.setJobPortalDAOImpl(jobPortalDAOImpl);
//        jobPortalServiceImpl.setSkillLookUpBuilder(skillLookUpBuilder);
//
//        Mockito.when(jobPortalDAOImpl.getAllSkillsLookUp()).thenReturn(skillLookUps);
//
//        List<SkillLookUpDTO> skillLookUpDTOList = jobPortalServiceImpl.getAllSkillsLookUp();
//
//        Assert.assertEquals(skillLookUpDTOs.size(), skillLookUpDTOList.size());
//
//    }
//
//    @Test
//    public void getAllnotScheduleCadidates() throws JobPortalException {
//        CandidateBuilder candidateBuilder = new CandidateBuilder();
//        JobVacancyBuilder jobVacancyBuilder = new JobVacancyBuilder();
//        AddressBuilder addressBuilder = new AddressBuilder();
//        DocumentBuilder documentBuilder = new DocumentBuilder();
//        SourceLookUpBuilder sourceLookUpBuilder = new SourceLookUpBuilder();
//        candidateBuilder.setJobVacancyBuilder(jobVacancyBuilder);
//        candidateBuilder.setAddressBuilder(addressBuilder);
//        candidateBuilder.setDocumentBuilder(documentBuilder);
//        candidateBuilder.setSourceLookUpBuilder(sourceLookUpBuilder);
//        List<Candidate> candidateList = new ArrayList<Candidate>();
//        Candidate candidate = new Candidate();
//        Set<String> skills = new HashSet<String>();
//        skills.add("java");
//        skills.add(".net");
//        candidate.setPersonId(1L);
//        candidate.setFirstName("naresh");
//        candidate.setSkills(skills);
//        candidateList.add(candidate);
//        List<CandidateDTO> candidateDTOs = new ArrayList<CandidateDTO>();
//        CandidateDTO candidateDTO = new CandidateDTO();
//        candidateDTO.setPersonId(1l);
//        candidateDTO.setFirstName("naresh");
//        candidateDTOs.add(candidateDTO);
//        JobPortalServiceImpl jobPortalServiceImpl = new JobPortalServiceImpl();
//        DAO dao = Mockito.mock(DAO.class);
//        jobPortalServiceImpl.setDao(dao);
//        jobPortalServiceImpl.setCandidateBuilder(candidateBuilder);
//        Mockito.when(dao.get(Candidate.class)).thenReturn(candidateList);
//        List<CandidateDTO> candidateDTOList = jobPortalServiceImpl.getAllnotScheduleCadidates();
//        Assert.assertEquals(candidateDTOs.size(), candidateDTOList.size());
//    }
//
//    @Test
//    public void editCandidate() {
//        CandidateScheduleDto candidateScheduleDto = new CandidateScheduleDto();
//        candidateScheduleDto.setCandidateId(1l);
//        candidateScheduleDto.setCandidateName("naresh");
//        CandidateBuilder candidateBuilder = new CandidateBuilder();
//        Candidate candidate = new Candidate();
//        Set<String> skills = new HashSet<String>();
//        skills.add("java");
//        skills.add(".net");
//        candidate.setPersonId(1L);
//        candidate.setFirstName("naresh");
//        candidate.setSkills(skills);
//        JobPortalServiceImpl jobPortalServiceImpl = new JobPortalServiceImpl();
//        DAO dao = Mockito.mock(DAO.class);
//        jobPortalServiceImpl.setDao(dao);
//        jobPortalServiceImpl.setCandidateBuilder(candidateBuilder);
//
//        Mockito.when(dao.findBy(Candidate.class, 1l)).thenReturn(candidate);
//
//        CandidateScheduleDto candidateScheduleDto1 = jobPortalServiceImpl.editCandidate("1");
//
//        Assert.assertEquals(candidateScheduleDto.getCandidateName(), candidateScheduleDto1.getCandidateName());
//
//    }
//
//    @Test
//    public void getAllEmployeePanelData() {
//
//        List<Panel> panelList = new ArrayList<Panel>();
//        Panel panel = new Panel();
//        panel.setPanelId(1l);
//        panel.setPanelLevel(PanelLevel.TECHNICAL);
//
//        List<PanelDTO> panelDTOs = new ArrayList<PanelDTO>();
//        PanelDTO panelDTO = new PanelDTO();
//        panelDTO.setPanelId(1l);
//
//        PanelBuilder panelBuilder = new PanelBuilder();
//        JobPortalServiceImpl jobPortalServiceImpl = new JobPortalServiceImpl();
//        DAO dao = Mockito.mock(DAO.class);
//        jobPortalServiceImpl.setDao(dao);
//        jobPortalServiceImpl.setPanelBuilder(panelBuilder);
//        Mockito.when(dao.get(Panel.class)).thenReturn(panelList);
//
//        List<PanelDTO> panelData = jobPortalServiceImpl.getAllEmployeePanelData();
//        Assert.assertEquals(panelDTOs.size(), panelData.size());
//
//    }
//
//    @Test
//    public void addEmployeeToPanel() {
//
//        Employee employee = new Employee();
//        employee.setEmployeeId(1225l);
//        Panel panel1 = new Panel();
//        Set<Employee> employeeSet = new HashSet<Employee>();
//        employeeSet.add(employee);
//        panel1.setEmployeeSet(employeeSet);
//
//        List<Panel> panelList = new ArrayList<Panel>();
//        Panel panel = new Panel();
//        panel.setPanelId(1l);
//        panel.setPanelLevel(PanelLevel.TECHNICAL);
//
//        List<PanelDTO> panelDTOs = new ArrayList<PanelDTO>();
//        PanelDTO panelDTO = new PanelDTO();
//        panelDTO.setPanelId(1l);
//
//        PanelBuilder panelBuilder = new PanelBuilder();
//        JobPortalServiceImpl jobPortalServiceImpl = new JobPortalServiceImpl();
//        DAO dao = Mockito.mock(DAO.class);
//        jobPortalServiceImpl.setDao(dao);
//        jobPortalServiceImpl.setPanelBuilder(panelBuilder);
//        Mockito.when(dao.findBy(Employee.class, 1l)).thenReturn(employee);
//
//        Mockito.when(dao.get(Panel.class)).thenReturn(panelList);
//
//        List<PanelDTO> panelData = jobPortalServiceImpl.getAllEmployeePanelData();
//        Assert.assertEquals(panelDTOs.size(), panelData.size());
//
//    }
//
//    // @Test
//    public void scheduleNewCadidate() {
//        CandidateScheduleDto candidateScheduleDto = new CandidateScheduleDto();
//        candidateScheduleDto.setCandidateId(1l);
//        candidateScheduleDto.setScheduleFlag("yes");
//        candidateScheduleDto.setCandidateName("naresh");
//        Candidate candidate = new Candidate();
//        Set<String> skills = new HashSet<String>();
//        skills.add("java");
//        skills.add(".net");
//        candidate.setPersonId(1L);
//        candidate.setFirstName("naresh");
//        candidate.setSkills(skills);
//        EmployeeBuilder employeeBuilder = new EmployeeBuilder();
//        CandidateScheduleInterviewBuilder candidateScheduleInterviewBuilder = new CandidateScheduleInterviewBuilder();
//        candidateScheduleInterviewBuilder.setEmployeeBuilder(employeeBuilder);
//        JobPortalServiceImpl jobPortalServiceImpl = new JobPortalServiceImpl();
//        DAO dao = Mockito.mock(DAO.class);
//        jobPortalServiceImpl.setDao(dao);
//        jobPortalServiceImpl.setCandidateScheduleInterviewBuilder(candidateScheduleInterviewBuilder);
//        Mockito.when(dao.findBy(Candidate.class, 1l)).thenReturn(candidate);
//        jobPortalServiceImpl.scheduleNewCadidate(candidateScheduleDto);
//
//    }
//
//    @Test
//    public void updateCandidadate() {
//        Candidate candidate = new Candidate();
//        Set<String> skills = new HashSet<String>();
//        skills.add("java");
//        skills.add(".net");
//        candidate.setPersonId(1L);
//        candidate.setFirstName("naresh");
//        candidate.setSkills(skills);
//        EmployeeBuilder employeeBuilder = new EmployeeBuilder();
//        CandidateScheduleInterviewBuilder candidateScheduleInterviewBuilder = new CandidateScheduleInterviewBuilder();
//        candidateScheduleInterviewBuilder.setEmployeeBuilder(employeeBuilder);
//        JobPortalServiceImpl jobPortalServiceImpl = new JobPortalServiceImpl();
//        DAO dao = Mockito.mock(DAO.class);
//        jobPortalServiceImpl.setDao(dao);
//        jobPortalServiceImpl.setCandidateScheduleInterviewBuilder(candidateScheduleInterviewBuilder);
//        Mockito.when(dao.findBy(Candidate.class, 1l)).thenReturn(candidate);
//
//    }

            }
*/
