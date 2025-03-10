package com.raybiztech.projecttailoring.service;

import java.io.IOException;
import java.util.List;

import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.raybiztech.projecttailoring.dto.ProcessAreaDTO;
import com.raybiztech.projecttailoring.dto.ProcessHeadDto;
import com.raybiztech.projecttailoring.dto.ProcessSubHeadDto;
import com.raybiztech.projecttailoring.dto.ProjectTailoringDTO;


public interface ProjectTailoringService {

	/* To get the view of Project Tailoring document list */
	List<ProcessHeadDto> getProjectTailoringDocument(String flag);

	void saveProjectTailoringDocument(ProjectTailoringDTO projectTailoringDTO)
			throws IOException;

	ProjectTailoringDTO getProjectTailoring(Long projectId);

	ProjectDTO getProjectDetails(Long projectId);

	void saveProjectTailoringDocumentForManager(ProjectTailoringDTO projectTailoringDTO);
	
	void saveProcessSubHead(ProcessSubHeadDto processsubHead);
	
	//Boolean checkForDuplicateOrder(Long processHeadId, String order);
	
	void incrementOrDecrementOrder(ProcessSubHeadDto processsubHeadDto);
	
	void createProcessArea(ProcessAreaDTO processAreaDto);
	
	List<ProcessAreaDTO> getProcessAreasgetProcessAreas(Long categoryId);
	
	ProcessSubHeadDto getProcessAreaDetails(Long processSubHeadId);
	
	Boolean checkforDuplicateDoc(String docName);
	
	Boolean checkDuplicateProcess(String processName);
	
	Long getOrderCountOfActiveProcesses(Long categoryId);
}
