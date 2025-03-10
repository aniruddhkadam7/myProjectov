package com.raybiztech.delegation.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.dto.EmployeeDTO;
import com.raybiztech.delegation.dto.DelegationDTO;
import com.raybiztech.delegation.service.DelegationService;

@Controller
@RequestMapping("/delegation")
public class DelegationController {

	@Autowired
	DelegationService delegationServiceImpl;

	@RequestMapping(value = "/updateReportingManager", method = RequestMethod.POST)
	public @ResponseBody void updateReportingManager(@RequestBody DelegationDTO delegationDTO) {
		delegationServiceImpl.updateReportingManager(delegationDTO);
	}

	@RequestMapping(value = "/getMangerUnderEmployees", method = RequestMethod.GET, params = { "managerId" })
	public @ResponseBody List<EmployeeDTO> getMangerUnderEmployees(@RequestParam("managerId") Long mangerId) {
		return delegationServiceImpl.getMangerUnderEmployees(mangerId);

	}

	@RequestMapping(value = "/getAllReportingManagerData", method = RequestMethod.GET)
	public @ResponseBody List<EmployeeDTO> getAllReportingManagerData() {
		return delegationServiceImpl.getAllReportingManagerData();

	}

	@RequestMapping(value = "/getroleTypeMangers", method = RequestMethod.GET, params = { "role" })
	public @ResponseBody List<EmployeeDTO> getroleTypeMangers(@RequestParam String role) {
		return delegationServiceImpl.getroleTypeMangers(role);

	}

	@RequestMapping(value = "/getAllHRList", method = RequestMethod.GET)
	public @ResponseBody List<EmployeeDTO> getAllHRList() {
		return delegationServiceImpl.getAllHRList();

	}

	@RequestMapping(value = "/getHrAssociates", method = RequestMethod.GET, params = { "hrId" })
	public @ResponseBody List<EmployeeDTO> getHrAssociates(@RequestParam("hrId") Long hrId) {
		return delegationServiceImpl.getHrAssociates(hrId);

	}

	@RequestMapping(value = "/updateHrAssociates", method = RequestMethod.POST)
	public @ResponseBody void updateHrAssociates(@RequestBody DelegationDTO delegationDTO) {
		delegationServiceImpl.updateHrAssociates(delegationDTO);
	}
}
