package com.raybiztech.projecttailoring.service;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projectmanagement.dto.ProjectDTO;
import com.raybiztech.projecttailoring.builder.ProjectTailoringBuilder;
import com.raybiztech.projecttailoring.business.ProcessArea;
import com.raybiztech.projecttailoring.business.ProcessHead;
import com.raybiztech.projecttailoring.business.ProcessSubHead;
import com.raybiztech.projecttailoring.business.ProjectTailoring;
import com.raybiztech.projecttailoring.dao.ProjectTailoringDao;
import com.raybiztech.projecttailoring.dto.ProcessAreaDTO;
import com.raybiztech.projecttailoring.dto.ProcessHeadDto;
import com.raybiztech.projecttailoring.dto.ProcessSubHeadDto;
import com.raybiztech.projecttailoring.dto.ProjectTailoringDTO;
import com.raybiztech.projecttailoring.exception.DuplicateException;
import com.raybiztech.projecttailoring.mailNotification.ProjectTailoringMailNotification;

@Service("projectTailoringServiceImpl")
@Transactional
public class ProjectTailoringServiceImpl implements ProjectTailoringService {

	@Autowired
	DAO dao;

	@Autowired
	ProjectTailoringDao projectTailoringDaoImpl;

	@Autowired
	ProjectTailoringBuilder projectTailoringBuilder;

	@Autowired
	ProjectTailoringMailNotification projectTailoringMailNotification;

	Logger logger = Logger.getLogger(ProjectTailoringServiceImpl.class);

	@Override
	public List<ProcessHeadDto> getProjectTailoringDocument(String flag) {

		List<ProcessHead> processHeadList = dao.get(ProcessHead.class);

		return projectTailoringBuilder.toDto(processHeadList,flag);
	}

	// saving ProjectTailoring Document
	@Override
	public void saveProjectTailoringDocument(
			ProjectTailoringDTO projectTailoringDTO) throws IOException {

		ProjectTailoring projectTailoring = projectTailoringBuilder
				.connvertDtoToEntity(projectTailoringDTO);
		if (projectTailoringDTO.getId() == null) {
			dao.save(projectTailoring);
		} else {
			dao.update(projectTailoring);
		}
		if (projectTailoring.getTailoringStatus().equalsIgnoreCase("Submitted")
				|| projectTailoring.getTailoringStatus().equalsIgnoreCase(
						"Updated")) {
			projectTailoringMailNotification
					.sendProjectTailoringSubmissionMail(projectTailoring);
		} else if (projectTailoring.getTailoringStatus().equalsIgnoreCase(
				"Approved")) {
			projectTailoringMailNotification
					.sendProjectTailoringApprovalMail(projectTailoring.getId());
		} else if (projectTailoring.getTailoringStatus().equalsIgnoreCase(
				"Rejected")) {
			projectTailoringMailNotification
					.sendProjectTailoringRejectionMail(projectTailoring.getId());
		}
	}

	@Override
	public ProjectTailoringDTO getProjectTailoring(Long projectId) {

		return projectTailoringBuilder.projectToDto(projectTailoringDaoImpl
				.getProjectTailoring(projectId));

	}

	@Override
	public ProjectDTO getProjectDetails(Long projectId) {

		Project project = dao.findBy(Project.class, projectId);
		ProjectDTO dto = new ProjectDTO();
		dto.setProjectName(project.getProjectName());
		dto.setProjectCode(project.getProjectCode());
		dto.setClient(project.getClient().getName());
		dto.setType(project.getType().toString());

		return dto;
	}

	@Override
	public void saveProjectTailoringDocumentForManager(ProjectTailoringDTO projectTailoringDTO) {

		ProjectTailoring projectTailoring = projectTailoringBuilder
				.connvertDtoToEntity(projectTailoringDTO);
	
		if(projectTailoringDTO.getId()==null)
		{
			dao.save(projectTailoring);
			
		}
		else
		{
			dao.update(projectTailoring);
		}
		
		
	}

	@Override
	public void saveProcessSubHead(ProcessSubHeadDto processSubHeadDto) {
		
		//if processsubhead id is null then we are adding new
		if(processSubHeadDto.getId() == null) {
			//checking for duplicate document
			Boolean flag = checkforDuplicateDoc(processSubHeadDto.getDocumentName());
			//if document exists then exception
			if(flag == Boolean.TRUE) {
				throw new DuplicateException();
			}	
			else {
				
				ProcessSubHead processSubHeadEntity = projectTailoringBuilder.toSubHeadEntity(processSubHeadDto);
				dao.save(processSubHeadEntity);
			}
		}
		
		//if processsubhead id is not null then we are updating existing
		else {
			ProcessSubHead processSubHead = dao.findBy(ProcessSubHead.class, processSubHeadDto.getId());
			//if old document name is not equal to new document name 
				if(!processSubHead.getDocumentName().equalsIgnoreCase(processSubHeadDto.getDocumentName())) {
					// then checking for duplicate document
					Boolean flag = checkforDuplicateDoc(processSubHeadDto.getDocumentName());
					
					if(flag == Boolean.TRUE) {
						throw new DuplicateException();
					}	
					else {
						ProcessSubHead processSubHeadEntity = projectTailoringBuilder.toSubHeadEntity(processSubHeadDto);
						dao.update(processSubHeadEntity);
					}
			}
				//if old document name is equal to new document name then simply update
				else {
					ProcessSubHead processSubHeadEntity = projectTailoringBuilder.toSubHeadEntity(processSubHeadDto);
					dao.update(processSubHeadEntity);
				}
		}
		
			
	}

