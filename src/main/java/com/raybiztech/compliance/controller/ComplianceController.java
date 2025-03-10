package com.raybiztech.compliance.controller;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.compliance.builder.ComplianceBuilder;
import com.raybiztech.compliance.business.ComplianceTask;
import com.raybiztech.compliance.dao.ComplianceDao;
import com.raybiztech.compliance.dto.ComplianceDTO;
import com.raybiztech.compliance.dto.ComplianceTaskSubmitDTO;
import com.raybiztech.compliance.dto.ComplianceTaskDTO;
import com.raybiztech.compliance.service.ComplianceService;
import com.raybiztech.mailtemplates.util.ComplianceMailConfig;

@Controller
@RequestMapping("/compliance")
public class ComplianceController {

	@Autowired
	ComplianceService complianceServiceImpl;
	
	@Autowired
    ComplianceDao complianceDaoImpl;
	
	@Autowired
	ComplianceBuilder complianceBuilder;
	
	@Autowired
	ComplianceMailConfig complianceMailConfig;
	
	@RequestMapping(value = "/addCompliance", method = RequestMethod.POST)
	public @ResponseBody Long addCompliance(@RequestBody ComplianceDTO complianceDTO) {
		return complianceServiceImpl.addCompliance(complianceDTO);
	}
	@RequestMapping(value = "/editCompliance", method = RequestMethod.PUT)
	public @ResponseBody void editCompliance(@RequestBody ComplianceDTO complianceDTO) {
		complianceServiceImpl.editCompliance(complianceDTO);
	}
	
	@RequestMapping(value = "/getComplainceTasks",params= {"fromIndex","toIndex"}, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getComplainceTasks(@RequestParam Integer fromIndex,@RequestParam Integer toIndex) {
		System.out.println("tasks");
		return complianceServiceImpl.getComplianceTasks(fromIndex, toIndex);
	}
	@RequestMapping(value = "/closeComplianceTask", method = RequestMethod.POST)
	public @ResponseBody void closeComplianceTask(@RequestBody ComplianceTaskSubmitDTO complianceTaskSubmitDTO) {
		complianceServiceImpl.closeComplianceTask(complianceTaskSubmitDTO);
	}
	@RequestMapping(value = "/isComplianceNameExist",params= {"complianceName","id"}, method = RequestMethod.GET)
	public @ResponseBody Boolean isComplianceNameExist(@RequestParam String complianceName,@RequestParam Long id) {
		return complianceServiceImpl.isComplianceNameExist(complianceName,id);
	}
	@RequestMapping(value = "/getComplainces",params= {"fromIndex","toIndex"}, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getComplainces(@RequestParam Integer fromIndex,@RequestParam Integer toIndex) {
		return complianceServiceImpl.getCompliances(fromIndex, toIndex);
	}
	
}
