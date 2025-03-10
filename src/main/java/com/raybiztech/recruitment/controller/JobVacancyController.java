package com.raybiztech.recruitment.controller;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.recruitment.dto.JobVacancyAuditDto;
import com.raybiztech.recruitment.dto.JobVacancyDTO;
import com.raybiztech.recruitment.dto.VacancyLookUpDTO;
import com.raybiztech.recruitment.exception.JobPortalException;
import com.raybiztech.recruitment.service.JobVacancyService;

@Controller
@RequestMapping("/jobvacancy")
public class JobVacancyController {

    @Autowired
    JobVacancyService jobVacancyServieImpl;
    Logger logger = Logger.getLogger(JobVacancyController.class);

    @RequestMapping(value = "/jobAdmin/addJobVacancy", method = RequestMethod.POST)
    public @ResponseBody void addJobVacancy(@RequestBody JobVacancyDTO jobVacancyDTO)
            throws JobPortalException {
        jobVacancyServieImpl.addJobVacancy(jobVacancyDTO);
    }
    
     @RequestMapping(value = "/jobAdmin/updateJobVacancy", method = RequestMethod.POST)
    public @ResponseBody void updateJobVacancy(@RequestBody JobVacancyDTO jobVacancyDTO)
            throws JobPortalException {
        jobVacancyServieImpl.updateJobVacancy(jobVacancyDTO);
    }

    @RequestMapping(value = "/getAllJobVacancies",params={"startIndex","endIndex","status","searchJobTitle"},method = RequestMethod.GET)
    public @ResponseBody Map<String, Object> getOpenJobVacancies(@RequestParam Integer startIndex,
    		@RequestParam Integer endIndex,@RequestParam String status,@RequestParam String searchJobTitle)
            throws JobPortalException {
        return jobVacancyServieImpl.getOpenJobVacancies(startIndex,endIndex,status,searchJobTitle);
    }
//    @RequestMapping(value = "/getSearchJobVacancy",params={"searchJobTitle"}, method = RequestMethod.GET)
//    public @ResponseBody
//    List<JobVacancyDTO> getAllJobVacancies(@RequestParam String searchJobTitle )
//            throws JobPortalException {
//        return jobVacancyServieImpl.getAllJobVacancies(searchJobTitle);
//    }


    @RequestMapping(value = "/jobAdmin/deleteJobVacancy", params = {"jobvacancyId"}, method = RequestMethod.POST)
    public @ResponseBody void deleteJobVacancy(@RequestParam String jobvacancyId) {
        jobVacancyServieImpl.deleteJobVacancy(Long.parseLong(jobvacancyId));
    }

    @RequestMapping(value = "/jobAdmin/getEditJobvacancy", params = {"id"}, method = RequestMethod.GET)
    public JobVacancyDTO getEditJobvacancy(@RequestParam Long id) {
        return jobVacancyServieImpl.getEditJobvacancy(id);
    }

    @RequestMapping(value = "/getAllVacancyLookup", method = RequestMethod.GET)
    public @ResponseBody
    List<VacancyLookUpDTO> getAllVacancyLookUp() {
        return jobVacancyServieImpl.getAllVacancyLookUp();
    }

    @RequestMapping(value = "/jobvacantcheck", params = {"vacantposition"}, method = RequestMethod.GET)
    public @ResponseBody
    Boolean jobVacantAlreadyCheck(@RequestParam String vacantposition) {
        return jobVacancyServieImpl.jobVacantAlreadyCheck(vacantposition);

    }

    @RequestMapping(value = "/jobAdmin/addPositionVacantName", params = {"positionVacantName","jobCode"}, method = RequestMethod.GET)
    public @ResponseBody
    List<VacancyLookUpDTO> addPositionVacantName(@RequestParam String positionVacantName,String jobCode) {
        return jobVacancyServieImpl.addPositionVacantName(positionVacantName,jobCode);

    }

    @RequestMapping(value = "/jobAdmin/deletePositionVacancy", params = {"positionvacancyLookUpId"}, method = RequestMethod.POST)
    public @ResponseBody void deletePositionVacancy(@RequestParam String positionvacancyLookUpId) {
        jobVacancyServieImpl.deletePositionVacancy(Long.parseLong(positionvacancyLookUpId));
    }
    
    @RequestMapping(value="/getJobVacancyAudit",params={"jobVacancyId"},method=RequestMethod.GET)
    public @ResponseBody Map<String,Object> getjobVacancyAudit(@RequestParam Long jobVacancyId)
    {
    	return jobVacancyServieImpl.getJobVacancyAudit(jobVacancyId);
    }
    
    

}
