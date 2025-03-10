package com.raybiztech.projecttailoring.builder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raybiztech.appraisals.business.Employee;
import com.raybiztech.appraisals.dao.DAO;
import com.raybiztech.appraisals.security.utils.SecurityUtils;
import com.raybiztech.date.Second;
import com.raybiztech.projectmanagement.business.Project;
import com.raybiztech.projecttailoring.business.ProcessArea;
import com.raybiztech.projecttailoring.business.ProcessHead;
import com.raybiztech.projecttailoring.business.ProcessHeadData;
import com.raybiztech.projecttailoring.business.ProcessSubHead;
import com.raybiztech.projecttailoring.business.ProcessSubHeadData;
import com.raybiztech.projecttailoring.business.ProjectTailoring;
import com.raybiztech.projecttailoring.dto.ProcessAreaDTO;
import com.raybiztech.projecttailoring.dto.ProcessHeadDto;
import com.raybiztech.projecttailoring.dto.ProcessSubHeadDto;
import com.raybiztech.projecttailoring.dto.ProjectTailoringDTO;

@Component("projectTailoringBuilder")
public class ProjectTailoringBuilder {

	Logger logger = Logger.getLogger(ProjectTailoringBuilder.class);

	@Autowired
	DAO dao;

	@Autowired
	SecurityUtils securityUtils;

	static int count = 0;
	static int waivedDocCount = 0;

	/* For getting master lookup data for project tailoring */
	public List<ProcessHeadDto> toDto(List<ProcessHead> processHeads,String flag) {

		List<ProcessHeadDto> tailoringList = new ArrayList<ProcessHeadDto>();

		Map<String, Object> processSubheadMap = new HashMap<String,Object>();
		if (processHeads != null) {
			for (ProcessHead head : processHeads) {
				ProcessHeadDto tailoringDtoList = new ProcessHeadDto();
				tailoringDtoList.setProcessHeadId(head.getId());
				tailoringDtoList.setProcessHeadname(head.getProcessHeadname());
				if(flag.equals("view")) {
				processSubheadMap= subHeadEntityToDto(head
						.getActiveProcessSubHeadList());
				}
				else {
				 processSubheadMap = subHeadEntityToDto(head
						.getProcessSubHeads());
				}
				tailoringDtoList.setProcessSubHeadsDto((Set<ProcessSubHeadDto>) processSubheadMap.get("list"));
				tailoringDtoList.setDocumentCount(String.valueOf(processSubheadMap.get("count")));
				tailoringDtoList.setProcessSubHeadCount(String.valueOf(processSubheadMap.get("count")));
				tailoringDtoList.setProcessCount(head.getProcessCount());
				tailoringList.add(tailoringDtoList);
			}
		}
		return tailoringList;
	}

	public Map<String, Object> subHeadEntityToDto(Set<ProcessSubHead> processSubHeads){
		
		Map<String, Object> map = new HashMap<String,Object>();
		Set<ProcessSubHeadDto> processSubHeadDtos = new HashSet<ProcessSubHeadDto>();
		ProcessSubHeadDto processSubHeadList = null;
		Integer processSubHeadCount = 0;
		
		for (ProcessSubHead processSubHead : processSubHeads) {
				processSubHeadCount++;
				processSubHeadList = new ProcessSubHeadDto();
				processSubHeadList.setCategoryId(processSubHead.getProcessHead().getId());
				processSubHeadList.setProcessSubHeadId(processSubHead.getId());
				processSubHeadList.setProcessSubHeadName(processSubHead
						.getProcessSubHeadName());
				processSubHeadList.setProcessName(processSubHead.getProcessName());
				processSubHeadList.setCommon(processSubHead.getCommon());
				processSubHeadList
						.setDocumentName(processSubHead.getDocumentName());
				processSubHeadList.setSpecificToProject("No");
				processSubHeadList.setResponsible(processSubHead.getResponsible());
				processSubHeadList.setLink(processSubHead.getLinks());
				processSubHeadList.setStatus(processSubHead.getStatus().toString());
				processSubHeadList.setOrder((processSubHead.getOrder()!=null)?
						processSubHead.getOrder():null);
				processSubHeadDtos.add(processSubHeadList);
			
				
			
		}
		
		map.put("list", processSubHeadDtos);
		map.put("count", processSubHeadCount);
		return map;
	}

