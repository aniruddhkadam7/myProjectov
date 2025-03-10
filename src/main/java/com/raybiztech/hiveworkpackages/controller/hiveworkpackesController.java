package com.raybiztech.hiveworkpackages.controller;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.itextpdf.text.pdf.PdfStructTreeController.returnType;
import com.raybiztech.hiveworkpackages.Exception.HiveWorkPackageEception;
import com.raybiztech.hiveworkpackages.Exception.InvalidUserException;
import com.raybiztech.hiveworkpackages.business.Enumerations;
import com.raybiztech.hiveworkpackages.business.statuses;
import com.raybiztech.hiveworkpackages.business.CustomOptions;
import com.raybiztech.hiveworkpackages.business.types;
import com.raybiztech.hiveworkpackages.business.versions;
import com.raybiztech.hiveworkpackages.dto.ActivityDTO;
import com.raybiztech.hiveworkpackages.dto.spentTimeDTO;
import com.raybiztech.hiveworkpackages.dto.versionsDto;
import com.raybiztech.hiveworkpackages.dto.work_packagesDto;
import com.raybiztech.hiveworkpackages.service.hiveworkpackagesService;
import com.raybiztech.projectmanagement.business.AllocationDetails;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.dao.AllocationDetailsDTO;
import com.raybiztech.projectmanagement.dto.ProjectDTO;

@Controller
@RequestMapping("/hiveworkpackagesController")
public class hiveworkpackesController {

	@Autowired
	hiveworkpackagesService hiveworkpackageServiceImpl;

	@RequestMapping(value = "/getHiveTasks", method = RequestMethod.GET)
	public @ResponseBody Map<String, Object> getHiveTasks() {
		return hiveworkpackageServiceImpl.getHiveTasks();
	}

	@RequestMapping(value = "/getTaskTypes", method = RequestMethod.GET)
	public @ResponseBody List<types> getTaskTypes() {
		return hiveworkpackageServiceImpl.getTaskTypes();
	}

	@RequestMapping(value = "/getEnumerations", method = RequestMethod.GET)
	public @ResponseBody List<Enumerations> getPriorityList() {
		return hiveworkpackageServiceImpl.getPriorityList();
	}

	@RequestMapping(value = "logTime", method = RequestMethod.POST)
	public @ResponseBody void logTime(@RequestBody spentTimeDTO spentTimeDTO)
			throws ParseException {
		hiveworkpackageServiceImpl.logTime(spentTimeDTO);

	}

	@RequestMapping(value = "getactivitytypes", method = RequestMethod.GET)
	public @ResponseBody List<ActivityDTO> getactivitytypes() {
		return hiveworkpackageServiceImpl.getactivitytypes();
	}

	@RequestMapping(value = "/createWorkPackage", method = RequestMethod.POST)
	public @ResponseBody void createNewWorkPackage(
			@RequestBody work_packagesDto workPackageDto,
			HttpServletResponse httpServletResponse) throws ParseException {
		try {
			hiveworkpackageServiceImpl.createNewWorkPackage(workPackageDto);
		} catch (InvalidUserException e) {
			httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

	}

	@RequestMapping(value = "/getHiveTaskClients", params = { "startIndex",
			"endIndex" }, method = RequestMethod.GET)
	public @ResponseBody List<String> getHiveTaskClients(@RequestParam Integer startIndex, @RequestParam Integer endIndex) {
		return hiveworkpackageServiceImpl.getHiveTasksClients(startIndex, endIndex);
	}

	@RequestMapping(value = "/getSprintTasks", params = { "id" }, method = RequestMethod.GET)
	public @ResponseBody List<work_packagesDto> getSprintTasksList(Long id) {
		return hiveworkpackageServiceImpl.getSprintTasksList(id);
	}

	@RequestMapping(value = "/projectTasksList", params = { "hiveProjectName" }, method = RequestMethod.GET)
	public @ResponseBody List<work_packagesDto> getProjecttasksList(
			String hiveProjectName) {
		return hiveworkpackageServiceImpl.getProjecttasksList(hiveProjectName);
	}

	@RequestMapping(value = "/hivetaskProjectList", params = { "client" }, method = RequestMethod.GET)
	public @ResponseBody List<ProjectDTO> getHiveProjectsList(String client) {
		return hiveworkpackageServiceImpl.getHiveProjectsList(client);
	}

	@RequestMapping(value = "/getCustomValues", method = RequestMethod.GET)
	public @ResponseBody List<CustomOptions> getCustomValues() {
		return hiveworkpackageServiceImpl.getCustomValues();
	}

	@RequestMapping(value = "/getWorkapackageData", params = { "workpackageId" }, method = RequestMethod.GET)
	public @ResponseBody work_packagesDto getWorkapackageData(
			@RequestParam String workpackageId, HttpServletResponse response)
			throws ParseException {
		work_packagesDto work_packagesDto = null;
		try {
			work_packagesDto = new work_packagesDto();
			work_packagesDto = hiveworkpackageServiceImpl
					.getWorkapackageData(workpackageId);
		} catch (HiveWorkPackageEception hiveWorkPackageEception) {
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
		{

		}
		return work_packagesDto;

	}

	@RequestMapping(value = "/getVersionsList", params = { "projectName" }, method = RequestMethod.GET)
	public @ResponseBody List<versionsDto> getVersionsList(String projectName) {
		return hiveworkpackageServiceImpl.getVersionsList(projectName);

	}

	@RequestMapping(value = "/getProjectsAssignedToEmployee", method = RequestMethod.GET)
	public @ResponseBody List<ProjectDTO> getProjectsAssignedToEmployee() {
		return hiveworkpackageServiceImpl.getProjectsAssignedToEmployee();
	}

	@RequestMapping(value = "/getworkPkgStatuses", method = RequestMethod.GET)
	public @ResponseBody List<statuses> getworkPkgStatuses() {
		return hiveworkpackageServiceImpl.getworkPkgStatuses();
	}

	@RequestMapping(value = "/editWorkPackage", method = RequestMethod.POST)
	public @ResponseBody void updateWorkPackage(
			@RequestBody work_packagesDto workPackageDto,
			HttpServletResponse httpServletResponse) throws ParseException {
		try {
			hiveworkpackageServiceImpl.updateWorkPackage(workPackageDto);
		} catch (InvalidUserException e) {
			httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
		}

	}
}
