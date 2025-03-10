package com.raybiztech.checklist.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.appraisals.business.EmpDepartment;
import com.raybiztech.checklist.dto.ChecklistConfigurationDto;
import com.raybiztech.checklist.dto.ChecklistDTO;
import com.raybiztech.checklist.dto.ChecklistSectionDTO;
import com.raybiztech.checklist.dto.DepartmentSectionDto;
import com.raybiztech.checklist.dto.DepartmentChecklistDTO;
import com.raybiztech.checklist.service.ChecklistService;
import com.raybiztech.checklist.service.ChecklistServiceImpl;
import com.raybiztech.projectmanagement.business.ProjectCheckList;

@Controller
@RequestMapping("/checklist")
public class ChecklistController {

	@Autowired
	ChecklistService checklistService;

	// getting all departments
	@RequestMapping(value = "/getDepartments", method = RequestMethod.GET)
	public @ResponseBody List<EmpDepartment> getDepartments() {
		return checklistService.getDeparments();
	}

	// Getting sections department wise
	@RequestMapping(value = "/getSections", params = { "deptId" }, method = RequestMethod.GET)
	public @ResponseBody List<ChecklistSectionDTO> getSections(
			@RequestParam Long deptId) {
		return checklistService.getSections(deptId);
	}

	// Adding checklist item
	@RequestMapping(value = "/saveChecklistItem", method = RequestMethod.POST)
	@ResponseBody
	public void saveChecklistItem(
			@RequestBody ChecklistConfigurationDto configurationDto) {
		checklistService.saveChecklistItem(configurationDto);

	}

	// Getting checklist items
	@RequestMapping(value = "/getChecklistItems", params = "departmentId", method = RequestMethod.GET)
	@ResponseBody
	List<ChecklistConfigurationDto> getChecklistItems(
			@RequestParam Long departmentId) {

		return checklistService.getChecklistItems(departmentId);

	}

	// adding section
	@RequestMapping(value = "/saveSection", method = RequestMethod.POST)
	public @ResponseBody void saveSection(
			@RequestBody ChecklistSectionDTO checklistSectionDTO) {
		checklistService.saveSection(checklistSectionDTO);
	}

	// editing section
	@RequestMapping(value = "/editSection", method = RequestMethod.PUT)
	public @ResponseBody void editSection(
			@RequestBody ChecklistSectionDTO checklistSectionDTO) {
		checklistService.editSection(checklistSectionDTO);
	}

	// Dupilcate section checking
	@RequestMapping(value = "/isSectionExist", params = { "sectionName",
			"departmentId" }, method = RequestMethod.GET)
	public @ResponseBody Boolean isSectionExist(
			@RequestParam String sectionName, @RequestParam Long departmentId) {
		return checklistService.isSectionExist(sectionName, departmentId);
	}

	// editing checklist item
	@RequestMapping(value = "/editChecklistSection", method = RequestMethod.PUT)
	public @ResponseBody void editChecklistSection(
			@RequestBody ChecklistConfigurationDto configurationDto) {
		checklistService.editChecklistSection(configurationDto);
	}

	// checking for checklist item name already exist or not
	@RequestMapping(value = "/isAlreadyExist", params = { "name",
			"departmentId", "sectionId" }, method = RequestMethod.GET)
	public @ResponseBody Boolean isAlreadyExist(@RequestParam String name,
			@RequestParam Long departmentId, @RequestParam Long sectionId) {

		return checklistService.isAlreadyExist(name, departmentId, sectionId);

	}

	// getting sections based on logged - in employee department
	@RequestMapping(value = "/getSectionsByDeptId", method = RequestMethod.GET)
	public @ResponseBody List<ChecklistSectionDTO> getSectionsByDeptId() {
		return checklistService.getSectionsByDeptId();
	}

	/*
	 * //getting checklist based on logged- in employee department
	 * 
	 * @RequestMapping(value="/getChecklistBySecId" , params =
	 * {"sectionsId"},method = RequestMethod.GET) public @ResponseBody
	 * List<ChecklistDTO> getChecklistBySecId(@RequestParam Long sectionsId){
	 * 
	 * 
	 * return checklistService.getChecklistBySecId(sectionsId);
	 * 
	 * }
	 */

	// getting checklist based on logged - in employee department
	@RequestMapping(value = "/getChecklistBySecId", params = { "sectionsId" }, method = RequestMethod.GET)
	public @ResponseBody List<ChecklistDTO> getChecklistBySecId(
			@RequestParam Long sectionsId) {

		return checklistService.getChecklistBySecId(sectionsId);

	}

	// checking and uploading checklist items to db whether working or not
	@RequestMapping(value = "/addComments", method = RequestMethod.POST)
	public @ResponseBody void addComments(
			@RequestBody DepartmentSectionDto departmentSectionDto) {
		checklistService.addComments(departmentSectionDto);

	}

	// getting department wise sections and checklist

	@RequestMapping(value = "/getDepartmentSections", params = {
			"departmentId", "startIndex", "endIndex", "dateSelection", "from",
			"to", "multiSearch" }, method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getDepartmentSections(
			@RequestParam Long departmentId, @RequestParam Integer startIndex,
			@RequestParam Integer endIndex, @RequestParam String dateSelection,
			@RequestParam String from, @RequestParam String to,
			@RequestParam String multiSearch) {

		return checklistService.getDepartmentSections(departmentId, startIndex,
				endIndex, dateSelection, from, to, multiSearch);

	}
}