	// convert projecttailoringdto to projecttailoring
	public ProjectTailoring connvertDtoToEntity(
			ProjectTailoringDTO projectTailoringDTO) {
		ProjectTailoring projectTailoring = null;
		if (projectTailoringDTO.getId() != null) {
			projectTailoring = dao.findBy(ProjectTailoring.class,
					projectTailoringDTO.getId());
		} else {
			projectTailoring = new ProjectTailoring();
		}
		projectTailoring.setProject(dao.findBy(Project.class,
				projectTailoringDTO.getProjectId()));
		Employee loggedInEmployee = (Employee) securityUtils
				.getLoggedEmployeeDetailsSecurityContextHolder()
				.get("employee");
		projectTailoring.setCreatedBy(loggedInEmployee);
		projectTailoring.setCreatedDate(new Second());
		projectTailoring.setTailoringStatus(projectTailoringDTO
				.getTailoringStatus());
		projectTailoring.setRejectComments(projectTailoringDTO
				.getRejectComments());
		projectTailoring
				.setProcessHeadData(convertProcessHeadDtoTProcessHeadData(projectTailoringDTO
						.getProcessHeaddto()));
		return projectTailoring;
	}

	private Set<ProcessHeadData> convertProcessHeadDtoTProcessHeadData(
			Set<ProcessHeadDto> processHeaddto) {
		Set<ProcessHeadData> headDatas = new HashSet<ProcessHeadData>();
		ProcessHeadData headData = null;
		for (ProcessHeadDto headDto : processHeaddto) {
			if (headDto.getId() != null) {
				headData = dao.findBy(ProcessHeadData.class, headDto.getId());
			} else {
				headData = new ProcessHeadData();
			}

			headData.setProcessHead(dao.findBy(ProcessHead.class,
					headDto.getProcessHeadId()));
			headData.setProcessSubHeadData(convertProcessSubHeaddtoToProcessSubHead(headDto
					.getProcessSubHeadsDto()));
			headData.setProcessSubHeadCount(headDto.getProcessSubHeadCount());
			headData.setProcessCount(headDto.getProcessCount());

			headDatas.add(headData);
		}

		return headDatas;
	}

	private Set<ProcessSubHeadData> convertProcessSubHeaddtoToProcessSubHead(
			Set<ProcessSubHeadDto> processSubHeadsDto) {
		ProcessSubHeadData processSubHeadData = null;
		Set<ProcessSubHeadData> processSubHeadDatas = new HashSet<ProcessSubHeadData>();
		for (ProcessSubHeadDto processSubHeadDto : processSubHeadsDto) {
			if (processSubHeadDto.getId() != null) {
				processSubHeadData = dao.findBy(ProcessSubHeadData.class,
						processSubHeadDto.getId());
			} else {
				processSubHeadData = new ProcessSubHeadData();
			}
			processSubHeadData.setComments(processSubHeadDto.getComments());
			processSubHeadData.setProcessSubHead(dao.findBy(
					ProcessSubHead.class,
					processSubHeadDto.getProcessSubHeadId()));
			processSubHeadData.setSpecificToProject(processSubHeadDto
					.getSpecificToProject());
			processSubHeadData.setSqaApproval(processSubHeadDto
					.getSqaApproval());
			processSubHeadData.setSqaComments(processSubHeadDto
					.getSqaComments());
			processSubHeadDatas.add(processSubHeadData);
		}
		return processSubHeadDatas;
	}

	public ProjectTailoringDTO projectToDto(ProjectTailoring projectTailoring) {

		ProjectTailoringDTO projectTailoringDTO = null;
		if (projectTailoring != null) {
			projectTailoringDTO = new ProjectTailoringDTO();
			projectTailoringDTO.setId(projectTailoring.getId());
			projectTailoringDTO.setProjectId(projectTailoring.getProject()
					.getId());
			projectTailoringDTO.setTailoringStatus(projectTailoring
					.getTailoringStatus());
			projectTailoringDTO.setRejectComments(projectTailoring
					.getRejectComments());
			projectTailoringDTO
					.setProcessHeaddto(processHeadToDto(projectTailoring
							.getProcessHeadData()));

		}

		return projectTailoringDTO;
	}

	public Set<ProcessHeadDto> processHeadToDto(
			Set<ProcessHeadData> processHeadDatas) {

		Set<ProcessHeadDto> processHeadDtos = new HashSet<ProcessHeadDto>();
		for (ProcessHeadData processHeadData : processHeadDatas) {
			count = 0;
			waivedDocCount = 0;
			ProcessHeadDto processHeadDto = new ProcessHeadDto();
			processHeadDto.setId(processHeadData.getId());
			processHeadDto.setProcessHeadId(processHeadData.getProcessHead()
					.getId());
			processHeadDto.setProcessHeadname(processHeadData.getProcessHead()
					.getProcessHeadname());
			processHeadDto
					.setProcessSubHeadsDto(processSubHeadToDto(processHeadData
							.getProcessSubHeadData()));
			processHeadDto.setTailoredCount(String.valueOf(count));
			processHeadDto.setWaivedCount(String.valueOf(waivedDocCount));
			processHeadDto.setDocumentCount(String.valueOf(processHeadData
					.getProcessSubHeadData().size()));
			processHeadDto.setProcessSubHeadCount(String.valueOf(processHeadData
					.getProcessSubHeadData().size()));
			processHeadDto.setProcessCount(processHeadData.getProcessCount());
			processHeadDtos.add(processHeadDto);
		}

		return processHeadDtos;
	}

