package com.raybiztech.appraisals.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.dto.CycleDTO;
import com.raybiztech.appraisals.service.ManageAppraisalService;

@Controller
@RequestMapping("/appraisalController")
public class ManageAppraisalController {

	@Autowired
	ManageAppraisalService manageAppraisalService;
	
	Logger logger = Logger.getLogger(ManageAppraisalController.class);
	
	@RequestMapping(value = "/addCycle", method = RequestMethod.POST)
    public @ResponseBody
    void addCycle(@RequestBody CycleDTO cycleDTO) {
        logger.info("Cycle data is in controller: " +cycleDTO);
        manageAppraisalService.addCycle(cycleDTO);
    }

	@RequestMapping(value = "/getAllCycles", method = RequestMethod.GET)
	public @ResponseBody
	List<CycleDTO> getAllCycles() {
		return manageAppraisalService.getAllCycles();

	}
	
	@RequestMapping(value = "/changeCycleStatus", params = { "cycleId" , "status"}, method = RequestMethod.POST)
    public @ResponseBody void changeCycleStatus(Long cycleId, String status) {
	    logger.info("cycleid in controller : "+cycleId+" status : "+status);
        manageAppraisalService.changeCycleStatus(cycleId, status);
    }

}
