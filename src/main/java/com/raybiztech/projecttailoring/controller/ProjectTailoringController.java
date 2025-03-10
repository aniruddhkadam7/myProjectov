package com.raybiztech.projecttailoring.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.raybiztech.projecttailoring.business.ProcessSubHeadData;
import com.raybiztech.projecttailoring.dto.ProcessAreaDTO;
import com.raybiztech.projecttailoring.dto.ProcessHeadDto;
import com.raybiztech.projecttailoring.dto.ProcessSubHeadDto;
import com.raybiztech.projecttailoring.dto.ProjectTailoringDTO;
import com.raybiztech.projecttailoring.exception.DuplicateException;
import com.raybiztech.projecttailoring.service.ProjectTailoringService;

@Controller("projectTailoringController")
@RequestMapping("/projectTailoring")
public class ProjectTailoringController {

	@Autowired
	ProjectTailoringService projectTailoringServiceImpl;

	Logger logger = Logger.getLogger(ProjectTailoringController.class);

	@RequestMapping(value = "/getProjectTailoringDocument", params = {"flag"},method = RequestMethod.GET)
	public @ResponseBody List<ProcessHeadDto> getProjectTailoringDocument(@RequestParam String flag) {
		return projectTailoringServiceImpl.getProjectTailoringDocument(flag);

	}
	
	@RequestMapping(value = "/saveProjectTailoringDocument", method = RequestMethod.POST)
	public @ResponseBody void saveProjectTailoringDocument(
			@RequestBody ProjectTailoringDTO projectTailoringDTO)
			throws IOException {
		projectTailoringServiceImpl
				.saveProjectTailoringDocument(projectTailoringDTO);
	}

	@RequestMapping(value = "/getProjectTailoring", params = { "projectId" }, method = RequestMethod.GET)
	public @ResponseBody ProjectTailoringDTO getProjectTailoring(
			@RequestParam Long projectId) {
		return projectTailoringServiceImpl.getProjectTailoring(projectId);
	}

	@RequestMapping(value = "/getProjectDetails", params = { "projectId" }, method = RequestMethod.GET)
	public @ResponseBody ProjectDTO getProjectDetails(
			@RequestParam Long projectId) {
		return projectTailoringServiceImpl.getProjectDetails(projectId);

	}
	
	@RequestMapping(value = "/saveProjectTailoringDocumentForManager", method = RequestMethod.POST)
	public @ResponseBody void saveProjectTailoringDocumentForManager(
			@RequestBody ProjectTailoringDTO projectTailoringDTO)
			throws IOException {
		projectTailoringServiceImpl
				.saveProjectTailoringDocumentForManager(projectTailoringDTO);
	}
	
	@RequestMapping(value="/saveProcessArea", method = RequestMethod.POST)
	public @ResponseBody void saveProcessSubHead(
			@RequestBody ProcessSubHeadDto processsubHeadDto,
			HttpServletResponse httpServletResponse) throws DuplicateException{
		try {
		projectTailoringServiceImpl.saveProcessSubHead(processsubHeadDto);
		}
		catch(DuplicateException e) {
			httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);
		}
		
	}
	
	@RequestMapping(value = "/incrementOrDecrementOrder",method = RequestMethod.PUT)
	public @ResponseBody void incrementOrDecrementOrder(@RequestBody ProcessSubHeadDto processsubHeadDto)throws IOException {
		projectTailoringServiceImpl.incrementOrDecrementOrder(processsubHeadDto);
		
	}
	
	@RequestMapping(value = "/createProcessArea",method = RequestMethod.POST)
	public @ResponseBody void createProcessArea(@RequestBody ProcessAreaDTO processAreaDto,
			HttpServletResponse httpServletResponse)throws DuplicateException{
		try {
		projectTailoringServiceImpl.createProcessArea(processAreaDto);
		}
		catch(DuplicateException e) {
			httpServletResponse.setStatus(HttpServletResponse.SC_CONFLICT);
		} 
	}
	
	@RequestMapping(value = "/getProcessAreas", params = {"categoryId"}, method = RequestMethod.GET)
	public @ResponseBody List<ProcessAreaDTO> getProcessAreas(Long categoryId){
		return projectTailoringServiceImpl.getProcessAreasgetProcessAreas(categoryId);
	}
	
	@RequestMapping(value = "/getProcessAreaDetails", params = {"processSubHeadId"}, method = RequestMethod.GET)
	public @ResponseBody ProcessSubHeadDto getProcessAreaDetails(Long processSubHeadId) {
		return projectTailoringServiceImpl.getProcessAreaDetails(processSubHeadId);
	}
	
	@RequestMapping(value = "/checkforDuplicateDoc", params = {"docName"}, method = RequestMethod.GET)
	public @ResponseBody Boolean checkforDuplicateDoc(String docName){
		return projectTailoringServiceImpl.checkforDuplicateDoc(docName);
	}
	
	@RequestMapping(value = "/checkDuplicateProcess", params = {"processName"}, method = RequestMethod.GET)
	public @ResponseBody Boolean checkDuplicateProcess(String processName) {
		return projectTailoringServiceImpl.checkDuplicateProcess(processName);
	}
	
	@RequestMapping(value = "/getOrderCountOfActiveProcesses", params = {"categoryId"}, method = RequestMethod.GET)
	public @ResponseBody Long getOrderCountOfActiveProcesses(Long categoryId) {
		return projectTailoringServiceImpl.getOrderCountOfActiveProcesses(categoryId);
	}
	
	/*@RequestMapping(value = "/checkForDuplicateOrder" ,params = {"processHeadId","order"},method = RequestMethod.GET)
	public @ResponseBody Boolean checkForDuplicateOrder(Long processHeadId, String order) {
		return projectTailoringServiceImpl.checkForDuplicateOrder(processHeadId,order);
	}*/
	
	
}