	/*@Override
	public Boolean checkForDuplicateOrder(Long processHeadId, String order) {
		
		return projectTailoringDaoImpl.checkForDuplicateOrder(processHeadId,order);
	}*/

	@Override
	public void incrementOrDecrementOrder(ProcessSubHeadDto processsubHeadDto) {
		Long oldOrder = null;
		
		//if processsubhead already exists then get the order of it
		if(processsubHeadDto.getProcessSubHeadId()!= null) {
		ProcessSubHead existingsubHead = dao.findBy(ProcessSubHead.class, processsubHeadDto.getProcessSubHeadId());
		
		oldOrder = existingsubHead.getOrder();
		}
		
		//getting list based on order
		List<ProcessSubHead> processSubHeadList = projectTailoringDaoImpl.getProcessSubHeadList(processsubHeadDto,oldOrder);
		
		
		ProcessSubHead subHead =  null;
		int i = 1;
		
		//if status is inactive get the list greater than equal to the old order 
		if(processsubHeadDto.getStatus().equals("false")) {
			
			for(ProcessSubHead processSubHead :processSubHeadList) {
				
				subHead = processSubHead;
				//for inactive processSubHead set order to null
				if(processSubHead.getId() == processsubHeadDto.getProcessSubHeadId()) {
					subHead.setOrder(null);
				}
				//for all the other list decrement the order
				else {
					
					subHead.setOrder(subHead.getOrder()-i);
					
				}
				dao.update(subHead);
			}
			
		}
		
		else if(oldOrder != null && processsubHeadDto.getOrder() > oldOrder) {
			
			for(ProcessSubHead processSubHead :processSubHeadList) {
				subHead = processSubHead;
				subHead.setOrder(subHead.getOrder()-i);
				dao.update(subHead);
				
			}
		}
			else {
				
				for(ProcessSubHead processSubHead :processSubHeadList) {
					if(processSubHead.getId() != processsubHeadDto.getProcessSubHeadId()) {
						subHead = processSubHead;
						subHead.setOrder(processsubHeadDto.getOrder()+i);
						dao.update(subHead);
						i++;
					}
				}
			}
	}

	@Override
	public void createProcessArea(ProcessAreaDTO processAreaDto) {
		Boolean flag = checkDuplicateProcess(processAreaDto.getName());
		if(flag == Boolean.TRUE) {
			throw new DuplicateException();
		}
		else {
		ProcessArea process = new ProcessArea();
		process = projectTailoringBuilder.toProcessEntity(processAreaDto);
		dao.save(process);
		}
	}

	@Override
	public List<ProcessAreaDTO> getProcessAreasgetProcessAreas(Long categoryId) {
		List<ProcessArea> process= projectTailoringDaoImpl.getProcessAreas(categoryId);
		List<ProcessAreaDTO> processDtos = projectTailoringBuilder.toProcessDtos(process);
		return processDtos;
	}

	@Override
	public ProcessSubHeadDto getProcessAreaDetails(Long processSubHeadId) {
		ProcessSubHeadDto dto = new ProcessSubHeadDto();
		ProcessSubHead processSubHead = dao.findBy(ProcessSubHead.class,processSubHeadId);
		dto.setCategoryId(processSubHead.getProcessHead().getId());
		ProcessArea process = dao.findByName(ProcessArea.class, processSubHead.getProcessSubHeadName());
		dto.setProcessAreaId(process.getId());
		dto.setProcessSubHeadId(processSubHead.getId());
		dto.setProcessSubHeadName(processSubHead.getProcessSubHeadName());
		dto.setProcessName(processSubHead.getProcessName());
		dto.setCommon(processSubHead.getCommon());
		dto.setDocumentName(processSubHead.getDocumentName());
		dto.setSpecificToProject("No");
		dto.setResponsible(processSubHead.getResponsible());
		dto.setLink(processSubHead.getLinks());
		dto.setStatus(processSubHead.getStatus().toString());
		dto.setOrder((processSubHead.getOrder()!=null)?processSubHead.getOrder():null);
		return dto;
	}

	@Override
	public Boolean checkforDuplicateDoc(String docName) {
		
		return projectTailoringDaoImpl.checkforDuplicateDoc(docName);
	}

	@Override
	public Boolean checkDuplicateProcess(String processName) {
		
		return projectTailoringDaoImpl.checkDuplicateProcess(processName);
	}

	@Override
	public Long getOrderCountOfActiveProcesses(Long categoryId) {
		
		return projectTailoringDaoImpl.getOrderCountOfActiveProcesses(categoryId);
	}
}