	public Set<ProcessSubHeadDto> processSubHeadToDto(
			Set<ProcessSubHeadData> processSubHeadDatas) {

		Set<ProcessSubHeadDto> processSubHeadDtos = new HashSet<ProcessSubHeadDto>();
		for (ProcessSubHeadData processSubHeadData : processSubHeadDatas) {
			ProcessSubHeadDto processSubHeadDto = new ProcessSubHeadDto();
			processSubHeadDto.setId(processSubHeadData.getId());
			processSubHeadDto.setProcessSubHeadId(processSubHeadData
					.getProcessSubHead().getId());
			processSubHeadDto.setProcessSubHeadName(processSubHeadData
					.getProcessSubHead().getProcessSubHeadName());
			processSubHeadDto.setProcessName(processSubHeadData
					.getProcessSubHead().getProcessName());
			processSubHeadDto.setDocumentName(processSubHeadData
					.getProcessSubHead().getDocumentName());
			processSubHeadDto.setResponsible(processSubHeadData
					.getProcessSubHead().getResponsible());
			processSubHeadDto.setSpecificToProject(processSubHeadData
					.getSpecificToProject());
			if (processSubHeadData.getSpecificToProject().equalsIgnoreCase(
					"Yes")) {
				count++;
			}
			if (processSubHeadData.getSpecificToProject().equalsIgnoreCase(
					"Waived off")) {
				
				waivedDocCount++;
			}
			if (processSubHeadData.getComments() != null) {
				processSubHeadDto.setComments(processSubHeadData.getComments());
			} else {
				processSubHeadDto.setComments("");
			}

			if (processSubHeadData.getSqaApproval() != null) {
				processSubHeadDto.setSqaApproval(processSubHeadData
						.getSqaApproval());
			} else {
				processSubHeadDto.setSqaApproval("Approved");
			}
			if (processSubHeadData.getSqaComments() != null) {
				processSubHeadDto.setSqaComments(processSubHeadData
						.getSqaComments());
			} else {
				processSubHeadDto.setSqaComments("");
			}
			processSubHeadDto.setLink(processSubHeadData.getProcessSubHead().getLinks());
			processSubHeadDto.setOrder(processSubHeadData.getProcessSubHead().getOrder()!=null?
				processSubHeadData.getProcessSubHead().getOrder():null);
			processSubHeadDtos.add(processSubHeadDto);
		}

		return processSubHeadDtos;
	}
	
	public ProcessSubHead toSubHeadEntity(ProcessSubHeadDto subHeadDto) {
		ProcessSubHead subHeadEntity = null;
		if(subHeadDto.getId()!=null) {
			subHeadEntity = dao.findBy(ProcessSubHead.class, subHeadDto.getId());
		}
		else {
			subHeadEntity = new ProcessSubHead();
		}
		ProcessHead head = dao.findBy(ProcessHead.class, subHeadDto.getCategoryId());
		subHeadEntity.setProcessHead(head);
		ProcessArea process = dao.findBy(ProcessArea.class, subHeadDto.getProcessAreaId());
		subHeadEntity.setProcessSubHeadName(process.getName());
		subHeadEntity.setDocumentName(subHeadDto.getDocumentName());
		subHeadEntity.setResponsible(subHeadDto.getResponsible());
		subHeadEntity.setLinks(subHeadDto.getLink());
		subHeadEntity.setStatus((subHeadDto.getStatus().equalsIgnoreCase("true"))?Boolean.TRUE:Boolean.FALSE);
		subHeadEntity.setOrder((subHeadDto.getOrder() != null && !subHeadDto.getOrder().equals("") && !subHeadDto.getStatus().equalsIgnoreCase("false"))?
				Long.valueOf(subHeadDto.getOrder()):null);
		
		return subHeadEntity;
		
	}
	
	public ProcessAreaDTO toProcessDto(ProcessArea process) {
		ProcessAreaDTO processDto = new ProcessAreaDTO();
		processDto.setId(process.getId());
		processDto.setName(process.getName());
		processDto.setCategoryId(process.getProcessHead().getId());
		return processDto;
		
	}
	
	public ProcessArea toProcessEntity(ProcessAreaDTO dto) {
		ProcessArea process = new ProcessArea();
		process.setName(dto.getName());
		process.setProcessHead(dao.findBy(ProcessHead.class, dto.getCategoryId()));		
		return process;
		
	}
	
	
	
	public List<ProcessAreaDTO> toProcessDtos(List<ProcessArea> process){
		List<ProcessAreaDTO> Dtos = new ArrayList<>();
		ProcessAreaDTO processDto = new ProcessAreaDTO();
		for(ProcessArea pro : process) {
			processDto = toProcessDto(pro);
			Dtos.add(processDto);
		}
		return Dtos;
		
	}
	
	
}
