package com.raybiztech.appraisals.manager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.dto.AppraisalDTO;
import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.appraisals.manager.service.ManagerAppraisalService;

@Controller
@RequestMapping("/managerAppraisalController")
public class ManagerAppraisalController {
	@Autowired
	private ManagerAppraisalService managerAppraisalService;

	@RequestMapping(value = "/getEmployees", params = { "managerEmployeeId" }, method = RequestMethod.GET)
	public @ResponseBody
	List<EmployeeDTO> getEmployees(Long managerEmployeeId) {
		return managerAppraisalService.getEmployees(managerEmployeeId);
	}

	@RequestMapping(value = "/saveManagerRating", method = RequestMethod.POST)
	public @ResponseBody
	void saveManagerRating(@RequestBody AppraisalDTO appraisalDto) {
		managerAppraisalService.saveManagerRating(appraisalDto);

	}

	@RequestMapping(value = "/saveAppraisalStatusForManager", method = RequestMethod.POST)
	public @ResponseBody
	void saveAppraisalStatusForManager(@RequestBody AppraisalDTO appraisalDTO) {
		managerAppraisalService.saveManagerStatus(appraisalDTO);
	}
	
	@RequestMapping(value = "/submitManagerAppraisal", params = { "employeeId" }, method = RequestMethod.POST)
    public @ResponseBody void submitManagerAppraisal(Long employeeId) {
        managerAppraisalService.submitManagerAppraisal(employeeId);
    }

}